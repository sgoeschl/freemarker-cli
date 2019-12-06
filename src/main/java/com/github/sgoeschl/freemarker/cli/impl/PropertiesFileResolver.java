package com.github.sgoeschl.freemarker.cli.impl;

import com.github.sgoeschl.freemarker.cli.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

/**
 * Resolve a given properties file either from the file system or the classapth.
 */
public class PropertiesFileResolver {

    private final String fileName;

    public PropertiesFileResolver(String fileName) {
        this.fileName = requireNonNull(fileName);
    }

    public Properties resolve() throws IOException {
        return resolveFromFileSystemOrClassPath(fileName);
    }

    private static Properties resolveFromFileSystemOrClassPath(String fileName) throws IOException {
        final Properties result = resolveFromFileSystem(fileName);
        return result != null ? result : resolveFromClassPath(fileName);
    }

    private static Properties resolveFromClassPath(String fileName) throws IOException {
        final Properties properties = new Properties();
        final InputStream stream = Main.class.getResourceAsStream("/" + fileName);

        if (stream == null) {
            return null;
        }

        try {
            properties.load(stream);
            return properties;
        } finally {
            stream.close();
        }

    }

    private static Properties resolveFromFileSystem(String fileName) throws IOException {
        if (!new File(fileName).exists()) {
            return null;
        }

        try (InputStream input = new FileInputStream("fileName")) {
            final Properties properties = new Properties();
            properties.load(input);
            return properties;
        }
    }
}
