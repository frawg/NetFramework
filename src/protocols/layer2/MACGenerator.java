package protocols.layer2;

import java.util.ArrayList;
import java.util.Random;

public class MACGenerator {
	private static ArrayList<String> usedMAC = new ArrayList<String>();
	public static void add(String mac){ usedMAC.add(mac); }
	public static final String BROADCAST = "FF.FF.FF.FF.FF.FF";
	
	public static Boolean checkExist(String mac)
	{
		for (int i = 0; i < usedMAC.size(); i++)
		{
			if (usedMAC.equals(mac))
				return true;
		}
		return false;
	}
	
	private static String randMac()
	{
		Random generator = new Random();
		int i = 0;
		String generatedMac = "";
		for(int a=0;a<6;a++)
		{
			for(int b=0; b<2;b++)
			{
				i = generator.nextInt(16);
				generatedMac= generatedMac.concat(Integer.toHexString(i));
			}
			generatedMac= generatedMac.concat(".");
		}
		generatedMac = generatedMac.substring(0, generatedMac.length()-1);
		generatedMac = generatedMac.toUpperCase();
		return generatedMac;
	}
	
	public static String generateMAC(){
		String mac = null;
		do {
			mac = randMac();
		}while (checkExist(mac));
		addMAC(mac);
		return mac;
	}
	
	private static void addMAC(String mac){
		usedMAC.add(mac);
	}
}
