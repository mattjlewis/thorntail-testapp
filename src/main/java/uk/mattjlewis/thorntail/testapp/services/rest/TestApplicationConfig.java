package uk.mattjlewis.thorntail.testapp.services.rest;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

import uk.mattjlewis.thorntail.testapp.services.rest.exception.EntityNotFoundMapper;
import uk.mattjlewis.thorntail.testapp.services.rest.exception.NoResultMapper;
import uk.mattjlewis.thorntail.testapp.services.rest.exception.PersistenceExceptionMapper;
import uk.mattjlewis.thorntail.testapp.services.rest.exception.TransactionRollbackMapper;

@ApplicationScoped
@ApplicationPath("/rest")
@LoginConfig(authMethod = "MP-JWT")
public class TestApplicationConfig extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();

		// Add resources (alphabetical)
		classes.addAll(Set.of(DepartmentResource.class, HelloWorldResource.class, ProtectedResource.class));
		// Add exception mappers (alphabetical)
		classes.addAll(Set.of(EntityNotFoundMapper.class, NoResultMapper.class, PersistenceExceptionMapper.class,
				TransactionRollbackMapper.class));

		return classes;
	}
}
