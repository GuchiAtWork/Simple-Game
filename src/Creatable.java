import org.newdawn.slick.SlickException;

/** Interface that represents a creation of an object (e.g. Builder making Factory) */
public interface Creatable<T> {
	/** Method that represents the creating process beneath an object (e.g. Waiting for time to buikd) */
	public abstract void createProcess(int delta, Place place, T container, Player player) throws SlickException;
}
