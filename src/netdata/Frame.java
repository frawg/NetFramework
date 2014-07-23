package netdata;

public class Frame {
	private Packet payload;
	private String sourceMAC, destMAC;
	
	public Packet getPayload() { return payload; }
//	public void setPayload(Packet payload) {
//		this.payload = payload;
//	}
	public String getSourceMAC() {
		return sourceMAC;
	}
	public String getDestMAC() {
		return destMAC;
	}
	public Frame(String source, String dest, Packet pay){
		
	}
}
