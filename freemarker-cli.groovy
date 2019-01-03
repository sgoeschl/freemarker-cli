#!/usr/bin/env groovy
@Grapes([
        @Grab(group = "commons-cli", module = "commons-cli", version = "1.4"),
        @Grab(group = "org.apache.poi", module = "poi", version = "4.0.1"),
        @Grab(group = "org.apache.poi", module = "poi-ooxml", version = "4.0.1"),
        @Grab(group = "org.apache.poi", module = "poi-ooxml-schemas", version = "4.0.1"),
        @Grab(group = "com.jayway.jsonpath", module = "json-path", version = "2.4.0"),
        @Grab(group = "org.slf4j", module = "slf4j-api", version = "1.7.21"),
        @Grab(group = "org.slf4j", module = "slf4j-log4j12", version = "1.7.21"),
        @Grab(group = "org.freemarker", module = "freemarker", version = "2.3.28"),
        @Grab(group = "org.apache.commons", module = "commons-csv", version = "1.6")])

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

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import freemarker.ext.beans.BeansWrapper
import freemarker.ext.beans.BeansWrapperBuilder
import freemarker.ext.dom.NodeModel
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler
import freemarker.template.utility.ObjectConstructor
import groovy.cli.commons.CliBuilder
import groovy.cli.commons.OptionAccessor
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.poi.ss.usermodel.*
import org.xml.sax.InputSource

import java.text.SimpleDateFormat

import static java.nio.charset.StandardCharsets.ISO_8859_1
import static java.nio.charset.StandardCharsets.UTF_8

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
        final File outputFile = cli.hasOutputFile() ? new File(cli.outputFile) : null
        final long startTime = System.currentTimeMillis()
        final List<Document> documents = readDocuments(cli)
        final Configuration configuration = createFreeMarkerConfiguration(cli.baseDir)
        final Map<String, Object> dataModel = createDataModel(documents, cli.description)
        final Template template = configuration.getTemplate(cli.templateName)

        createFileWriter(cli.outputFile).withCloseable { writer ->
            template.process(dataModel, writer)
            final long durationInMs = System.currentTimeMillis() - startTime
            log("Template processing finished in ${durationInMs} ms")

            if (outputFile != null && outputFile.exists()) {
                log("The ouptut file has ${outputFile.length()} bytes")
            }
        }
    }

    private static List<Document> readDocuments(CommandLine cli) {
        final List<Document> documents = []

        if (cli.stdin) {
            log("Reading from System.stdin")
            documents.add(new Document("stdin", cli.stdin))
        }

        cli.sourceFiles.eachWithIndex { fileName, index ->
            final File file = new File(fileName)
            final String name = file.getName()
            final String encoding = getEncoding(file, cli.encoding)
            final Document document = new Document(name, file, encoding)
            log("document[${index}]: ${file.getAbsolutePath()}")
            documents.add(document)
        }

        return documents
    }

    private static String getEncoding(File file, String encoding) {
        final String extension = IOUtils.getFileExtension(file.name)
        switch (extension) {
            case "json": return UTF_8.name()
            case "properties": return ISO_8859_1.name()
            default: return encoding
        }
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
        dataModel.put("Documents", new Documents(documents))
        dataModel.putAll(createCommonsCsvDataModel())
        dataModel.putAll(createJsonPathDataModel())
        dataModel.putAll(createXmlParserDataModel())
        dataModel.putAll(createExcelParserDataModel())
        dataModel.putAll(createFreeMarkerDataModel())
        dataModel.putAll(createSystemPropertiesDataModel())
        dataModel.putAll(createEnvironmentDataModel())
        dataModel.putAll(createReportDataModel(description))
        dataModel.putAll(createPropertiesParserDataModel())
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

    private static Map<String, Object> createPropertiesParserDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>()
        dataModel.put("PropertiesParser", new PropertiesParserBean())
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
        final BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_28)
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
// Helper methods
// ==========================================================================

class IOUtils {

    static getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".")
        if (lastIndexOf == -1) {
            return ""
        }
        return name.substring(lastIndexOf+1).toLowerCase()
    }
}

// ==========================================================================
// Objects exposed in FreeMarker templates
// ==========================================================================

class Document {
    private String name
    private long length
    private String encoding
    private File file
    private String content

    Document(String name, String content) {
        this.name = name
        this.content = content != null ? content : ""
        this.length = content != null ? content.length() : 0
        this.encoding = System.getProperty("file.encoding")
    }

    Document(String name, File file, String encoding) {
        this.name = name
        this.file = file
        this.length = file.length()
        this.encoding = encoding
    }

    String getName() {
        return name
    }

    long getLength() {
        return length
    }

    String getEncoding() {
        return encoding
    }

    String getText() {
        if (isFile()) {
            return getInputStream().getText(encoding)
        } else {
            return content
        }
    }

    String getLocation() {
        if (isFile()) {
            return file.getAbsolutePath()
        } else {
            return "stdin"
        }
    }

    private InputStream getInputStream() {
        return isFile() ? new FileInputStream(file) : new ByteArrayInputStream(content.getBytes(encoding))
    }

    private boolean isFile() {
        return file != null
    }

    @Override
    public String toString() {
        return "Document{" +
                "name='" + name + '\'' +
                ", location='" + getLocation() + '\'' +
                '}';
    }
}

class Documents {
    private List<Document> documents;

    Documents(List<Document> documents) {
        this.documents = documents
    }

    List<String> getNames() {
        documents.stream().map { it.getName }.toList()
    }

    int size() {
        return documents.size
    }

    List<Document> getAll() {
        return documents
    }

    Document get(int index) {
        return documents.get(index);
    }

    Document get(String name) {
        return documents.find() { (it.name == value) }
    }

    List<Document> findByName(String value) {
        return documents.findAll() { it.name.contains(value) }
    }

    List<Document> findByExtension(String extension) {
        return documents.findAll() { IOUtils.getFileExtension(it.name) == extension }
    }

}

class CSVParserBean {
    CSVParser parse(Document document, CSVFormat format) {
        // The input stream would be closed by CSVParser#close but it
        // is unlikely to be called so we load the file into a String.
        final String text = document.getText()
        return CSVParser.parse(text, format)
    }
}

class JsonPathBean {
    DocumentContext parse(Document document) {
        document.getInputStream().withCloseable {
            return JsonPath.parse(it)
        }
    }
}

class PropertiesParserBean {
    Properties parse(Document document) {
        document.getInputStream().withCloseable {
            final Properties properties = new Properties()
            properties.load(it)
            return properties
        }
    }
}

class XmlParserBean {
    NodeModel parse(Document document) {
        document.getInputStream().withCloseable {
            return NodeModel.parse(new InputSource(it))
        }
    }
}

class ExcelParserBean {

    Workbook parse(Document document) {
        document.getInputStream().withCloseable {
            return WorkbookFactory.create(it)
        }
    }

    List<Sheet> getAllSheets(Workbook workbook) {
        final List<Sheet> result = []
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            result.add(workbook.getSheetAt(i))
        }
        return result
    }

    List<List<Object>> parseSheet(Sheet sheet) {
        final DataFormatter formatter = new DataFormatter()
        final Iterator<Row> iterator = sheet.iterator()
        final List<List<Object>> result = new ArrayList<>()

        while (iterator.hasNext()) {
            final Row nextRow = iterator.next()
            final List<Object> currRowValues = new ArrayList<>()
            final Iterator<Cell> cellIterator = nextRow.cellIterator()
            result.add(currRowValues)
            while (cellIterator.hasNext()) {
                final Cell cell = cellIterator.next()
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
    String encoding = "UTF-8"
    String description = ""
    Locale locale = Locale.getDefault()
    String templateName = ""
    boolean verbose = false
    String outputFile
    List<String> sourceFiles = []
    String includePattern = "**/*"
    String stdin

    CommandLine(String[] args) {
        def cli = new CliBuilder(usage: 'groovy freemarker-cli.groovy [options] file[s]', stopAtNonOption: false)
        cli.h(longOpt: 'help', 'Usage information', required: false)
        cli.v(longOpt: 'verbose', 'Verbose mode', required: false)
        cli.b(longOpt: 'basedir', 'Base directory to resolve FreeMarker templates', required: false, args: 1)
        cli.d(longOpt: 'description', 'Custom report description', required: false, args: 1)
        cli.e(longOpt: 'encoding', 'Encoding of source file', required: false, args: 1)
        cli.i(longOpt: 'include', 'Ant file pattern for directory search', required: false, args: 1)
        cli.o(longOpt: 'output', 'Generated output file', required: false, args: 1)
        cli.t(longOpt: 'template', 'Template name', required: false, args: 1)
        cli.l(longOpt: 'locale', 'Locale value', required: false, args: 1)

        OptionAccessor opt = cli.parse(args)

        if (args.length == 0 || !opt) {
            cli.usage()
            System.exit(1)
        }

        if (opt.h) {
            cli.usage()
            System.exit(0)
        }

        if (opt.v) {
            this.verbose = true
        }

        if (opt.d) {
            this.description = opt.d
        }

        if (opt.e) {
            this.encoding = opt.e
        }

        if (opt.i) {
            this.includePattern = opt.i
        }

        if (opt.t) {
            this.templateName = opt.t
        } else {
            cli.usage()
            System.exit(0)
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
            this.sourceFiles = resolveSourceFiles(opt.arguments())
        } else {
            final String lineSeparator = System.getProperty("line.separator")
            final StringBuffer buffer = new StringBuffer()
            System.in.eachLine { line ->
                buffer.append(line)
                buffer.append(lineSeparator)
            }
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

    private List<String> resolveSourceFiles(List<String> sourceFiles) {
        sourceFiles.stream()
                .map { name -> resolveSourceFile(name) }
                .toList()
                .flatten()
    }

    private List<String> resolveSourceFile(String name) {
        final File file = new File(name)

        if (!file.exists()) {
            throw new IllegalArgumentException("The file/directory does not exist: " + file.getAbsolutePath())
        }

        if (file.isDirectory()) {
            new FileNameFinder().getFileNames(name, this.includePattern)
        } else {
            [name]
        }
    }
}
