package com.github.sgoeschl.freemarker.cli;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {

    private static final int MIN_OUTPUT_SIZE = 128;

    @Test
    public void shouldRunDemoExamples() throws IOException {
        assertValid(execute("-t templates/demo.ftl README.md"));
    }

    @Test
    public void shouldRunCsvExamples() throws IOException {
        assertValid(execute("-t templates/csv/html/transform.ftl site/sample/csv/contract.csv"));
        assertValid(execute("-t templates/csv/md/transform.ftl site/sample/csv/contract.csv"));
        assertValid(execute("-t templates/csv/shell/curl.ftl site/sample/csv/user.csv"));
        assertValid(execute("-t templates/csv/fo/transform.ftl site/sample/csv/locker-test-users.csv"));
        assertValid(execute("-t templates/csv/fo/transactions.ftl site/sample/csv/transactions.csv"));
        assertValid(execute("-t templates/csv/html/transactions.ftl site/sample/csv/transactions.csv"));
    }

    @Test
    public void shouldRunExcelExamples() throws IOException {
        assertValid(execute("-t templates/excel/html/transform.ftl site/sample/excel/test.xls"));
        assertValid(execute("-t templates/excel/html/transform.ftl site/sample/excel/test.xlsx"));
        assertValid(execute("-t templates/excel/html/transform.ftl site/sample/excel/test-multiple-sheets.xlsx"));
        assertValid(execute("-t templates/excel/md/transform.ftl site/sample/excel/test-multiple-sheets.xlsx"));
        assertValid(execute("-t templates/excel/csv/transform.ftl site/sample/excel/test-multiple-sheets.xlsx"));
    }

    @Test
    public void shouldRunHtmlExamples() throws IOException {
        assertValid(execute("-t templates/html/csv/dependencies.ftl site/sample/html/dependencies.html"));
    }

    @Test
    public void shouldRunJsonExamples() throws IOException {
        assertValid(execute("-t templates/json/csv/swagger-endpoints.ftl site/sample/json/swagger-spec.json"));
        assertValid(execute("-t templates/json/html/customer-user-products.ftl site/sample/json/customer-user-products.json"));
        assertValid(execute("-t templates/json/md/customer-user-products.ftl site/sample/json/customer-user-products.json"));
        assertValid(execute("-t templates/json/md/github-users.ftl site/sample/json/github-users.json"));
    }

    @Test
    public void shouldRunPropertiesExamples() throws IOException {
        assertValid(execute("-t templates/properties/csv/locker-test-users.ftl site/sample/properties"));
    }

    @Test
    public void shouldRunYamlExamples() throws IOException {
        assertValid(execute("-t ./templates/yaml/txt/transform.ftl ./site/sample/yaml/customer.yaml"));
    }

    @Test
    public void shouldRunXmlExamples() throws IOException {
        assertValid(execute("-t ./templates/xml/txt/recipients.ftl site/sample/xml/recipients.xml"));
    }

    private String execute(String line) throws IOException {
        final File file = tempFile();
        final String[] args = line.split(" ");
        try {
            if (Main.execute(addOutputFileToArgs(args, file)) == 0) {
                return FileUtils.readFileToString(file, "UTF-8");
            }
            throw new RuntimeException("Executing freemarker-cli failed: " + Arrays.toString(args));
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }

    private File tempFile() throws IOException {
        final File file = File.createTempFile("freemarker-cli", null);
        file.deleteOnExit();
        return file;
    }

    private String[] addOutputFileToArgs(String[] args, File file) {
        final List<String> list = new ArrayList<>(asList(args));
        list.add("-o");
        list.add(file.getAbsolutePath());
        return list.toArray(new String[0]);
    }

    private static void assertValid(String output) {
        assertTrue(output.length() > MIN_OUTPUT_SIZE);
        assertFalse(output.contains("Exception"));
    }
}
