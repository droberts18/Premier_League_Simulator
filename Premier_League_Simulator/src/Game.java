/**
 * @author Drew Roberts
 * @version 1.0
 */

import java.awt.Dimension;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Game {
	Team away;
	Team home;
	Team winner;
	JLabel result;
	double awayWinChance;
	double homeWinChance;
	double drawChance;
	
	/**
	 * Creates a new game
	 */
	public Game(){
		// score eventually shows who won, lost, or drew in the UI
		result = new JLabel("@");
		result.setPreferredSize(new Dimension(117, 50));
		result.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	/**
	 * Creates a game with an away team and home team
	 * @param away	Away team
	 * @param home	Home team
	 */
	public Game(Team away, Team home){
		this.away = away;
		this.home = home;
		
		result = new JLabel("@");
		result.setPreferredSize(new Dimension(117, 50));
		result.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	/**
	 * Figures out who wins a game based on formulas from:
	 * http://www.worldcup-simulator.de/static/data/Dormagen_2014_World_Cup_Simulator_2014-05-29.pdf
	 */
	public void calculateProbabilities(){
		// Works Cited: http://www.worldcup-simulator.de/static/data/Dormagen_2014_World_Cup_Simulator_2014-05-29.pdf
		double r1 = away.getEloRating();
		double r2 = home.getEloRating();
		
		// Calculates the chance the away team will win
		double awayWinDenominator = 1 + Math.pow(10, -(r1-r2)/400);
		awayWinChance = 1/awayWinDenominator;
		
		// Calculates the chance the home team will win based on away team chances
		homeWinChance = 1 - awayWinChance;
		
		// Calculates the chance that both teams will draw
		double drawNumerator = Math.pow((awayWinChance - 0.5),2);
		double drawDenominator = 2 * Math.pow(0.28, 2);
		drawChance = (1.0/3.0)*Math.exp(-(drawNumerator/drawDenominator));
		
		// Adds all probabilities together
		double totalChance = awayWinChance + homeWinChance + drawChance;
		
		// Gets a random double between 0 and 1
		double randomChance = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
		
		// Away team wins
		if(randomChance <= awayWinChance/totalChance){
			winner = away;
			// Changes both teams' Elo rating accordingly by 5% of home team's Elo rating
			away.addWin(.05 * home.getEloRating());
			home.addLoss(-(.05 * home.getEloRating()));
			result.setText("W - L");
		}
		// Home teams wins
		else if(randomChance <= (awayWinChance+homeWinChance)/totalChance){
			winner = home;
			// Changes both teams' Elo rating accordingly by 5% of away team's Elo rating
			away.addLoss(-(.05 * away.getEloRating()));
			home.addWin(.05 * away.getEloRating());
			result.setText("L - W");
		}
		// Draw
		else{
			// Adds 5% of each team's Elo rating together to make Elo pot
			double eloPot = .05*(away.getEloRating() + home.getEloRating());
			// Adds half of Elo pot
			away.addDraw(eloPot/2);
			home.addDraw(eloPot/2);
			result.setText("D");
		}
	}
	
	/**
	 * Gets the away team
	 * @return	Away team
	 */
	public Team getAway(){
		return away;
	}
	
	/**
	 * Gets the home team
	 * @return	Home team
	 */
	public Team getHome(){
		return home;
	}
	
	/**
	 * Gets the winning team
	 * @return	Winning team
	 */
	public Team getWinner(){
		return winner;
	}
	
	/**
	 * Gets the result of game (either W-L, L-W, or D)
	 * @return Game's result
	 */
	public JLabel getResult(){
		return result;
	}
}
