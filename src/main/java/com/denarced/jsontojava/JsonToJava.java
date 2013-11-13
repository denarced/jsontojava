package com.denarced.jsontojava;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author denarced
 */
public class JsonToJava {
    private final JavaFileWriter writer;
    private String rootClassName = "Root";
    private boolean generateStatic = false;

    /**
     * Initialize with JavaFileWriter.
     */
    public JsonToJava(JavaFileWriter writer) {
        this.writer = writer;
    }

    public void setGenerateStatic(boolean generateStatic) {
        this.generateStatic = generateStatic;
    }

    /**
     * Parse the current json object.
     * @param jp is the parser in which the next token is then parsed json's
     * next json object.
     * @param packageStack The relative package names into which the found
     * objects are placed into. These packages will be under the base package
     * that was set into the {@code JavaFileWriter} attribute.
     * @return the java class for the current json object.
     */
    JavaClass parseObject(JsonParser jp, List<String> packageStack) {
        JavaClass javaClass = new JavaClass();
        try {
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jp.getCurrentName();
                JsonToken val = jp.nextToken();
                if (val == JsonToken.VALUE_STRING) {
                    javaClass.attributes.put(fieldname, jp.getText());
                } else if (val == JsonToken.START_OBJECT) {
                    List<String> newStack = new ArrayList<String>(packageStack);
                    newStack.add(fieldname);
                    JavaClass jc = parseObject(jp, newStack);
                    writer.write(
                        fieldname, 
                        jc.attributes, 
                        jc.longAttributes, 
                        jc.inner, 
                        generateStatic,
                        packageStack);
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
            List<String> rootStack = Collections.singletonList(rootClassName);
            JavaClass javaClass = parseObject(jp, rootStack);
            writer.write(
                rootClassName, 
                javaClass.attributes, 
                javaClass.longAttributes, 
                javaClass.inner,
                generateStatic,
                Collections.<String>emptyList());
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
