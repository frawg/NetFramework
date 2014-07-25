package netdata.packet;

import netdata.Payload;

public class Packet {
	protected String sourceIP, destIP = null;
	protected Payload payload = null;
	protected short TTL;
	
	public Packet(String source, String dest, short TTL, Payload payload){
		this.sourceIP = source;
		this.destIP = dest;
		this.payload = payload;
		this.TTL = TTL;
	}

	public String getSourceIP() { return sourceIP; }
	public String getDestIP() { return destIP; }
	public Payload getPayload() { return payload; }
	
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
