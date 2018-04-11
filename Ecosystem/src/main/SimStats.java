package main;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class SimStats {
	private List<Stat> stats;
	private int numAgents, numFoods;
	private Simulation sim;
	
	public SimStats(Simulation sim) {
		stats = new ArrayList<>();
		this.sim = sim;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Num Agents: " + numAgents + "\n");
		builder.append("Num Foods: " + numFoods + "\n");
		
		for (Stat stat : stats) {
			builder.append(stat + "\n");
		}
		
		return builder.toString();
	}
	
	public List<Stat> getStats() {
		return stats;
	}
	
	public Simulation getSimulation() {
		return sim;
	}
	
	public static SimStats calculateStats(Simulation sim) {
		SimStats simStats = new SimStats(sim);
		
		simStats.numAgents = sim.getAgents().size();
		simStats.numFoods = sim.getAllFoods().size();
		
		Stat size = new Stat("Size", 0f, 1000000f, -1000000f);
		Stat speed = new Stat("Speed", 0f, 1000000f, -1000000f);
		Stat steeringPow = new Stat("Steering Power", 0f, 1000000f, -1000000f);
		Stat maxHunger = new Stat("Max Hunger", 0f, 1000000f, -1000000f);
		Stat minFoodToMate = new Stat("Min Food to Mate", 0f, 1000000f, -1000000f);
		Stat visionRange = new Stat("Vision Range", 0f, 1000000f, -1000000f);
		Stat memStrength = new Stat("Memory Strength", 0f, 1000000f, -1000000f);
	
		for (Agent agent : sim.getAgents()) {
			size.average += agent.getSize().getWidth();
			size.min = Math.min(size.min, agent.getSize().getWidth());
			size.max = Math.max(size.max, agent.getSize().getWidth());
			
			speed.average += agent.getMaxSpeed();
			speed.min = Math.min(speed.min, agent.getMaxSpeed());
			speed.max = Math.max(speed.max, agent.getMaxSpeed());
			
			steeringPow.average += agent.getSteeringPower();
			steeringPow.min = Math.min(steeringPow.min, agent.getSteeringPower());
			steeringPow.max = Math.max(steeringPow.max, agent.getSteeringPower());
			
			maxHunger.average += agent.getDna().getGene(1).getValue();
			maxHunger.min = Math.min(maxHunger.min, agent.getDna().getGene(1).getValue());
			maxHunger.max = Math.max(maxHunger.max, agent.getDna().getGene(1).getValue());
			
			minFoodToMate.average += agent.getDna().getGene(2).getValue();
			minFoodToMate.min = Math.min(minFoodToMate.min, agent.getDna().getGene(2).getValue());
			minFoodToMate.max = 
					Math.max(minFoodToMate.max, agent.getDna().getGene(2).getValue());
			
			visionRange.average += agent.getDna().getGene(3).getValue();
			visionRange.min = Math.min(visionRange.min, agent.getDna().getGene(3).getValue());
			visionRange.max = Math.max(visionRange.max, agent.getDna().getGene(3).getValue());
			
			memStrength.average += agent.getDna().getGene(4).getValue();
			memStrength.min = Math.min(memStrength.min, agent.getDna().getGene(4).getValue());
			memStrength.max = Math.max(memStrength.max, agent.getDna().getGene(4).getValue());
		}
		
		final int NUM_AGENTS = sim.getAgents().size();
		size.average /= NUM_AGENTS;
		speed.average /= NUM_AGENTS;
		steeringPow.average /= NUM_AGENTS;
		maxHunger.average /= NUM_AGENTS;
		minFoodToMate.average /= NUM_AGENTS;
		visionRange.average /= NUM_AGENTS;
		memStrength.average /= NUM_AGENTS;
		
		simStats.stats.addAll(Arrays.asList(size, speed, steeringPow, 
				maxHunger, minFoodToMate, visionRange, memStrength));
		
		return simStats;
	}
	
//	public static SimStats calculateStats(Simulation sim) {
//		SimStats simStats = new SimStats(sim);
//		
//		simStats.numAgents = sim.getAgents().size();
//		simStats.numFoods = sim.getAllFoods().size();
//		
//		String[] categories = {
//			"Size", "Speed", "Steering Power", "Max Hunger", 
//			"Min Food to Mate", "Vision Range", "Memory Strength"
//		};
//		Stat[] stats = new Stat[categories.length];
//		
//		for (Agent agent : sim.getAgents()) {
//			for (int i = 0; i < stats.length; i++) {
//				if (stats[i] == null)
//					stats[i] = new Stat();
//				
//				
//				final float val = agent.getDna().getGene(i).getValue();
//				stats[i].average += val;
//				stats[i].min = Math.min(stats[i].min, val);
//				stats[i].max = Math.max(stats[i].max, val);
//			}
//		}
//	}
	
	public static class Stat {
		private String name;
		private float average, min, max;
		
		public Stat(String name, float average, float min, float max) {
			this.name = name;
			this.average = average;
			this.min = min;
			this.max = max;
		}
		
		public Stat() {
			this("", 0f, 1000000f, -1000000f);
		}
		
		@Override
		public String toString() {
			return name + ": " + average + " (" + min + " - " + max + ")";
		}

		public String getName() {
			return name;
		}

		public float getAverage() {
			return average;
		}

		public float getMin() {
			return min;
		}

		public float getMax() {
			return max;
		}
	}
}
