package netdata.packet;

import netdata.Payload;
import netdata.enums.EtherType;

public class ARPpacket extends Packet{
	public enum OPERATION{
		REQUEST,
		REPLY
	};
	private EtherType type = null;
	private OPERATION arpOp;
	private String sourceMAC, destMAC = null;
	
	public ARPpacket(String sourceip, String destip, String sourceMAC, String destMAC, OPERATION op, short TTL) {
		super(sourceip, destip, TTL, null);
		this.arpOp = op;
		// TODO Auto-generated constructor stub
	}
	
	public EtherType getProtocolType() { return type; }
	public OPERATION getOperation() { return arpOp; }
}
