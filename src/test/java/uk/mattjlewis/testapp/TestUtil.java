package uk.mattjlewis.testapp;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestUtil {
	public static String createHttpBasicAuthToken(String username, String password) {
		return "Basic "
				+ Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
	}
}
