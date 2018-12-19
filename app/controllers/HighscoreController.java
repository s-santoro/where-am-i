package controllers;

import models.HighscoreEntry;
import models.Session;
import models.User;
import play.Logger;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;
import services.UserService;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for handling highscore-routes.
 *
 * Implemented methods are:
 *   getHighscoreList():	get server-side processed highscore-list as html
 */
public class HighscoreController extends Controller {

	private final UserService userService;
	private final SessionService sessionService;
	private final HttpExecutionContext ec;

	@Inject
	public HighscoreController(UserService userService,
			SessionService sessionService,
			HttpExecutionContext ec)
	{
		this.userService = userService;
		this.sessionService = sessionService;
		this.ec = ec;
	}

	@SuppressWarnings("Duplicates")
	public CompletionStage<Result> getHighscoreList() {

		// get userFuture
		CompletionStage<Stream<User>> userStreamComp = userService.get();
		CompletableFuture userFuture =(CompletableFuture)userStreamComp;
		// get sessionFuture
		CompletionStage<Stream<Session>> sessionStreamComp = sessionService.get((String) null);
		CompletableFuture sessionFuture =(CompletableFuture)sessionStreamComp;
		try
		{
			// collect stream and create user-list
			Stream<User> usersStream = (Stream<User>)userFuture.get();
			List<User> users = usersStream.collect(Collectors.toList());
			try
			{
				// collect stream and create session-list
				Stream<Session> sessionsStream = (Stream<Session>)sessionFuture.get();
				List<Session> sessions = sessionsStream.collect(Collectors.toList());
				// could not fetch users or sessions
				if (users == null || sessions == null) {
					Logger.error("Error occurred at {}",
							Thread.currentThread().getStackTrace()[1]);
					return CompletableFuture.supplyAsync(() -> {
						return status(404, "Error: Resource not found!");
					});
				}
				// combine users with sessions by creating highscore-entries
				List<HighscoreEntry> highscoreEntries = new ArrayList<>();
				for(User user: users) {
					HighscoreEntry entry = new HighscoreEntry();
					entry.setUser(user.getUsername());
					for(Session session: sessions) {
						if(user.getId() == session.getUser_fk()) {
							entry.setAverageScore(entry.getAverageScore() + session.getScore());
							entry.setNumberOfGames(entry.getNumberOfGames() + 1);
						}
					}
					highscoreEntries.add(entry);
				}
				// collect all entries with 0 games played
				List<HighscoreEntry> entriesToDelete = new ArrayList<>();
				for (HighscoreEntry entry: highscoreEntries) {
					if(entry.getNumberOfGames() == 0) {
						entriesToDelete.add(entry);
					}
				}
				// remove all entries with 0 games played
				highscoreEntries.removeAll(entriesToDelete);
				// calculate average-score
				for (HighscoreEntry entry: highscoreEntries) {
					int avgScore = entry.getAverageScore() / entry.getNumberOfGames();
					entry.setAverageScore(avgScore);
				}
				// sort highscore-list
				Collections.sort(highscoreEntries, new Comparator<HighscoreEntry>()
				{
					@Override public int compare(HighscoreEntry e1,
							HighscoreEntry e2)
					{
						int eval = 0;
						if(e1.getAverageScore() < e2.getAverageScore()) {
							eval = -1;
						}
						if(e1.getAverageScore() > e2.getAverageScore()) {
							eval = 1;
						}
						return eval;
					}
				});
				String tableBeginning = "<table class='table table-striped'>"+
						"<thead>"+
						"<tr>"+
						"<th scope='col'>#</th>"+
						"<th scope='col'>User</th>"+
						"<th scope='col'>Number of Games</th>"+
						"<th scope='col'>Average Score</th>"+
						"</tr>"+
						"</thead>"+
						"<tbody id ='tableContent'>";
				String tableEnd = "</tbody></table>";
				// assemble highscore-list string
				String highscoreList = tableBeginning;
				for (int x = 0; x < highscoreEntries.size(); x++) {
					HighscoreEntry e = highscoreEntries.get(x);
					String[] list = new String[4];
					int count = x+1;
					list[0] = count+"";
					list[1] = e.getUser();
					list[2] = e.getNumberOfGames()+"";
					list[3] = e.getAverageScore()+"";
					String entry = "<tr><th scope='row'>" + x + 1 +
							"</th><td>" + e.getUser() +
							"</td><td>" + e.getNumberOfGames() +
							"</td><td>" + e.getAverageScore() +
							"</td></tr>";
					highscoreList += entry;
				}
				highscoreList += tableEnd;
				// completableFuture needs a final value for return-statement
				final String highscore = highscoreList;
				return CompletableFuture.supplyAsync(() -> ok(highscore).as("text/html"));

			} catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
			}
		} catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}
		return CompletableFuture.supplyAsync(() -> {
			return status(404, "Error: Resource not found!");
		});
	}
}
