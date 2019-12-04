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
package com.github.sgoeschl.freemarker.cli.tools;

import com.github.sgoeschl.freemarker.cli.tools.commonscsv.CommonsCsvDataModel;
import com.github.sgoeschl.freemarker.cli.tools.commonsexec.CommonsExecDataModel;
import com.github.sgoeschl.freemarker.cli.tools.environment.EnvironmentDataModel;
import com.github.sgoeschl.freemarker.cli.tools.excel.ExcelDataModel;
import com.github.sgoeschl.freemarker.cli.tools.freemarker.FreeMarkerDataModel;
import com.github.sgoeschl.freemarker.cli.tools.grok.GrokDataModel;
import com.github.sgoeschl.freemarker.cli.tools.jsonpath.JsonPathDataModel;
import com.github.sgoeschl.freemarker.cli.tools.jsoup.JsoupDataModel;
import com.github.sgoeschl.freemarker.cli.tools.propertiesparser.PropertiesParserDataModel;
import com.github.sgoeschl.freemarker.cli.tools.snakeyaml.SnakeYamlDataModel;
import com.github.sgoeschl.freemarker.cli.tools.system.SystemToolDataModel;
import com.github.sgoeschl.freemarker.cli.tools.systemproperties.SystemPropertiesDataModel;
import com.github.sgoeschl.freemarker.cli.tools.xml.XmlDataModel;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class Tools {

    private final Map<String, Object> settings;

    public Tools(Map<String, Object> settings) {
        this.settings = requireNonNull(settings);
    }

    public Map<String, Object> create() {
        final Map<String, Object> dataModel = new HashMap<>();

        dataModel.putAll(new EnvironmentDataModel(settings).create());
        dataModel.putAll(new FreeMarkerDataModel(settings).create());
        dataModel.putAll(new SystemPropertiesDataModel(settings).create());

        dataModel.putAll(new CommonsCsvDataModel(settings).create());
        dataModel.putAll(new CommonsExecDataModel(settings).create());
        dataModel.putAll(new ExcelDataModel(settings).create());
        dataModel.putAll(new GrokDataModel(settings).create());
        dataModel.putAll(new JsonPathDataModel(settings).create());
        dataModel.putAll(new JsoupDataModel(settings).create());
        dataModel.putAll(new PropertiesParserDataModel(settings).create());
        dataModel.putAll(new SnakeYamlDataModel(settings).create());
        dataModel.putAll(new SystemToolDataModel(settings).create());
        dataModel.putAll(new XmlDataModel(settings).create());

        return dataModel;
    }
}
