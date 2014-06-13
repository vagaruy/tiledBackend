package code;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MessageDecoder {
	
	private byte message[];
	byte messagetype;
	InetAddress ip;
	byte SerialNo;
	TiledMessage tile;
	public MessageDecoder(byte[] rmesg) {
		message=rmesg;
		System.out.println(message);
	}
	
	private boolean message_sanity()
	{
		//check if the message has the right delimiter
		if(!(message[0]==Protocol.START))
		{
			System.out.println("Start failed");
			return false;
		}
		
		//check if the message has the right length
	//	if(!(message[1]==(message.length-2)))
//		{
	//		System.out.println("Length failed");
//			return false;
	//	}
		//check if the message has the right checksum
		if(!Checksum.checkChecksum(message))
		{
			System.out.println("Checksum failed");
			return false;
		}
		
		if(!(message[3]==0))
		{
			System.out.println("Serial Failed");
			return false;
		}
		return true;		
	}
	
	private int message_decode_ping()
	{
		
		SerialNo=message[5];
		byte ip1[]=new byte[]{message[6],message[7],message[8],message[9]};
		try {
			ip=InetAddress.getByAddress(ip1);
		} catch (UnknownHostException e) {
		System.out.println("This should not happen at all!!");
		e.printStackTrace();
		return -2;
		}
		return 1;
	}
	
	public int message_decode()
	{
		if(!message_sanity())
		{
			return -1;
		}
		messagetype=message[4];	
		switch (message[4])
		{
		case 0x00:
			return(message_decode_ping());
			
		case 0x02:
			return(message_state());
			
		case 0x04:
			return(message_brightness());
			
		case 0x05:
			return(message_transition());
			
		case 0x0F:
			return(message_status());			
			
		default:
			System.out.println("Message decoding for this tyoe not yet impleemnted");
		}
		return -3;
			
	}

	public TiledMessage getTile() {
		return tile;
	}

	private int message_status() {
		// TODO Auto-generated method stub
		try{
			
		
		SerialNo=message[5];
		tile=new TiledMessage(SerialNo);
		tile.setMessagetype((byte) 10);
		byte []intensity={message[7],message[8],message[9],message[10],message[11],message[12]};
		tile.setIntensity(intensity);
		tile.setDimspeed(message[13]);
		if(message[6]==1)
			tile.setState(true);
		else
			tile.setState(false);
		}catch(Exception ex)
		{
			return -2;
		}
		return 1;
	}

	private int message_transition() {
		// TODO Auto-generated method stub
		tile.setMessagetype((byte) 8);
		return 1;
	}

	private int message_brightness() {
		// TODO Auto-generated method stub
		tile.setMessagetype((byte) 6);
		return 1;
	}

	private int message_state() {
		// TODO Auto-generated method stub
		tile.setMessagetype((byte) 7);
		return 1;
	}
}
