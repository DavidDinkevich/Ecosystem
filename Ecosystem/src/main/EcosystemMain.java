package main;

import javax.swing.SwingUtilities;

import graphics.ui.MainWindow;

import simelements.Saver;
import simelements.Simulation;

public class EcosystemMain {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Load Simulation
			Simulation sim = Saver.promptUserToLoadSimulation();

			// Create a new simulation from scratch
			if (sim == null) {
				new MainWindow(null);
			} else {
				new MainWindow(sim);
			}
		});
	}

}