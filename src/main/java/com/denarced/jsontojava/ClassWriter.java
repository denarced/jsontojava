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
public class ClassWriter implements JavaFileWriter {
    private String packageName;
    private String baseDir;
    private File targetDir;
    private boolean dirCreated = false;

    /**
     * Initialize.
     * @param packageName in the standard form com.example.subpack.
     * @param baseDir under which the folders according to the packageName are
     * created. The directory separators can be either Unix or Windows kind.
     */
    public ClassWriter(String packageName, String baseDir) {
        this.packageName = packageName;
        this.baseDir = FilenameUtils.separatorsToSystem(baseDir);
    }

    /**
     * Create the directory into which the Java files are generated.
     */
    void createPackageDir() {
        if (dirCreated) {
            return;
        }
        String packagePath = FilenameUtils.separatorsToSystem(packageName.replaceAll("\\.", "/"));
        targetDir = new File(FilenameUtils.concat(baseDir, packagePath));
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                System.err.println("WARNING! DIRECTORY CREATION FAILED: " + targetDir.getAbsolutePath());
                dirCreated = true;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(
        String name, 
        final Map<String, String> attributes, 
        final Map<String, Long> longAttributes,
        final List<String> objects,
        boolean generateStatic) {
        
        createPackageDir();
        final String className = WordUtils.capitalize(name);
        File javaFile = new File(targetDir, className + ".java");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javaFile), "UTF-8"));
            writer.write("package " + packageName + ";");
            final String nl = System.getProperty("line.separator");
            writer.write(nl + nl);
            writer.write(String.format("public class %s {%s", className, nl));
            final String staticStr = generateStatic ? "static" : "";
            for (Entry<String, String> set : attributes.entrySet()) {
                writer.write(String.format("%spublic %s String %s = \"%s\";%s",
                    ClassWriter.tab(1),
                    staticStr,
                    set.getKey(),
                    StringEscapeUtils.escapeJava(set.getValue()),
                    nl));
            }
            for (Entry<String, Long> set: longAttributes.entrySet()) {
                writer.write(String.format("%spublic %s long %s = %d;%s",
                    ClassWriter.tab(1),
                    staticStr,
                    set.getKey(),
                    set.getValue(),
                    nl));
            }
            for (String s : objects) {
                String cname = WordUtils.capitalize(s);
                String objLine = String.format("%spublic %s %s %s = new %s();%s",
                    ClassWriter.tab(1),
                    staticStr,
                    cname,
                    s,
                    cname,
                    nl);
                writer.write(objLine);
            }
            writer.write("}" + nl);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failure rises again: " + e.getMessage(), e);
        }
    }

    private static String tab(int count) {
        String t = "    ";
        String s = "";
        for (int i = 0; i < count; ++i) {
            s += t;
        }
        return s;
    }
}
