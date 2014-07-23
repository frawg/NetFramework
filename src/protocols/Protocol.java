package protocols;

public abstract class Protocol {
	private int protocolCode = 0;
	
	public int getProtocolCode(){ return protocolCode; }
	
	public abstract void doAction();
}
