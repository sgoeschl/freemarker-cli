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

import org.apache.commons.io.output.NullWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TemplateLoadingTest extends AbstractMainTest {

    private static final int SUCCESS = 0;
    private static final String ANY_TEMPLATE_NAME = "templates/info.ftl";
    private static final String CURR_DIR = System.getProperty("user.dir", ".");

    @Test
    public void shouldLoadRelativeTemplate() throws IOException {
        final String[] args = new String[] { "-t", ANY_TEMPLATE_NAME };

        assertEquals(SUCCESS, Main.execute(args, new NullWriter()));
    }

    @Test
    public void shouldLoadAbsoluteTemplate() throws IOException {
        final String absoluteFileName = new File(CURR_DIR, ANY_TEMPLATE_NAME).getAbsolutePath();
        final String[] args = new String[] { "-t", absoluteFileName };

        assertEquals(SUCCESS, Main.execute(args, new NullWriter()));
    }
}
