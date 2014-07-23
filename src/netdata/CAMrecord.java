package netdata;

public class CAMrecord {
	private String macAdd;
	private short interMod, portNum;
	private boolean dynamic;
	private short TTL;
	
	public CAMrecord(String mac, short inter, short port, short t, boolean b)
	{
		macAdd = mac;
		interMod = inter;
		portNum = port;
		TTL = t;
		dynamic = b;
	}
	
	public short getIntMod(){ return interMod; }
	public short getPort(){ return portNum; }
	public String getMacAdd(){ return macAdd; }
	
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
