package com.ir.etsy.clusterizer;

import com.ir.etsy.clusterizer.listings.Listing;
import com.ir.etsy.clusterizer.listings.ListingState;
import com.ir.etsy.clusterizer.utils.EtsyListingUnmarshaller;
import com.ir.etsy.clusterizer.utils.IOUtils;
import com.ir.etsy.clusterizer.utils.ListingProperties;
import com.ir.etsy.clusterizer.utils.LuceneIndexUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author Dimitar
 */
public class Main {
    
    private static final String EXAMPLE_LISTINGS_FOLDER = "";
    private static final String INDEX_DIR = "";

    public static void main(String[] args) throws IOException {
        File[] files = IOUtils.getAllFiles(EXAMPLE_LISTINGS_FOLDER);
        IndexWriter indexWriter = LuceneIndexUtils.createIndex(INDEX_DIR);
        addDocuments(files, indexWriter);
        LuceneIndexUtils.closeIndex(indexWriter);

        IndexReader indexReader = LuceneIndexUtils.getIndexReader(INDEX_DIR);
        IndexSearcher indexSearcher = LuceneIndexUtils.getIndexSearcher(indexReader);

        Query query = new PhraseQuery(ListingProperties.TITLE, "chess", "board");

        TopDocs results = indexSearcher.search(query, 100);

        System.out.println("Found " + results.scoreDocs.length + " hits.");
        
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get(ListingProperties.TITLE));
        }

        LuceneIndexUtils.closeIndex(indexReader);
    }

    private static void addDocuments(File[] files, IndexWriter indexWriter) throws IOException {
        for (File file : files) {
            System.out.println("Reading file " + file.getName());
            List<Listing> listings = EtsyListingUnmarshaller.getListings(file);
            for (Listing listing : listings) {
                // Strangely, there are some non-active items
                if (listing.getState() == ListingState.ACTIVE) {
                    indexWriter.addDocument(listing.toDocument());
                }
            }
        }
    }
}
