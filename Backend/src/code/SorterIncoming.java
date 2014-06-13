package code;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;



public class SorterIncoming implements Runnable {
	
	private final ObjectInputStream inStream;
	private final LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueue;
	private final LinkedList<TileIndex> index;
	private Socket sock;
	Optitrack tracker;
	Thread track;

	public SorterIncoming(Socket sock,ObjectInputStream inStream, LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueue, LinkedList<TileIndex> index) {
		// TODO Auto-generated constructor stub
		this.inStream=inStream;
		this.sharedQueue=sharedQueue;
		this.index=index;
		this.sock=sock;	
		
	}
	
	private void sort(TiledMessage tile) {
		// TODO Auto-generated method stub
		try{
		TileIndex in;
		int SerialNo = (int) tile.SerialNo;
		Iterator<TileIndex> q = index.iterator();
		int pos = 0;
		boolean success = false;
		while (q.hasNext()) {
			in = (TileIndex) q.next();
			if (in.SerialNo == SerialNo) {
				System.out.println("Adding at position"+pos);
				System.out.println("Size of shared queue is"+sharedQueue.size());
				System.out.println("Size of the index is "+index.size());
				sharedQueue.get(pos).add(tile);
				
				success=true;
				break;
			}
			pos++;
		}
		if(!success)
			//if it is not a tiled message, then it could be a optitrack toggle message..check if the serial no is 999
			if(tile.SerialNo==127)
			{
				System.out.println("OPtitrack message recived");
				if(tile.isState()==true)
				{
					if(track==null)
					{
						tracker=new Optitrack(sharedQueue,index);
						track=new Thread(tracker);
					}
					if(!track.isAlive())
					{
						track.start();
					}
				}
				else
				{
					if(track.isAlive())
					{
						track.stop();
					}
					track=null;
				}
			}
		
		//color sensors message are given serial no 126
			else if(tile.SerialNo==126)
			System.out
				.println("This message has no available reciepent..FUck this message!!");
		}catch(Exception ex){
			System.out.println("Inside sort(TiledMessage) function.Nothing added yet!Index empty");
		}
	}
	
	private void recv_msg()
	{
		if(sock!=null)
		{
		try {
			TiledMessage message = null;
			message = (TiledMessage) inStream.readObject();

			if (message != null) {

				if(message instanceof TiledMessage)
				{
				System.out
						.println("A tile actually recieved with Serial No"
								+ message.getSerialNo());
				sort(message);
				}
				else
				{
					System.out.println("weird characters read");
				}
				
			} 
			
		
		}catch(Exception ex)
		{
			ex.printStackTrace();
			sock=null;
		}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("my birth");
		try{
			while(true){
				recv_msg();
				Thread.sleep(500);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	

}
