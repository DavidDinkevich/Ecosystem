package graphics.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import simelements.Food;

public class FoodStatsPanel extends StatsPanel<Food> {

	private static final long serialVersionUID = 8280311947731112920L;
	
	private JLabel titleLabel;
	private JLabel foodIDLabel;
	private JLabel foodAmtLabel, foodAmtValueLabel;
	private JLabel numAgentsLabel, numAgentsValueLabel;
	
	public FoodStatsPanel(MainWindow parentWindow, Food food) {
		super(parentWindow, food);
		
		MigLayout lay = (MigLayout)getLayout();
		lay.setColumnConstraints("[]10[]");
		
		titleLabel = new JLabel("Food");
		foodIDLabel = new JLabel();
		titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
		foodIDLabel.setFont(new Font("Arial", Font.BOLD, 24));
		foodIDLabel.setForeground(new Color(150, 150, 150));
		add(titleLabel);
		add(foodIDLabel, "gapleft 30, wrap");
		foodAmtLabel = new JLabel("Content:");
		foodAmtValueLabel = new JLabel();
		add(foodAmtLabel);
		add(foodAmtValueLabel, "wrap");
		numAgentsLabel = new JLabel("Num Agents:");
		numAgentsValueLabel = new JLabel();
		add(numAgentsLabel);
		add(numAgentsValueLabel, "wrap");
	}
	
	@Override
	public void updateStats() {
		if (getObject() != null) {
			foodIDLabel.setText("ID: " + getObject().getID());
			foodAmtValueLabel.setText(getObject().getValue() + " / " + getObject().getMaxValue());
			numAgentsValueLabel.setText("" + getObject().getAgents().size());
		}
	}
	
}
