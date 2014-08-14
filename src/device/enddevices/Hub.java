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
				public void frameIsNotForThis(Frame f) { receiveFrame(f, getIndex()); }
				@Override
				public void frameIsForThis(Frame f) {} //there will never be a frame meant for this.
			};
		}
	}
	
	@Override
	public void run() {
		super.run();
		while(!isInterrupted())
		{
			FrameMovement fm = null;
			try {
				fm = incoming.take();
			} catch (InterruptedException e) { }
			if (fm != null)
			{
				ProcessFrame(fm.getFrame(), fm.getPortIndex());
			}
		}
	}

	private synchronized void ProcessFrame(Frame f, short port) {
		for (short i = 0; i < ports.length; i++)
		{
			if (ports[i].isConnected() && i != port)
				ports[i].sendFrame(f);
		}
	}

	@Override
	public boolean setPortConnection(int i, Port p) { return ports[i].setOppo(p); }

	@Override
	public int totalPorts() { return ports.length; }
}
