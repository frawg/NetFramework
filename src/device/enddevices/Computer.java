package device.enddevices;

import java.util.ArrayList;

import protocols.layer2.MACGenerator;
import netdata.ARPrecord;
import netdata.Frame;
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
	
	public void ping(String destination) throws InterruptedException
	{
		Frame f = null;
		int i = arpCache.size();
		int time = 0;
		if (IPtoMAC(destination) == null)
			arpRequest(destination);
		do {
			if (IPtoMAC(destination) != null)
			{
				f = ports[0].sendFrame(IPtoMAC(destination), Frame.IPv4, new ICMPpacket(ports[0].getIPv4().getAddress(), destination, ICMPpacket.Type.ECHO, 0));
			}
			else
			{
				i = arpCache.size();
				time += 1;
			}
			sleep(10);
		} while (time < 64 && arpCache.size() > i);
		if (f != null && time < 64)
		{
			triggerSentListener(new NetEvent(this, f, "IPv4 - ICMP", ACTION.SENT));
			//Frame pack = NIMs[0].getAppFrame(ProtocolNumbers.ICMP, 0);
			f = null;
			do {
				f = current;
				if (f.getPayload() instanceof ICMPpacket && f.getDestMAC().equals(ports[0].getMacAdd()) 
						&& f.getPayload().getDestIP().equals(ports[0].getIPv4().getAddress()))
				{
					//((ICMPpacket) pack).getType();
					triggerReceiveListener(new NetEvent(this, f, "IPv4 - ICMP", ACTION.RECEIVED));
					if (current == f)
					{
						//request next frame to be processed
					}
				}
				else
				{
					f = null;
					time += 1;
				}
				sleep(10);
			} while (time < 64 && f == null);
		}
		else
		{
			//triggerDropListener(new NetEvent(this, f, EtherType.IPv4.toString() + " " + ProtocolNumbers.ICMP.toString(), ACTION.DROPPED));
			//Time out;
		}
	}
	
	private void ProcessFrame(Frame f, /*int m,*/ int p){
		if (f.getEtherType() == Frame.IPv4)
		{
			if (((IPv4Packet)f.getPayload()).getProtocol() == Packet.ICMP)
			{
				if (((ICMPpacket)f.getPayload()).getType() == ICMPpacket.Type.ECHO)
				{
					//NIMs[0].portSend(0, new Frame(NIMs[0].getMacAdd(), f.getSourceMAC(), EtherType.IPv4, new ICMPpacket(NIMs[0].getIPv4().getAddress(), f.getPayload().getSourceIP(), Type.ECHO_REPLY, 0)));
				}
				//else if (((ICMPpacket)f.getPayload()).getType() == ICMPpacket.Type.ECHO_REPLY)
				//{
					//triggerReceiveListener(e);
				//}
			}
		}
		else if (f.getEtherType() == Frame.ARP)
		{
			if (f.getPayload() instanceof ARPpacket)
			{ 
				ARPpacket pack = (ARPpacket) f.getPayload();
				if (pack.getProtocolType() == Frame.IPv4)
				{
					//todo
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
						//arpReply(NIMs[0].getIPv4().getAddress(), NIMs[0].getMacAdd());
					}
					else if (pack.getOperation() == ARPpacket.OPERATION.REPLY)
					{
						//triggerReceiveListener(e);
					}
				}
			}
		}
	}
	
	private synchronized Frame getCurrent() { return current; }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!isInterrupted())
		{
//			for (short m = 0; m < NIMs.length; m++)
//				for (short p = 0; p < NIMs[m].size(); p++)
//				{
//					if (NIMs[m].hasFrameOnPort(p))
//					{ ProcessFrame(NIMs[m].takeFrameFromPort(p), m, p); }
//				}
//			try {
//				sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}

	@Override
	public boolean setPortConnection(int i, Port p) {
		// TODO Auto-generated method stub
		return ports[0].setOppo(p);
	}
}
