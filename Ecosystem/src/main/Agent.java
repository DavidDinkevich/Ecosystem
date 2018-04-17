package main;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Agent extends GraphicsEllipse {
	
	private static final long serialVersionUID = -315636718119846248L;

	public static int NUM_GENES = 8;
		
	private Simulation sim;
	private DNA dna;
	
	// FOOD/HUNGER
	private float maxHunger;
	private float hunger;
	private float hungerDecay;
	private boolean eatingFood;
	private float minFoodForMating;
	private Food currFood;
	private boolean showVisionRadius;
	
	// MOTION
	private float maxSpeed;
	private float steeringPower;
	private float visionRange;
	private Vec2.Mutable currDest;
	private float explorationDist;
	
	// STATES
	private STATES state;
	public static enum STATES {
		SEEK_FOOD, MATE, EAT_FOOD
	}
	
	// MEMORY
	private List<Memory<Food>> memFood;
	private int memoryStrength;
	
	// MATING
	private int numChildren;
	
	// SOUND
	private int soundCooldown;
	private int soundLifetime;
	private int lastSoundTime;
	
	private int initLifetime;
	private int lifetime;
	
	public Agent(Simulation sim, DNA dna) {
		this.sim = sim;
		this.dna = dna;

		// READ GENES FROM DNA
		
		this.size.set(dna.getGene(0).getValue());
		maxSpeed = 1f/(size.getWidth()/100f);
		// Recommended range: 0.08-0.8
		steeringPower = Utils.constrain((size.getWidth()*size.getWidth())/10000f, 0.08f, 0.8f);
		maxHunger = dna.getGene(1).getValue();
		minFoodForMating = dna.getGene(2).getValue();
		visionRange = dna.getGene(3).getValue();
		memoryStrength = (int)dna.getGene(4).getValue();
		soundCooldown = (int)dna.getGene(5).getValue();
		soundLifetime = (int)dna.getGene(6).getValue();
		explorationDist = dna.getGene(7).getValue();
		
		hunger = maxHunger;
		hungerDecay = 0.0005f;
		
		state = STATES.MATE;
		
		memFood = new LinkedList<>();
		
		currDest = new Vec2.Mutable();
		
		initLifetime = 60 * 100;
		lifetime = initLifetime;
		
		refreshDestIfNecessary(); // Initial loc
	}
	
	public Agent(Simulation sim) {
		this(sim, new DNA(NUM_GENES));
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof Agent;
	}
	
	@Override
	public void display(Canvas c) {
		// Body
		c.fill(color);
		c.ellipse(loc, size);
		// Vision radius
		if (showVisionRadius) {
			c.fill(new Color(0, 10f));
			c.ellipse(loc, visionRange*2f);
		}
		// Display text
//		c.fill(0f);
//		c.textAlign(Canvas.CENTER);
//		c.text(getID(), loc.getX(), loc.getY());
		
//		c.fill(0f, 255f, 255f);
//		c.ellipse(currDest, Dimension.TEN);
	}

	@Override
	public void update(Canvas c) {
		--lifetime;
		hunger -= hungerDecay;
		color.setA(Utils.map(hunger, 0f, maxHunger, 0f, 255f));
//		color.setA(Utils.map(lifetime, 0, initLifetime, 0f, 255f));
		
		veloc.add(accel);
		loc.add(veloc);
		accel.mult(0f);
		
		// UPDATE MEMORY
		updateMemory();

		// BEHAVIOR
		
		Vec2.Mutable force = new Vec2.Mutable();
		
		// Standard behavior
		force.add(Vec2.mult(separate(sim.getAgents()), 2f));
		
		// Determine action
		
		if (canMate()) {
			state = STATES.MATE;
			findMate();
		} else {
			state = STATES.SEEK_FOOD;
			seekFood();
		}
		
		// If we've reached the dest, set a new dest
		refreshDestIfNecessary();
		
		// Only pursue the current destination if we're NOT eating
		if (!eatingFood) {
			force.add(Vec2.mult(steerToward(currDest), 0.5f));
		}
			
		applyForce(force);
	}
	
	public Vec2 steerToward(Vec2 dest) {
		if (dest.equals(loc)) {
			return Vec2.ZERO;
		}
		// Calculate ideal vector to dest
		Vec2.Mutable desired = new Vec2.Mutable(Vec2.sub(dest, loc));
		// Get the distance to dest
		final float dist = desired.getMag();
		desired.normalize();
		// Slow down
//		final float arrivalDist = getSize().getWidth() * 2f;
		final float arrivalDist = maxSpeed * 50f;
		final float arrivalSpeed = Utils.map(dist, 0f, arrivalDist, 0f, maxSpeed);
		desired.mult(dist <= arrivalDist ? arrivalSpeed : maxSpeed);
		// Calculate the steering vector to dest
		Vec2.Mutable steer = new Vec2.Mutable(Vec2.sub(desired, veloc));
		steer.setMag(Math.min(steeringPower, steer.getMag()));
		return steer;
	}
	
	private Vec2 separate(List<Agent> others) {
		// The average vector pointing away from all other agents within
		// desiredSep range
		Vec2.Mutable sum = new Vec2.Mutable();
		int count = 0;
		
		for (Agent other : others) {
			if (getID() == other.getID() || !canSee(other.getLoc()))
				continue;
			final float desiredSep = size.getWidth()/2f + other.getSize().getWidth()/2f;
			final float dist = Vec2.dist(loc, other.getLoc());
			// Check if d > 0 to ensure that we're not comparing us to ourself
			if (dist > 0f && dist < desiredSep) {
				// Normalized vector pointing away from other's location
				Vec2.Mutable diff = new Vec2.Mutable(Vec2.sub(loc, other.getLoc()).normalized());
				// The closer we are to other objects, the more we should flee.
				// Divide by distance to weigh veloc properly.	
				diff.div(dist);
				sum.add(diff);
				++count;
			}
		}

		// Get the average vector pointing away from other agents
		if (count > 0) {
			sum.div(count);
			sum.setMag(maxSpeed); // Scale average to maxSpeed (desired force)
			
			Vec2.Mutable steer = new Vec2.Mutable(Vec2.sub(sum, veloc));
			steer.setMag(Math.min(steeringPower, steer.getMag()));
			
			return steer;
		}
		return Vec2.ZERO;
	}
	
	private void seekFood() {	
		// Get the "best" food to go to
		float highestScore = -1000000f;
		Food closestFood = null;
		
		Iterator<Memory<Food>> it = memFood.iterator();
		while (it.hasNext()) {
			Food f = it.next().getObject();
			
			final float dist = Vec2.dist(loc, f.getLoc());
			final float numAgentsOnFood = (float)f.getNumAgentsExcluding(getID());
			
			final float score = -(dist/10f * (numAgentsOnFood*1000f+1f));
			
			// New best food?
			if (score > highestScore) {
				highestScore = score;
				closestFood = f;
			}
		}
		
		// If there is no food in range, do nothing (default motion behavior).
		if (closestFood == null) {
			return;
		}
		
		// If we're intersecting with the food
		if (intersects(closestFood)) {
			// Determine whether we are/should be eating the food
			eatingFood = hunger < maxHunger * minFoodForMating;
			// If we're eating the food
			if (eatingFood) {
				state = STATES.EAT_FOOD;
				// Update the food we're currently eating
				currFood = closestFood;
				// Tell the food we're eating it
				currFood.addAgent(getID());
				// Make sure we're not moving
				veloc.mult(0f);
				// Actually eat the food
				eat(closestFood);
			}
		} 
		// If we're not intersecting with the closest food
		else {
			eatingFood = false; // Update
			// Tell the food we're no longer eating it
			if (currFood != null)
				currFood.removeAgent(getID());
			// Steer toward the closest food
			currDest.set(closestFood.getLoc());
		}
	}
		
	public void findMate() {
		for (Agent other : sim.getAgents()) {
			if (getID() == other.getID() || other.state != STATES.MATE || 
					!canSee(other.getLoc()))
				continue;

			if (intersects(other)) {
				// MATE
				Agent child = reproduce(this, other);
				sim.getAgents().add(child);
				// Revert to default motion behavior
			} else {
				// Move toward the other
				currDest.set(other.getLoc());
			}
			// We found a suitable mate
			break;
		}
		
		// No agents eligible for mating were found. Let's listen for mating sounds, 
		// and then emit our own mating sound.
		
		float shortestDist = 1000000f; // Easily beatable
		Sound closestSound = null;
		
		for (Sound sound : sim.getSounds()) {
			if (sound.getSourceID() != getID() && intersects(sound)) {
				final float dist = Vec2.dist(loc, sound.getLoc());
				if (dist < shortestDist) {
					closestSound = sound;
					shortestDist = dist;
				}
			}
		}
		
		if (closestSound != null) {
			currDest.set(closestSound.getLoc());
		}
		
		// Emit our own mating sound
		final int currFrameCount = sim.getCanvas().frameCount;
		
		if (currFrameCount - soundCooldown >= lastSoundTime) {
			lastSoundTime = currFrameCount;		
			Sound mateSound = new Sound(loc, getID(), soundLifetime);
			mateSound.setSize(getSize());
			sim.getSounds().add(mateSound);
		}
	}
	
	/**
	 * If this {@link Agent} has reached the destination, reset it to a new random one
	 */
	private void refreshDestIfNecessary() {
		// If we've arrived at dest
		if (Vec2.dist(loc, currDest) <= size.getWidth()/2f) {
			// Random vector pointing away from our curr loc
			currDest.set(Vec2.add(Vec2.mult(Vec2.random2D(), explorationDist), loc));
		}
	}
	
	private void updateMemory() {
		// Fill memory with all foods in sight
		
		for (Food food : sim.getAllFoods()) {
			if (!canSee(food.getLoc()))
				continue;
			// Check if the food is already in our memory
			Memory<Food> memOfFood = getMemoryOfFood(food);
						
			// If food is not contained in memory
			if (memOfFood == null) {
				memFood.add(new Memory<Food>(food, memoryStrength));
			} else {
				// Since we're seeing the food again, refresh the memory
				memOfFood.refresh();
			}
//			food.getColor().set(255f, 0f, 255f);
		}
		
		// Update all memories
		Iterator<Memory<Food>> it = memFood.iterator();
		while (it.hasNext()) {
			Memory<Food> mem = it.next();
			mem.update();
			// TODO: Problem: Agents will know where the food is
			// even if the food relocated.
			if (mem.isDead() || mem.getObject().isEmpty()) {
//				mem.getObject().getColor().set(0f, 255f, 0f);
				it.remove();
			}
		}		
	}
	
	/**
	 * Get whether the given {@link Food} is contained
	 * in one of this {@link Agent}'s memories.
	 * @param food the food
	 * @return the @{@link Agent}'s {@link Memory} that
	 * contains the {@link Food}, or null if the {@link Food}
	 * is not contained in memory.
	 */
	private Memory<Food> getMemoryOfFood(Food food) {
		Memory<Food> memOfFood = null;
		for (Memory<Food> mem : memFood) {
			if (food.equals(mem.getObject())) {
				memOfFood = mem;
				break;
			}
		}
		return memOfFood;
	}
	
	public void eat(Food food) {
		if (hunger + getBiteSize() <= maxHunger)
			hunger += food.withdrawAmt(getBiteSize());
	}
	
	public boolean canMate() {
		// Food must be above 50% in order to mate
		return !eatingFood && hunger > maxHunger * 0.5f;
	}
	
	public boolean canSee(Vec2 loc) {
		return Vec2.dist(loc, this.loc) <= visionRange;
	}
	
	public static Agent reproduce(Agent a, Agent b) {
		DNA childDNA = DNA.crossover(a.getDna(), b.getDna());
		Agent child = new Agent(a.getSimulationManager(), childDNA);
		child.setLoc(a.getLoc());
		// Penalty to hunger of parents
		a.hunger /= 2f;
		b.hunger /= 2f;
		a.state = STATES.SEEK_FOOD;
		b.state = STATES.SEEK_FOOD;
		++a.numChildren;
		++b.numChildren;
		return child;
	}
	
	public void setShowVisionRadius(boolean val) {
		showVisionRadius = val;
	}
	
	public boolean getShowVisionRadius() {
		return showVisionRadius;
	}
	
	public float getBiteSize() {
		return Math.max(0.001f, maxHunger * hungerDecay * 1.8f);
	}
	
	public boolean isDead() {
		return hunger <= 0f || lifetime <= 0;
	}
	
	public Simulation getSimulationManager() {
		return sim;
	}
	
	public DNA getDna() {
		return dna;
	}
	
	public float getHunger() {
		return hunger;
	}
	
	public STATES getState() {
		return state;
	}
	
	public float getMaxSpeed() {
		return maxSpeed;
	}
	
	public float getSteeringPower() {
		return steeringPower;
	}
	
	public List<Memory<Food>> getFoodMemory() {
		return memFood;
	}
	
	public int getNumChildren() {
		return numChildren;
	}
	
	public int getLifetime() {
		return lifetime;
	}
	
	public int getInitialLifetime() {
		return initLifetime;
	}
}

