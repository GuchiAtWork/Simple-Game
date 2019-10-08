import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** The unit responsible for creating a Command Center, allowing more units to be born */
public class Truck extends Unit implements Creatable<BuildingContainer>, Selectable{
	private static final float TRUCKSPEED = 0.25f;
	private static final int TIMETOBUILD = 15000;
	private static final float DEFAULT = 0f;
	
	/** The subclass' name in string */
	public static final String NAME = "Truck";

	// Different adjustments given to texts for proper display on screen
	private static final float TEXTADJUSTY = -40f;
	private static final float TEXTADJUSTX1 = -50f;
	private static final float TEXTADJUSTX2 = -35f;
	private static final float SELECTIONADJUSTX = 32f;
	private static final float SELECTIONADJUSTY = 143f;
	
	// These variables serve as conditions to allow certain processes to occur 
	private boolean canCreate = false;
	private boolean buildingDisplay = false;
	private boolean obstructed = false;
	
	private int timeElapsed = 0;
	
	// storing coordinates of builder when they begin to build or otherwise default
	private float buildX = DEFAULT;
	private float buildY = DEFAULT;
	
	/** Initializes object of subclass Truck */
	public Truck(float x, float y, Place place) throws SlickException {
		super("assets/truck.png", x, y, place);
		super.setMovePixelPerMS(TRUCKSPEED);
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
	 * Method determines the processes (either showing text or approving of further processes (building)) when a 
	 * key is pressed (Key 1 here), the unit is selected and the tile it occupies isn't occupied.
	 */
	public void selectProcess(Input input, Place place, Player player) {
		if (super.getSelected()) {
			if (input.isKeyDown(Input.KEY_1)) {
				
				/* Determines if current tile is occupied and depending on result, will provide different solutions */
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
				buildings.addBuildings(new CommandCentre(buildX, buildY, place));
				buildingDisplay = false;
				canCreate = false;
				buildX = DEFAULT; buildY = DEFAULT;
				
				// this is done to alert the destruction of this truck which has served its purposes
				super.setCreateComCentre(true);
				
			// checking if unit has moved and if so, disables building process
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
		if (obstructed) {
			g.drawString("TILE OCCUPIED", super.getX() + TEXTADJUSTX1, super.getY() + TEXTADJUSTY);
			obstructed = false;
		}
		if (buildingDisplay) {
			g.drawString("BUILDING", super.getX() + TEXTADJUSTX2, super.getY() + TEXTADJUSTY);
		}
		if (super.getSelected()) {
			g.drawString("1- Create CommandCentre\n", worldCam.getCameraX() + SELECTIONADJUSTX, 
					     worldCam.getCameraY() + SELECTIONADJUSTY);
		}
		super.render(g, worldCam);
	}	
}
