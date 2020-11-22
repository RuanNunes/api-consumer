package ruan.com.consumer.api;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ruan.com.consumer.domain.Consumer;

@Path("/api/consumers/v1/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "consumers", description = "Operations on consumers.")
public class ConsumerResource {

    @GET
    @Operation(summary = "Get all consumers")
    public List<Consumer> get() {
        return Consumer.listAll();
    }

    @GET
    @Path("{id}")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "404", description = "consumer not found")
    @Operation(summary = "Find consumer by legacyID")
    public Consumer getSindle(@PathParam("id") String id) {
        Consumer entity = Consumer.findByLegacyId(id);

        if (entity == null)
            throw new WebApplicationException("consumer not found", Status.NOT_FOUND);

        return entity;
    }

    @POST
    @Transactional
    @APIResponse(responseCode = "201",
            description = "consumer created",
            content = @Content(schema = @Schema(implementation = Consumer.class)))
    @APIResponse(responseCode = "406", description = "Invalid data")
    @APIResponse(responseCode = "409", description = "consumer already exists")
    @Operation(summary = "Create new consumer")
    public Response create(@Valid Consumer entity) {
        if (entity.id != null)
            throw new WebApplicationException("Id was invalidly set on request",Status.NOT_ACCEPTABLE);

        if (Consumer.exists(entity))
            return Response.status(Status.CONFLICT).build();
    
        entity.persist();
        return Response.ok(entity).status(Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    @APIResponse(responseCode = "200",
            description = "consumer created",
            content = @Content(schema = @Schema(implementation = Consumer.class)))
    @APIResponse(responseCode = "404", description = "consumer not found")
    @APIResponse(responseCode = "409", description = "consumer already exists")
    @Operation(summary = "Edit consumer by legacyID")
    public Response update(@PathParam("id") String id, @Valid Consumer newEntity) {
        Consumer entity = Consumer.findByLegacyId(id);

        if (entity == null)
            throw new WebApplicationException("resource not found", Status.NOT_FOUND);

        entity.updateEntity(newEntity);
        entity.update();
        return Response.ok(entity).status(Status.OK).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    @APIResponse(responseCode = "204", description = "consumer deleted")
    @APIResponse(responseCode = "404", description = "consumer not found")
    @Operation(summary = "Delete consumer by legacyID")
    public Response delete(@PathParam("id") String id) {
        Consumer entity = Consumer.findByLegacyId(id);

        if (entity == null)
            throw new WebApplicationException("resource not found", Status.NOT_FOUND);

        entity.delete();
        return Response.status(Status.NO_CONTENT).build();
    }
}
