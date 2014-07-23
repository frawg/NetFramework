package netdata;

public class ARPrecord {
	private String macAdd, ip;
	private boolean dynamic;
	private short TTL;
	
	public ARPrecord(String mac, String ip, short t, boolean b)
	{
		macAdd = mac;
		this.ip = ip;
		TTL = t;
		dynamic = b;
	}

	public String getMacAdd(){ return macAdd; }
	public String getIP(){ return ip; }
	
	public boolean decTTL()
	{
		if (dynamic)
		{
			TTL =- 1;
			if (TTL > 0)
			{
				return true;
			}
		}
		return false;
	}
}
