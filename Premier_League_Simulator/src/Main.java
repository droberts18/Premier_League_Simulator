/**
 * @author Drew Roberts
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSlider;

public class Main {
	ArrayList<Team> teams = new ArrayList<Team>();
	ArrayList<Week> weeks = new ArrayList<Week>();
	JPanel tablePanel = new JPanel();
	
	/**
	 * Creates the frame, components, and event handlers, UI
	 */
	public void initialize(){
		// Creating the basic frame
		JFrame frame = new JFrame("Premier League Simulator");
		frame.setSize(800, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Creating tabs with an event listener for when the selected tab changes
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				if(tabbedPane.getSelectedIndex() == 1){
					createTable();
				}
			}
		});
		// First tab for weekly games and simulation
		JPanel weekPanel = new JPanel();

		// Second tab for table/standings based on total points
		tablePanel.setBackground(Color.LIGHT_GRAY);
		tabbedPane.addTab("This Week's Games", weekPanel);
		tabbedPane.addTab("Current Table", tablePanel);
		tablePanel.setLayout(new GridLayout(0, 5, 0, 0));
		frame.getContentPane().add(tabbedPane);
		frame.setVisible(true);
		
		weekPanel.setVisible(true);
		weekPanel.setLayout(null);
		
		// A panel for the 10 away teams for the week, shows each team's respective TeamLabel
		JPanel awayTeamPanel = new JPanel();
		awayTeamPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		awayTeamPanel.setBounds(10, 66, 135, 556);
		weekPanel.add(awayTeamPanel);
		
		// A panel for the 10 home teams for the week, shows each team's respective TeamLabel
		JPanel homeTeamPanel = new JPanel();
		homeTeamPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		homeTeamPanel.setBounds(282, 66, 135, 556);
		weekPanel.add(homeTeamPanel);
		
		// Shows the "@" symbol until the week is simulated, shows results of the week
		JPanel scorePanel = new JPanel();
		scorePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		scorePanel.setBounds(155, 66, 117, 556);
		weekPanel.add(scorePanel);
		
		// Displays the current week of 38
		JLabel weekLbl = new JLabel("");
		weekLbl.setFont(new Font("Tahoma", Font.BOLD, 18));
		weekLbl.setHorizontalAlignment(SwingConstants.CENTER);
		weekLbl.setBounds(10, 11, 407, 44);
		weekLbl.setForeground(Color.white);
		weekLbl.setText("WEEK " + (weeks.size()+1) + " OF 38");
		weekPanel.add(weekLbl);
		
		// Advances to the next week, disabled until the current week is simulated
		JButton advanceWeekBtn = new JButton("ADVANCE TO NEXT WEEK");
		advanceWeekBtn.setEnabled(false);
		advanceWeekBtn.setBounds(508, 133, 205, 56);
		weekPanel.add(advanceWeekBtn);
		
		// Premier League logo in the bottom right corner
		JLabel premierLogo = new JLabel();
		premierLogo.setLocation(440, 200);
		premierLogo.setSize(329, 422);
		premierLogo.setPreferredSize(new Dimension(300, 300));
		
		// Retrieving the Premier League logo
		try{
			Image img = Toolkit.getDefaultToolkit().getImage("resources\\premierleague.png");
			ImageIcon premierImageIcon = new ImageIcon(img);
			premierLogo.setIcon(premierImageIcon);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}

		premierLogo.setOpaque(false);
		weekPanel.add(premierLogo);
		
		// A slider for simulating multiple weeks at a time, default value is 1
		JSlider weeksSimSlider = new JSlider(JSlider.HORIZONTAL, 1, 38-weeks.size(), 1);
		weeksSimSlider.setValue(0);
		weeksSimSlider.setBounds(508, 200, 205, 26);
		weekPanel.add(weeksSimSlider);
		
		// Label that corresponds to the weeksSimSlider's value
		JLabel simWeeksSliderLbl = new JLabel("1 WEEK(S)");
		simWeeksSliderLbl.setForeground(Color.WHITE);
		simWeeksSliderLbl.setHorizontalAlignment(SwingConstants.CENTER);
		simWeeksSliderLbl.setBounds(508, 237, 205, 26);
		weekPanel.add(simWeeksSliderLbl);
		
		// Action listener that changes value of simWeeksSliderLbl depending on where the user lets go of the slider
	    weeksSimSlider.addChangeListener(new ChangeListener() {
	      public void stateChanged(ChangeEvent evt) {
	        JSlider slider = (JSlider) evt.getSource();
	        if (slider.getValueIsAdjusting()) {
	          int value = slider.getValue();
	          simWeeksSliderLbl.setText(value + " WEEK(S)");
	        }
	      }
	    });
	    
	    // Button that simulates a selected amount of weeks (based on slider) when pressed
		JButton simWeekBtn = new JButton("SIM WEEK(S)");
		simWeekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simWeekBtn.setEnabled(false);
				
				int currentWeekIndex = weeks.size()-1;
				
				for(int i = 0; i < weeksSimSlider.getValue()-1; i++){
					weeks.add(new Week(teams));
				}
				
				weekLbl.setText("WEEK " + (weeks.size()) + " OF 38");
				if(weeks.size() < 38)
					advanceWeekBtn.setEnabled(true);

				for(int i = currentWeekIndex; i < weeks.size(); i++){
					for(Game g: weeks.get(i).getGames()){
						g.calculateProbabilities();
					}
				}
				
				System.out.printf("weeks size: %d", weeks.size());
				weeksSimSlider.setMaximum(38 - weeks.size());
				weeksSimSlider.setValue(1);
				simWeeksSliderLbl.setText("1 WEEK(S)");
			}
		});
		simWeekBtn.setBounds(508, 66, 205, 56);
		weekPanel.add(simWeekBtn);
		
		// Action listener that sets up the next week match-ups when pressed
		advanceWeekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				advanceWeekBtn.setEnabled(false);
				simWeekBtn.setEnabled(true);
				
				awayTeamPanel.removeAll();
				homeTeamPanel.removeAll();
				scorePanel.removeAll();
				
				weeks.add(new Week(teams));
				addTeamsFromCurrentWeek(awayTeamPanel, homeTeamPanel, scorePanel);
				weekLbl.setText("WEEK " + weeks.size() + " OF 38");
				
				awayTeamPanel.updateUI();
				homeTeamPanel.updateUI();
				scorePanel.updateUI();
			}
		});
		
		// Creating teams from a .csv file
		CreateTeamsFromFile();
		weeks.add(new Week(teams));

		// Adds TeamLabels to UI
		addTeamsFromCurrentWeek(awayTeamPanel, homeTeamPanel, scorePanel);
		
		// Getting the background image
		JLabel bg = new JLabel("");
		bg.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		bg.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
		
		try{
			Image img = Toolkit.getDefaultToolkit().getImage("resources\\soccerfield.jpg");
			ImageIcon premierImageIcon = new ImageIcon(img);
			bg.setIcon(premierImageIcon);
		}
		catch (Exception ex){
			System.out.println("Couldn't get background image");
		}

		bg.setOpaque(false);
		weekPanel.add(bg);
		
		// Updates the UI to show all components
		weekPanel.updateUI();
	}
	
	/**
	 * Creates all teams from a .csv file with stats
	 */
	private void CreateTeamsFromFile(){
		try (FileInputStream is = new FileInputStream("resources\\PremierLeagueTeams.csv")){
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader rdr = new BufferedReader(ir);
			
			String line = rdr.readLine();
			
			while(line != null){
				String[] parts = line.split(",");
				String name = parts[0];
				String initials = parts[1];
				Color primaryColor = new Color(Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
				Color secondaryColor = new Color(Integer.parseInt(parts[5]),
						Integer.parseInt(parts[6]), Integer.parseInt(parts[7]));
				double eloRating = Double.parseDouble(parts[8]);
				
				teams.add(new Team(name, initials, primaryColor, secondaryColor, eloRating));
				line = rdr.readLine();
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Adds each team's TeamLabel to either away or home panel depending on game, sets
	 * up score panel with results of game
	 * @param awayTeamPanel		Away TeamLabels go here
	 * @param homeTeamPanel		Home TeamLabels go here
	 * @param scorePanel	Results of each game
	 */
	private void addTeamsFromCurrentWeek(JPanel awayTeamPanel, JPanel homeTeamPanel,
			JPanel scorePanel){
		for(Game g: weeks.get(weeks.size()-1).getGames()){
			awayTeamPanel.add(g.getAway().getTeamLbl());
			homeTeamPanel.add(g.getHome().getTeamLbl());
			scorePanel.add(g.getResult());
		}
	}
	
	/**
	 * Creates the table/standings based on number of points
	 */
	private void createTable(){
		// Sorts all teams by points in descending order
	    Collections.sort(teams, new Comparator<Team>() {
	        @Override public int compare(Team t1, Team t2) {
	            return t2.getPoints() - t1.getPoints();
	        }

	    });
		
	    // Removes all components in the table
		tablePanel.removeAll();
		
		// Adds column headers
		tablePanel.add(new JLabel("TEAM"));
		tablePanel.add(new JLabel("WINS"));
		tablePanel.add(new JLabel("DRAWS"));
		tablePanel.add(new JLabel("LOSSES"));
		tablePanel.add(new JLabel("POINTS"));
		
		// Adds all teams and stats in table
		for(Team t : teams){
			tablePanel.add(new JLabel(t.getName()));
			tablePanel.add(new JLabel(Integer.toString(t.getWins())));
			tablePanel.add(new JLabel(Integer.toString(t.getDraws())));
			tablePanel.add(new JLabel(Integer.toString(t.getLosses())));
			tablePanel.add(new JLabel(Integer.toString(t.getPoints())));
		}
	}
	
	/**
	 * Simulates a certain amount of weeks
	 * @param weeksToSim	Amount of weeks to simulate
	 */
	private void simWeeks(int weeksToSim){
		// Adds week(s) based on how many were chosen to be simulated
		for(int i = 0; i < weeksToSim; i++){
			weeks.add(new Week(teams));
			
			// Simulates all games for simulated week(s)
			for(Game g: weeks.get(weeks.size()-1).getGames()){
				g.calculateProbabilities();
			}
		}
	}
	
	/**
	 * Creates the window
	 */
	public Main(){
		initialize();
	}

	/**
	 * Runs the program
	 * @param args	Standard
	 */
	public static void main(String[] args) {
		Main m = new Main();
	}
}
