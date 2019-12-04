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
package com.github.sgoeschl.freemarker.cli.tools.system;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("unchecked")
public class SystemTool {

    private final List<String> args;
    private final Map<String, String> properties;
    private final List<File> templateDirectories;

    public SystemTool(Map<String, Object> settings) {
        this.args = (List<String>) settings.getOrDefault("args", Collections.emptyList());
        this.properties = (Map<String, String>) settings.getOrDefault("properties", new HashMap<>());
        this.templateDirectories = (List<File>) settings.getOrDefault("templateDirectories", Collections.emptyList());
    }

    public List<String> getArgs() {
        return args;
    }

    public List<File> getTemplateDirectories() {
        return templateDirectories;
    }

    public Map<String, String> getUserSuppliedProperties() {
        return properties;
    }

    public Properties getProperties() {
        return System.getProperties();
    }

    public String getProperty(String key) {
        return System.getProperty(key);
    }

    public String getProperty(String key, String def) {
        return System.getProperty(key, def);
    }

    public Map<String, String> getEnvironment() {
        return System.getenv();
    }

    public String getEnvironment(String name) {
        return System.getenv(name);
    }

    public String getEnvironment(String name, String def) {
        return System.getenv(name) != null ? System.getenv(name) : def;
    }

    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
            return "localhost";
        }
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
