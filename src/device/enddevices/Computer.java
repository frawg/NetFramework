package device.enddevices;

import java.util.ArrayList;

import protocols.MACGenerator;
import netdata.ARPrecord;
import netdata.Frame;
import netdata.Packet;
import device.Device;
import device.elements.NetIntModule;
import device.elements.Port;

public class Computer implements Device {
	private NetIntModule[] NIMs = null;
	private String devName = null;
	private ArrayList<ARPrecord> arpCache = null;
	private ArrayList<String> history = null;
	private boolean interrupted = false;

	public Computer(String name)
	{
		devName = name;
		NIMs = new NetIntModule[1];
		NIMs[1] = new NetIntModule((short) 1, this);
		history = new ArrayList<String>();
		arpCache = new ArrayList<ARPrecord>();
	}
	
	private String MACtoIP(String mac){
		for (int i = 0; i < arpCache.size(); i++)
			if (arpCache.get(i).equals(mac))
				return arpCache.get(i).getIP();
		return null;
	}
	
	private void arpRequest(String ip){
		NIMs[0].portSend(0, new Frame(NIMs[0].getMacAdd(), MACGenerator.BROADCAST, 
				new Packet(NIMs[0].getIPv4().getAddress(), ip, "")));
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!isInterrupted())
		{
			
		}
	}

	public void stop() { interrupted = true; }
	public void resetInterrupted() { interrupted = false; }
	public boolean isInterrupted() { return interrupted; } 

	@Override
	public NetIntModule getNetIntModule(int ind) { return NIMs[ind]; }

	@Override
	public String getDeviceName() { return devName; }

	@Override
	public void setDeviceName(String name) { this.devName = name; }

	@Override
	public String getHistory(int i) { return history.get(i); }

	@Override
	public int sizeOfHistory() { return history.size(); }

	@Override
	public int totalPorts() {
		int temp = 0;
		for (int m = 0; m < NIMs.length; m++)
			temp += NIMs[m].size();
		return temp;
	}

	@Override
	public int totalNIMs() { return NIMs.length; }

	@Override
	public int amountOfPorts(short ind) { return NIMs[ind].size(); }

	@Override
	public boolean setPortOppo(Port p, short module, short port) { return NIMs[module].setPortOppo(p, port); }
}
