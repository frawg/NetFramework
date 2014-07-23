package netdata;

public class Packet {
	private String sourceIP, destIP, payload;
	private short TTL;
	
	public Packet(String source, String dest, String payload){
		this.sourceIP = source;
		this.destIP = dest;
		this.payload = payload;
		this.TTL = 64;
	}

	public String getSourceIP() { return sourceIP; }
	public String getDestIP() { return destIP; }
	public String getPayload() { return payload; }
	public void setPayload(String payload) { this.payload = payload; }
	
	public Boolean decTime(){
		if (TTL < 1)
			return false;
		else
		{
			this.TTL -= 1;
			return true;
		}
	}
}
