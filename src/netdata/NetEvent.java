package netdata;

import device.Device;

public class NetEvent {
	public enum ACTION {
		RECEIVED,
		DROPPED,
		PROCESSED,
		SENT
	};
	
	private String type = null;
	private Frame fr = null;
	private ACTION act = null;
	private Device source;
	
	public NetEvent(Device source, Frame f, String type, ACTION t)
	{
		this.source = source;
		this.fr = f;
		this.type = type;
		this.act = t;
	}
	
	public String getDestinationIP() { return fr.getPayload().getDestIP(); }
	public String getSourceIP() { return fr.getPayload().getSourceIP(); }
	public String getDestinationMAC() { return fr.getDestMAC(); }
	public String getSourceMAC() { return fr.getSourceMAC(); }
	public String getAction() { return act.toString(); }
	public Device getSource() { return source; }
	public int getFrameType() { return fr.getEtherType(); }
	
	public boolean isSameFrame(Frame f)
	{
		if (fr == f)
			return true;
		else
			return false;
	}
}
