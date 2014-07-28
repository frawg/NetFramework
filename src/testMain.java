import java.util.ArrayList;

import netdata.NetEvent;
import netdata.NetEventListener;
import device.Device;
import device.enddevices.Computer;
import device.enddevices.Switch;


public class testMain {

	public static void main(String []args)
	{
		NetEventListener list = new NetEventListener() {
			@Override
			public void frameSent(NetEvent e) {
				// TODO Auto-generated method stub
				System.out.println(e.getSource().getDeviceName() + " - " + e.getAction());
			}
			@Override
			public void frameReceived(NetEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void frameProcessed(NetEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void frameDropped(NetEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		ArrayList<Device> dev = new ArrayList<Device>();
		Computer c1 = new Computer("PC1", 20);
		c1.getPort(0).setIPv4("192.168.1.1", "", "255.255.255.0");
		System.out.println("Computer1 initialized");
		
		Computer c2 = new Computer("PC2", 20);
		c2.getPort(0).setIPv4("192.168.1.2", "", "255.255.255.0");
		System.out.println("Computerq initialized");
		
		Switch sw1 = new Switch("SW1");
		sw1.setPortConnection(0, c1.getPort(0));
		sw1.setPortConnection(1, c2.getPort(0));
		System.out.println("Switch initialized");
		
		c1.setPortConnection(0, sw1.getPort(0));
		c2.setPortConnection(0, sw1.getPort(1));
		
		c1.start();
		System.out.println("Computer1 started");
		c2.start();
		System.out.println("Computer2 started");
		sw1.start();
		System.out.println("Switch started");
		
		c1.ping("192.168.1.2");
		System.out.println("Computer1 ping");
		
		try {
			c1.interrupt(60);
			System.out.println("Computer1 interrupted");
			c2.interrupt(60);
			System.out.println("Computer2 interrupted");
			sw1.interrupt(60);
			System.out.println("Switch interrupted");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
