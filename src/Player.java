import org.newdawn.slick.Graphics;

/** Class that focuses on player information, in regards to resource amount acquired */
public class Player {
	private static final int AMOUNTMINIMUM = 0;
	private static final float TEXTPLACEX = 32.0f;
	private static final float TEXTPLACEY = 75.0f;
	
	private int metalAmount;
	private int unobtainiumAmount;
	
	/** Responsible for the initialization of "Player" Object */
	public Player() {
		this.metalAmount = AMOUNTMINIMUM;
		this.unobtainiumAmount = AMOUNTMINIMUM;
	}

	/** Responsible for displaying player information on screen */
	public void render(Graphics g, Camera worldCam) {
		g.drawString("Metal: " + metalAmount + "\nUnobtainium: " + unobtainiumAmount, worldCam.getCameraX() + TEXTPLACEX,
				     worldCam.getCameraY() + TEXTPLACEY);
	}
	
	/** Returns amount of metal that player currently possesses */
	public int getMetalAmount() {
		return metalAmount;
	}

	/** Changes the current metal amount that the player has by setting a new metal amount (within reasonable standards) */
	public void setMetalAmount(int metalAmount) {
		if (metalAmount <= AMOUNTMINIMUM) {
			this.metalAmount = AMOUNTMINIMUM;
		} else {
			this.metalAmount = metalAmount;
		}
	}

	/** Returns amount of unobtainium that player currently possesses */
	public int getUnobtainiumAmount() {
		return unobtainiumAmount;
	}

	/** Changes the current unobtainium amount that the player has by 
	 *  setting a new unobtainium amount (within reasonable standards) */
	public void setUnobtainiumAmount(int unobtainiumAmount) {
		if (unobtainiumAmount <= AMOUNTMINIMUM) {
			this.unobtainiumAmount = AMOUNTMINIMUM;
		} else {
			this.unobtainiumAmount = unobtainiumAmount;
		}
	}
	
	
	
	
}
