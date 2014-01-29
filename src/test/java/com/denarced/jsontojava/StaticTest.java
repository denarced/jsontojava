package com.denarced.jsontojava;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author denarced
 */
public class StaticTest {
    @Test
    public void testIt() throws IOException {
        InputStream json = getClass().getResourceAsStream("/test.json");
        String rootClassName = "Root";
        String targetPackage = "com.example";
        String targetDirectory = "src/gen/java/com/example";

        PrintStream output = JavaFileOutputFactory.create(
            rootClassName,
            targetDirectory);

        Parser parser = new JsonFileParser(json);
        JsonObject jsonObject = new JsonObject(rootClassName);
        parser.parse(jsonObject);
        JavaClassWriter javaClassWriter = new JavaClassWriterImpl(output);
        javaClassWriter.setTargetPackage(targetPackage);
        jsonObject.printInto(javaClassWriter);
    }
}
