package device.elements;

import java.util.concurrent.LinkedBlockingQueue;
import netdata.Frame;

public class Port {
	private Port oppo = null;
	private LinkedBlockingQueue<Frame> recvQueue = null;
	
	public Port(){
		recvQueue = new LinkedBlockingQueue<Frame>();
	}
	
	public boolean setOppo(Port p)
	{
		if (!isConnected())
		{
			oppo = p;
			return true;
		}
		return false;
	}
	
	public void ReceiveFrame(Frame f){
		try { recvQueue.put(f); }
		catch (InterruptedException e){}
	}
	
	public boolean hasFrame() { return !recvQueue.isEmpty(); }
	
	public Frame PollQueue(){
		try { return recvQueue.take(); }
		catch (InterruptedException e) { /*e.printStackTrace();*/ }
		return null;
	}
	
	public void SendFrame(Frame f){
		oppo.ReceiveFrame(f);
	}
	
	public boolean isConnected(){ 
		if (oppo != null)
			return true;
		return false;
	}
}
