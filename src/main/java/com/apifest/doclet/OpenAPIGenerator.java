package com.apifest.doclet;

import com.apifest.api.MappingEndpointDocumentation;
import com.apifest.api.params.RequestParamDocumentation;
import com.apifest.api.params.ResultParamDocumentation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.ws.rs.HttpMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OpenAPIGenerator {

    private static final String OAUTH_SCHEMA_TYPE = "oauth2";

    void generateOpenAPIFile(Set<Class<?>> classes, List<ParsedEndpoint> parsedEndpoints, String outputFile) throws IOException {
        JsonMapper mapper = new JsonMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        OpenAPI jaxrsOpenAPI = createJaxrsOpenAPI(classes);
        OpenAPI fullOpenAPI = createOpenAPIWithInfo();
        fullOpenAPI.components(jaxrsOpenAPI.getComponents());
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
                        System.out.println("pathItem in jaxrsOpenAPI is null: " + parsed.getMappingEndpoint().getInternalEndpoint());
                    }
                } else {
                    //add the new operation
                    PathItem existingPathItem = fullOpenAPI.getPaths().get(endpointPath);
                    PathItem jaxrsPathItem = jaxrsOpenAPI.getPaths().get(parsed.getMappingEndpoint().getInternalEndpoint());
                    Operation operation = getOperation(jaxrsPathItem, endpoint);
                    if (operation != null) {
                        updateOperationDocumentation(endpoint, operation);
                        addOperationToPathItem(endpoint, existingPathItem, operation);
                    } else {
                        createPathItemDocumentation(existingPathItem, endpoint);
                    }
                }
            }
        }
        mapper.writeValue(new File(outputFile), fullOpenAPI);
    }

    protected OpenAPI createOpenAPIWithInfo() {
        OpenAPI openAPI = new OpenAPI();
        // TODO: add version param
        Info info = new Info()
                .version("1.0")
                .title("NOTO API");

        Contact contact = new Contact()
                .name("NOTO")
                .email("info@notolytix.com")
                .url("https://noto360.com/");
        info.setContact(contact);
        openAPI.setInfo(info);
        // TODO: add parameters for the server/s
        List<Server> serverList = new ArrayList<>();
        Server server = new Server();
        server.description("my local env");
        server.url("https://api.builder.lxd/1.0");
        serverList.add(server);
        openAPI.servers(serverList);

        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("oauth_20", "");
        List<SecurityRequirement> securityRequirementList = new ArrayList<>();
        securityRequirementList.add(securityRequirement);
        openAPI.security(securityRequirementList);
        openAPI.setPaths(new Paths());
        return openAPI;
    }

    protected OpenAPI createJaxrsOpenAPI(Set<Class<?>> classes) {
        Reader reader = new Reader(new OpenAPI());
        return reader.read(classes);
    }

    private void createPathItemDocumentation(PathItem path, MappingEndpointDocumentation endpoint) {
        Operation operation = new Operation();
        operation.setDescription(endpoint.getDescription());
        updateOperationDocumentation(endpoint, operation);
        addOperationToPathItem(endpoint, path, operation);
    }

    protected static void addOperationToPathItem(MappingEndpointDocumentation endpoint, PathItem path, Operation operation) {
        switch (endpoint.getMethod()) {
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

    protected void updateOperationDocumentation(MappingEndpointDocumentation endpoint, Operation operation) {
        operation.setDescription(endpoint.getDescription());
        operation.setSummary(endpoint.getSummary());
        List<String> tags = new ArrayList<>();
        tags.add(endpoint.getGroup());
        operation.setTags(tags);
        if (endpoint.getRequestParamsDocumentation() != null && !endpoint.getRequestParamsDocumentation().isEmpty()) {
            List<Parameter> parameters = new ArrayList<>();
            for (RequestParamDocumentation paramDocumentation : endpoint.getRequestParamsDocumentation()) {
                Parameter param = new Parameter();
                param.setName(paramDocumentation.getName());
                param.setDescription(paramDocumentation.getDescription());
                param.setRequired(paramDocumentation.isRequired());
                param.example(paramDocumentation.getExampleValue());
                parameters.add(param);
            }
            operation.setParameters(parameters);

            ApiResponses responses = new ApiResponses();
            for (ResultParamDocumentation resultParamDocumentation : endpoint.getResultParamsDocumentation()) {
                ApiResponse response = new ApiResponse();
                response.setDescription(resultParamDocumentation.getDescription());
                responses.addApiResponse("200", response);
            }
            operation.setResponses(responses);
        }

        if (endpoint.getScope() != null) {
            List<SecurityRequirement> securityRequirements = new ArrayList<>();
            SecurityRequirement requirement = new SecurityRequirement();
            requirement.addList(OAUTH_SCHEMA_TYPE,endpoint.getScope());
            securityRequirements.add(requirement);
            operation.setSecurity(securityRequirements);
        }
    }

    protected void addDocumentationToPathItem(PathItem pathItem, MappingEndpointDocumentation endpoint) {
        Operation operation = getOperation(pathItem, endpoint);
        if (operation != null) {
            updateOperationDocumentation(endpoint, operation);
        } else {
            createPathItemDocumentation(pathItem, endpoint);
        }

        if (endpoint.getResultParamsDocumentation() != null && !endpoint.getResultParamsDocumentation().isEmpty()) {
            ApiResponses responses = new ApiResponses();
            for (ResultParamDocumentation resultParamDocumentation : endpoint.getResultParamsDocumentation()) {
                ApiResponse response = new ApiResponse();
                response.setDescription(resultParamDocumentation.getDescription());
                responses.addApiResponse("200", response);
            }
            operation.setResponses(responses);
        }
    }

    protected Operation getOperation(PathItem pathItem, MappingEndpointDocumentation endpoint) {
        Operation operation = null;
        switch (endpoint.getMethod()) {
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
