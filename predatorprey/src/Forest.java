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
	private int rabbitCount=0;
	private int foxCount=0;
	private int mouseCount=0;
	private int hawkCount=0;
	
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
	
	/**
	 * Converts an index suitable for the array returned by {@link #getSurroundingAnimals(int, int)} back to the x, y location on the map
	 * @param idx The index
	 * @param x The X of the centre
	 * @param y The Y of the centre
	 * @return The x, y location in an array. Index 0 is the x coordinate, 1 is the y coordinate
	 * @throws IllegalArgumentException when the index is out of bounds
	 */
	public int[] surroundingAnimalIndexToGridLocation(int idx, int x, int y) throws IllegalArgumentException {
		int[] ret = {getPositionX(x), getPositionY(y)};
		
		if (idx < 0 || idx > 7) {
			throw new IllegalArgumentException("Index out of range");
		}
		
		if (idx == 0 || idx == 3 || idx == 5) {
			ret[0] = getPositionX(ret[0]-1);
		} else if (idx == 2 || idx == 4 || idx == 7) {
			ret[0] = getPositionX(ret[0]+1);
		}
		
		if(idx <= 2) {
			ret[1] = getPositionY(ret[1]-1);
		} else if (idx >= 5) {
			ret[1] = getPositionY(ret[1]+1);
		}
		
		return ret;
	}
	
	public int getGridWidth() {
		return gridWidth;
	}
	
	public int getGridHeight() {
		return gridHeight;
	}
	
	public WeightedRandom getRandom() {
		return random;
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
	public boolean moveAnimal(int oldX, int oldY, int newX, int newY) throws UnsupportedOperationException {
		oldX = getPositionX(oldX);
		oldY = getPositionY(oldY);
		newX = getPositionX(newX);
		newY = getPositionY(newY);
		
		if (grid[oldX][oldY] instanceof MovableAnimal) {
			if (newGrid[newX][newY] == null && grid[newX][newY] == null) { // Check whether the new space is not occupied
				newGrid[newX][newY] = getAnimalAtPosition(oldX, oldY);
				newGrid[oldX][oldY] = null;

				if (newGrid[newX][newY] instanceof Mouse) {
					((Mouse)newGrid[newX][newY]).move(newX, newY);
				} else if (newGrid[newX][newY] instanceof Hawk) {
					((Hawk)newGrid[newX][newY]).move(newX,  newY);
				}
				
				return true;
			}
		} else {
			throw new UnsupportedOperationException();
		}
		return false;
	}
	
	public void removeAnimal(int x, int y) {
		newGrid[getPositionX(x)][getPositionY(y)] = null;
	}
	
	public void setAnimal(Animal animal) throws IllegalArgumentException {
		if (!(animal instanceof Animal))
			throw new IllegalArgumentException("Argument is of type" + animal.getClass().getName());
		newGrid[getPositionX(animal.getX())][getPositionY(animal.getY())] = animal;
	}
	
	/**
	 * Populates the forest with animals in random possitions.
	 */
	public void generateInitialForest() {
		int x, y;
		Animal tmp;
		
		setAnimalWeights();
		
		for (y=0; y<getGridHeight(); ++y) {
			for (x=0; x<getGridWidth(); ++x) {
				tmp = random.nextAnimal();
				
				if (tmp instanceof Rabbit) {
					grid[x][y] = new Rabbit(x, y);
				} else if (tmp instanceof Fox) {
					grid[x][y] = new Fox(x, y);
				} else if (tmp instanceof Mouse) {
					grid[x][y] = new Mouse(x, y);
				} else if (tmp instanceof Hawk) {
					grid[x][y] = new Hawk(x, y);
				}
			}
		}
	}
	
	private void setAnimalWeights() {
		random.addWeight(20, null);
		random.addWeight(4, new Rabbit());
		random.addWeight(2, new Fox());
		random.addWeight(6, new Mouse());
		random.addWeight(1, new Hawk());
	}
	
	public void startSimulation(int maxIterations) {
		System.out.println("--- STARTING SIMULATION ---");
		for (int i=1; i<=maxIterations; ++i) {
			System.out.println("---ITERATION " + i + "---");
			reportPopulationChange();
			
			newGrid = new Animal[gridWidth][gridHeight];
			
			// First do all the stuff that does not involve moving
			for (Animal[] y : grid) {
				for (Animal x : y) {
					try {
						// If there is no animal here or it is already dead (it has been eaten) then we can skip this location
						if (x == null || x.isDead()) {
							continue;
						}
						x.turn();
						if (!x.isDead()) {
							setAnimal(x);
						}
					} catch (IllegalArgumentException e) {
						System.out.println("Fatal error occured, an Animal is not an Animal!");
						e.printStackTrace();
					} catch (NullPointerException e) {
						// There is no animal in this spot (x.turn() fails), just do nothing but make sure we don't crash
						System.out.println("Tried to run logic on an Animal that does not exist!");
						e.printStackTrace();
					}
				}
			}
			
			grid = newGrid; // Finally move update the grid
			//printMap();
		}
		
		System.out.println("-- FINISHED SIMULATION --");
	}
	
	/**
	 * Creates offspring for animals. It will add the offspring to the forest next to the parent animal in the first available empty field.
	 * @param type The type of Animal @see Animal
	 * @param x The x location of the parent
	 * @param y The y location of the parent
	 * @return True if there was a space available and offspring has been created.
	 */
	public boolean procreateAnimal(Animal type, int x, int y) {
		x = getPositionX(x);
		y = getPositionY(y);
		Animal[] AnimalList = getSurroundingAnimals(x, y);
		boolean procreated = false;
		int[] newAnimalLoc;
		
		for (int i=0; i<AnimalList.length; ++i) {
			if (AnimalList[i] == null) { // Spot is empty
				try {
					newAnimalLoc = surroundingAnimalIndexToGridLocation(i, x, y);
				} catch (IllegalArgumentException e) {
					System.out.println("Failed to procreate: " + e.getMessage());
					return false;
				}
				
				if (type instanceof Rabbit) {
					newGrid[newAnimalLoc[0]][newAnimalLoc[1]] = new Rabbit(newAnimalLoc[0], newAnimalLoc[1]);
					procreated = true;
					break;
				} else if (type instanceof Fox) {
					newGrid[newAnimalLoc[0]][newAnimalLoc[1]] = new Fox(newAnimalLoc[0], newAnimalLoc[1]);
					procreated = true;
					break;
				} else if (type instanceof Mouse) {
					newGrid[newAnimalLoc[0]][newAnimalLoc[1]] = new Mouse(newAnimalLoc[0], newAnimalLoc[1]);
					procreated = true;
					break;
				} else if (type instanceof Hawk) {
					newGrid[newAnimalLoc[0]][newAnimalLoc[1]] = new Hawk(newAnimalLoc[0], newAnimalLoc[1]);
					procreated = true;
					break;
				}
			}
		}
		
		return procreated;
	}
	
	public Animal eatAnimal(int eaterX, int eaterY, int victimX, int victimY) {
		Animal eater = getAnimalAtPosition(eaterX, eaterY);
		Animal victim = getAnimalAtPosition(victimX, victimY);
		
		if (eater instanceof Predator) {
			victim.die();
		}
		
		return victim;
	}
	
	/**
	 * Reports the size and change in population directly to console.
	 */
	public void reportPopulationChange() {
		int newRabbitCount = 0, newFoxCount = 0, newMouseCount = 0, newHawkCount = 0;
		
		for (Animal[] y : grid) {
			for (Animal x : y) {
				if (x instanceof Rabbit)
					newRabbitCount++;
				else if (x instanceof Fox)
					newFoxCount++;
				else if (x instanceof Mouse)
					newMouseCount++;
				else if (x instanceof Hawk)
					newHawkCount++;
			}
		}
		
		System.out.println("\tTotal\tChange");
		System.out.format("Rabbits\t%d\t%d%n", newRabbitCount, newRabbitCount-rabbitCount);
		System.out.format("Foxes\t%d\t%d%n", newFoxCount, newFoxCount-foxCount);
		System.out.format("Mice\t%d\t%d%n", newMouseCount, newMouseCount-mouseCount);
		System.out.format("Hawks\t%d\t%d%n", newHawkCount, newHawkCount-hawkCount);
		System.out.println();
		
		rabbitCount = newRabbitCount;
		foxCount = newFoxCount;
		mouseCount = newMouseCount;
		hawkCount = newHawkCount;
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
