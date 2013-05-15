/**
 * Keeps track of where all the animals are and runs the simulation.
 * 
 * @author Xeryus Stokkel
 *
 */
public class Forest {
	/**
	 * Keep track of all the animals in this grid. 
	 */
	private Animal[][] grid;
	/**
	 * Keeps track of what the grid should look like on the next iteration of the simulation. Is 
	 * used so we can simulate every animal executing its move at the same time.
	 */
	private Animal[][] newGrid;
	private int gridWidth, gridHeight;
	private WeightedRandom random;
	
	/**
	 * @param width The width of the forest in squares. One square per animal.
	 * @param height The height of the forest in squares. One square per animal.
	 */
	public Forest(int width, int height, long seed) {
		gridWidth = width;
		gridHeight = height;
		grid = new Animal[width][height];
		random = new WeightedRandom(seed);
	}
	
	public Forest() {
		this(20, 20, 0);
	}
	
	public Animal[][] getGrid() {
		return grid;
	}
	
	/**
	 * Retrieves the animal at a specific position in the grid
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @return The animal at the position (x, y)
	 */
	public Animal getAnimalAtPosition(int x, int y) {
		return grid[getPositionX(x)][getPositionY(y)];
	}
	
	/**
	 * Gets an array of the animals surrounding a cell at position (X, Y). Gets 8 surrounding animals.
	 * 
	 * An array of 8 elements is returned, the array is one dimensional so the different IDs are mapped like this:<br />
	 * <code>0 1 2<br />
	 * 3 A 4<br />
	 * 5 6 7</code><br />
	 * A is the (x, y) coordinate as specified in the parameters.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @return An one-dimensional array of the surrounding Animals
	 */
	public Animal[] getSurroundingAnimals(int x, int y) {
		Animal[] ret = new Animal[8];
		ret[0] = getAnimalAtPosition(x-1, y-1);
		ret[1] = getAnimalAtPosition(x, y-1);
		ret[2] = getAnimalAtPosition(x+1, y-1);
		ret[3] = getAnimalAtPosition(x-1, y);
		ret[4] = getAnimalAtPosition(x+1, y);
		ret[5] = getAnimalAtPosition(x-1, y+1);
		ret[6] = getAnimalAtPosition(x, y+1);
		ret[7] = getAnimalAtPosition(x+1, y+1);
		return ret;
	}
	
	public int getGridWidth() {
		return gridWidth;
	}
	
	public int getGridHeight() {
		return gridHeight;
	}
	
	/**
	 * Helper function which calculates the correct X location based on the input
	 * @param x The raw X input
	 * @return An X coordinate that is a valid location to use with the grid array
	 */
	private int getPositionX(int x) {
		return (gridWidth + (x % gridWidth)) % gridWidth;
	}
	
	/**
	 * Helper function which calculates the correct Y location based on the given Y location
	 * @param y The raw Y input
	 * @return A Y coordinate that is a valid location to use with the grid array
	 */
	private int getPositionY(int y) {
		return (gridHeight + (y % gridHeight)) % gridHeight;
	}
	
	/**
	 * Moves an animal to a new location, but only if it is allowed to move.
	 * @param oldX Old X coordinate
	 * @param oldY Old Y coordinate
	 * @param newX New X coordinate
	 * @param newY New Y coordinate
	 * @throws UnsupportedOperationException The animal at this location can not move
	 */
	public void moveAnimal(int oldX, int oldY, int newX, int newY) throws UnsupportedOperationException {
		oldX = getPositionX(oldX);
		oldY = getPositionY(oldY);
		newX = getPositionX(newX);
		newY = getPositionY(newY);
		
		if (grid[oldX][oldY] instanceof MovableAnimal) {
			newGrid[newX][newY] = grid[oldX][oldY];
			newGrid[oldX][oldY] = null;
			// TODO cast to appropriate animal type and call the move function
			//grid[newX][newY].move(newX, newY);
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	public void removeAnimal(int x, int y) {
		newGrid[getPositionX(x)][getPositionY(y)] = null;
	}
	
	public void setAnimal(Animal animal) throws IllegalArgumentException {
		if (!(animal instanceof Animal))
			throw new IllegalArgumentException("Argument is of type" + animal.getClass().getName());
		newGrid[getPositionX(animal.getX())][getPositionY(animal.getY())] = animal;
	}
	
	public void generateInitialForest() {
		int x, y;
		Animal tmp;
		
		setAnimalWeights();
		
		for (y=0; y<getGridHeight(); ++y) {
			for (x=0; x<getGridWidth(); ++x) {
				tmp = random.nextAnimal();
				
				if (tmp instanceof Rabbit) {
					grid[x][y] = new Rabbit(x, y);
				}
			}
		}
	}
	
	private void setAnimalWeights() {
		random.addWeight(5, null);
		random.addWeight(1, new Rabbit());
	}
	
	public void startSimulation(int maxIterations) {
		System.out.println("--- STARTING SIMULATION ---");
		for (int i=1; i<=maxIterations; ++i) {
			System.out.println("---ITERATION " + i + "---");
			
			newGrid = new Animal[gridWidth][gridHeight];
			
			// First do all the stuff that does not involve moving
			for (Animal[] y : grid) {
				for (Animal x : y) {
					try {
						x.turn();
						if (!x.isDead()) {
							setAnimal(x);
						}
					} catch (IllegalArgumentException e) {
						System.out.println("Fatal error occured, an animal is not an animal!");
					} catch (NullPointerException e) {
						// There is no animal in this spot (x.turn() fails), just do nothing but stop the crash
					}
				}
			}
			
			grid = newGrid; // Finally move update the grid
			printMap();
		}
	}
	
	@Override
	public String toString() {
		String ret = new String("");
		for(Animal[] y : grid) {
			for(Animal x : y) {
				if (x == null)
					System.out.print("_");
				else
					System.out.print(x);
			}
			System.out.print("\n");
		}
		return ret;
	}
	
	public void printMap() {
		for(Animal[] y : grid) {
			for(Animal x : y) {
				if (x == null)
					System.out.print("_");
				else
					System.out.print(x.printCharacter());
			}
			System.out.print("\n");
		}
	}
}
