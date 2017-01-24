/**
 * @author Drew Roberts
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class TeamLabel extends JLabel {
	
	/**
	 * Creates a custom JLabel for each team
	 * @param primaryColor	Background color for the label
	 * @param secondaryColor	Text color and border color for the label
	 * @param initials		Initials shown on label
	 * @param logo		Image of the team logo
	 */
	public TeamLabel(Color primaryColor, Color secondaryColor, String initials, ImageIcon logo){
		this.setPreferredSize(new Dimension(115, 50));
		this.setForeground(secondaryColor);
		this.setBorder(new LineBorder(secondaryColor));
		this.setBackground(primaryColor);
		this.setOpaque(true);
		this.setIcon(logo);
		this.setText(initials);
	}
}
