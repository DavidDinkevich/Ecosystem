package graphics.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import graphics.GraphicsObject;

public class EmptyStatsPanel extends StatsPanel<GraphicsObject> {

	private static final long serialVersionUID = 6313643706626243827L;
	
	private JLabel message;

	public EmptyStatsPanel(MainWindow parentWindow, GraphicsObject object) {
		super(parentWindow, object);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 30));
		
		message = new JLabel("<html>Nothing<br>Selected</html>");
		message.setFont(new Font("Arial", Font.BOLD, 60));
		message.setForeground(Color.LIGHT_GRAY);
		add(message, BorderLayout.CENTER);
	}
	
	public EmptyStatsPanel(MainWindow parentWindow) {
		this(parentWindow, null);
	}

	@Override
	public void updateStats() {
		// Do nothing
	}

}
