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

public class Sorter implements Runnable {

	private ServerSocket listener=null;
	private Socket sock=null;
	private final int port;
	ObjectInputStream inStream = null;
	ObjectOutputStream outStream = null;

	private final LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueue;
	private final ArrayBlockingQueue<TiledMessage> sharedQueueRev;
	private final LinkedList<TileIndex> index;

	Sorter(LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueue,ArrayBlockingQueue<TiledMessage> sharedQueueRev, LinkedList<TileIndex> index,
			int port) {
		this.sharedQueue = sharedQueue;
		this.sharedQueueRev=sharedQueueRev;
		this.index = index;
		this.port = port;
}

	private void optitrack_sort() {

	}

	private void frontend_sort() {

		System.out.println("frontend sorter up and running yo");
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
		
		frontend_sort();
		
		
		SorterIncoming inc=new SorterIncoming(sock,inStream,sharedQueue,index);
		SorterOutgoing out=new SorterOutgoing(sock,outStream,sharedQueueRev);
		Thread incThread=new Thread(inc);
		Thread outThread=new Thread(out);
		incThread.start();
		outThread.start();
		while(true)
		{
			if(sock==null)
				frontend_sort();
			else
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("Sorter disturbed not cool bro!");
					e.printStackTrace();
				}
		}

	}

	/*public static void main(String args[]) {
		Sorter sort = new Sorter(null,null, null, 6969);
		Thread t = new Thread(sort);
		t.start();

	}*/

}
