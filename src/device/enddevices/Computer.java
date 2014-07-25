package device.enddevices;

import java.util.ArrayList;

import protocols.MACGenerator;
import netdata.ARPrecord;
import netdata.Frame;
import netdata.NetEvent;
import netdata.NetEvent.ACTION;
import netdata.enums.EtherType;
import netdata.enums.ProtocolNumbers;
import netdata.enums.ICMP.Type;
import netdata.packet.ARPpacket;
import netdata.packet.ICMPpacket;
import netdata.packet.IPv4Packet;
import netdata.packet.Packet;
import device.Device;
import device.elements.NetIntModule;
import device.elements.Port;

public class Computer extends Device {
	private ArrayList<ARPrecord> arpCache = null;

	public Computer(String name)
	{
		super(name, 1, 1);
		arpCache = new ArrayList<ARPrecord>();
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
		NIMs[0].portSend(0, new Frame(NIMs[0].getPortMAC(0), MACGenerator.BROADCAST, EtherType.ARP, 
				new ARPpacket(NIMs[0].getIPv4().getAddress(), ip, NIMs[0].getMacAdd(), MACGenerator.BROADCAST, ARPpacket.OPERATION.REQUEST, (short)255)));
	}
	
	private void arpReply(String ip, String mac){
		NIMs[0].portSend(0, new Frame(NIMs[0].getPortMAC(0), mac, EtherType.ARP, 
				new ARPpacket(NIMs[0].getIPv4().getAddress(), ip, NIMs[0].getMacAdd(), mac, ARPpacket.OPERATION.REPLY, (short)255)));
	}
	
	public void ping(String destination) throws InterruptedException {
		Frame f = null;
		if (IPtoMAC(destination) != null)
		{
			f = new Frame(NIMs[0].getPortMAC(0), IPtoMAC(destination), EtherType.IPv4,
					new ICMPpacket(this.NIMs[0].getIPv4().getAddress(), destination, Type.ECHO, 0));
			NIMs[0].portSend(0, f);
		}
		else
		{
			int i = arpCache.size();
			int time = 0;
			arpRequest(destination);
			do {
				if (IPtoMAC(destination) != null)
				{
					f = new Frame(NIMs[0].getPortMAC(0), IPtoMAC(destination), EtherType.IPv4,
							new ICMPpacket(this.NIMs[0].getIPv4().getAddress(), destination, Type.ECHO, 0));
					NIMs[0].portSend(0, f);
				}
				else
				{
					i = arpCache.size();
					time += 1;
				}
				sleep(100);
			} while (time < 64 && arpCache.size() > i);
			if (f != null && time < 64)
			{
				triggerSentListener(new NetEvent(this, f, EtherType.IPv4.toString() + " " + ProtocolNumbers.ICMP.toString(), ACTION.SENT));
				Frame pack = NIMs[0].getAppFrame(ProtocolNumbers.ICMP, 0);
				do {
					if (pack.getPayload() instanceof ICMPpacket)
					{
						//((ICMPpacket) pack).getType();
						triggerReceiveListener(new NetEvent(this, pack, EtherType.IPv4.toString() + " " + ProtocolNumbers.ICMP.toString(), ACTION.RECEIVED));
					}
					else
						time += 1;
					sleep(100);
				} while (time < 64 && pack != null);
			}
			else
			{
				triggerDropListener(new NetEvent(this, f, EtherType.IPv4.toString() + " " + ProtocolNumbers.ICMP.toString(), ACTION.DROPPED));
				//Time out;
			}
		}
	}
	
	private void ProcessFrame(Frame f, int m, int p){
		if (f.getEtherType() == EtherType.IPv4)
		{
			
		}
		else if (f.getEtherType() == EtherType.ARP)
		{
			if (f.getPayload() instanceof ARPpacket)
			{ 
				ARPpacket pack = (ARPpacket) f.getPayload();
				if (pack.getProtocolType() == EtherType.IPv4)
				{
					//todo
					if ()
					
					if (pack.getOperation() == ARPpacket.OPERATION.REQUEST)
					{
						
					}
					else if (pack.getOperation() == ARPpacket.OPERATION.REPLY)
					{
						
					}
				}
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!isInterrupted())
		{
			for (short m = 0; m < NIMs.length; m++)
				for (short p = 0; p < NIMs[m].size(); p++)
				{
					if (NIMs[m].hasFrameOnPort(p))
					{ ProcessFrame(NIMs[m].takeFrameFromPort(p), m, p); }
				}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
