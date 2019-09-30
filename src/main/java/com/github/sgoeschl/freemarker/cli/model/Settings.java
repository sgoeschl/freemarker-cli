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
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import static com.github.sgoeschl.freemarker.cli.util.ObjectUtils.isNullOrEmtpty;

public class Settings {

    private final String baseDir;
    private final String template;
    private final Charset sourceEncoding;
    private final Charset outputEncoding;
    private final String description;
    private final boolean verbose;
    private final File outputFile;
    private final String include;
    private final Locale locale;
    private final String stdin;
    private final List<String> sources;

    private Settings(
            String baseDir,
            String template,
            Charset sourceEncoding,
            Charset outputEncoding,
            String description,
            boolean verbose,
            File outputFile,
            String include,
            Locale locale,
            String stdin,
            List<String> sources) {
        this.baseDir = baseDir;
        this.template = template;
        this.sourceEncoding = sourceEncoding;
        this.outputEncoding = outputEncoding;
        this.description = description;
        this.verbose = verbose;
        this.outputFile = outputFile;
        this.include = include;
        this.locale = locale;
        this.stdin = stdin;
        this.sources = sources;
    }

    public static SettingsBuilder builder() {
        return new SettingsBuilder();
    }

    public String getBaseDir() {
        return baseDir;
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

    public String getDescription() {
        return description;
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

    public boolean hasOutputFile() {
        return outputFile != null;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "baseDir='" + baseDir + '\'' +
                ", template='" + template + '\'' +
                ", sourceEncoding=" + sourceEncoding +
                ", outputEncoding=" + outputEncoding +
                ", description='" + description + '\'' +
                ", verbose=" + verbose +
                ", outputFile=" + outputFile +
                ", include='" + include + '\'' +
                ", locale=" + locale +
                ", stdin='" + stdin + '\'' +
                ", sources=" + sources +
                '}';
    }

    public static class SettingsBuilder {
        private String baseDir;
        private String template;
        private String sourceEncoding;
        private String outputEncoding;
        private String description;
        private boolean verbose;
        private String outputFile;
        private String include;
        private String locale;
        private String stdin;
        private List<String> sources;

        private SettingsBuilder() {
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

        public SettingsBuilder setDescription(String description) {
            this.description = description;
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

        public Settings build() {
            return new Settings(
                    baseDir,
                    template,
                    Charset.forName(sourceEncoding),
                    Charset.forName(outputEncoding),
                    description,
                    verbose,
                    outputFile != null ? new File(outputFile) : null,
                    include,
                    parseLocale(locale),
                    stdin,
                    sources);
        }

        private static Locale parseLocale(String value) {
            if (isNullOrEmtpty(value)) {
                return Locale.getDefault();
            }

            final String[] parts = value.split("_");
            return parts.length == 1 ? new Locale(parts[0]) : new Locale(parts[0], parts[1]);
        }
    }
}
