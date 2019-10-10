package com.github.sgoeschl.freemarker.cli;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class MainEncodingTest {

    private static final String CYRILLIC_TERM = "\u041A\u0438\u0440\u0438\u043B\u043B\u0438\u0446\u0430";

    @Test
    public void shouldLoadUtf8Text() throws IOException {
        final String content = execute("-e UTF-8 -t templates/echo.ftl ./src/test/data/encoding/utf8.txt");
        assertTrue("Unable to find expected term", content.contains(CYRILLIC_TERM));
    }

    @Test
    public void shouldLoadUtf16Text() throws IOException {
        final String content = execute("-e UTF-16 -t templates/echo.ftl ./src/test/data/encoding/utf16.txt");
        assertTrue("Unable to find expected term", content.contains(CYRILLIC_TERM));
    }

    private String execute(String line) throws IOException {
        try (final Writer writer = new StringWriter()) {
            final String[] args = line.split(" ");
            if (Main.execute(args, writer) == 0) {
                return writer.toString();
            } else {
                throw new RuntimeException("Executing freemarker-cli failed: " + Arrays.toString(args));
            }
        }
    }
}
