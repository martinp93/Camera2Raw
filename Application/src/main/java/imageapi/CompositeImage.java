
package imageapi;


import android.graphics.Bitmap;
import android.widget.Toast;

import imageapi.tools.Tools;
import imageapi.tools.BayerConversionManager;

/**
 * Wrapper class for matrices containing integer pixel values. Contains various operations relevant to image processing.
 */
public class CompositeImage implements ImageHolder {

  // CompositeImage variable "pixelMatrix" should ALWAYS contain values in ARGB-format
  /**
   * Matrix holding the integer pixel values.
   */
  private int[][] pixelMatrix;

  /**
   * Width of image.
   */
  private int width;

  /**
   * Height of image.
   */
  private int height;

  /**
   * The total number of pixels in the image.
   */
  private int numPixels;

/* Android*/
  public CompositeImage(Bitmap bitmap) {
    width = bitmap.getWidth();
    height = bitmap.getHeight();
    numPixels = width * height;
    int[] tabell=new int[numPixels];

    bitmap.getPixels(tabell,0,width,0,0,width,height);
    pixelMatrix = Tools.distributeRows(tabell,width);
  }


  // CONSTRUCTORS
  public CompositeImage(int[][] matrix) {
    width = matrix[0].length;
    height = matrix.length;
    numPixels = width * height;

    pixelMatrix = Tools.copy(matrix);
  }

  public CompositeImage(int[] values, int width) {
		this.width = width;
		height = values.length / width;
		numPixels = values.length;

		pixelMatrix = Tools.distributeRows(values, width);
	}
/*
  public CompositeImage(BufferedImage image) {
    width = image.getWidth();
    height = image.getHeight();
    numPixels = width * height;

    pixelMatrix = Tools.getPixelsFast(image);
  }
*/
  public CompositeImage(BayerImage rawImage) {
    width = rawImage.getWidth() / 2;
    height = rawImage.getHeight() / 2;
    numPixels = width * height;

    pixelMatrix = BayerConversionManager.composeImage(rawImage);
  }

  public CompositeImage(double[][] matrix) {
		width = matrix[0].length;
		height = matrix.length;
    numPixels = width * height;

    pixelMatrix = Tools.linearToARGB(matrix);
	}

  // METHODS

  /**
   * Get the integer pixel matrix.
   * @return pixelMatrix matrix
   */
  public int[][] getMatrix() {
    return pixelMatrix;
  }

  /**
   * Set a new integer pixel matrix for this image.
   * <p>
   * Updates width, height and numPixels for this object
   * @param swap new matrix
   */
  public void setComposite(int[][] swap) {
    pixelMatrix = null;
    pixelMatrix = swap;
    width = swap[0].length;
    height = swap.length;
    numPixels = width * height;
  }

  /**
   * Get pixel at specified coordinate.
   * @param  x x coordinate
   * @param  y y coordinate
   * @return   element at (x,y)
   */
  public int getPixel(int x, int y) {
    return pixelMatrix[y][x];
  }

  /**
   * Set a new pixel value at specified coordinate.
   * @param x     x coordinate
   * @param y     y coordinate
   * @param value new pixel value
   */
  public void setPixel(int x, int y, int value) {
    pixelMatrix[y][x] = value;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  /**
   * Get the total number of pixels in the image.
   * <p>
   * Same as multiplying the width with the height.
   * @return number of pixels
   */
  public int getNumberOfPixels() {
    return numPixels;
  }

/* Android
  public static int[][] getPixelsFast(Bitmap bitmap) {
    int[] values = Bitmap.getPixels(bitmap, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
    return distributeRows(values, bitmap.getWidth());
  }
*/

  /**
   * Extract color component matrix from image.
   * <p>
   * Note: Returns as a two-dimensional integer array with values from 0 to 255.
   * @param component   component: 0 - A | 1 - R | 2 - G | 3 - B
   * @return            integer matrix
   */
  public int[][] getColorChannel(int component) {
    int result[][] = new int[height][width];

    int bitShift = 24 - (8 * component);

    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = (pixelMatrix[i][j] >> bitShift) & 0xFF;
      }
    }
    return result;
  }

  /**
   * Gets a one-dimensional array of elements from the two-dimensional integer pixel matrix.
   * @return array of integer pixel elements
   */
  public int[] getArray() {
    int[] values = new int[numPixels];
    for (int i = 0; i < numPixels; i++) {
      values[i] = pixelMatrix[i / width][i % width];
    }
    return values;
  }

  /**
   * Adjust brightness of image by value.
   * @param   value         adjustment value
   * @return                brightnessadjusted image
   */
  public CompositeImage adjustBrightness(int value) {
		int[][] result = Tools.copy(pixelMatrix);
    int bitPos = 16;

    for (int x = 0; x < 3; x++) {
      for (int i = 0; i < pixelMatrix.length; i++) {
        for (int j = 0; j < pixelMatrix[i].length; j++) {
          if (((((pixelMatrix[i][j] >> bitPos) & 0xFF) + value)) > 255) {
            result[i][j] += (255 - ((pixelMatrix[i][j] >> bitPos) & 0xFF)) << bitPos;
          } else if (((((pixelMatrix[i][j] >> bitPos) & 0xFF) + value)) < 0) {
            result[i][j] -= ((pixelMatrix[i][j] >> bitPos) & 0xFF) << bitPos;
          } else {
            result[i][j] += (value << bitPos);
          }
        }
      }
      bitPos -= 8;
    }
    return new CompositeImage(result);
  }

  /**
   * Raise each value in the image to the power of the specified factor.
   * <p>
   * Raises each color component individually before merging them into a resulting pixel.
   * <p>
   * This is in principle the same as an gamma adjustment with the factor inversed.
   * @param  factor        power to raise to
   * @return               exponentiated image
   */
	public CompositeImage exponentiate(double factor) {
		int[][] result = new int[getHeight()][getWidth()];
		double redNum = 0.0, greenNum = 0.0, blueNum = 0.0;

		int[][] reds = getColorChannel(1);
		int[][] greens = getColorChannel(2);
		int[][] blues = getColorChannel(3);

		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				redNum = Math.pow((double)reds[i][j] / 255.0, factor) * 255;
				greenNum = Math.pow((double)greens[i][j] / 255.0, factor) * 255;
				blueNum = Math.pow((double)blues[i][j] / 255.0, factor) * 255;

				int color = ((255 << 24) + ((int)redNum << 16) + ((int)greenNum << 8) + (int)blueNum);
				result[i][j] = color;
			}
		}
		return new CompositeImage(result);
	}

  /**
   * Adjust gamma of image by factor.
   * @param   factor        factor to adjust with
   * @return                gamma-adjusted image
   */
  public CompositeImage adjustGamma(double factor) {
		int[][] result = new int[getHeight()][getWidth()];
		double redNum = 0.0, greenNum = 0.0, blueNum = 0.0;

		int[][] reds = getColorChannel(1);
		int[][] greens = getColorChannel(2);
		int[][] blues = getColorChannel(3);

		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				redNum = Math.pow((double)reds[i][j] / 255.0, (1/factor)) * 255;
				greenNum = Math.pow((double)greens[i][j] / 255.0, (1/factor)) * 255;
				blueNum = Math.pow((double)blues[i][j] / 255.0, (1/factor)) * 255;

        int color = ((255 << 24) + ((int)redNum << 16) + ((int)greenNum << 8) + (int)blueNum);
				result[i][j] = color;
			}
		}
		return new CompositeImage(result);
	}

  /**
   * Calculates the average value of the component specified.
   * @param   channel       component: 0 - A | 1 - R | 2 - G | 3 - B
   * @return                average value of component (0 - 255)
   */
  public int getAverageColor(int channel) {
    int[][] colorChannel = getColorChannel(channel);
    int sum = 0;
    for (int i = 0; i < colorChannel.length; i++) {
      for (int j = 0; j < colorChannel[i].length; j++) {
        sum += colorChannel[i][j];
      }
    }
    sum /= numPixels;
    return sum;
  }
}
