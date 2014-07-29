package device.enddevices;

import java.util.ArrayList;

import protocols.layer2.MACGenerator;
import netdata.CAMrecord;
import netdata.Frame;
import netdata.FrameMovement;
import device.Device;
import device.elements.Port;

public class Switch extends Device {
	private ArrayList<CAMrecord> camTable = null;
	private short age = 300; // in seconds
	
	public Switch(String name) {
		super(name, 24, 10);
		camTable = new ArrayList<CAMrecord>();
	}

	@Override
	protected void initPorts(int bufferSize) {
		// TODO Auto-generated method stub
		for (int i = 0; i < ports.length; i++)
		{
			ports[i] = new Port(this, bufferSize, (short)i) {
				
				@Override
				public void frameIsNotForThis(Frame f) {
					receiveFrame(f, getIndex());
				}
				@Override
				public void frameIsForThis(Frame f) {
					// insert relevant protocol handlers
				}
			};
		}
		super.initPorts(bufferSize);
	}
	
	private void Broadcast(Frame f, int sourcePort)
	{
		for (int m = 0; m < ports.length; m++)
		{
			if (m != sourcePort)
				ports[m].sendFrame(f);
		}
	}
	
	private CAMrecord recordExist(String mac, int port) // mainly used for looking up existing records on source
	{
		for (int i = 0; i < camTable.size(); i++)
		{
			if ( (camTable.get(i).getPort() == port) // if the port matches
						&& camTable.get(i).getMacAdd().toUpperCase().equals(mac.toUpperCase()) // and the mac address matches
						 && !camTable.get(i).isExpired() ) // and has not expired
				return camTable.get(i); // this is the record
		}
		return null; // none found
	}
	
	private boolean recordExist(String mac)
	{
		for (int i = 0; i < camTable.size(); i++)
		{
			if ( camTable.get(i).getMacAdd().toUpperCase().equals(mac.toUpperCase()) // if the mac matches
					&& !camTable.get(i).isExpired() ) // and has not expired
				return true; // a record has been found
		}
		return false; // nope, no record
	}
	
	private /*synchronized*/ void ProcessFrame(Frame f, short port) {
		if (f.getDestMAC().toUpperCase().equals(MACGenerator.BROADCAST) || !recordExist(f.getDestMAC()))
		{ Broadcast(f, port); }
		else
		{
			cleanCAM(); // remove expired records
			for (int i = 0; i < camTable.size(); i++)
			{
				if (camTable.get(i).getMacAdd().equals(f.getDestMAC()))
				{
					ports[camTable.get(i).getPort()].sendFrame(f);
				}
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!isInterrupted())
		{
			FrameMovement fm = null;
			try {
				fm = incoming.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (fm != null)
			{
				updateCAMRecord(fm.getFrame().getSourceMAC(), fm.getPortIndex()); // update the source and port
				
				ProcessFrame(fm.getFrame(), fm.getPortIndex());
			}
//			for (short m = 0; m < NIMs.length; m++)
//				for (short p = 0; p < NIMs[m].size(); p++)
//				{
//					if (NIMs[m].hasFrameOnPort(p))
//					{ ProcessFrame(NIMs[m].takeFrameFromPort(p), m, p); }
//					try {
//						sleep(100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
		}
	}

	@Override
	public Port getPort(int i) {
		if (i >= ports.length)
			return null;
		else
			return ports[i];
	}

	@Override
	public boolean setPortConnection(int i, Port p) {
		if (i >= ports.length)
			return false;
		else
		{
			return ports[i].setOppo(p);
		}
	}

	@Override
	public int totalPorts() {
		// TODO Auto-generated method stub
		return ports.length;
	}
	
	private synchronized void updateCAMRecord(String mac, int port){
		if (!mac.toUpperCase().equals(MACGenerator.BROADCAST)) // if the source is not illegal
		{
			CAMrecord r = recordExist(mac, port); // check if record exist
			if (r != null) // if it does
			{
				r.UpdateAge(age); // update the age of it
			}
			else
				camTable.add(new CAMrecord(mac, (short)port, age, true)); // if it doesn't, add the record in
		}
	}
	
//	private synchronized void requestCAMupdate()
//	{
//		for (CAMrecord r : camTable)
//		{
//			if (r.isTimeForUpdate()) // if the source is not illegal
//			{
//				if (!r.isSent())
//				{
//					r.getMacAdd();
//					ports[r.getPort()].sendFrame(new Frame(ports[r.getPort()].getMacAdd(), r.getMacAdd(), ));
//				}
//			}
//		}
//	}
	
	private void cleanCAM()
	{
		for (CAMrecord r : camTable)
		{
			if (r.isExpired())
				camTable.remove(r);
		}
	}

	private int getAgingMilliSec() { return age * 1000; }
}