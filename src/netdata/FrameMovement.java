package netdata;

public class FrameMovement {
	private short port;
	private Frame f = null;
	
	public FrameMovement(short port, Frame pack){
		this.port = port;
		this.f = pack;
	}
	
	public Frame getFrame() {
		return f;
	}
	public short getPortIndex(){
		return port;
	}
}
