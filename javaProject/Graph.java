import java.util.ArrayList;
import java.util.HashMap;


public class Graph {
	private ArrayList<Node> nodes;
	private HashMap<Character, Node> charToNode;
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public Node getNodeAtLetter(char letter) {
		return charToNode.get(letter);
	}
}
