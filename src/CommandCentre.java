import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** The building responsible for creating most units within game, as well as receiving resources */
public class CommandCentre extends Building implements Creatable<UnitContainer>, Selectable{
	/** The class' name  in string */
	public static final String NAME = "CommandCentre";
	
	private static final int TIMETOBUILD = 5000;
	private static final int COSTOFSCOUT = 5;
	private static final int COSTOFBUILDER = 10;
	private static final int COSTOFENGINEER = 20;
	
	// The keys representing which unit should be build
	private static final int SCOUTCHOICE = 1;
	private static final int BUILDERCHOICE = 2;
	private static final int ENGINEERCHOICE = 3;
	
	private static final float TEXTADJUSTX = -35f;
	private static final float INSUFFICIENTADJUSTX = -100f;
	private static final float TEXTADJUSTY = -50f;
	private static final float TEXT2ADJUSTX = 32f;
	private static final float TEXT2ADJUSTY = 143f;
	
	// The correction made to Input.key<1, 2, 3> to make sure it exactly represents number (1 == 1)
	private static final int CORRECTION = 1;
	
	private int timeElapsed = 0;
	private int metalCost = 0;
	private int buttonPressed = 0;
	
	// These variables serve as conditions to allow certain processes to occur
	private boolean notEnoughMoney = false;
	private boolean buildingDisplay = false;
	private boolean canCreate = false;
	
	/** Initializes object of subclass Command Center */
	public CommandCentre(float x, float y, Place place) throws SlickException {
		super(x, y, "assets/command_centre.png", place);
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
	 * key is pressed (Key 1,2 or 3 here).
	 */
	public void selectProcess(Input input, Place place, Player player) {
		if (super.getSelected()) {
			ifKeyPressed(input, Input.KEY_1, player, COSTOFSCOUT);
			ifKeyPressed(input, Input.KEY_2, player, COSTOFBUILDER);
			ifKeyPressed(input, Input.KEY_3, player, COSTOFENGINEER);
		}
	}
	
	/** 
	 * Method determines the creation of units given if appropriate conditions are fulfilled (time needed to make),
	 * as well as setting player balance once unit is made
	 */
	public void createProcess(int delta, Place place, UnitContainer units, Player player) throws SlickException {
		if (canCreate) {
			if (timeElapsed > TIMETOBUILD) {
				if (buttonPressed == SCOUTCHOICE) {
					units.addUnits(new Scout(super.getX(), super.getY(), place));
				} else if (buttonPressed == BUILDERCHOICE) {
					units.addUnits(new Builder(super.getX(), super.getY(), place));
				} else if (buttonPressed == ENGINEERCHOICE) {
					units.addUnits(new Engineer(super.getX(), super.getY(), place));
				}
				player.setMetalAmount(player.getMetalAmount() - metalCost);
				metalCost = 0;
				buttonPressed = 0;
				canCreate = false;
				buildingDisplay = false;
			} else {
				timeElapsed += delta;
			}
		}
	}
	
	/**
	 * Determines if particular unit should be made given if correct key is pressed and player balance is enough to make
	 */
	private void ifKeyPressed(Input input, int keyPressed, Player player, int costOfUnit) {
		if (input.isKeyDown(keyPressed)) {
			if (player.getMetalAmount() >= costOfUnit) {
				metalCost = costOfUnit;
				buttonPressed = keyPressed - CORRECTION;
				canCreate = true;
				timeElapsed = 0;
				buildingDisplay = true;
			} else {
				notEnoughMoney = true;
			}
		}
	}
	
	/** @override 
	 * Responsible for displaying the building, alongside with the texts that may occur if certain processes/conditions
	 * are fulfilled.
	 */
	public void render(Graphics g, Camera worldCam) {
		if (super.getSelected()) {
			g.drawString("1- Create Scout\n2- Create Builder\n3- Create Engineer\n"
					     , worldCam.getCameraX() + TEXT2ADJUSTX, worldCam.getCameraY() + TEXT2ADJUSTY);
		}
		if (notEnoughMoney) {
			g.drawString("INSUFFICIENT RESOURCES", super.getX() + INSUFFICIENTADJUSTX, super.getY() + TEXTADJUSTY);
			notEnoughMoney = false;
		}
		if (buildingDisplay) {
			g.drawString("BUILDING", super.getX() + TEXTADJUSTX, super.getY() + TEXTADJUSTY);
		}
		super.render(g, worldCam);
	}
}
