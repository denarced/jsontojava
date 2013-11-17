package com.denarced.jsontojava;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author denarced
 */
public class ClassGenerator {
    private final List<String> packageStack;
    private final String basePackage;
    private final boolean isStatic;

    public ClassGenerator(List<String> packageStack, String basePackage, boolean isStatic) {
        this.packageStack = packageStack;
        this.basePackage = basePackage;
        this.isStatic = isStatic;
    }

    public String packageLine() {
        return packageLine(packageStack);
    }

    public List<String> imports(List<String> objects, String className) {
        String strPackageLine = packageLine();
        List<String> importLines = new ArrayList<String>(objects.size());
        for (String s: objects) {
            String woSemicolon =
                strPackageLine.substring(0, strPackageLine.length() - 1);
            String woPackageKeyword = woSemicolon.replaceFirst("package", "");
            importLines.add(
                String.format(
                    "import %s.%s.%s;",
                    woPackageKeyword.trim(),
                    className.toLowerCase(),
                    WordUtils.capitalize(s)));
        }

        return importLines;
    }

    public List<String> stringAttributes(Map<String, String> attributes) {
        List<AttributeProducer> attributeProducerList =
            new ArrayList<AttributeProducer>(attributes.size());
        for (Map.Entry<String, String> each: attributes.entrySet()) {
            attributeProducerList.add(newStringAttributeProducer(each.getKey(), each.getValue()));
        }
        return attributes(attributeProducerList);
    }

    public List<String> longAttributes(Map<String, Long> attributes) {
        List<AttributeProducer> attributeProducerList =
            new ArrayList<AttributeProducer>(attributes.size());
        for (Map.Entry<String, Long> each: attributes.entrySet()) {
            attributeProducerList.add(newLongAttributeProducer(each.getKey(), each.getValue()));
        }
        return attributes(attributeProducerList);
    }

    public List<String> objectAttributes(List<String> attributes) {
        List<AttributeProducer> attributeProducerList =
            new ArrayList<AttributeProducer>(attributes.size());
        for (String each: attributes) {
            attributeProducerList.add(newObjectAttributeProducer(each));
        }
        return attributes(attributeProducerList);
    }

    private List<String> attributes(List<AttributeProducer> producerList) {
        List<String> lineList = new ArrayList<String>(producerList.size());
        String staticStr = isStatic ? "static " : "";
        for (AttributeProducer each: producerList) {
            String line = String.format("public %s%s %s = %s;",
                staticStr,
                each.kind(),
                each.name(),
                each.valueRepresentation());
            lineList.add(line);
        }

        return lineList;
    }

    private String packageLine(List<String> packageStack) {
        String strPackage = "package " + basePackage;
        String postPackage = packageStackToPackage(packageStack);
        if (postPackage.isEmpty()) {
            return strPackage + ";";
        } else {
            return strPackage + "." + postPackage + ";";
        }
    }

    String packageStackToPackage(List<String> packageStack) {
        String strPackage = "";
        boolean first = true;
        for (String each: packageStack) {
            if (first) {
                first = false;
            } else {
                strPackage += ".";
            }
            strPackage += each.toLowerCase();
        }
        return strPackage;
    }

    private static class AttributeProducer {
        private final String _name;
        private final String _valueRepresentation;
        private final String _kind;

        private AttributeProducer(String name, String value, String kind) {
            this._name = name;
            this._valueRepresentation = value;
            this._kind = kind;
        }

        public String kind() {
            return _kind;
        }

        public String name() {
            return _name;
        }

        public String valueRepresentation() {
            return _valueRepresentation;
        }
    }

    private static AttributeProducer newStringAttributeProducer(String name, String value) {
        return new AttributeProducer(
            name,
            String.format("\"%s\"", StringEscapeUtils.escapeJava(value)),
            "String");
    }

    private static AttributeProducer newLongAttributeProducer(String name, Long value) {
        return new AttributeProducer(name, value.toString(), "long");
    }

    private static AttributeProducer newObjectAttributeProducer(String name) {
        String className = WordUtils.capitalize(name);
        return new AttributeProducer(
            name,
            String.format("new %s()", className),
            className);
    }
}
