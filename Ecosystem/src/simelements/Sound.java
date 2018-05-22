package simelements;

import main.Canvas;
import main.GraphicsEllipse;
import utils.Dimension;
import utils.Utils;
import utils.Vec2;

public class Sound extends GraphicsEllipse {

	private static final long serialVersionUID = 3209614259337530317L;

	public static final float EXPANSION_SPEED = 10f;
	
	private int sourceID;	
	private int lifetime;
	private int initLifetime;
	
	public Sound(Vec2 src, int srcID, int lifetime) {
		loc.set(src);
		this.sourceID = srcID;
		size.set(Dimension.ONE);
		initLifetime = lifetime;
		this.lifetime = lifetime;
	}
	
	@Override
	public void display(Canvas c) {
		c.noFill();
		c.stroke(color);
		c.strokeWeight(3f);
		c.ellipse(loc, size);
	}
	
	@Override
	public void update(Canvas c) {
		color.setA(Utils.map(lifetime, 0f, initLifetime, 0f, 255f));
		size.add(EXPANSION_SPEED);
		--lifetime;
	}
			
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof Sound;
	}
	
	public boolean isDead() {
		return lifetime <= 0;
	}
	
	public int getLifetime() {
		return lifetime;
	}
	
	public int getSourceID() {
		return sourceID;
	}

}
