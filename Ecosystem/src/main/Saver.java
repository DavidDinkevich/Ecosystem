package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;

public class Saver {
	
	private Saver() {
		throw new AssertionError();
	}
	
	public static void save(Simulation sim) {
		ObjectOutputStream oos = null; 
		try {
			oos = new ObjectOutputStream(new FileOutputStream("data/sim.sim"));
			oos.writeObject(sim);
			oos.close();
		} catch (IOException e) {
			System.err.println("Encountered problem while trying to save the default directory file.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static Simulation loadSimulation(String filePath) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/sim.sim"));
			Simulation sim = (Simulation)ois.readObject();
			ois.close();
			return sim;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Encountered problem while trying to load the default directory file.");
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
	
	public static Simulation promptUserToLoadSimulation(String parentFilePath) {
		JFileChooser fileChooser = new JFileChooser(parentFilePath);
		fileChooser.setDialogTitle("Open Simulation");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		final int result = fileChooser.showOpenDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			return loadSimulation(chosenFile.getPath());
		} else {
			return null;
		}
	}
	
}
