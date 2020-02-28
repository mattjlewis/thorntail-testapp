package uk.mattjlewis.testapp.services.rest.exception;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {
	@Override
	public Response toResponse(PersistenceException e) {
		return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
	}
}
