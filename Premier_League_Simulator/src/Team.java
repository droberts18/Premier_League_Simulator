/**
 * @author Drew Roberts
 * @version 1.0
 */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class Team {
	private String name;
	private String initials;
	private ImageIcon logo;
	private TeamLabel teamLbl;
	private Color primaryColor;
	private Color secondaryColor;
	private double eloRating;
	private int wins;
	private int losses;
	private int draws;
	private int points;

	/**
	 * Creates a new team in the simulation
	 * @param name	Team name
	 * @param initials	Team initials
	 * @param primaryColor	Team's primary color
	 * @param secondaryColor	Team's secondary color
	 * @param eloRating		Team's Elo rating (based on performance, used to find out a winner
	 * in each game)
	 */
	public Team(String name, String initials, Color primaryColor, Color secondaryColor, double eloRating){
		this.name = name;
		this.initials = initials;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.eloRating = eloRating;
		wins = 0;
		losses = 0;
		draws = 0;
		points = 0;
		
		generateLogo();
		
		teamLbl = new TeamLabel(this.primaryColor, this.secondaryColor, initials, logo);
		teamLbl.setLayout(new FlowLayout());
	}
	
	/**
	 * Retrieves the team's logo from a .png
	 */
	private void generateLogo(){
		try{
			System.out.println("resources\\" + name.toLowerCase() + ".png");
			Image img = Toolkit.getDefaultToolkit().getImage("resources\\" +
					name.toLowerCase() + ".png");
			img = img.getScaledInstance(45,
					45, Image.SCALE_SMOOTH);
			logo = new ImageIcon(img);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Gets the team's TeamLabel
	 * @return	Team's TeamLabel
	 */
	public TeamLabel getTeamLbl(){
		return teamLbl;
	}
	
	/**
	 * Adds a win and 3 points to team's statistics, and alters Elo rating accordingly
	 * @param elo
	 */
	public void addWin(double elo){
		wins++;
		points += 3;
		eloRating += elo;
	}
	
	/**
	 * Adds a loss to team's statistics and alters Elo rating accordingly
	 * @param elo
	 */
	public void addLoss(double elo){
		losses++;
		eloRating += elo;
	}
	
	/**
	 * Adds a draw and 1 point to team's statistics, and alters Elo rating accordingly
	 * @param elo
	 */
	public void addDraw(double elo){
		draws++;
		points++;
		eloRating = .95 * eloRating;
		eloRating += elo;
	}
	
	/**
	 * Gets team's Elo rating
	 * @return	Team's Elo rating
	 */
	public double getEloRating(){
		return eloRating;
	}
	
	/**
	 * Gets the team's name
	 * @return	Team's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the amount of games the team has won
	 * @return	Team's wins
	 */
	public int getWins(){
		return wins;
	}
	
	/**
	 * Gets the amount of games the team has drawn
	 * @return	Team's draws
	 */
	public int getDraws(){
		return draws;
	}
	
	/**
	 * Gets the amount of games the team has lost
	 * @return	Team's losses
	 */
	public int getLosses(){
		return losses;
	}
	
	/**
	 * Gets the amount of points the team has accumulated
	 * @return	Team's points
	 */
	public int getPoints(){
		return points;
	}
}
