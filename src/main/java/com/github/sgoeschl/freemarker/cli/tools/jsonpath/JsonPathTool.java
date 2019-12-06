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

import com.github.sgoeschl.freemarker.cli.model.Document;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class JsonPathTool {

    public JsonPathTool(Map<String, Object> settings) {
        requireNonNull(settings);
    }

    public DocumentContext parse(Document document) throws IOException {
        try (InputStream is = document.getInputStream()) {
            return JsonPath.parse(is);
        }
    }

    public DocumentContext parse(String json) {
        return JsonPath.parse(json);
    }

}
