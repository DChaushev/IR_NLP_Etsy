package com.ir.etsy.clusterizer.utils;

import java.io.File;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author Dimitar
 */
public class IOUtils {

    public static File[] getAllFiles(String directory) {
        Validate.notEmpty(directory);
        return getAllFiles(new File(directory));
    }

    public static File[] getAllFiles(File directory) {
        Validate.notNull(directory);
        return directory.listFiles();
    }
}
