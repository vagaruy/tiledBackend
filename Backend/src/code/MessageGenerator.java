package code;

public class MessageGenerator {
	
	private byte[] message;
	private TiledMessage tile;
	
	MessageGenerator(TiledMessage msg)
	{
		this.tile=msg;
	}
	
	public void generate_message()
	{
		byte type=tile.getMessagetype();
		if(type==10)		//input delimiter length apiid SerialNo INFO chksum
			{byte info[]={Protocol.START,0x04,Protocol.API_ID_1,tile.getSerialNo(),Protocol.INFO,0x00};
			info[5]=Checksum.generateCheckSum(info,6);
			message=info;
			}
		else if((type>0)&&(type<7))	//setting only one led at a time....
		{
			byte intensity[]=tile.getIntensity();
			byte info[]={Protocol.START,0x06,Protocol.API_ID_1,tile.getSerialNo(),Protocol.CHANGE_BRIGHTNESS_DIFF,tile.messagetype,intensity[type-1],0x00};
			info[7]=Checksum.generateCheckSum(info,8 );
			message=info;
		}
		else if(type==8)	//INPut-delimiter length apiid serialno change_transition_all value chksum
		{
			byte info[]={Protocol.START,0x05,Protocol.API_ID_1,tile.getSerialNo(),Protocol.CHANGE_TRANSITION_ALL,tile.getDimspeed(),0x00};
			info[6]=Checksum.generateCheckSum(info,7);
			message=info;
		}
		else if(type==7)	////INPUT-delimiter length apiid serialno CHANGE_STATE statevalue CHKSUM
		{	
			if(!tile.isState()){
				 byte info[]={Protocol.START,0x05,Protocol.API_ID_1,tile.getSerialNo(),Protocol.CHANGE_STATE,(byte)0,0x00};
				 info[6]=Checksum.generateCheckSum(info,7);
				 message=info;
			}
			else
			{
				 byte info[]={Protocol.START,0x05,Protocol.API_ID_1,tile.getSerialNo(),Protocol.CHANGE_STATE,(byte)1,0x00};
				 info[6]=Checksum.generateCheckSum(info,7);
				 message=info;
				 
			}
			System.out.println("Message is"+message);
			 
			
		}
		else{
			System.out.println("Never seen this before yo");
		}
	}
	
	public byte[] getMessage()
	{
		generate_message();
		//System.out.println("message is "+message);
		return message;
	}
	
	
}
