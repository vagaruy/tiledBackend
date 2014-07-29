package code;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 
 */

/**
 * @author Vipul Agarwal
 *
 */
public class Main {

	/**
	 * @param args
	 */
	
	private static String startIP;
	private static String endIP;
	
	Main(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.prop");
			// load a properties file
			prop.load(input);
			startIP=prop.getProperty("Start IP");
			endIP=prop.getProperty("End IP");
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Basic Data structure we will be working on......
		
		//THis is problematic..Gotta use some thread safe version of this yo!!!
		LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueueFwd=new LinkedList<ArrayBlockingQueue<TiledMessage>>();
		ArrayBlockingQueue<TiledMessage> sharedQueueRev=new ArrayBlockingQueue<TiledMessage>(100);
		LinkedList<TileIndex> index=new LinkedList<TileIndex>();
		try {
			System.out.println("Are we here yet ");
			Scanner scanner = new  Scanner(startIP,endIP,sharedQueueFwd,sharedQueueRev,index);
			//Optitrack tracker=new Optitrack(sharedQueueFwd,index);
			Sorter sort = new Sorter(sharedQueueFwd, sharedQueueRev, index,6969);
			Thread sortThread = new Thread(sort);
			Thread scanThread=new Thread(scanner); 
		//	Thread trackThread=new Thread(tracker);
			scanThread.start(); 
			sortThread.start();
		//	trackThread.start();
		} catch (UnknownHostException e) {
			System.out.println("Scanner thread problemo..");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
