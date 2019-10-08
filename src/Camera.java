/**
 * The class that stores possible world coordinates of camera through setting and passing conditions set.
 */

public class Camera {
	private static final int HALF = 2;
	
	private float cameraX, cameraY;
	
	/** Initializes objects by immediately setting camera coordinates */
	public Camera(float x, float y, Place place) {
		updateCamX(x, place);
		updateCamY(y, place);
	}
	
	/** 
	 * updates x-coordinate of camera if passes certain conditions (not passing borders and 
	 * not letting screen show black background 
	 */
	public void updateCamX(float x, Place place) {
		float left = x - (App.WINDOW_WIDTH / HALF), right = x + (App.WINDOW_WIDTH / HALF);
		int maxWidth = place.mapWidth();
		if (left < Place.BORDERLIMIT) {
			left = Place.BORDERLIMIT;
		}	
		if (right > maxWidth) {
			// to prevent from camera showing black background if right exceeds border limit
			left = maxWidth - App.WINDOW_WIDTH;
		}
		cameraX = left;
	}
	
	/** 
	 * updates y-coordinate of camera if passes certain conditions (not passing borders and 
	 * not letting screen show black background 
	 */
	public void updateCamY(float y, Place place) {
		float top = y - (App.WINDOW_HEIGHT / HALF), bottom = y + (App.WINDOW_HEIGHT / HALF);
		int maxHeight = place.mapHeight();
		if (top < Place.BORDERLIMIT) {
			top = Place.BORDERLIMIT;
		}
		if (bottom > maxHeight) {
			// to prevent from camera showing black background if bottom exceeds border limit
			top = maxHeight - App.WINDOW_HEIGHT;
		}
		cameraY = top;
	}
	
	/** Returns x-coordinate of camera position */
	public float getCameraX() {
		return cameraX;
	}
	
	/** Returns y-coordinate of camera position */
	public float getCameraY() {
		return cameraY;
	}
}



