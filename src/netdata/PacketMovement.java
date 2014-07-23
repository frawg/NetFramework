package netdata;

public class PacketMovement {
	private int dev, port;
	private Packet pack;
	
	public PacketMovement(int dev, int port, Packet pack){
		this.dev = dev;
		this.port = port;
		this.pack = pack;
	}
	
	public int getDevID() {
		return dev;
	}
	public Packet getPack() {
		return pack;
	}
	public int getDevPortID(){
		return port;
	}
}
