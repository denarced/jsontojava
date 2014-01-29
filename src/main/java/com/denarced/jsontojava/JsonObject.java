package com.denarced.jsontojava;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author denarced
 */
public class JsonObject {
    private final String name;
    private Map<String, String> strings =
        new LinkedHashMap<String, String>();
    private Map<String, Long> longs = new LinkedHashMap<String, Long>();
    private Map<String, JsonObject> objects =
        new LinkedHashMap<String, JsonObject>();

    public JsonObject(String name) {
        this.name = name;
    }

    public void addString(String name, String value) {
        strings.put(name, value);
    }

    public void addLong(String name, long value) {
        longs.put(name, value);
    }

    public void addObject(String name, JsonObject object) {
        objects.put(name, object);
    }

    public void printInto(JavaClassWriter javaClassWriter) {
        javaClassWriter.printPackage();
        printInto(0, this, javaClassWriter);
    }

    public void printInto(int level, JsonObject jsonObject, JavaClassWriter javaClassWriter) {
        javaClassWriter.setLevel(level);
        javaClassWriter.classDeclaration(jsonObject.name);
        for (Map.Entry<String, String> each: jsonObject.strings.entrySet()) {
            javaClassWriter.addString(each.getKey(), each.getValue());
        }
        for (Map.Entry<String, Long> each: jsonObject.longs.entrySet()) {
            javaClassWriter.addLong(each.getKey(), each.getValue());
        }
        for (Map.Entry<String, JsonObject> each: jsonObject.objects.entrySet()) {
            printInto(level + 1, each.getValue(), javaClassWriter);
        }
        javaClassWriter.setLevel(level);
        javaClassWriter.endClass();
    }
}
