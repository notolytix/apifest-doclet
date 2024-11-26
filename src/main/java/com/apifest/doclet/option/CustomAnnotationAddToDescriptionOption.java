package com.apifest.doclet.option;

import jdk.javadoc.doclet.Doclet;

import java.util.ArrayList;
import java.util.List;

public class CustomAnnotationAddToDescriptionOption implements Doclet.Option {

    private List<String> customAnnotationsAddToDescription = new ArrayList<>();

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
        for (String annotation : customAnnotationsValue.split(",")) {
            customAnnotationsAddToDescription.add(annotation);
        }
        return true;
    }

    public List<String> getCustomAnnotationsAddToDescription() {
        return customAnnotationsAddToDescription;
    }
}
