package main;

import java.io.File;

import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public final class EcosystemMain extends Application {
//	private EcosystemMain() {
//		throw new AssertionError();
//	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		File file = Saver.promptUserToLoadSimulation("data", primaryStage);
		if (file != null) {
			System.out.println(file.getPath());
		}
		Platform.exit();
	}

	public static void main(String[] args) {
		launch(args);
		System.out.println("Here");

		
		SwingUtilities.invokeLater(() -> {
//			new MainWindow();
		});
	}

}
