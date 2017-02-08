package com.ir.etsy.clusterizer;

import com.ir.etsy.clusterizer.categories.Category;
import com.ir.etsy.clusterizer.listings.Listing;
import com.ir.etsy.clusterizer.listings.ListingState;
import com.ir.etsy.clusterizer.utils.EtsyUnmarshaller;
import com.ir.etsy.clusterizer.utils.IOUtils;
import com.ir.etsy.clusterizer.utils.LuceneIndexUtils;
import com.ir.etsy.clusterizer.utils.LuceneDocumentType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.index.IndexWriter;

/**
 *
 * @author Dimitar
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if (LuceneDocumentType.BASE_INDEX_DIR.isEmpty() || LuceneDocumentType.BASE_DATA_DIR.isEmpty()) {
            throw new Error("Both LuceneDocumentType.BASE_INDEX_DIR and LuceneDocumentType.BASE_DATA_DIR" +
                    " should be set to the correct directories before starting the indexing operation.");
        }
        
        indexListingDocuments();
        indexCategoryDocuments();
    }

    private static void indexListingDocuments() throws IOException {
        File[] listingFiles = IOUtils.getAllFiles(LuceneDocumentType.LISTING.getDataDir());
        IndexWriter listingsIndexWriter = LuceneIndexUtils.createIndex(LuceneDocumentType.LISTING);
        
        for (File file : listingFiles) {
            System.out.println("Reading file " + file.getName());
            List<Listing> listings = EtsyUnmarshaller.getListings(file);
            for (Listing listing : listings) {
                // Strangely, there are some non-active items
                if (listing.getState() == ListingState.ACTIVE) {
                    listingsIndexWriter.addDocument(listing.toDocument());
                }
            }
        }
        
        LuceneIndexUtils.closeIndex(listingsIndexWriter);
    }
    
    private static void indexCategoryDocuments() throws IOException {
        File[] categoryFiles = IOUtils.getAllFiles(LuceneDocumentType.CATEGORY.getDataDir());
        IndexWriter categoriesIndexWriter = LuceneIndexUtils.createIndex(LuceneDocumentType.CATEGORY);
        
        for (File file : categoryFiles) {
            System.out.println("Reading file " + file.getName());
            List<Category> categories = EtsyUnmarshaller.getCategories(file);
            for (Category category : categories) {
                categoriesIndexWriter.addDocument(category.toDocument());
            }
        }
        
        LuceneIndexUtils.closeIndex(categoriesIndexWriter);
    }
}
