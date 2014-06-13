package code;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;


public class SocketConnection {
	private Socket socket;
	private InetAddress ip;
	private int port;
	
	SocketConnection(InetAddress addr,int conport) throws IOException 
	{
		ip=addr;
		port=conport;
			
	}
	
	public int isConnected() throws IOException
	{
		try {
			socket=new Socket();
			SocketAddress sockaddr=new InetSocketAddress(ip,port);
			socket.connect(sockaddr,3000);
			} catch (SocketException e) {
				return 0;
			} catch (IOException e) {
				return 0;
			}
			if(socket.isConnected())
				return 1;			
		return 0;	
	}
	private void write(byte[] msg,BufferedOutputStream out) throws IOException
	{
		
		out.write(msg);
		out.flush();
		
	}
	
	private byte[] read(BufferedInputStream in) throws IOException, InterruptedException
	{	
		
		byte[] msg=null;
		int count=in.available()-1;
		
		while(count<in.available())
		{
			count=in.available();
			Thread.sleep(20);
		}
		if(in.available()>0)
		{
			msg=new byte[in.available()];
			in.read(msg);			
		}
		return msg;		
	}
	
	public byte[] snd_recv(byte[] msg) throws IOException, InterruptedException
	{	
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in= new BufferedInputStream(socket.getInputStream());
		byte rmesg[]=new byte[50];
		//System.out.println("Ip address is "+ip);
		write(msg,out);
		Thread.sleep(30);
		rmesg=read(in);
		return rmesg;						
	}
	
	
	public void close() throws IOException
	{
		socket.close();
	}
}
