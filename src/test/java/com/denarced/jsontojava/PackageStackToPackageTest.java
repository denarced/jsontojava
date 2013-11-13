package com.denarced.jsontojava;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author denarced
 */
public class PackageStackToPackageTest {
    private ClassWriter classWriter;

    @Before
    public void setUp() {
        classWriter = new ClassWriter("", "");
    }
    @Test
    public void testWithEmptyParameter() {
        // EXERCISE
        String packageString =
            classWriter.packageStackToPackage(Collections.<String>emptyList());

        // VERIFY
        Assert.assertEquals(
            "With empty input, empty string should be returned.",
            "",
            packageString);
    }

    @Test
    public void testWithOneParameterThatContainsUppercaseCharacters() {
        // EXERCISE
        final String packageName = "orderName";
        List<String> stack = Arrays.asList(packageName);
        String packageString =
            classWriter.packageStackToPackage(stack);

        // VERIFY
        Assert.assertEquals(
            "String.toLower should be applied to all package names.",
            packageName.toLowerCase(),
            packageString);
    }

    @Test
    public void testWithSeveralParameters() {
        // EXERCISE
        String packageString =
            classWriter.packageStackToPackage(Arrays.asList("com", "sun"));

        // VERIFY
        Assert.assertEquals(
            "com.sun",
            packageString);
    }
}
