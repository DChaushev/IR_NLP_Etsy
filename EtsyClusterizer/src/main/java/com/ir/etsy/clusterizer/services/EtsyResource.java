package com.ir.etsy.clusterizer.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Dimitar
 */
@Path("get-similar-listings")
public class EtsyResource {

    //TODO: imlement
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getSimilarListings() {
        return "HALLO!";
    }
}
