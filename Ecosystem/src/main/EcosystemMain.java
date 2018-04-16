package main;

public class EcosystemMain {
	
	public static void main(String[] args) {
		Simulation sim = Saver.promptUserToLoadSimulation();
		
		// Create a new simulation from scratch
		if (sim == null) {
			new MainWindow(null);
		} else {
			new MainWindow(sim);
		}
	}

}