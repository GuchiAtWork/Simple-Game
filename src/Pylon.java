import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** The building responsible for giving engineers more mining capacity */
public class Pylon extends Building implements Searchable<Unit>{
	/** The class' name in string */
	public static final String NAME = "Pylon";

	private static final int NOTINARRAY = -1;
	
	// serve as condition to allow searching of units 
	private boolean searchUnits = true;
	
	/** Initializes object of subclass Pylon */
	public Pylon(float x, float y, Place place) throws SlickException {
		super(x, y, "assets/pylon.png", place);
	}

	/** @override
	 *  Responsible for updating the processes which the building is currently undergoing through
	 */
	public void update(Input input, int delta, Place place, UnitContainer units, Player player) throws SlickException {
		if (searchUnits) {
			ArrayList<Unit> unitArray = units.getUnits();
			int index = search(super.getX(), super.getY(), unitArray);
			if (index > NOTINARRAY) {
				
				// changing to pylon activated image, as well as increasing engineer mining capacity by one
				super.setBuilding("assets/pylon_active.png");
				Engineer.setMiningCapacity(Engineer.getMiningCapacity() + Engineer.ADDTOCAPACITY);
				searchUnits = false;
			}
		}
	}
	
	/**
	 * Method responsible for finding the nearest unit to the pylon and determining if it should be activated by 
	 * if the distance between is less or equal to 32 pixels
	 * @param x - the x-coordinate of the pylon
	 * @param y - the y-coordinate of the pylon
	 * @return foundIndex - the element in the unit array that surpasses all conditions 
	 */
	public int search(float x, float y, ArrayList<Unit> units) {
		int index = 0, foundIndex = NOTINARRAY;
		for (Unit unit : units) {
			double difX = x - unit.getX(), difY = y - unit.getY();
			if (Math.hypot(difX, difY) <= Searchable.PROXIMITYTO) {
				foundIndex = index;
				return foundIndex;
			}
			index++;
		}
		return foundIndex;
	}
}
