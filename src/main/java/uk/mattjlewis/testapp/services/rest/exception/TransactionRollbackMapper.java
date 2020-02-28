package uk.mattjlewis.testapp.services.rest.exception;

import javax.transaction.RollbackException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TransactionRollbackMapper implements ExceptionMapper<RollbackException> {
	@Override
	public Response toResponse(RollbackException e) {
		return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
	}
}
