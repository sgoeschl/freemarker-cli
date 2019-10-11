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
package com.github.sgoeschl.freemarker.cli.resolver;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TemplateLoaderResolver {

    private final List<File> templateDirectories;

    public TemplateLoaderResolver(List<File> templateDirectories) {
        this.templateDirectories = templateDirectories;
    }

    public TemplateLoader resolve() {
        return new MultiTemplateLoader(
                templateDirectories.stream()
                        .map(this::fileTemplateLoader)
                        .toArray(TemplateLoader[]::new));
    }

    private FileTemplateLoader fileTemplateLoader(File directory) {
        try {
            return new FileTemplateLoader(directory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create MultiTemplateLoader: " + directory, e);
        }
    }

}
