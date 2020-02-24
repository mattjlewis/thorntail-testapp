package uk.mattjlewis.thorntail.testapp;

import static org.junit.Assert.assertNotNull;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;

@RunWith(Arquillian.class)
@DefaultDeployment(type = DefaultDeployment.Type.JAR)
public class InContainerTest {
	@ArquillianResource
	InitialContext context;

	@Test
	public void testDataSourceIsBound() throws Exception {
		DataSource ds = (DataSource) context.lookup("java:jboss/datasources/TestAppDataSource");
		assertNotNull(ds);
	}
}
