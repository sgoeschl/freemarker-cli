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
package com.github.sgoeschl.freemarker.cli.resolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static com.github.sgoeschl.freemarker.cli.util.ObjectUtils.isNullOrEmtpty;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

/**
 * Resolve a list of files or directories recursively and
 * skip hidden files & directories.
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
        final List<File> result = new ArrayList<>();

        for (String source : sources) {
            final File file = new File(source);
            if (file.isFile()) {
                result.add(file);
            } else {
                result.addAll(resolveDirectory(source));
            }
        }

        return result;
    }

    private List<File> resolveDirectory(String directory) {
        final List<File> result = new ArrayList<>();

        try {
            final Path startDir = Paths.get(directory);
            final FileSystem fileSystem = FileSystems.getDefault();
            final PathMatcher matcher = fileSystem.getPathMatcher("glob:" + includes);
            final FileVisitor<Path> fileVisitor = createFileVisitor(result, matcher);
            Files.walkFileTree(startDir, fileVisitor);
        } catch (IOException e) {
            throw new RuntimeException("Failed to resolve files of the following directory: " + directory, e);
        }

        return result;
    }

    private static SimpleFileVisitor<Path> createFileVisitor(List<File> result, PathMatcher matcher) {
        return new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                final Path name = file.getFileName();
                if (matcher.matches(name) && !isHiddenFile(file, attribs)) {
                    result.add(file.toFile());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
                if (isReferencingCurrentDirectory(dir)) {
                    return super.preVisitDirectory(dir, attr);
                } else if (isHiddenDirectory(dir, attr)) {
                    return FileVisitResult.SKIP_SUBTREE;
                } else {
                    return super.preVisitDirectory(dir, attr);
                }
            }
        };
    }

    private static boolean isReferencingCurrentDirectory(Path dir) {
        final File file = dir.toFile();
        final String name = file.getName();
        return !file.isFile() && (name.equals(".") || name.startsWith("./") || name.startsWith("../"));
    }

    private static boolean isHiddenDirectory(Path dir, BasicFileAttributes attr) throws IOException {
        return Files.isHidden(dir) || hasHiddenFileAttribute(attr);
    }

    private static boolean isHiddenFile(Path file, BasicFileAttributes attr) {
        return fileNameStartsWithDot(file) || hasHiddenFileAttribute(attr);
    }

    private static boolean fileNameStartsWithDot(Path file) {
        return file.toFile().getName().startsWith(".");
    }

    private static boolean hasHiddenFileAttribute(BasicFileAttributes attr) {
        return (attr instanceof DosFileAttributes && ((DosFileAttributes) attr).isHidden());
    }
}
