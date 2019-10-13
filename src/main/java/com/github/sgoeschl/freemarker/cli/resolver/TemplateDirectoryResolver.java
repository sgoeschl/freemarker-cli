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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * Determine a list of directories to load a relative template file in the following order
 * <ol>
 *    <li>User-defined template directory</li>
 *    <li>Current working directory</li>
 *    <li>~/.freemarker-cli</li>
 *    <li>Application installation directory</li>
 * </ol>
 */
public class TemplateDirectoryResolver {

    private static final String UNDEFINED = "__undefined__";

    /** Installation directory of "freemarkr-cli" */
    private static final String APP_HOME = "app.home";

    /** Current working directory */
    private static final String USER_DIR = "user.dir";

    /** Home directory of the user */
    private static final String USER_HOME = "user.home";

    /** The user's "freemarker-cli" directory */
    private static final String USER_CONFIGURATION_DIR_NAME = ".freemarker-cli";

    /** User-supplied template directory */
    private final String userSuppliedTemplateDir;

    public TemplateDirectoryResolver(String userSuppliedTemplateDir) {
        this.userSuppliedTemplateDir = userSuppliedTemplateDir;
    }

    public List<File> resolve() {
        final File applicationDir = new File(System.getProperty(APP_HOME, UNDEFINED));
        final File userHomeDir = new File(System.getProperty(USER_HOME, UNDEFINED));
        final File currentWorkingDir = new File(System.getProperty(USER_DIR, UNDEFINED));
        final File userConfigDir = new File(userHomeDir, USER_CONFIGURATION_DIR_NAME);
        final File userTemplateDir = userSuppliedTemplateDir != null ? new File(userSuppliedTemplateDir) : null;

        final List<File> loaders = new ArrayList<>(asList(
                applicationDir,
                userConfigDir,
                currentWorkingDir,
                userTemplateDir));

        return loaders.stream()
                .filter(TemplateDirectoryResolver::isTemplateDirectory)
                .distinct()
                .collect(Collectors.toList());
    }

    private static boolean isTemplateDirectory(File directory) {
        return directory != null && directory.exists() && directory.isDirectory() && directory.canRead();
    }

}
