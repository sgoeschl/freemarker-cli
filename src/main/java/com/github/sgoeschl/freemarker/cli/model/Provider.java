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

import com.github.sgoeschl.freemarker.cli.extensions.commonscsv.CommonsCsvDataModel;
import com.github.sgoeschl.freemarker.cli.extensions.environment.EnvironmentDataModel;
import com.github.sgoeschl.freemarker.cli.extensions.excel.ExcelDataModel;
import com.github.sgoeschl.freemarker.cli.extensions.freemarker.FreeMarkerDataModel;
import com.github.sgoeschl.freemarker.cli.extensions.jsonpath.JsonPathDataModel;
import com.github.sgoeschl.freemarker.cli.extensions.jsoup.JsoupDataModel;
import com.github.sgoeschl.freemarker.cli.extensions.propertiesparser.PropertiesParserDataModel;
import com.github.sgoeschl.freemarker.cli.extensions.reportdata.ReportDataModel;
import com.github.sgoeschl.freemarker.cli.extensions.systemproperties.SystemPropertiesDataModel;
import com.github.sgoeschl.freemarker.cli.extensions.xml.XmlParserDataModel;
import com.github.sgoeschl.freemarker.cli.resolver.DocumentResolver;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.xmlbeans.SystemProperties;

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
import static java.util.Objects.requireNonNull;
import static org.apache.commons.codec.Charsets.UTF_8;

public class Provider {

    private final Settings settings;

    public Provider(Settings settings) {
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
            final Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
            configuration.setDirectoryForTemplateLoading(new File(settings.getBaseDir()));
            configuration.setTemplateLoader(templateLoader());
            configuration.setDefaultEncoding(UTF_8.name());
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setLogTemplateExceptions(false);
            return configuration;
        } catch (IOException e) {
            throw new RuntimeException("Unable to configure FreeMarker", e);
        }
    }

    private Map<String, Object> dataModel(Documents documents) {
        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("documents", documents.getAll());
        dataModel.put("Documents", documents);

        dataModel.putAll(new EnvironmentDataModel().create());
        dataModel.putAll(new FreeMarkerDataModel().create());
        dataModel.putAll(new ReportDataModel().create(settings.getDescription()));
        dataModel.putAll(new SystemPropertiesDataModel().create());

        dataModel.putAll(new CommonsCsvDataModel().create());
        dataModel.putAll(new ExcelDataModel().create());
        dataModel.putAll(new JsonPathDataModel().create());
        dataModel.putAll(new JsoupDataModel().create());
        dataModel.putAll(new PropertiesParserDataModel().create());
        dataModel.putAll(new XmlParserDataModel().create());
        return dataModel;
    }

    private Documents documents() {
        final List<Document> documents = new ArrayList<>(documentResolver().resolve());

        // Add optional document from STDIN at the and of the list
        if (settings.getStdin() != null) {
            documents.add(new Document("stdin", settings.getStdin()));
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
        final String appHome = SystemProperties.getProperty("app.home");

        if (!isNullOrEmtpty(appHome)) {
            loaders.add(new FileTemplateLoader(new File(SystemProperties.getProperty("app.home"))));
        }

        if (!isNullOrEmtpty(settings.getBaseDir())) {
            loaders.add(new FileTemplateLoader(new File(settings.getBaseDir())));
        }

        return new MultiTemplateLoader(loaders.toArray(new TemplateLoader[0]));
    }
}
