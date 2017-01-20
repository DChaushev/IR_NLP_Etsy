package com.ir.etsy.clusterizer.utils;

import com.ir.etsy.clusterizer.listings.ListingAnalyzer;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.Validate;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Dimitar
 */
public class LuceneIndexUtils {

    public static IndexWriter createIndex(String indexDir) throws IOException {
        Validate.notEmpty(indexDir);
        return createIndex(new File(indexDir));
    }

    public static IndexWriter createIndex(File indexDir) throws IOException {
        Validate.notNull(indexDir);
        Directory dir = FSDirectory.open(indexDir.toPath());
        Analyzer analyzer = ListingAnalyzer.getAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return new IndexWriter(dir, cfg);
    }

    public static void closeIndex(Closeable index) throws IOException {
        index.close();
    }

    public static IndexReader getIndexReader(String indexDir) throws IOException {
        Validate.notEmpty(indexDir);
        return getIndexReader(new File(indexDir));
    }

    public static IndexReader getIndexReader(File indexDir) throws IOException {
        Validate.notNull(indexDir);
        return DirectoryReader.open(FSDirectory.open(indexDir.toPath()));
    }

    public static IndexSearcher getIndexSearcher(IndexReader indexReader) {
        Validate.notNull(indexReader);
        return new IndexSearcher(indexReader);
    }
}
