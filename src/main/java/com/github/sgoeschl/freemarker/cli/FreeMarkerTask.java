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

import com.github.sgoeschl.freemarker.cli.model.Document;
import com.github.sgoeschl.freemarker.cli.model.Documents;
import com.github.sgoeschl.freemarker.cli.model.Settings;
import com.github.sgoeschl.freemarker.cli.resolver.DocumentFactory;
import com.github.sgoeschl.freemarker.cli.resolver.DocumentResolver;
import com.github.sgoeschl.freemarker.cli.resolver.TemplateLoaderResolver;
import com.github.sgoeschl.freemarker.cli.tools.Tools;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static freemarker.template.Configuration.VERSION_2_3_29;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class FreeMarkerTask implements Callable<Integer> {

    private static final String STDIN = "stdin";

    private final Settings settings;

    public FreeMarkerTask(Settings settings) {
        this.settings = requireNonNull(settings);
    }

    @Override
    public Integer call() {
        try (final Documents documents = documents()) {
            final Configuration configuration = configuration();
            final Map<String, Object> dataModel = dataModel(documents);
            final Template template = getTemplate(settings, configuration);

            try (Writer out = settings.getWriter()) {
                template.process(dataModel, out);
            }

            return 0;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to render FreeMarker Template: " + settings.getTemplateName(), e);
        }
    }

    private Configuration configuration() {
        final Configuration configuration = new Configuration(VERSION_2_3_29);
        configuration.setObjectWrapper(objectWrapper());
        configuration.setTemplateLoader(templateLoader());
        configuration.setDefaultEncoding(settings.getTemplateEncoding().name());
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setOutputEncoding(settings.getOutputEncoding().name());
        configuration.setLocale(settings.getLocale());
        return configuration;
    }

    /**
     * Loading FreeMarker templates from absolute paths is not encouraged due to security
     * concern (see https://freemarker.apache.org/docs/pgui_config_templateloading.html#autoid_42)
     * which are mostly irrelevant when running on the command line. So we resolve the absolute file
     * instead of relyong on existing template loaders.
     */
    private Template getTemplate(Settings settings, Configuration configuration) throws IOException {
        final File templateFile = new File(settings.getTemplateName());
        if (isAbsoluteTemplateFile(templateFile)) {
            return new Template(settings.getTemplateName(),
                    FileUtils.readFileToString(templateFile, settings.getTemplateEncoding()),
                    configuration);
        } else {
            return configuration.getTemplate(settings.getTemplateName());
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
        if (settings.isReadFromStdin()) {
            documents.add(0, DocumentFactory.create(STDIN, System.in, STDIN, UTF_8));
        }

        return new Documents(documents);
    }

    private DocumentResolver documentResolver() {
        return new DocumentResolver(settings.getSources(), settings.getInclude(), settings.getSourceEncoding());
    }

    private TemplateLoader templateLoader() {
        return new TemplateLoaderResolver(settings.getTemplateDirectories()).resolve();
    }

    private boolean isAbsoluteTemplateFile(File file) {
        return file.isAbsolute() && file.exists() & !file.isDirectory();
    }
}
