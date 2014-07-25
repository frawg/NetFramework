package device.enddevices;

import java.util.ArrayList;

import protocols.MACGenerator;
import netdata.CAMrecord;
import netdata.Frame;
import device.Device;
import device.elements.NetIntModule;
import device.elements.Port;

public class Switch extends Device {
	private ArrayList<CAMrecord> camTable = null;
	private short age = 300; // in seconds
	
	public Switch(String name) {
		super(name, 1, 24);
		camTable = new ArrayList<CAMrecord>();
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

		f.getPayload().decTime();
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!isInterrupted())
		{
			for (short m = 0; m < NIMs.length; m++)
				for (short p = 0; p < NIMs[m].size(); p++)
				{
					if (NIMs[m].hasFrameOnPort(p))
					{ ProcessFrame(NIMs[m].takeFrameFromPort(p), m, p); }
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
	}
}