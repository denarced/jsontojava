package com.denarced.jsontojava;

import java.io.PrintStream;

/**
 * @author denarced
 */
public class JavaClassWriterImpl implements JavaClassWriter {
    private int level;
    private String targetPackage;

    private final PrintStream printStream;

    public JavaClassWriterImpl(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void classDeclaration(String name) {
        printStream.println(
            String.format(
                "%spublic %sclass %s {",
                tab(level),
                level == 0 ? "" : "static ",
                name));
    }

    @Override
    public void addString(String name, String value) {
        printStream.println(
            String.format(
                "%spublic static final String %s = \"%s\";",
                tab(level + 1),
                name,
                value));
    }

    @Override
    public void addLong(String name, long value) {
        printStream.println(
            String.format(
                "%spublic static final long %s = %d;",
                tab(level + 1),
                name,
                value));
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void endClass() {
        printStream.println(
            String.format(
                "%s}",
                tab(level)));
    }

    @Override
    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    @Override
    public void printPackage() {
        printStream.println(
            String.format(
                "package %s;",
                targetPackage));
    }

    private String tab(int level) {
        String s = "";
        for (int i = 0; i < level; ++i) {
            s += "    ";
        }
        return s;
    }
}
