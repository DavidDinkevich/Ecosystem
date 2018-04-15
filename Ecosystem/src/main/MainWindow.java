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

	public MainWindow(Simulation sim) {
		super("Ecosystem");
		setSize(1000, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		// Create and add the Canvas
		canvas = new Canvas(this);
		add(canvas, BorderLayout.CENTER);
		canvas.init();

		if (sim == null) {
			this.sim = new Simulation(canvas, 0);
			this.sim.init();
		} else {
			this.sim = sim;
			sim.setCanvas(canvas);
		}

		addWindowListener(sim);
		setVisible(true);
		canvas.requestFocus();
	}
	
	public MainWindow() {
		this(null);
	}

	public Canvas getCanvas() {
		return canvas;
	}
	
	public Simulation getSimulation() {
		return sim;
	}
}
