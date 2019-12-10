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

import com.github.sgoeschl.freemarker.cli.impl.ConfigurationSupplier;
import com.github.sgoeschl.freemarker.cli.impl.DocumentFactory;
import com.github.sgoeschl.freemarker.cli.impl.DocumentSupplier;
import com.github.sgoeschl.freemarker.cli.impl.TemplateLoaderSupplier;
import com.github.sgoeschl.freemarker.cli.impl.ToolsSupplier;
import com.github.sgoeschl.freemarker.cli.model.Document;
import com.github.sgoeschl.freemarker.cli.model.Documents;
import com.github.sgoeschl.freemarker.cli.model.Settings;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static freemarker.template.Configuration.VERSION_2_3_29;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class FreeMarkerTask implements Callable<Integer> {

    private static final int SUCCESS = 0;
    private static final String STDIN = "stdin";
    private static final Version FREEMARKER_VERSION = VERSION_2_3_29;

    private final Settings settings;

    public FreeMarkerTask(Settings settings) {
        this.settings = requireNonNull(settings);
    }

    @Override
    public Integer call() {
        return call(settings);
    }

    protected Integer call(Settings settings) {
        final Supplier<Map<String, Object>> tools = tools(settings);
        final Supplier<List<Document>> documentResolver = documentResolver(settings);
        final Supplier<TemplateLoader> templateLoader = templateLoader(settings);
        final Supplier<Configuration> configuration = configuration(settings, templateLoader);

        final Template template = getTemplate(settings, configuration);

        try (Writer writer = writer(settings); Documents documents = documents(settings, documentResolver)) {
            final Map<String, Object> dataModel = dataModel(settings, documents, tools);
            template.process(dataModel, writer);
            return SUCCESS;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to render FreeMarker Template: " + settings.getTemplateName(), e);
        }
    }

    protected Supplier<Configuration> configuration(Settings settings, Supplier<TemplateLoader> templateLoader) {
        return new ConfigurationSupplier(settings, templateLoader);
    }

    protected Supplier<TemplateLoader> templateLoader(Settings settings) {
        return new TemplateLoaderSupplier(settings.getTemplateDirectories());
    }

    protected Supplier<List<Document>> documentResolver(Settings settings) {
        return new DocumentSupplier(settings.getSources(), settings.getInclude(), settings.getInputEncoding());
    }

    protected Documents documents(Settings settings, Supplier<List<Document>> documentResolver) {
        final List<Document> documents = new ArrayList<>(documentResolver.get());

        // Add optional document from STDIN at the start of the list since
        // this allows easy sequence slicing in FreeMarker.
        if (settings.isReadFromStdin()) {
            documents.add(0, DocumentFactory.create(STDIN, System.in, STDIN, UTF_8));
        }

        return new Documents(documents);
    }

    /**
     * Loading FreeMarker templates from absolute paths is not encouraged due to security
     * concern (see https://freemarker.apache.org/docs/pgui_config_templateloading.html#autoid_42)
     * which are mostly irrelevant when running on the command line. So we resolve the absolute file
     * instead of relying on existing template loaders.
     */
    protected Template getTemplate(Settings settings, Supplier<Configuration> configurationSupplier) {
        final File templateFile = new File(settings.getTemplateName());
        final Configuration configuration = configurationSupplier.get();

        try {
            if (isAbsoluteTemplateFile(templateFile)) {
                return new Template(settings.getTemplateName(),
                        FileUtils.readFileToString(templateFile, settings.getTemplateEncoding()),
                        configuration);
            } else {
                return configuration.getTemplate(settings.getTemplateName());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load template file: " + templateFile);
        }
    }

    protected Map<String, Object> dataModel(Settings settings, Documents documents, Supplier<Map<String, Object>> tools) {
        final Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("Documents", documents);

        if (settings.isEnvironmentExposed()) {
            dataModel.putAll(System.getenv());
            dataModel.putAll(settings.getProperties());
        }

        dataModel.putAll(tools.get());

        return dataModel;
    }

    protected Supplier<Map<String, Object>> tools(Settings settings) {
        return new ToolsSupplier(settings);
    }

    protected Writer writer(Settings settings) {
        return settings.getWriter();
    }

    private static boolean isAbsoluteTemplateFile(File file) {
        return file.isAbsolute() && file.exists() & !file.isDirectory();
    }
}
