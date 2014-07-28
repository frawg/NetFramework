package device.enddevices;
import java.util.ArrayList;

import netdata.Frame;
import netdata.FrameMovement;
import device.Device;
import device.elements.Port;

public class Hub extends Device {
	
	public Hub(String name, int bufferSize){
		super(name, 10, bufferSize);
	}

	@Override
	protected void initPorts(int bufferSize) {
		for (int i = 0; i < ports.length; i++)
		{
			ports[i] = new Port(this, bufferSize, (short) i) {
				
				@Override
				public void frameIsNotForThis(Frame f) {
					// TODO Auto-generated method stub
					//receiveFrame(f);
					receiveFrame(f, getIndex());
				}
				@Override
				public void frameIsForThis(Frame f) {} //there will never be a frame meant for this.
			};
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
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
				ProcessFrame(fm.getFrame(), fm.getPortIndex());
			}
		}
	}

	private synchronized void ProcessFrame(Frame f, short port) {
		for (int i = 0; i < ports.length; i++)
		{
			if (ports[i] != null && i != port)
				ports[i].sendFrame(f);
		}
	}

	@Override
	public boolean setPortConnection(int i, Port p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int totalPorts() {
		// TODO Auto-generated method stub
		return 0;
	}
}
