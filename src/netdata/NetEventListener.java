package netdata;

public interface NetEventListener {
	public void frameReceived(NetEvent e);
	public void frameDropped(NetEvent e);
	public void frameProcessed(NetEvent e);
	public void frameSent(NetEvent e);
}
