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

import java.nio.charset.Charset;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class DocumentsResolverTest {

    private static final String ANY_DIRECTORY = "./site/sample";
    private static final String ANY_FILE_NAME = "test.xls";
    private static final String ANY_INCLUDE = "*.*";
    private static final String UNKNOWN_FILE_NAME = "unknown.file";

    @Test
    public void shouldResolveFilesOfDirectory() {
        assertEquals(19, resolver(ANY_DIRECTORY, null).resolve().size());
        assertEquals(19, resolver(ANY_DIRECTORY, "").resolve().size());
        assertEquals(19, resolver(ANY_DIRECTORY, "*").resolve().size());
        assertEquals(5, resolver(ANY_DIRECTORY, "*.csv").resolve().size());
        assertEquals(4, resolver(ANY_DIRECTORY, "*.x*").resolve().size());
    }

    @Test(expected = RuntimeException.class)
    public void shoulThrowExceptionForNonexistingSourceDirectorly() {
        assertEquals(0, resolver("/does-not-exist", null).resolve().size());
    }

    private static DocumentResolver resolver(String directory, String include) {
        return new DocumentResolver(singletonList(directory), include, Charset.defaultCharset());
    }
}
