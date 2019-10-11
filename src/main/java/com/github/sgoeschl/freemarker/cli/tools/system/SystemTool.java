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

import com.github.sgoeschl.freemarker.cli.model.Settings;

import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

public class SystemTool {

    private final Settings settings;

    public SystemTool(Settings settings) {
        this.settings = settings;
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

    public Settings getSettings() {
        return settings;
    }

    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
            return "localhost";
        }
    }

    public Writer getWriter() {
        return getSettings().getWriter();
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
