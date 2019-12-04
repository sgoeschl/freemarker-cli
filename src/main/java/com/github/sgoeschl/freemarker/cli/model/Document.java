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
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import static com.github.sgoeschl.freemarker.cli.util.ClosableUtils.closeQuietly;
import static java.nio.charset.Charset.forName;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.io.IOUtils.lineIterator;

/**
 * Source document which encapsulates a data source. When
 * accessing content it is loaded on demand on not kept in memory to
 * allow processing of large volumes of data.
 */
public class Document {

    private static final int UKNOWN_LENGTH = -1;

    /** Human-radable name of the document */
    private final String name;

    /** Charset for directly accessing text-based content */
    private final Charset charset;

    /** The data source */
    private final DataSource dataSource;

    /** The location of the content */
    private final String location;

    public Document(String name, DataSource dataSource, String location, Charset charset) {
        this.name = requireNonNull(name);
        this.dataSource = requireNonNull(dataSource);
        this.location = requireNonNull(location);
        this.charset = requireNonNull(charset);
    }

    public String getName() {
        return name;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Try to get the length lazily, efficient and without consuming the input stream.
     *
     * @return Length of document or "-1"
     */
    public long getLength() {
        if (dataSource instanceof FileDataSource) {
            return ((FileDataSource) dataSource).getFile().length();
        } else if (dataSource instanceof StringDataSource) {
            return ((StringDataSource) dataSource).getContent().length();
        } else {
            return UKNOWN_LENGTH;
        }
    }

    /**
     * The caller is responsible to close the input stream.
     *
     * @return input stream
     */
    public InputStream getInputStream() throws IOException {
        return dataSource.getInputStream();
    }

    public String getText() throws IOException {
        return getText(getCharset().name());
    }

    public String getText(String charsetName) throws IOException {
        final StringWriter writer = new StringWriter();
        try (InputStream is = getInputStream()) {
            IOUtils.copy(is, writer, forName(charsetName));
            return writer.toString();
        }
    }

    /**
     * Returns an Iterator for the lines in an <code>InputStream</code>, using
     * the default character encoding specified. The caller is responsible to close
     * the line iterator.
     *
     * @return line iterator
     */
    public LineIterator getLineIterator() throws IOException {
        return getLineIterator(getCharset().name());
    }

    /**
     * Returns an Iterator for the lines in an <code>InputStream</code>, using
     * the character encoding specified. The caller is responsible to close
     * the line iterator.
     *
     * @param charsetName The name of the requested charset; may be either
     *                    a canonical name or an alias
     * @return line iterator
     */
    public LineIterator getLineIterator(String charsetName) throws IOException {
        return lineIterator(getInputStream(), forName(charsetName));
    }

    /**
     * Convinience method to close a Closable.
     *
     * @param closeable The closable
     */
    public void close(Closeable closeable) {
        closeQuietly(closeable);
    }

    @Override
    public String toString() {
        return "Document{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", charset='" + charset + '\'' +
                '}';
    }
}
