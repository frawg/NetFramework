package netdata;

import netdata.packet.IPv4Packet;
import netdata.packet.Packet;

public class Frame {
	public final static int IEEE802_3_LENGTH_FIELD = 0;
	public final static int EXPERIMENTAL = 0257;
	public final static int XEROX_PUP = 0512;
	public final static int PUP_Addr_Trans = 0513;
	public final static int XEROS_NS_IDP = 1536;
	public final static int IPv4 = 2048;
	public final static int X75 = 2049;
	public final static int NBS = 2050;
	public final static int ECMA = 2051;
	public final static int CHAOSNET = 2052;
	public final static int X25_LEVEL3 = 2053;
	public final static int ARP = 2054;
	public final static int XNS_COMPATABILITY = 2055;
	public final static int FRAME_RELAY_ARP = 2056;
	public final static int SYMBOLICS_PRIVATE = 2076;
	public final static int XYPLEX = 2184;
	public final static int UNGERMANN_BASS_NET_DEBUG = 2304;
	public final static int XEROX_IEEE802_PUP = 2560;
	public final static int IEEE802_PUP_Addr_Trans = 2561;
	public final static int BANYAN_VINES = 2989;
	public final static int VINES_loop = 2990;
	public final static int VINES_ECHO = 2991;
	public final static int BERKELEY_TRAIL_NEGO = 4096;
	public final static int BERKELEY_TRAIL_ENCAP = 4097;
	public final static int VALID_SYS = 5632;
	public final static int PCS_BASIC_BLOCK = 16962;
	public final static int BBN_SIMNET = 21000;
	public final static int DEC_unassigned = 24576;
	public final static int DEC_MOP_dump = 24577;
	public final static int DEC_MOP_remote_con = 24578;
	public final static int DEC_DECNET_PHASE4_ROUTE = 24579;
	public final static int DEC_LAT = 24580;
	public final static int DEC_DIAG = 24581;
	public final static int DEC_CUST = 24582;
	public final static int DEC_LAVC = 24583;
	public final static int DEC_unassigned2 = 24584;
	public final static int _3COM_CORP = 24592;
	public final static int TRANS_ETHER_BRIDGING = 25944;
	public final static int RAW_FRAME_RELAY = 25945;
	public final static int UNGERMANN_BASS_DOWNLOAD = 28672;
	public final static int UNGERMANN_BASS_LOOP = 28674;
	public final static int LRT = 28704;
	public final static int PROTEON = 28720;
	public final static int CABLETRON = 28724;
	public final static int CRONUS_VLN = 32771;
	public final static int CRONUS_DIRECT = 32772;
	public final static int HP_PROBE = 32773;
	public final static int NESTAR = 32774;
	public final static int ATNT = 32776;
	public final static int EXCELAN = 32784;
	public final static int SGL_DIAG = 32787;
	public final static int SGL_NET_GAMES = 32788;
	public final static int SGL_RESERVED = 32789;
	public final static int SGL_BOUNCED = 32790;
	public final static int APOLLO_DOMAIN = 32793;
	public final static int TYMSHARE = 32814;
	public final static int TIGAN = 32815;
	public final static int RARP = 32821;
	public final static int AEONIC_SYS = 32822;
	public final static int DEC_LANBridge = 32824;
	public final static int DEC_unassigned3 = 32825;
	public final static int DEC_ETHERNET_ENC = 32829;
	public final static int DEC_unassigned4 = 32830;
	public final static int DEC_LAN_TRAFFIC_MONITOR = 32831;
	public final static int DEC_unassigned5 = 32832;
	public final static int PLANNING_RESEARCH_CORP = 32836;
	public final static int ATNT2 = 32838;
	public final static int ATNT3 = 32839;
	public final static int EXPERDATA = 32841;
	public final static int IPv6 = 34525;
	public final static int LOOPBACK = 36864;
	
	private Packet payload;
	private String sourceMAC, destMAC;
	private int type;
	
	public Packet getPayload() { return payload; }
//	public void setPayload(Packet payload) {
//		this.payload = payload;
//	}
	public String getSourceMAC() { return sourceMAC; }
	public String getDestMAC() { return destMAC; }
	public int getEtherType() { return type; }
	
	public Frame(String source, String dest, int type, Packet pay){
		this.sourceMAC = source;
		this.destMAC = dest;
		this.type = type;
		this.payload = pay;
	}
}
