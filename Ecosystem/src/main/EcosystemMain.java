package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import graphics.ui.MainWindow;
import simelements.Saver;
import simelements.Simulation;

public class EcosystemMain {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Sets the background color of JToggleButtons that are selected
			// to black
			UIManager.put("ToggleButton.select", new java.awt.Color(0));

			// Load Simulation
			Simulation sim = Saver.promptUserToLoadSimulation();

			// Change look and feel
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Create a new simulation from scratch
			if (sim == null) {
				new MainWindow(null);
			} else {
				new MainWindow(sim);
			}
		});
	}

}