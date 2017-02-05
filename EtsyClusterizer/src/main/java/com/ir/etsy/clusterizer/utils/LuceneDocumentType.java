package com.ir.etsy.clusterizer.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum LuceneDocumentType {
    LISTING ("listings", "listings"),
    CATEGORY ("categories", "categories");
    
    public static final String BASE_INDEX_DIR = "";
    public static final String BASE_DATA_DIR = "";
    
    private final String indexSubdir;
    private final String dataSubdir;
            
    LuceneDocumentType(String indexSubdir, String dataSubdir) {
        this.indexSubdir = indexSubdir;
        this.dataSubdir = dataSubdir;
    }
    
    public Path getIndexDir() {
        return Paths.get(BASE_INDEX_DIR, indexSubdir);
    }
    
    public Path getDataDir() {
        return Paths.get(BASE_DATA_DIR, dataSubdir);
    }
}