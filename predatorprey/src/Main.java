
public class Main {
	public static Forest forest;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO read settings from file
		
		Main.forest = new Forest(10, 10);
		Main.forest.startSimulation(50);
	}

}
