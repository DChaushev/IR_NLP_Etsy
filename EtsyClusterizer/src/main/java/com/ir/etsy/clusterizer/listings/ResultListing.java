package com.ir.etsy.clusterizer.listings;

import java.util.Arrays;
import org.apache.lucene.document.Document;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResultListing extends Listing {
    @JsonProperty("score")
    protected float score;
    
    // For JSON serialization
    public ResultListing() {
    }
    
     /**
     * Creates a slim ResultListing from a Document
     *
     * @param doc
     */
    public ResultListing(Document doc, float score) {
        this.listingId = Long.valueOf(doc.get(ListingProperties.LISTING_ID));
        this.title = doc.get(ListingProperties.TITLE);
        this.categoryPathIds = pathAsList(doc);
        this.score = score;
    }
    
    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
