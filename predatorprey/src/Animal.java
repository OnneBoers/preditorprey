/**
 * Basic class that implements everything an animal should at least be able to do.
 * 
 * @author Xeryus Stokkel 
 */
public abstract class Animal {
	private int energy;
	protected int x, y; // To keep track of the animal's location internally
	Boolean dead;
		
	public Animal() {
		energy = 100;
		dead = false;
	}
	
	public Animal(int x, int y) {
		this();
		this.x = x;
		this.y = y;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public void setEnergy(int energy) {
		this.energy = energy; 
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public Boolean isDead() {
		return dead;
	}
	
	abstract public void turn();
	
	public void die() {
		Main.forest.removeAnimal(x, y);
		dead = true;
	}
	
	public void procreate() {
		// TODO implement
	}
	
	public abstract String printCharacter();
}
