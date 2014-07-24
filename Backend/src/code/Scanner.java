package code;

//First initialise this class with the range of ip addresses the program should search within...
//Keep the range short..I don't wnat to create seperate threads for scanning..a range of 100 or so should suffice.

//Next for each ip that is found,create a seperate thread and a place in the queue for it..

//Also try to implemenet this..this should goto sleep and wake up every minute or half minute to see 
//if new devices have been added and if old ones have gone silent..

//Inside the run method of this...it will run the method call scan which has all the functionalities init..
//Also after every run, it will sleep for quite some time before doing it again..
//Don't want this to be a hogger of thy resources.

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

public class Scanner implements Runnable {
	
	// Linked List of the queues of tiles..Initially contains nothing but after the tiles are discovered 
	//they will be added to the queue.
	private final LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueueFwd; 
	
	//messages from the tiled devices are routed back to the front end through this queue.
	//Just one queue .Sorting is done in the front end.
	private final ArrayBlockingQueue<TiledMessage> sharedQueueRev; // Playing with thread
	// unsafe versions
	// here...maybe fix
	// it later with
	// some other data
	// structure and
	// minimal change
	private final LinkedList<TileIndex> index;
	
	//Range of IP Addresses that the scanner takes. 
	InetAddress startip;
	InetAddress endip;

	
	//Constructor to initialise the queues and ip range.
	Scanner(String start, String end, LinkedList<ArrayBlockingQueue<TiledMessage>> queueFwd,ArrayBlockingQueue<TiledMessage> queueRev,
			LinkedList<TileIndex> index) throws UnknownHostException {
		this.index = index;
		this.sharedQueueFwd = queueFwd;
		this.sharedQueueRev = queueRev;
		startip = InetAddress.getByName(start);
		endip = InetAddress.getByName(end);
	}
	
	//Constructor using a default IP Range.
	Scanner(LinkedList<ArrayBlockingQueue<TiledMessage>> queueFwd,ArrayBlockingQueue<TiledMessage> queueRev, LinkedList<TileIndex> index)
			throws UnknownHostException {
		this.sharedQueueFwd = queueFwd;
		this.sharedQueueRev = queueRev;
		startip = InetAddress.getByName("10.0.0.1");
		endip = InetAddress.getByName("10.0.0.25");
		this.index = index;
	}

	public void scan() throws IOException, InterruptedException {
		final byte[] message = new byte[] { 0x7E, 0x04, 0x00, 0x00, 0x00, 0x7A };
		System.out.println("COntents of index is " + index.toString());
		LinkedList<TileIndex> index1 = new LinkedList<TileIndex>();
		// First creat a loop that will search from the start to the end address
		InetAddress addr = startip;
		while (!addr.equals(endip)) {
			byte match = 0;
			Iterator<TileIndex> ipIt = index.iterator();
			while (ipIt.hasNext()) {
				TileIndex t = (TileIndex) ipIt.next();
				if (t.ip.equals(addr)) {
				//	System.out.println("Please dont scan me.."+addr);
					match = 1;
					
				}

			}

			if (match == 0)// Send the message here and wait for the return
				// comand
			{

				// Type 1 is for checking alive status !??

				

				SocketConnection sock = new SocketConnection(addr, 8080);
				//String hex = DatatypeConverter.printHexBinary(message);
				//System.out.println("Sent message is" + hex);

				if (sock.isConnected() == 1) {
					System.out.println(addr.toString());
					byte rmesg[] = null;

					rmesg = sock.snd_recv(message);
					if (rmesg == null) {
						System.out.println("Not the right device...Skip me");
					}

					else if (rmesg.length > 0) {
					//	String hex1 = DatatypeConverter.printHexBinary(rmesg);
					//	System.out.println("Recieved message is" + hex1);

						MessageDecoder decode = new MessageDecoder(rmesg);
						//System.out.println(decode.message_decode());
						if (decode.message_decode() == 1) {
							 System.out.println("SerialNo is "+decode.SerialNo);
							 System.out.println("IP ADDRESS is "+decode.ip.toString());
							TileIndex tile = new TileIndex();
							tile.SerialNo = decode.SerialNo;
							tile.ip = addr;
							// add to the index and now time to spawn a new
							index1.add(tile);
							// New worker thread to be spawned here...
						}

					} else {
						System.out.println("Nothing recieved");

					}
				}

			}
			// increment the ip...for now only considering that the end ip only
			// differs in the last
			// to make it easier to calculate.
			// It will overflow after 255 to 0 so make sure the range only
			// differs in lowest order as of now..

			byte ipaddr[] = addr.getAddress();
			ipaddr[3] += 1;
			addr = InetAddress.getByAddress(ipaddr);
		}

		// index1.removeAll(index);
		if (!index1.isEmpty()) {
			Iterator<TileIndex> i1 = index1.iterator();
			while (i1.hasNext()) {

				ArrayBlockingQueue<TiledMessage> queueFwd = new ArrayBlockingQueue<TiledMessage>(
						100);
				sharedQueueFwd.add(queueFwd);
			//	sharedQueueRev.add(queueRev);
				TileIndex ind = (TileIndex) i1.next();
				index.add(ind);
				System.out.println("Finally making a new thread here for"
						+ "Serial No " + ind.SerialNo + ind.ip.toString());
				TileThreads tilethread = new TileThreads(sharedQueueFwd,sharedQueueRev,
						(ArrayBlockingQueue<TiledMessage>) queueFwd,index,
						ind.SerialNo, ind.ip);
				Thread thread = new Thread(tilethread);
				thread.start();
				// threads.add(thread);

			}
		}
		/*
		 * System.out.println("After creatign the threads");
		 * 
		 * System.out.println("THe index   made yet is "+index.toString());
		 * System.out.println("THe index  1 made yet is "+index1.toString());
		 * System.out.println("THe index  2 made yet is "+index2.toString());
		 */
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (true) {
			try {
				scan();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Scanning error man");
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Scanner Thread Sleep Interrupted.");
				e.printStackTrace();
			}
		}

	}
}
