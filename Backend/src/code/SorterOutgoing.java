package code;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;



public class SorterOutgoing implements Runnable {
	
	private final ObjectOutputStream outStream;
	private final ArrayBlockingQueue<TiledMessage> sharedQueueRev;
	private Socket sock;
	

	public SorterOutgoing(Socket sock,ObjectOutputStream outStream,
			ArrayBlockingQueue<TiledMessage> sharedQueueRev)
	{
		// TODO Auto-generated constructor stub
		this.outStream=outStream;
		this.sharedQueueRev=sharedQueueRev;
		this.sock=sock;
	}
	
	public void send_msg()
	{
		if(sock!=null)
		{
		
				System.out.println("writing objects to stream yo");
				try {
					outStream.writeObject(sharedQueueRev.take());
					outStream.flush();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
				send_msg();
				Thread.sleep(100);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	

}
