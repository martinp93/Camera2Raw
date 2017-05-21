
package imageapi;

// IMPORT LIST
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Luminance extends ImageOperation {

	// CONSTANTS
  public static final float WEIGHT_RED = 0.2126f;
  public static final float WEIGHT_GREEN = 0.7152f;
  public static final float WEIGHT_BLUE = 0.0722f;

  public static final float THRESHOLD = 0.03928f;
  public static final float ALTERNATIVE_THRESHOLD = 0.04045f;

  public static final float A_VALUE = 0.055f;
  public static final float GAMMA = 2.4f;

  // USE THIS
  /*
  public static void luminance(CompositeImage image) {
		float red, green, blue, grey;
		int color;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				red = ((image.getPixel(j, i) >> 16) & 0xFF) / 255.0f;
				red = red < THRESHOLD ? red / 12.92f : (float)Math.pow((red + 0.055f) / 1.055f, 2.4f);
				green = ((image.getPixel(j, i) >> 8) & 0xFF) / 255.0f;
				green = green < THRESHOLD ? green / 12.92f : (float)Math.pow((green + 0.055f) / 1.055f, 2.4f);
				blue = ((image.getPixel(j, i)) & 0xFF) / 255.0f;
				blue = blue < THRESHOLD ? blue / 12.92f : (float)Math.pow((blue + 0.055f) / 1.055f, 2.4f);
				grey = ((WEIGHT_RED * red) + (WEIGHT_GREEN * green) + (WEIGHT_BLUE * blue)) * 255.0f;
				color = 255 << 24 | (int)grey << 16 | (int)grey << 8 | (int)grey;
				image.setPixel(j, i, color);
  		}
		}
  }
  */

  // Code for android SDK, won't compile
  /*
  public static CompositeImage luminance(CompositeImage bitmap) {
    int alpha = 0xFF000000;
    for (int i = 0; i < bitmap.getHeight(); i++) {
      for (int j = 0; j < bitmap.getWidth(); j++) {
        int rgb = bitmap.getPixel(j,i);
        double red = (rgb >> 16 & 0xFF) / 255.0;
        red = red < 0.03928 ? red / 12.92 : Math.pow((red + 0.055) / 1.055, 2.4);
        int redInt = (int)(WEIGHT_RED * red * 255.0);
       // System.out.println(redInt);
        double green = (rgb >> 8 & 0xFF) / 255.0;
        green = green < 0.03928 ? green / 12.92 : Math.pow((green + 0.055) / 1.055, 2.4);
        int greenInt = (int)(WEIGHT_GREEN * green * 255.0);
        // System.out.println(greenInt);
        double blue = (rgb & 0xFF) / 255.0;
        blue = blue < 0.03928 ? blue / 12.92 : Math.pow((blue + 0.055) / 1.055, 2.4);
        int blueInt = (int)(WEIGHT_BLUE * blue * 255.0);
        // System.out.println(blueInt;
        int grey = redInt + greenInt + blueInt;
        System.out.println("Grey before: " + grey);
        grey = (grey << 16) + (grey << 8) + (grey);
        System.out.println("Grey after: " + Integer.toHexString(grey));
        bitmap.setPixel(j, i, alpha + grey);
      }
    }
    return bitmap;
  }
  */

  public static CompositeImage luminance(CompositeImage img) {
    float[][] luminanceValues = new float[img.getHeight()][img.getWidth()];
    for (int i = 0; i < img.getHeight(); i++) {
      for (int j = 0; j < img.getWidth(); j++) {
        luminanceValues[i][j] = luminance(img.getPixel(j, i));
      }
    }
    return new CompositeImage(relativeLuminanceToColorDomain(luminanceValues));
  }

  // Copied and modified from Android.color API to work with Java.awt.Color-methods
  // Based on the formula for relative luminance defined in WCAG 2.0, W3C Recommendation 11 December 2008.
  public static float luminance(Color color) {
    return luminance(color.getRGB());
  }

  public static float luminance(int color) {
    Color RGB = new Color(color);
    double red = RGB.getRed() / 255.0;
    red = red < THRESHOLD ? red / 12.92 : Math.pow((red + 0.055) / 1.055, 2.4);
    double green = RGB.getGreen() / 255.0;
    green = green < THRESHOLD ? green / 12.92 : Math.pow((green + 0.055) / 1.055, 2.4);
    double blue = RGB.getBlue() / 255.0;
    blue = blue < THRESHOLD ? blue / 12.92 : Math.pow((blue + 0.055) / 1.055, 2.4);
    return (float) ((WEIGHT_RED * red) + (WEIGHT_GREEN * green) + (WEIGHT_BLUE * blue));
  }

  // Mapping straight from 0 - 255 to 0.0 - 1.0, no gamma correction or other correction method
  private static double[][] convertToLinear(int[][] imageValues) {
    double[][] result = new double[imageValues.length][imageValues[0].length];
    for (int i = 0; i < imageValues.length; i++) {
      for (int j = 0; j < imageValues[i].length; j++) {
        result[i][j] = (double)imageValues[i][j] / 255.0;
      }
    }
    return result;
  }

  // Based on the formula in the publication "How to interpret the sRGB color space (specified in IEC 61966-2-1) for ICC profiles"
  // double a = 0.055, gamma = 2.4;
  private static double[][] convertToLinear(int[][] imageValues, double a, double gamma) {
    double[][] result = new double[imageValues.length][imageValues[0].length];
    for (int i = 0; i < imageValues.length; i++) {
      for (int j = 0; j < imageValues[i].length; j++) {
        double val = (double)imageValues[i][j] / 255.0;
        result[i][j] = interpretSRGB(val, a, gamma);
      }
    }
    return result;
  }

  private static double interpretSRGB(double value, double a, double gamma) {
    if (value <= ALTERNATIVE_THRESHOLD) {
      return value / 12.92;
    } else {
      return Math.pow((value + a) / (1 + a), gamma);
    }
  }

  private static float[][] getRelativeLuminance(double[][] reds, double[][] greens, double[][] blues) {
    float[][] result = new float[reds.length][reds[0].length];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = (float)(WEIGHT_RED * reds[i][j] + WEIGHT_GREEN * greens[i][j] + WEIGHT_BLUE * blues[i][j]);
      }
    }
    return result;
  }

  private static int[][] relativeLuminanceToColorDomain(float[][] imageValues) {
    int[][] result = new int[imageValues.length][imageValues[0].length];
    for (int i = 0; i < imageValues.length; i++) {
      for (int j = 0; j < imageValues[i].length; j++) {
        result[i][j] = (int)(255 * imageValues[i][j]);
      }
    }
    return result;
  }

 	/*
   public static void main(String[] args) {
 		try {

 			System.gc();

 			long pre = System.nanoTime();
 			long beforeUsedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000;

 			System.out.println(beforeUsedMem + " kilobytes");

 			BufferedImage image1 = ImageIO.read(new File("media/testimage_small.jpg"));
 			CompositeImage composite1 = new CompositeImage(image1);

 			long from = System.nanoTime();

 			System.out.println((double)(from - pre) / (double)1000000000 + " seconds.");

 			// Luminance.luminance1(composite1);

 			long to = System.nanoTime();
 			long afterUsedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000;

 			System.out.println("\n" + "New:");
 			System.out.println((double)(to - from) / (double)1000000000 + " seconds.");
 			System.out.println(afterUsedMem - beforeUsedMem + " kilobytes");

 			composite1 = null; image1 = null;
 			System.gc();

 			afterUsedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000;

 			System.out.println();
 			System.out.println("Cleanup: " + afterUsedMem + " kilobytes");
 			System.out.println();

 			beforeUsedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000;

 			BufferedImage image2 = ImageIO.read(new File("media/testimage_small.jpg"));
 			CompositeImage composite2 = new CompositeImage(image2);

 			from = System.nanoTime();

 			// composite2 = Luminance.luminance2(composite2);

 			to = System.nanoTime();
 			afterUsedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000;

 			System.out.println("Old:");
 			System.out.println((double)(to - from) / (double)1000000000 + " seconds.");
 			System.out.println(afterUsedMem - beforeUsedMem + " kilobytes");


 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 	}
	*/
}
