package com.denarced.jsontojava;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class JsonToJava {
    static class JavaClass {
        public Map<String, String> attributes = new HashMap<String, String>();
        public List<String> inner = new LinkedList<String>();
    }

    static String tab(int count) {
        String t = "    ";
        String s = "";
        for (int i = 0; i < count; ++i) {
            s += t;
        }
        return s;
    }

    JavaClass parseObject(JsonParser jp, final ClassWriter writer) {
        JavaClass javaClass = new JavaClass();
        try {
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jp.getCurrentName();
                JsonToken val = jp.nextToken(); // move to value, or START_OBJECT/START_ARRAY
                if (val == JsonToken.VALUE_STRING) {
                    javaClass.attributes.put(fieldname, jp.getText());
                } else if (val == JsonToken.START_OBJECT) {
                    JavaClass jc = parseObject(jp, writer);
                    writer.write(fieldname, jc.attributes, jc.inner);
                    javaClass.inner.add(fieldname);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("parseObject exception: " + e.getMessage(), e);
        }

        return javaClass;
    }

    public void parse(File file) {
        try {
            JsonFactory f = new JsonFactory();
            JsonParser jp = f.createJsonParser(file);
            jp.nextToken();
            final ClassWriter writer = new ClassWriter(
                    "com.denarced.jacksonparsetryout",
                    "target/gen/");
            JavaClass javaClass = parseObject(jp, writer);
            writer.write("root", javaClass.attributes, javaClass.inner);
            jp.close(); // ensure resources get cleaned up timely and properly
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
