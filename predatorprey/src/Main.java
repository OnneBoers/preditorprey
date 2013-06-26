
public class Main {
	public static Forest forest;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO read settings from file
		
		Main.forest = new Forest(200, 200, -1);
		Main.forest.generateInitialForest();
		//Main.forest.printMap();
		Main.forest.startSimulation(10000);
		//Main.forest.printMap();
	}

}
