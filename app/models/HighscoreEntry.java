package models;

/**
 * A class for creating highscore-entries.
 * This class is used to render server-side a highscore-list.
 */
public class HighscoreEntry
{
	private int rank;
	private String user;
	private int numberOfGames;
	private int averageScore;

	public HighscoreEntry() {
		rank = 0;
		user = "";
		numberOfGames = 0;
		averageScore = 0;
	}

	public int getRank()
	{
		return rank;
	}

	public void setRank(int rank)
	{
		this.rank = rank;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public int getNumberOfGames()
	{
		return numberOfGames;
	}

	public void setNumberOfGames(int numberOfGames)
	{
		this.numberOfGames = numberOfGames;
	}

	public int getAverageScore()
	{
		return averageScore;
	}

	public void setAverageScore(int averageScore)
	{
		this.averageScore = averageScore;
	}
}
