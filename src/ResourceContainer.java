import java.util.ArrayList;

/** The container which holds objects of Resource class (used for encapsulation) */
public class ResourceContainer {
	private static final int MININDEX = 0;
	
	private ArrayList<Resource> resources;
	
	/** Responsible for the initialization of object */
	public ResourceContainer() {
		resources = new ArrayList<Resource>();
	}
	
	/** returns new copy of ResourceContainer object */
	public ArrayList<Resource> getResources() {
		return new ArrayList<Resource>(resources);
	}
	
	/** adds new resource to contained ArrayList */
	public void addResources(Resource newResource) {
		if (newResource != null) {
			resources.add(newResource);
		}
	}
	
	/** removes specified resource in contained ArrayList */
	public void removeResources(int index) {
		if (index >= MININDEX && index < resources.size()) {
			resources.remove(index);
		}
	}
	
	/** modifies amount of specific resource within ArrayList */
	public void modifyResource(int index, int deductedAmount) {
		if (index >= MININDEX && index < resources.size()) {
			
			// used to identify if deducted amount is legitimate 
			if (deductedAmount == Engineer.getMiningCapacity()) {
				int resourceAmount = resources.get(index).getAmount();
				resources.get(index).setAmount(resourceAmount - deductedAmount);
			}
		}
	}
	
	/** returns x-coordinate of specified resource */
	public float resourceX(int index) {
		return resources.get(index).getX();
	}
	
	/** returns y-coordinate of specified resource */
	public float resourceY(int index) {
		return resources.get(index).getY();
	}
	
	/** returns size of resources within contained ArrayList */
	public int resourceSize() {
		return resources.size();
	}
}
