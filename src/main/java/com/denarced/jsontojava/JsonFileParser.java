package com.denarced.jsontojava;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author denarced
 */
public class JsonFileParser implements Parser {
    private final JsonParser jsonParser;
    private final Map<JsonToken, TokenHandler> handlers =
        createHandlers();

    public JsonFileParser(InputStream json) {
        jsonParser = buildFactory(json);
        nextToken();
    }

    private Map<JsonToken, TokenHandler> createHandlers() {
        Map<JsonToken, TokenHandler> handlers =
            new HashMap<JsonToken, TokenHandler>();
        final ParserUtil parserUtil = new ParserUtil();

        handlers.put(
            JsonToken.VALUE_STRING,
            new StringTokenHandler(parserUtil));
        handlers.put(
            JsonToken.VALUE_NUMBER_INT,
            new LongTokenHandler(parserUtil));
        handlers.put(
            JsonToken.START_OBJECT,
            new ObjectTokenHandler(parserUtil, this));

        return handlers;
    }

    @Override
    public void parse(JsonObject jsonObject) {
        while (nextToken() != JsonToken.END_OBJECT) {
            JsonToken jsonToken = nextToken();
            TokenHandler handler = handlers.get(jsonToken);
            handler.handle(jsonObject, jsonParser);
        }
    }

    private JsonToken nextToken() {
        try {
            return jsonParser.nextToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonParser buildFactory(InputStream json) {
        JsonFactory jsonFactory = new JsonFactory();
        try {
            return jsonFactory.createJsonParser(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
