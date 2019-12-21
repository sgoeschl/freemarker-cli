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
package com.github.sgoeschl.freemarker.cli.tools.jsonpath;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class JsonPathToolTest {

    private static final String JSON_OBJECT_STRING = "{\n" +
            "\"id\": 110,\n" +
            "\"language\": \"Python\",\n" +
            "\"price\": 1900,\n" +
            "}";

    private static final String JSON_ARRAY_STRING = "{\n" +
            "   \"eBooks\":[\n" +
            "      {\n" +
            "         \"language\":\"Pascal\",\n" +
            "         \"edition\":\"third\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"language\":\"Python\",\n" +
            "         \"edition\":\"four\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"language\":\"SQL\",\n" +
            "         \"edition\":\"second\"\n" +
            "      }\n" +
            "   ]\n" +
            "}";

    @Test
    public void shallParseJsonObject() {
        assertEquals(parse(JSON_OBJECT_STRING).read("$.language"), "Python");
        assertEquals(parse(JSON_OBJECT_STRING).read("$['language']"), "Python");
    }

    @Test
    public void shallParseJsonArray() {
        assertEquals(parse(JSON_ARRAY_STRING).read("$.eBooks[0].language"), "Pascal");
        assertEquals(parse(JSON_ARRAY_STRING).read("$['eBooks'][0]['language']"), "Pascal");
    }

    @Test
    public void shallSuppressExceptionForUnknonwPath() {
        assertNull(parse(JSON_OBJECT_STRING).read("$.unknown"));
    }

    private DocumentContext parse(String json) {
        return jsonPathTool().parse(json);
    }

    private JsonPathTool jsonPathTool() {
        return new JsonPathTool();
    }
}
