package com.denarced.jsontojava;

import org.codehaus.jackson.JsonParser;

/**
 * @author denarced
 */
public interface TokenHandler {
    void handle(JsonObject jsonObject, JsonParser jsonParser);
}
