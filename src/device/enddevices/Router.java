package device.enddevices;

import java.util.ArrayList;

import device.Device;
import device.elements.Port;

public class Router extends Device {

public Router(String addr, String gateway, String subnet, String name,
			int portNum, Type type) {
		super(addr, gateway, subnet, name, portNum, type);
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public ArrayList<Port> getPorts() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void setPorts(ArrayList<Port> ports) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public String getMacAdd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMacAdd(String macAdd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public short getType() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void setType(short type) {
//		// TODO Auto-generated method stub
//		
//	}

}
