package code;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							System.out.println("INterupted exception caught here");
						    Thread.currentThread().interrupt();//preserve the message
						    return;//Stop doing whatever I am doing and terminate
							
						}
						try {
							outStream.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
		}catch(InterruptedException ex)
		{
			System.out.println("INterupted exception caught here");
		    Thread.currentThread().interrupt();//preserve the message
		    return;//Stop doing whatever I am doing and terminate
		}
		
	}
	

}
