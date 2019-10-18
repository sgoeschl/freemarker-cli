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
import com.github.sgoeschl.freemarker.cli.model.Settings;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.sgoeschl.freemarker.cli.util.ObjectUtils.isNullOrEmtpty;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class CommonsCsvTool {

    private final Settings settings;

    public CommonsCsvTool(Settings settings) {
        this.settings = requireNonNull(settings);
    }

    public CSVParser parse(Document document, CSVFormat format) {
        if (document == null) {
            throw new IllegalArgumentException("No document was provided");
        }

        try {
            // TODO ensure that the input stream is closed?!
            final BOMInputStream bomInputStream = new BOMInputStream(document.getInputStream(), false);
            return CSVParser.parse(bomInputStream, document.getCharset(), format);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV: " + document, e);
        }
    }

    /**
     * Extract the list of unique values (keys) of the column "name".
     *
     * @param records records to process
     * @param name    column name to process
     */
    public List<String> toKeys(Collection<CSVRecord> records, String name) {
        return toKeys(records, new ValueResolver(name));
    }

    /**
     * Extract the list of unique values (keys) of the column with the given index..
     *
     * @param records records to process
     * @param index   column index to map
     */
    public List<String> toKeys(Collection<CSVRecord> records, Integer index) {
        return toKeys(records, new ValueResolver(index));
    }

    /**
     * Map the given value of the CVS record into (key -> record). If duplicates
     * are encountered return the first occurence of the CVS record. The map
     * retains the insertion order of they keys.
     *
     * @param records records to process
     * @param name    column name to map
     */
    public Map<String, CSVRecord> toMap(Collection<CSVRecord> records, String name) {
        return toMap(records, new ValueResolver(name));
    }

    /**
     * Map the given value of the CVS record into (key -> record). If duplicates
     * are encountered return the first occurence of the CVS record. The map
     * retains the insertion order of they keys.
     *
     * @param records records to process
     * @param index   column index to map
     */
    public Map<String, CSVRecord> toMap(Collection<CSVRecord> records, Integer index) {
        return toMap(records, new ValueResolver(index));
    }

    /**
     * Map the given value of the CVS record into a list of records.
     *
     * @param records records to process
     * @param name    column name to map
     */
    public Map<String, List<CSVRecord>> toMultiMap(Collection<CSVRecord> records, String name) {
        return toMultiMap(records, new ValueResolver(name));
    }

    /**
     * Map the given value of the CVS record into a list of records.
     *
     * @param records records to process
     * @param index   column index to map
     */
    public Map<String, List<CSVRecord>> toMultiMap(Collection<CSVRecord> records, Integer index) {
        return toMultiMap(records, new ValueResolver(index));
    }

    /**
     * Get a CSVPrinter using the FreeMarker's writer instance.
     *
     * @param csvFormat CSV format to use for writing records
     */
    public CSVPrinter printer(CSVFormat csvFormat) throws IOException {
        return new CSVPrinter(getSettings().getWriter(), csvFormat);
    }

    /**
     * Maps the sybmolic name of a delimiter to a single character since it
     * is not possible to define commony used delimiters on the command line.
     *
     * @param name symbolic name of delimiter
     */
    public char toDelimiter(String name) {
        if (isNullOrEmtpty(name)) {
            throw new IllegalArgumentException("Now CSV delimiter provided");
        }

        switch (name.toUpperCase().trim()) {
            case "COMMA":
                return ',';
            case "HASH":
                return '#';
            case "PIPE":
                return '|';
            case "RS":
                return 30;
            case "SEMICOLON":
                return ';';
            case "SPACE":
                return ' ';
            case "TAB":
                return '\t';
            default:
                if (name.length() == 1) {
                    return name.charAt(0);
                } else {
                    throw new IllegalArgumentException("Unsupported CSV delimiter: " + name);
                }
        }
    }

    private List<String> toKeys(Collection<CSVRecord> csvRecords, Function<CSVRecord, String> value) {
        return csvRecords.stream()
                .map(value)
                .distinct()
                .collect(toList());
    }

    private Map<String, CSVRecord> toMap(Collection<CSVRecord> records, Function<CSVRecord, String> value) {
        return records.stream()
                .collect(Collectors.toMap(
                        value,
                        record -> record,
                        (firstKey, currentKey) -> firstKey,
                        LinkedHashMap::new
                ));
    }

    private Map<String, List<CSVRecord>> toMultiMap(Collection<CSVRecord> records, Function<CSVRecord, String> value) {
        final Map<String, List<CSVRecord>> result = new LinkedHashMap<>();
        final List<String> keys = toKeys(records, value);
        keys.forEach(key -> result.put(key, new ArrayList<>()));
        records.forEach(record -> result.get(value.apply(record)).add(record));
        return result;
    }

    Settings getSettings() {
        return settings;
    }

    private static final class ValueResolver implements Function<CSVRecord, String> {

        private final Integer index;
        private final String name;

        public ValueResolver(Integer index) {
            this.index = requireNonNull(index);
            this.name = null;
        }

        public ValueResolver(String name) {
            this.index = null;
            this.name = requireNonNull(name);
        }

        @Override
        public String apply(CSVRecord record) {
            return index != null ? record.get(index) : record.get(name);
        }
    }

}
