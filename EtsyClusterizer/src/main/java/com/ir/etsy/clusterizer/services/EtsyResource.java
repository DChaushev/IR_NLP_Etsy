package com.ir.etsy.clusterizer.services;

import com.ir.etsy.clusterizer.listings.Listing;
import com.ir.etsy.clusterizer.listings.SimilarListingsRetriever;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Dimitar
 */
@Path("get-similar-listings")
public class EtsyResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimilarListings(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Listing listing = mapper.readValue(json, Listing.class);
            List<Listing> similarListings = SimilarListingsRetriever.process(listing);

            String jsonResult = mapper.writeValueAsString(similarListings);

            return Response.ok().entity(jsonResult).build();
        } catch (IOException ex) {
            Logger.getLogger(EtsyResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    "The send json is of unsupported format!\n" + ex.getMessage()).build();
        }
    }
}
