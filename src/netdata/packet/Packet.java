package netdata.packet;

import netdata.Payload;

public class Packet {
	public final static int  HOPOPT = 0;
	public final static int  ICMP = 1;
	public final static int  IGMP = 2;
	public final static int  GGP = 3;
	public final static int  IP_in_IP = 4;
	public final static int  ST = 5;
	public final static int  TCP = 6;
	public final static int  CBT = 7;
	public final static int  EGP = 8;
	public final static int  IGP = 9;
	public final static int  BBN_RCC_MON = 10;
	public final static int  NVPII = 11;
	public final static int  PUP = 12;
	public final static int  ARGUS = 13;
	public final static int  EMCON = 14;
	public final static int  XNET = 15;
	public final static int  CHAOS = 16;
	public final static int  UDP = 17;
	public final static int  MUX = 18;
	public final static int  DCN_MEAS = 19;
	public final static int  HMP = 20;
	public final static int  PRM = 21;
	public final static int  XNSIDP = 22;
	public final static int  TRUNK1 = 23;
	public final static int  TRUNK2 = 24;
	public final static int  LEAF1 = 25;
	public final static int  LEAF2 = 26;
	public final static int  RDP = 27;
	public final static int  IRTP = 28;
	public final static int  ISOTP4 = 29;
	public final static int  NETBLT = 30;
	public final static int  MFENSP = 31;
	public final static int  MERITINP = 32;
	public final static int  DCCP = 33;
	public final static int  T3PC = 34;
	public final static int  IDPR = 35;
	public final static int  XTP = 36;
	public final static int  DDP = 37;
	public final static int  IDPR_CMTP = 38;
	public final static int  TPPP = 39;
	public final static int  IL = 40;
	public final static int  IPv6 = 41;
	public final static int  SDRP = 42;
	public final static int  IPv6_ROUTE = 43;
	public final static int  IPv6_FLAG = 44;
	public final static int  IDRP = 45;
	public final static int  RSVP = 46;
	public final static int  GRE = 47;
	public final static int  DSR = 48;
	public final static int  BNA = 49;
	public final static int  ESP = 50;
	public final static int  AH = 51;
	public final static int  INLSP = 52;
	public final static int  SWIPE = 53;
	public final static int  NARP = 54;
	public final static int  MOBILE = 55;
	public final static int  TLSP = 56;
	public final static int  SKIP = 57;
	public final static int  IPv6_ICMP = 58;
	public final static int  IPv6_NONXT = 59;
	public final static int  IPv6_OPTS = 60;
	public final static int  INTERNAL_HOST_PROTOCOL = 61;
	public final static int  CFTP = 62;
	public final static int  INTERNAL_HOST_NET = 63;
	public final static int  SAT_EXPAK = 64;
	public final static int  KRYPTOLAN = 65;
	public final static int  RVD = 66;
	public final static int  IPPC = 67;
	public final static int  DISTRIBUTED_FS = 68;
	public final static int  SAT_MON = 69;
	public final static int  VISA = 70;
	public final static int  IPCV = 71;
	public final static int  CPNX = 72;
	public final static int  WSN = 73;
	public final static int  PVP = 74;
	public final static int  BR_SAT_MON = 75;
	public final static int  SUN_ND = 76;
	public final static int  WB_MON = 77;
	public final static int  WB_EXPAK = 78;
	public final static int  ISO_IP = 79;
	public final static int  VMTP = 80;
	public final static int  SECURE_VMTP = 81;
	public final static int  VINES = 82;
	public final static int  TTP = 83;
	public final static int  IPTM = 84;
	public final static int  NSFNET_IGP = 85;
	public final static int  DGP = 86;
	public final static int  TCF = 87;
	public final static int  EIGRP = 88;
	public final static int  OSPFIGP = 89;
	public final static int  SPITERPC = 90;
	public final static int  LARP = 91;
	public final static int  MTP = 92;
	public final static int  AX25 = 93;
	public final static int  IPIP = 94;
	public final static int  MICP = 95;
	public final static int  SCC_SP = 96;
	public final static int  ETHERIP = 97;
	public final static int  ENCAP = 98;
	public final static int  PRIVATE_ENCRYPTION = 99;
	public final static int  GMTP = 100;
	public final static int  IFMP = 101;
	public final static int  PNNI = 102;
	public final static int  PIM = 103;
	public final static int  ARIS = 104;
	public final static int  SCPS = 105;
	public final static int  QNX = 106;
	public final static int  AN = 107;
	public final static int  IPComp = 108;
	public final static int  SNP = 109;
	public final static int  COMPAQ_PEER = 110;
	public final static int  IPX_IN_IP = 111;
	public final static int  VRRP = 112;
	public final static int  PGM = 113;
	public final static int  ANY_0_HOP = 114;
	public final static int  L2TP = 115;
	public final static int  DDX = 116;
	public final static int  IATP = 117;
	public final static int  STP = 118;
	public final static int  SRP = 119;
	public final static int  UTI = 120;
	public final static int  SMP = 121;
	public final static int  SM = 122;
	public final static int  PTP = 123;
	public final static int  ISIS_IPv4 = 124;
	public final static int  FIRE = 125;
	public final static int  CRTP = 126;
	public final static int  CRUDP = 127;
	public final static int  SSCOPMCE = 128;
	public final static int  IPLT = 129;
	public final static int  SPS = 130;
	public final static int  PIPE = 131;
	public final static int  SCTP = 132;
	public final static int  FC = 133;
	public final static int  RSVP_E2E_IGNORE = 134;
	public final static int  MOBILITY = 135;
	public final static int  UDPLITE = 136;
	public final static int  MPLS_IN_IP = 137;
	public final static int  MANET = 138;
	public final static int  HIP = 139;
	public final static int  SHIM6 = 140;
	public final static int  WESP = 141;
	public final static int  ROHC = 142;
	public final static int  EXPERIMENTAL = 253;
	public final static int  EXPERIMENTAL2 = 254;
	public final static int  RESERVERD = 255;
	
	protected String sourceIP, destIP = null;
	protected Payload payload = null;
	protected short TTL;
	
	public Packet(String source, String dest, short TTL, Payload payload){
		this.sourceIP = source;
		this.destIP = dest;
		this.payload = payload;
		this.TTL = TTL;
	}

	public String getSourceIP() { return sourceIP; }
	public String getDestIP() { return destIP; }
	public Payload getPayload() { return payload; }
	
	public Boolean decTime(){
		if (TTL < 1)
			return false;
		else
		{
			this.TTL -= 1;
			return true;
		}
	}
}
