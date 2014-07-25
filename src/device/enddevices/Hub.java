package device.enddevices;
import java.util.ArrayList;

import netdata.Frame;
import device.Device;
import device.elements.NetIntModule;
import device.elements.Port;

public class Hub extends Device {
	
	public Hub(String name){
		super(name, 8, 1);
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

	private void ProcessFrame(Frame f, short module, short port) {
		f.getPayload().decTime();
		for (int m = 0; m < NIMs.length; m++)
			for (int p = 0; p < NIMs[m].size(); p++)
				if (m != module && p != port)
					NIMs[m].portSend(p, f);
	}
}
