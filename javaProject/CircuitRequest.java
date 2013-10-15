import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CircuitRequest {
	
	private double time;
	private int source;
	private int destination;
	private double active;
	
	public CircuitRequest(double time, int source, int destination,
			double active) {
		super();
		this.time = time;
		this.source = source;
		this.destination = destination;
		this.active = active;
	}

	public static List<CircuitRequest> generateCircuitRequests(String workload) throws NumberFormatException, IOException {
		List<CircuitRequest> circuitRequests = new ArrayList<CircuitRequest>();
		
		BufferedReader workloadReader = new BufferedReader(new FileReader(workload));
		
		String line = null;
		while ((line = workloadReader.readLine()) != null) {
			String[] info = line.split("\\s+");
			double time = Double.parseDouble(info[0]);
			int source = info[1].charAt(0)-'A';
			int destination = info[2].charAt(0)-'A';
			double active = Double.parseDouble(info[3]);
			
			circuitRequests.add(new CircuitRequest(time, source, destination, active));
		}
		
		return circuitRequests;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public double getActive() {
		return active;
	}

	public void setActive(double active) {
		this.active = active;
	}
	
}
