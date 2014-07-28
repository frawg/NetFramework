package netdata;

import java.util.Date;

public class ARPrecord {
	private String macAdd, ip;
	private short port;
	private boolean dynamic;
	private Date TTL;
	
	public ARPrecord(String mac, String ip, short port, int seconds, boolean b)
	{
		macAdd = mac;
		this.ip = ip;
		this.port = port;
		TTL = new Date(System.currentTimeMillis() + (seconds * 1000));
		dynamic = b;
	}

	public String getMacAdd(){ return macAdd; }
	public void setMac(String mac) { this.macAdd = mac; }
	public String getIP(){ return ip; }
	public void setIP(String ip) { this.ip = ip; }
	public int getPort() { return port; }
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
