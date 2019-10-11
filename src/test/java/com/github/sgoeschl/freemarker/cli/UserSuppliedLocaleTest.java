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

import static org.junit.Assert.assertTrue;

public class UserSuppliedLocaleTest extends AbstractMainTest {

    @Test
    public void shouldUserGermanLocalForRenderingTemplate() throws IOException {
        final String content = execute("-b ./src/test -l de -t templates/locale.ftl README.md");

        assertTrue(content.contains("3,142"));
        assertTrue(content.contains("99.999,99"));
    }

    @Test
    public void shouldUserEnglishLocalForRenderingTemplate() throws IOException {
        final String content = execute("-b ./src/test -l en -t templates/locale.ftl README.md");

        assertTrue(content.contains("3.142"));
        assertTrue(content.contains("99,999.99"));
    }
}
