package device.elements;

import netdata.Frame;

public class Connection {
	private Port[] ports = null;
	String medium, speed;
	//double speed;

	public Connection(Port p1, Port p2, String medium, String speed){
		ports = new Port[]{p1, p2};
		this.medium = medium;
		this.speed = speed;
	}
	
	public void sentToOpposite(Frame f, Port p){
		if (ports[0] == p)
			ports[1].ReceiveFrame(f);
		else
			ports[0].ReceiveFrame(f);
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}
	
}
