package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;


/**
 * Main class/window - contains the main method.
 * 
 * @author David Dinkevich
 */
public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private Canvas canvas;
	private Simulation sim;

	public MainWindow() {
		super("Ecosystem");
		setSize(1000, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		canvas = new Canvas(this);
		add(canvas, BorderLayout.CENTER);

//		sim = new Simulation(canvas, 4);
		sim = Saver.loadSimulation("data/sim.sim");
		sim.canvas = canvas;
		addWindowListener(sim);
		
		canvas.init();
//		sim.init();
		setVisible(true);
		canvas.requestFocus();

	}

	public static void main(String[] args) {
		 new MainWindow();
	}

	public Canvas getCanvas() {
		return canvas;
	}
	
	public Simulation getSimulation() {
		return sim;
	}
}
