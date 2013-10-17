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
	
	public boolean requestCircuit(CircuitRequest req, String algorithm) {
		boolean success = true;
		
		// TODO - call dijkstra here and figure out path
		List<Link> path = dijkstraSearch(req.getSource(),req.getDestination(),algorithm);
		
		if (path.size() == 0) {
			success = false;
		} else {
			
			for (Link link: path) {
				LinkInfo linkInfo = graph[link.source][link.destination];
				
				if (linkInfo != null && linkInfo.connections < linkInfo.size) {
					
				} else {
					success = false;
					break;
				}
			}
			
			if (success) {
				for (Link link: path) {
					LinkInfo linkInfo = graph[link.source][link.destination];
					linkInfo.addCircuit(req.getTime() + req.getActive());
				}
			}
		}
		
		return success;
	}
	
	
	public List<Link> dijkstraSearch(int source,int dest,String algorithm) {
		
		double[] dist = new double[26];
		int[] prev = new int[26];
		
		for (int i = 0; i < 26; i++) {
			dist[i] = Integer.MAX_VALUE;
			prev[i] = -1;
		}
		
		dist[source] = 0;
		
		List<Integer> Q = getNodes();
		while (Q.size() > 0) {
			int nodeU = getShortestDistanceInQ(Q,dist);
			Q.remove(new Integer(nodeU));
			
			if (dist[nodeU] == Integer.MAX_VALUE) {
				break;
			}
			
			for (int v = 0; v < graph[nodeU].length; v++) {
				if (graph[nodeU][v] != null) {
					double alt;
					
					if ("LLP".equals(algorithm)) {
						if (dist[nodeU] < costBetween(nodeU,v,algorithm)) {
							alt = costBetween(nodeU,v,algorithm);
						} else {
							alt = dist[nodeU];
						}
					} else {
						alt = dist[nodeU] + costBetween(nodeU,v,algorithm);
					}

					if (alt < dist[v]) {
						dist[v] = alt;
						prev[v] = nodeU;
						
						Q.remove(new Integer(nodeU));
						Q.add(nodeU);
					}
				}
			}
			
		}
		
		List<Link> path = new ArrayList<Link>();
		
		int next = dest;
		if (prev[next] != -1) {
			int current = prev[dest];
			path.add(0, new Link(current,next));
			while (prev[current] != -1) {
				next = current;
				current = prev[current];
				path.add(0, new Link(current,next));
			}
		}
		
		return path;
	}
	
	private double costBetween(int nodeU, int v, String algorithm) {
		// algorithm = SHP SDP LLP
		double cost = 0;
		
		if ("SHP".equals(algorithm)) {
			cost = 1;
		} else if ("SDP".equals(algorithm)) {
			cost = graph[nodeU][v].delay;
		} else if ("LLP".equals(algorithm)) {
			cost = graph[nodeU][v].connections/graph[nodeU][v].size;
		}
		
		return cost;
	}

	private List<Integer> getNodes() {
		List<Integer> nodes = new ArrayList<Integer>();
		
		for (int i = 0; i < graph.length; i++) {
			if (isConnected(i)) {
				nodes.add(i);
			}
		}
		
		return nodes;
	}
	
	private boolean isConnected(int node) {
		boolean isConnected = false;
		for (LinkInfo link: graph[node]) {
			if (link != null) {
				isConnected = true;
				break;
			}
		}
		return isConnected;
	}
	
	private int getShortestDistanceInQ(List<Integer> Q, double[] dist) {
		int closestNode = -1;
		double shortestDistance = Integer.MAX_VALUE;
		
		for (Integer node: Q) {
			if (dist[node] < shortestDistance) {
				closestNode = node;
				shortestDistance = dist[node];
			}
		}
		return closestNode;
	}

	public void addLink(int node, int destination, LinkInfo linkInfo) {
		// Add link in both directions, same reference
		this.graph[node][destination] = linkInfo;
		this.graph[destination][node] = linkInfo;
	}

	public LinkInfo getLinkInfo(int node, int destination) {
		return this.graph[node][destination];
	}
	
	public static class Link {
		public final int source;
		public final int destination;
		
		public Link(int source,int destination) {
			this.source = source;
			this.destination = destination;
		}
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
			connections++;
		}
		
		public void removeExpiredCircuits(double currentTime) {
			for (int i = 0; i < expiryTimes.size(); i++) {
				if (expiryTimes.get(i) <= currentTime) {
					expiryTimes.remove(i);
					i--;
					connections--;
				}
			}
		}
		
	}
}
