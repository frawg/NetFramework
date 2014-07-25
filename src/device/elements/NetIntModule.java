package device.elements;

import java.util.ArrayList;
import java.util.LinkedList;

import protocols.IPv4;
import protocols.MACGenerator;
import device.Device;
import netdata.Frame;
import netdata.LookupRecord;
import netdata.PacketMovement;
import netdata.enums.ProtocolNumbers;
import netdata.packet.IPv4Packet;
import netdata.packet.Packet;

public class NetIntModule {
	private Device parentDevice = null;
	private String macAdd = null;
	private Port[] ports = null;
	private IPv4 ip = null;
	
	public NetIntModule(short portNum, Device parent){
		this.parentDevice = parent;
		this.ports = new Port[portNum];
		this.macAdd = MACGenerator.generateMAC();
	}

	public void setIPv4(String add, String gateway, String subnet){
		this.ip = new IPv4(add, gateway, subnet);
	}
	
	public boolean hasFrameOnPort(int ind){
		return ports[ind].hasFrame();
	}
	
	public boolean hasAppType(ProtocolNumbers n, int p)
	{
		if (ports[p].hasAppPacket(n))
				return true;
		return false;
	}
	
	public Packet getAppPacket(ProtocolNumbers n, int p)
	{
		return ports[p].getAppPacket(n);
	}
	
	public Frame takeFrameFromPort(int ind){
		return ports[ind].PollQueue();
	}
	
	public Device getParent(){ return parentDevice; }
	public String getMacAdd(){ return macAdd; }
	public IPv4 getIPv4(){ return ip; }
	public int size(){ return ports.length; }
	public void portSend(int index, Frame f){ ports[index].SendFrame(f); }
	public String getPortMAC(int ind) { return ports[ind].getMAC(); } 
	//public boolean setPortOppo(Port p, short port){ return ports[port].setOppo(p); }
	public boolean setConnection(Connection p, short port){ return ports[port].setConnection(p); }

	public Frame getAppFrame(ProtocolNumbers n, int i) {
		return ports[i].getAppFrame(n);
	}
}
