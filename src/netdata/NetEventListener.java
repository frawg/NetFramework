package netdata;

public abstract class NetEventListener {
	public abstract void frameReceived(NetEvent e);
	public abstract void frameDropped(NetEvent e);
	public abstract void frameProcessed(NetEvent e);
	public abstract void frameSent(NetEvent e);
}
