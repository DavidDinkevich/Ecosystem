package graphics.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

import graphics.GraphicsObject;
import net.miginfocom.swing.MigLayout;

public abstract class StatsPanel<T extends GraphicsObject> extends JPanel {

	private static final long serialVersionUID = -2104982643190917717L;
	
	private T object;
	
	public StatsPanel(MainWindow parentWindow, T object) {
		this.object = object;
		setPreferredSize(new Dimension(300, parentWindow.getHeight()));
		setLayout(new MigLayout("", "[]26[]", "[]10[]"));
	}
	
	
	public abstract void updateStats();
	
	public T getObject() {
		return object;
	}
	
	public void setObject(T obj) {
		object = obj;
		updateStats();
	}
	
}
