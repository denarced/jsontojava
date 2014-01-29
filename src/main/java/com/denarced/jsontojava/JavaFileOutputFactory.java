package com.denarced.jsontojava;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author denarced
 */
public class JavaFileOutputFactory {
    public static PrintStream create(
        String rootClassName,
        String targetDirectory) throws IOException {


        String filename = String.format("/%s.java", rootClassName);
        String fullPath =
            FilenameUtils.separatorsToSystem(targetDirectory + filename);
        File javaFile = new File(fullPath);
        FileUtils.forceMkdir(javaFile.getParentFile());
        return new PrintStream(javaFile);
    }
}
