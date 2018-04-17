package main;

import java.io.Serializable;

public class Memory<T> implements Serializable {

	private static final long serialVersionUID = 6571752705754415898L;

	private T object;
	private int lifespan;
	private int currLife;
	
	public Memory(T object, int lifespan) {
		this.object = object;
		this.lifespan = lifespan;
		currLife = lifespan;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Memory))
			return false;
		// This could fail if this object's T != other's T
		@SuppressWarnings("unchecked")
		Memory<T> other = (Memory<T>)o;
		return object.equals(other.object) 
				&& lifespan == other.lifespan
				&& currLife == other.currLife;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + object.hashCode();
		result = 31 * result + Integer.hashCode(lifespan);
		result = 31 * result + Integer.hashCode(currLife);
		return result;
	}
	
	public void update() {
		--currLife;
	}
	
	public void refresh() {
		currLife = lifespan;
	}

	public boolean isDead() {
		return currLife <= 0f;
	}
	
	public T getObject() {
		return object;
	}
	
	public float getCurrentLife() {
		return currLife;
	}
	
	public int getLifespan() {
		return lifespan;
	}
	
}
