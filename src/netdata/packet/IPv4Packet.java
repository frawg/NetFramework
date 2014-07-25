package netdata.packet;

import netdata.Payload;
import netdata.enums.ProtocolNumbers;

public class IPv4Packet extends Packet {
	private ProtocolNumbers protocol = null;
	
	public IPv4Packet(String source, String dest, ProtocolNumbers protocol, Payload payload){
		super(source, dest, (short)255, payload);
		this.protocol = protocol;
	}
	
	public ProtocolNumbers getProtocol() { return protocol; }
}
