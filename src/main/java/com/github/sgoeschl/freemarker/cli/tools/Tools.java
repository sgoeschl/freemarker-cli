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

import com.github.sgoeschl.freemarker.cli.tools.commonscsv.CommonsCsvTool;
import com.github.sgoeschl.freemarker.cli.tools.commonsexec.CommonsExecTool;
import com.github.sgoeschl.freemarker.cli.tools.excel.ExcelTool;
import com.github.sgoeschl.freemarker.cli.tools.freemarker.FreeMarkerTool;
import com.github.sgoeschl.freemarker.cli.tools.grok.GrokTool;
import com.github.sgoeschl.freemarker.cli.tools.jsonpath.JsonPathTool;
import com.github.sgoeschl.freemarker.cli.tools.jsoup.JsoupTool;
import com.github.sgoeschl.freemarker.cli.tools.properties.PropertiesTool;
import com.github.sgoeschl.freemarker.cli.tools.snakeyaml.SnakeYamlTool;
import com.github.sgoeschl.freemarker.cli.tools.system.SystemTool;
import com.github.sgoeschl.freemarker.cli.tools.xml.XmlTool;

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

        dataModel.put("FreeMarkerTool", new FreeMarkerTool(settings));
        dataModel.put("PropertiesTool", new PropertiesTool(settings));
        dataModel.put("SystemTool", new SystemTool(settings));

        dataModel.put("CSVTool", new CommonsCsvTool(settings));
        dataModel.put("CommonsExecTool", new CommonsExecTool(settings));
        dataModel.put("ExcelTool", new ExcelTool(settings));
        dataModel.put("GrokTool", new GrokTool(settings));
        dataModel.put("JsonPathTool", new JsonPathTool(settings));
        dataModel.put("JsoupTool", new JsoupTool(settings));
        dataModel.put("YamlTool", new SnakeYamlTool(settings));
        dataModel.put("XmlTool", new XmlTool(settings));

        return dataModel;
    }
}
