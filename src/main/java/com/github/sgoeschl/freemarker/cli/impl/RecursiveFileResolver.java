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
package com.github.sgoeschl.freemarker.cli.impl;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.sgoeschl.freemarker.cli.util.ObjectUtils.isNullOrEmtpty;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FileUtils.listFiles;

/**
 * Resolve a list of files or directories recursively and
 * skip hidden files &amp; directories.
 */
public class RecursiveFileResolver {

    private static final String DEFAULT_INCLUDE = "*";

    private final List<String> sources;
    private final String includes;

    public RecursiveFileResolver(String source, String includes) {
        this(singletonList(source), includes);
    }

    public RecursiveFileResolver(List<String> sources, String includes) {
        this.sources = requireNonNull(sources);
        this.includes = isNullOrEmtpty(includes) ? DEFAULT_INCLUDE : includes;
    }

    public List<File> resolve() {
        return sources.stream()
                .map(this::resolve)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<File> resolve(String source) {
        final File file = new File(source);
        if (file.isFile()) {
            return singletonList(file);
        } else {
            return new ArrayList<>(resolveDirectory(file));
        }
    }

    private List<File> resolveDirectory(File directory) {
        return new ArrayList<>(listFiles(directory, fileFilter(), directoryFilter()));
    }

    private IOFileFilter fileFilter() {
        return new AndFileFilter(new WildcardFileFilter(includes), HiddenFileFilter.VISIBLE);
    }

    private static IOFileFilter directoryFilter() {
        return HiddenFileFilter.VISIBLE;
    }
}
