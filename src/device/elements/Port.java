package device.elements;

import java.util.concurrent.LinkedBlockingQueue;

import device.Device;
import protocols.layer2.DataProtocol;
import protocols.layer2.IPv4;
import protocols.layer2.MACGenerator;
import netdata.Frame;
import netdata.packet.IPv4Packet;
import netdata.packet.Packet;

public abstract class Port extends Thread {
	private Port oppo = null;
	//private Connection conn = null;
	private LinkedBlockingQueue<Frame> recvQueue = null;
	private LinkedBlockingQueue<Frame> sendQueue = null;
	private Device parentDevice = null;
	private String macAdd = null;
	private IPv4 ip = null;
	private int speed; //bytes per second or Bps. A frame is considered to be 64 bytes + 20 bytes of overhead.
	private int bufferSize;
	private short index;
	
	public Port(Device p, int bufferSize, short index){
		recvQueue = new LinkedBlockingQueue<Frame>();//bufferSize);
		sendQueue = new LinkedBlockingQueue<Frame>();//bufferSize);
		this.bufferSize = bufferSize;
		this.index = index;
		parentDevice = p;	
		macAdd = MACGenerator.generateMAC();
		speed = 100000;
	}
	
	public void run()
	{
		while (!isInterrupted())
		{
//			System.out.println(parentDevice.getDeviceName() + " port" + getIndex() + " send cycle start");
			if (!sendQueue.isEmpty())
			{
				if (oppo != null)
				{
					oppo.ReceiveFrame(sendQueue.poll());
					System.out.println("Frame sent from " + parentDevice.getDeviceName() + " port" + getIndex() + " to " + oppo.getParent().getDeviceName() + " port" + oppo.getIndex());
				}
				else
				{
					sendQueue.poll();
//					System.out.println("Opposite port is null");
				}
			}
			
//			System.out.println(parentDevice.getDeviceName() + " port" + getIndex() + " send cycle over, receive cycle start");
			Frame f = null; 
			if (!recvQueue.isEmpty())
			{
				try {
					f = recvQueue.take();
					System.out.println(parentDevice.getDeviceName() + " port" + getIndex() + " receive queue not empty");
				} catch (InterruptedException e) {}
				if (f != null)
				{
					if (!f.getDestMAC().equals(macAdd))
					{
						//the frame is not for this
						frameIsNotForThis(f);
					}
					else
					{
						//the frame is for this
						frameIsForThis(f);
					}	
				}
			}
//			System.out.println(parentDevice.getDeviceName() + " port" + getIndex() + " receive cycle over");
			try {
				sleep(0, sleepTime());
			} catch (InterruptedException e) {}
		}
	}
	
	public abstract void frameIsForThis(Frame f);
	public abstract void frameIsNotForThis(Frame f);
	
	public synchronized void ReceiveFrame(Frame f){
		if (recvQueue.size() < bufferSize)
		{
			if (recvQueue.offer(f))
			{
				System.out.println("Frame " + f.getEtherType() + " received on " + parentDevice.getDeviceName() + " port" + getIndex());
				// frame is dropped 
			}
			else
				System.out.println("Frame " + f.getEtherType() + " dropped at port");
		}
		else
			System.out.println("Frame " + f.getEtherType() + " dropped at port");
		
	}

	public synchronized void setIPv4(String add, String gateway, String subnet){
		this.ip = new IPv4(add, gateway, subnet);
	}
	
	public synchronized boolean setOppo(Port p)
	{
		if (!isConnected())
		{
			oppo = p;
			return true;
		}
		return false;
	}
	
	public short getIndex() { return index; }
	public Device getParent(){ return parentDevice; }
	public String getMacAdd(){ return macAdd; }
	public IPv4 getIPv4(){ return ip; }
	public int getBufferSize(){ return bufferSize; }
	public Frame sendFrame(String destMac, int etherType, Packet p){
		Frame f = new Frame(macAdd, destMac, etherType, p);
		sendQueue.offer(f);
		return f;
	}
	
	public void sendFrame(Frame f)
	{
		sendQueue.offer(f);
	}
	
	private int sleepTime() { return (int)(((double)(64 + 20) / (double)speed) * 1000 * 1000 * 1000); } // convert to micro then nano
	
	public boolean hasAppPacket(int n)
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
	
	public Frame getAppFrame(int n)
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
	
	public Packet getAppPacket(int n)
	{
		return getAppFrame(n).getPayload();
	}
	
	public boolean hasFrame() { return !recvQueue.isEmpty(); }
	
	public synchronized Frame PollQueue(){
		try { return recvQueue.take(); }
		catch (InterruptedException e) { /*e.printStackTrace();*/ }
		return null;
	}
	
	public boolean isConnected(){ return (oppo /*conn*/ != null); }
}