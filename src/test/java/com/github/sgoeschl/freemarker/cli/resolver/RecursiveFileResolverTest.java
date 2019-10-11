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

import org.junit.Test;

import java.io.File;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RecursiveFileResolverTest {

    private static final String ANY_DIRECTORY = ".";
    private static final String ANY_FILE_NAME = "pom.xml";
    private static final String UNKNOWN_FILE_NAME = "unknown.file";

    @Test
    public void shouldResolveAllFilesOfDirectory() {
        assertTrue(fileResolver(ANY_DIRECTORY, null).resolve().size() > 100);
        assertTrue(fileResolver(ANY_DIRECTORY, UNKNOWN_FILE_NAME).resolve().isEmpty());
    }

    @Test
    public void shouldResolveSingleMatchingFile() {
        final List<File> files = fileResolver(ANY_DIRECTORY, ANY_FILE_NAME).resolve();

        assertEquals(1, files.size());
        assertEquals(ANY_FILE_NAME, files.get(0).getName());
    }

    @Test
    public void shouldResolveMultipleFilesRecursivelyWithIncludes() {
        final List<File> files = fileResolver("./site", "*.csv").resolve();

        assertEquals(5, files.size());
    }

    private static RecursiveFileResolver fileResolver(String directory, String include) {
        return new RecursiveFileResolver(singletonList(directory), include);
    }
}
