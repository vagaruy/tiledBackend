package code;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

import org.bytedeco.javacv.*;
public class webcam implements Runnable {
	
	private final ArrayBlockingQueue<TiledMessage> msgqueue;
	int SerialNo;
	IplImage image;

	public webcam(int SerialNo, ArrayBlockingQueue<TiledMessage> msgqueue) {
		this.SerialNo=SerialNo;
		this.msgqueue=msgqueue;

	}
	@Override
	public void run() {
		//  public static void main (String args[])
		//{
		try {
			FrameGrabber grabber = FrameGrabber.createDefault(0); 

			int i=0;

			grabber.start();

			IplImage img;//=cvLoadImage("C:\\Users\\Testing\\Desktop\\image.jpg");
			// while (true) {
			img = grabber.grab();
			System.out.println("I am here atleast");


			System.out.println("Saving the iamge");
			//cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
			//cvSaveImage("C:\\Users\\Testing\\Desktop\\imag1e.jpg", img);
			// show image on window
			// canvas.showImage(img);
			CvRect r = new CvRect();
			r.x(img.height()-50);
			r.y(img.height()-50);
			r.width(100);
			r.height(100);

			cvSetImageROI(img,r);
			//IplImage cropped = cvCreateImage(cvGetSize(img), img.depth(), img.nChannels());
			//cvCopy(img, cropped);
			CvScalar c= cvAvg(img);
			//System.out.println(c.red()+" "+c.green()+" "+c.blue());
			ColorSpaceConversion convert=new ColorSpaceConversion();
			colorRgbw color=convert.rgbToRgbw((int)c.red(),(int)c.green(),(int)c.blue());
			System.out.println(color.red+" "+color.white+" "+color.green+" "+color.blue);
			send_msg(color.red,color.green,color.blue,color.white);

		} catch (Exception e) {
		}
	}

	private void send_msg(int red, int green, int blue, int white) {
		// TODO Auto-generated method stub

		try{

			TiledMessage tile=null;
			tile=new TiledMessage((byte)SerialNo);
			byte[] intensity = new byte[6];
			// System.out.println("Recieving something daily");
			tile.setMessagetype((byte) 1);
			intensity[0] = (byte) red;
			tile.setIntensity(intensity);

			msgqueue.add(tile);
			tile.setMessagetype((byte) 2);
			intensity[1] = (byte) green;
			tile.setIntensity(intensity);
			msgqueue.add(tile);
			tile.setMessagetype((byte) 3);
			intensity[2] = (byte) blue;
			tile.setIntensity(intensity);
			msgqueue.add(tile);
			tile.setMessagetype((byte) 5);
			intensity[4] = (byte) white;
			tile.setIntensity(intensity);
			msgqueue.add(tile);
			tile.setMessagetype((byte) 6);
			intensity[5] = (byte) white;
			tile.setIntensity(intensity);
			msgqueue.add(tile);
		}catch(Exception ex){
			System.out.println("Inside sort(TiledMessage) function.Nothing added yet!Index empty");
		}

	}

	/*public static void main(String args[])
{
	webcam cam=new webcam();
	Thread sort = new Thread(cam);
	sort.start();
}*/
}


