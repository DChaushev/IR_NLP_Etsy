package com.ir.etsy.clusterizer.services.configuration;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        applyFilters(resources);
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.ir.etsy.clusterizer.services.EtsyResource.class);
    }

    private void applyFilters(Set<Class<?>> resources) {
        resources.add(com.ir.etsy.clusterizer.services.configuration.CORSFilter.class);
    }
}