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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.github.sgoeschl.freemarker.cli.util.LocaleUtil.parseLocale;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;

/**
 * Capture all the settings required for rendering a FreeMarker template.
 */
public class Settings {

    private final List<String> args;
    private final String baseDir;
    private final String template;
    private final Charset sourceEncoding;
    private final Charset outputEncoding;
    private final boolean verbose;
    private final File outputFile;
    private final String include;
    private final Locale locale;
    private final String stdin;
    private final List<String> sources;
    private final Map<String, String> properties;
    private final Writer writer;

    private Settings(
            List<String> args,
            String baseDir,
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
        this.args = args;
        this.baseDir = baseDir;
        this.template = template;
        this.sourceEncoding = sourceEncoding;
        this.outputEncoding = outputEncoding;
        this.verbose = verbose;
        this.outputFile = outputFile;
        this.include = include;
        this.locale = locale;
        this.stdin = stdin;
        this.sources = sources;
        this.properties = properties;
        this.writer = writer;
    }

    public static SettingsBuilder builder() {
        return new SettingsBuilder();
    }

    public List<String> getArgs() {
        return args;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public String getTemplate() {
        return template;
    }

    public Charset getSourceEncoding() {
        return sourceEncoding != null ? sourceEncoding : UTF_8;
    }

    public Charset getOutputEncoding() {
        return outputEncoding != null ? outputEncoding : UTF_8;
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
                ", baseDir='" + baseDir + '\'' +
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
        private String baseDir;
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
        }

        public SettingsBuilder setArgs(String[] args) {
            if (args == null) {
                this.args = emptyList();
            } else {
                this.args = Arrays.asList(args);
            }

            return this;
        }

        public SettingsBuilder setBaseDir(String baseDir) {
            this.baseDir = baseDir;
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
                    baseDir,
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
