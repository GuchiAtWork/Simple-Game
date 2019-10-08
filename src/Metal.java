import org.newdawn.slick.SlickException;

/** The common resource that needs to be mined in order to create more objects within game */
public class Metal extends Resource{
	private static final int MAXAMOUNT = 500;
	
	/** The class' name in string */
	public static final String NAME = "Metal";
	
	/** The initialization of object Metal within game */
	public Metal(float x, float y) throws SlickException {
		super(x, y, "assets/metal_mine.png", MAXAMOUNT);
	}
	
	/** @override
	 *  setting new amount for metal object if new amount is less than maximum resource amount */
	public void setAmount(int amount) {
		if (amount < MAXAMOUNT) {
			super.setAmount(amount);
		}
	}
}
