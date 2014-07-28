package netdata;

import java.util.Date;

public class CAMrecord {
	private String macAdd;
	private short portNum;
	private boolean dynamic;
	private Date TTL;
	
	public CAMrecord(String mac, short port, int seconds, boolean b)
	{
		macAdd = mac;
		portNum = port;
		TTL = new Date(System.currentTimeMillis() + (seconds * 1000));
		dynamic = b;
	}
	
	public short getPort(){ return portNum; }
	public String getMacAdd(){ return macAdd; }
	public boolean UpdateAge(int seconds)
	{
		if (!isExpired() && dynamic)
		{
			TTL = new Date(System.currentTimeMillis() + (seconds * 1000));
			return true;
		}
		else if (!dynamic)
			return true;
		else
			return false;
	}
	
	public boolean isExpired(){
		if (dynamic)
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
