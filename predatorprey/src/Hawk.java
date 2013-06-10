
public class Hawk extends Predator implements MovableAnimal {
	private final int HAWK_ENERGY = 30;
	private final int HAWK_ENERGY_DIST = 10;
	private final int HAWK_PROCREATION_AGE_START = 10;
	private final int HAWK_PROCREATION_AGE_END = 40;
	private final int HAWK_MAX_PROCREATION_COUNT = 1;
	private final int HAWK_MAX_EAT_COUNT = 2;
	
	private int startEnergy;
	private int procreationCount = 0;
	private boolean hasMetHawk = false;
	
	
	public Hawk() {
		super();
		startEnergy = HAWK_ENERGY + Main.forest.getRandom().nextInt(HAWK_ENERGY_DIST);
		setEnergy(startEnergy);
	}
	
	public Hawk(int x, int y) {
		super(x, y);
		startEnergy = HAWK_ENERGY + Main.forest.getRandom().nextInt(HAWK_ENERGY_DIST);
		setEnergy(startEnergy);
	}
	
	public void turn() {
		Animal[] surroundingAnimals = Main.forest.getSurroundingAnimals(getX(), getY());
		int animalsEaten = 0;
		
		for (Animal a : surroundingAnimals) {
			if (a instanceof Hawk) {
				hasMetHawk = true;
			} else if (a instanceof Prey && animalsEaten < HAWK_MAX_EAT_COUNT) {
				if (a instanceof Mouse && ((Mouse)a).getHasFlown())
					continue;
				eat(a.getX(), a.getY());
				animalsEaten++;
			}
		}
		
		if (animalsEaten == 0)
			Main.forest.moveAnimal(getX(), getY(), getX()+Main.forest.getRandom().nextInt(2)-1, getY()+Main.forest.getRandom().nextInt(2)-1);
		
		if (hasMetHawk && (startEnergy-getEnergy()) > HAWK_PROCREATION_AGE_START && (startEnergy-getEnergy() < HAWK_PROCREATION_AGE_END)) {
			if (procreationCount < HAWK_MAX_PROCREATION_COUNT && procreate())
				procreationCount++;
		}
		
		lowerEnergy();
	}
	
	public void move(int x, int y) {
		setX(x);
		setY(y);
	}

	public String printCharacter() {
		return "H";
	}
}
