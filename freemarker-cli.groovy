#!/usr/bin/env groovy

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Grapes([
        @Grab(group = "org.apache.poi", module = "poi", version = "3.15"),
        @Grab(group = "org.apache.poi", module = "poi-ooxml", version = "3.15"),
        @Grab(group = "org.apache.poi", module = "poi-ooxml-schemas", version = "3.15"),
        @Grab(group = "com.jayway.jsonpath", module = "json-path", version = "2.2.0"),
        @Grab(group = "org.slf4j", module = "slf4j-api", version = "1.7.21"),
        @Grab(group = "org.slf4j", module = "slf4j-log4j12", version = "1.7.21"),
        @Grab(group = "org.freemarker", module = "freemarker", version = "2.3.25-incubating"),
        @Grab(group = "org.apache.commons", module = "commons-csv", version = "1.4")])

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import freemarker.ext.beans.BeansWrapper
import freemarker.ext.beans.BeansWrapperBuilder
import freemarker.ext.dom.NodeModel
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler
import freemarker.template.utility.ObjectConstructor
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.poi.ss.usermodel.*
import org.xml.sax.InputSource

import java.text.SimpleDateFormat

static void main(String[] args) {
    final CommandLine cli = new CommandLine(args)
    new Task("freemarker-cli", cli).run()
}

class Task {
    final String name
    final CommandLine cli
    static boolean verbose

    Task(String name, CommandLine cli) {
        verbose = cli.verbose
        this.name = name
        this.cli = cli
        Locale.setDefault(cli.locale)
    }

    void run() {
        final Writer writer
        final File outputFile = cli.hasOutputFile() ? new File(cli.outputFile) : null
        final long startTime = System.currentTimeMillis()
        final List<Document> documents = readDocuments(cli)
        final Configuration configuration = createFreeMarkerConfiguration(cli.baseDir)
        final Map<String, Object> dataModel = createDataModel(documents, cli.description)
        final Template template = configuration.getTemplate(cli.templateName)

        try {
            writer = createFileWriter(cli.outputFile)
            template.process(dataModel, writer)
            final long durationInMs = System.currentTimeMillis() - startTime
            log("Template processing finished in ${durationInMs} ms")

            if (outputFile != null && outputFile.exists()) {
                log("The ouptut file has ${outputFile.length()} bytes")
            }
        }
        finally {
            if (writer != null) {
                writer.close()
            }
        }
    }

    private static List<Document> readDocuments(CommandLine cli) {
        final List<Document> documents = []

        if (cli.stdin) {
            log("Reading from System.stdin")
            documents.add(new Document("stdin", null, cli.stdin))
        }

        cli.sourceFiles.eachWithIndex { sourceFile, index ->
            final File file = new File(sourceFile)
            final String content = file.getText("UTF-8")
            final Document document = new Document(file.getName(), file, content)
            log("document[${index}]: ${file.getAbsolutePath()}")
            documents.add(document)
        }

        return documents
    }

    private static Configuration createFreeMarkerConfiguration(File baseDir) {
        final Configuration configuration = new Configuration(Configuration.VERSION_2_3_25)
        configuration.setDirectoryForTemplateLoading(baseDir)
        configuration.setDefaultEncoding("UTF-8")
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER)
        configuration.setLogTemplateExceptions(false)
        return configuration
    }

    private static Map<String, Object> createDataModel(List<Document> documents, String description) {
        final Map<String, Object> dataModel = new HashMap<String, Object>()
        dataModel.put("documents", documents)
        dataModel.putAll(createCommonsCsvDataModel())
        dataModel.putAll(createJsonPathDataModel())
        dataModel.putAll(createXmlParserDataModel())
        dataModel.putAll(createExcelParserDataModel())
        dataModel.putAll(createFreeMarkerDataModel())
        dataModel.putAll(createSystemPropertiesDataModel())
        dataModel.putAll(createEnvironmentDataModel())
        dataModel.putAll(createReportDataModel(description))
        return dataModel
    }

    private static Map<String, Object> createCommonsCsvDataModel() {
        final Map<String, CSVFormat> csvFormats = new HashMap<String, CSVFormat>()
        csvFormats.put("DEFAULT", CSVFormat.DEFAULT)
        csvFormats.put("EXCEL", CSVFormat.EXCEL)
        csvFormats.put("MYSQL", CSVFormat.MYSQL)
        csvFormats.put("RFC4180", CSVFormat.RFC4180)
        csvFormats.put("TDF", CSVFormat.TDF)

        Map<String, Object> dataModel = new HashMap<String, Object>()
        dataModel.put("CSVFormat", csvFormats)
        dataModel.put("CSVParser", new CSVParserBean())
        return dataModel
    }

    private static Map<String, Object> createSystemPropertiesDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>()
        dataModel.put("SystemProperties", System.getProperties())
        return dataModel
    }

    private static Map<String, Object> createEnvironmentDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>()
        dataModel.put("Environment", System.getenv())
        return dataModel
    }

    private static Map<String, Object> createJsonPathDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>()
        dataModel.put("JsonPath", new JsonPathBean())
        return dataModel
    }

    private static Map<String, Object> createXmlParserDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>()
        dataModel.put("XmlParser", new XmlParserBean())
        return dataModel
    }

    private static Map<String, Object> createExcelParserDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>()
        dataModel.put("ExcelParser", new ExcelParserBean())
        return dataModel
    }

    private static Map<String, Object> createFreeMarkerDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>()
        final BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_25)
        final BeansWrapper beansWrapper = builder.build()
        dataModel.put("ObjectConstructor", new ObjectConstructor())
        dataModel.put("Statics", beansWrapper.getStaticModels())
        dataModel.put("Enums", beansWrapper.getEnumModels())
        return dataModel
    }

    private static Map<String, Object> createReportDataModel(String description) {
        final Properties properties = new Properties()
        properties.put("host", getHostName())
        properties.put("description", description)
        properties.put("user", System.getProperty("user.name", "unknown"))
        properties.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        final Map<String, Object> dataModel = new HashMap<String, Object>()
        dataModel.put("ReportData", properties)
        return dataModel
    }

    private static Writer createFileWriter(String outputFileName) {
        if (outputFileName == null || outputFileName.isEmpty()) {
            new BufferedWriter(new OutputStreamWriter(System.out))
        } else {
            final File outputFile = new File(outputFileName)
            if (outputFile.exists()) {
                log("Deleting the exiting output file: ${outputFile.getAbsolutePath()}")
                outputFile.delete()
                assert !outputFile.exists()
            }
            new BufferedWriter(new FileWriter(outputFile))
        }
    }

    private static String getHostName() {
        try {
            InetAddress.getLocalHost().getHostName()
        }
        catch (UnknownHostException ignored) {
            "localhost"
        }
    }

    private static void log(String message) {
        if (verbose) {
            System.err.println("[freemarker-cli] " + message)
        }
    }
}

// ==========================================================================
// Objects exposed in FreeMarker templates
// ==========================================================================

class Document {
    String name
    File file
    String content
    long length

    Document(String name, File file, String content) {
        this.name = name
        this.file = file
        this.content = content != null ? content : ""
        this.length = content != null ? content.length() : -1
    }

    public boolean hasFile() {
        return file != null
    }
}

class CSVParserBean {
    public CSVParser parse(String string, CSVFormat format) {
        return CSVParser.parse(string, format)

    }
}

class JsonPathBean {
    public DocumentContext parse(String string) {
        return JsonPath.parse(string)

    }
}

class XmlParserBean {
    public NodeModel parse(String string) {
        final InputSource inputSource = new InputSource(new StringReader(string))
        return NodeModel.parse(inputSource)
    }
}

class ExcelParserBean {
    public Workbook parseFile(File sourceFile) {
        return WorkbookFactory.create(sourceFile)
    }

    public List<List<Object>> parseSheet(Sheet sheet) {
        DataFormatter formatter = new DataFormatter()
        Iterator<Row> iterator = sheet.iterator()
        List<List<Object>> result = new ArrayList<>()

        while (iterator.hasNext()) {
            Row nextRow = iterator.next()
            List<Object> currRowValues = new ArrayList<>()
            Iterator<Cell> cellIterator = nextRow.cellIterator()
            result.add(currRowValues)
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next()
                currRowValues.add(formatter.formatCellValue(cell))
            }
        }

        return result
    }
}

// ==========================================================================
// CLI
// ==========================================================================

class CommandLine {
    File baseDir
    String description = ""
    Locale locale = Locale.getDefault()
    String templateName = ""
    boolean verbose = false
    String outputFile
    List<String> sourceFiles = []
    String stdin

    CommandLine(String[] args) {
        def cli = new CliBuilder(usage: 'groovy freemarker-cli.groovy [options] file[s]', stopAtNonOption: false)
        cli.h(longOpt: 'help', 'Usage information', required: false)
        cli.v(longOpt: 'verbose', 'Verbose mode', required: false)
        cli.b(longOpt: 'basedir', 'Base directory to resolve FreeMarker templates', required: false, args: 1)
        cli.d(longOpt: 'description', 'Custom report description', required: false, args: 1)
        cli.o(longOpt: 'output', 'Generated output file', required: false, args: 1)
        cli.t(longOpt: 'template', 'Template name', required: true, args: 1)
        cli.l(longOpt: 'locale', 'Locale value', required: false, args: 1)

        OptionAccessor opt = cli.parse(args)

        if (!opt) {
            System.exit(1)
        }

        if (opt.v) {
            this.verbose = true
        }

        if (opt.h) {
            cli.usage()
            System.exit(0)
        }

        if (opt.d) {
            this.description = opt.d
        }

        if (opt.t) {
            this.templateName = opt.t
        }

        if (opt.b) {
            this.baseDir = new File((String) opt.b)
        } else {
            this.baseDir = getScriptDirectory()
        }

        if (opt.o) {
            this.outputFile = opt.o
        }

        if (opt.l) {
            this.locale = createLocale((String) opt.l)
        }

        if (!opt.arguments().isEmpty()) {
            this.sourceFiles = opt.arguments()
        } else {
            StringBuffer buffer = new StringBuffer()
            System.in.eachLine { line -> buffer.append(line) }
            stdin = buffer.toString()
        }
    }

    boolean hasOutputFile() {
        return this.outputFile != null && !this.outputFile.isEmpty()
    }

    private static Locale createLocale(String value) {
        final String[] parts = value.split("_")
        return parts.size() == 1 ? new Locale(parts[0]) : new Locale(parts[0], parts[1])
    }

    private File getScriptDirectory() {
        new File(getClass().protectionDomain.codeSource.location.path).getParentFile()
    }

}
