package main;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
		
//		if (c.keyPressed) {
//			List<Agent> surrounding = getSurroundingObjects(c.getMouseLocOnGrid(), Agent.class);
//			for (GraphicsObject o : surrounding) {
//				o.getColor().set(255f, 0f, 255f);
//			}
//		}
	}
	
	@Override
	public void update(Canvas c) {
		// Clear all the elements in every Cell (new frame)
		for (Cell[] row : cells) {
			for (Cell cell : row) {
				cell.elements.clear();
			}
		}
	}

	@Override
	public boolean containsPoint(Vec2 point) {
		return false;
	}
	
	private boolean indexWithinGrid(int[] index) {
		return index[0] >= 0 && index[0] < getSideCellCount() && 
				index[1] >= 0 && index[1] < getSideCellCount();
	}
	
	private Cell getCell(int[] index) {
		if (indexWithinGrid(index))
			return cells[index[0]][index[1]];
		return null;
	}
	
	public int[] getCell(Vec2 point) {
		final float cw = getCellSize().getWidth();
		final float ch = getCellSize().getHeight();
		
		int[] index = {
				getSideCellCount()/2 + (int)Math.floor(point.x/cw),
				getSideCellCount()/2 + (int)Math.floor(point.y/ch)
		};
		
		return indexWithinGrid(index) ? index : null;
	}
	
	public int getSideCellCount() {
		return cells.length;
	}
	
	public Dimension getCellSize() {
		return Dimension.div(size, getSideCellCount(), false);
	}
	
	public void register(GraphicsObject o) {
		int[] index = getCell(o.getLoc());
		if (index != null) {
			Cell cell = getCell(index);
			cell.elements.add(o);
		}
	}
	
	public void register(Collection<? extends GraphicsObject> c) {
		for (GraphicsObject o : c) {
			register(o);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GraphicsObject> List<T> getSurrounding(Vec2 point, Class<T> type) {
		
		int[] index = getCell(point);
		// If out of bounds
		if (index == null)
			return null; // Or Collections.emptyList()??
		
		// Surrounding cells
		int[][] surroundingCells = {
				// Middle
				{ index[0], index[1] },
				// Top left
				{ index[0]-1, index[1]-1 },
				// Top middle
				{ index[0], index[1]-1 },
				// Top right
				{ index[0]+1, index[1]-1 },
				// Middle left
				{ index[0]-1, index[1] },
				// Middle right
				{ index[0]+1, index[1] },
				// Bottom left
				{ index[0]-1, index[1]+1 },
				// Bottom middle
				{ index[0], index[1]+1 },
				// Bottom right
				{ index[0]+1, index[1]+1 }
		};
		
		// Add all elements of type T in the 
		List<T> all = new LinkedList<>();
		for (int[] cellIndex : surroundingCells) {
			Cell cell = getCell(cellIndex);
			if (cell != null) {
				for (GraphicsObject o : cell.elements) {
					if (type.isInstance(o)) {
						all.add((T)o);
					}
				}				
			}
		}
		
		return all;
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
		public void display(Canvas c) {
			c.textSize(60f);
			c.text(elements.size(), loc);
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
