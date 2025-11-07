package com.example.command;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/commands")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommandResource {

    @Inject
    CommandService commandService;

    @POST
    public Response execute(CommandRequest request) {
        CommandResult result = commandService.execute(request);
        Response.ResponseBuilder builder = switch (result.status()) {
            case "SUCCESS" -> Response.ok(result);
            case "ERROR" -> Response.status(Response.Status.BAD_REQUEST).entity(result);
            default -> Response.serverError().entity(result);
        };
        return builder.build();
    }
}
