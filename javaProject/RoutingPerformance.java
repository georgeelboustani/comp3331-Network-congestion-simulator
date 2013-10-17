import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class RoutingPerformance {
	
	public static void main(String args[]) {
		String algorithm = args[0];
		String topologyFileName = args[1];
		String workloadFileName = args[2];
		
		VirtualNetwork vnet = VirtualNetwork.generateNetwork(topologyFileName);
		
		int numFailedRequests = 0;
		int totalRequests = 0;
		
		try {
			
			for (CircuitRequest req: CircuitRequest.generateCircuitRequests(workloadFileName)) {
				double currentTime = req.getTime();
				
				vnet.closeExpiredCircuits(currentTime);
				
				boolean requestSuccess = vnet.requestCircuit(req,algorithm);
				if (!requestSuccess) {
					numFailedRequests++;
				}
				
				totalRequests++;
			}
			
			double averageHops = 0;
			double averageCumulativeDelay = 0;
			
			System.out.println("total number of virtual circuit requests: " + totalRequests);
			System.out.println("number of succesfully routed requests: " + (totalRequests - numFailedRequests));
			System.out.println("percentage of succesfully routed request: " + (((double)(totalRequests - numFailedRequests)/totalRequests)*100));
			System.out.println("number of blocked requests: " + numFailedRequests);
			System.out.println("percentage of blocked requests: " + (((double)numFailedRequests/totalRequests)*100));
			System.out.println("average number of hops per circuit: " + averageHops);
			System.out.println("average cumulative propagation delay per circuit: " + averageCumulativeDelay);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}