package com.denarced.jsontojava;

import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Main test class.
 *
 * @author denarced
 */
public class MainTest {
    @Test
    public void basicTest() throws URISyntaxException {
        URI uri = getClass().getResource("/test.json").toURI();
        File file = new File(uri);
        JavaFileWriter writer = 
            new ClassWriter("com.denarced.jsontojava", "target/gen/");
        JsonToJava json = new JsonToJava(writer);
        json.setRootClassName("base");
        json.parse(file);
    }

    @Test
    public void generateStaticTest() throws URISyntaxException {
        URI uri = getClass().getResource("/test.json").toURI();
        File file = new File(uri);
        JavaFileWriter writer = 
            new ClassWriter("com.denarced.staticjsontojava", "target/gen/");
        JsonToJava json = new JsonToJava(writer);
        json.setRootClassName("base");
        json.setGenerateStatic(true);
        json.parse(file);
    }
}

