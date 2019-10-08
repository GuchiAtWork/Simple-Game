import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


/** The unit responsible for mining and delivering the resources within game */
public class Engineer extends Unit{
	private static final float ENGINEERSPEED = 0.1f;
	private static final int TIMETOBUILD = 5000;
	private static final int NOTINARRAY = -1;
	private static final double DEFAULT = -10.0;
	
	// Different adjustments given to texts for proper display on screen
	private static final float TEXTADJUSTX = -27f;
	private static final float TEXTADJUSTY = -30f;
	
	/** The subclass' name in string */
	public static final String NAME = "Engineer";
	
	// How much resources the engineer can mine
	private static int MININGCAPACITY = 2;
	
	/** The number added to MININGCAPACITY when its limit is extended (when pylon is activated) */
	public static final int ADDTOCAPACITY = 1;
	
	private int resourceAmount = 0;
	private String resourceName = "";
	
	// These variables serve as indexes to store element of interest in an ListArray
	private int nextResource = NOTINARRAY;
	private int indexOfResource = NOTINARRAY;
	private int indexOfComCentre = NOTINARRAY;
	
	// These variables serve as conditions to allow certain processes to occur
	private boolean notSearched = true;
	private boolean found = false;
	private boolean miningDisplay = false;
	private boolean findComCentre = false;
	private boolean automaticSearch = false;
	
	private int timeElapsed = 0;
	
	/** Initializes object of subclass Engineer */
	public Engineer(float x, float y, Place place) throws SlickException {
		super("assets/engineer.png", x, y, place);
		super.setMovePixelPerMS(ENGINEERSPEED);
	}

	/** @override
	 *  Responsible for updating the movements and processes which the unit currently undergoes.
	 */
	public void update(Input input, int delta, Place place, BuildingContainer buildings, ResourceContainer resources,
			           Player player, Camera worldCam) throws SlickException {
		findingAndMining(delta, buildings, resources);
		comCentreDelivery(delta, place, buildings, player);
		automaticSearching(delta, place, resources);
		super.update(input, delta, place, buildings, resources, player, worldCam);
	}
	
	/** @override 
	 * Responsible for displaying the unit, alongside with the texts that may occur if certain processes/conditions
	 * are fulfilled.
	 */
	public void render(Graphics g, Camera worldCam) {
		if (miningDisplay) {
			g.drawString("MINING", super.getX() + TEXTADJUSTX, super.getY() + TEXTADJUSTY);
		}
		super.render(g, worldCam);
	}
	
	/** Method for returning how much resources the engineer can bring along to the Command Centre. */ 
	public static int getMiningCapacity() {
		return MININGCAPACITY;
	}

	/** Method for increasing the mining capacity of the engineer */
	public static void setMiningCapacity(int newMiningCapacity) {
		// determines if the new mining capacity can be set (Mining capacity can only be increased by one)
		if (MININGCAPACITY + ADDTOCAPACITY == newMiningCapacity) {
			
			MININGCAPACITY = newMiningCapacity;
		}
	}
	
	/*
	 * This method allows the engineer to start mining if there are nearby resources around the vicinity. However, iff
	 * the engineer does not have any resources with them at the first place
	 */
	private void findingAndMining(int delta, BuildingContainer buildings, ResourceContainer resources) {
		if (resourceAmount == 0) {
			ArrayList<Resource> resourceArray = resources.getResources();
			
			/* if haven't found any resources, will start trying to find resources nearby themselves and if found, will 
			 * store resource of interest and activate its capability to mine */ 
			if (notSearched) {
				searchResources resourceHave = new searchResources();
				indexOfResource = resourceHave.search(super.getX(), super.getY(), resourceArray);
				if (indexOfResource > NOTINARRAY) {
					timeElapsed = 0;
					found = true;
					notSearched = false;
					miningDisplay = true;
				}
			}
			
			/* when nearby resource is found, will start mining (done via time measurement and calculation of 
			 * closeness to mine). Also will modify amount of resources left in mine itself. 
			 */
			if (found) {
				double difX = super.getX() - resourceArray.get(indexOfResource).getX();
				double difY = super.getY() - resourceArray.get(indexOfResource).getY();
				if (timeElapsed > TIMETOBUILD) {
					resourceAmount = MININGCAPACITY;
					
					/* if mine resources is less than engineer's mining capacity, will determine amount of resources
					   the engineer can actually carry */
					if (resourceArray.get(indexOfResource).getAmount() - MININGCAPACITY < Resource.NOMORELEFT) {
						resourceAmount = Math.abs(resourceArray.get(indexOfResource).getAmount() - MININGCAPACITY);
					}
					
					resources.modifyResource(indexOfResource, MININGCAPACITY);
					if (resourceArray.get(indexOfResource).getAmount() == Resource.NOMORELEFT) {
						resources.removeResources(indexOfResource);
					}
					resourceName = resourceArray.get(indexOfResource).getClass().getSimpleName();
					findComCentre = true;
					automaticSearch = true;
					
					// resets value of variables
					found = false;
					miningDisplay = false;
					indexOfResource = NOTINARRAY;
				
				// determines course of action if engineer strays away from mine of interest
				} else if (Math.hypot(difX, difY) > Searchable.PROXIMITYTO) {
					indexOfResource = NOTINARRAY;
					found = false;
					notSearched = true;
					miningDisplay = false;
				} else {
					timeElapsed += delta;
				}
			}
		}
	}
	
	/*
	 * Method responsible for finding the nearest Command Center and delivering its resources there
	 */
	private void comCentreDelivery(int delta, Place place, BuildingContainer buildings, Player player) {
		// on process of finding nearby Command Center
		if (findComCentre) {
			ArrayList<Building> buildArray = buildings.getBuildings();
			searchComCentres findingComCentre = new searchComCentres();
			indexOfComCentre = findingComCentre.search(super.getX(), super.getY(), buildArray);
			if (indexOfComCentre > NOTINARRAY) {
				findComCentre = false;
			}
		}
		
		/* will begin to move and deliver iff there is a Command Center and there are no instructions from the player
		   to move the unit */
		if (indexOfComCentre > NOTINARRAY && !(super.getIfClicked())) {
			super.move(buildings.findBuildX(indexOfComCentre), buildings.findBuildY(indexOfComCentre), delta, place);
			double difX = super.getX() - buildings.findBuildX(indexOfComCentre);
			double difY = super.getY() - buildings.findBuildY(indexOfComCentre);
			
			// dependent on name of resource carried, will modify player's amount of resources differently
			if (Math.hypot(difX, difY) <= Searchable.PROXIMITYTO) {
				if (resourceName.equals(Metal.NAME)) {
					player.setMetalAmount(player.getMetalAmount() + resourceAmount);
				} else if (resourceName.equals(Unobtainium.NAME)) {
					player.setUnobtainiumAmount(player.getUnobtainiumAmount() + resourceAmount);
				}
				
				notSearched = true;
				resourceAmount = 0;
				resourceName = "";
				indexOfComCentre = NOTINARRAY;
			}
		}
	}
	
	/*
	 * Once engineer has mined and delivered resources, the engineer themselves will attempt to find the nearest
	 * resource and head there
	 */
	private void automaticSearching(int delta, Place place, ResourceContainer resources) {
		// will move to resource if there is no player guidance and delivery instructions
		if (automaticSearch && !(super.getIfClicked()) && indexOfComCentre == NOTINARRAY) {
			searchCloseResource whereMine = new searchCloseResource();
			nextResource = whereMine.search(super.getX(), super.getY(), resources.getResources());
			/* stops engineer from heading towards nearest resource if there is none or it tries to access outside of
			   resource array */
			if (nextResource >= resources.resourceSize() || nextResource == NOTINARRAY) {
				automaticSearch = false;
			} else {
				super.move(resources.resourceX(nextResource), resources.resourceY(nextResource), delta, place);
			}
		}
	}
	
	/* 
	 * Class made to implement Searchable interface. The method within is responsible for spitting out the index 
	 * of the closest resource (or none) to the engineer within a certain distance (32 pixels)
	 * @param x - where the unit is in the x-axis
	 * @param y - where the unit is in the y-axis 
	 * @return closestIndex - returns the index in resource array where resource is closest or -1 if not
	 */
	private class searchResources implements Searchable<Resource> {
		/** responsible for finding closest resource to engineer within 32 pixels */
		public int search(float x, float y, ArrayList<Resource> resources) {
			int index = 0, closestIndex = NOTINARRAY;
			double currentDistance, closestDistance = DEFAULT;
			for (Resource resource : resources) {
				double difX = x - resource.getX(), difY = y - resource.getY();
				if ((currentDistance = Math.hypot(difX, difY)) <= Searchable.PROXIMITYTO) {
					
					// first resource is found within acceptable limits
					if (closestDistance == DEFAULT) {
						closestDistance = currentDistance;
						closestIndex = index;
					}
					if (currentDistance < closestDistance) {
						closestDistance = currentDistance;
						closestIndex = index;
					}
				}
				index++;
			}
			return closestIndex;
		}
	}
	
	/* 
	 * Class made to implement Searchable interface. The method within is responsible for spitting out the index 
	 * of the closest resource (or none) to the engineer.
	 */
	private class searchCloseResource implements Searchable<Resource> {
		/** responsible for finding resource closest to engineer */
		public int search(float x, float y, ArrayList<Resource> resources) {
			int index = 0, closestIndex = NOTINARRAY;
			double currentDistance, closestDistance = DEFAULT;
			for (Resource resource : resources) {
				double difX = x - resource.getX(), difY = y - resource.getY();
				currentDistance = Math.hypot(difX, difY);
				
				// first resource is found 
				if (closestDistance == DEFAULT) {
					closestDistance = currentDistance;
					closestIndex = index;
				}
				if (currentDistance < closestDistance) {
					closestDistance = currentDistance;
					closestIndex = index;
				}
				index++;	
			}
		return closestIndex;
		}
	}
	
	/* 
	 * Class made to implement Searchable interface. The method within is responsible for spitting out the index 
	 * of the closest Command Center (or none) to the engineer.
	 */
	private class searchComCentres implements Searchable<Building> {
		/** responsible for finding Command Center closest to engineer */
		public int search(float x, float y, ArrayList<Building> buildings) {
			int index = 0, closestIndex = NOTINARRAY;
			double currentDistance, closestDistance = DEFAULT;
			for (Building building : buildings) {
				if (building.getClass().getSimpleName().equals(CommandCentre.NAME)) {
					double difX = x - building.getX(), difY = y - building.getY();
					currentDistance = Math.hypot(difX, difY);
					
					// first Command Center is found 
					if (closestDistance == DEFAULT) {
						closestDistance = currentDistance;
						closestIndex = index;
					}
					if (currentDistance < closestDistance) {
						closestDistance = currentDistance;
						closestIndex = index;
					}
				}
				index++;
			}
			return closestIndex;
		}
	}
}
