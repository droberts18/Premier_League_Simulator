/**
 * @author Drew Roberts
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Random;

public class Week {
	ArrayList<Game> games = new ArrayList<Game>();
	ArrayList<Team> teams = new ArrayList<Team>();
	
	/**
	 * Creates a new week in the season
	 * @param teams		Teams in the league
	 */
	public Week(ArrayList<Team> teams){
		this.teams = teams;
		generateGames();
	}
	
	/**
	 * Creates match-ups for the week
	 * TO DO LATER: Make it so teams can only play each other twice(once away and once home)
	 */
	private void generateGames(){
		Random rand = new Random();
		int awayIndex;
		int homeIndex;
		ArrayList<Team> teamsMatched = new ArrayList<Team>();
		
		do{
			//	Gets a random team to be away, removes them from ArrayList teams, adds
			//	them to an ArrayList of teams already in a match-up
			awayIndex = rand.nextInt(teams.size());
			Team awayTeam = teams.get(awayIndex);
			teams.remove(awayIndex);
			teamsMatched.add(awayTeam);
			
			//	Gets a random team from the teams left to be home, removes then from ArrayList
			//	teams, adds them to an ArrayList of teams already in a match-up
			homeIndex = rand.nextInt(teams.size());
			Team homeTeam = teams.get(homeIndex);
			teams.remove(homeIndex);
			teamsMatched.add(homeTeam);
			
			//	Creates a game with the selected away and home teams
			games.add(new Game(awayTeam, homeTeam));
		} while (teams.size() > 0);
		
		// Re-populating the ArrayList teams with all teams after it has been emptied
		for(Team t : teamsMatched)
			teams.add(t);
	}
	
	/**
	 * Gets all games in the week
	 * @return	Week's games
	 */
	public ArrayList<Game> getGames(){
		return games;
	}
}
