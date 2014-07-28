package protocols;

import java.util.ArrayList;

import protocols.layer2.DataProtocol;

public class ProtocolMap{
	private ArrayList<DataProtocol> protocols = null;
	
	public ProtocolMap(){
		protocols = new ArrayList<DataProtocol>();
	}
	
	public DataProtocol findProtocol(int hex){
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
