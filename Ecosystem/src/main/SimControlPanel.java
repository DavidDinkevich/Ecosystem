package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import net.miginfocom.swing.MigLayout;

import processing.core.PConstants;

public class SimControlPanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = -2079485536930521833L;

	private MainWindow parentWindow; 
	
	private int gapDistance;
	
	private JCheckBox displayAllCheckBox;
	private JCheckBox displaySoundsCheckBox;
	private JCheckBox displayVisionCheckBox;
	
	private ButtonGroup buttonGroup;
	private JToggleButton pauseButton;
	private JToggleButton infoButton;
	private JToggleButton addFoodPatchButton;
	private JToggleButton addAgentButton;
	private JToggleButton toggleDisplayAgentVisionButton;
	private JButton killAllAgentsButton;
	private JButton clearAllFoodPatchesButton;
	private JButton clearAllButton;
	
	// Currently selected agent (if there is one)
	private Agent currAgent;
	
	public SimControlPanel(MainWindow parentWindow) {
		this.parentWindow = parentWindow;
		parentWindow.getCanvas().addMouseListener(this);
		gapDistance = 40;
		
		setBorder(BorderFactory.createEtchedBorder());
		setBackground(java.awt.Color.BLACK);
		
		setLayout(new MigLayout("insets 30 30 30 30"));
		
		buttonGroup = new ButtonGroup();
		
		initPauseButton();		
		initDisplayAllCheckBox();
		initDisplaySoundsCheckBox();
		initDisplayVisionCheckBox();
		initInfoButton();
		initAddFoodPatchButton();
		initAddAgentButton();
		initToggleDisplayAgentVisionButton();
		initKillAllAgentsButton();
		initClearAllFoodPatchesButton();
		initClearAllButton();
	}
	
	private java.awt.Dimension getButtonSize() {
		return new java.awt.Dimension(165, pauseButton.getHeight());
	}
	
	private JCheckBox newCheckBox(String text) {
		JCheckBox newLabel = new JCheckBox(text);
		java.awt.Color col = new java.awt.Color(0, 255, 255);
		newLabel.setForeground(col);
		newLabel.setRolloverEnabled(false);
		newLabel.setOpaque(false);
		return newLabel;
	}
	
	private JToggleButton newToggleButton(String text) {
		JToggleButton butt = new JToggleButton(text);
		java.awt.Color col = new java.awt.Color(0, 255, 255);
		butt.setBackground(col);
		butt.setOpaque(true);
//		butt.setBorder(BorderFactory.createLineBorder(java.awt.Color.CYAN, 2));
		return butt;
	}
	
	private JButton newButton(String text) {
		JButton butt = new JButton(text);
		java.awt.Color col = new java.awt.Color(0, 255, 255);
		butt.setBackground(col);
		return butt;
	}
	
	private void initPauseButton() {
		pauseButton = newToggleButton("Pause");
		pauseButton.setPreferredSize(getButtonSize());
		add(pauseButton, "wrap " + gapDistance);
		
		pauseButton.addActionListener(e -> {
			parentWindow.getSimulation().togglePause();
		});
	}
	
	private void initDisplayAllCheckBox() {
		displayAllCheckBox = newCheckBox("Display All");
		displayAllCheckBox.setSelected(true);
		add(displayAllCheckBox, "wrap");
		
		displayAllCheckBox.addChangeListener(e -> {
			Simulation sim = parentWindow.getSimulation();
			sim.setDisplayAll(!sim.isDisplayingAll());
		});
	}
	
	private void initDisplaySoundsCheckBox() {
		displaySoundsCheckBox = newCheckBox("Display Sounds");
		add(displaySoundsCheckBox, "wrap");
		
		displaySoundsCheckBox.addChangeListener(e -> {
			Simulation sim = parentWindow.getSimulation();
			sim.setDisplaySounds(!sim.displaySounds());
			System.out.println(sim.displaySounds());
		});
	}
	
	private void initDisplayVisionCheckBox() {
		displayVisionCheckBox = newCheckBox("Display Vision Radii");
		add(displayVisionCheckBox, "wrap " + + gapDistance);
		
		displayVisionCheckBox.addChangeListener(e -> {
			Simulation sim = parentWindow.getSimulation();
			for (Agent a : sim.getAgents()) {
				a.setShowVisionRadius(displayVisionCheckBox.isSelected());
			}
		});
	}
	
	private void initInfoButton() {
		infoButton = newToggleButton("Info");
		infoButton.setPreferredSize(getButtonSize());
		add(infoButton, "wrap");
		buttonGroup.add(infoButton);
	}
	
	private void initAddFoodPatchButton() {
		addFoodPatchButton = newToggleButton("Add Food Patch");
		addFoodPatchButton.setPreferredSize(getButtonSize());
		add(addFoodPatchButton, "wrap");
		buttonGroup.add(addFoodPatchButton);
	}
	
	private void initAddAgentButton() {
		addAgentButton = newToggleButton("Add Agent");
		addAgentButton.setPreferredSize(getButtonSize());
		add(addAgentButton, "wrap");
		buttonGroup.add(addAgentButton);
	}
	
	private void initToggleDisplayAgentVisionButton() {
		toggleDisplayAgentVisionButton = newToggleButton("Show Agent Vision");
		toggleDisplayAgentVisionButton.setPreferredSize(getButtonSize());
		add(toggleDisplayAgentVisionButton, "wrap");
		buttonGroup.add(toggleDisplayAgentVisionButton);
	}
	
	private void initKillAllAgentsButton() {
		killAllAgentsButton = newButton("Kill All Agents");
		killAllAgentsButton.setPreferredSize(getButtonSize());
		add(killAllAgentsButton, "wrap");
		
		killAllAgentsButton.addActionListener(e -> {
			currAgent = null;
			parentWindow.getSimulation().getAgents().clear();
		});
	}
	
	private void initClearAllFoodPatchesButton() {
		clearAllFoodPatchesButton = newButton("Clear Food Patches");
		clearAllFoodPatchesButton.setPreferredSize(getButtonSize());
		add(clearAllFoodPatchesButton, "wrap");
		
		clearAllFoodPatchesButton.addActionListener(e -> {
			parentWindow.getSimulation().getFoodPatches().clear();
			parentWindow.getSimulation().getAllFoods().clear();
		});
	}
	
	private void initClearAllButton() {
		clearAllButton = newButton("Clear All");
		clearAllButton.setPreferredSize(getButtonSize());
		add(clearAllButton, "wrap");
		
		clearAllButton.addActionListener(e -> {
			Simulation sim = parentWindow.getSimulation();
			sim.getAgents().clear();
			sim.getFoodPatches().clear();
			sim.getAllFoods().clear();
			sim.getSounds().clear();
		});
	}
	
	
	public JToggleButton getPauseButton() {
		return pauseButton;
	}


	@Override
	public void mousePressed(MouseEvent e) {
		Canvas canvas = parentWindow.getCanvas();
		Vec2 mouse = canvas.getMouseLocOnGrid();
		Simulation sim = parentWindow.getSimulation();
		
		if (infoButton.isSelected()) {
			buttonGroup.clearSelection();
			
			boolean newAgent = false;
			for (Agent agent : sim.getAgents()) {
				if (agent.containsPoint(mouse)) {
					if (currAgent != null) {
						currAgent.getColor().set(0f);
					}
					newAgent = true;
					currAgent = agent;
					currAgent.getColor().set(255f, 0f, 255f);
					
					break;
				}
			}
			if (!newAgent) {
				if (currAgent != null)
					currAgent.getColor().set(0f);
				currAgent = null;
			}			
			
			// Update data panel
			parentWindow.getDataPanel().setAgent(currAgent);
		}
		
		else if (addAgentButton.isSelected()) {
			// If the user is not holding down shift, deselect button
			if (!(canvas.keyPressed && canvas.keyCode == PConstants.SHIFT))
				buttonGroup.clearSelection();
			Agent agent = sim.genRandomAgent();
			agent.setLoc(mouse);
			sim.getAgents().add(agent);
		}
		
		else if (addFoodPatchButton.isSelected()) {
			// If the user is not holding down shift, deselect button
			if (!(canvas.keyPressed && canvas.keyCode == PConstants.SHIFT))
				buttonGroup.clearSelection();
			FoodPatch patch = new FoodPatch(sim, 10);
			patch.setLoc(mouse);
			sim.addLater(patch);
		}
		
		else if (toggleDisplayAgentVisionButton.isSelected()) {
			// If the user is not holding down shift, deselect button
			if (!(canvas.keyPressed && canvas.keyCode == PConstants.SHIFT))
				buttonGroup.clearSelection();
			
			for (Agent agent : sim.getAgents()) {
				if (agent.containsPoint(mouse)) {
					agent.setShowVisionRadius(!agent.getShowVisionRadius());
				}
			}
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
}
