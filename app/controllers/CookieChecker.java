package controllers;

import com.google.inject.Inject;
import play.mvc.Http;
import services.UserService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * Class for cookie-checking.
 *
 * Implemented methods are:
 *   checkCookie():	check if cookie is valid
 */
public class CookieChecker
{

	private final UserService userService;

	@Inject public CookieChecker(UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * check if cookie exists and user is logged in.
	 *
	 * @param cookie
	 * @return {@code true} if user is logged in or else {@code false}
	 */
	public Boolean checkCookie(Http.Cookie cookie)
	{
		if (cookie != null)
		{
			System.out.println(cookie);
			System.out.println(cookie.name());
			System.out.println(cookie.value());
			try
			{
				CompletionStage<Boolean> compStage = userService
						.check(cookie.value());
				CompletableFuture future = (CompletableFuture) compStage;
				System.out.println(future.get());
				return (Boolean)future.get();
			} catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
}
