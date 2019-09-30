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

import com.github.sgoeschl.freemarker.cli.activation.StringDataSource;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.requireNonNull;

/**
 * Source document which encapsulates a data source. When
 * accessing content it is loaded on demand on not kept in memory to
 * allow processing of large volumes of data.
 */
public class Document {

    private final String name;
    private final Charset charset;
    private final DataSource dataSource;

    public Document(String name, String content) {
        this.name = requireNonNull(name);
        this.dataSource = new StringDataSource(name, content);
        this.charset = null;
    }

    public Document(File file) {
        this.name = file.getName();
        this.dataSource = new FileDataSource(file);
        this.charset = defaultCharset();
    }

    public Document(File file, Charset charset) {
        this.name = file.getName();
        this.dataSource = new FileDataSource(file);
        this.charset = charset;
    }

    public Document(DataSource dataSource) {
        this.name = dataSource.getName();
        this.dataSource = dataSource;
        this.charset = null;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        if (dataSource instanceof FileDataSource) {
            return ((FileDataSource) dataSource).getFile().getAbsolutePath();
        } else if (dataSource instanceof URLDataSource) {
            return ((URLDataSource) dataSource).getURL().toExternalForm();
        } else {
            return dataSource.getName();
        }
    }

    public Charset getCharset() {
        return charset;
    }

    public long getLength() {
        if (dataSource instanceof FileDataSource) {
            return ((FileDataSource) dataSource).getFile().length();
        } else if (dataSource instanceof StringDataSource) {
            return ((StringDataSource) dataSource).getContent().length();
        } else {
            return -1;
        }
    }

    public InputStream getInputStream() {
        try {
            return dataSource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get input stream: " + toString(), e);
        }
    }

    @Override
    public String toString() {
        return "Document{" +
                "name='" + name + '\'' +
                ", location=" + getLocation() +
                ", charset='" + charset + '\'' +
                '}';
    }
}
