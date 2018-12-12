package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.User;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Controller for handling user-routes.
 *
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

	@Inject
	public UserController(UserService userService,
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

	public CompletionStage<Result> createNewUser()
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
		return userService.add(userToPersist)
				.thenApplyAsync(user -> ok(Json.toJson(user)), ec.current());
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

	public CompletionStage<Result> deleteUser(
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

	public CompletionStage<Result> updateUser()
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

	public CompletionStage<Result> userExists()
	{
		Optional<String> auth = request().getHeaders().get("Authorization");
		try {

		String[] password = decodeBasicAuth(auth.get());
		System.out.println(password[0]);
		System.out.println(password[1]);
		if(password[0].equals("corsin")) {
			return (CompletionStage<Result>) ok("Validation successful");
		} else {
			return (CompletionStage<Result>) forbidden("Validation failed");
		}
		} catch (Exception e) {
			Logger.error("Error occurred at {}",Thread.currentThread().getStackTrace()[1]);
		}
		return null;

//		final JsonNode json = Json.toJson(request().body().asJson());
//		if (json.isNull() || !validateUser(json))
//		{
//			CompletableFuture.supplyAsync(() -> {
//				Logger.error("Error occurred at {}",
//						Thread.currentThread().getStackTrace()[1]);
//				return status(404, "Error: Resource not found!");
//			});
//		}
//		User userToCheck = Json.fromJson(json, User.class);
//		return userService.check(userToCheck)
//				.thenApplyAsync(aBoolean -> {
//					if (aBoolean)
//					{
//						 return ok("Validation successful");
//					}
//					else
//					{
//						return forbidden("Validation failed");
//					}
//				}, ec.current());
	}


	private String[] decodeBasicAuth(String auth) throws IOException
	{
		String baStr = auth.replaceFirst("Basic ", "");
		String[] password= new String(new sun.misc.BASE64Decoder().decodeBuffer(baStr), "UTF-8").split(":");
		return password;
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
