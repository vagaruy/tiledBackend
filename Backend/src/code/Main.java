package code;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 */

/**
 * @author vagar
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Basic Data structure we will be working on......
		
		
		//BlockingQueue<TiledMessage> queue = new ArrayBlockingQueue<TiledMessage>(100);
		
		
		
		//THis is problematic..Gotta use some thread safe version of this yo!!!
		LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueueFwd=new LinkedList<ArrayBlockingQueue<TiledMessage>>();
		ArrayBlockingQueue<TiledMessage> sharedQueueRev=new ArrayBlockingQueue<TiledMessage>(100);
		LinkedList<TileIndex> index=new LinkedList<TileIndex>();
		
		
		try {
			System.out.println("Are we here yet ");
			Scanner scanner = new  Scanner("192.168.1.202","192.168.1.213",sharedQueueFwd,sharedQueueRev,index);
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