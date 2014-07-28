package netdata.packet;

import netdata.Payload;

public class ICMPpacket extends IPv4Packet {
	public enum Type {
		ECHO_REPLY(0),
		DESTINATION_UNREACHABLE(3),
		SOURCE_QUENCH(4),
		REDIRECT(5),
		ALT_HOST_ADDR(6),
		ECHO(8),
		ROUTER_ADVERT(9),
		ROUTER_SELECTION(10),
		TIME_EXCEEDED(11),
		PARAMETER_PROBLEM(12),
		TIMESTAMP(13),
		TIMESTAMP_REPLY(14),
		INFORMATION_REQ(15),
		INFORMATION_REPLY(16),
		ADDR_MASK_REQUEST(17),
		ADDR_MASK_REPLY(18),
		TRACE_ROUTE(30),
		DATAGRAM_CONVERSION_ERR(31),
		MOBILE_HOST_REDIRECT(32),
		IPv6_WHERE(33),
		IPv6_HERE(34),
		MOBILE_REGISTRATION_REQ(35),
		MOBILE_REGISTRATION_REPLY(36),
		DOMAIN_NAME_REQ(37),
		DOMAIN_NAME_REPLY(38),
		SKIP(39),
		PHOTURIS(40);
		
		private short value;
		private Type(int value){ this.value = (short)value; }
		public short getValue() { return value; }
	}
	public enum TIME_EXCEEDED {
		TTL_TRANSIT,
		FRAGMENT_REASSEMBLY
	}
	public enum REDIRECT {
		NETWORK,
		HOST,
		TOS_NETWORK,
		TOS_HOST
	}
	public enum PARAMETER_PROBLEM {
		POINTER_ERROR,
		MISSING_OPTION,
		BAD_LENGTH
	}
	public enum DESTINATION_UNREACHABLE {
		NET_UNREACHABLE,
		HOST_UNREACHABLE,
		PROTOCOL_UNREACHABLE,
		PORT_UNREACHABLE,
		FRAGMENTATION_NEEDED,
		SOURCE_ROUTE_FAIL,
		DESTINATION_NETWORK_UNKNOWN,
		DESTINATION_HOST_ISOLATED,
		SOURCE_HOST_ISOLATED,
		COMMUNICATION_DESTINATION_NETWORK_PROHIBITED,
		COMMUNICATION_DESTINATION_HOST_PROHIBITED,
		DESTINATION_NETWORK_UNREACHABLE,
		DESTINATION_HOST_UNREACHABLE,
		COMMUNICATION_PROHIBITED,
		HOST_PRECEDENCE_VIOLATION,
		PRECEDENCE_CUTOFF
	}

	private short ICMPcode;
	private Type ICMPtype = null;
	
	public ICMPpacket(String source, String dest, Type t, int code) {
		super(source, dest,ICMP, null);
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
