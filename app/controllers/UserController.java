package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.User;
import org.apache.commons.codec.binary.Base64;
import play.Logger;
import play.api.mvc.Cookie;
import play.api.mvc.Session;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.Option;
import services.UserService;

import java.io.IOException;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
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
					if (aBoolean)
					{
						return CompletableFuture.supplyAsync(() -> {
							return status(404, "Error: Resource not found!");
						});
					}
					else
					{
						return userService.add(userToPersist)
								.thenApplyAsync(user -> ok(Json.toJson(user)),
										ec.current());
					}
				});
	}

	public CompletionStage<Result> getUser(long id)
	{
		if(checkSession())
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
		else
		{
			return CompletableFuture.supplyAsync(() -> forbidden());
		}
	}

	@SuppressWarnings("Duplicates") public CompletionStage<Result> deleteUser(
			long id)
	{
		if (checkSession()) {
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
		else
		{
			return CompletableFuture.supplyAsync(() -> forbidden());
		}
	}

	@SuppressWarnings("Duplicates") public CompletionStage<Result> updateUser()
	{
		if (checkSession())
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
					.thenApplyAsync(user -> ok(Json.toJson(user)),
							ec.current());
		}
		else
		{
			return CompletableFuture.supplyAsync(() -> forbidden());
		}
	}

	/**
	 * Client sends authorization request:
	 * GET: header { key: Authorization, value: "Basic username:password" }
	 *
	 * @return Result ok() when user is authorized or result forbidden if not
	 */
	public CompletionStage<Result> userLogin()
	{
		Optional<String> auth = request().getHeaders().get("Authorization");
		if (auth.isPresent())
		{
			try
			{
				String[] credentials = auth.get().replace("Basic ", "")
						.split(":");
				return userService.validate(credentials)
						.thenApplyAsync(aBoolean -> {
							if (aBoolean)
							{
								// TODO: set session-conf in application.conf
								//session("logged-in", "true");
								//session("logged-in-as", credentials[0]);

								Http.Cookie cookie = Http.Cookie.builder("", "usernameid:1").build();
								return ok(Json.toJson("Validation successful")).withCookies(cookie);

							}
							else
							{
								return forbidden("Validation failed").withCookies();
							}
						}, ec.current());

			} catch (Exception e)
			{
				Logger.error("Error occurred at {}",
						Thread.currentThread().getStackTrace()[1]);
			}
		}
		return CompletableFuture.supplyAsync(() -> badRequest("Bad Request"));
	}

	/**
	 * Client sends a logout-signal and session will be deleted
	 * @return Result ok() if logout was successful
	 */
	public CompletionStage<Result> userLogout() {
		session().clear();
		if(session().isEmpty()) {
			return CompletableFuture.supplyAsync(() -> ok());
		}
		else {
			return CompletableFuture.supplyAsync(() -> badRequest());
		}
	}

	/**
	 * Get id of given username.
	 *
	 * @param username as string
	 * @return Result with id
	 */
	public CompletionStage<Result> getIdByName(String username)
	{
		if (checkSession())
		{
			return userService.getIdByName(username)
					.thenApplyAsync(id -> ok(Json.toJson(id)));
		}
		else
		{
			return CompletableFuture.supplyAsync(() -> forbidden());
		}
	}

	/**
	 * check if session exists and user is logged in.
	 * @return {@code true} if user is logged in or else {@code false}
	 */
	@SuppressWarnings("Duplicates")
	private Boolean checkSession()
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
