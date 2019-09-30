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
package com.github.sgoeschl.freemarker.cli.model;

import org.junit.Test;

import java.io.File;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentsTest {

    private static final String UNKNOWN = "unknown";
    private static final String ANY_TEXT = "Hello World";
    private static final String ANY_FILE_NAME = "pom.xml";
    private static final String ANY_FILE_EXTENSION = "xml";
    private static final File ANY_FILE = new File(ANY_FILE_NAME);

    @Test
    public void shouldFindByName() {
        final Documents documents = documents();

        assertEquals(1, documents.findByName(ANY_FILE_NAME).size());
        assertEquals(0, documents.findByName(UNKNOWN).size());
        assertEquals(0, documents.findByName("").size());
        assertEquals(0, documents.findByName(null).size());
    }

    @Test
    public void shouldFindByExtension() {
        final Documents documents = documents();

        assertEquals(1, documents.findByExtension(ANY_FILE_EXTENSION).size());
        assertEquals(ANY_FILE_NAME, documents.findByExtension(ANY_FILE_EXTENSION).get(0).getName());
        assertEquals(0, documents.findByExtension(UNKNOWN).size());
        assertEquals(0, documents.findByExtension("").size());
        assertEquals(0, documents.findByExtension(null).size());
    }

    @Test
    public void shouldGetByName() {
        final Documents documents = documents();

        assertEquals(ANY_FILE_NAME, documents.get(ANY_FILE_NAME).getName());
        assertNull(documents.get(UNKNOWN));
        assertNull(documents.get(""));
        assertNull(documents.get(null));
    }

    @Test
    public void shouldGetAllDocuments() {
        assertEquals(2, documents().getAll().size());
    }

    @Test
    public void shouldGetNames() {
        assertEquals(asList("stdin", "pom.xml"), documents().getNames());
    }

    private static Documents documents() {
        return new Documents(asList(textDocument(), fileDocument()));
    }

    private static Document textDocument() {
        return new Document("stdin", ANY_TEXT);
    }

    private static Document fileDocument() {
        return new Document(ANY_FILE);
    }
}
