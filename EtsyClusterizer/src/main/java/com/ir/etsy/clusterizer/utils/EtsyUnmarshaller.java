package com.ir.etsy.clusterizer.utils;

import com.ir.etsy.clusterizer.categories.Category;
import com.ir.etsy.clusterizer.listings.Listing;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Dimitar
 */
public class EtsyUnmarshaller {

    public static List<Listing> getListings(File listingsFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Listing> listings = mapper.readValue(listingsFile, mapper.getTypeFactory().constructCollectionType(List.class, Listing.class));
        return listings;
    }
    
    public static List<Category> getCategories(File categoriesFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Category> categories = mapper.readValue(categoriesFile, mapper.getTypeFactory().constructCollectionType(List.class, Category.class));
        return categories;
    }
}
