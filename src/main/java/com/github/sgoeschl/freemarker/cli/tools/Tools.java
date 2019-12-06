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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Arrays.stream;

public class Tools {

    public Map<String, Object> create(Map<String, Object> settings) {
        final Map<String, Object> result = new HashMap<>();
        final Properties toolsProperties = (Properties) settings.get("freemarker.tools.properties");

        for (String tool : toolsProperties.stringPropertyNames()) {
            final String clazzName = toolsProperties.getProperty(tool).trim();
            result.put(tool, createTool(clazzName, settings));
        }

        return result;
    }

    private static Object createTool(String clazzName, Map<String, Object> settings) {
        try {
            final Class<?> clazz = Class.forName(clazzName);
            final Constructor<?>[] constructors = clazz.getConstructors();
            final Constructor constructorWithSettings = findSingleParameterConstructor(constructors, Map.class);
            final Constructor defaultConstructor = findDefaultConstructor(constructors);
            return constructorWithSettings != null ? constructorWithSettings.newInstance(settings) : defaultConstructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tool: " + clazzName, e);
        }
    }

    private static Constructor findSingleParameterConstructor(Constructor[] constructors, Class parameterClazz) {
        return stream(constructors)
                .filter(c -> c.getParameterCount() == 1 && c.getParameterTypes()[0].equals(parameterClazz))
                .findFirst()
                .orElse(null);
    }

    private static Constructor findDefaultConstructor(Constructor[] constructors) {
        return stream(constructors)
                .filter(c -> c.getParameterCount() == 0)
                .findFirst()
                .orElse(null);
    }

}
