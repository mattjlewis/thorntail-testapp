package uk.mattjlewis.testapp.services.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("hello")
@SuppressWarnings("static-method")
public class HelloWorldResource {
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response doGet() {
        return Response.ok("Hello from Thorntail!").build();
    }
}
