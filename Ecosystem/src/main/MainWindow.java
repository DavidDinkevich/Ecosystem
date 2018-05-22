package main;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import graphics.Canvas;
import simelements.Saver;
import simelements.Simulation;


/**
 * Main class/window - contains the main method.
 * 
 * @author David Dinkevich
 */
public class MainWindow extends JFrame implements WindowListener {
	
	private static final long serialVersionUID = 1L;
	
	private Canvas canvas;
	private Simulation sim;
	
	private SimControlPanel controlPanel;
	private JPanel dataPanel;

	public MainWindow(Simulation sim) {
		super("Ecosystem");
		setSize(1300, 820);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		
		// Create and add the Canvas
		canvas = new Canvas(this);
		add(canvas, BorderLayout.CENTER);
		canvas.init();

		if (sim == null) {
			this.sim = new Simulation(this);
			this.sim.init();
		} else {
			this.sim = sim;
			sim.setCanvas(canvas);
		}

		controlPanel = new SimControlPanel(this);
		add(controlPanel, BorderLayout.WEST);
		
		dataPanel = new AgentStatsPanel(this, null);
		add(new JScrollPane(dataPanel), BorderLayout.EAST);
		
		addWindowListener(this);
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
	
	public SimControlPanel getControlPanel() {
		return controlPanel;
	}
	
	public AgentStatsPanel getDataPanel() {
		return (AgentStatsPanel) dataPanel;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// Prompt user to save Simulation
		final int result = JOptionPane.showConfirmDialog(this, "Would you like to save "
				+ "the Simulation?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
		
		if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
			if (result == JOptionPane.YES_OPTION) {
				Saver.promptUserToSaveSimulation(sim);
			}
			
			System.out.println(sim.dataReport());
			
			dispose(); // This may not be necessary
			System.exit(0);
		}
	}
	
	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
}
