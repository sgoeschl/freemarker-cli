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
package com.github.sgoeschl.freemarker.cli.model;

import com.github.sgoeschl.freemarker.cli.util.IOUtils;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * Container for documents with a couple of convinience functions to select
 * a subset of documents.
 */
public class Documents {

    private final List<Document> documents;

    public Documents(List<Document> documents) {
        this.documents = requireNonNull(documents);
    }

    public List<String> getNames() {
        return documents.stream()
                .map(Document::getName)
                .collect(toList());
    }

    public int size() {
        return documents.size();
    }

    public boolean isEmpty() {
        return documents == null || documents.isEmpty();
    }

    public List<Document> getAll() {
        return documents;
    }

    public Document get(int index) {
        return documents.get(index);
    }

    public Document get(String name) {
        final List<Document> list = findByName(name);
        return (list.isEmpty() ? null : list.get(0));
    }

    public List<Document> findByName(String name) {
        if (isNullOrEmtpty(name)) {
            return emptyList();
        }

        return documents.stream()
                .filter(d -> name.equals(d.getName()))
                .collect(toList());
    }

    public List<Document> findByExtension(String extension) {
        if (isNullOrEmtpty(extension)) {
            return emptyList();
        }

        return documents.stream()
                .filter(d -> extension.equalsIgnoreCase(IOUtils.getFileExtension(d.getName())))
                .collect(toList());
    }

    @Override
    public String toString() {
        return "Documents{" +
                "documents=" + documents +
                '}';
    }

    private static boolean isNullOrEmtpty(String value) {
        return (value == null) || value.trim().isEmpty();
    }

}
