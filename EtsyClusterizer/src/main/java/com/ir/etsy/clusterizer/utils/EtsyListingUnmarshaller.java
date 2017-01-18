package com.ir.etsy.clusterizer.utils;

import com.ir.etsy.clusterizer.Listing;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Dimitar
 */
public class EtsyListingUnmarshaller {

    public static List<Listing> getListings(File listingsFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Listing> listings = mapper.readValue(listingsFile, mapper.getTypeFactory().constructCollectionType(List.class, Listing.class));
        return listings;
    }

}
