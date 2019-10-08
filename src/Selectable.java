import org.newdawn.slick.Input;

/** Interface that represents selection within an object (e.g. selecting which units to build) */
public interface Selectable {
	/** Method that represents the selecting process */
	public abstract void selectProcess(Input input, Place place, Player player);
}
