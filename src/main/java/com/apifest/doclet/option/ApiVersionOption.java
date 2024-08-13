package com.apifest.doclet.option;

import jdk.javadoc.doclet.Doclet;

import java.util.List;

public class ApiVersionOption implements Doclet.Option {

    private String apiVersion;

    @Override
    public int getArgumentCount() {
        return 1;  // The option requires one argument
    }

    @Override
    public String getDescription() {
        return "Sets the API version";
    }

    @Override
    public Kind getKind() {
        return Doclet.Option.Kind.STANDARD;
    }

    @Override
    public List<String> getNames() {
        return List.of("-apiVersion", "--apiVersion");
    }

    @Override
    public String getParameters() {
        return "apiVersion";
    }

    @Override
    public boolean process(String option, List<String> arguments) {
        String value = arguments.get(0);
        if ("NONE".equalsIgnoreCase(value)) {
            return true;
        }
        apiVersion = value;
        return true;
    }

    public String getApiVersion() {
        return apiVersion;
    }
}
