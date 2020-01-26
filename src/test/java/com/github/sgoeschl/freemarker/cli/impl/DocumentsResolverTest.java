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

import org.junit.Test;

import java.nio.charset.Charset;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class DocumentsResolverTest {

    private static final String ANY_DIRECTORY = "./site/sample";

    @Test
    public void shouldResolveFilesOfDirectory() {
        assertEquals(18, supplier(ANY_DIRECTORY, null).get().size());
        assertEquals(18, supplier(ANY_DIRECTORY, "").get().size());
        assertEquals(18, supplier(ANY_DIRECTORY, "*").get().size());
        assertEquals(5, supplier(ANY_DIRECTORY, "*.csv").get().size());
        assertEquals(4, supplier(ANY_DIRECTORY, "*.x*").get().size());
    }

    @Test(expected = RuntimeException.class)
    public void shoulThrowExceptionForNonexistingSourceDirectory() {
        assertEquals(0, supplier("/does-not-exist", null).get().size());
    }

    private static DocumentSupplier supplier(String directory, String include) {
        return new DocumentSupplier(singletonList(directory), include, Charset.defaultCharset());
    }
}
