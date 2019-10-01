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
package com.github.sgoeschl.freemarker.cli.tools.commonscsv;

import com.github.sgoeschl.freemarker.cli.model.Document;
import com.github.sgoeschl.freemarker.cli.util.StreamUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CommonsCsvParserBean {

    public CSVParser parse(Document document, CSVFormat format) {
        try {
            // The input stream would be closed by CSVParser#close but it
            // is unlikely to be called so we load the file into a byte[].
            final ByteArrayInputStream bais = new ByteArrayInputStream(StreamUtils.toByteArray(document.getInputStream()));
            return CSVParser.parse(bais, document.getCharset(), format);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV: " + document, e);
        }
    }
}
