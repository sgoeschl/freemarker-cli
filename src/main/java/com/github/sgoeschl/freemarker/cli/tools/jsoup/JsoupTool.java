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

import com.github.sgoeschl.freemarker.cli.model.Document;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;

public class JsoupTool {

    public org.jsoup.nodes.Document parse(Document document) {
        try (InputStream is = document.getInputStream()) {
            final byte[] bytes = IOUtils.toByteArray(is);
            return Jsoup.parse(new String(bytes, document.getCharset()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse HTML document: " + document, e);
        }
    }
}
