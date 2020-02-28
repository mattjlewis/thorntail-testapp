package uk.mattjlewis.testapp.services.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import uk.mattjlewis.testapp.services.rest.HelloWorldResource;

@SuppressWarnings("static-method")
@RunWith(Arquillian.class)
public class HelloWorldTest {
	@Deployment(testable = false)
	public static Archive createDeployment() throws Exception {
		return ShrinkWrap.create(JAXRSArchive.class, "myapp.war").addClass(HelloWorldResource.class)
				.setContextRoot("rest").addAsResource("project-test-defaults.yml", "project-defaults.yml").addAllDependencies();
	}

	@Test
	@RunAsClient
	public void helloWorld() {
		Client client = ClientBuilder.newClient();
		WebTarget root = client.target("http://localhost:8080").path("rest").path("hello");

		try (Response response = root.request(MediaType.TEXT_PLAIN).get()) {
			assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
			String message = response.readEntity(String.class);
			assertNotNull(message);
		}
	}

	@Test
	@RunAsClient
	public void testSimple() throws IOException {
		HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8080/rest/hello").openConnection();
		con.setRequestMethod("GET");
		con.addRequestProperty("Accept", MediaType.TEXT_PLAIN);
		assertEquals(Response.Status.OK.getStatusCode(), con.getResponseCode());
		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			assertEquals("Hello from Thorntail!", in.readLine());
		}
	}
}
