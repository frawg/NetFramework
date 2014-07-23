package protocols;

import java.util.ArrayList;

public class ProtocolMap{
	private ArrayList<Protocol> protocols = null;
	
	public ProtocolMap(){
		protocols = new ArrayList<Protocol>();
	}
	
	public Protocol findProtocol(int hex){
		if(protocols.size() > 0)
		{
			for (int i = 0; i < protocols.size(); i++)
			{
				if (protocols.get(i).getProtocolCode() == hex)
					return protocols.get(i);
			}
		}
		return null;
	}
}
