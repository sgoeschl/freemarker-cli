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
package com.github.sgoeschl.freemarker.cli.tools.environment;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class EnvironmentDataModel {

    private final boolean isEnvironmentExposed;
    private final Map<String, String> properties;

    public EnvironmentDataModel(Map<String, Object> settings) {
        this.isEnvironmentExposed = (Boolean) settings.getOrDefault("isEnvironmentExposed", Boolean.FALSE);
        this.properties = (Map<String, String>) settings.getOrDefault("properties", new HashMap<String, String>());
    }

    public Map<String, Object> create() {
        final Map<String, Object> dataModel = new HashMap<>();
        final Map<String, String> env = System.getenv();

        dataModel.put("Environment", env);

        if (isEnvironmentExposed) {
            dataModel.putAll(env);
            dataModel.putAll(properties);
        }

        return dataModel;
    }
}
