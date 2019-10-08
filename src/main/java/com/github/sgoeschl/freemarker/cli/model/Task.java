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

import com.github.sgoeschl.freemarker.cli.resolver.DocumentResolver;
import com.github.sgoeschl.freemarker.cli.tools.Tools;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.sgoeschl.freemarker.cli.util.ObjectUtils.isNullOrEmtpty;
import static freemarker.template.Configuration.VERSION_2_3_29;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;


public class Task {

    private static final String APP_HOME = "app.home";

    private final Settings settings;

    public Task(Settings settings) {
        this.settings = requireNonNull(settings);
    }

    public void run() {
        try {
            final Documents documents = documents();
            final Configuration configuration = configuration();
            final Map<String, Object> dataModel = dataModel(documents);
            final Template template = configuration.getTemplate(settings.getTemplate());

            try (Writer out = writer()) {
                template.process(dataModel, out);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to render FreeMarker Template: " + settings.getTemplate(), e);
        }
    }

    private Configuration configuration() {
        try {
            final Configuration configuration = new Configuration(VERSION_2_3_29);
            configuration.setObjectWrapper(objectWrapper());
            configuration.setTemplateLoader(templateLoader());
            // assume that FTLs are in UTF-8
            configuration.setDefaultEncoding(UTF_8.name());
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setLogTemplateExceptions(false);
            configuration.setOutputEncoding(settings.getOutputEncoding().name());
            return configuration;
        } catch (IOException e) {
            throw new RuntimeException("Unable to configure FreeMarker", e);
        }
    }

    private DefaultObjectWrapper objectWrapper() {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(VERSION_2_3_29);
        builder.setIterableSupport(false);
        return builder.build();
    }

    private Map<String, Object> dataModel(Documents documents) {
        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("documents", documents.getAll());
        dataModel.put("Documents", documents);
        dataModel.putAll(new Tools(settings).create());
        return dataModel;
    }

    private Documents documents() {
        final List<Document> documents = new ArrayList<>(documentResolver().resolve());

        // Add optional document from STDIN at the start of the list since
        // this allows easy sequence slicing in FreeMarker.
        if (settings.getStdin() != null) {
            documents.add(0, new Document("stdin", settings.getStdin()));
        }

        return new Documents(documents);
    }

    private Writer writer() {
        try {
            if (settings.hasOutputFile()) {
                return new BufferedWriter(new FileWriter(settings.getOutputFile()));
            } else {
                return new BufferedWriter(new OutputStreamWriter(System.out, settings.getOutputEncoding()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to create writer", e);
        }
    }

    private DocumentResolver documentResolver() {
        return new DocumentResolver(settings.getSources(), settings.getInclude(), settings.getSourceEncoding());
    }

    private TemplateLoader templateLoader() throws IOException {
        final List<TemplateLoader> loaders = new ArrayList<>();
        final String appHome = System.getProperty(APP_HOME);
        final String baseDir = settings.getBaseDir();
        final String currentDir = System.getProperty("user.dir", ".");

        // When started with the shell script we pick up the templates of the installation
        if (!isNullOrEmtpty(appHome)) {
            loaders.add(new FileTemplateLoader(new File(System.getProperty(APP_HOME))));
        }

        // User has provided a template directory
        if (!isNullOrEmtpty(baseDir)) {
            loaders.add(new FileTemplateLoader(new File(baseDir)));
        }

        // If nothing is set use the current working directory
        if(isNullOrEmtpty(appHome) && isNullOrEmtpty(baseDir)) {
            loaders.add(new FileTemplateLoader(new File(currentDir)));
        }

        return new MultiTemplateLoader(loaders.toArray(new TemplateLoader[0]));
    }
}
