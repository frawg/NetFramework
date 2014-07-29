package device;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import netdata.Frame;
import netdata.FrameMovement;
import netdata.NetEvent;
import netdata.NetEventListener;
import device.elements.*;

public abstract class Device extends Thread {
	protected LinkedBlockingQueue<FrameMovement> incoming = null;
	protected String devName = null;
	protected ArrayList<String> history = null;
	protected ArrayList<NetEventListener> listeners = null;
	protected Port[] ports = null;
	protected ArrayList<Thread> threads = null;

	public Device(String name, int ports, int bufferSize){
		this.devName = name;
		this.ports = new Port[ports];
		this.history = new ArrayList<String>();
		this.listeners = new ArrayList<NetEventListener>();
		incoming = new LinkedBlockingQueue<FrameMovement>(bufferSize);
		threads = new ArrayList<Thread>();
		
		initPorts(bufferSize);
	}
	
	protected void initPorts(int bufferSize){
		for (Port p : ports)
		{
			p.start();
		}
	}
	
	@Override
	public void run()
	{
	}
	@Override
	public void interrupt()
	{
		super.interrupt();
		for (Port p : ports)
			p.interrupt();
	}
	
	public void interrupt(int sec) throws InterruptedException
	{
		sleep(sec * 1000);
		this.interrupt();
	}
	
	public String getDeviceName() { return devName; }
	public void setDeviceName(String name) { this.devName = name; }
	public String getHistory(int i) { return history.get(i); }
	public int sizeOfHistory() { return history.size(); }
	public Port getPort(int i) { 
		if (i < ports.length)
			return ports[i];
		return null;
	}
	public abstract boolean setPortConnection(int i, Port p);

	public int totalPorts(){ return ports.length; };
	
	protected synchronized void receiveFrame(Frame f, int port)
	{
		incoming.offer(new FrameMovement((short)port, f));
	}
	
	protected void triggerReceiveListener(NetEvent e)
	{
		for (NetEventListener l : listeners)
		{ l.frameReceived(e); }
	}
	
	protected void triggerDropListener(NetEvent e)
	{
		for (NetEventListener l : listeners)
		{ l.frameDropped(e); }
	}
	
	protected void triggerProcessListener(NetEvent e)
	{
		for (NetEventListener l : listeners)
		{ l.frameProcessed(e); }
	}
	
	protected void triggerSentListener(NetEvent e)
	{
		for (NetEventListener l : listeners)
		{ l.frameSent(e); }
	}
	
	public boolean addListener(NetEventListener l){ return listeners.add(l); }
	public boolean removeListener(NetEventListener l){ return listeners.remove(l); }	
}