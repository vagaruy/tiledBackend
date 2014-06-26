/**
 * 
 */
package code;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Testing
 * 
 */
public class Optitrack implements Runnable {
	private final LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueueFwd;
	private final LinkedList<TileIndex> index;
	static int SerialNo[];
	MulticastSocket s;

	public Optitrack(
			LinkedList<ArrayBlockingQueue<TiledMessage>> sharedQueueFwd,
			LinkedList<TileIndex> index) {
		// TODO Auto-generated constructor stub
		this.sharedQueueFwd = sharedQueueFwd;
		this.index = index;
		
	}
	
	

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	public void shutdown()
	{
		try{
			s.close();
		}catch(Exception e){
			System.out.println("Multicast socket caught here");
		}
	}
	
	public void start_track() {
		InetAddress group;
		int major = 0;
		byte[] buf = new byte[10000];
		
	
			try {
				group = InetAddress.getByName("239.255.42.99");
				s = new MulticastSocket(1511);
				s.joinGroup(group);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Have we joined the multicast group yet or still waiting on it yo?");
			while (true) {

				// get their responses!
				if(Thread.currentThread().isInterrupted())
				{
					Thread.currentThread().interrupt();
					return;
				}
				DatagramPacket recv = new DatagramPacket(buf, buf.length);
				try {
					s.receive(recv);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				

				// System.out.println(buf);
				// System.out.println("Begin Packet-----");

				int ptr = 0;
				// message ID
				int MessageID = 0;
				// MessageID=(int)bb.getShort(0);
				MessageID = (short) (((buf[ptr + 1] & 0xFF) << 8) | (buf[ptr] & 0xFF));

			//	 System.out.println("Message ID :"+ MessageID);
				ptr += 2;
				// size
				int nBytes = 0;
				nBytes = (short) (((buf[ptr + 1] & 0xFF) << 8) | (buf[ptr] & 0xFF));
				ptr += 2;
				// System.out.println("Byte count :"+ nBytes);
				if (MessageID == 7) {
					int frameNumber = 0;
					frameNumber = buf[ptr + 3] << 24
							| (buf[ptr + 2] & 0xFF) << 16
							| (buf[ptr + 1] & 0xFF) << 8 | (buf[ptr] & 0xFF);
					ptr += 4;
					// System.out.println("Frame Number is "+frameNumber);
					// number of data sets (markersets, rigidbodies, etc)
					int nMarkerSets = 0;
					nMarkerSets = buf[ptr + 3] << 24
							| (buf[ptr + 2] & 0xFF) << 16
							| (buf[ptr + 1] & 0xFF) << 8 | (buf[ptr] & 0xFF);
					ptr += 4;
					// System.out.println("Marker Set Count : "+ nMarkerSets);

					for (int i = 0; i < nMarkerSets; i++) {
						// Markerset name
						byte szName[] = Arrays.copyOfRange(buf, ptr, ptr + 255);

						int nDataBytes = (int) szName.length + 1;
						ptr += nDataBytes;
						// System.out.println("Model Name:"+ szName);

						// marker data
						int nMarkers = 0;
						nMarkers = buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF);
						ptr += 4;
						// System.out.println("Marker Count : "+ nMarkers);

						for (int j = 0; j < nMarkers; j++) {
							float x = 0;
							x = Float.intBitsToFloat(buf[ptr + 3] << 24
									| (buf[ptr + 2] & 0xFF) << 16
									| (buf[ptr + 1] & 0xFF) << 8
									| (buf[ptr] & 0xFF));

							ptr += 4;
							float y = 0;
							y = Float.intBitsToFloat(buf[ptr + 3] << 24
									| (buf[ptr + 2] & 0xFF) << 16
									| (buf[ptr + 1] & 0xFF) << 8
									| (buf[ptr] & 0xFF));
							ptr += 4;
							float z = 0;
							z = Float.intBitsToFloat(buf[ptr + 3] << 24
									| (buf[ptr + 2] & 0xFF) << 16
									| (buf[ptr + 1] & 0xFF) << 8
									| (buf[ptr] & 0xFF));
							ptr += 4;
							// System.out.println("Marker Number="+j+" X="+x+" Y="+y+" Z="+z);
						}
					}

					// unidentified markers
					int nOtherMarkers = 0;
					nOtherMarkers = buf[ptr + 3] << 24
							| (buf[ptr + 2] & 0xFF) << 16
							| (buf[ptr + 1] & 0xFF) << 8 | (buf[ptr] & 0xFF);
					ptr += 4;
					// System.out.println("Unidentified Marker Count : "+
					// nOtherMarkers);
					for (int j = 0; j < nOtherMarkers; j++) {
						float x = 0;
						x = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
						float y = 0;
						y = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
						float z = 0;
						z = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
						// System.out.println("Marker Number="+j+" X="+x+" Y="+y+" Z="+z);
					}
					// rigid bodies
					int nRigidBodies = 0;
					nRigidBodies = buf[ptr + 3] << 24
							| (buf[ptr + 2] & 0xFF) << 16
							| (buf[ptr + 1] & 0xFF) << 8 | (buf[ptr] & 0xFF);
					ptr += 4;
					
					float x[]=new float[nRigidBodies];
					float z[]=new float[nRigidBodies];
			//	 System.out.println("Rigid Body Count :"+ nRigidBodies);
					for (int j = 0; j < nRigidBodies; j++) {
						// rigid body pos/ori
						int ID = 0;
						ID = buf[ptr + 3] << 24 | (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF);
						ptr += 4;
						//float x = 0;
						x[j] = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
						float y = 0;
						y = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
						//float z = 0;
						z[j] = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
						float qx = 0;
						qx = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
						float qy = 0;
						qy = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
						float qz = 0;
						qz = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
						float qw = 0;
						qw = Float.intBitsToFloat(buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF));
						ptr += 4;
					//System.out.println("ID : "+ ID);
						
					// System.out.println("pos:X="+ x+" Y="+y+" Z="+z);
						// System.out.println("ori: "+qx+" "+qy+" "+qz+" "+qw);

						// associated marker positions
						int nRigidMarkers = 0;
						nRigidMarkers = buf[ptr + 3] << 24
								| (buf[ptr + 2] & 0xFF) << 16
								| (buf[ptr + 1] & 0xFF) << 8
								| (buf[ptr] & 0xFF);
						ptr += 4;
						// System.out.println("Marker Count:"+ nRigidMarkers);
						float markerData[] = new float[nRigidMarkers * 3];
						for (int i = 0; i < nRigidMarkers * 3; i++) {
							markerData[i] = Float
									.intBitsToFloat(buf[ptr + 3] << 24
											| (buf[ptr + 2] & 0xFF) << 16
											| (buf[ptr + 1] & 0xFF) << 8
											| (buf[ptr] & 0xFF));
							ptr += 4;
						}

						if (major >= 2) {
							// associated marker IDs
							int markerIDs[] = new int[nRigidMarkers];
							for (int i = 0; i < nRigidMarkers; i++) {
								markerIDs[i] = buf[ptr + 3] << 24
										| (buf[ptr + 2] & 0xFF) << 16
										| (buf[ptr + 1] & 0xFF) << 8
										| (buf[ptr] & 0xFF);
								ptr += 4;
							}

							// associated marker sizes

							float markerSizes[] = new float[nRigidMarkers];
							for (int i = 0; i < nRigidMarkers * 3; i++) {
								markerSizes[i] = Float
										.intBitsToFloat(buf[ptr + 3] << 24
												| (buf[ptr + 2] & 0xFF) << 16
												| (buf[ptr + 1] & 0xFF) << 8
												| (buf[ptr] & 0xFF));
								ptr += 4;
							}

							for (int k = 0; k < nRigidMarkers; k++) {
								// System.out.println("Marker "+k+" id="+markerIDs[k]+" tsize="+markerSizes[k]+" tpos= "+
								// markerData[k*3]+markerData[k*3+1]+markerData[k*3+2]);
							}
						} else {
							for (int k = 0; k < nRigidMarkers; k++) {
								// System.out.println("\tMarker :"+k+" pos = "+
								// markerData[k*3]+
								// markerData[k*3+1]+markerData[k*3+2]);
							}
						}

					} // next rigid body
					find_tile(x,z,nRigidBodies);

				}

			
		
			}
	}

	private  void find_tile(float[] x, float[] z, int nRigidBodies)  {

		
		int serial[]=new int[nRigidBodies];
		for(int i=0;i<nRigidBodies;i++)
		{
		// TODO Auto-generated method stub
		if ((x[i] > -1) && (x[i] < 0) && (z[i] > 0.5) && (z[i] < 1.5))
			serial[i] = 1;
		else if ((x[i] > -2) && (x[i] < -1) && (z[i] > 0.5) && (z[i] < 1.5))
			serial[i] = 2;
		else if ((x[i] > -1) && (x[i] < 0) && (z[i] < 0.5) && (z[i] > -1.5))
			serial[i] = 3;
		else if ((x[i] > -2) && (x[i] < -1) && (z[i] < 0.5) && (z[i] > -1.5))
			serial[i] = 4;
		}
		if(!Arrays.equals(SerialNo,serial))
		{
			SerialNo=serial.clone();
			send_msg();
			System.out.println("CUrrent tile is" + SerialNo);
		}
		

	}

	private void send_msg()  {
		// TODO Auto-generated method stub
		
		
			TileIndex in;
			TiledMessage tile=null;
			Iterator<TileIndex> q = index.iterator();
			int pos = 0;
			boolean success = false;
			while (q.hasNext()) {
				in = (TileIndex) q.next();
				int flag=0;
				for(int i=0;i<SerialNo.length;i++)
				{
				if (in.SerialNo == SerialNo[i]) {
					tile=new TiledMessage((byte)in.SerialNo);
					tile.setMessagetype((byte) 7);
					tile.setState(true);
					break;
					
				}
				else
				{
					flag=1;
				}
				}
				if(flag==1)
				{
					tile=new TiledMessage((byte)in.SerialNo);
					tile.setMessagetype((byte)7);
					tile.setState(false);
				}
				System.out.println("Adding at position"+pos);
				System.out.println("Size of shared queue is"+sharedQueueFwd.size());
				System.out.println("Size of the index is "+index.size());
				sharedQueueFwd.get(pos).add(tile);
				pos++;
			}
			
		
	}

	@Override
	public void run()  {
		// TODO Auto-generated method stub
			
			
				start_track();
			
			

	}

}
