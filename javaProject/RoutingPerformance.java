import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class RoutingPerformance {
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
	
	public static void main(String args[]) {
		String algorithm = args[0];
		String topologyFileName = args[1];
		String workloadFileName = args[2];
		
		LinkInfo[][] graph = new LinkInfo[26][26];
		readTopology(topologyFileName, graph);
		System.out.println(graph[0][1].delay);
		System.out.println(graph[1][2].delay);
	}

	public static void readTopology(String topologyFileName,LinkInfo[][] graph) {
		
		try {
			BufferedReader topologyReader = new BufferedReader(new FileReader(
					topologyFileName));
			String line = null;
			while ((line = topologyReader.readLine()) != null) {
				String[] info = line.split("\\s+");
				int node = info[0].charAt(0)-'A';
				int destination = info[1].charAt(0)-'A';
				int delay = Integer.parseInt(info[2]);
				int size = Integer.parseInt(info[3]);
				graph[node][destination] = new LinkInfo(delay, size, 0);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}