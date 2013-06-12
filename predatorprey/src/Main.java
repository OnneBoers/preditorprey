
public class Main {
	public static Forest forest;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO read settings from file
		
		Main.forest = new Forest(100, 200, -1);
		Main.forest.generateInitialForest();
		//Main.forest.printMap();
		Main.forest.startSimulation(1000);
		//Main.forest.printMap();
	}

}
