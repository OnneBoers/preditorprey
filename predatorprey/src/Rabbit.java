/**
 * Implementation of the required Rabbit
 * @author Xeryus Stokkel
 *
 */
public class Rabbit extends Prey {
	public Rabbit() {
		super();
		setEnergy(50);
	}
	
	public void turn() {
		Boolean lonely = true; 
		
		// Check if the animal is lonely (there are no other rabbits surrounding it)
		for(Animal a : Main.forest.getSurroundingAnimals(getX(), getY())) {
			if (a instanceof Rabbit) {
				lonely = false;
				break;
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
}
