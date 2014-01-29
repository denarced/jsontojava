package com.denarced.jsontojava;

/**
 * @author denarced
 */
public interface JavaClassWriter {
    void classDeclaration(String name);
    void addString(String name, String value);
    void addLong(String name, long value);
    void setLevel(int level);
    void endClass();
    void setTargetPackage(String targetPackage);
    void printPackage();
}
