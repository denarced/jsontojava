package com.denarced.jsontojava;

import org.codehaus.jackson.JsonParser;

/**
 * @author denarced
 */
public class StringTokenHandler implements TokenHandler {
    private final ParserUtil parserUtil;

    public StringTokenHandler(ParserUtil parserUtil) {
        this.parserUtil = parserUtil;
    }

    @Override
    public void handle(JsonObject jsonObject, JsonParser jsonParser) {
        jsonObject.addString(
            parserUtil.currentName(jsonParser),
            parserUtil.text(jsonParser));
    }
}
