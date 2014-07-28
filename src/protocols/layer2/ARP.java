package protocols.layer2;

import java.util.ArrayList;

import netdata.ARPrecord;

public class ARP extends DataProtocol {
	private ArrayList<ARPrecord> arpCache = null;
	
	public ARP(int code) {
		super(code);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
	}
	
	
}
