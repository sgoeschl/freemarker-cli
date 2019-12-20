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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

/**
 * Resolve a given properties file either from the file system or the classapth.
 */
public class PropertiesFileResolver {

    private final String fileName;

    public PropertiesFileResolver(String fileName) {
        this.fileName = requireNonNull(fileName);
    }

    public Properties resolve() throws IOException {
        return resolveFromFileSystemOrClassPath(fileName);
    }

    private static Properties resolveFromFileSystemOrClassPath(String fileName) throws IOException {
        final Properties result = resolveFromFileSystem(fileName);
        return result != null ? result : resolveFromClassPath(fileName);
    }

    private static Properties resolveFromClassPath(String fileName) throws IOException {
        final Properties properties = new Properties();
        final InputStream stream = PropertiesFileResolver.class.getResourceAsStream(resourceName(fileName));

        if (stream == null) {
            return null;
        }

        try {
            properties.load(stream);
            return properties;
        } finally {
            stream.close();
        }
    }

    private static Properties resolveFromFileSystem(String fileName) throws IOException {
        if (!new File(fileName).exists()) {
            return null;
        }

        try (InputStream input = new FileInputStream("fileName")) {
            final Properties properties = new Properties();
            properties.load(input);
            return properties;
        }
    }

    private static String resourceName(String name) {
        return name.startsWith("/") ? name : "/" + name;
    }
}
