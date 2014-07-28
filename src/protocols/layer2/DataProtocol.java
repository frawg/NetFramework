package protocols.layer2;

public abstract class DataProtocol {
	private int protocolCode = 0;
	
	public DataProtocol(int code){
		protocolCode = code;
	}
	
	public int getProtocolCode(){ return protocolCode; }
	
	public abstract void doAction();
}
