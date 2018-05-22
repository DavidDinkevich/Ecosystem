package simelements;

import java.util.List;

import main.Canvas;
import main.GraphicsEllipse;
import utils.Utils;
import utils.Vec2;

import java.util.ArrayList;

public class FoodPatch extends GraphicsEllipse {
	
	private static final long serialVersionUID = -4826240891064563674L;
	
	private Simulation sim;
	private List<Food> foods;
	private float newFoodRate;
	
	public FoodPatch(Simulation sim, int initAmt) {
		// Initial amount of food must be > 1
		if (initAmt <= 0)
			initAmt = 1;
		this.sim = sim;
		foods = new ArrayList<>(initAmt);
		newFoodRate = 0.2f;
		addFoods(initAmt);
	}
	
	private Vec2 genRandomFoodLoc() {
		Vec2.Mutable random = new Vec2.Mutable(Vec2.random2D());
		final float dist = Utils.random(((int)getRadius())/10) * 10f;
		random.setMag(dist);
		return Vec2.add(loc, random);
	}
	
	@Override
	public void display(Canvas c) {
		for (Food food : foods) {
			food.display(c);
		}
//		c.fill(0);
//		c.ellipse(loc, 10f);
//		c.fill(new Color(0, 10));
//		c.ellipse(loc, size);
	}

	@Override
	public void update(Canvas c) {
		// Every 60 frames, there is an equal chance that a food
		// will be added or removed
//		if (c.frameCount % 60 == 0) {
//			final float random = Utils.random(1f);
//			if (random <= newFoodRate) {
//				addFoods(1);				
//			}
//			else if (random >= 1f-newFoodRate) {
//				removeFoods(1);
//			}
//		}
		
		for (int i = foods.size()-1; i >= 0; i--) {
			Food f = foods.get(i);
			f.update(c);
			
			// Every time a food is eaten up, there is a chance that either:
			//    1) 2 new foods will spawn (the one that was eaten, and a new one)
			//    2) The food will not reset
			if (f.isEmpty()) {
				final float random = Utils.random(1f);
				
				// "newFoodRate * 1.2" makes it slightly more likely that a food 
				// will be added than removed
				if (random <= newFoodRate * 1.2f) {
					f.reset(genRandomFoodLoc());
					addFoods(1);				
				}
				else if (random >= 1f-newFoodRate) {
					foods.remove(i);
					sim.getAllFoods().remove(f);
					// Update size
					size.set(getRadius()*2f);
				}
			}
		}
	}
	
	public void addFoods(int num) {
		if (num < 0)
			throw new IllegalArgumentException();
		for (int i = 0; i < num; i++) {
			Food food = sim.genRandomFood();
			food.setLoc(genRandomFoodLoc());
			foods.add(food);
			sim.getAllFoods().add(food);
		}
		// Update size
		size.set(getRadius()*2f);
	}
	
	public void removeFoods(int num) {
		if (num < 0)
			throw new IllegalArgumentException();
		for (int i = 0; i < num; i++) {
			Food food = foods.get(foods.size()-1);
			foods.remove(foods.size()-1);
			sim.getAllFoods().remove(food);
		}
		// Update size
		size.set(getRadius()*2f);
	}
	
	@Override
	public void setLoc(Vec2 loc) {
		Vec2 diff = Vec2.sub(loc, this.loc);
		super.setLoc(loc);
		for (Food food : foods) {
			food.getLoc().add(diff);
		}
	}

	public float getRadius() {
		return foods.size() * 30f;
	}
	
	public float getNewFoodRate() {
		return newFoodRate;
	}
	
	public void setNewFoodRate(float rate) {
		newFoodRate = rate;
	}
	
	public boolean isDead() {
		return foods.size() == 0;
	}
	
	public List<Food> getFoods() {
		return foods;
	}
	
	public Simulation getSimulationManager() {
		return sim;
	}
}
