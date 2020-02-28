package uk.mattjlewis.testapp.services.rest;

import java.security.Principal;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@ApplicationScoped
@Path("protected")
@SuppressWarnings("static-method")
@Produces(MediaType.TEXT_PLAIN)
@DenyAll
public class ProtectedResource {
	@GET
	public String securityContextTest(@Context SecurityContext context) {
		Principal principal = context.getUserPrincipal();
		
		return "Protected Resource\n"
				+ "Principal name: " + principal.getName() + "\n"
				+ "Has role 'Application/HelidonTxTestSP': " + context.isUserInRole("Application/HelidonTxTestSP") + "\n"
				+ "Has role 'Application/user': " + context.isUserInRole("Application/user") + "\n"
				+ "Has role 'Internal/everyone': " + context.isUserInRole("Internal/everyone") + "\n"
				+ "Has role 'HelidonTxTestUser': " + context.isUserInRole("HelidonTxTestUser") + "\n"
				+ "Has role 'admin': " + context.isUserInRole("admin") + "\n"
				+ "Has role 'user': " + context.isUserInRole("user") + "\n";
	}

	@GET
	@Path("applicationHelidon")
	@RolesAllowed("Application/HelidonTxTestSP")
	public String applicationHelidon(@Context SecurityContext context) {
		return "User '" + context.getUserPrincipal().getName() + "' has role Application/HelidonTxTestSP";
	}

	@GET
	@Path("applicationUser")
	@RolesAllowed("Application/user")
	public String applicationUser(@Context SecurityContext context) {
		return "User '" + context.getUserPrincipal().getName() + "' has role Application/user";
	}

	@GET
	@Path("everyone")
	@RolesAllowed("Internal/everyone")
	public String everyone(@Context SecurityContext context) {
		return "User '" + context.getUserPrincipal().getName() + "' has role Internal/everyone";
	}

	@GET
	@Path("helidonTxTestUser")
	@RolesAllowed("HelidonTxTestUser")
	public String helidonTxTestUser(@Context SecurityContext context) {
		return "User '" + context.getUserPrincipal().getName() + "' has role HelidonTxTestUser";
	}

	@GET
	@Path("admin")
	@RolesAllowed("admin")
	public String adminRole(@Context SecurityContext context) {
		return "User '" + context.getUserPrincipal().getName() + "' has role admin";
	}

	@GET
	@Path("user")
	@RolesAllowed("user")
	public String userRole(@Context SecurityContext context) {
		return "User '" + context.getUserPrincipal().getName() + "' has role user";
	}
}
