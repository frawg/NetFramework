package device.elements;

public class Connection {
	int deviceTo, deviceToPort, deviceFrom, deviceFromPort, connID;
	String medium, speed;
	//double speed;

	public Connection(int dev1, int dev1P, int dev2, int dev2P, String medium, String speed){
		this.deviceTo = dev1;
		this.deviceToPort = dev1P;
		this.deviceFrom = dev2;
		this.deviceFromPort = dev2P;
		this.medium = medium;
		this.speed = speed;
	}
	
	public int getDevice1() {
		return deviceTo;
	}

	public void setDevice1(int device1) {
		this.deviceTo = device1;
	}

	public int getDevice1P() {
		return deviceToPort;
	}

	public void setDeviceToP(int device1p) {
		deviceToPort = device1p;
	}

	public int getDevice2() {
		return deviceFrom;
	}

	public void setDevice2(int device2) {
		this.deviceFrom = device2;
	}

	public int getDevice2Port() {
		return deviceFromPort;
	}

	public void setDevice2Port(int device2p) {
		deviceFromPort = device2p;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}
	
}
