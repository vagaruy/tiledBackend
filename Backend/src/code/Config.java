/**
 * 
 */
package code;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Vipul Agarwal	
 *
 */
public class Config {

	/**
	 * @param args
	 */
	File file;
	public static final String filePathString="config.prop";
	
	public void load_prop()
	{
		File f = new File(filePathString);
		if(f.exists() && !f.isDirectory()) 
		{  
			//do nothing if a config.prop is already present 
		}
		else
		{
			System.out.println("New Properties Executing");
			//no configuration file present..make one with default values
			Properties prop = new Properties();
			OutputStream output = null;
			try {
		 		output = new FileOutputStream(filePathString);
				
		 		// set the properties value
				
				//either to use Conversion with Color Correction or Regular Conversion
				prop.setProperty("ColorSpaceConvertCorrection", "true");
				
				//Scanner IP Range
				prop.setProperty("Start IP","192.168.1.23");
				prop.setProperty("End IP","192.168.1.23");
				
				//Optitrack IP Address, Socket and Buffer Size
				prop.setProperty("Optitrack IP", "239.255.42.99");
				prop.setProperty("Optitrack Socket", "1511");
				prop.setProperty("Optitrack UDP Buffer Size","10000");
				prop.setProperty("Tiles","4");
				
				//Optitrack Tile Coordinates [x_beginning x_end Y_beginning Y_end]
				prop.setProperty("Optitrack Tile1 Coords","-1 0 0.5 1.5");
				prop.setProperty("Optitrack Tile2 Coords","-2 -1 0.5 1.5");
				prop.setProperty("Optitrack Tile3 Coords","-1 0 0.5 -1.5");
				prop.setProperty("Optitrack Tile4 Coords","-2 -1 0.5 -1.5");
				prop.setProperty("Optitrack Tile5 Coords","-2 -1 0.5 -1.5");
				prop.setProperty("Optitrack Tile6 Coords","-2 -1 0.5 -1.5");
				prop.setProperty("Optitrack Tile7 Coords","-2 -1 0.5 -1.5");
				prop.setProperty("Optitrack Tile8 Coords","-2 -1 0.5 -1.5");
				prop.setProperty("Optitrack Tile9 Coords","-2 -1 0.5 -1.5");
				prop.setProperty("Optitrack Tile10 Coords","-2 -1 0.5 -1.5");
				
				//Sleep Time after each scan is complete 
				prop.setProperty("Scanner Sleep Time","5000");//in milliseconds
				
				//Sleep time for various other applications.Most of the sleep
				//times are optimized for the test bed.Changes might result in dropped packets or
				//some other weird characters.
				prop.setProperty("Socket Connections Timeout","3000");
				prop.setProperty("Socket Recieve Waittime","30");
				prop.setProperty("Sorter Connection Fail Sleeptime","4000");
				prop.setProperty("Frontend->Backend Sorter Sleeptime","500");
				prop.setProperty("Backend->Frontend Sorter Sleeptime","100");
				
				//Port Number of the tiled device
				prop.setProperty("Tiles Port Number","8080");
				prop.setProperty("Tile Sleep Time","1000");
								 
				// save properties to project root folder
				prop.store(output, null);
		 
			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		 
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Config config=new Config();
		System.out.println("Executing....");
		config.load_prop();
	}

}
