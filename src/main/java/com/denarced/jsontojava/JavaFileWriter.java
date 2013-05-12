package com.denarced.jsontojava;

import java.util.List;
import java.util.Map;

public interface JavaFileWriter {
    /**
     * Write Java source code file.
     * @param name of the class which is capitalized. So having a name
     * diamondPalace is translated as {@code public class DiamondPalace}.
     * @param attributes are the class' public string attributes and their
     * values. E.g. a map item with key mkey and value mvalue is translated as 
     * {@code public String mkey = "mvalue";}.
     * @param objects are the class' object attributes which are initialized
     * with the classes' default constructors. Each of list's string items are
     * used as such as the attribute's name. The name is capitalized to get the
     * attribute's class. E.g. diamondPalace is translated into 
     * {@code public DiamondPalace diamondPalace = new DiamondPalace();}.
     */
    void write(String name, Map<String, String> attributes, List<String> objects);
}
