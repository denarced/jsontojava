package com.denarced.jsontojava;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

/**
 * @author denarced
 */
public class JsonToJava {
    private final JavaFileWriter writer;
    private String rootClassName = "Root";

    /**
     * Initialize with JavaFileWriter.
     */
    public JsonToJava(JavaFileWriter writer) {
        this.writer = writer;
    }

    /**
     * Parse the current json object.
     * @param jp is the parser in which the next token is then parsed json's
     * next json object.
     * @return the java class for the current json object.
     */
    JavaClass parseObject(JsonParser jp) {
        JavaClass javaClass = new JavaClass();
        try {
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jp.getCurrentName();
                JsonToken val = jp.nextToken();
                if (val == JsonToken.VALUE_STRING) {
                    javaClass.attributes.put(fieldname, jp.getText());
                } else if (val == JsonToken.START_OBJECT) {
                    JavaClass jc = parseObject(jp);
                    writer.write(fieldname, jc.attributes, jc.longAttributes, jc.inner);
                    javaClass.inner.add(fieldname);
                } else if (val == JsonToken.VALUE_NUMBER_INT) {
                    javaClass.longAttributes.put(fieldname, jp.getLongValue());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("parseObject exception: " + e.getMessage(), e);
        }

        return javaClass;
    }

    public void parse(File file) {
        JsonParser jp = null;
        try {
            JsonFactory f = new JsonFactory();
            jp = f.createJsonParser(file);
            jp.nextToken();
            JavaClass javaClass = parseObject(jp);
            writer.write(rootClassName, javaClass.attributes, javaClass.longAttributes, javaClass.inner);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (jp != null) {
                try {
                    jp.close();
                } catch (IOException ex) {
                    ; // ignore, what is there to do
                }
            }
        }
    }

    public void setRootClassName(String name) {
        rootClassName = name;
    }

    static class JavaClass {
        public Map<String, String> attributes = new HashMap<String, String>();
        public Map<String, Long> longAttributes = new HashMap<String, Long>();
        public List<String> inner = new LinkedList<String>();
    }
}
