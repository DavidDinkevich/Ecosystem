package main;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class AgentStatsPanel extends JComponent {

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
		
		setPreferredSize(new java.awt.Dimension(300, parentWindow.getHeight()));
		setLayout(new MigLayout("", "[]26[]", "[]10[]"));
		
		titleLabel = new JLabel("Agent");
		agentIDLabel = new JLabel();
		titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
		agentIDLabel.setFont(new Font("Arial", Font.BOLD, 24));
		agentIDLabel.setForeground(new java.awt.Color(150, 150, 150));
		add(titleLabel);
		add(agentIDLabel, "wrap");
		sizeLabel = new JLabel("Size:");
		sizeValueLabel = new JLabel();
		add(sizeLabel);
		add(sizeValueLabel, "wrap");
		speedLabel = new JLabel("Speed:");
		speedValueLabel = new JLabel();
		add(speedLabel);
		add(speedValueLabel, "wrap");
		steeringPowerLabel = new JLabel("Steering Power:");
		steeringPowerValueLabel = new JLabel();
		add(steeringPowerLabel);
		add(steeringPowerValueLabel, "wrap");
		hungerLabel = new JLabel("Hunger:");
		hungerValueLabel = new JLabel();
		add(hungerLabel);
		add(hungerValueLabel, "wrap");
		minMatingFoodLabel = new JLabel("Min Food to Mate:");
		minMatingFoodValueLabel = new JLabel();
		add(minMatingFoodLabel);
		add(minMatingFoodValueLabel, "wrap");
		explorationRangeLabel = new JLabel("Exploration Range:");
		explorationRangeValueLabel = new JLabel();
		add(explorationRangeLabel);
		add(explorationRangeValueLabel, "wrap");
		visionRangeLabel = new JLabel("Vision Range:");
		visionRangeValueLabel = new JLabel();
		add(visionRangeLabel);
		add(visionRangeValueLabel, "wrap");
		soundCooldownLabel = new JLabel("Sound Cooldown:");
		soundCooldownValueLabel = new JLabel();
		add(soundCooldownLabel);
		add(soundCooldownValueLabel, "wrap");
		soundLifetimeLabel = new JLabel("Sound Lifetime:");
		soundLifetimeValueLabel = new JLabel();
		add(soundLifetimeLabel);
		add(soundLifetimeValueLabel, "wrap");
		memoryStrengthLabel = new JLabel("Memory Strength:");
		memoryStrengthValueLabel = new JLabel();
		add(memoryStrengthLabel);
		add(memoryStrengthValueLabel, "wrap");
		foodInMemoryLabel = new JLabel("Food in Memory:");
		foodInMemoryValueLabel = new JLabel();
		add(foodInMemoryLabel);
		add(foodInMemoryValueLabel, "wrap");
	}
	
	public AgentStatsPanel(MainWindow parentWindow) {
		this(parentWindow, null);
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
