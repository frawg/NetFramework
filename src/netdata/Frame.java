package netdata;

import netdata.enums.EtherType;
import netdata.packet.IPv4Packet;
import netdata.packet.Packet;

public class Frame {
	private Packet payload;
	private String sourceMAC, destMAC;
	private EtherType type;
	
	public Packet getPayload() { return payload; }
//	public void setPayload(Packet payload) {
//		this.payload = payload;
//	}
	public String getSourceMAC() { return sourceMAC; }
	public String getDestMAC() { return destMAC; }
	public EtherType getEtherType() { return type; }
	
	public Frame(String source, String dest, EtherType type, Packet pay){
		this.sourceMAC = source;
		this.destMAC = dest;
		this.type = type;
		this.payload = pay;
	}
}
