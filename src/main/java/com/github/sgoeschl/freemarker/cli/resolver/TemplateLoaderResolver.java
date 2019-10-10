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
import java.util.ArrayList;
import java.util.List;

import static com.github.sgoeschl.freemarker.cli.util.ObjectUtils.isNullOrEmtpty;

public class TemplateLoaderResolver {

    private static final String APP_HOME = "app.home";

    private final String baseDir;

    public TemplateLoaderResolver(String baseDir) {
        this.baseDir = baseDir;
    }

    public TemplateLoader resolve() {
        try {
            final List<TemplateLoader> loaders = new ArrayList<>();
            final String appHome = System.getProperty(APP_HOME);
            final String currentDir = System.getProperty("user.dir", ".");

            // When started with the shell script we pick up the templates of the installation
            if (!isNullOrEmtpty(appHome)) {
                loaders.add(new FileTemplateLoader(new File(System.getProperty(APP_HOME))));
            }

            // User has provided a template directory
            if (!isNullOrEmtpty(baseDir)) {
                loaders.add(new FileTemplateLoader(new File(baseDir)));
            }

            // If nothing is set use the current working directory
            if (isNullOrEmtpty(appHome) && isNullOrEmtpty(baseDir)) {
                loaders.add(new FileTemplateLoader(new File(currentDir)));
            }

            return new MultiTemplateLoader(loaders.toArray(new TemplateLoader[0]));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create MultiTemplateLoader", e);
        }
    }
}
