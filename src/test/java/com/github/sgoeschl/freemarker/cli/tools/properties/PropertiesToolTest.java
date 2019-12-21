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
package com.github.sgoeschl.freemarker.cli.tools.properties;

import com.github.sgoeschl.freemarker.cli.impl.DocumentFactory;
import com.github.sgoeschl.freemarker.cli.model.Document;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class PropertiesToolTest {

    private static final String ANY_PROPERTIES_STRING = "foo=bar";

    @Test
    public void shallParsePropertiesDocument() {
        try (Document document = document(ANY_PROPERTIES_STRING)) {
            assertEquals("bar", propertiesTool().parse(document).getProperty("foo"));
        }
    }

    @Test
    public void shallParsePropertiesString() {
        assertEquals("bar", propertiesTool().parse(ANY_PROPERTIES_STRING).getProperty("foo"));
    }

    private PropertiesTool propertiesTool() {
        return new PropertiesTool();
    }

    private Document document(String value) {
        return DocumentFactory.create("test.properties", value);
    }
}
