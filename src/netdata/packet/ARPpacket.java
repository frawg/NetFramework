package netdata.packet;

import netdata.Payload;

public class ARPpacket extends Packet{
	public enum OPERATION{
		REQUEST,
		REPLY
	};
	private int type;
	private OPERATION arpOp;
	private String sourceMAC, destMAC = null;
	
	public ARPpacket(String sourceip, String destip, String sourceMAC, String destMAC, int type, OPERATION op, short TTL) {
		super(sourceip, destip, TTL, null);
		
		this.sourceMAC = sourceMAC;
		this.destMAC = destMAC;
		this.type = type;
		this.arpOp = op;
		// TODO Auto-generated constructor stub
	}
	
	public int getProtocolType() { return type; }
	public OPERATION getOperation() { return arpOp; }
	public String getSourceMAC() { return sourceMAC; }
	public String getDestinationMAC() { return destMAC; }
	
}
