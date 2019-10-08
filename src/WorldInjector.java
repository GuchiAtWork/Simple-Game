import java.io.FileReader;
import java.io.BufferedReader;

/** Class that focuses on injecting objects into the game via csv file */
public class WorldInjector {
	// These constants represent the elements within the csv file
	private static final int NAMEOFENTITY = 0;
	private static final int XCOORD = 1;
	private static final int YCOORD = 2;
	
	private static final String PYLON = "pylon";
	private static final String COMMAND_CENTRE = "command_centre";
	private static final String FACTORY = "factory";
	private static final String SCOUT = "scout";
	private static final String BUILDER = "builder";
	private static final String TRUCK = "truck";
	private static final String ENGINEER = "engineer";
	private static final String UNOBTAINIUM = "unobtainium_mine";
	private static final String METAL = "metal_mine";
	
	// the location of the csv file within computer
	private String objectsFile;
	
	/** Initializes object of class WorldInjector (brings things into life) */
	public WorldInjector(String objectsFile) {
		this.objectsFile = objectsFile;
	}
	
	/** Actual process of adding objects into their corresponding Arrays by reading csv file */
	public void creation(BuildingContainer buildings, ResourceContainer resources, UnitContainer units, Place place) {
		try (BufferedReader br =
			 new BufferedReader(new FileReader(objectsFile))) {

	         String text;
	    
	         while ((text = br.readLine()) != null) {
	        	 // breaking record into multiple elements 
	        	 String[] entity = text.split(",");
	        	 // storing world coordinates found in csv file for one record 
	        	 float x = Float.parseFloat(entity[XCOORD]);
	        	 float y = Float.parseFloat(entity[YCOORD]);
	        	 
	        	 if (entity[NAMEOFENTITY].equals(COMMAND_CENTRE)) {
	        		 buildings.addBuildings(new CommandCentre(x, y, place));
	        	 } else if (entity[NAMEOFENTITY].equals(METAL)) {
	        		 resources.addResources(new Metal(x, y));
	        	 } else if (entity[NAMEOFENTITY].equals(UNOBTAINIUM)) {
	        		 resources.addResources(new Unobtainium(x, y));
	        	 } else if (entity[NAMEOFENTITY].equals(PYLON)) {
	        		 buildings.addBuildings(new Pylon(x, y, place));
	        	 } else if (entity[NAMEOFENTITY].equals(ENGINEER)) {
	        		 units.addUnits(new Engineer(x, y, place));
	        	 } else if (entity[NAMEOFENTITY].equals(SCOUT)) {
	        		 units.addUnits(new Scout(x, y, place));
	        	 } else if (entity[NAMEOFENTITY].equals(TRUCK)) {
	        		 units.addUnits(new Truck(x, y, place));	
	        	 } else if (entity[NAMEOFENTITY].equals(BUILDER)) {
	        		 units.addUnits(new Builder(x, y, place));
	        	 } else if (entity[NAMEOFENTITY].equals(FACTORY)) {
	        		 buildings.addBuildings(new Factory(x, y, place));
	        	 }
	         }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}
