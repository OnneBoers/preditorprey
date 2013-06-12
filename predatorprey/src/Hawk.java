/** Implementation of the free to choose predator.
 * 
 * Hawks will move around to find prey, if there is no prey close to them. They will only eat HAWK_MAX_EAT_COUNT per turn so they can sit around the same
 * area for a while. Hawks will fight with other hawks if they are nearby and there is also food available, this will cost them HAWK_FIGHT_COST energy per
 * fight.
 * <p>
 * Hawks only reproduce from age HAWK_PROCREATION_AGE_START to age HAWK_PROCREATION_AGE_END. Hawks will only procreate HAWK_MAX_PROCREATION_COUNT initially.
 * When this limit is reached they can reproduce one more time, but the possibility to do this is only HAWK_2ND_PROCREATION_CHANCE big.
 * 
 * @author Xeryus Stokkel and Onne Boers
 *
 */
public class Hawk extends Predator implements MovableAnimal {
	private final int HAWK_ENERGY = 40;
	private final int HAWK_ENERGY_DIST = 10;
	private final int HAWK_PROCREATION_AGE_START = 15;
	private final int HAWK_PROCREATION_AGE_END = 45;
	private final int HAWK_MAX_PROCREATION_COUNT = 1;
	private final int HAWK_MAX_EAT_COUNT = 2;
	private final double HAWK_2ND_PROCREATION_CHANCE = 0.01;
	private final int HAWK_FIGHT_COST = 1;
	
	private int startEnergy;
	private int procreationCount = 0;
	private boolean hasMetHawk = false;
	private boolean done2ndProcreation = false;
	
	
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
		boolean hasMetRival = false;
		
		for (Animal a : surroundingAnimals) {
			if (a instanceof Hawk) {
				hasMetHawk = true;
				hasMetRival = true;
			} else if (a instanceof Prey && animalsEaten < HAWK_MAX_EAT_COUNT) {
				if (a instanceof Mouse && ((Mouse)a).getHasFlown())
					continue;
				if (hasMetRival)
					setEnergy(getEnergy() - HAWK_FIGHT_COST);
				eat(a.getX(), a.getY());
				animalsEaten++;
			}
		}
		
		if (animalsEaten == 0)
			Main.forest.moveAnimal(getX(), getY(), getX()+Main.forest.getRandom().nextInt(3)-1, getY()+Main.forest.getRandom().nextInt(3)-1);
		
		if (hasMetHawk && (startEnergy-getEnergy()) > HAWK_PROCREATION_AGE_START && (startEnergy-getEnergy() < HAWK_PROCREATION_AGE_END)) {
			if (procreationCount < HAWK_MAX_PROCREATION_COUNT && procreate())
				procreationCount++;
			else if (!done2ndProcreation && Math.abs(Main.forest.getRandom().nextGaussian()) <= HAWK_2ND_PROCREATION_CHANCE && procreate()) {
				procreationCount++;
				done2ndProcreation = true;
			}
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
