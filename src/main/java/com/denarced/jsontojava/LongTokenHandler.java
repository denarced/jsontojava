package com.denarced.jsontojava;

import org.codehaus.jackson.JsonParser;

/**
 * @author denarced
 */
public class LongTokenHandler implements TokenHandler {
    private final ParserUtil parserUtil;

    public LongTokenHandler(ParserUtil parserUtil) {
        this.parserUtil = parserUtil;
    }

    @Override
    public void handle(JsonObject jsonObject, JsonParser jsonParser) {
        jsonObject.addLong(
            parserUtil.currentName(jsonParser),
            parserUtil.number(jsonParser));
    }
}
