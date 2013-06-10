/**
 * Class that is purely meant to determine whether an animal is a predator or a prey. All predators should inherit from this class
 * @author Xeryus Stokkel
 * @see Prey
 */
public abstract class Predator extends Animal {
	public Predator() {
		super();
	}
	
	public Predator(int x, int y) {
		super(x, y);
	}
	
	public Animal eat(int x, int y) {
		return Main.forest.eatAnimal(getX(), getY(), x, y);
	}
}
