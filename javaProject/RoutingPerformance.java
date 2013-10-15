import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class RoutingPerformance {
	
	public static void main(String args[]) {
		String algorithm = args[0];
		String topologyFileName = args[1];
		String workloadFileName = args[2];
		
		VirtualCircuit graph = VirtualCircuit.generateCircuit(topologyFileName);
		System.out.println(graph.getLink(0,1).delay);
		System.out.println(graph.getLink(1,2).delay);
	}

}