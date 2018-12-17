package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import models.Location;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.DefaultLocationService;
import services.LocationService;

import javax.xml.ws.http.HTTPException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Controller for handling location-routes.
 * <p>
 * Implemented methods are:
 * get all locations
 * get location with given id
 * create new location
 */
public class LocationController extends Controller
{

	private final LocationService locationService;
	private final HttpExecutionContext ec;

	@Inject public LocationController(LocationService locationService,
			HttpExecutionContext ec)
	{
		this.locationService = locationService;
		this.ec = ec;
	}

	@SuppressWarnings("Duplicates") public CompletionStage<Result> getLocations()
	{
			return locationService.get().thenApplyAsync(locationStream -> {
				if (locationStream == null)
				{
					Logger.error("Error occurred at {}",
							Thread.currentThread().getStackTrace()[1]);
					return status(404, "Error: Resource not found!");
				}
				//response().setHeader("Access-Control-Allow-Origin", "*"); //Übergangslösung?
				return ok(Json.toJson(
						locationStream.collect(Collectors.toList())));
			}, ec.current());
	}

	@SuppressWarnings("Duplicates") public CompletionStage<Result> getLocation(
			long id)
	{
		return locationService.get(id).thenApplyAsync(location -> {
			if (location == null)
			{
				Logger.error("Error occurred at {}",
						Thread.currentThread().getStackTrace()[1]);
				return status(404, "Error: Resource not found!");
			}
			return ok(Json.toJson(location));
		}, ec.current());

	}

	public CompletionStage<Result> createNewLocation()
	{
		if (checkSession())
		{
			final JsonNode json = Json.toJson(request().body().asJson());
			if (json.isNull() || !validateLocation(json))
			{
				CompletableFuture.supplyAsync(() -> {
					Logger.error("Error occurred at {}",
							Thread.currentThread().getStackTrace()[1]);
					return status(404, "Error: Resource not found!");
				});
			}
			Location locationToPersist = Json.fromJson(json, Location.class);
			return locationService.add(locationToPersist)
					.thenApplyAsync(location -> ok(Json.toJson(location)),
							ec.current());
		}
		else
		{
			return CompletableFuture.supplyAsync(() -> forbidden());
		}
	}

	/**
	 * check if session exists and user is logged in.
	 *
	 * @return {@code true} if user is logged in or else {@code false}
	 */
	@SuppressWarnings("Duplicates") private Boolean checkSession()
	{
		if (!session().isEmpty())
		{
			if (!session("logged-in").isEmpty())
			{
				if (session("logged-in").equals("true"))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Validate location if fields are set correct.
	 *
	 * @param json with location-parameters
	 * @return {@code true} if validation successfull, otherwise {@code false}
	 */
	// TODO: update class-comments if implemented
	private boolean validateLocation(JsonNode json)
	{
		// timestamp-format: "2016-11-16"

		return true;
	}
}
