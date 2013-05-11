package com.denarced.jsontojava;

import java.util.List;
import java.util.Map;

public interface JavaFileWriter {
    void write(String name, Map<String, String> attributes, List<String> objects);
}
