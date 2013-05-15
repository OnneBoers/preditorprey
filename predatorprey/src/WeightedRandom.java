import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class WeightedRandom extends Random {

	ArrayList<Animal> weights;

	public WeightedRandom() {
		super();
		weights = new ArrayList<Animal>();
	}
	
	public WeightedRandom(long seed) {
		this();
		if(seed != -1)
			setSeed(seed);
	}
	
	public void addWeight(int weight, Animal type) {
		for (int i=0; i<weight; ++i)
			if (type == null) {
				weights.add(new DummyAnimal());
			} else {
				weights.add(type);
			}
	}
	
	public Animal nextAnimal() {
		Animal tmp = weights.get(nextInt(weights.size()));
		return (tmp instanceof DummyAnimal ? null : tmp);
	}
	
	/**
	 * Because we can not use null in the ArrayList we have to use this private class to fake it till we make it.
	 * @author XeryusTC
	 *
	 */
	private class DummyAnimal extends Prey {
		public DummyAnimal() {
			super();
		}
		public void turn() {
			
		}  
		
		public String printCharacter() {
			return "x"; 
		}
	}
}
