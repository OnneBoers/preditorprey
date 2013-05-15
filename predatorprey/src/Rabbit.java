/**
 * Implementation of the required Rabbit
 * @author Xeryus Stokkel
 *
 */
public class Rabbit extends Prey {
	public final static int RABBIT_ENERGY = 50;
	@SuppressWarnings("unused")
	private final String PRINT_CHARACTER = "R";
	
	public Rabbit() {
		super();
		setEnergy(RABBIT_ENERGY);
	}
	
	public Rabbit(int x, int y) {
		super (x, y);
		setEnergy(RABBIT_ENERGY);
	}
	
	public void turn() {
		Boolean lonely = true; 
		Animal[] surrounding = Main.forest.getSurroundingAnimals(getX(), getY());
		// Check if the animal is lonely (there are no other rabbits surrounding it)
		for(Animal a : surrounding) {
			if (a instanceof Rabbit) {
				lonely = false;
			}
		}
		if (lonely)
			die();
		else
			procreate();
		
		// Finally lower the energy
		setEnergy(getEnergy()-1);
		// If the animal runs out of energy it should die
		if (getEnergy() == 0)
			die();
	}
	
	@Override
	public String toString() {
		return "Rabbit(" + getX() + ", " + getY() + "): " + getEnergy();
	}
	
	public String printCharacter() {
		return "R";
	}
}
