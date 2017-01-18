package com.ir.etsy.clusterizer;

import com.ir.etsy.clusterizer.utils.EtsyListingUnmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Dimitar
 */
public class Main {

    private static final String EXAMPLE_JSON = ""; /* Add json file path */


    public static void main(String[] args) throws IOException {
        List<Listing> listings = EtsyListingUnmarshaller.getListings(new File(EXAMPLE_JSON));
        System.out.println(listings.size());
        for (Listing listing : listings) {
            System.out.println(listing.getListingId());
        }
    }
}
