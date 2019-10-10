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
package com.github.sgoeschl.freemarker.cli;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class MainEncodingTest {

    private static final String CYRILLIC_TERM = "\u041A\u0438\u0440\u0438\u043B\u043B\u0438\u0446\u0430";

    @Test
    public void shouldLoadUtf8Text() throws IOException {
        final String content = execute("-e UTF-8 -t templates/echo.ftl ./src/test/data/encoding/utf8.txt");
        assertTrue("Unable to find expected term", content.contains(CYRILLIC_TERM));
    }

    @Test
    public void shouldLoadUtf16Text() throws IOException {
        final String content = execute("-e UTF-16 -t templates/echo.ftl ./src/test/data/encoding/utf16.txt");
        assertTrue("Unable to find expected term", content.contains(CYRILLIC_TERM));
    }

    private String execute(String line) throws IOException {
        try (final Writer writer = new StringWriter()) {
            final String[] args = line.split(" ");
            if (Main.execute(args, writer) == 0) {
                return writer.toString();
            } else {
                throw new RuntimeException("Executing freemarker-cli failed: " + Arrays.toString(args));
            }
        }
    }
}
