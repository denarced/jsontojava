package com.denarced.jsontojava;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;

public class MainTest {
    @Test
    public void basicTest() throws URISyntaxException {
        URI uri = getClass().getResource("/test.json").toURI();
        File file = new File(uri);
        JsonToJava json = new JsonToJava();
        json.parse(file);
    }
}

