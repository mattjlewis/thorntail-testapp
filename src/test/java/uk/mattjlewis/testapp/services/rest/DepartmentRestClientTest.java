package uk.mattjlewis.testapp.services.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import uk.mattjlewis.testapp.model.Department;
import uk.mattjlewis.testapp.model.Employee;
import uk.mattjlewis.testapp.services.rest.DepartmentResource;
import uk.mattjlewis.testapp.services.service.DepartmentService;

@SuppressWarnings("static-method")
@RunWith(Arquillian.class)
//@DefaultDeployment(type = DefaultDeployment.Type.JAR)
public class DepartmentRestClientTest {
	private static final String DEPARTMENT_PATH = "department";

	@Deployment(testable = false)
	public static Archive createDeployment() throws Exception {
		return ShrinkWrap.create(JAXRSArchive.class, "myapp.war").addClass(DepartmentResource.class)
				.addPackage(Department.class.getPackage())
				.addPackage(DepartmentService.class.getPackage()).setContextRoot("rest")
				.addAsWebInfResource(new File("src/main/resources/META-INF/beans.xml"))
				.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Department.class.getClassLoader()), "classes/META-INF/persistence.xml")
				.addAsResource("project-test-defaults.yml", "project-defaults.yml").addAllDependencies();
	}

	@Test
	public void restClientDepartmentTest() {
		Client client = ClientBuilder.newClient();
		// Required to use PATCH when using the Jersey REST client
		//client.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, Boolean.TRUE);
		WebTarget root = client.target("http://localhost:8080").path("rest");

		// Create a department with employees
		List<Employee> employees = Arrays.asList(new Employee("Matt", "matt@test.org", "Coffee"),
				new Employee("Fred", "fred@test.org", "Beer"));
		Department dept = new Department("IT", "London", employees);
		Department created_dept = null;
		try (Response response = root.path(DEPARTMENT_PATH).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(dept))) {
			assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
			System.out.println("Response location: " + response.getLocation());
			created_dept = response.readEntity(Department.class);
			assertNotNull(created_dept);
			assertNotNull(created_dept.getId());
			assertEquals(dept.getName(), created_dept.getName());
			assertEquals(dept.getLocation(), created_dept.getLocation());
		}

		// Find the department
		Department found_dept = null;
		try {
			found_dept = root.path(DEPARTMENT_PATH).path(created_dept.getId().toString())
					.request(MediaType.APPLICATION_JSON).get(Department.class);
			assertNotNull(found_dept);
			assertNotNull(found_dept.getId());
			assertEquals(dept.getName(), found_dept.getName());
			assertEquals(employees.size(), found_dept.getEmployees().size());
			assertEquals(dept.getEmployees().size(), found_dept.getEmployees().size());
			assertEquals(1, found_dept.getVersion().intValue());
		} catch (WebApplicationException e) {
			fail("Unexpected response status: " + e.getResponse().getStatus());
			// Here simply to avoid the compiler warning about a potential null pointer access
			return;
		}

		// Update the department
		found_dept.setName(dept.getName() + " - updated");
		try {
			Department updated_dept = root.path(DEPARTMENT_PATH).path(created_dept.getId().toString())
					.request(MediaType.APPLICATION_JSON)
					.method(HttpMethod.PATCH, Entity.json(found_dept), Department.class);
			assertNotNull(updated_dept);
			assertEquals(dept.getName() + " - updated", updated_dept.getName());
			assertEquals(found_dept.getVersion().intValue() + 1, updated_dept.getVersion().intValue());
		} catch (WebApplicationException e) {
			fail("Unexpected response status: " + e.getResponse().getStatus());
		}

		// Should trigger bean validation failure
		dept = new Department("012345678901234567890123456789", "London");
		try (Response response = root.path(DEPARTMENT_PATH).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(dept, MediaType.APPLICATION_JSON))) {
			assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		}

		/*
		 * // Should pass bean validation but trigger database constraint violation dept = new Department("HR",
		 * "Reading", Arrays.asList(new Employee("Rod", "rod@test.org", "Water"), new Employee("Jane", "jane@test.org",
		 * "012345678901234567890123456789"), new Employee("Freddie", "freddie@test.org", "Tea"))); try (Response
		 * response = root.path(DEPARTMENT_PATH).request(MediaType.APPLICATION_JSON) .post(Entity.entity(dept,
		 * MediaType.APPLICATION_JSON))) { assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
		 * }
		 */
	}
}
