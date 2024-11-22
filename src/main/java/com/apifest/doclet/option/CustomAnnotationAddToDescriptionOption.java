package com.apifest.doclet.option;

import jdk.javadoc.doclet.Doclet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAnnotationAddToDescriptionOption implements Doclet.Option {
    private Map<String, List<String>> customAnnotationsAddToDescription = new HashMap<>();

    @Override
    public int getArgumentCount() {
        return 1;  // The option requires one argument
    }

    @Override
    public String getDescription() {
        return "Sets the custom annotations the values of which will be added to the operation description";
    }

    @Override
    public Kind getKind() {
        return Doclet.Option.Kind.STANDARD;
    }

    @Override
    public List<String> getNames() {
        return List.of("-customAnnotationsAddToDescription", "--custom-annotations-add-to_descr");
    }

    @Override
    public String getParameters() {
        return "annotations";
    }

    @Override
    public boolean process(String option, List<String> arguments) {
        String customAnnotationsValue = arguments.get(0);
        System.out.println("process custom annotations add to description");
        for (String annotation : customAnnotationsValue.split(",")) {
            if (annotation.contains(":")) {
                String[] tokens = annotation.split(":");
                List<String> annotationAttributeList = customAnnotationsAddToDescription.computeIfAbsent(tokens[0], k -> new ArrayList<>());
                annotationAttributeList.add(tokens[1]);
            } else {
                customAnnotationsAddToDescription.put(annotation, Collections.emptyList());
            }
        }
        return true;
    }

    public Map<String, List<String>> getCustomAnnotationsAddToDescription() {
        return customAnnotationsAddToDescription;
    }
}
