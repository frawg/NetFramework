package netdata.packet;

import netdata.Payload;

public class IPv4Packet extends Packet {
	private int protocol;
	
	public IPv4Packet(String source, String dest, int protocol, Payload payload){
		super(source, dest, (short)255, payload);
		this.protocol = protocol;
	}
	
	public int getProtocol() { return protocol; }
}
