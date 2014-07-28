package netdata;

import java.util.Calendar;
import java.util.Date;

public class CAMrecord {
	private String macAdd;
	private short portNum;
	private boolean dynamic;
	private Date TTL, sent;
	
	public CAMrecord(String mac, short port, int seconds, boolean b)
	{
		macAdd = mac;
		portNum = port;
		TTL = new Date(System.currentTimeMillis() + (seconds * 1000));
		dynamic = b;
		sent = null;
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

	public boolean isSent()
	{
		if (sent == null)
			return false;
		if ((sent.getTime() - System.currentTimeMillis()) < (5 * 1000)
				&& (sent.getTime() - System.currentTimeMillis()) > 0)
			return true;
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
	
	public boolean isTimeForUpdate()
	{
		if (!isExpired() && ((TTL.getTime() - System.currentTimeMillis()) < (30 * 1000))
				&& ((TTL.getTime() - System.currentTimeMillis()) > 10) && !isSent())
		{
			return true;
		}
		else return false;	
	}
	
	public void toggleSent(){
		if (!isSent())
		{
			sent = new Date(System.currentTimeMillis());
		}
	}
}
