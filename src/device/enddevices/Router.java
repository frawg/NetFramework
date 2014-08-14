package device.enddevices;

import java.util.ArrayList;

import device.Device;
import device.elements.Port;

public class Router extends Device {

	public Router(String name, int portNum, int bufferSize) {
		super(name, portNum, bufferSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean setPortConnection(int i, Port p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void initPorts(int bufferSize) {
		// TODO Auto-generated method stub
		
	}
}
