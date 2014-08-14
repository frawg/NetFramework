package netdata;

import java.util.Date;

public class LookupRecord {
	private String ip;
	private boolean type;
	private short portID;
	private Date TTL;
	
	public LookupRecord(String ip, short portID, int seconds, boolean type){
		this.ip = ip;
		this.type = type;
		this.portID = portID;
		this.TTL = new Date(System.currentTimeMillis() + (seconds * 1000));
	}
	
	public String getIp() { return ip; }
	public boolean isDynamic() { return type; }
	public short getPortID() { return portID; }

	public boolean UpdateAge(int seconds)
	{
		if (!isExpired() && type)
		{
			TTL = new Date(System.currentTimeMillis() + (seconds * 1000));
			return true;
		}
		else if (!type)
			return true;
		else
			return false;
	}
	
	public boolean isExpired(){
		if (type)
		{
			if (TTL.before(new Date(System.currentTimeMillis())))
			{
				return true;
			}
			else 
				return false;
		}
		else
		{
			return false;
		}
	}
}
