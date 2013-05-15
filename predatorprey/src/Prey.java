/**
 * Class that is purely meant to determine whether an animal is a predator or a prey. All prey should inherit from this class.
 * @author Xeryus Stokkel
 * @see Predator
 */
public abstract class Prey extends Animal {
	public Prey() {
		super();
	}
	
	public Prey(int x, int y) {
		super(x, y);
	}
}
