package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Location;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.LocationService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Controller for handling location-routes.
 *
 * Implemented methods are:
 *   getLocations():			get all locations
 *   getLocation(ID):			get location with given id
 *   createNewLocation():		create new location
 *   validateLocation(Json):	validate json of location (no logic implemented)
 */
public class LocationController extends Controller
{

	private final LocationService locationService;
	private final HttpExecutionContext ec;
	private final CookieChecker cc;

	@Inject public LocationController(LocationService locationService,
			CookieChecker cc,
			HttpExecutionContext ec)
	{
		this.locationService = locationService;
		this.ec = ec;
		this.cc = cc;
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
			return ok(Json.toJson(locationStream.collect(Collectors.toList())));
		}, ec.current());
	}

	@SuppressWarnings("Duplicates") public CompletionStage<Result> getLocation(
			long id)
	{
		if (cc.checkCookie(request().cookie("logged-in")))
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
		else
		{
			return CompletableFuture.supplyAsync(() -> forbidden());
		}
	}

	public CompletionStage<Result> createNewLocation()
	{
		if (cc.checkCookie(request().cookie("logged-in")))
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
	 * Validate location if fields are set correct.
	 *
	 * @param json with location-parameters
	 * @return {@code true} if validation successfull, otherwise {@code false}
	 */
	private boolean validateLocation(JsonNode json)
	{
		// timestamp-format: "2016-11-16"

		return true;
	}
}
