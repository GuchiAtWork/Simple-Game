

import org.newdawn.slick.SlickException;

/** The unit responsible mainly for activating pylons, although this objective can be shared with all units */
public class Scout extends Unit{	
	private static final float SCOUTSPEED = 0.3f;
	
	/** The subclass' name in string */
	public static final String NAME = "Scout";
	
	/** Initializes object of subclass Scout */
	public Scout(float x, float y, Place place) throws SlickException {
		super("assets/scout.png", x, y, place);
		super.setMovePixelPerMS(SCOUTSPEED);
	}
}
