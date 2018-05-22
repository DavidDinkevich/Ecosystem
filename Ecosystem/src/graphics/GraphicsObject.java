package graphics;

import java.io.Serializable;

import graphics.ui.Canvas;
import utils.Color;
import utils.Dimension;
import utils.Vec2;

public abstract class GraphicsObject implements Serializable {

	private static final long serialVersionUID = -8662825256000837171L;

	// The amount of times that a GraphicsObject has been constructed,
	// NOT the amount of GraphicsObjects
	private static int constructionCount = 0;
	
	private final int id;
	
	protected Vec2.Mutable loc, veloc, accel;
	protected Dimension.Mutable size;
	protected Color color;
	
	public GraphicsObject() {
		loc = new Vec2.Mutable();
		veloc = new Vec2.Mutable();
		accel = new Vec2.Mutable();
		size = new Dimension.Mutable();
		color = new Color();
		id = constructionCount++;
	}
	
	public void display(Canvas c) {
		c.fill(color);
		c.stroke(color);
	}
	
	public abstract void update(Canvas c);
	
	public abstract boolean containsPoint(Vec2 point);
	
	public void keepInScreen(Canvas c) {
		if (loc.getX() - size.getWidth()/2f >= c.width)
			loc.setX(-size.getWidth()/2f);
		else if (loc.getX() + size.getWidth()/2f < 0f)
			loc.setX(c.width+size.getWidth()/2f);
		if (loc.getY() - size.getHeight()/2f >= c.height)
			loc.setY(-size.getHeight()/2f);
		else if (loc.getY() + size.getHeight()/2f < 0f)
			loc.setY(c.height+size.getHeight()/2f);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof GraphicsObject))
			return false;
		return id == ((GraphicsObject)o).id;
	}
	
	public void applyForce(Vec2 force) {
		accel.add(force);
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + Integer.hashCode(id);
		return result;
	}
	
	public Dimension.Mutable getSize() {
		return size;
	}
	
	public void setSize(Dimension size) {
		this.size.set(size);
	}

	public Vec2.Mutable getLoc() {
		return loc;
	}
	
	public void setLoc(Vec2 loc) {
		this.loc.set(loc);
	}

	public Vec2.Mutable getVelocity() {
		return veloc;
	}
	
	public void setVelocity(Vec2 veloc) {
		this.veloc.set(veloc);
	}

	public Vec2.Mutable getAcceleration() {
		return accel;
	}
	
	public void setAcceleration(Vec2 accel) {
		this.accel.set(accel);
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getID() {
		return id;
	}
	
}
