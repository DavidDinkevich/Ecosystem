package main;

import java.util.LinkedList;
import java.util.List;

public class Grid extends GraphicsObject {

	private static final long serialVersionUID = 4555801987102490820L;

	private Cell[][] cells;
	
	public Grid(Dimension s, int numCells) {
		size.set(s);
		color.set(0f);
		final int sideNumCells = (int)Math.sqrt(numCells);
		cells = new Cell[sideNumCells][sideNumCells];

		Dimension cellSize = getCellSize();
		
		for (int x = 0; x < sideNumCells; x++) {
			for (int y = 0; y < sideNumCells; y++) {
				Vec2 loc = new Vec2(
						(x-sideNumCells/2) * cellSize.getWidth() + cellSize.getWidth()/2f, 
						(y-sideNumCells/2) * cellSize.getHeight() + cellSize.getHeight()/2f
				);
				cells[x][y] = new Cell(loc, getCellSize());
			}
		}
	}
	
	@Override
	public void display(Canvas c) {
		super.display(c);
		
		c.strokeWeight(10);
		c.noFill();
		// Outer frame
		final float size = this.size.getWidth();
		c.rect(0f, 0f, size, size);

		c.strokeWeight(1f);
		
		Dimension cellSize = getCellSize();
				
		for (float x = -size/2f; x < size/2f; x += cellSize.getWidth()) {
			c.line(x, -size/2f, x, size/2f);
		}
		for (float y = -size/2f; y < size/2f; y += cellSize.getHeight()) {
			c.line(-size/2f, y, size/2f, y);
		}
	}
	
	@Override
	public void update(Canvas c) {

	}

	@Override
	public boolean containsPoint(Vec2 point) {
		return false;
	}
	
	public int[] getCell(Vec2 point) {
		final float cw = getCellSize().getWidth();
		final float ch = getCellSize().getHeight();
		final int cellI = getSideCellCount()/2 + (int)Math.floor(point.x/cw);
		final int cellJ = getSideCellCount()/2 + (int)Math.floor(point.y/ch);
		
		// If out of bounds
		if (cellI < 0 || cellI >= getSideCellCount() || 
				cellJ < 0 || cellJ >= getSideCellCount())
			return null;
		return new int[] { cellI, cellJ };
	}
	
	public int getSideCellCount() {
		return cells.length;
	}
	
	public Dimension getCellSize() {
		return Dimension.div(size, getSideCellCount(), false);
	}
	
	private static class Cell extends GraphicsObject {

		private static final long serialVersionUID = -8473469887856452303L;

		private List<GraphicsObject> elements;
		
		public Cell(Vec2 loc, Dimension size) {
			this.loc.set(loc);
			this.size.set(size);
			elements = new LinkedList<>();
		}

		@Override
		public void update(Canvas c) {
			
		}
		
		@Override
		public boolean containsPoint(Vec2 point) {
			final float x = getLoc().getX();
			final float y = getLoc().getY();
			final float w = getSize().getWidth();
			final float h = getSize().getHeight();
			
			return point.x > x - w/2f && point.x < x + w/2 
					&& point.y > y - h/2f && point.y < y + h/2f;
		}
		
	}
}
