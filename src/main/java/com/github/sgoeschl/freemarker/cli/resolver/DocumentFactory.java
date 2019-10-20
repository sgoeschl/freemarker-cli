package com.github.sgoeschl.freemarker.cli.resolver;

import com.github.sgoeschl.freemarker.cli.activation.InputStreamDataSource;
import com.github.sgoeschl.freemarker.cli.activation.StringDataSource;
import com.github.sgoeschl.freemarker.cli.model.Document;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DocumentFactory {

    private static final String STRING_LOCATION = "string";
    private static final String NAME_URL = "url";

    private DocumentFactory() {
    }

    public static Document create(URL url) {
        final URLDataSource dataSource = new URLDataSource(url);
        return create(NAME_URL, dataSource, url.toExternalForm(), UTF_8);
    }

    public static Document create(String name, String content) {
        final StringDataSource dataSource = new StringDataSource(name, content, UTF_8);
        return create(name, dataSource, STRING_LOCATION, UTF_8);
    }

    public static Document create(File file, Charset charset) {
        final FileDataSource dataSource = new FileDataSource(file);
        return create(file.getName(), dataSource, file.getAbsolutePath(), charset);
    }

    public static Document create(String name, InputStream is, String location, Charset charset) {
        final InputStreamDataSource dataSource = new InputStreamDataSource(name, is);
        return create(name, dataSource, location, charset);
    }

    public static Document create(String name, DataSource dataSource, String location, Charset charset) {
        return new Document(name, dataSource, location, charset);
    }
}
