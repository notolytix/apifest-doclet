package com.apifest.doclet.tests.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

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


    /**
     * @apifest.external /user/{streamId}/events
     * @apifest.internal /user/{organization}/{division}/{streamId}/events
     * @apifest.scope umbrella
     * @apifest.auth.type user
     *
     * @apifest.queryParams.division Specifies the current division
     * @apifest.queryParams.division.required true
     *
     * @apifest.re.streamId \w[\w-\\+%.\s*]+
     * @apifest.re.eventId [\w{}][\w-|%.{}]*
     * @apifest.docs.group Event Feed
     * @apifest.docs.summary Publish an event into a stream
     * @apifest.docs.description This endpoint is used to feed key/value flat JSON to a specific stream. Fields will be
     *                           treated either like *TEXT* or *NUMBER* depending on the way the JSON object is
     *                           formatted.
     * @apifest.docs.params.streamId Specifies the stream id to feed data to
     * @apifest.docs.params.streamId.type String
     * @apifest.docs.params.streamId.exampleValue sampleStream
     *
     * @apifest.docs.params.division Specifies the division
     * @apifest.docs.params.division.type String
     * @apifest.docs.params.division.in query
     * @apifest.docs.params.division.required true
     * @apifest.docs.params.division.exampleValue payments
     *
     * @apifest.docs.params.event The event to screen
     * @apifest.docs.params.event.type String
     * @apifest.docs.params.event.exampleValue {"id":3124213, "amount": 23.45, "customerId":423423234, "country":"GER"}
     *
     * @apifest.docs.params.async Whether to process the event immediately or enqueue it for later processing
     * @apifest.docs.params.async.type Boolean
     * @apifest.docs.params.async.exampleValue false
     *
     * @apifest.docs.params.update Allows explicit updates of events in channels with autoupdate turned off
     * @apifest.docs.params.async.type Boolean
     * @apifest.docs.params.async.exampleValue false
     *
     * @apifest.docs.params.excludeRunningValues Removes running values from response
     * @apifest.docs.params.excludeRunningValues.type Boolean
     * @apifest.docs.params.excludeRunningValues.exampleValue true
     *
     * @return
     */
    @POST
    @Path("/{organization}/{division}/{streamId}/events")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eventWithId(@PathParam("organization") String organization, @PathParam("division") String division,
                                @PathParam("streamId") String streamId, @QueryParam("async") Boolean async,
                                @QueryParam("update") Boolean update,
                                @QueryParam("excludeRunningValues") Boolean excludeRunningValues);


}
