package com.denarced.jsontojava;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;

/**
 * @author denarced
 */
class ClassWriter implements JavaFileWriter {
    private String packageName;
    private String baseDir;
    private File targetDir;
    private boolean dirCreated = false;

    public ClassWriter(String packageName, String baseDir) {
        this.packageName = packageName;
        this.baseDir = FilenameUtils.separatorsToSystem(baseDir);
    }

    void createPackageDir() {
        String packagePath = FilenameUtils.separatorsToSystem(packageName.replaceAll("\\.", "/"));
        targetDir = new File(FilenameUtils.concat(baseDir, packagePath));
        if (!targetDir.exists()) {
            boolean directoryCreated = targetDir.mkdirs();
            if (!directoryCreated) {
                System.err.println("WARNING! DIRECTORY CREATION FAILED: " + targetDir.getAbsolutePath());
            }
        }
    }

    @Override
    public void write(
        String name, 
        final Map<String, String> attributes, 
        final List<String> objects) {
        
        if (!dirCreated) {
            createPackageDir();
        }
        final String className = WordUtils.capitalize(name);
        File javaFile = new File(targetDir, className + ".java");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javaFile), "UTF-8"));
            writer.write("package " + packageName + ";");
            final String nl = System.getProperty("line.separator");
            writer.write(nl + nl);
            writer.write("public class " + className + " {" + nl);
            for (Entry<String, String> set : attributes.entrySet()) {
                String attrLine = JsonToJava.tab(1) + "public String " + set.getKey() + " = \"" + StringEscapeUtils.escapeJava(set.getValue()) + "\";" + nl;
                writer.write(attrLine);
            }
            for (String s : objects) {
                String cname = WordUtils.capitalize(s);
                String objLine = JsonToJava.tab(1) + "public " + cname + " " + s + " = new " + cname + "();" + nl;
                writer.write(objLine);
            }
            writer.write("}" + nl);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failure rises again: " + e.getMessage(), e);
        }
    }
    
}
