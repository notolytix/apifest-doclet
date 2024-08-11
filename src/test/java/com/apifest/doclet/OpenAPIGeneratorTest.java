package com.apifest.doclet;

import com.apifest.api.MappingEndpoint;
import com.apifest.api.MappingEndpointDocumentation;
import com.apifest.doclet.tests.resources.CreateUserRequest;
import com.apifest.doclet.tests.resources.Users;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OpenAPIGeneratorTest {

    @Test
    public void test() {
        // GIVEN
        OpenAPIGenerator generator = new OpenAPIGenerator();
        Set<Class<?>> classSet = new HashSet<>();
        classSet.add(CreateUserRequest.class);
        classSet.add(Users.class);
        List<ParsedEndpoint> parsedEndpoints = new ArrayList<>();
        ParsedEndpoint createUserEndpoint = new ParsedEndpoint();
        createUserEndpoint.setEndpointPathWithoutVersion("/user");
        MappingEndpointDocumentation mappingEndpointDocumentation = new MappingEndpointDocumentation();
        mappingEndpointDocumentation.setGroup("User");
        mappingEndpointDocumentation.setSummary("Create a user");
        mappingEndpointDocumentation.setDescription("This web service creates a new user");
        mappingEndpointDocumentation.setScope("umbrella");
        mappingEndpointDocumentation.setMethod("PUT");
        createUserEndpoint.setMappingEndpointDocumentation(mappingEndpointDocumentation);

        MappingEndpoint mappingEndpoint = new MappingEndpoint();
        mappingEndpoint.setAuthType("user");
        mappingEndpoint.setScope("umbrella");
        mappingEndpoint.setInternalEndpoint("/user/{organization}");
        mappingEndpoint.setExternalEndpoint("/1.0/users");
        createUserEndpoint.setMappingEndpoint(mappingEndpoint);
        parsedEndpoints.add(createUserEndpoint);

        ParsedEndpoint getUserInfoEndpoint = new ParsedEndpoint();
        getUserInfoEndpoint.setEndpointPathWithoutVersion("/user");
        MappingEndpointDocumentation mappingEndpointDocumentation2 = new MappingEndpointDocumentation();
        mappingEndpointDocumentation2.setGroup("User");
        mappingEndpointDocumentation2.setSummary("Current user's access information");
        mappingEndpointDocumentation2.setDescription("This web service returns information about the user");
        mappingEndpointDocumentation2.setScope("umbrella");
        mappingEndpointDocumentation2.setMethod("GET");
        getUserInfoEndpoint.setMappingEndpointDocumentation(mappingEndpointDocumentation2);

        MappingEndpoint mappingEndpoint2 = new MappingEndpoint();
        mappingEndpoint2.setAuthType("user");
        mappingEndpoint2.setScope("umbrella");
        mappingEndpoint2.setInternalEndpoint("/user");
        mappingEndpoint2.setExternalEndpoint("/1.0/users");
        getUserInfoEndpoint.setMappingEndpoint(mappingEndpoint2);
        parsedEndpoints.add(getUserInfoEndpoint);

        // WHEN
//        try {
//            generator.generateOpenAPIFile(classSet, parsedEndpoints, "opentAPI-test.json");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        // THEN
        System.out.println("OK");;
    }
}
