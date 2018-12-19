package controllers;

import com.google.inject.Inject;
import play.mvc.Http;
import services.UserService;

import java.util.Base64;
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
			byte[] bytes = Base64.getDecoder().decode(cookie.value().getBytes());
			String user = new String(bytes);
			try
			{
				CompletionStage<Boolean> compStage = userService
						.check(user);
				CompletableFuture future = (CompletableFuture) compStage;
				return (Boolean)future.get();
			} catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
}
