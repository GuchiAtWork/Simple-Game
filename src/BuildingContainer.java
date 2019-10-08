import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** 
 * A container for objects of Building class for the purpose of encapsulation
 */
public class BuildingContainer {
	private static final int MININDEX = 0;
	
	private ArrayList<Building> buildings;
	
	/** Responsible for initializing object of BuildingContainer class */
	public BuildingContainer() {
		buildings = new ArrayList<Building>();
	}
	
	/** returns copy of ArrayList to prevent outside access */
	public ArrayList<Building> getBuildings() {
		return new ArrayList<Building>(buildings);
	}
	
	/** responsible for adding new building to contained ArrayList */
	public void addBuildings(Building newBuilding) {
		if (newBuilding != null) {
			buildings.add(newBuilding);
		}
	}
	
	/** responsible for removing specified building from contained ArrayList */
	public void removeBuildings(int index) {
		if (index >= MININDEX && index < buildings.size()) {
			buildings.remove(index);
		}
	}
	
	/** responsible for updating specified building within ArrayList */
	public void updateBuilding(Input input, int delta, Place place, int index, UnitContainer units, Player player) throws SlickException {
		if (index >= MININDEX && index < buildings.size()) {
			buildings.get(index).update(input, delta, place, units, player);
		}
	}
	
	/** responsible for determining whether specified building should be selected (this to allow performance of actions) */
	public void selectBuilding(int index, boolean condition) {
		if (index >= MININDEX && index < buildings.size()) {
			buildings.get(index).setSelected(condition);
		}
	}
	
	/** returns current size of buildings within ArrayList */
	public int buildingArraySize() {
		return buildings.size();
	}
	
	/** returns x-coordinate of a specific building within ArrayList */
	public float findBuildX(int index) {
		return buildings.get(index).getX();
	}
	
	/** returns x-coordinate of a specific building within ArrayList */
	public float findBuildY(int index) {
		return buildings.get(index).getY();
	}
	
	/** returns answer if a specific building is selected in a contained ArrayList */
	public boolean findBuildSelect(int index) {
		return buildings.get(index).getSelected();
	}
}
