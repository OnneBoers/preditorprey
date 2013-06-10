/**
 * Implementation of the required Rabbit
 * @author Xeryus Stokkel
 *
 */
public class Rabbit extends Prey {
	public final static int RABBIT_ENERGY = 10;
	public final static int RABBIT_ENERGY_DIST = 5;
	public final static int RABBIT_MAX_PROCREATION_COUNT = 4;
	
	private int procreationCount = 0;
	
	public Rabbit() {
		super();
		setEnergy(RABBIT_ENERGY + Main.forest.getRandom().nextInt(RABBIT_ENERGY_DIST));
	}
	
	public Rabbit(int x, int y) {
		super (x, y);
		setEnergy(RABBIT_ENERGY + Main.forest.getRandom().nextInt(RABBIT_ENERGY_DIST));
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
		else if (procreationCount <= RABBIT_MAX_PROCREATION_COUNT) {
			if (procreate())
				procreationCount++;
		}
		
		lowerEnergy();
	}
	
	@Override
	public String toString() {
		return "Rabbit(" + getX() + ", " + getY() + "): " + getEnergy();
	}
	
	public String printCharacter() {
		return "R";
	}
}
