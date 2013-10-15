import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class VirtualNetwork {
	
	private LinkInfo[][] graph;
	
	public VirtualNetwork() {
		this.graph = new LinkInfo[26][26];
	}
	
	public static VirtualNetwork generateNetwork(String topologyFileName) {
		try {
			VirtualNetwork newGraph = new VirtualNetwork();

			BufferedReader topologyReader = new BufferedReader(new FileReader(topologyFileName));
			
			String line = null;
			while ((line = topologyReader.readLine()) != null) {
				String[] info = line.split("\\s+");
				int node = info[0].charAt(0)-'A';
				int destination = info[1].charAt(0)-'A';
				int delay = Integer.parseInt(info[2]);
				int size = Integer.parseInt(info[3]);
				newGraph.addLink(node,destination,new VirtualNetwork.LinkInfo(delay, size, 0));
			}

			return newGraph;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void closeExpiredCircuits(double currentTime) {
		for (int source = 0; source < 26; source++) {
			for (int dest = 0; dest < 26; dest++) {
				LinkInfo link = graph[source][dest];
				if (link != null) {
					link.removeExpiredCircuits(currentTime);
				}
			}
		}
	}
	
	public boolean requestCircuit(CircuitRequest req) {
		boolean success = true;
		
		LinkInfo link = graph[req.getSource()][req.getDestination()];
		
		if (link != null && link.connections < link.size) {
			link.addCircuit(req.getTime() + req.getActive());
			
			link.connections++;
		} else {
			success = false;
		}
		
		return success;
	}
	
	public void addLink(int node, int destination, LinkInfo linkInfo) {
		// Add link in both directions, same reference
		this.graph[node][destination] = linkInfo;
		this.graph[destination][node] = linkInfo;
	}

	public LinkInfo getLink(int node, int destination) {
		return this.graph[node][destination];
	}
	
	public static class LinkInfo {
		public final int delay;
		public final int size;
		public int connections;
		public List<Double> expiryTimes;

		public LinkInfo (int delay, int size, int connections) {
			this.delay = delay;
			this.size = size;
			this.connections = connections;
			expiryTimes = new ArrayList<Double>();
		}
		
		public void addCircuit(double expiryTime) {
			expiryTimes.add(expiryTime);
		}
		
		public void removeExpiredCircuits(double currentTime) {
			for (int i = 0; i < expiryTimes.size(); i++) {
				if (expiryTimes.get(i) <= currentTime) {
					expiryTimes.remove(i);
					i--;
				}
			}
		}
		
	}
}
