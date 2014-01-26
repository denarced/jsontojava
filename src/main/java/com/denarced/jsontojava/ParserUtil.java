package com.denarced.jsontojava;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;

/**
 * @author denarced
 */
public class ParserUtil {
    public String currentName(JsonParser jsonParser) {
        try {
            return jsonParser.getCurrentName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long number(JsonParser jsonParser) {
        try {
            return jsonParser.getLongValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String text(JsonParser jsonParser) {
        try {
            return jsonParser.getText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
