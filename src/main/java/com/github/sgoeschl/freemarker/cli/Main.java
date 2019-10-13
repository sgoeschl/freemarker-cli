/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.sgoeschl.freemarker.cli;

import com.github.sgoeschl.freemarker.cli.Main.GitVersionProvider;
import com.github.sgoeschl.freemarker.cli.model.Settings;
import com.github.sgoeschl.freemarker.cli.resolver.TemplateDirectoryResolver;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

import static com.github.sgoeschl.freemarker.cli.util.ObjectUtils.isNotEmpty;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Command(description = "Apache FreeMarker CLI", name = "freemarker-cli", mixinStandardHelpOptions = true, versionProvider = GitVersionProvider.class)
public class Main implements Callable<Integer> {

    @Option(names = { "-b", "--basedir" }, description = "Additional template directory to resolve FreeMarker templates")
    private String baseDir;

    @Option(names = { "-t", "--template" }, description = "FreeMarker template used for rendering", required = true)
    private String template;

    @Option(names = { "-e", "--source-encoding" }, description = "Encoding of source file", defaultValue = "UTF-8")
    String sourceEncoding;

    @Option(names = { "--output-encoding" }, description = "Encoding of output file, e.g. UTF-8", defaultValue = "UTF-8")
    String outputEncoding;

    // Curretnly useless since we don't have verbose output
    // @Option(names = { "-v", "--verbose" }, description = "Verbose mode")
    // private boolean verbose;

    @Option(names = { "-o", "--output" }, description = "Output file")
    private String outputFile;

    @Option(names = { "--include" }, description = "File pattern for directory search")
    private String include;

    @Option(names = { "-l", "--locale" }, description = "Locale being used for output file, e.g. 'en_US")
    private String locale;

    @Option(names = { "--stdin" }, description = "Read source document from stdin")
    private boolean readFromStdin;

    @Option(names = { "-D" }, description = "Set a system property")
    private Map<String, String> properties;

    @Option(names = { "-E", "--expose-env" }, description = "Expose environment variables")
    private boolean isEnvironmentExposed;

    @Parameters(description = "Any number of input source files and/or directories")
    private List<String> sources;

    /** User-supplied command line parameters */
    private final String[] args;

    /** User-supplied writer */
    private Writer userSuppliedWriter;

    private Main(String[] args) {
        this.args = requireNonNull(args);
    }

    private Main(String[] args, Writer userSuppliedWriter) {
        this.args = requireNonNull(args);
        this.userSuppliedWriter = requireNonNull(userSuppliedWriter);
    }

    public static void main(String[] args) {
        try {
            System.exit(execute(args));
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static int execute(String[] args) {
        return new CommandLine(new Main(args)).execute(args);
    }

    /** User for testing to inject a writer */
    public static int execute(String[] args, Writer writer) {
        return new CommandLine(new Main(args, writer)).execute(args);
    }

    @Override
    public Integer call() {

        // set system properties as soon as possible
        if (properties != null && !properties.isEmpty()) {
            System.getProperties().putAll(properties);
        }

        final List<File> templateDirectories = getTemplateDirectories(baseDir);

        final Settings settings = Settings.builder()
                .setArgs(args)
                .setTemplateDirectories(templateDirectories)
                .setTemplateName(template)
                .setSourceEncoding(sourceEncoding)
                .setOutputEncoding(outputEncoding)
                .setOutputFile(outputFile)
                .setInclude(include)
                .setLocale(locale)
                .isReadFromStdin(readFromStdin)
                .isEnvironmentExposed(isEnvironmentExposed)
                .setSources(sources != null ? sources : new ArrayList<>())
                .setProperties(properties != null ? properties : new HashMap<>())
                .setWriter(writer(outputFile, sourceEncoding))
                .build();

        try {
            return new FreeMarkerTask(settings).call();
        } finally {
            if (settings.hasOutputFile()) {
                close(settings.getWriter());
            }
        }
    }

    private Writer writer(String outputFile, String ouputEncoding) {
        try {
            if (userSuppliedWriter != null) {
                return userSuppliedWriter;
            } else if (isNotEmpty(outputFile)) {
                return new BufferedWriter(new FileWriter(outputFile));
            } else {
                return new BufferedWriter(new OutputStreamWriter(System.out, ouputEncoding));
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to create writer", e);
        }
    }

    private static void close(Writer writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to close output writer", e);
        }
    }

    private static List<File> getTemplateDirectories(String baseDir) {
        return new TemplateDirectoryResolver(baseDir).resolve();
    }

    public static final class GitVersionProvider implements IVersionProvider {

        private final String gitBuildVersion;
        private final String gitCommitId;
        private final String gitCommitTime;

        public GitVersionProvider() {
            final Properties gitProperties = getGitProperties();
            this.gitBuildVersion = gitProperties.getProperty("git.build.version", "unknown");
            this.gitCommitId = gitProperties.getProperty("git.commit.id", "unknown");
            this.gitCommitTime = gitProperties.getProperty("git.commit.time", "unknown");
        }

        @Override
        public String[] getVersion() {
            return new String[] { format("version=%s, time=%s, commit=%s", gitBuildVersion, gitCommitTime, gitCommitId) };
        }

        private static Properties getGitProperties() {
            final Properties properties = new Properties();
            try (InputStream is = Main.class.getClassLoader().getResourceAsStream("git.properties")) {
                if (is != null) {
                    properties.load(is);
                }
            } catch (Exception e) {
                return properties;
            }
            return properties;
        }
    }
}
