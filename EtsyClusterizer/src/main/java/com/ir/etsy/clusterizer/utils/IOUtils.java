package com.ir.etsy.clusterizer.utils;

import java.io.File;
import java.nio.file.Path;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author Dimitar
 */
public class IOUtils {

    public static File[] getAllFiles(Path directoryPath) {
        Validate.notNull(directoryPath);
        File directory = directoryPath.toFile();
        Validate.notNull(directory);
        return directory.listFiles();
    }
}
