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
package com.github.sgoeschl.freemarker.cli.tools.jsoup;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class JsoupToolTest {

    private static final String ANY_HTML_CONTENT = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "  <head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <title>title</title>\n" +
            "    <link rel=\"stylesheet\" href=\"style.css\">\n" +
            "    <script src=\"script.js\"></script>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <h1>Hello World</h1>\n" +
            "  </body>\n" +
            "</html>";

    @Test
    public void shallParseStringHtmlContent() {
        assertEquals("Hello World", parse(ANY_HTML_CONTENT).select("h1").first().text());
    }

    @Test
    public void shallReturnNullForUndefinedCSSPath() {
        assertNull(parse(ANY_HTML_CONTENT).select("h2").first());
    }

    private Document parse(String html) {
        return jsoupTool().parse(html);
    }

    private JsoupTool jsoupTool() {
        return new JsoupTool();
    }


}
