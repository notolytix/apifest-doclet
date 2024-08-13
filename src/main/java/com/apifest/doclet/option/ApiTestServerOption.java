package com.apifest.doclet.option;

import jdk.javadoc.doclet.Doclet;

import java.util.List;

public class ApiTestServerOption implements Doclet.Option {

    private String apiTestServer;

    @Override
    public int getArgumentCount() {
        return 1;  // The option requires one argument
    }

    @Override
    public String getDescription() {
        return "Sets the API test server URL";
    }

    @Override
    public Kind getKind() {
        return Doclet.Option.Kind.STANDARD;
    }

    @Override
    public List<String> getNames() {
        return List.of("-apiTestServer", "--apiTestServer");
    }

    @Override
    public String getParameters() {
        return "apiTestServer";
    }

    @Override
    public boolean process(String option, List<String> arguments) {
        String value = arguments.get(0);
        if ("NONE".equalsIgnoreCase(value)) {
            return true;
        }
        apiTestServer = value;
        return true;
    }

    public String getApiTestServer() {
        return apiTestServer;
    }
}
