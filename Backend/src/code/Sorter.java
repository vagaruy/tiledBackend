package code;

//This is where all the incoming messages are routed to specific queues...acts as a producer of sorts I guess..

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sorter implements Runnable {

	private ServerSocket listener=null;
	private Socket sock=null;
	private final int port;
	ObjectInputStream inStream = null;
	ObjectOutputStream outStream = null;
	Thread incThread;
	Thread outThread;

	private final LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueue;
	private final ArrayBlockingQueue<TiledMessage> sharedQueueRev;
	private final LinkedList<TileIndex> index;
	private AtomicBoolean flag;

	Sorter(LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueue,ArrayBlockingQueue<TiledMessage> sharedQueueRev, LinkedList<TileIndex> index,
			int port) {
		this.sharedQueue = sharedQueue;
		this.sharedQueueRev=sharedQueueRev;
		this.index = index;
		this.port = port;
		flag=new AtomicBoolean();
		flag.set(true);
		incThread=null;
		outThread=null;
}

	private void optitrack_sort() {

	}

	private void frontend_sort() {

		System.out.println("frontend sorter up and running yo");
		sock=null;
		if ((sock == null) || (!sock.isConnected())) {
				System.out.println("Socket got dead");

				try {
					listener = new ServerSocket(port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Frontend & Sorter Connection Failed");
					e.printStackTrace();
				}
				System.out.println("Listener acquired");
				try {
					sock = listener.accept();
				} catch (IOException e) {
					System.out.println("Some kind of Front end socket error");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Socket acquired");
				try {
					outStream = new ObjectOutputStream(sock.getOutputStream());
					//outStream.flush();
					inStream = new ObjectInputStream(sock.getInputStream());

				} catch (IOException e) {
					System.out.println("Socket fron end input stream problem");
					e.printStackTrace();
				}
				System.out.println("Stream acquired");
			}
			
			
			
			
		}
	

	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		System.out.println("Sorter thread birth");
		while(true)
		{
			//if((sock == null) || (!sock.isConnected()))
			//{
			if(flag.get())
			{
				System.out.println("restarting all the threads yo");
				if(incThread==null)
				{
				
				}
				else if(incThread.isAlive())
				{
					incThread.interrupt();
					//incThread.stop();
				}
				if(outThread==null)
				{
				
				}
				else if(outThread.isAlive())
				{
				outThread.interrupt();
				//outThread.stop();
				}
				start_threads();
				flag.set(false);
			}
			
				/*frontend_sort();
				System.out.println("Socket connection failed in front end check....");*/
			//}else
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("Sorter disturbed not cool bro!");
					e.printStackTrace();
				}
				
		}//

	}

	private void start_threads() {
		// TODO Auto-generated method stub
		frontend_sort();
		SorterIncoming inc=new SorterIncoming(flag,sock,inStream,sharedQueue,index);
		SorterOutgoing out=new SorterOutgoing(sock,outStream,sharedQueueRev);
		incThread=new Thread(inc);
		outThread=new Thread(out);
		incThread.start();
		outThread.start();
		
	}

	/*public static void main(String args[]) {
		Sorter sort = new Sorter(null,null, null, 6969);
		Thread t = new Thread(sort);
		t.start();

	}*/

}
