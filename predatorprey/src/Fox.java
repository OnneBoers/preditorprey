
public class Fox extends Predator {
	private final int FOX_ENERGY = 20;
	private final int FOX_LAST_EATEN_LIMIT = 5;
	private final int FOX_MAX_PROCREATION_COUNT = 2;
	private final int FOX_MAX_EAT_COUNT = 2;
	
	private int lastEaten = 0;
	private int procreationCount = 0;
	
	public Fox() {
		super();
		setEnergy(FOX_ENERGY);
	}
	
	public Fox(int x, int y) {
		super(x, y);
		setEnergy(FOX_ENERGY);
	}

	public void turn() {
		Animal[] surroundingAnimals = Main.forest.getSurroundingAnimals(getX(), getY());
		Boolean nextToFox = false;
		int animalsEaten = 0;
		
		// Check the surroundings for food and partners
		for (Animal a : surroundingAnimals) {
			if (a instanceof Fox) {
				nextToFox = true;
			} else if (a instanceof Rabbit && animalsEaten < FOX_MAX_EAT_COUNT) {
				eat(a.getX(), a.getY());
				setLastEaten(FOX_LAST_EATEN_LIMIT);
				animalsEaten++;
			} else if (a instanceof Mouse && !((Mouse)a).getHasFlown() && animalsEaten < FOX_MAX_EAT_COUNT) {
				eat(a.getX(), a.getY());
				animalsEaten++;
			}
		}
		
		if (nextToFox && lastEaten>0 && procreationCount< FOX_MAX_PROCREATION_COUNT) {
			if (procreate())
				procreationCount++;
		}
		
		lowerEnergy();
	}
	
	public void setLastEaten(int last) {
		this.lastEaten = last;
	}

	public String printCharacter() {
		return "F";
	}
}
