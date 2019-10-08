import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** The unit responsible for building a factory */
public class Builder extends Unit implements Creatable<BuildingContainer>, Selectable{
	private static final float BUILDERSPEED = 0.1f;
	private static final int COSTOFFACTORY = 100;
	private static final int TIMETOBUILD = 10000;
	private static final float DEFAULT = 0f;
	
	// Different adjustments given to texts for proper display on screen
	private static final float TEXTADJUSTY = -30f;
	private static final float TEXTADJUSTX1 = -100f;
	private static final float TEXTADJUSTX2 = -50f;
	private static final float TEXTADJUSTX3 = -35f;
	private static final float SELECTIONADJUSTX = 32f;
	private static final float SELECTIONADJUSTY = 143f;
	
	/** The subclass' name in string */
	public static final String NAME = "Builder";
	
	// These variables serve as conditions to allow certain processes to occur
	private boolean canCreate = false;
	private boolean buildingDisplay = false;
	private boolean notEnoughMoney = false;
	private boolean obstructed = false;
	
	private int timeElapsed = 0;
	
	// storing coordinates of builder when they begin to build or otherwise default
	private float buildX = DEFAULT;
	private float buildY = DEFAULT;
	
	/** Initializes object of subclass Builder */
	public Builder(float x, float y, Place place) throws SlickException {
		super("assets/builder.png", x, y, place);
		super.setMovePixelPerMS(BUILDERSPEED);
	}
	
	/** @override
	 *  Responsible for updating the movements and processes which the unit currently undergoes.
	 */
	public void update(Input input, int delta, Place place, BuildingContainer buildings, ResourceContainer resources,
			           Player player, Camera worldCam) throws SlickException {
		selectProcess(input, place, player);
		createProcess(delta, place, buildings, player);
		super.update(input, delta, place, buildings, resources, player, worldCam);
	}
	
	/**
	 * Method determines the processes (either showing text or approving of further processes (building) when a 
	 * key is pressed (Key 1 here) and the unit is selected, as well as fulfilling certain conditions.
	 */
	public void selectProcess(Input input, Place place, Player player) {
		if (super.getSelected()) {
			if (input.isKeyDown(Input.KEY_1)) {
				if (player.getMetalAmount() >= COSTOFFACTORY) {
					
					/* determines if tile is occupied and give different actions if so (display obstructed message or
				     * allows building */
					int xTileID = place.changeToTile(super.getX()), yTileID = place.changeToTile(super.getY());
					int currTileID = place.findTileId(xTileID, yTileID, Place.PROPLAYER2);
					if (!(Boolean.parseBoolean(place.valueOfProp(currTileID, Place.TILEPROP2, Place.DEFAULT)))) {
						canCreate = true;
						timeElapsed = 0;
						buildingDisplay = true;
						buildX = super.getX(); buildY = super.getY();
					} else {
						obstructed = true;
					}
				} else {
					notEnoughMoney = true;
				}
			} 
		}
	}
	
	/** 
	 * Method responsible for determining whether building which unit is constructing should be made; depends iff 
	 * unit doesn't move and remains at its position for a certain length of time
	 */
	public void createProcess(int delta, Place place, BuildingContainer buildings, Player player) throws SlickException {
		if (canCreate) {
			if (timeElapsed > TIMETOBUILD) {
				buildings.addBuildings(new Factory(buildX, buildY, place));
				player.setMetalAmount(player.getMetalAmount() - COSTOFFACTORY);
				buildingDisplay = false;
				canCreate = false;
				buildX = DEFAULT; buildY = DEFAULT;
				
			// checks if unit has moved when currently building and if so, stops building process
			} else if (super.getX() != buildX || super.getY() != buildY) {
				canCreate = false;
				buildingDisplay = false;
				buildX = DEFAULT; buildY = DEFAULT;
			} else {
				timeElapsed += delta;
			}
		}
	}
	
	/** @override 
	 * Responsible for displaying the unit, alongside with the texts that may occur if certain processes/conditions
	 * are fulfilled.
	 */
	public void render(Graphics g, Camera worldCam) {
		if (notEnoughMoney) {
			g.drawString("INSUFFICIENT RESOURCES", super.getX() + TEXTADJUSTX1, super.getY() + TEXTADJUSTY);
			notEnoughMoney = false;
		}
		if (obstructed) {
			g.drawString("TILE OCCUPIED", super.getX() + TEXTADJUSTX2, super.getY() + TEXTADJUSTY);
			obstructed = false;
		}
		if (buildingDisplay) {
			g.drawString("BUILDING", super.getX() + TEXTADJUSTX3, super.getY() + TEXTADJUSTY);
		}
		if (super.getSelected()) {
			g.drawString("1- Create Factory\n", worldCam.getCameraX() + SELECTIONADJUSTX, 
					     worldCam.getCameraY() + SELECTIONADJUSTY);
		}
		super.render(g, worldCam);
	}
}
