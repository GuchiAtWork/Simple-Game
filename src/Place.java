import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.*;

/** The class that is responsible for storing tiled maps and informing its properties towards other classes */
public class Place {
	/** The initial border limits that are set within map */
	public static final int BORDERLIMIT = 0;
	/** Pixels per tile set within this game */
	public static final int PIXELSPERTILE = 64; 
	
	/** Represents one of the properties that a tile can possess */
	public static final String TILEPROP1 = "solid";
	/** the index of the "solid" property */
	public static final int PROPLAYER1 = 0;
	
	/** Represents one of the properties that a tile can possess */
	public static final String TILEPROP2 = "occupied";
	/** the index of the "occupied" property */
	public static final int PROPLAYER2 = 0;
	
	/** The default value given if methods in class can't give desired values */
	public static final String DEFAULT = "-1";
	
	private TiledMap background;
	
	/** responsible for initializing object of Place class */
	public Place(String location) throws SlickException {
		background = new TiledMap(location);
	}
	
	/** responsible for changing world coordinate into tile x and y coordinates if called */
	public int changeToTile(float coordinate) {
		return ((int) (coordinate/PIXELSPERTILE));
	}
	
	/** finds global ID of tile */
	public int findTileId(int x_coord, int y_coord, int layerIndex) {
		return background.getTileId(x_coord, y_coord, layerIndex);
	}
	
	/** responsible for finding property of a specific tile */
	public String valueOfProp(int tileId, String propertyName, String def) {
		return background.getTileProperty(tileId, propertyName, def);
	}
	
	/** returns Height of tiled map in terms of world coordinates and not tile count */
	public int mapHeight() {
		return (background.getHeight() * PIXELSPERTILE);
	}
	
	/** returns Width of tiled map in terms of world coordinates */
	public int mapWidth() {
		return (background.getWidth() * PIXELSPERTILE);
	}
	
	/** responsible for displaying map onto screen */
	public void render() {
		background.render(BORDERLIMIT, BORDERLIMIT);
	}
}
