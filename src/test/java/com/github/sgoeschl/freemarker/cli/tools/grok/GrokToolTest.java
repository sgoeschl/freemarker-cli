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
package com.github.sgoeschl.freemarker.cli.tools.grok;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GrokToolTest {

    private static final String LOG = "112.169.19.192 - - [06/Mar/2013:01:36:30 +0900] \"GET / HTTP/1.1\" 200 44346 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.152 Safari/537.22\"";

    @Test
    public void shallParseCombinedAccessLog() {
        final GrokWrapper grok = grokTool().compile("%{COMBINEDAPACHELOG}");

        final Map<String, Object> map = grok.match(LOG);

        assertEquals("GET", map.get("verb"));
        assertEquals("06/Mar/2013:01:36:30 +0900", map.get("timestamp"));
        assertEquals("44346", map.get("bytes"));
        assertEquals("/", map.get("request"));
        assertEquals("1.1", map.get("httpversion"));
    }

    private GrokTool grokTool() {
        return new GrokTool(new HashMap<>());
    }
}
