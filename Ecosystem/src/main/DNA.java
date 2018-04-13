package main;

import java.io.Serializable;

public class DNA implements Serializable {

	private static final long serialVersionUID = -8985907195150639431L;

	private Gene[] genes;
	
	public DNA(int numGenes) {
		this.genes = new Gene[numGenes];
	}
	
	public float getFitness() {
		return 0f;
	}
	
	public static DNA crossover(DNA a, DNA b) {
		if (a.genes.length != b.genes.length) {
			throw new IllegalArgumentException("Both DNAs must contain the "
					+ "same number of attributes.");
		}
		
		DNA child = new DNA(a.getGeneCount());
		
		for (int i = 0; i < a.genes.length; i++) {
			if (Utils.random(1f) < 0.5f)
				child.genes[i] = a.genes[i].getMutatedCopy();
			else
				child.genes[i] = b.genes[i].getMutatedCopy();
		}
		
		return child;
	}
	
	public Gene getGene(int index) {
		return genes[index];
	}
	
	public void setGene(int index, Gene gene) {
		genes[index] = gene;
	}
	
	public int getGeneCount() {
		return genes.length;
	}
}
