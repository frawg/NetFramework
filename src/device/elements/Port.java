package device.elements;

import java.util.concurrent.LinkedBlockingQueue;

import protocols.IPv4;
import protocols.MACGenerator;
import protocols.Protocol;
import netdata.Frame;
import netdata.enums.ProtocolNumbers;
import netdata.packet.IPv4Packet;
import netdata.packet.Packet;

public class Port {
	//private Port oppo = null;
	private Connection conn = null;
	private String MACaddress = null;
	private LinkedBlockingQueue<Frame> recvQueue = null;
	private NetIntModule parent = null;
	
	public Port(NetIntModule p){
		recvQueue = new LinkedBlockingQueue<Frame>();
		parent = p;
		MACaddress = MACGenerator.generateMAC();
	}
	
//	public boolean setOppo(Port p)
//	{
//		if (!isConnected())
//		{
//			oppo = p;
//			return true;
//		}
//		return false;
//	}
	
	public NetIntModule getParent() { return parent; }
	public String getMAC() { return MACaddress; }
	
	public boolean setConnection(Connection conn){
		if (isConnected())
			return false;
		else
			this.conn = conn;
		return true;
	}
	
	public void ReceiveFrame(Frame f){
		try { recvQueue.put(f); }
		catch (InterruptedException e){}
	}
	
	public boolean hasAppPacket(ProtocolNumbers n)
	{
		for (Frame f : recvQueue)
			if (f.getPayload() instanceof IPv4Packet) {
				if (((IPv4Packet) f.getPayload()).getProtocol() == n)
				{
					return true;
				}
			}
		return false;
	}
	
	public Frame getAppFrame(ProtocolNumbers n)
	{
		for (Frame f : recvQueue)
			if (f.getPayload() instanceof IPv4Packet) {
				if (((IPv4Packet) f.getPayload()).getProtocol() == n)
				{
					recvQueue.remove(f);
					return f;
				}
			}
		return null;
	}
	
	public Packet getAppPacket(ProtocolNumbers n)
	{
		return getAppFrame(n).getPayload();
	}
	
	public boolean hasFrame() { return !recvQueue.isEmpty(); }
	
	public Frame PollQueue(){
		try { return recvQueue.take(); }
		catch (InterruptedException e) { /*e.printStackTrace();*/ }
		return null;
	}
	
	public boolean SendFrame(Frame f){
		//oppo.ReceiveFrame(f);
		if (conn != null)
		{
			conn.sentToOpposite(f, this);
			return true;
		}
		else
			return false;
	}
	
	public boolean isConnected(){ 
		if (/*oppo*/ conn != null)
			return true;
		return false;
	}
}
