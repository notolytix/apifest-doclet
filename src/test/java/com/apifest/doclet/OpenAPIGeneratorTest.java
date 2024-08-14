package com.apifest.doclet;

import com.apifest.api.MappingEndpoint;
import com.apifest.api.MappingEndpointDocumentation;
import com.apifest.api.params.RequestParamDocumentation;
import com.apifest.api.params.ResultParamDocumentation;
import com.apifest.doclet.tests.resources.CreateUserRequest;
import com.apifest.doclet.tests.resources.Users;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class OpenAPIGeneratorTest {

    OpenAPIGenerator generator;

    @BeforeMethod
    public void setup() {
        generator = new OpenAPIGenerator("1.0","https://api.nototest.net");
    }

    @Test
    public void when_get_method_return_GET_operation() {
        // GIVEN
        PathItem pathItem = new PathItem();
        Operation postOperation = new Operation();
        pathItem.setPost(postOperation);
        Operation getOperation = new Operation();
        pathItem.setGet(getOperation);
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("GET");

        // WHEN
        Operation operation = generator.getOperation(pathItem, endpointDocumentation);

        // THEN
        assertEquals(operation, getOperation);
    }

    @Test
    public void when_post_method_return_POST_operation() {
        // GIVEN
        PathItem pathItem = new PathItem();
        Operation postOperation = new Operation();
        pathItem.setPost(postOperation);
        Operation getOperation = new Operation();
        pathItem.setGet(getOperation);
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("POST");

        // WHEN
        Operation operation = generator.getOperation(pathItem, endpointDocumentation);

        // THEN
        assertEquals(operation, postOperation);
    }

    @Test
    public void when_put_method_return_PUT_operation() {
        // GIVEN
        PathItem pathItem = new PathItem();
        Operation postOperation = new Operation();
        pathItem.setPost(postOperation);
        Operation putOperation = new Operation();
        pathItem.setPut(putOperation);
        Operation getOperation = new Operation();
        pathItem.setGet(getOperation);
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("PUT");

        // WHEN
        Operation operation = generator.getOperation(pathItem, endpointDocumentation);

        // THEN
        assertEquals(operation, putOperation);
    }

    @Test
    public void when_delete_method_return_DELETE_operation() {
        // GIVEN
        PathItem pathItem = new PathItem();
        Operation postOperation = new Operation();
        pathItem.setPost(postOperation);
        Operation putOperation = new Operation();
        pathItem.setPut(putOperation);
        Operation deleteOperation = new Operation();
        pathItem.setDelete(deleteOperation);
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("DELETE");

        // WHEN
        Operation operation = generator.getOperation(pathItem, endpointDocumentation);

        // THEN
        assertEquals(operation, deleteOperation);
    }

    @Test
    public void when_head_method_return_HEAD_operation() {
        // GIVEN
        PathItem pathItem = new PathItem();
        Operation postOperation = new Operation();
        pathItem.setPost(postOperation);
        Operation putOperation = new Operation();
        pathItem.setPut(putOperation);
        Operation headOperation = new Operation();
        pathItem.setHead(headOperation);
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("HEAD");

        // WHEN
        Operation operation = generator.getOperation(pathItem, endpointDocumentation);

        // THEN
        assertEquals(operation, headOperation);
    }

    @Test
    public void when_option_method_return_OPTIONS_operation() {
        // GIVEN
        PathItem pathItem = new PathItem();
        Operation postOperation = new Operation();
        pathItem.setPost(postOperation);
        Operation optionOperation = new Operation();
        pathItem.setOptions(optionOperation);
        Operation deleteOperation = new Operation();
        pathItem.setDelete(deleteOperation);
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("OPTIONS");

        // WHEN
        Operation operation = generator.getOperation(pathItem, endpointDocumentation);

        // THEN
        assertEquals(operation, optionOperation);
    }

    @Test
    public void when_patch_method_return_PATCH_operation() {
        // GIVEN
        PathItem pathItem = new PathItem();
        Operation postOperation = new Operation();
        pathItem.setPost(postOperation);
        Operation patchOperation = new Operation();
        pathItem.setPatch(patchOperation);
        Operation deleteOperation = new Operation();
        pathItem.setDelete(deleteOperation);
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("PATCH");

        // WHEN
        Operation operation = generator.getOperation(pathItem, endpointDocumentation);

        // THEN
        assertEquals(operation, patchOperation);
    }

    @Test
    public void when_addOperation_with_POST_method_add_it_as_POST_to_pathItem() {
        // GIVEN
        PathItem pathItem = new PathItem();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("POST");
        Operation postOperation = new Operation();

        // WHEN
        generator.addOperationToPathItem(pathItem, postOperation, endpointDocumentation);

        // THEN
        assertEquals(pathItem.getPost(), postOperation);
    }

    @Test
    public void when_addOperation_with_GET_method_add_it_as_GET_to_pathItem() {
        // GIVEN
        PathItem pathItem = new PathItem();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("GET");
        Operation getOperation = new Operation();

        // WHEN
        generator.addOperationToPathItem(pathItem, getOperation, endpointDocumentation);

        // THEN
        assertEquals(pathItem.getGet(), getOperation);
    }

    @Test
    public void when_addOperation_with_PUT_method_add_it_as_PUT_to_pathItem() {
        // GIVEN
        PathItem pathItem = new PathItem();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("PUT");
        Operation putOperation = new Operation();

        // WHEN
        generator.addOperationToPathItem(pathItem, putOperation, endpointDocumentation);

        // THEN
        assertEquals(pathItem.getPut(), putOperation);
    }

    @Test
    public void when_addOperation_with_DELETE_method_add_it_as_DELETE_to_pathItem() {
        // GIVEN
        PathItem pathItem = new PathItem();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("DELETE");
        Operation deleteOperation = new Operation();

        // WHEN
        generator.addOperationToPathItem(pathItem, deleteOperation, endpointDocumentation);

        // THEN
        assertEquals(pathItem.getDelete(), deleteOperation);
    }

    @Test
    public void when_addOperation_with_HEAD_method_add_it_as_HEAD_to_pathItem() {
        // GIVEN
        PathItem pathItem = new PathItem();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("HEAD");
        Operation headOperation = new Operation();

        // WHEN
        generator.addOperationToPathItem(pathItem, headOperation, endpointDocumentation);

        // THEN
        assertEquals(pathItem.getHead(), headOperation);
    }

    @Test
    public void when_addOperation_with_OPTIONS_method_add_it_as_OPTIONS_to_pathItem() {
        // GIVEN
        PathItem pathItem = new PathItem();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("OPTIONS");
        Operation optionsOperation = new Operation();

        // WHEN
        generator.addOperationToPathItem(pathItem, optionsOperation, endpointDocumentation);

        // THEN
        assertEquals(pathItem.getOptions(), optionsOperation);
    }

    @Test
    public void when_addOperation_with_PATCH_method_add_it_as_PATCH_to_pathItem() {
        // GIVEN
        PathItem pathItem = new PathItem();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("PATCH");
        Operation patchOperation = new Operation();

        // WHEN
        generator.addOperationToPathItem(pathItem, patchOperation, endpointDocumentation);

        // THEN
        assertEquals(pathItem.getPatch(), patchOperation);
    }

    @Test
    public void test_updateOperationDocumentation() {
        // GIVEN
        String description = "My test operation";
        String summary = "Get a user info";
        String group = "User";
        String scope = "test_scope";
        Operation operation = new Operation();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setDescription(description);
        endpointDocumentation.setSummary(summary);
        endpointDocumentation.setGroup(group);
        endpointDocumentation.setScope(scope);
        List<RequestParamDocumentation> requestParams = new ArrayList<>();
        RequestParamDocumentation requestUserParam = new RequestParamDocumentation();
        requestUserParam.setName("user");
        requestUserParam.setRequired(true);
        requestUserParam.setDescription("The user the info will be returned about");
        requestUserParam.setType("string");
        requestUserParam.setExampleValue("user@example.com");
        requestParams.add(requestUserParam);
        endpointDocumentation.setRequestParamsDocumentation(requestParams);

        List<ResultParamDocumentation> resultParamsList = new ArrayList<>();
        ResultParamDocumentation resultParamDocumentation = new ResultParamDocumentation();
        resultParamDocumentation.setName("successful response");
        resultParamDocumentation.setDescription("Return user info");
        resultParamDocumentation.setType("User");
        resultParamsList.add(resultParamDocumentation);
        endpointDocumentation.setResultParamsDocumentation(resultParamsList);

        // WHEN
        generator.updateOperationDocumentation(operation, endpointDocumentation);

        // THEN
        assertEquals(operation.getDescription(), description);
        assertEquals(operation.getSummary(), summary);
        assertEquals(operation.getTags().get(0), group);
        assertEquals(operation.getSecurity().get(0).get(OpenAPIGenerator.NOTO_SECURITY_SCHEME).get(0), scope);
        Parameter userParameter = operation.getParameters().get(0);
        assertEquals(userParameter.getName(), "user");
        assertEquals(userParameter.getDescription(), "The user the info will be returned about");
        assertTrue(userParameter.getRequired());
        assertEquals(userParameter.getExample(), "user@example.com");
        ApiResponse response = (ApiResponse) operation.getResponses().get("successful response");
        assertEquals(response.getDescription(), "Return user info");
    }

    @Test
    public void test_createPathItemDocumentation() {
        // GIVEN
        OpenAPIGenerator spyGenerator = spy(new OpenAPIGenerator("", ""));
        PathItem pathItem = new PathItem();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("POST");

        // WHEN
        spyGenerator.createPathItemDocumentation(pathItem, endpointDocumentation);

        // THEN
        verify(spyGenerator).updateOperationDocumentation(any(), eq(endpointDocumentation));
        verify(spyGenerator).addOperationToPathItem(eq(pathItem), any(), eq(endpointDocumentation));
    }

    @Test
    public void test_addDocumentationToPathItem_when_operation_null() {
        // GIVEN
        OpenAPIGenerator spyGenerator = spy(new OpenAPIGenerator("", ""));
        PathItem pathItem = new PathItem();
        MappingEndpointDocumentation endpointDocumentation = new MappingEndpointDocumentation();
        endpointDocumentation.setMethod("POST");
        doNothing().when(spyGenerator).createPathItemDocumentation(pathItem, endpointDocumentation);

        // WHEN
        spyGenerator.addDocumentationToPathItem(pathItem, endpointDocumentation);

        // THEN
        verify(spyGenerator).createPathItemDocumentation(pathItem, endpointDocumentation);
    }

    @Test
    public void test_generateOpenAPI_file() {
        // GIVEN
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
        OpenAPI openAPI = generator.generateOpenAPI(classSet, parsedEndpoints);

        // THEN
        PathItem path = openAPI.getPaths().get("/user");
        Operation getUserOperation = path.getGet();
        assertNotNull(getUserOperation);
        assertEquals(getUserOperation.getSummary(), "Current user's access information");
        assertEquals(getUserOperation.getDescription(), "This web service returns information about the user");
        assertEquals(getUserOperation.getTags().get(0), "User");
        assertEquals(getUserOperation.getSecurity().get(0).get(OpenAPIGenerator.NOTO_SECURITY_SCHEME).get(0), "umbrella");

        Operation putUserOperation = path.getPut();
        assertNotNull(putUserOperation);
        assertEquals(putUserOperation.getSummary(), "Create a user");
        assertEquals(putUserOperation.getDescription(), "This web service creates a new user");
        assertEquals(putUserOperation.getTags().get(0), "User");
    }

    @Test
    public void test_generate_openAPIFile() {
        // GIVEN
        OpenAPIGenerator spyGenerator = spy(new OpenAPIGenerator("1.0", ""));
        Set<Class<?>> classSet = new HashSet<>();
        List<ParsedEndpoint> parsedEndpoints = new ArrayList<>();
        OpenAPI openAPI = new OpenAPI();
        when(spyGenerator.generateOpenAPI(classSet, parsedEndpoints)).thenReturn(openAPI);

        // WHEN
        generator.generateOpenAPIFile(classSet, parsedEndpoints, "openAPI-test.json");

        // THEN
        verify(spyGenerator).generateOpenAPI(classSet, parsedEndpoints);
    }
}
