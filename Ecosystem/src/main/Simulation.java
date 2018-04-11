package main;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Simulation extends WindowAdapter {
	
	private Canvas canvas;
	
	private List<Agent> agents;
	private int initNumAgents;
	
	// FOOD
	private List<FoodPatch> foodPatches;
	private List<Food> allFoods;
	
	// SOUND
	private List<Sound> sounds;
	
	// MECHANICS
	private boolean run;
	private boolean display;
	private boolean displaySound;
	
	// STATISTICS
	private int maxConcurrentAgents;
	private int maxConcurrentFood;
	
	// RECORDS
	private SimStats startStats;
	private SimStats endStats;

	// FOR DEBUGGING
	public Agent currAgent;
	
	public Simulation(Canvas c, int initNumAgents) {
		canvas = c;
		this.initNumAgents = initNumAgents;
	}
	
	public void init() {
		run = true;
		display = true;
		
		// Create initial generation
		agents = new ArrayList<>();
		for (int i = 0; i < initNumAgents; i++) {
			Agent agent = genRandomAgent();
			agents.add(agent);
		}
		
		allFoods = new LinkedList<>();
		
		foodPatches = new ArrayList<>();
		// Initial FoodPatch
		foodPatches.add(new FoodPatch(this, 20));
		
		sounds = new LinkedList<>();
		
		// Calculate initial stats
		startStats = SimStats.calculateStats(this);
	}
	
	public void update() {
		if (!run)
			return;
		
		canvas.background(255f);
		
		canvas.textAlign(Canvas.LEFT);
		canvas.fill(0);
		canvas.text("Num Agents: " + agents.size(), 10f, canvas.height-150f);
		canvas.text("Max Concurrent Agents: " + maxConcurrentAgents, 10f, canvas.height-125f);
		canvas.text("Num Foods: " + allFoods.size(), 10f, canvas.height-100f);
		canvas.text("Max Concurrent Food: " + maxConcurrentFood, 10f, canvas.height-75f); 
		canvas.text("FPS: " + canvas.frameRate, 10f, canvas.height-50f);
		canvas.text("Frame Count: " + canvas.frameCount + " (" + canvas.frameCount/60 + ")", 
				10f, canvas.height-25f);
		
		canvas.translate(canvas.getTranslation());
		canvas.scale(canvas.getScale());
		
		for (int i = foodPatches.size()-1; i >= 0; i--) {
			FoodPatch f = foodPatches.get(i);
			if (f.isDead()) {
				foodPatches.remove(i);
				continue;
			}
			if (display)
				f.display(canvas);
			f.update(canvas);
		}

		for (int i = agents.size()-1; i >= 0; i--) {
			Agent a = agents.get(i);
			if (a.isDead()) {
				agents.remove(i);
				continue;
			}
			if (display)
				a.display(canvas);
			a.update(canvas);
		}
		
		Iterator<Sound> soundsIt = sounds.iterator();
		while (soundsIt.hasNext()) {
			Sound sound = soundsIt.next();
			if (sound.isDead()) {
				soundsIt.remove();
				continue;
			}
			if (display && displaySound)
				sound.display(canvas);
			sound.update(canvas);
		}

				
		// Update statistics
		maxConcurrentAgents = Math.max(maxConcurrentAgents, agents.size());
		maxConcurrentFood = Math.max(maxConcurrentFood, allFoods.size());
		
		if (currAgent != null) {
			System.out.println("Agent: " + currAgent.getID());
			System.out.println("State: " + currAgent.getState());
			System.out.println("Size: " + currAgent.getSize().getWidth());
			System.out.println("Speed: " + currAgent.getMaxSpeed());
			System.out.println("Steering Power: " + currAgent.getSteeringPower());
			// "Hunger: <current food> / <max food> / <min food for mating>"
			final float maxFood = currAgent.getDna().getGene(1).getValue();
			final float minFoodForMating = maxFood * currAgent.getDna().getGene(2).getValue();
			System.out.println(
					"Hunger: " + currAgent.getHunger() + " / " + 
					maxFood + " / " + 
					minFoodForMating
			);
			System.out.println("Bite Size: " + currAgent.getBiteSize());
			System.out.println("Vision Range: " + currAgent.getDna().getGene(3).getValue());
			System.out.println("Exploration Range: " + currAgent.getDna().getGene(7).getValue());
			final int soundCooldown = (int)currAgent.getDna().getGene(5).getValue();
			System.out.println("Sound Cooldown: " + soundCooldown + " (" + soundCooldown/60 + ")");
			final int soundLifetime = (int)currAgent.getDna().getGene(6).getValue();
			System.out.println("Sound Lifetime: " + soundLifetime + " (" + soundLifetime/60 + ")");
			final int memStrength = (int)currAgent.getDna().getGene(4).getValue();
			System.out.println("Memory Strength: " + memStrength + " (" + memStrength/60 + ")");
			System.out.println("Food in Memory: " + currAgent.getFoodMemory().size());
			System.out.println("Children: " + currAgent.getNumChildren());
			System.out.println();
			if (currAgent.isDead()) {
				System.out.println("Agent: " + currAgent.getID() + " DIED!");
				currAgent = null;
			}
		}
	}
	
	public Agent genRandomAgent() {
		DNA dna = new DNA(Agent.NUM_GENES);
		
		// Size
		final float size = Utils.random(10f, 60f);
		dna.setGene(0, new Gene(size, 8f));
		// Max Hunger
		dna.setGene(1, new Gene(Utils.constrain(size/20f, 1f, 2.5f), 0.4f));
		// Min food for mating
		dna.setGene(2, new Gene(0.7f, 0.1f));
		// Vision range
		dna.setGene(3, new Gene(Utils.random(80f, 350f), 15f));
		// Memory strength
		dna.setGene(4, new Gene(Utils.random(60 * 8, 60 * 70), 60 * 4));
		// Sound cooldown
		dna.setGene(5, new Gene(Utils.random(60 * 3, 60 * 8), 60 * 1));
		// Sound lifetime
		dna.setGene(6, new Gene(Utils.random(25, 60 * 2), 10));
		// Exploration range
		dna.setGene(7, new Gene(Math.abs(Utils.random(60f, 220f)), 30f));
		
		Agent agent = new Agent(this, dna);
		Vec2 loc = canvas.randomLoc();
		agent.setLoc(loc);
		return agent;
	}
	
	public Food genRandomFood() {
		Vec2 loc = canvas.randomLoc();
		final float val = Utils.random(0.8f, 2.8f);
		return new Food(loc, val);
	}
	
	public void mousePressed() {	
	}
	
	public void keyPressed() {
//		final char key = e.getKeyChar();
		final char key = canvas.key;
		Vec2 mouse = canvas.getMouseLocOnGrid();

		if (key == 'p') {
			run = !run;
		}
		else if (key == 'v') {
			for (Agent agent : agents) {
				if (agent.containsPoint(mouse)) {
					agent.setShowVisionRadius(!agent.getShowVisionRadius());
				}
			}
		}
		else if (key == 'a') {
			Agent agent = genRandomAgent();
			agent.setLoc(mouse);
			agents.add(agent);
		}
		else if (key == 'f') {
			for (FoodPatch patch : foodPatches) {
				if (patch.containsPoint(mouse)) {
					patch.addFoods(1);
					break;
				}
			}
		}
		else if (key == 'o') {
			FoodPatch patch = new FoodPatch(this, 10);
			patch.setLoc(mouse);
			foodPatches.add(patch);
		}
		else if (key == 'i') {
			boolean newAgent = false;
			for (Agent agent : agents) {
				if (agent.containsPoint(mouse)) {
					if (currAgent != null) {
						currAgent.getColor().set(0f, 0f, 0f);
					}
					newAgent = true;
					currAgent = agent;
					currAgent.getColor().set(255f, 0f, 255f);
					break;
				}
			}
			if (!newAgent) {
				if (currAgent != null) currAgent.getColor().set(0f, 0f, 0f);
				currAgent = null;
			}
		}
		else if (Character.isDigit(key)) {
			canvas.frameRate(Character.getNumericValue(canvas.key) * 10f);
			System.out.println("Frame Rate: " + canvas.frameRate);
		}
		else if (key == 'r') {
			startStats = SimStats.calculateStats(this);
			System.out.println();
			System.out.println("Simulation Statistics:");
			System.out.println(startStats);
		}
		else if (key == 'd') {
			display = !display;
		}
		else if (key == 's') {
			displaySound = !displaySound;
		}
		else if (key == 'k') {
			agents.clear();
			currAgent = null;
		}
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		endStats = SimStats.calculateStats(this);
		System.out.println();
		System.out.println("Initial Simulation Statistics:");
		System.out.println(startStats);
		System.out.println();
		System.out.println("Final Simulation Statistics:");
		System.out.println(endStats);
		System.exit(0);
	}

	public List<Agent> getAgents() {
		return agents;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public List<FoodPatch> getFoodPatches() {
		return foodPatches;
	}
	
	public List<Food> getAllFoods() {
		return allFoods;
	}
	
	public List<Sound> getSounds() {
		return sounds;
	}
	
	public int getMaxConcurrentAgents() {
		return maxConcurrentAgents;
	}
}
