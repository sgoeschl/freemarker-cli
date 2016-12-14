#!/usr/bin/env groovy
@Grapes([
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
import groovy.transform.ToString
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.xml.sax.InputSource

public static void main(String[] args) {
    final CommandLine cli = new CommandLine(args)
    new Task("freemarker-cli", cli).run()
}

@ToString(includeNames = true)
class Task {

    String name
    CommandLine cli
    static boolean verbose

    Task(String name, CommandLine cli) {
        this.name = name
        this.cli = cli
        Locale.setDefault(cli.locale)
        verbose = cli.verbose
    }

    void run() {
        final Writer writer
        final File outputFile = cli.hasOutputFile() ? new File(cli.outputFile) : null
        final long startTime = System.currentTimeMillis();
        final List<Document> documents = readDocuments(cli)
        final Configuration configuration = createConfiguration()
        final Map<String, Object> dataModel = createDataModel(documents)
        final Template template = configuration.getTemplate(cli.templateName);

        try {
            writer = createWriter(cli.outputFile)
            template.process(dataModel, writer)
            final long durationInMs = System.currentTimeMillis() - startTime;
            log("Template processing finished in ${durationInMs} ms")

            if(outputFile != null && outputFile.exists()) {
                log("The ouptut file has ${outputFile.length()} bytes")
            }
        }
        finally {
            if (writer != null) {
                writer.close()
            }
        }
    }

    static List<Document> readDocuments(CommandLine cli) {
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

    static Configuration createConfiguration() {
        final Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);
        configuration.setDirectoryForTemplateLoading(new File("."));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER)
        configuration.setLogTemplateExceptions(false);
        return configuration
    }

    static Map<String, Object> createDataModel(List<Document> documents) {
        final Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("documents", documents)
        dataModel.putAll(createCommonsCsvDataModel())
        dataModel.putAll(createJsonPathDataModel())
        dataModel.putAll(createXmlParserDataModel())
        dataModel.putAll(createFreeMarkerDataModel())
        dataModel.putAll(createSystemPropertiesDataModel())
        dataModel.putAll(createEnvironmentDataModel())
        return dataModel
    }

    static Map<String, Object> createCommonsCsvDataModel() {
        final Map<String, CSVFormat> csvFormats = new HashMap<String, CSVFormat>();
        csvFormats.put("DEFAULT", CSVFormat.DEFAULT)
        csvFormats.put("EXCEL", CSVFormat.EXCEL)
        csvFormats.put("MYSQL", CSVFormat.MYSQL)
        csvFormats.put("RFC4180", CSVFormat.RFC4180)
        csvFormats.put("TDF", CSVFormat.TDF)

        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("CSVFormat", csvFormats)
        dataModel.put("CSVParser", new CSVParserBean())
        return dataModel
    }

    static Map<String, Object> createSystemPropertiesDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("SystemProperties", System.getProperties())
        return dataModel
    }

    static Map<String, Object> createEnvironmentDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("Environment", System.getenv())
        return dataModel
    }

    static Map<String, Object> createJsonPathDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("JsonPath", new JsonPathBean())
        return dataModel
    }

    static Map<String, Object> createXmlParserDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("XmlParser", new XmlParserBean())
        return dataModel
    }

    static Map<String, Object> createFreeMarkerDataModel() {
        final Map<String, Object> dataModel = new HashMap<String, Object>();
        final BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_25);
        final BeansWrapper beansWrapper = builder.build();
        dataModel.put("ObjectConstructor", new ObjectConstructor());
        dataModel.put("Statics", beansWrapper.getStaticModels());
        dataModel.put("Enums", beansWrapper.getEnumModels());
        return dataModel
    }

    static Writer createWriter(String outputFileName) {
        if (outputFileName == null || outputFileName.isEmpty()) {
            new BufferedWriter(new OutputStreamWriter(System.out));
        } else {
            final File outputFile = new File(outputFileName)
            if (outputFile.exists()) {
                log("Deleting the exiting output file: ${outputFile.getAbsolutePath()}")
                outputFile.delete()
                assert !outputFile.exists()
            }
            new BufferedWriter(new FileWriter(outputFile));
        }
    }

    static void log(String message) {
        if (verbose) {
            System.err.println("[freemarker-cli] " + message)
        }
    }
}

@ToString(includeNames = true)
class Document {
    String name
    File file
    String content
    long length;

    Document(String name, File file, String content) {
        this.name = name
        this.file = file
        this.content = content != null ? content : ""
        this.length = content != null ? content.length() : -1
    }

    public boolean hasFile() {
        return file != null;
    }
}

@ToString(includeNames = true)
class CSVParserBean {
    public CSVParser parse(String string, CSVFormat format) {
        return CSVParser.parse(string, format);

    }
}

@ToString(includeNames = true)
class JsonPathBean {
    public DocumentContext parse(String string) {
        return JsonPath.parse(string)

    }
}

@ToString(includeNames = true)
class XmlParserBean {
    public NodeModel parse(String string) {
        final InputSource inputSource = new InputSource(new StringReader(string))
        return NodeModel.parse(inputSource)
    }
}

@ToString(includeNames = true, excludes = "stdin")
class CommandLine {
    Locale locale = Locale.getDefault();
    String templateName = ''
    boolean verbose = false
    String outputFile
    List<String> sourceFiles = []
    String stdin

    CommandLine(String[] args) {
        def cli = new CliBuilder(usage: 'groovy freemarker-cli.groovy [options] file[s]', stopAtNonOption: false)
        cli.h(longOpt: 'help', 'Usage information', required: false)
        cli.v(longOpt: 'verbose', 'Verbose mode', required: false)
        cli.o(longOpt: 'output', 'Output file', required: false, args: 1)
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
            cli.usage();
            System.exit(0)
        }

        if (opt.t) {
            this.templateName = opt.t
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
}
