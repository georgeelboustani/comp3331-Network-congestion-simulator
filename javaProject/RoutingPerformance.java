import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class RoutingPerformance {
	
	public static void main(String args[]) {
		String algorithm = args[0];
		String topologyFileName = args[1];
		String workloadFileName = args[2];
		
		VirtualNetwork vnet = VirtualNetwork.generateNetwork(topologyFileName);
		
		System.out.println(vnet.getLink(0,1).delay);
		System.out.println(vnet.getLink(1,2).delay);
		
		try {
			
			long startTime = System.currentTimeMillis();
			
			for (CircuitRequest req: CircuitRequest.generateCircuitRequests(workloadFileName)) {
				long currentTime = System.currentTimeMillis() - startTime;
				
				while (currentTime < req.getActive()) {
					// Close expired circuits
					vnet.closeExpiredCircuits(currentTime);
				}
				
				boolean requestSuccess = vnet.requestCircuit(req);
				
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}