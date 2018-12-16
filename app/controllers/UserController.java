package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.User;
import org.apache.commons.codec.binary.Base64;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Controller for handling user-routes.
 * <p>
 * Implemented methods are:
 * get all users
 * get user with given id
 * create new user
 * update user with given id
 * delete user with given id
 */
public class UserController extends Controller
{

	private final UserService userService;
	private final HttpExecutionContext ec;

	@Inject public UserController(UserService userService,
			HttpExecutionContext ec)
	{
		this.ec = ec;
		this.userService = userService;
	}

	public CompletionStage<Result> getUsers()
	{
		return userService.get().thenApplyAsync(userStream -> {
			if (userStream == null)
			{
				Logger.error("Error occurred at {}",
						Thread.currentThread().getStackTrace()[1]);
				return status(404, "Error: Resource not found");
			}
			return ok(Json.toJson(userStream.collect(Collectors.toList())));
		}, ec.current());
	}

	@SuppressWarnings("Duplicates") public CompletionStage<Result> createNewUser()
	{
		final JsonNode json = Json.toJson(request().body().asJson());
		if (json.isNull() || !validateUser(json))
		{
			CompletableFuture.supplyAsync(() -> {
				Logger.error("Error occurred at {}",
						Thread.currentThread().getStackTrace()[1]);
				return status(404, "Error: Resource not found!");
			});
		}
		User userToPersist = Json.fromJson(json, User.class);

		// first check if user exists in database
		// if user exists => result = status 404
		// if user doesn't exists = add user to database and result = ok
		return userService.check(userToPersist.getUsername())
				.thenApplyAsync(aBoolean -> aBoolean, ec.current())
				.thenCompose(aBoolean -> {
					if (aBoolean) {
						return CompletableFuture.supplyAsync(() -> {
							return status(404, "Error: Resource not found!");
						});
					}
					else {
					return userService.add(userToPersist)
							.thenApplyAsync(user -> ok(Json.toJson(user)), ec.current());
					}
				});
	}

	public CompletionStage<Result> getUser(long id)
	{
		return userService.get(id).thenApplyAsync(user -> {
			if (user == null)
			{
				Logger.error("Error occurred at  {}",
						Thread.currentThread().getStackTrace()[1]);
				return status(404, "Error: Resource not found!");
			}
			return ok(Json.toJson(user));
		}, ec.current());
	}

	@SuppressWarnings("Duplicates") public CompletionStage<Result> deleteUser(
			long id)
	{
		return userService.delete(id).thenApplyAsync(user -> {
			if (user == null)
			{
				Logger.error("Error occurred at {}",
						Thread.currentThread().getStackTrace()[1]);
				return status(404, "Error: Resource not found!");
			}
			return ok(Json.toJson(user));
		}, ec.current());
	}

	@SuppressWarnings("Duplicates") public CompletionStage<Result> updateUser()
	{
		final JsonNode json = Json.toJson(request().body().asJson());
		if (json.isNull() || !validateUser(json))
		{
			CompletableFuture.supplyAsync(() -> {
				Logger.error("Error occurred at {}",
						Thread.currentThread().getStackTrace()[1]);
				return status(404, "Error: Resource not found!");
			});
		}
		User userToUpdate = Json.fromJson(json, User.class);
		return userService.update(userToUpdate)
				.thenApplyAsync(user -> ok(Json.toJson(user)), ec.current());
	}

	/**
	 * Client sends authorization request:
	 * 	GET: header { key: Authorization, value: "Basic username:password" }
	 * @return Result ok() when user is authorized or result forbidden if not
	 */
	public CompletionStage<Result> userValidate()
	{
		Optional<String> auth = request().getHeaders().get("Authorization");
		if (auth.isPresent())
		{
			try
			{
				String[] credentials = auth.get()
						.replace("Basic ", "")
						.split(":");
				return userService.validate(credentials)
						.thenApplyAsync(aBoolean -> {
							if (aBoolean)
							{
								return ok("Validation successful");
							}
							else
							{
								return forbidden("Validation failed");
							}
						}, ec.current());

			} catch (Exception e)
			{
				Logger.error("Error occurred at {}", Thread.currentThread().getStackTrace()[1]);
			}
		}
		return CompletableFuture.supplyAsync(() -> badRequest("Bad Request"));
	}

	/**
	 * Validate user if fields are set correct.
	 *
	 * @param json with user-parameters
	 * @return {@code true} if validation successful, otherwise {@code false}
	 */
	// TODO: update class-comments if implemented
	private boolean validateUser(JsonNode json)
	{
		// timestamp-format: "2016-11-16"

		return true;
	}
}
