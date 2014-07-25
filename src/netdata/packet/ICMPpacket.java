package netdata.packet;

import netdata.Payload;
import netdata.enums.ProtocolNumbers;
import netdata.enums.ICMP.Type;

public class ICMPpacket extends IPv4Packet {
	private short ICMPcode;
	private Type ICMPtype = null;
	
	public ICMPpacket(String source, String dest, Type t, int code) {
		super(source, dest, ProtocolNumbers.ICMP, null);
		// TODO Auto-generated constructor stub
		this.ICMPtype = t;
		this.ICMPcode = (short) code;
	}
	
	public String TypeToString() { return ICMPtype.toString(); }
	public short getTypeValue() { return ICMPtype.getValue(); }
	public Type getType() { return ICMPtype; }
	
	public short getCode() { return ICMPcode; }
	public String getData() { return payload.getData(); }
}
