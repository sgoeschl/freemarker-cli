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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class CommonsCsvParserBean {

    public CSVParser parse(Document document, CSVFormat format) {
        try {
            // TODO ensure that the input stream is closed
            return CSVParser.parse(document.getInputStream(), document.getCharset(), format);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV: " + document, e);
        }
    }

    public CommonsCsvRecordReader getReader(Document document, CSVFormat format) {
        return new CommonsCsvRecordReader(parse(document, format).iterator());
    }

    public Map<String, CSVRecord> toMap(CSVParser csvParser, List<CSVRecord> csvRecords, String key) {
        return toMap(csvRecords, csvParser.getHeaderMap().get(key));
    }

    public Map<String, CSVRecord> toMap(List<CSVRecord> csvRecords, Integer index) {
        return csvRecords.stream().collect(Collectors.toMap(r -> r.get(index), r -> r));
    }

    public Set<String> keySet(CSVParser csvParser, List<CSVRecord> csvRecords, String key) {
        return keySet(csvRecords, csvParser.getHeaderMap().get(key));
    }

    public Set<String> keySet(List<CSVRecord> csvRecords, Integer index) {
        return csvRecords.stream().map(r -> r.get(index)).collect(toSet());
    }
}
