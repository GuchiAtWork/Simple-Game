import org.newdawn.slick.SlickException;

/** The special resource that needs to be mined in order to end game */
public class Unobtainium extends Resource{
	private static final int MAXAMOUNT = 50;
	
	/** The class' name in string */
	public static final String NAME = "Unobtainium";
	
	/** The initialization of object Unobtainium within game */
	public Unobtainium(float x, float y) throws SlickException {
		super(x, y, "assets/unobtainium_mine.png", MAXAMOUNT);
	}
	
	/** @override
	 *  setting new amount for unobtainium object if new amount is less than maximum resource amount */
	public void setAmount(int amount) {
		if (amount < MAXAMOUNT) {
			super.setAmount(amount);
		}
	}
}
