import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** 
 * The abstract class responsible for creating a potential building, alongside with its properties, actions and its
 * whereabouts.
 */
public abstract class Building {
	private Image building;
	private final Image highlight;
	
	// represents the world coordinates of building within game
	private final float x, y;
	
	// variable determines if building can perform its special capabilities (building)
	private boolean selected = false;
	
	/** 
	 * Initialize object of Building class within game
	 * @param location - where the image used to represent the building is within the file directory (in string)
	 * @param place - the background of the game used 
	 * @throws SlickException
	 */ 
	public Building(float x, float y, String picLocation, Place place) throws SlickException {
		this.x = x;
		this.y = y;
		building = new Image(picLocation);
		highlight = new Image("assets/highlight_large.png");
	}
	
	/** Method serves as potential means to further update the building and its actions
	 * @param input - represents input from keyboard/hardware
	 * @param place - the current background tiled map
	 * @param units - the container which contains current units in game 
	 * @param player - contains current player info (metal and unobtainium amount) 
	 */
	public abstract void update(Input input, int delta, Place place, UnitContainer units, Player player) throws SlickException;
	
	/** 
	 * Responsible for displaying the building and the highlight (if selected).
	 */
	public void render(Graphics g, Camera worldCam) {
		if (selected) {
			highlight.drawCentered(getX(), getY());
		}
		building.drawCentered(getX(), getY());
	}

	/** Method returns x-coordinate of current building */
	public float getX() {
		return x;
	}

	/** Method returns y-coordinate of current building */
	public float getY() {
		return y;
	}

	/** Method determines whether building was selected by player */
	public boolean getSelected() {
		return selected;
	}

	/** Method determines whether building should be selected or not */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	/** Method changes image of building, but only if its a pylon */
	public void setBuilding(String newLocation) throws SlickException {
		if (newLocation.equals("assets/pylon_active.png")) {
			this.building = new Image(newLocation);
		}
	}
}
