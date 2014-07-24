package code;

/*
 * This class performs the color space conversion.
 * The Values are converted from RGB to RGBW depending on the saturation.
 * A method for conversion with color correction is also provided.
 * Doesnt affect the amber value of the tiled device which is far more complicated to include in the equation!
 * Methods are mostly self explanatory!
*/

public class ColorSpaceConversion {



	// The saturation is the colorfulness of a color relative to its own brightness.
	 int saturation(colorRgbw rgbw) {
	    // Find the smallest of all three parameters.
	    float low = Math.min(rgbw.red, Math.min(rgbw.green, rgbw.blue));
	    // Find the highest of all three parameters.
	    float high = Math.max(rgbw.red, Math.max(rgbw.green, rgbw.blue));
	    // The difference between the last two variables
	    // divided by the highest is the saturation.
	    
	    return Math.round(100 * ((high - low) / high));
	}
	 
	// Returns the value of White
	int getWhite(colorRgbw rgbw) {
		
	    return (255 - saturation(rgbw)) / 255 * (rgbw.red + rgbw.green + rgbw.blue) / 3;
	}
	 
	// Use this function for too bright emitters. It corrects the highest possible value.
	 int getWhite(colorRgbw rgbw, int redMax, int greenMax, int blueMax) {
	    // Set the maximum value for all colors.
	    rgbw.red = (int) ((float)rgbw.red / 255.0 * (float)redMax);
	    rgbw.green = (int) ((float)rgbw.green / 255.0 * (float)greenMax);
	    rgbw.blue = (int) ((float)rgbw.blue / 255.0 * (float)blueMax);
	    return (255 - saturation(rgbw)) / 255 * (rgbw.red + rgbw.green + rgbw.blue) / 3;
	 }
	 
	// Conversion function
	colorRgbw rgbToRgbw( int red, int green,  int blue) {
	    colorRgbw rgbw = new colorRgbw();
	    rgbw.red=red;
	    rgbw.green=green;
	    rgbw.blue=blue;
	    rgbw.white = getWhite(rgbw);
	    return rgbw;
	}
	 
	// Conversion function with color correction.
	colorRgbw rgbToRgbw( int red, int redMax,
	                     int green,  int greenMax,
	                     int blue,  int blueMax) {
	     
	     colorRgbw rgbw = new colorRgbw();
	     rgbw.red=red;
		    rgbw.green=green;
		    rgbw.blue=blue;
	    rgbw.white = getWhite(rgbw, redMax, greenMax, blueMax);
	    return rgbw;
	}
	
	
}