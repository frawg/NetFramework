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
		// TODO Auto-generated method stub
		for (int i = 0; i < ports.length; i++)
		{
			ports[i] = new Port(this, bufferSize, (short)i) {
				@Override
				public void frameIsNotForThis(Frame f) {
					if (f.getSourceMAC().equals(MACGenerator.BROADCAST))
						frameIsForThis(f);
				}
				@Override
				public void frameIsForThis(Frame f) {
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
	
	private String IPtoMAC(String ip){
		for (int i = 0; i < arpCache.size(); i++)
			if (arpCache.get(i).equals(ip))
				return arpCache.get(i).getMacAdd();
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
	
	private void updateMAC(String ip, String mac){
		for (ARPrecord a : arpCache)
		{
			if (a.getIP().equals(ip))
			{
				a.setMac(mac);
				break;
			}
		}
	}
	
	private void updateIP(String mac, String ip){
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
		ports[0].sendFrame(MACGenerator.BROADCAST, Frame.ARP, 
				new ARPpacket(ports[0].getIPv4().getAddress(), ip, ports[0].getMacAdd(), MACGenerator.BROADCAST, Frame.IPv4, ARPpacket.OPERATION.REQUEST, (short)255));
	}
	
	private void arpReply(String ip, String mac){
		ports[0].sendFrame(mac, Frame.ARP, 
				new ARPpacket(ports[0].getIPv4().getAddress(), ip, ports[0].getMacAdd(), mac, Frame.IPv4, ARPpacket.OPERATION.REPLY, (short)255));
	}
	
	public void ping(final String destination)
	{
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					System.out.println("Ping started"); 
					runPing(destination);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
			
//				Frame f = null;
//				int time = 0;
//				if (IPtoMAC(destination) == null)
//					arpRequest(destination);
//				do {
//					if (IPtoMAC(destination) != null)
//					{
//						f = ports[0].sendFrame(IPtoMAC(destination), Frame.IPv4, new ICMPpacket(ports[0].getIPv4().getAddress(), destination, ICMPpacket.Type.ECHO, 0));
//					}
//					else
//					{
//						time += 1;
//					}
//					try {
//						sleep(10);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				} while (time < 64);
//				if (f != null && time < 64)
//				{
//					triggerSentListener(new NetEvent(temp, f, "IPv4 - ICMP", ACTION.SENT));
//					//Frame pack = NIMs[0].getAppFrame(ProtocolNumbers.ICMP, 0);
//					f = null;
//					do {
//						synchronized (getCurrent()) {
//							try {
//								getCurrent().wait();
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//						f = getCurrent();
//						if (f.getPayload() instanceof ICMPpacket && f.getDestMAC().equals(ports[0].getMacAdd()) 
//								&& f.getPayload().getDestIP().equals(ports[0].getIPv4().getAddress()))
//						{
//							//((ICMPpacket) pack).getType();
//							triggerReceiveListener(new NetEvent(temp, f, "IPv4 - ICMP", ACTION.RECEIVED));
//						}
//						else
//						{
//							f = null;
//							time += 1;
//						}
//						try {
//							sleep(10);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					} while (time < 64 && f == null);
//				}
//				else
//				{
//					triggerDropListener(new NetEvent(temp, f, "IPv4 - ICMP", ACTION.DROPPED));
//				}
//			}
//		});
		t.start();
		System.out.println("Ping ran");
	}
	
	private void runPing(String destination) throws InterruptedException
	{
		System.out.println("Ping process started"); 
		Frame f = null;
		int time = 0;
		if (IPtoMAC(destination) == null)
			arpRequest(destination);
		System.out.println("ARP request over");
		do {
			System.out.println("Waiting for arp reply");
			if (IPtoMAC(destination) != null)
			{
				f = ports[0].sendFrame(IPtoMAC(destination), Frame.IPv4,
						new ICMPpacket(ports[0].getIPv4().getAddress(), destination, ICMPpacket.Type.ECHO, 0));
				System.out.println("Ping sent");
			}
			else
			{
				time += 1;
			}
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (time < 64);
		if (f != null && time < 64)
		{
			triggerSentListener(new NetEvent(this, f, "IPv4 - ICMP", ACTION.SENT));
			//Frame pack = NIMs[0].getAppFrame(ProtocolNumbers.ICMP, 0);
			f = null;
			do {
				synchronized (getCurrent()) {
					try {
						System.out.println("Awaiting ICMP reply");
						getCurrent().wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				f = getCurrent();
				System.out.println("Frame received");
				if (f.getPayload() instanceof ICMPpacket && f.getDestMAC().equals(ports[0].getMacAdd()) 
						&& f.getPayload().getDestIP().equals(ports[0].getIPv4().getAddress()))
				{
					//((ICMPpacket) pack).getType();
					triggerReceiveListener(new NetEvent(this, f, "IPv4 - ICMP", ACTION.RECEIVED));
					System.out.println("ICMP reply received");
				}
				else
				{
					f = null;
					time += 1;
				}
				try {
					sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (time < 64 && f == null);
		}
		else
		{
			System.out.println("Timeout");
			triggerDropListener(new NetEvent(this, f, "IPv4 - ICMP", ACTION.DROPPED));
		}
	}
	
	private void ProcessFrame(Frame f, /*int m,*/ int p){
		System.out.println("Processing frame...");
		if (f.getEtherType() == Frame.IPv4)
		{
			System.out.println("Frame is IPv4");
			triggerReceiveListener(new NetEvent(this, f, "IPv4", ACTION.RECEIVED));
			if (((IPv4Packet)f.getPayload()).getProtocol() == Packet.ICMP)
			{
				System.out.println("Packet is IPv4 ICMP");
				if (((ICMPpacket)f.getPayload()).getType() == ICMPpacket.Type.ECHO)
				{
					System.out.println("This is a ping request, generating reply.");
					ports[p].sendFrame(f.getSourceMAC(), f.getEtherType(),
							new ICMPpacket(ports[p].getIPv4().getAddress(), f.getPayload().getSourceIP(), ICMPpacket.Type.ECHO_REPLY, 0));
				}
			}
		}
		else if (f.getEtherType() == Frame.ARP)
		{
			System.out.println("Frame is ARP");
			if (f.getPayload() instanceof ARPpacket)
			{ 
				ARPpacket pack = (ARPpacket) f.getPayload();
				if (pack.getProtocolType() == Frame.IPv4)
				{
					System.out.println("ARP IPv4");
					if (!cacheContains(f.getPayload().getSourceIP(), f.getSourceMAC()))
						if (hasIP(f.getPayload().getSourceIP()))
						{
							updateMAC(f.getPayload().getSourceIP(), f.getSourceMAC());
						}
						else if (hasMAC(f.getSourceMAC()))
						{
							updateIP(f.getSourceMAC(), f.getPayload().getSourceIP());
						}
						else // does not exist
						{
							arpCache.add(new ARPrecord(f.getSourceMAC(), f.getPayload().getSourceIP(), (short)p, 1800, true));
						}
					
					if (pack.getOperation() == ARPpacket.OPERATION.REQUEST)
					{
						System.out.println("ARP request");
						arpReply(f.getPayload().getSourceIP(), f.getSourceMAC());

						triggerReceiveListener(new NetEvent(this, f, "ARP request", ACTION.RECEIVED));
					}
					else if (pack.getOperation() == ARPpacket.OPERATION.REPLY)
					{
						System.out.println("ARP reply");
						triggerReceiveListener(new NetEvent(this, f, "ARP reply", ACTION.RECEIVED));
					}
				}
			}
		}
	}
	
	private synchronized Frame getCurrent() { return current; }
	
	@Override
	public void run() {
		super.run();
		// TODO Auto-generated method stub
		while (!isInterrupted())
		{
			FrameMovement fm = null;
			try {
				fm = incoming.take();
				System.out.println("There is an incoming frame");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (fm != null){
				current = fm.getFrame();
//				if (!cacheContains(current.getPayload().getSourceIP(), current.getSourceMAC()))
//				{
//					arpCache.add(new ARPrecord(current.getSourceMAC(), current.getPayload().getSourceIP(), 
//							fm.getPortIndex(), 300, true));
//				}
				getCurrent().notifyAll();
				System.out.println("Notifying all waiting processes");
				ProcessFrame(current, fm.getPortIndex());
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
