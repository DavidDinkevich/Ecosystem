package simelements;

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
	
	private static String defaultSaveDirectory = "data";
	
	public static String getDefaultSaveDirectory() {
		return defaultSaveDirectory;
	}
	
	public static void setDefaultSaveDirectory(String s) {
		defaultSaveDirectory = s;
	}
	
	public static void save(Simulation sim) {
		ObjectOutputStream oos = null; 
		try {
			String filePath = defaultSaveDirectory + File.separator + sim.getSaveFileName();
			oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(sim);
			oos.close();
		} catch (IOException e) {
			System.err.println("Encountered problem while trying to save the Simulation.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static Simulation load(String fileName) {
		try {
			String filePath = defaultSaveDirectory + File.separator + fileName;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
			Simulation sim = (Simulation)ois.readObject();
			ois.close();
			return sim;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Encountered problem while trying to load the Simulation.");
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
	
	public static boolean promptUserToSaveSimulation(Simulation sim) {
		String saveFileName = sim.getSaveFileName();
		
		if (saveFileName == null) {
			JFileChooser chooser = new JFileChooser(defaultSaveDirectory);
			final int result = chooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				saveFileName = chooser.getSelectedFile().getName();
				sim.setSaveFileName(saveFileName);
			}
			// If user doesn't want to save, exit
			else {
				return false;
			}
		}
		
		save(sim);
		return true;
	}
	
	public static Simulation promptUserToLoadSimulation() {
		JFileChooser fileChooser = new JFileChooser(defaultSaveDirectory);
		fileChooser.setDialogTitle("Open Simulation");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		final int result = fileChooser.showOpenDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			return load(chosenFile.getName());
		} else {
			return null;
		}
	}
	
}
