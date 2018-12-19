package controllers;

import models.Location;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.FORBIDDEN;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class LocationControllerTest extends WithApplication {

	private final String uri = "https://quiet-shelf-61716.herokuapp.com/api/locations";

	@Override
	protected Application provideApplication() {
		return new GuiceApplicationBuilder().build();
	}

	@Test
	public void testGetLocations() {
		Http.RequestBuilder request = new Http.RequestBuilder()
				.method(GET)
				.uri(uri);

		Result result = route(app, request);
		assertEquals(OK, result.status());
	}

	@Test
	public void testGetLocation1() {
		Http.RequestBuilder request = new Http.RequestBuilder()
				.method(GET)
				.uri(uri + "/1");

		Result result = route(app, request);
		assertEquals(FORBIDDEN, result.status());
	}

	@Test
	public void testCreateLocation() {

		Location location = new Location();
		location.setImageKey("-P_QunqnlgpOIwttmkvc5g");

		Http.RequestBuilder request = new Http.RequestBuilder()
				.method(POST)
				.uri(uri)
				.bodyJson(Json.toJson(location));

		Result result = route(app, request);
		assertEquals(FORBIDDEN, result.status());
	}
}
