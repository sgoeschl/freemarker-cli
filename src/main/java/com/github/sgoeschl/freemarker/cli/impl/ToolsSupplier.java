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
package com.github.sgoeschl.freemarker.cli.impl;

import freemarker.template.utility.ClassUtil;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

public class ToolsSupplier implements Supplier<Map<String, Object>> {

    private static final String FREEMARKER_TOOLS_PREFIX = "freemarker.tools.";

    private final Properties configuration;
    private final Map<String, Object> settings;

    /**
     * Constructor.
     *
     * @param configuration Containing "freemarker.tools.XXX=class"
     * @param settings      Passed tp the instanitated tools
     */
    public ToolsSupplier(Properties configuration, Map<String, Object> settings) {
        this.configuration = requireNonNull(configuration);
        this.settings = requireNonNull(settings);
    }

    @Override
    public Map<String, Object> get() {
        return configuration.stringPropertyNames().stream()
                .filter(name -> name.startsWith(FREEMARKER_TOOLS_PREFIX))
                .filter(name -> toolClassCanBeLoaded(configuration.getProperty(name)))
                .collect(toMap(ToolsSupplier::toolName, name -> createTool(configuration.getProperty(name), settings)));
    }

    private boolean toolClassCanBeLoaded(String clazzName) {
        try {
            ClassUtil.forName(clazzName);
            return true;
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Create a tool instance either using single argument constructor taking a map or
     * the default constructor.
     *
     * @param clazzName Class to instantiate
     * @param settings  Settings used to configure the tool
     * @return Tool instance
     */
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

    private static String toolName(String propertyName) {
        return propertyName.substring(FREEMARKER_TOOLS_PREFIX.length());
    }
}
