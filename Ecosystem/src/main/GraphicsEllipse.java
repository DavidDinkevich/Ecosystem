package main;

public abstract class GraphicsEllipse extends GraphicsObject {
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof GraphicsEllipse;
	}
	
	@Override
	public void display(Canvas c) {
		c.noStroke();
		c.fill(color);
		c.ellipse(loc, size);
	}
	
	@Override
	public boolean containsPoint(Vec2 point) {
		return Vec2.dist(point, loc) <= getSize().getWidth()/2f;
	}
	
	public boolean intersects(GraphicsEllipse other) {
		return Vec2.dist(loc, other.getLoc()) < 
				getSize().getWidth()/2f + other.getSize().getWidth()/2f;
	}
}
