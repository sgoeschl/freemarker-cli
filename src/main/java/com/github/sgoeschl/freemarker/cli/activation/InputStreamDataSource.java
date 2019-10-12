package com.github.sgoeschl.freemarker.cli.activation;

import javax.activation.DataSource;
import java.io.InputStream;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

public class InputStreamDataSource implements DataSource {

    private final String name;
    private final InputStream is;

    public InputStreamDataSource(String name, InputStream is) {
        this.name = requireNonNull(name);
        this.is = requireNonNull(is);
    }

    @Override
    public InputStream getInputStream() {
        return is;
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public String getContentType() {
        return "application/octet-stream";
    }

    @Override
    public String getName() {
        return name;
    }
}

