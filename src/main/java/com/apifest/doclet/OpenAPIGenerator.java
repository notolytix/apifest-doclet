package com.apifest.doclet;

import com.apifest.api.MappingEndpointDocumentation;
import com.apifest.api.params.ParameterIn;
import com.apifest.api.params.RequestParamDocumentation;
import com.apifest.api.params.ResultParamDocumentation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.ws.rs.HttpMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OpenAPIGenerator {

    protected static final String NOTO_SECURITY_SCHEME = "noto-oauth";
    protected String apiVersion = null;
    protected String apiTestServer = null;

    public OpenAPIGenerator(String apiVersion, String apiTestServer) {
        this.apiVersion = apiVersion;
        this.apiTestServer = apiTestServer;
    }

    void generateOpenAPIFile(Set<Class<?>> classes, List<ParsedEndpoint> parsedEndpoints, String outputFile) {
        var mapper = ObjectMapperFactory.createJson();
        mapper.writer(new DefaultPrettyPrinter());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        OpenAPI openAPI = generateOpenAPI(classes, parsedEndpoints);
        try {
            mapper.writeValue(new File(outputFile), openAPI);
        } catch (IOException e) {
            System.err.println("Cannot write OpenAPI file");
            throw new RuntimeException(e);
        }
    }

    protected OpenAPI generateOpenAPI(Set<Class<?>> classes, List<ParsedEndpoint> parsedEndpoints) {
        OpenAPI jaxrsOpenAPI = createJaxrsOpenAPI(classes);
        OpenAPI fullOpenAPI = createOpenAPIWithInfo();
        fullOpenAPI.setComponents(jaxrsOpenAPI.getComponents() == null ? new Components() : jaxrsOpenAPI.getComponents());
        var oAuthFlow = new OAuthFlow();
        oAuthFlow.setTokenUrl(apiTestServer + "/oauth20/tokens");
        var oAuthFlows = new OAuthFlows();
        oAuthFlows.setPassword(oAuthFlow);
        var securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.OAUTH2);
        securityScheme.setFlows(oAuthFlows);
        fullOpenAPI.getComponents().addSecuritySchemes(NOTO_SECURITY_SCHEME, securityScheme);
        for (ParsedEndpoint parsed : parsedEndpoints) {
            MappingEndpointDocumentation endpoint = parsed.getMappingEndpointDocumentation();
            if (endpoint != null && !endpoint.isHidden()) {
                String endpointPath = parsed.getEndpointPathWithoutVersion();
                if (fullOpenAPI.getPaths().get(endpointPath) == null) {
                    // if the path does not exist, add it
                    PathItem pathItem = jaxrsOpenAPI.getPaths().get(parsed.getMappingEndpoint().getInternalEndpoint());
                    // copy the JAXRS documentation and add ApiFest documentation
                    if (pathItem != null) {
                        addDocumentationToPathItem(pathItem, endpoint);
                        fullOpenAPI.path(endpointPath, pathItem);
                    } else {
                        // should not happen
                        System.out.println("pathItem in jaxrsOpenAPI is null: " + parsed.getMappingEndpoint().getInternalEndpoint());
                    }
                } else {
                    //add the new operation
                    PathItem existingPathItem = fullOpenAPI.getPaths().get(endpointPath);
                    PathItem jaxrsPathItem = jaxrsOpenAPI.getPaths().get(parsed.getMappingEndpoint().getInternalEndpoint());
                    Operation operation = getOperation(jaxrsPathItem, endpoint);
                    if (operation != null) {
                        updateOperationDocumentation(operation, endpoint);
                        addOperationToPathItem(existingPathItem, operation, endpoint);
                    } else {
                        createPathItemDocumentation(existingPathItem, endpoint);
                    }
                }
            }
        }
        return fullOpenAPI;
    }

    protected OpenAPI createOpenAPIWithInfo() {
        OpenAPI openAPI = new OpenAPI();
        Info info = new Info()
                .version(apiVersion)
                .title("NOTO API");
        Contact contact = new Contact()
                .name("NOTO")
                .email("info@notolytix.com")
                .url("https://noto360.com");
        info.setContact(contact);
        openAPI.setInfo(info);
        List<Server> serverList = new ArrayList<>();
        Server server = new Server();
        server.description("API test environment");
        server.url(apiTestServer);
        serverList.add(server);
        openAPI.servers(serverList);
        openAPI.setPaths(new Paths());
        List<SecurityRequirement> securityRequirements = new ArrayList<>();
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList(NOTO_SECURITY_SCHEME, new ArrayList<>());
        securityRequirements.add(securityRequirement);
        openAPI.setSecurity(securityRequirements);
        return openAPI;
    }

    protected OpenAPI createJaxrsOpenAPI(Set<Class<?>> classes) {
        Reader reader = new Reader(new OpenAPI());
        return reader.read(classes);
    }

    protected void createPathItemDocumentation(PathItem path, MappingEndpointDocumentation endpointDocumentation) {
        Operation operation = new Operation();
        operation.setDescription(endpointDocumentation.getDescription());
        updateOperationDocumentation(operation, endpointDocumentation);
        addOperationToPathItem(path, operation, endpointDocumentation);
    }

    protected void addOperationToPathItem(PathItem path, Operation operation, MappingEndpointDocumentation endpointDocumentation) {
        switch (endpointDocumentation.getMethod()) {
            case HttpMethod.POST:
                path.post(operation);
                break;
            case HttpMethod.GET:
                path.get(operation);
                break;
            case HttpMethod.PUT:
                path.put(operation);
                break;
            case HttpMethod.DELETE:
                path.delete(operation);
                break;
            case HttpMethod.HEAD:
                path.head(operation);
                break;
            case HttpMethod.OPTIONS:
                path.options(operation);
                break;
            case HttpMethod.PATCH:
                path.patch(operation);
                break;
            default:
                // no default
        }
    }

    protected void updateOperationDocumentation(Operation operation, MappingEndpointDocumentation endpointDocumentation) {
        operation.setDescription(endpointDocumentation.getDescription());
        operation.setSummary(endpointDocumentation.getSummary());
        List<String> tags = new ArrayList<>();
        tags.add(endpointDocumentation.getGroup());
        operation.setTags(tags);
        if (endpointDocumentation.getRequestParamsDocumentation() != null && !endpointDocumentation.getRequestParamsDocumentation().isEmpty()) {
            List<Parameter> parameters = (operation.getParameters() != null) ? operation.getParameters() : new ArrayList<>();
            Map<String, Parameter> parametersMap = new HashMap<>();
            parameters.stream().forEach(parameter -> parametersMap.put(parameter.getName(), parameter));
            List<Parameter> newParameters = new ArrayList<>();
            for (RequestParamDocumentation paramDocumentation : endpointDocumentation.getRequestParamsDocumentation()) {
                Parameter param = (parametersMap.containsKey(paramDocumentation.getName())) ? parametersMap.get(paramDocumentation.getName()) : new Parameter();
                param.setName(paramDocumentation.getName());
                param.setDescription(paramDocumentation.getDescription());
                param.setRequired(paramDocumentation.isRequired());
                param.example(paramDocumentation.getExampleValue());
                if (paramDocumentation.getIn() != null) {
                    param.in(paramDocumentation.getIn().toString());
                } else {
                    param.in(ParameterIn.PATH.toString());
                }
                newParameters.add(param);
            }
            operation.setParameters(newParameters);
        }

        if (endpointDocumentation.getResultParamsDocumentation() != null && !endpointDocumentation.getResultParamsDocumentation().isEmpty()) {
            ApiResponses responses = new ApiResponses();
            for (ResultParamDocumentation resultParamDocumentation : endpointDocumentation.getResultParamsDocumentation()) {
                ApiResponse response = new ApiResponse();
                response.setDescription(resultParamDocumentation.getDescription());
                responses.addApiResponse(resultParamDocumentation.getName(), response);
            }
            operation.setResponses(responses);
        }

        if (endpointDocumentation.getScope() != null) {
            List<SecurityRequirement> securityRequirements = new ArrayList<>();
            SecurityRequirement requirement = new SecurityRequirement();
            requirement.addList(NOTO_SECURITY_SCHEME, endpointDocumentation.getScope());
            securityRequirements.add(requirement);
            operation.setSecurity(securityRequirements);
        }
    }

    protected void addDocumentationToPathItem(PathItem pathItem, MappingEndpointDocumentation endpointDocumentation) {
        Operation operation = getOperation(pathItem, endpointDocumentation);
        if (operation != null) {
            updateOperationDocumentation(operation, endpointDocumentation);
        } else {
            createPathItemDocumentation(pathItem, endpointDocumentation);
        }
    }

    protected Operation getOperation(PathItem pathItem, MappingEndpointDocumentation endpointDocumentation) {
        Operation operation = null;
        switch (endpointDocumentation.getMethod()) {
            case HttpMethod.POST:
                operation = pathItem.getPost();
                break;
            case HttpMethod.GET:
                operation = pathItem.getGet();
                break;
            case HttpMethod.PUT:
                operation = pathItem.getPut();
                break;
            case HttpMethod.DELETE:
                operation = pathItem.getDelete();
                break;
            case HttpMethod.HEAD:
                operation = pathItem.getHead();
                break;
            case HttpMethod.OPTIONS:
                operation = pathItem.getOptions();
                break;
            case HttpMethod.PATCH:
                operation = pathItem.getPatch();
                break;
            default:
                // no default
        }
        return operation;
    }
}
