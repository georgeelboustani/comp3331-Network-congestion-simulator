import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


public class VirtualCircuit {
	
	private LinkInfo[][] graph;
	
	public VirtualCircuit() {
		this.graph = new LinkInfo[26][26];
	}
	
	public static VirtualCircuit generateCircuit(String topologyFileName) {
		try {
			VirtualCircuit newGraph = new VirtualCircuit();

			BufferedReader topologyReader = new BufferedReader(new FileReader(topologyFileName));
			
			String line = null;
			while ((line = topologyReader.readLine()) != null) {
				String[] info = line.split("\\s+");
				int node = info[0].charAt(0)-'A';
				int destination = info[1].charAt(0)-'A';
				int delay = Integer.parseInt(info[2]);
				int size = Integer.parseInt(info[3]);
				newGraph.addLink(node,destination,new VirtualCircuit.LinkInfo(delay, size, 0));
			}

			return newGraph;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void addLink(int node, int destination, LinkInfo linkInfo) {
		this.graph[node][destination] = linkInfo;
	}
	
	public LinkInfo getLink(int node, int destination) {
		return this.graph[node][destination];
	}

	public static class LinkInfo {
		public final int delay;
		public final int size;
		public int connections;

		public LinkInfo (int delay, int size, int connections) {
			this.delay = delay;
			this.size = size;
			this.connections = connections;
		}	
	}
}
