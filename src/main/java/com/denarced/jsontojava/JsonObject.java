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

    public void print() {
        printObject(0, this);
    }

    public static void printObject(int level, JsonObject jsonObject) {
        System.out.println(tab(level) + jsonObject.name + ":");
        for (Map.Entry<String, String> each: jsonObject.strings.entrySet()) {
            System.out.println(
                String.format(
                    "%s%s: %s",
                    tab(level + 1),
                    each.getKey(),
                    each.getValue()));
        }
        for (Map.Entry<String, Long> each: jsonObject.longs.entrySet()) {
            System.out.println(
                String.format(
                    "%s%s: %d",
                    tab(level + 1),
                    each.getKey(),
                    each.getValue()));
        }
        for (Map.Entry<String, JsonObject> each: jsonObject.objects.entrySet()) {
            printObject(level + 1, each.getValue());
        }
    }

    private static String tab(int level) {
        String s = "";
        for (int i = 0; i < level; ++i) {
            s += " ";
        }
        return s;
    }
}
