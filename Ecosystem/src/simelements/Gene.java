package simelements;

import java.io.Serializable;

import utils.Utils;

public class Gene implements Serializable {

	private static final long serialVersionUID = -1189375062888506383L;

	private float val;
	private float mutAmt;
	
	public Gene(float val, float mutAmt) {
		this.val = val;
		this.mutAmt = mutAmt;
	}
	
	public Gene getMutatedCopy() {
//		return new Gene(val + Utils.random(mutAmt/-3f, mutAmt*(2f/3f)), mutAmt);
		return new Gene(val + Utils.random(mutAmt/-2f, mutAmt/2f), mutAmt);
	}
	
	public float getValue() {
		return val;
	}
	
	public void setValue(float v) {
		val = v;
	}
	
	public float getMutationAmount() {
		return mutAmt;
	}
	
	public void setMutationAmount(float amt) {
		mutAmt = amt;
	}
}
