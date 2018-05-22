package main;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import simelements.Agent;

public class AgentStatsPanel extends JPanel {

	private static final long serialVersionUID = -7730111280150089222L;
	
	private Agent agent;
	
	private JLabel titleLabel, agentIDLabel;
	private JLabel sizeLabel, sizeValueLabel;
	private JLabel speedLabel, speedValueLabel;
	private JLabel steeringPowerLabel, steeringPowerValueLabel;
	private JLabel hungerLabel, hungerValueLabel;	
	private JLabel minMatingFoodLabel, minMatingFoodValueLabel;
	private JLabel explorationRangeLabel, explorationRangeValueLabel;
	private JLabel visionRangeLabel, visionRangeValueLabel;
	private JLabel soundCooldownLabel, soundCooldownValueLabel;
	private JLabel soundLifetimeLabel, soundLifetimeValueLabel;
	private JLabel memoryStrengthLabel, memoryStrengthValueLabel;
	private JLabel foodInMemoryLabel, foodInMemoryValueLabel;
	
	public AgentStatsPanel(MainWindow parentWindow, Agent agent) {
		this.agent = agent;
		
		setBackground(java.awt.Color.BLACK);
		setPreferredSize(new java.awt.Dimension(300, parentWindow.getHeight()));
		setLayout(new MigLayout("", "[]26[]", "[]10[]"));
		
		titleLabel = newLabel("Agent");
		agentIDLabel = newLabel();
		titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
		agentIDLabel.setFont(new Font("Arial", Font.BOLD, 24));
		agentIDLabel.setForeground(new java.awt.Color(150, 150, 150));
		add(titleLabel);
		add(agentIDLabel, "wrap");
		sizeLabel = newLabel("Size:");
		sizeValueLabel = newLabel();
		add(sizeLabel);
		add(sizeValueLabel, "wrap");
		speedLabel = newLabel("Speed:");
		speedValueLabel = newLabel();
		add(speedLabel);
		add(speedValueLabel, "wrap");
		steeringPowerLabel = newLabel("Steering Power:");
		steeringPowerValueLabel = newLabel();
		add(steeringPowerLabel);
		add(steeringPowerValueLabel, "wrap");
		hungerLabel = newLabel("Hunger:");
		hungerValueLabel = newLabel();
		add(hungerLabel);
		add(hungerValueLabel, "wrap");
		minMatingFoodLabel = newLabel("Min Food to Mate:");
		minMatingFoodValueLabel = newLabel();
		add(minMatingFoodLabel);
		add(minMatingFoodValueLabel, "wrap");
		explorationRangeLabel = newLabel("Exploration Range:");
		explorationRangeValueLabel = newLabel();
		add(explorationRangeLabel);
		add(explorationRangeValueLabel, "wrap");
		visionRangeLabel = newLabel("Vision Range:");
		visionRangeValueLabel = newLabel();
		add(visionRangeLabel);
		add(visionRangeValueLabel, "wrap");
		soundCooldownLabel = newLabel("Sound Cooldown:");
		soundCooldownValueLabel = newLabel();
		add(soundCooldownLabel);
		add(soundCooldownValueLabel, "wrap");
		soundLifetimeLabel = newLabel("Sound Lifetime:");
		soundLifetimeValueLabel = newLabel();
		add(soundLifetimeLabel);
		add(soundLifetimeValueLabel, "wrap");
		memoryStrengthLabel = newLabel("Memory Strength:");
		memoryStrengthValueLabel = newLabel();
		add(memoryStrengthLabel);
		add(memoryStrengthValueLabel, "wrap");
		foodInMemoryLabel = newLabel("Food in Memory:");
		foodInMemoryValueLabel = newLabel();
		add(foodInMemoryLabel);
		add(foodInMemoryValueLabel, "wrap");
	}
	
	public AgentStatsPanel(MainWindow parentWindow) {
		this(parentWindow, null);
	}
	
	private JLabel newLabel(String text) {
		JLabel newLabel = new JLabel(text);
		java.awt.Color col = new java.awt.Color(0, 255, 255);
		newLabel.setForeground(col);
		return newLabel;
	}
	
	private JLabel newLabel() {
		return newLabel("");
	}
	
	public void setAgent(Agent agent) {
		this.agent = agent;
		updateStats();
	}
	
	public void updateStats() {
		if (agent != null) {
			agentIDLabel.setText("ID: " + agent.getID());
			sizeValueLabel.setText("" + agent.getSize().getWidth());
			speedValueLabel.setText("" + agent.getMaxSpeed());
			steeringPowerValueLabel.setText("" + agent.getSteeringPower());
			final float maxFood = agent.getDna().getGene(1).getValue();
			hungerValueLabel.setText("" + agent.getHunger() + " / " + maxFood);
			final float minFoodForMating = maxFood * agent.getDna().getGene(2).getValue();
			minMatingFoodValueLabel.setText("" + minFoodForMating);
			explorationRangeValueLabel.setText("" + agent.getDna().getGene(7).getValue());
			visionRangeValueLabel.setText("" + agent.getDna().getGene(3).getValue());
			final int soundCooldown = (int)agent.getDna().getGene(5).getValue();
			soundCooldownValueLabel.setText(soundCooldown + " (" + soundCooldown/60 + ")");
			final int soundLifetime = (int)agent.getDna().getGene(6).getValue();
			soundLifetimeValueLabel.setText(soundLifetime + " (" + soundLifetime/60 + ")");
			final int memStrength = (int)agent.getDna().getGene(4).getValue();
			memoryStrengthValueLabel.setText(memStrength + " (" + memStrength/60 + ")");
			foodInMemoryValueLabel.setText("" + agent.getFoodMemory().size());
		}
	}
	
	public Agent getAgent() {
		return agent;
	}
}
