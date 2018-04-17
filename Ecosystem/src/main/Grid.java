package main;

import java.io.Serializable;
import java.util.Arrays;
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

		for (int x = 0; x < sideNumCells; x++) {
			for (int y = 0; y < sideNumCells; y++) {
				cells[x][y] = new Cell(getCellSize());
			}
		}
	}
	
	@Override
	public void display(Canvas c) {
		super.display(c);
		
		c.strokeWeight(10);
		c.noFill();
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
		Vec2 mouse = c.getMouseLocOnGrid();
//		System.out.println(Arrays.toString(getCell(mouse)));
		System.out.println(getCell(mouse)[0] + ", " + getCell(mouse)[1]);
	}

	@Override
	public boolean containsPoint(Vec2 point) {
		return false;
	}
	
	public int[] getCell(Vec2 point) {
		return new int[] { getSideCellCount()/2 + (int)Math.floor(point.x/getCellSize().getWidth()), (int)Math.floor(point.getY()/100f) };
	}
	
	public int getSideCellCount() {
		return cells.length;
	}
	
	public Dimension getCellSize() {
		return Dimension.div(size, getSideCellCount(), false);
	}
	
	private static class Cell implements Serializable {

		private static final long serialVersionUID = -8473469887856452303L;

		private List<GraphicsObject> elements;
		private Dimension.Mutable size;
		
		public Cell(Dimension size) {
			this.size = new Dimension.Mutable(size);
			elements = new LinkedList<>();
		}
	}
}
