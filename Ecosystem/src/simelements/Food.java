package simelements;

import java.util.ArrayList;
import java.util.List;

import graphics.GraphicsEllipse;
import graphics.ui.Canvas;
import utils.Vec2;

public class Food extends GraphicsEllipse {
	
	private static final long serialVersionUID = 2705553895686746409L;
	
	private float maxVal;
	private float value;
	
	private List<Integer> agents;
	
	public String t = "";
		
	public Food(Vec2 loc, float val) {
		this.loc.set(loc);
		size.set(val*10f);
		color.set(0, 255, 0);
		
		maxVal = val;
		value = maxVal;
		
		agents = new ArrayList<>();
	}

	@Override
	public void update(Canvas c) {
//		color.setA(Utils.map(value, 0f, maxVal, 0, 255));
		size.set(value*10f);
	}

	public float getValue() {
		return value;
	}
	
	public void setValue(float val) {
		value = val;
	}
	
	public float getMaxValue() {
		return maxVal;
	}
	
	public float withdrawAmt(float amt) {
		if (value - amt > 0f) {
			value -= amt;
			return amt;
		} else {
			final float temp = amt - value;
			value = 0f;
			return temp;
		}
	}
	
	public void reset(Vec2 loc) {
		value = maxVal;
		setLoc(loc);
	}
	
	public boolean isEmpty() {
		return value <= 0f;
	}
	
	public List<Integer> getAgents() {
		return agents;
	}
	
	public int getNumAgentsExcluding(int agentToExclude) {
		int count = 0;
		for (long agent : agents) {
			if (agent != agentToExclude)
				++count;
		}
		return count;
	}
	
	public boolean addAgent(int agent) {
		if (!agents.contains(agent))
			return agents.add(agent);
		return false;
	}
	
	public boolean removeAgent(int agent) {
		return agents.remove((Integer)agent);
	}
}
