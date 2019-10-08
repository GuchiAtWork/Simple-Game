import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** The building responsible for building a truck to allow further creation of units */
public class Factory extends Building implements Creatable<UnitContainer>, Selectable{
	private static final int TIMETOBUILD = 5000;
	private static final int COSTOFTRUCK = 150;
	
	/** The subclass' name in a string */
	public static final String NAME = "Factory";
	
	// Different adjustments given to texts for proper display on screen
	private static final float TEXTADJUSTX = 32f;
	private static final float TEXTADJUSTY = 143f;
	private static final float TEXTADJUSTY2 = -50f;
	private static final float TEXTADJUSTX2 = -35f;
	private static final float TEXTADJUSTX3 = -100f;
	
	private int timeElapsed = 0;
	
	// These variables serve as conditions to allow certain processes to occur
	private boolean canCreate = false;
	private boolean buildingDisplay = false;
	private boolean notEnoughMoney = false;
	
	/** Initializes object of subclass Factory */
	public Factory(float x, float y, Place place) throws SlickException {
		super(x, y, "assets/factory.png", place);
	}

	/** @override
	 *  Responsible for updating the processes which the building is currently undergoing through
	 */
	public void update(Input input, int delta, Place place, UnitContainer units, Player player) throws SlickException {
		selectProcess(input, place, player);
		createProcess(delta, place, units, player);
	}
	
	/** 
	 * Method determines the processes (either showing text or approving of further processes (making) when a 
	 * key is pressed (Key 1 here) and that the building is selected, as well as fulfilling certain conditions.
	 */
	public void selectProcess(Input input, Place place, Player player) {
		if (super.getSelected()) {
			if (input.isKeyDown(Input.KEY_1)) {
				if (player.getMetalAmount() >= COSTOFTRUCK) {
					canCreate = true;
					buildingDisplay = true;
					timeElapsed = 0;
				} else {
					notEnoughMoney = true;
				}
			}
		}
	}
	
	/** 
	 * Method determines the creation of units given if appropriate conditions are fulfilled (time needed to make),
	 * as well as setting player balance once unit is made
	 */
	public void createProcess(int delta, Place place, UnitContainer units, Player player) throws SlickException {
		if (canCreate) {
			if (timeElapsed > TIMETOBUILD) {
				units.addUnits(new Truck(super.getX(), super.getY(), place));
				canCreate = false;
				buildingDisplay = false;
				player.setMetalAmount(player.getMetalAmount() - COSTOFTRUCK);
			} else {
				timeElapsed += delta;
			}
		}
	}
	
	/** @override 
	 * Responsible for displaying the building, alongside with the texts that may occur if certain processes/conditions
	 * are fulfilled.
	 */
	public void render(Graphics g, Camera worldCam) {
		if (super.getSelected()) {
			g.drawString("1- Create Truck\n", worldCam.getCameraX() + TEXTADJUSTX, worldCam.getCameraY() + TEXTADJUSTY);
		}
		if (buildingDisplay) {
			g.drawString("BUILDING", super.getX() + TEXTADJUSTX2, super.getY() + TEXTADJUSTY2);
		}
		if (notEnoughMoney) {
			g.drawString("INSUFFICIENT RESOURCES", super.getX() + TEXTADJUSTX3, super.getY() + TEXTADJUSTY2);
			notEnoughMoney = false;
		}
		super.render(g, worldCam);
	}
}
