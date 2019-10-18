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
package com.github.sgoeschl.freemarker.cli.tools.commonscsv;

import com.github.sgoeschl.freemarker.cli.model.Settings;
import org.apache.commons.csv.CSVFormat;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class CommonsCsvDataModel {

    private final Settings settings;

    public CommonsCsvDataModel(Settings settings) {
        this.settings = requireNonNull(settings);
    }

    public Map<String, Object> create() {
        final Map<String, CSVFormat> csvFormats = new HashMap<>();
        csvFormats.put("DEFAULT", CSVFormat.DEFAULT);
        csvFormats.put("EXCEL", CSVFormat.EXCEL);
        csvFormats.put("INFORMIX_UNLOAD", CSVFormat.INFORMIX_UNLOAD);
        csvFormats.put("INFORMIX_UNLOAD_CSV", CSVFormat.INFORMIX_UNLOAD_CSV);
        csvFormats.put("MONGODB_CSV", CSVFormat.MONGODB_CSV);
        csvFormats.put("MONGODB_TSV", CSVFormat.MONGODB_TSV);
        csvFormats.put("MYSQL", CSVFormat.MYSQL);
        csvFormats.put("RFC4180", CSVFormat.RFC4180);
        csvFormats.put("ORACLE", CSVFormat.ORACLE);
        csvFormats.put("POSTGRESQL_CSV", CSVFormat.POSTGRESQL_CSV);
        csvFormats.put("POSTGRESQL_TEXT", CSVFormat.POSTGRESQL_TEXT);
        csvFormats.put("TDF", CSVFormat.TDF);

        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("CSVFormat", csvFormats);
        dataModel.put("CSVTool", new CommonsCsvTool(settings));
        return dataModel;
    }
}
