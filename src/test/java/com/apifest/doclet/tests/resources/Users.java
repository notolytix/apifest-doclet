package com.apifest.doclet.tests.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
public interface Users {

    /**
     * @apifest.external /user
     * @apifest.internal /user/{organization}
     * @apifest.scope umbrella
     * @apifest.auth.type user
     * @apifest.docs.group User
     * @apifest.docs.summary Create a user
     * @apifest.docs.description This web service creates a new user
     *
     * @return
     */
    @PUT
    @Path("/{organization}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createUser(@PathParam("organization") String organization,
            CreateUserRequest request);

    /**
     * @apifest.external /user
     * @apifest.internal /user
     * @apifest.scope umbrella
     * @apifest.auth.type user
     * @apifest.docs.group User
     * @apifest.docs.summary Current user's access information
     * @apifest.docs.description This web service returns information about the user
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getCurrentUserInfo(@HeaderParam("X-Current-User") String username);
}
