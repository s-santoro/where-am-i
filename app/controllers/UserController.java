package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.User;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.UserService;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Controller for handling user-routes.
 *
 * Implemented methods are:
 *   getUsers():			get all users
 *   getUser(ID):			get user with given id
 *   createNewUser():		create new user
 *   updateUser():			update user with given id
 *   deleteUser(ID):		delete user with given id
 *   userLogin():			user Login
 *   userLogout():			user Logout
 *   getIdByName(Name):		get user id with given name
 *   validateUser(Json):	validate json of user (no logic implemented)
 */
public class UserController extends Controller
{

	private final UserService userService;
	private final HttpExecutionContext ec;
	private final CookieChecker cc;

	@Inject public UserController(UserService userService,
			CookieChecker cc,
			HttpExecutionContext ec)
	{
		this.ec = ec;
		this.cc = cc;
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
		if(cc.checkCookie(request().cookie("logged-in")))
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
		if (cc.checkCookie(request().cookie("logged-in"))) {
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
		if (cc.checkCookie(request().cookie("logged-in")))
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
	 * When authorization was successful, a cookie will be created.
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
								Http.Cookie cookie = Http.Cookie.builder("logged-in", credentials[0]).build();
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
	 * Client sends a logout-signal and cookie will be deleted
	 * @return Result ok() if logout was successful
	 */
	public CompletionStage<Result> userLogout() {
		response().discardCookie("logged-in");
		return CompletableFuture.supplyAsync(() -> ok());
	}

	/**
	 * Get id of given username.
	 *
	 * @param username as string
	 * @return Result with id
	 */
	public CompletionStage<Result> getIdByName(String username)
	{
		if (cc.checkCookie(request().cookie("logged-in")))
		{
			return userService.getIdByName(username)
					.thenApplyAsync(id -> {
						if (id == null) {
							return badRequest();
						}
						return ok(Json.toJson(id));
					});
		}
		else
		{
			return CompletableFuture.supplyAsync(() -> forbidden());
		}
	}

	/**
	 * Validate user if fields are set correct.
	 *
	 * @param json with user-parameters
	 * @return {@code true} if validation successful, otherwise {@code false}
	 */
	private boolean validateUser(JsonNode json)
	{
		// timestamp-format: "2016-11-16"

		return true;
	}
}
