import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** 
 * A container for objects of Unit class for the purpose of encapsulation
 */
public class UnitContainer {
	private static final int MININDEX = 0;
	
	private ArrayList<Unit> units;
	
	/** Responsible for initializing object of UnitContainer class */
	public UnitContainer() {
		units = new ArrayList<Unit>();
	}
	
	/** returns copy of ArrayList to prevent outside access */
	public ArrayList<Unit> getUnits() {
		return new ArrayList<Unit>(units);
	}
	
	/** responsible for adding new unit to contained ArrayList */
	public void addUnits(Unit newUnit) {
		if (newUnit != null) {
			units.add(newUnit);
		}
	}
	
	/** responsible for removing specified unit from contained ArrayList */
	public void removeUnits(int index) {
		if (index >= MININDEX && index < units.size()) {
			units.remove(index);
		}
	}
	
	/** responsible for updating specified unit within ArrayList */
	public void updateUnit(Input input, int delta, Place place, int index, BuildingContainer buildings, 
			               ResourceContainer resources, Player player, Camera worldCam) throws SlickException {
		if (index >= MININDEX && index < units.size()) {
			units.get(index).update(input, delta, place, buildings, resources, player, worldCam);
		}
	}
	
	/** responsible for determining whether specified unit should be selected (this to allow performance of actions) */
	public void selectUnit(int index, boolean condition) {
		if (index >= MININDEX && index < units.size()) {
			units.get(index).setSelected(condition);
		}
	}
	
	/** returns current size of units within ArrayList */
	public int unitArraySize() {
		return units.size();
	}
	
	/** returns answer if a specific unit is selected in a contained ArrayList */
	public boolean findUnitSelect(int index) {
		return units.get(index).getSelected();
	}
	
	/** returns x-coordinate of a specific unit within ArrayList */
	public float findUnitX(int index) {
		return units.get(index).getX();
	}
	
	/** returns y-coordinate of a specific unit within ArrayList */
	public float findUnitY(int index) {
		return units.get(index).getY();
	}
}
