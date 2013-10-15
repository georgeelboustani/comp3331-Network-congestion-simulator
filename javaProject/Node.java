import java.util.ArrayList;


public class Node {
	private Node[] neighbours = new Node[26];
	private ArrayList<Integer> delayBetweenNeighbour;
	private ArrayList<Integer> maxNumberCircuitsBetweenNeighbour;
	
	public void addNeighbour(Node neighbour, int delayToNeighbour, int maxNumberCircuits) {
		if (neighbour != null) {
			neighbours.add(neighbour);
			delayBetweenNeighbour.add(delayToNeighbour);
			maxNumberCircuitsBetweenNeighbour.add(maxNumberCircuits);
		}
	}
	
}