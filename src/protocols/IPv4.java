package protocols;

public class IPv4 {
	private String address, gateway, subnet;
	private Boolean DHCP;
	
	public String getAddress(){ return address; }
	public String getGateWay(){ return gateway; }
	public String getSubnet(){ return subnet; }
	public Boolean DHCPisEnabled(){ return DHCP; }
	
	public IPv4(String addr, String gateway, String subnet){
		this.address = addr;
		this.gateway = gateway;
		this.subnet = subnet;
	}
	
	public static Boolean valFields(String addr, String gateway, String subnet){
		return true;
	}
	
	public void setAddress(String add){
		this.address = add;
	}
	
	public void setGateway(String gateway){
		this.gateway = gateway;
	}
	
	public void setSubnet(String subnet){
		this.subnet = subnet;
	}
	
	public void DHCPToggle(){
		if (DHCP)
			DHCP = false;
		else
			DHCP = true;
	}
}
