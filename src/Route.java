/*
 * 			Source    		Port    	Destination
 * 
 * */
public class Route {
	
	private String source;
	private int port;
	private String destination;
	
	public Route(String source, int port,String destination) {
		// TODO Auto-generated constructor stub
		super();
		this.source = source;
		this.port = port;
		this.destination = destination;
	}

	public String getSource() {
		return source;
	}


	public int getPort() {
		return port;
	}

	public String getDestination() {
		return destination;
	}

}
