package com.denarced.jsontojava;

import org.junit.Test;

import java.io.InputStream;

/**
 * @author denarced
 */
public class StaticTest {
    @Test
    public void testIt() {
        InputStream json = getClass().getResourceAsStream("/test.json");
        Parser parser = new JsonFileParser(json);
        JsonObject jsonObject = new JsonObject("root");
        parser.parse(jsonObject);
        jsonObject.print();
    }
}
