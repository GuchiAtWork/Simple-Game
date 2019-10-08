import java.util.ArrayList;

/** Interface that represents searching for an object */
public interface Searchable<T>{
	/** the minimum distance between two objects that is needed for object of interest to be recognized
	  * (not always that case that this is needed) */
	public static final int PROXIMITYTO = 32;
	
	/** Method that represents the searching for an object within an ArrayList */
	public abstract int search(float x, float y, ArrayList<T> objectList);
}
