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
			
			System.out.println("Total number of requests: " + totalRequests);
			System.out.println("Number of failed requests: " + numFailedRequests);
			System.out.println("% Failures: " + ((double)numFailedRequests/totalRequests)*100 + "%");
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}