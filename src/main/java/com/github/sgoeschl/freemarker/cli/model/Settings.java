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
package com.github.sgoeschl.freemarker.cli.model;

import java.io.File;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.github.sgoeschl.freemarker.cli.util.LocaleUtils.parseLocale;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

/**
 * Capture all the settings required for rendering a FreeMarker template.
 */
public class Settings {

    /** User-supplied command line arguments */
    private final List<String> args;

    /** Optional template directories */
    private final List<File> templateDirectories;

    /** Template to be rendered */
    private final String template;

    /** Encoding of source files */
    private final Charset sourceEncoding;

    /** Encoding of output files */
    private final Charset outputEncoding;

    /** Enable verbose mode **/
    private final boolean verbose;

    /** Optional output file */
    private final File outputFile;

    /** Include pattern for recursice directly search of sourc files */
    private final String include;

    /** The locale to use for rendering */
    private final Locale locale;

    /** Content of "System.in" */
    private final String stdin;

    /** User-supplied list of source files or directories */
    private final List<String> sources;

    /** User-supplied system properties, i.e. "-Dfoo=bar" */
    private final Map<String, String> properties;

    /** The writer oused for rendering templates */
    private final Writer writer;

    private Settings(
            List<String> args,
            List<File> templateDirectories,
            String template,
            Charset sourceEncoding,
            Charset outputEncoding,
            boolean verbose,
            File outputFile,
            String include,
            Locale locale,
            String stdin,
            List<String> sources,
            Map<String, String> properties,
            Writer writer) {
        this.args = requireNonNull(args);
        this.templateDirectories = requireNonNull(templateDirectories);
        this.template = requireNonNull(template);
        this.sourceEncoding = sourceEncoding;
        this.outputEncoding = outputEncoding;
        this.verbose = verbose;
        this.outputFile = outputFile;
        this.include = include;
        this.locale = requireNonNull(locale);
        this.stdin = stdin;
        this.sources = requireNonNull(sources);
        this.properties = requireNonNull(properties);
        this.writer = requireNonNull(writer);
    }

    public static SettingsBuilder builder() {
        return new SettingsBuilder();
    }

    public List<String> getArgs() {
        return args;
    }

    public List<File> getTemplateDirectories() {
        return templateDirectories;
    }

    public String getTemplate() {
        return template;
    }

    public Charset getSourceEncoding() {
        return sourceEncoding;
    }

    public Charset getOutputEncoding() {
        return outputEncoding;
    }

    public Charset getTemplateEncoding() {
        return UTF_8;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public String getInclude() {
        return include;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getStdin() {
        return stdin;
    }

    public List<String> getSources() {
        return sources;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public boolean hasOutputFile() {
        return outputFile != null;
    }

    public Writer getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "args='" + args + '\'' +
                ", templateDirectories='" + templateDirectories + '\'' +
                ", template='" + template + '\'' +
                ", sourceEncoding=" + sourceEncoding +
                ", outputEncoding=" + outputEncoding +
                ", verbose=" + verbose +
                ", outputFile=" + outputFile +
                ", include='" + include + '\'' +
                ", locale=" + locale +
                ", stdin='" + stdin + '\'' +
                ", sources=" + sources +
                ", properties=" + properties +
                '}';
    }

    public static class SettingsBuilder {
        private List<String> args;
        private List<File> templateDirectories;
        private String template;
        private String sourceEncoding;
        private String outputEncoding;
        private boolean verbose;
        private String outputFile;
        private String include;
        private String locale;
        private String stdin;
        private List<String> sources;
        private Map<String, String> properties;
        private Writer writer;

        private SettingsBuilder() {
            this.args = emptyList();
            this.sources = emptyList();
            this.setSourceEncoding(UTF_8.name());
            this.setOutputEncoding(UTF_8.name());
            this.templateDirectories = emptyList();
            this.properties = new HashMap<>();
        }

        public SettingsBuilder setArgs(String[] args) {
            if (args == null) {
                this.args = emptyList();
            } else {
                this.args = Arrays.asList(args);
            }

            return this;
        }

        public SettingsBuilder setTemplateDirectories(List<File> list) {
            this.templateDirectories = list;
            return this;
        }

        public SettingsBuilder setTemplate(String template) {
            this.template = template;
            return this;
        }

        public SettingsBuilder setSourceEncoding(String sourceEncoding) {
            this.sourceEncoding = sourceEncoding;
            return this;
        }

        public SettingsBuilder setOutputEncoding(String outputEncoding) {
            this.outputEncoding = outputEncoding;
            return this;
        }

        public SettingsBuilder setVerbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        public SettingsBuilder setOutputFile(String outputFile) {
            this.outputFile = outputFile;
            return this;
        }

        public SettingsBuilder setInclude(String include) {
            this.include = include;
            return this;
        }

        public SettingsBuilder setLocale(String locale) {
            this.locale = locale;
            return this;
        }

        public SettingsBuilder setStdin(String stdin) {
            this.stdin = stdin;
            return this;
        }

        public SettingsBuilder setSources(List<String> sources) {
            this.sources = sources;
            return this;
        }

        public SettingsBuilder setProperties(Map<String, String> properties) {
            this.properties = properties;
            return this;
        }

        public SettingsBuilder setWriter(Writer writer) {
            this.writer = writer;
            return this;
        }

        public Settings build() {
            final Charset outputEncoding = Charset.forName(this.outputEncoding);
            final Charset sourceEncoding = Charset.forName(this.sourceEncoding);

            return new Settings(
                    args,
                    templateDirectories,
                    template,
                    sourceEncoding,
                    outputEncoding,
                    verbose,
                    outputFile != null ? new File(outputFile) : null,
                    include,
                    parseLocale(locale),
                    stdin,
                    sources,
                    properties,
                    writer
            );
        }
    }
}
