package com.denarced.jsontojava;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author denarced
 */
public class ClassGeneratorTest {
    private static final String BASE_PACKAGE = "com.denarced.jsontojava.generated";
    private static final String CLASS_NAME = "HellGoddess";

    @Test
    public void testPackageLine() throws Exception {
        // SETUP SUT
        ClassGenerator classGenerator =
            new ClassGenerator(Collections.<String>emptyList(), BASE_PACKAGE);

        // EXERCISE
        String strPackage = classGenerator.packageLine();

        // VERIFY
        List<String> expected = Arrays.asList("package", BASE_PACKAGE + ";");
        Assert.assertEquals(expected, Arrays.asList(StringUtils.split(strPackage)));
    }

    @Test
    public void testImportsWithEmptyObjectsList() {
        // SETUP SUT
        ClassGenerator classGenerator =
            new ClassGenerator(Collections.<String>emptyList(), BASE_PACKAGE);

        // EXERCISE
        List<String> importLines =
            classGenerator.imports(Collections.<String>emptyList(), CLASS_NAME);

        // VERIFY
        Assert.assertEquals(
            "No object, no import lines.",
            Collections.<String>emptyList(),
            importLines);
    }

    @Test
    public void testImports() {
        // SETUP SUT
        ClassGenerator classGenerator =
            new ClassGenerator(Collections.<String>emptyList(), BASE_PACKAGE);

        // EXERCISE
        final String objectName = "lineFormatter";
        List<String> objects = Arrays.asList(objectName);
        List<String> importLines = classGenerator.imports(objects, CLASS_NAME);

        // VERIFY
        Assert.assertEquals(
            "One object, one line, one list item.",
            1,
            importLines.size());
        List<String> expected = Arrays.asList(
            "import",
            String.format(
                "%s.%s.%s;",
                BASE_PACKAGE,
                CLASS_NAME.toLowerCase(),
                WordUtils.capitalize(objectName)));
        List<String> actual = Arrays.asList(StringUtils.split(importLines.get(0)));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testStringAttributes() {
        // SETUP SUT
        ClassGenerator classGenerator =
            new ClassGenerator(Collections.<String>emptyList(), BASE_PACKAGE);
        Map<String, String> stringAttributes = new HashMap<String, String>();
        final String attributeName = "fingerEraser";
        final String attributeValue = "Mr.Sharp";
        stringAttributes.put(attributeName, attributeValue);

        // EXERCISE
        List<String> attributeLineList =
            classGenerator.stringAttributes(stringAttributes);

        // VERIFY
        Assert.assertEquals(
            "One attribute, one list item.",
            1,
            attributeLineList.size());
        List<String> actual = Arrays.asList(
            StringUtils.split(
                attributeLineList.get(0)));
        List<String> expected = Arrays.asList(
            "public",
            "String",
            attributeName,
            "=",
            "\"" + attributeValue + "\";");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLongAttributes() {
        // SETUP SUT
        ClassGenerator classGenerator =
            new ClassGenerator(Collections.<String>emptyList(), BASE_PACKAGE);
        Map<String, Long> longAttributes = new HashMap<String, Long>();
        final String attributeName = "fingerEraser";
        final Long attributeValue = 49L;
        longAttributes.put(attributeName, attributeValue);

        // EXERCISE
        List<String> attributeLineList =
            classGenerator.longAttributes(longAttributes);

        // VERIFY
        Assert.assertEquals(
            "One attribute, one list item.",
            1,
            attributeLineList.size());
        List<String> actual = Arrays.asList(
            StringUtils.split(
                attributeLineList.get(0)));
        List<String> expected = Arrays.asList(
            "public",
            "long",
            attributeName,
            "=",
            attributeValue + ";");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testObjectAttributes() {
        // SETUP SUT
        ClassGenerator classGenerator =
            new ClassGenerator(Collections.<String>emptyList(), BASE_PACKAGE);
        List<String> objectAttributes = new ArrayList<String>();
        final String className = "MagicWand";
        final String attributeName = "magicWand";
        objectAttributes.add(attributeName);

        // EXERCISE
        List<String> attributeLineList =
            classGenerator.objectAttributes(objectAttributes);

        // VERIFY
        Assert.assertEquals(
            "One attribute, one list item.",
            1,
            attributeLineList.size());
        List<String> actual = Arrays.asList(
            StringUtils.split(
                attributeLineList.get(0)));
        List<String> expected = Arrays.asList(
            "public",
            className,
            attributeName,
            "=",
            "new",
            String.format("%s();", className));
        Assert.assertEquals(expected, actual);
    }
}
