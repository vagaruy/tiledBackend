package code;


public class Protocol {
	
	// Define statements for start byte:
	// Define statements for start byte:
	static byte START=(byte)0x7E;

	static byte API_ID_1=(byte)0x00;
	
	//start of the frame IDs	
	//PING is the only command works without a serial no. when the device sends it serial
	//to the computer requesting.....
	static byte PING=(byte)0x00;  //-->Input.delimiter length apiid PING CHK_SUM Output format->delimiter length apiid OK_CMD PING serialNo IP_Address Chksum
	
	
	//Input-Delimiter Length APIID CHANGE_XY X_Value Y_value CHKSUM
	//OUTPUT-delimiter length apiid OK_CMD CHKSUM
	static byte CHANGE_XY=(byte)0x01;
	
	//INPUT-delimiter length apiid CHANGE_STATE statevalue CHKSUM
	//OUTPUT-delimiter length apiid OK_CMD CHKSUM
	static byte CHANGE_STATE=(byte)0x02;
	
	////inPUT-delimiter length apiid CHANGE_BRIGHT_ALL leds_to_toggle Intensity chksum
	//OUTPUT-delimiter length apiid OK_CMD CHKSUM
	static byte CHANGE_BRIGHTNESS_ALL=(byte)0x03;
	
	////INPUT-delimiter length apiid serialNo change_brightness_diff led1 int1 led2 int2 ... chksum
	//OUTPUT-delimiter length apiid OK_CMD CHKSUM
	static byte CHANGE_BRIGHTNESS_DIFF=(byte)0x04;
	
	//INPut-delimiter length apiid change_transition_all value chksum
	//OUTPUT-delimiter length apiid Ok_CMD chksum
	static byte CHANGE_TRANSITION_ALL=(byte)0x05;
	
	////INPUT-delimiter length apiid CHANGE_TRANSITION_DIFF led1 val1 led2 val2 ....chksum
	static byte CHANGE_TRANSITION_DIFF=(byte)0x06;
	
	//INPUT delimiter length apiid SET_ALARM TIME ACTION CHKSUM
	static byte SET_ALARM=(byte)0x07;
	
	//input delimiter length apiid SerialNo INFO chksum
	//output delimiter length apiid OK_CMD PING serialNo CURRENT_STATE LED1 brightness1 dimval1 led2 bri2 dimval2 .... any upcoming alarms each take byte CHKSUM
	static byte INFO=(byte)0x0F;
	
	
	// Define statements for commands:

	      // General Commands:
	static byte OK=(byte)0x00;
	static byte ERR=(byte)0x01;
	static byte END=(byte)0x02;

	// Define statements for Channel (Value A):

	// LEDS
	static byte ALL_LEDS=(byte)0x3F;
	static byte RED_LED=(byte)0x01;       
	static byte GREEN_LED =(byte)0x02;
	static byte BLUE_LED=(byte)0x04;
	static byte AMBER_LED=(byte)0x08;
	static byte COOLWHITE_LED=(byte)0x10;
	static byte WARMWHITE_LED=(byte)0x20;
	static byte INFRARED=(byte)0x40;
	
	// Locations:
	static byte START_LOCATION=0;
	static byte LENGTH_LOCATION=(byte)1;
	static byte APIID_LOCATION=2;
	static byte SERIAL_LOCATION=3;
	static byte COMMAND_LOCATION=4;
	

}
