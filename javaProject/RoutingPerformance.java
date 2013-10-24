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
			double averageHops = 0;
			double averageCumulativeDelay = 0;
			
			for (CircuitRequest req: CircuitRequest.generateCircuitRequests(workloadFileName)) {
				double currentTime = req.getTime();
				
				vnet.closeExpiredCircuits(currentTime);
				
				VirtualNetwork.RequestFeedback res = vnet.requestCircuit(req,algorithm);
				if (!res.isSuccess()) {
					numFailedRequests++;
				} else {
					averageHops += res.getHops();
					averageCumulativeDelay += res.getCumulativeDelay();
				}
				
				totalRequests++;
			}
			
			averageHops /= totalRequests-numFailedRequests;
			averageCumulativeDelay /= totalRequests-numFailedRequests;
			
			System.out.printf("total number of virtual circuit requests: %d\n", totalRequests);
			System.out.printf("number of succesfully routed requests: %d\n", (totalRequests - numFailedRequests));
			System.out.printf("percentage of succesfully routed request: %.2f\n", (((double)(totalRequests - numFailedRequests)/totalRequests)*100));
			System.out.printf("number of blocked requests: %d\n", numFailedRequests);
			System.out.printf("percentage of blocked requests: %.2f\n", (((double)numFailedRequests/totalRequests)*100));
			System.out.printf("average number of hops per circuit: %.2f\n", averageHops);
			System.out.printf("average cumulative propagation delay per circuit: %.2f\n", averageCumulativeDelay);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}