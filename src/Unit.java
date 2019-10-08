import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;
import java.lang.Math;

/** 
 * The abstract class responsible for creating a potential soldier/unit, alongside with its properties, actions and its
 * whereabouts.
 */
public abstract class Unit {
	// The distance between unit and mouse location before the unit can stop moving
	private static final float REACH_LIMIT = 0.25f;
	
	// Acts as a safeguard to prevent unit from reaching outside tile array (outside the map essentially) 
	private static final int CORRECTION = 3;
	
	// The movement speed for a unit 
	private float movePixelPerMS = ZEROSPEED;
	
	private static final float ZEROSPEED = 0f;
	private static final float MAXSPEED = 0.3f;
	
	private final Image unit;
	private final Image highlight;
	
	// represents x and y coordinates of object within world
	private float x, y;
	
	private boolean createComCentre = false;
	private boolean selected = false;
	private boolean ifClicked = false;
	private float mouseX, mouseY;
	
	/** 
	 * Initialize object of Unit class within game
	 * @param location - where the image used to represent the unit is within the file directory (in string)
	 * @param place - the background of the game used 
	 * @throws SlickException
	 */
	public Unit(String location, float x, float y, Place place) throws SlickException {
		unit = new Image(location);
		this.setX(x, place);
		this.setY(y, place);
		highlight = new Image("assets/highlight.png");
	}

	/** 
	 * Responsible for determining movement for Unit objects via trigonometric means and also making sure that said movement
	 * is possible.
	 * @param input - represents input from keyboard/hardware
	 * @param place - the current background tiled map
	 * @param buildings - the container which contains current buildings within game
	 * @param resources - the container which contains current resources within game
	 * @param worldCam - tool for spectating object within background
	 */
	public void update(Input input, int delta, Place place, BuildingContainer buildings, ResourceContainer resources,
			           Player player, Camera worldCam) throws SlickException {
		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && selected) {

			
			// translating screen coordinates to world coordinates by adding camera coordinates
			mouseX = input.getMouseX() + worldCam.getCameraX();
			mouseY = input.getMouseY() + worldCam.getCameraY();
			
			ifClicked = true;
		} else if (ifClicked) {
			move(mouseX, mouseY, delta, place);
		}
	}
	
	/** 
	 * responsible for determining unit movement towards its target location by determining its difference in location
	 * between unit and target and updating unit coordinates.
	 */
	public void move(float targetX, float targetY, int delta, Place place) {
		float addX, addY, difX, difY, prevX, prevY;
		// This whole section beneath determines object's actions (to update coords or to stop moving)
		difX = getX() - targetX; difY = getY() - targetY;
	    double angle = Math.atan2((double) difY, (double) difX);
		            
	    // ensuring that object moves in the speed set
	    addX = movePixelPerMS * (float) (Math.cos(angle)); addY = movePixelPerMS * (float) (Math.sin(angle));
	    
		double trigX = (double) (getX() - targetX), trigY = (double) (getY() - targetY);
					
		// storing previous coordinates so object can return to in case if encounter with restricted places(border/solid) 
		prevX = getX(); prevY = getY();
					
		if (Math.hypot(trigX, trigY) <= REACH_LIMIT) {
						
		// prevents object from further updating its coordinates
			ifClicked = false;
		} else {
			setX(getX() - addX * delta, place);
			setY(getY() - addY * delta, place);
			int xTileID = place.changeToTile(getX()), yTileID = place.changeToTile(getY());
			int currTileID = place.findTileId(xTileID, yTileID, Place.PROPLAYER1);
			setX(solidEncounter(getX(), prevX, place, currTileID), place);
			setY(solidEncounter(getY(), prevY, place, currTileID), place);
			setX(borderEncounter(getX(), prevX, Place.BORDERLIMIT, place.mapWidth()), place);
			setY(borderEncounter(getY(), prevY, Place.BORDERLIMIT, place.mapHeight()), place);	
		}
	}
	
	/*
	 * determines movement of object if encountered with tiles that are "solid" (returns previous coordinates to prevent object 
	 * from further accessing into tile)
	 * @param tileID - the ID of current tile that object is on top of
	 */
	private float solidEncounter(float currCoord, float prevCoord, Place place, int tileID) {
		if (Boolean.parseBoolean(place.valueOfProp(tileID, Place.TILEPROP1, Place.DEFAULT))) {
			return prevCoord;
		} else {
			return currCoord;
		}
	}
	
	/*
	 * determines movement of object if encountered with world borders (returns previous coordinates to prevent object 
	 * from entering into empty space)
	 * @param BORDERLIMIT1 & 2 - represents numeric limits of borders (either x-coords or y-coords, can't be both)
	 */
	private float borderEncounter(float currCoord, float prevCoord, int borderLimit1, int borderLimit2) {
		if (currCoord <= borderLimit1 || currCoord >= (borderLimit2 - CORRECTION)) {
			return prevCoord;
		} else {
			return currCoord;
		}
	}
	
	/** 
	 * Responsible for displaying the unit and the highlight (if selected).
	 */
	public void render(Graphics g, Camera worldCam) {
		if (selected) {
			highlight.drawCentered(getX(), getY());
		}
		unit.drawCentered(getX(), getY());
	}

	/** 
	 * Method responsible for returning current x-coordinate of unit
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * sets x-coordinate of current object if passes conditions (if object is within given map borders)
	 */
	public void setX(float x, Place place) {
		if (x > Place.BORDERLIMIT && x < place.mapWidth()) {
			this.x = x;
		}
	}

	/**
	 * Method responsible for returning current y-coordinate of unit
	 */
	public float getY() {
		return y;
	}

	/**
	 * sets y-coordinate of current object if passes conditions (if object is within given map borders)
	 */
	public void setY(float y, Place place) {
		if (y > Place.BORDERLIMIT && y < place.mapHeight()) {
			this.y = y;
		}
	}
	
	/**
	 * sets new speed of unit iff within acceptable speed limits
	 */
	public void setMovePixelPerMS(float speed) {
		if (speed > ZEROSPEED || speed <= MAXSPEED) {
			movePixelPerMS = speed;
		}
	}

	/**
	 * returns whether unit has created Command Center 
	 */
	public boolean getCreateComCentre() {
		return createComCentre;
	}

	/**
	 * determines if Command Center has been created or not 
	 */
	public void setCreateComCentre(boolean createComCentre) {
		this.createComCentre = createComCentre;
	}

	/**
	 * Method determines whether unit was selected by player 
	 */
	public boolean getSelected() {
		return selected;
	}

	/**
	 * Method determines whether unit should be selected or not 
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	/**
	 * returns boolean value of variable ifClicked
	 */
	public boolean getIfClicked() {
		return ifClicked;
	}
}
