import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** Class responsible for maintaining game by looking over objects, map, player info and camera */
public class World {
	private static final float DEFAULT = 0f;
	private static final float CAMSPEED = 0.4f;
	private static final int NOTINARRAY = -1;
	// used to check if there are any close elements to mouse when clicked
	private static final double CHECK = 100.0;
	// first element of array (0)
	private static final int FIRSTELEMENT = 0;
	private static final int HALF = 2;
	
	private WorldInjector worldInjector;
	private Player player;
	private Place background;
	private BuildingContainer buildings;
	private UnitContainer units;
	private ResourceContainer resources;
	private Camera worldCam;
	
	private float leftClickX;
	private float leftClickY;
	
	// indexes represent closest element to mouse if clicked 
	private int indexBuild = NOTINARRAY;
	private int indexUnit = NOTINARRAY;
	
	private boolean buildChosen = false;
	private boolean unitChosen = false;
	// variable that represents finding closest object to mouse 
	private boolean findingClosest = false;
	private boolean leftClicked = false;

	/** Initializes object of class "World" */
	public World(String objectsFile) throws SlickException {
		worldInjector = new WorldInjector(objectsFile);
		player = new Player();
		background = new Place("assets/main.tmx");
		buildings = new BuildingContainer();
		units = new UnitContainer();
		resources = new ResourceContainer();
		worldInjector.creation(buildings, resources, units, background);
		worldCam = new Camera(DEFAULT, DEFAULT, background);
	}
	
	/** method that updates all objects, especially if object is selected via left click */
	public void update(Input input, int delta) throws SlickException {
		ArrayList<Building> buildArray = buildings.getBuildings();
		ArrayList<Unit> unitArray = units.getUnits();
		ifKeyPressed(input, delta);
		leftClickSet(input);
		updateUnits(input, delta, unitArray);
		
		// to prevent closest building from mouse to being selected
		if (unitChosen) {
			findingClosest = false;
		}
		
		updateBuildings(input, delta, buildArray);
		
		/* 
		 * updates objects if selected and constantly update world camera by following selected objects or deactivates 
		 * finding process near mouse click
		 */
		if (unitChosen) {
			if (!(units.findUnitSelect(indexUnit))) {
				units.selectUnit(indexUnit, true);
			}
			worldCam.updateCamX(units.findUnitX(indexUnit), background);
			worldCam.updateCamY(units.findUnitY(indexUnit), background);
		} else if (buildChosen) {
			if (!(buildings.findBuildSelect(indexBuild))) {
				buildings.selectBuilding(indexBuild, true);
				findingClosest = false;
				// only needed once as building is stationary
				worldCam.updateCamX(buildings.findBuildX(indexBuild), background);
				worldCam.updateCamY(buildings.findBuildY(indexBuild), background);
			}
		} else {
			leftClicked = false;
			findingClosest = false;
		}
	}
	
	/** renders all objects onto screen, as well as focusing which objects come onto screen (via camera) */
	public void render(Graphics g) {
		ArrayList<Building> buildArray = buildings.getBuildings();
		ArrayList<Resource> resourceArray = resources.getResources();
		ArrayList<Unit> unitArray = units.getUnits();
		g.translate(-worldCam.getCameraX(), -worldCam.getCameraY());
        background.render();	
		for (Resource resource : resourceArray) {
			resource.render();
		}
		for (Building building : buildArray) {
			building.render(g, worldCam);
		}
		for (Unit unit : unitArray) {
			unit.render(g, worldCam);
		}
		player.render(g, worldCam);
	}
	
	/* Method mainly updates units but also tries to find a specific unit that are either close to mouse click
	 * or has created Command Centers to allow further processes */
	private void updateUnits(Input input, int delta, ArrayList<Unit> unitArray) throws SlickException {
		int index = 0;
		double currentDistance, closeDistance = CHECK;
		for (Unit unit : unitArray) {
			
			// searching process either activated by if left click is present or still trying to find closest unit to click
			if (leftClicked || findingClosest) {
				double difX = (double) (unit.getX() - leftClickX), difY = (double) (unit.getY() - leftClickY);
				if ((currentDistance = Math.hypot(difX, difY)) <= Searchable.PROXIMITYTO) {
					if (currentDistance < closeDistance) {
						closeDistance = currentDistance;
						indexUnit = index;
					}
					leftClicked = false;
					unitChosen = true;
				}
			}
			
			// the underlying process beneath if a unit creates a command center
			if (unit.getCreateComCentre()) {
				/* if index of selected unit is not less than index, indexUnit needs to be minus one to assure that 
				 * selected unit is still same */
				if (!(indexUnit < index)) {
					indexUnit--;
				} else if (indexUnit == index) {
					indexUnit = NOTINARRAY;
					unitChosen = false;
				}
				
				units.removeUnits(index);
				index--;
				// to prevent access outside of array
				if (index < FIRSTELEMENT) {
					index = FIRSTELEMENT;
				}
			}
			units.updateUnit(input, delta, background, index, buildings, resources, player, worldCam);
			index++;
		}
	}
	
	/* Method mainly updates buildings but also tries to find a specific building that is close to mouse click */
	private void updateBuildings(Input input, int delta, ArrayList<Building> buildArray) throws SlickException {
		int index = 0;
		double currentDistance, closeDistance = CHECK;
		for (Building building : buildArray) {
			if (leftClicked || findingClosest) {
				double difX = (double) (building.getX() - leftClickX), difY = (double) (building.getY() - leftClickY);
				if ((currentDistance = Math.hypot(difX, difY)) <= Searchable.PROXIMITYTO) {
					if (currentDistance < closeDistance) {
						closeDistance = currentDistance;
						indexBuild = index;
					}
					leftClicked = false;
					buildChosen = true;
				}
			}
			buildings.updateBuilding(input, delta, background, index, units, player);
			index++;
		}
	}

	/* Method that occurs when left click input is detected (resets most values and stores left click coordinates) */
	private void leftClickSet(Input input) {
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			resetEntitySelect();
			leftClicked = true;
			findingClosest = true;
			leftClickX = worldCam.getCameraX() + input.getMouseX();
			leftClickY = worldCam.getCameraY() + input.getMouseY();
		}
	}
	
	/* Method that occurs to reset variables to prevent multiple selection over objects or crash program */
	private void resetEntitySelect() {
		if (unitChosen) {
			units.selectUnit(indexUnit, false);
			indexUnit = NOTINARRAY;
			unitChosen = false;
		} else if (buildChosen) {
			buildings.selectBuilding(indexBuild, false);
			indexBuild = NOTINARRAY;
			buildChosen = false;
		}
	}
	
	/* Method that allows camera to move around map if only specific keys are pressed */
	private void ifKeyPressed(Input input, int delta) {
		if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_A) || 
				input.isKeyDown(Input.KEY_D)) {
				resetEntitySelect();
				
				// World Camera stores in terms of coordinates in the middle of screen, hence needed 
				float middleX = worldCam.getCameraX() + (App.WINDOW_WIDTH / HALF);
				float middleY = worldCam.getCameraY() + (App.WINDOW_HEIGHT / HALF);
				
				if (input.isKeyDown(Input.KEY_W)) {
					worldCam.updateCamY(middleY - delta*CAMSPEED, background);
				} else if (input.isKeyDown(Input.KEY_S)) {
					worldCam.updateCamY(middleY + delta*CAMSPEED, background);
				} else if (input.isKeyDown(Input.KEY_A)) {
					worldCam.updateCamX(middleX - delta*CAMSPEED, background);
				} else if (input.isKeyDown(Input.KEY_D)) {
					worldCam.updateCamX(middleX + delta*CAMSPEED, background);
				}
			}
	}
}
