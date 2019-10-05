package com.github.sgoeschl.freemarker.cli.tools.commonscsv;

import org.apache.commons.csv.CSVRecord;

import java.util.Iterator;

/**
 * Ungly hack to avoid materializing all CSVRecords in memory while
 * still working with FreeMarker. I tried to enable "iterableSupport"
 * to handle CSVParser as "Iterable" but CVSRecord also exposes "Iterable"
 * to I got a collection instead of map (in FreeMarker speak).
 */
public class CommonsCSVRecordReader {

    private final Iterator<CSVRecord> iterator;

    public CommonsCSVRecordReader(Iterator<CSVRecord> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public CSVRecord getNext() {
        return iterator.next();
    }
}
