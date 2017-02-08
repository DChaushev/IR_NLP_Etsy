package com.ir.etsy.clusterizer.utils;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import com.ir.etsy.clusterizer.listings.ListingAnalyzer;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.lang3.Validate;
import org.apache.lucene.analysis.Analyzer;
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

    public static IndexWriter createIndex(LuceneDocumentType documentType) throws IOException {
        Path indexDirPath = documentType.getIndexDir();
        Validate.notNull(indexDirPath);
        if (!indexDirPath.toFile().exists()) {
            indexDirPath.toFile().mkdirs();
        }

        Analyzer analyzer;
        if (documentType == LuceneDocumentType.LISTING) {
            analyzer = ListingAnalyzer.getAnalyzer();
        } else {
            analyzer = new StandardAnalyzer();
        }

        Directory dir = FSDirectory.open(indexDirPath);
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return new IndexWriter(dir, cfg);
    }

    public static void closeIndex(Closeable index) throws IOException {
        index.close();
    }

    public static IndexReader getIndexReader(LuceneDocumentType documentType) throws IOException {
        Path indexDirPath = documentType.getIndexDir();
        Validate.notNull(indexDirPath);
        return DirectoryReader.open(FSDirectory.open(indexDirPath));
    }

    public static IndexSearcher getIndexSearcher(IndexReader indexReader) {
        Validate.notNull(indexReader);
        return new IndexSearcher(indexReader);
    }
}
