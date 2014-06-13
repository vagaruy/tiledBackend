package code;

public class Checksum {
	
	static byte generateCheckSum(byte buffer[], int length){ 
		  byte sendSum=0;
		  for(int j=0; j<length-1; j++){
		    sendSum^= buffer[j];
		  }
		  return sendSum;
		}
	
	static boolean checkChecksum(byte buffer[])
	{	
		byte sum=0;
		for (int j=0; j<buffer.length-1; j++){
		    sum ^=buffer[j];
		  }
		  byte check = buffer[buffer.length-1]; //This is correct

		  if(sum==check)
			  return true;
		  else
			  return false;
	}
	
}
