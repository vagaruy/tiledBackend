package code;

import java.io.Serializable;

public class TiledMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 550863553689975755L;
	byte messagetype;
	byte SerialNo;			//identifier for a tile
	boolean colors[]=new boolean[6];		//state of each color
	byte intensity[]=new byte[6];	//intensity of each color...pwm i guess???
	boolean state;					//switch on off the entire tile
	long time;						//to sync time on the devices and use same variable for setting future alarms.
	byte dimspeed;					//for some dimming effects on the devices
	
	public TiledMessage(byte Serial)
	{
		this.SerialNo=Serial;
		this.colors=null;
		this.intensity=null;
		this.state=false;
		this.time=0;
		this.dimspeed=0;
		this.messagetype=0; 
		
	}

    public byte getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(byte messagetype) {
        this.messagetype = messagetype;
    }

    public byte getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(byte SerialNo) {
        this.SerialNo = SerialNo;
    }

    public boolean[] getColors() {
        return colors;
    }

    public void setColors(boolean[] colors) {
        this.colors = colors;
    }

    public byte[] getIntensity() {
        return intensity;
    }

    public void setIntensity(byte[] intensity) {
        this.intensity = intensity;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public byte getDimspeed() {
        return dimspeed;
    }

    public void setDimspeed(byte dimspeed) {
        this.dimspeed = dimspeed;
    }

}
