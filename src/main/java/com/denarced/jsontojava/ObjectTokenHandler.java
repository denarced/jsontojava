package com.denarced.jsontojava;

import org.codehaus.jackson.JsonParser;

/**
 * @author denarced
 */
public class ObjectTokenHandler implements TokenHandler {
    private final ParserUtil parserUtil;
    private final Parser parser;

    public ObjectTokenHandler(ParserUtil parserUtil, Parser parser) {
        this.parserUtil = parserUtil;
        this.parser = parser;
    }

    @Override
    public void handle(JsonObject jsonObject, JsonParser jsonParser) {
        JsonObject newObject =
            new JsonObject(parserUtil.currentName(jsonParser));
        jsonObject.addObject(parserUtil.currentName(jsonParser), newObject);
        parser.parse(newObject);
    }
}
