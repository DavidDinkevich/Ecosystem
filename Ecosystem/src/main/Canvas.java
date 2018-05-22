package main;

import processing.event.MouseEvent;
import utils.Color;
import utils.Dimension;
import utils.Utils;
import utils.Vec2;
import processing.core.PApplet;

public class Canvas extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	private MainWindow parentWindow;
	private Vec2.Mutable translation;

	private float scale;
	private float minScale;
	private float maxScale;
	
	public Canvas(MainWindow window) {
		parentWindow = window;
		translation = new Vec2.Mutable();
		scale = 1f;
		minScale = 0.1f;
		maxScale = 15f;
		setSize(800, 800);
	}
	
	@Override
	public void setup() {
		noStroke();
		rectMode(CENTER);
		cursor(CROSS);
		frameRate(60f);
		
		translation.set(width/2f, height/2f);
	}
	
	@Override
	public void draw() {		
//		translate(translation);
//		scale(scale);
		
		// Update simulation
		parentWindow.getSimulation().update();
	}
	
	@Override
	public void mousePressed() {
		parentWindow.getSimulation().mousePressed();
	}
	
	@Override
	public void keyPressed() {
		parentWindow.getSimulation().keyPressed();
	}
	
	@Override
	public void mouseDragged() {
		if (!keyPressed) {
			Vec2 from = getOldMouseLocOnGrid();
			Vec2 to = getMouseLocOnGrid();
			Vec2 offset = Vec2.sub(from, getTranslation());
			Vec2 dest = Vec2.sub(to, offset);
			setTranslation(dest);
		}
	}
	
	@Override
	public void mouseWheel(MouseEvent e) {
		final float scrollDir = (float)e.getCount();
		final float newScale = scrollDir/10f;	
		
		// Maximum zoom out/zoom in
		if (scale + newScale < minScale || scale + newScale > maxScale) {
			return;
		}
		// Change scale
		scale += newScale;
	}
	
	public void fill(Color color) {
		fill(color.r(), color.g(), color.b(), color.a());
	}
	public void stroke(Color color) {
		stroke(color.r(), color.g(), color.b(), color.a());
	}
	public void ellipse(Vec2 loc, float sizeX, float sizeY) {
		ellipse(loc.getX(), loc.getY(), sizeX, sizeY);
	}
	public void ellipse(Vec2 loc, float diam) {
		ellipse(loc.getX(), loc.getY(), diam, diam);
	}
	public void ellipse(Vec2 loc, Dimension size) {
		ellipse(loc, size.getWidth(), size.getHeight());
	}
	public void rect(Vec2 loc, Dimension size) {
		rect(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
	}
	
	public void translate(Vec2 vec) {
		translate(vec.getX(), vec.getY());
	}
	
	public void scale(Vec2 vec) {
		scale(vec.getX(), vec.getY());
	}
	
	/** Get a random int within the given parameters. Inclusive, inclusive. */
	public static int randInt(int min, int max) {
		return (int)new PApplet().random(min, max+1);
	}
	public Vec2 getMiddlePoint() {
		return new Vec2(width/2, height/2);
	}
	
	/**
	 * Returns the given vector as a vector starting from
	 * the translation
	 * @return the mouse location
	 */
	public Vec2 getLocOnGrid(Vec2 loc) {
		return Vec2.div(Vec2.sub(loc, translation), scale);
	}
	
	/**
	 * Returns the mouse location as a vector from
	 * the top left of the screen (0, 0)
	 * @return the mouse location
	 */
	public Vec2 getMouseLocRaw() {
		return new Vec2(mouseX, mouseY);
	}
	
	/**
	 * Get the location of the mouse as a vector
	 * from the translation.
	 * @return the mouse location
	 * @see Canvas#getTranslation()
	 */
	public Vec2 getMouseLocOnGrid() {
		return getLocOnGrid(getMouseLocRaw());
	}
	
	/**
	 * Returns the old mouse location as a vector from
	 * the top left of the screen (0, 0)
	 * @return the mouse location
	 */
	public Vec2 getOldMouseLocRaw() {
		return new Vec2(pmouseX, pmouseY);
	}
	
	/**
	 * Get the old location of the mouse as a vector
	 * from the translation.
	 * @return the old mouse location
	 * @see Canvas#getTranslation()
	 */
	public Vec2 getOldMouseLocOnGrid() {
		return getLocOnGrid(getOldMouseLocRaw());
	}
	
	public boolean containsPoint(Vec2 point) {
		return point.getX() >= 0f && point.getX() < width && 
				point.getY() >= 0f && point.getY() < height;
	}
	public Vec2 randomLoc() {
		return new Vec2(Utils.random(-width/2f, width/2f), Utils.random(-height/2f, height/2f));
	}
	
	public float deltaTime() {
		return 1f/frameRate;
	}
	
	public Vec2 getTranslation() {
		return translation;
	}
	
	public void setTranslation(Vec2 point) {
		translation.set(point);
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float s) {
		scale = s;
	}
	
	public float getMinScale() {
		return minScale;
	}
	
	public float getMaxScale() {
		return maxScale;
	}
}
