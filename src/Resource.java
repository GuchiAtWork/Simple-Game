import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** The abstract class responsible for creating unique type of resources that are fundamental for this game */
public abstract class Resource {
	/** indicates if there are no more amount in resource left */
	public static final int NOMORELEFT = 0;
	
	private int amount;
	private final Image resource;
	
	// represent world coordinates within game 
	private final float x, y;
	
	/** The initialization of object resource within game */
	public Resource(float x, float y, String picLocation, int amount) throws SlickException {
		this.x = x;
		this.y = y;
		resource = new Image(picLocation);
		this.amount = amount;
	}
	
	/** The drawing of resource within screen */
	public void render() {
		resource.drawCentered(x, y);
	}

	/** Returns how much amount is left within a resource */
	public int getAmount() {
		return amount;
	}

	/** Sets new amount within a resource, given that new amount is within acceptable range */
	public void setAmount(int amount) {
		if (amount < NOMORELEFT) {
			this.amount = NOMORELEFT;
		} else {
			this.amount = amount;
		}
	}

	/** Returns the x-coordinate of resource within game */
	public float getX() {
		return x;
	}

	/** Returns the y-coordinate of resource within game */
	public float getY() {
		return y;
	}
}
