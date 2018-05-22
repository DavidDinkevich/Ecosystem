package graphics;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

public final class StyleManager {
	
	// To prevent instantiation
	private StyleManager() {}
	
	private static Color foreground;
	private static Color background;
	
	public static JToggleButton newToggleButton(String text) {
		JToggleButton butt = new JToggleButton(text);
		Color col = new Color(0, 255, 255);
		butt.setBackground(col);
		butt.setOpaque(true);
//		butt.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
		return butt;
	}
	
	public static JCheckBox newCheckBox(String text) {
		JCheckBox newLabel = new JCheckBox(text);
		Color col = new Color(0, 255, 255);
		newLabel.setForeground(col);
		newLabel.setRolloverEnabled(false);
		newLabel.setOpaque(false);
		return newLabel;
	}
	
	public static JButton newButton(String text) {
		JButton butt = new JButton(text);
		Color col = new Color(0, 255, 255);
		butt.setBackground(col);
		return butt;
	}
	
	public static Color getForeground() {
		return foreground;
	}
	public static void setForeground(Color foreground) {
		StyleManager.foreground = foreground;
	}
	public static Color getBackground() {
		return background;
	}
	public static void setBackground(Color background) {
		StyleManager.background = background;
	}
	
}
