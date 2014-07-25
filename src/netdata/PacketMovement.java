package netdata;

import netdata.packet.IPv4Packet;

public class PacketMovement {
	private int dev, port;
	private IPv4Packet pack;
	
	public PacketMovement(int dev, int port, IPv4Packet pack){
		this.dev = dev;
		this.port = port;
		this.pack = pack;
	}
	
	public int getDevID() {
		return dev;
	}
	public IPv4Packet getPack() {
		return pack;
	}
	public int getDevPortID(){
		return port;
	}
}
