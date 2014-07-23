package device;
import device.elements.*;

public interface Device extends Runnable {
	
	public void run();
	public void stop();
	public void resetInterrupted();
	public boolean isInterrupted();
	public NetIntModule getNetIntModule(int ind);
	public String getDeviceName();
	public void setDeviceName(String name);
	public String getHistory(int i);
	public int sizeOfHistory();
	public int totalPorts();
	public int totalNIMs();
	public int amountOfPorts(short ind);
	public boolean setPortOppo(Port p, short module, short port);
}

//public abstract class Device extends Thread{
//	private NetIntModule[] NIMs = null;
//	private String macAdd = null;
//	private String devName = null;
//	//private Queue<PacketMovement> receiveQueue = null;
//	//private ArrayList<LookupRecord> lookup = null;
//	private ArrayList<String> history = null;
//	
////	public Device(String name, int portNum){
////		this.devName = name;
////		this.NIMs = new NetIntModule[portNum];
////		this.history = new ArrayList<String>();
////		lookup = new ArrayList<LookupRecord>();
////	}
//	
//	public NetIntModule getNetIntModule(int ind){ return NIMs[ind]; }
//	public String getMacAdd(){ return this.macAdd; }
//	public void setMacAdd(String macAdd){ this.macAdd = macAdd; }
//	public String getDeviceName(){ return this.devName; }
//	public void setDeviceName(String name){ this.devName = name; }
//	public String getHistory(int i){ return history.get(i); }
//	public String peekHistory(){ return history.get(history.size() - 1); }
//	
//	public void setIP(String add, String gateway, String subnet, int module){
//		//this.ip = new IPv4(add, gateway, subnet);
//		this.NIMs[module].setIPv4(add, gateway, subnet);
//	}
//	
//	public abstract void run();
//	
//	//public abstract void ProcessPacket();
//		/*for (int i = 0; i < ports.length; i++){
//			//
//			while (ports[i].PeekQueue() != null)
//			{
//				//if (ip.getAddress().equals(ports[i].PeekQueue().getDestIP()))
//				if (ports[i].PeekQueue().getDestIP().equals(ip))
//				{
//					history.add("Packet received from " + ports[i].PeekQueue().getSourceIP() + " on port no." + i + ".");//receive the packet 
//					ports[i].PollQueue();//drop packet since we already received it
//					//put packet into history
//				}
//				else {
//					//learning
//					int temp = inOfRec(ports[i].PeekQueue().getSourceIP(), i);
//					if (temp == lookup.size())
//					{
//						lookup.add(new LookupRecord(ports[i].PeekQueue().getSourceIP(), LookupRecord.Type.DYNAMIC, (short)i));
//						System.out.println("added to lookup");
//					}
//					if (!ports[i].PeekQueue().decTime())
//						ports[i].PollQueue();//if packet expired, drop it
//					else
//					{
//						temp = inOfRec(ports[i].PeekQueue().getDestIP(), i);
//						//if record exist, just send to next dev
//						if (temp < lookup.size())
//						{
//							sendQueue.add(new PacketMovement(ports[lookup.get(temp).getPortID()].getOppositeIndex(), ports[lookup.get(temp).getPortID()].getOppositePortIndex(), ports[lookup.get(temp).getPortID()].PollQueue()));
//						}
//						//else flood network
//						else
//						{
//							for (int f = 0; f < ports.length; i++){
//								sendQueue.add(new PacketMovement(ports[f].getOppositeIndex(), ports[f].getOppositePortIndex(), ports[f].PollQueue()));
//							}
//						}
//					}
//				}
//			}
//		}
//		for (int i = 0; i < lookup.size(); i++){
//			if (!lookup.get(i).updTTL())
//				lookup.remove(i);
//		}*/
//	
//	//private abstract int inOfRec(String ip, int id);
//		/*for (int i = 0; i < lookup.size(); i++)
//		{
//			if (lookup.get(i).getIp().equals(ip) && lookup.get(i).getPortID() == id) return i;
//		}
//		return lookup.size(); */
//}