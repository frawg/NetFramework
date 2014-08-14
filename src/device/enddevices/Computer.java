package device.enddevices;

import java.util.ArrayList;

import protocols.layer2.MACGenerator;
import netdata.ARPrecord;
import netdata.Frame;
import netdata.FrameMovement;
import netdata.NetEvent;
import netdata.NetEvent.ACTION;
import netdata.packet.ARPpacket;
import netdata.packet.ICMPpacket;
import netdata.packet.ICMPpacket.Type;
import netdata.packet.IPv4Packet;
import netdata.packet.Packet;
import device.Device;
import device.elements.Port;

public class Computer extends Device {
	private ArrayList<ARPrecord> arpCache = null;
	private Frame current = null;

	public Computer(String name, int bufferSize)
	{
		super(name, 1, bufferSize);
		arpCache = new ArrayList<ARPrecord>();
	}

	@Override
	protected void initPorts(int bufferSize) {
		for (int i = 0; i < ports.length; i++)
		{
			ports[i] = new Port(this, bufferSize, (short)i) {
				@Override
				public void frameIsNotForThis(Frame f) {
//					System.out.println(devName + " port" + getIndex() + ": " + "Frame is not for this");
					if (f.getDestMAC().equals(MACGenerator.BROADCAST))
						frameIsForThis(f);
				}
				@Override
				public void frameIsForThis(Frame f) {
//					System.out.print(devName + " port" + getIndex() + ": " + "Frame is for this");
//					if (f.getDestMAC().equals(MACGenerator.BROADCAST))
//						System.out.println("BROADCAST");
//					else
//						System.out.println("UNICAST");
					receiveFrame(f, getIndex());
				}
			};
		}
	}
	
	private String MACtoIP(String mac){
		for (int i = 0; i < arpCache.size(); i++)
			if (arpCache.get(i).equals(mac))
				return arpCache.get(i).getIP();
		return null;
	}
	
	private synchronized String IPtoMAC(String ip){
		for (ARPrecord r : arpCache)
			if (r.getIP().equals(ip))
			{
				System.out.println(devName + " ARP cache record found for " + ip + " to " + r.getMacAdd());
				return r.getMacAdd();
			}
		return null;
	}
	
	private boolean hasMAC(String mac){
		for (ARPrecord a : arpCache)
			if (a.getMacAdd().equals(mac))
				return true;
		return false;
	}
	
	private boolean hasIP(String ip){
		for (ARPrecord a : arpCache)
			if (a.getIP().equals(ip))
				return true;
		return false;
	}
	
	private boolean cacheContains(String ip, String mac)
	{
		for (ARPrecord a : arpCache)
			if (a.getIP().equals(ip) && a.getMacAdd().equals(mac))
				return true;
		return false;
	}
	
	private void updateMACofIP(String ip, String mac){
		for (ARPrecord a : arpCache)
		{
			if (a.getIP().equals(ip))
			{
				a.setMac(mac);
				break;
			}
		}
	}
	
	private void updateIPofMAC(String mac, String ip){
		for (ARPrecord a : arpCache)
		{
			if (a.getIP().equals(mac))
			{
				a.setIP(ip);
				break;
			}
		}
	}
	
	private void arpRequest(String ip){
		Frame f = ports[0].sendFrame(MACGenerator.BROADCAST, Frame.ARP, 
				new ARPpacket(ports[0].getIPv4().getAddress(), ip, ports[0].getMacAdd(), MACGenerator.BROADCAST, Frame.IPv4, ARPpacket.OPERATION.REQUEST, (short)255));
		//System.out.println("ARP request sent");
		triggerSentListener(new NetEvent(this, f, "ARP Request", ACTION.SENT));
	}
	
	private void arpReply(String ip, String mac){
		Frame f =ports[0].sendFrame(mac, Frame.ARP, 
				new ARPpacket(ports[0].getIPv4().getAddress(), ip, ports[0].getMacAdd(), mac, Frame.IPv4, ARPpacket.OPERATION.REPLY, (short)255));
//		System.out.println("ARP reply sent");
		triggerSentListener(new NetEvent(this, f, "ARP Reply", ACTION.SENT));
	}
	
	public void ping(final String destination)
	{
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println("Ping started"); 
				runPing(destination);
			}
		});
			
		t.start();
		System.out.println("Ping ran");
	}
	
	public void runPing(String destination)
	{
//		System.out.println("Ping process started"); 
		Frame f = null;
		int time = 0;
		if (IPtoMAC(destination) == null)
			arpRequest(destination);
//		System.out.println("ARP request over");
		do {
			//System.out.println(IPtoMAC(destination));
			if (IPtoMAC(destination) != null && f == null)
			{
				f = ports[0].sendFrame(IPtoMAC(destination), Frame.IPv4,
						new ICMPpacket(ports[0].getIPv4().getAddress(), destination, ICMPpacket.Type.ECHO, 0));
//				System.out.println("Ping sent");
				triggerSentListener(new NetEvent(this, f,"IPv4 ICMP echo", ACTION.SENT));
			}
			else
			{
				System.out.println("Waiting for arp reply");
				time += 1;
			}
			try {
				sleep(10);
			} catch (InterruptedException e) {}
		} while (time < 64 && f == null);
		if (f != null && time < 64)
		{
			triggerSentListener(new NetEvent(this, f, "IPv4 - ICMP", ACTION.SENT));
			f = null;
			do {
					f = getCurrent();
//					System.out.println(getDeviceName() + " Frame received");
					if (f != null && f.getEtherType() == Frame.IPv4)
					{
						if (((IPv4Packet)f.getPayload()).getProtocol() == Packet.ICMP)
						{
							if (((ICMPpacket)f.getPayload()).getType() == Type.ECHO_REPLY && f.getDestMAC().equals(ports[0].getMacAdd()) 
							&& f.getPayload().getDestIP().equals(ports[0].getIPv4().getAddress()))
							{
								triggerReceiveListener(new NetEvent(this, f, "IPv4 - ICMP", ACTION.RECEIVED));
//								System.out.println(getDeviceName() + " ICMP reply received");
								break;
							}
						}
					}
					else
					{
						System.out.println(getDeviceName() + " Awaiting ICMP reply");
						if (f != null)
							triggerDropListener(new NetEvent(this, f, "", ACTION.DROPPED));
						f = null;
						time += 1;
					}
					try {
						sleep(10);
					} catch (InterruptedException e) {}
			} while (time < 64 && f == null);
		}
		else
		{
//			System.out.println("Timeout");
			triggerDropListener(new NetEvent(this, f, "IPv4 - ICMP", ACTION.DROPPED));
		}
//		System.out.println("\n");
	}
	
	private void ProcessFrame(Frame f, /*int m,*/ int p){
		System.out.println(getDeviceName() + " Processing frame...");
		if (f.getEtherType() == Frame.IPv4)
		{
			if (f.getPayload().getDestIP().equals(ports[0].getIPv4().getAddress()))
			{
				if (((IPv4Packet)f.getPayload()).getProtocol() == Packet.ICMP)
				{
//					System.out.println(getDeviceName() + " Packet is IPv4 " + ((ICMPpacket)f.getPayload()).getType().toString());
					if (((ICMPpacket)f.getPayload()).getType() == ICMPpacket.Type.ECHO)
					{
						triggerReceiveListener(new NetEvent(this, f, "IPv4 ICMP echo", ACTION.RECEIVED));
//						System.out.println(getDeviceName() + " This is a ping request, generating reply.");
						Frame m = ports[p].sendFrame(f.getSourceMAC(), f.getEtherType(),
								new ICMPpacket(ports[p].getIPv4().getAddress(), f.getPayload().getSourceIP(), ICMPpacket.Type.ECHO_REPLY, 0));
						triggerSentListener(new NetEvent(this, m, "IPv4 ICMP reply", ACTION.SENT));
					}
				}
			}
			else
				triggerDropListener(new NetEvent(this, f, "IPv4 Mistmatch", ACTION.DROPPED));
		}
		else if (f.getEtherType() == Frame.ARP)
		{
//			System.out.println(getDeviceName() + " Frame is ARP");
			if (f.getEtherType() == Frame.ARP && (f.getDestMAC().equals(MACGenerator.BROADCAST) || f.getDestMAC().equals(ports[0].getMacAdd())))//(f.getPayload() instanceof ARPpacket)
			{ 
				ARPpacket pack = (ARPpacket) f.getPayload();
				if (pack.getProtocolType() == Frame.IPv4)
				{
//					System.out.println(getDeviceName() + " ARP IPv4");
					if (!cacheContains(f.getPayload().getSourceIP(), f.getSourceMAC()))
					{
//						System.out.println(devName + " ARP cache added record for " + f.getPayload().getSourceIP() + " to " + f.getSourceMAC());
						arpCache.add(new ARPrecord(f.getSourceMAC(), f.getPayload().getSourceIP(), (short)p, 1800, true));
					}
					else 
					{
						triggerDropListener(new NetEvent(this, f, "ARP Exist", ACTION.RECEIVED));
//						System.out.println(devName + " ARP cache record exist for " + f.getPayload().getSourceIP() + " to " + f.getSourceMAC());
					}
					
					if (pack.getOperation() == ARPpacket.OPERATION.REQUEST && pack.getDestIP().equals(ports[0].getIPv4().getAddress()))
					{
						triggerReceiveListener(new NetEvent(this, f, "ARP request", ACTION.RECEIVED));
//						System.out.println(getDeviceName() + " ARP request");
						arpReply(f.getPayload().getSourceIP(), f.getSourceMAC());
					}
					else if (pack.getOperation() == ARPpacket.OPERATION.REPLY)
					{
//						System.out.println(getDeviceName() + " ARP reply");
						triggerReceiveListener(new NetEvent(this, f, "ARP reply", ACTION.RECEIVED));
					}
					else
						triggerDropListener(new NetEvent(this, f, "ARP", ACTION.DROPPED));
				}
			}
			else
				if (f != null)
					triggerDropListener(new NetEvent(this, f, "", ACTION.DROPPED));
		}
		else
			if (f != null)
				triggerDropListener(new NetEvent(this, f, "", ACTION.DROPPED));
	}
	
	private synchronized Frame getCurrent() { return current; }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!isInterrupted())
		{
			FrameMovement fm = null;
			try {
//				System.out.println(getDeviceName() + " Awaiting for incoming frame" + getDeviceName());
				fm = incoming.take();
//				System.out.println(getDeviceName() + " There is an incoming frame");
			} catch (InterruptedException e) {}
			
			if (fm != null){
				current = fm.getFrame();
//				synchronized(current)
//				{
//					System.out.println("Notifying all waiting processes");
//					current.notifyAll();
					ProcessFrame(current, fm.getPortIndex());
//				}
			}			
			for (Thread t : threads)
			{
				if (!t.isAlive())
					threads.remove(t);
			}
		}
	}

	@Override
	public boolean setPortConnection(int i, Port p) { return ports[0].setOppo(p); }
}
