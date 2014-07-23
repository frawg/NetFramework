package device.enddevices;

import java.util.ArrayList;

import protocols.MACGenerator;
import netdata.CAMrecord;
import netdata.Frame;
import device.Device;
import device.elements.NetIntModule;
import device.elements.Port;

public class Switch implements Device {
	private ArrayList<CAMrecord> camTable = null;
	private NetIntModule[] NIMs = null;
	private String devName = null;
	private ArrayList<String> history = null;
	private short age = 300; // in seconds
	private boolean interrupted = false;
	
	public Switch(String name) {
		NIMs = new NetIntModule[1];
		NIMs[1] = new NetIntModule((short) 24, this);
		camTable = new ArrayList<CAMrecord>();
		this.devName = name;
		this.history = new ArrayList<String>();
	}

	private void Broadcast(Frame f)
	{
		for (int m = 0; m < NIMs.length; m++)
		{
			for (int p = 0; p < NIMs[m].size(); p++)
			{
				if (!recordExist(f.getSourceMAC(), m, p))
				{
					NIMs[m].portSend(p, f);
					break;
				}
			}
		}
	}
	
	private boolean recordExist(String mac, int module, int port)
	{
		for (int i = 0; i < camTable.size(); i++)
		{
			if (!camTable.get(i).decTTL())
				camTable.remove(i);
			else 
				if ((camTable.get(i).getIntMod() == module) && (camTable.get(i).getPort() == port)
							&& camTable.get(i).getMacAdd().toUpperCase().equals(mac.toUpperCase()))
				return true;
		}
		return false;
	}
	
	private boolean recordExist(String mac)
	{
		for (int i = 0; i < camTable.size(); i++)
		{
			if (camTable.get(i).getMacAdd().toUpperCase().equals(mac.toUpperCase()))
				return true;
		}
		return false;
	}
	
	private void ProcessFrame(Frame f, short module, short port) {
		// TODO Auto-generated method stub
		if (!recordExist(f.getSourceMAC(), module, port) && (!f.getSourceMAC().toUpperCase().equals(MACGenerator.BROADCAST)))
		{
			camTable.add(new CAMrecord(f.getSourceMAC(), module, port, age, true));
		}
		
		if (f.getDestMAC().toUpperCase().equals(MACGenerator.BROADCAST) || !recordExist(f.getDestMAC()))
		{ Broadcast(f); }
		else
		{
			for (int c = 0; c < camTable.size(); c++)
			{
				if (camTable.get(c).getMacAdd().toUpperCase().equals(f.getDestMAC()))
				{ NIMs[camTable.get(c).getIntMod()].portSend(camTable.get(c).getPort(), f); }
			}
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		while(!isInterrupted())
		{
			for (short m = 0; m < NIMs.length; m++)
				for (short p = 0; p < NIMs[m].size(); p++)
				{
					if (NIMs[m].hasFrameOnPort(p))
					{ ProcessFrame(NIMs[m].takeFrameFromPort(p), m, p); }
				}
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