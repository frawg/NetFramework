package netdata;

public class Payload {
	private int sourceAppPort, destAppPort = 0;
	private String data = null;
	
	public Payload(int source, int dest, String data)
	{
		this.data = data;
		sourceAppPort = source;
		destAppPort = dest;
	}
	
	public String getData(){ return data; }
	public int getSourcePort(){ return sourceAppPort; }
	public int getDestPort(){ return destAppPort; }
}
