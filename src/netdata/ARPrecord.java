package netdata;

public class ARPrecord {
	private String macAdd, ip;
	private int module, port;
	private boolean dynamic;
	private short TTL;
	
	public ARPrecord(String mac, String ip, int module, int port, short t, boolean b)
	{
		macAdd = mac;
		this.ip = ip;
		this.module = module;
		this.port = port;
		TTL = t;
		dynamic = b;
	}

	public String getMacAdd(){ return macAdd; }
	public void setMac(String mac) { this.macAdd = mac; }
	public String getIP(){ return ip; }
	public void setIP(String ip) { this.ip = ip; }
	public int getModule() { return module; }
	public int getPort() { return port; }
	
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
