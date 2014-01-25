package com.denarced.jsontojava;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.WordUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author denarced
 */
public class ClassWriter implements JavaFileWriter {
    private String packageName;
    private String baseDir;
    private File targetDir;

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

    String packageStackToPackage(List<String> packageStack) {
        String strPackage = "";
        boolean first = true;
        for (String each: packageStack) {
            if (first) {
                first = false;
            } else {
                strPackage += ".";
            }
            strPackage += each.toLowerCase();
        }
        return strPackage;
    }

    /**
     * Create the directory into which the Java files are generated.
     *
     * @param packageStack Create sub-directories as necessary according to this
     * list. Example: packageName = "com.google" and packageStack = "us.gmail".
     * A directory baseDir/com/google/us/gmail will be created.
     */
    void createPackageDir(List<String> packageStack) {
        String fullPackage =
            packageName + "." + packageStackToPackage(packageStack);
        String packagePath = FilenameUtils.separatorsToSystem(fullPackage.replaceAll("\\.+", "/"));
        targetDir = new File(FilenameUtils.concat(baseDir, packagePath));
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                System.err.println("WARNING! DIRECTORY CREATION FAILED: " + targetDir.getAbsolutePath());
            }
        }
    }

    private String packageLine(List<String> packageStack) {
        String strPackage = "package " + packageName;
        String postPackage = packageStackToPackage(packageStack);
        if (postPackage.isEmpty()) {
            return strPackage + ";";
        } else {
            return strPackage + "." + postPackage + ";";
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
        List<String> packageStack) {
        
        createPackageDir(packageStack);
        final String className = WordUtils.capitalize(name);
        File javaFile = new File(targetDir, className + ".java");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javaFile), "UTF-8"));
            final String nl = System.getProperty("line.separator");
            final ClassGenerator classGenerator =
                new ClassGenerator(packageStack, packageName);

            final String strPackageLine = classGenerator.packageLine();
            writer.write(strPackageLine);
            writer.write(nl + nl);

            for (String each: classGenerator.imports(objects, className)) {
                writer.write(each);
                writer.write(nl);
            }
            writer.write(nl + nl);

            writer.write(String.format("public class %s {%s", className, nl));

            for (String each: classGenerator.stringAttributes(attributes)) {
                writer.write(ClassWriter.tab(1) + each + nl);
            }

            for (String each: classGenerator.longAttributes(longAttributes)) {
                writer.write(ClassWriter.tab(1) + each + nl);
            }

            for (String each: classGenerator.objectAttributes(objects)) {
                writer.write(ClassWriter.tab(1) + each + nl);
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
