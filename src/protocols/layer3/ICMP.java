package protocols.layer3;

import java.util.Random;

public class ICMP {

	public static String genID(){
			Random generator = new Random();
			int i = 0;
			String generated = "";
			for(int a = 0; a < 2; a++)
			{
				i = generator.nextInt(3);
				switch (i)
				{
				case 0: // numerical
					generated += (char)(generator.nextInt(10) + 48);
					break;
				case 1: // upper case alpha
					generated += (char)(generator.nextInt(26) + 65);
					break;
				case 2: // lower case alpha
					generated += (char)(generator.nextInt(26) + 97);
					break;
				default:  // defaults to upper case alpha
					generated += (char)(generator.nextInt(26) + 65);
					break;
				}
			}
			return generated;
	}
}
