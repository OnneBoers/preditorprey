/** Implementation of the free to choose prey animal.
 * 
 * Mice will flee from the first predator they detect from which they can move in the opposite direction. If this is not possible they will not move.
 * Mice will reproduce if they have not fled from predators and they are next to another mouse. They only have a chance to reproduce equal to 
 * MOUSE_PROCREATE_CHANCE and they will only have MOUSE_PROCREATE_LIMIT offspring.
 * 
 * @author Xeryus Stokkel and Onne Boers
 *
 */
public class Mouse extends Prey implements MovableAnimal {
	private final int MOUSE_ENERGY = 5;
	private final int MOUSE_ENERGY_DIST = 10;
	private final double MOUSE_PROCREATE_CHANCE = 0.35;
	private final int MOUSE_PROCREATE_LIMIT = 5;
	
	private int procreateCount = 0; 
	private boolean hasFlown = false;
	
	public Mouse() {
		super();
		setEnergy(MOUSE_ENERGY + Main.forest.getRandom().nextInt(MOUSE_ENERGY_DIST));
	}
	
	public Mouse(int x, int y) {
		super(x, y);
		setEnergy(MOUSE_ENERGY + Main.forest.getRandom().nextInt(MOUSE_ENERGY_DIST));
	}
	
	public boolean getHasFlown() {
		return hasFlown;
	}

	public void turn() {
		Animal[] surroundingAnimals = Main.forest.getSurroundingAnimals(getX(), getY());
		boolean lonely = true;
		hasFlown = false;
		
		// Check if the animal needs to flee
		for (int i=0; i<surroundingAnimals.length; i++) {
			try {
				if (surroundingAnimals[i] instanceof Predator && !hasFlown) {
					int[] pos = Main.forest.surroundingAnimalIndexToGridLocation(i, getX(), getY());
					Main.forest.moveAnimal(getX(), getY(), getX()-(pos[0]-getX()), getY()-(pos[1]-getY()));
					hasFlown = true;
				} else if (surroundingAnimals[i] instanceof Mouse) {
					lonely = false;
				}
			} catch (UnsupportedOperationException ex) {
				System.out.println("Exception: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
		
		if (!lonely && !hasFlown) {
			if (procreateCount < MOUSE_PROCREATE_LIMIT && Math.abs(Main.forest.getRandom().nextGaussian()) <= MOUSE_PROCREATE_CHANCE) {
				if (procreate()) {
					procreateCount++;
				}
			}
		}
		
		
		
		lowerEnergy();
	}
	
	public void move(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public String printCharacter() {
		return "M";
	}
}
