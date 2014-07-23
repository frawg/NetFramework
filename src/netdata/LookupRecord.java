package netdata;

public class LookupRecord {
	public enum Type{ STATIC, DYNAMIC };
	private String ip;
	private Type type;
	private short portID;
	private int TTL;
	
	public LookupRecord(String ip, Type type, short portID){
		this.ip = ip;
		this.type = type;
		this.portID = portID;
		this.TTL = 64;
	}
	
	public String getIp() { return ip; }
	public Type getType() { return type; }
	public short getPortID() { return portID; }
	
	public void refTTL(){ this.TTL = 64; }
	public Boolean updTTL(){ 
		if (TTL < 1)
			return false;
		else
		{
			this.TTL -= 1;
			return true;
		}
	}
	public int getTTL(){ return this.TTL; }
}
