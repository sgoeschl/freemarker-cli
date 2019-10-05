package com.github.sgoeschl.freemarker.cli.tools.commonscsv;

import org.apache.commons.csv.CSVRecord;

import java.util.Iterator;

public class CSVRecordReader {

    private final Iterator<CSVRecord> iterator;

    public CSVRecordReader(Iterator<CSVRecord> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public CSVRecord getNext() {
        return iterator.next();
    }
}
