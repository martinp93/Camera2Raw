
package imageapi.tools;

import java.util.Random;
import imageapi.*;


/**
 * Class containing operations to be used by any class.
 */
public class Tools {

	/**
	 * Get the pixel matrix from a BufferedImage object.
	 * @param image image
	 * @return      pixel matrix
	 */
	/*
	public static int[][] getPixelsFast(BufferedImage image) {
		int width = image.getWidth();
		int[] values = image.getRGB(0, 0, width, image.getHeight(), null, 0, width);
		return distributeRows(values, width);
	}*/

	/**
	 * Converts a one-dimensional array of values into a two-dimensional matrix.
	 * @param  values  array of elements
	 * @param  width   width of resulting matrix
	 * @return         matrix of row-distributed elements
	 */
	public static int[][] distributeRows(int[] values, int width) {
		int[][] result = new int[values.length / width][width];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = values[(i * width) + j];
			}
		}
		return result;
	}

  /**
   * Duplicates a single color channel into every channel (ARGB-format).
   * <p>
   * Sets A to <code>255</code>.
   * @param  imageValues  matrix with single channel values
   * @return              integer pixel matrix
   */
	public static int[][] from256toARGB(int[][] imageValues) {
		int[][] result = new int[imageValues.length][imageValues[0].length];
		for (int i = 0; i < imageValues.length; i++) {
			for (int j = 0; j < imageValues[i].length; j++) {
				result[i][j] = imageValues[i][j];
				result[i][j] += (imageValues[i][j] << 8);
				result[i][j] += (imageValues[i][j] << 16);
				result[i][j] += (255 << 24);
			}
		}
		return result;
	}

  /**
   * Randomly generates an image with greyscale values.
   * <p>
   * Resembles a no-signal static image seen on televisions.
   * @param  width   width of image
   * @param  height  height of image
   * @return         random greyscale image
   */
	public static CompositeImage makeNoise(int width, int height) {
		int[][] array = new int[height][width];
		Random rng = new Random();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				array[i][j] = rng.nextInt(256);
			}
		}
		return new CompositeImage(from256toARGB(array));
	}

	/**
	 * Randomly generates an image with color values.
	 * @param  width         width of image
	 * @param  height        height of image
	 * @return               random color image
	 */
	public static CompositeImage makeColorfulNoise(int width, int height) {
		int[][] array = new int[height][width];
		Random rng = new Random();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				array[i][j] = (255 << 24);
				array[i][j] += (rng.nextInt(256) << 16);
				array[i][j] += (rng.nextInt(256) << 8);
				array[i][j] += rng.nextInt(256);
			}
		}
		return new CompositeImage(array);
	}

	/**
	 * Copies a composite image.
	 * @param  source        image to copy
	 * @return               copied image
	 */
	public static CompositeImage copy(CompositeImage source) {
		return new CompositeImage(source.getMatrix());
	}

	/**
	 * Copies a matrix.
	 * @param  matrix  matrix to copy
	 * @return         copied matrix
	 */
  public static int[][] copy(int[][] matrix) {
		int[][] copy = new int[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				copy[i][j] = matrix[i][j];
			}
		}
		return copy;
	}

  /**
   * Prints values from an integer pixel matrix.
   * <p>
   * Setting the <code>channel</code> parameter to <code>0, 1, 2 or 3</code> prints the corresponding channel A, R, G or B respectively.
   * <p>
   * Setting the <code>channel</code> parameter to 4 prints the values in hexadecimal.
   * @param matrix  the matrix to print
   * @param channel channel to print ('4' prints all in hex)
   */
	public static void printMatrix(int[][] matrix, int channel) {

		if (channel == 4) {
			System.out.print("\n");
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					System.out.print(Integer.toHexString(matrix[i][j]) + " ");
				}
				System.out.print("\n");
			}

		} else if (channel <= 3 && channel >= 0) {
			System.out.print("\n");

			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					if (((matrix[i][j] >> (channel * 8)) & 0xFF) < 10) {
						System.out.print(" 00" + ((matrix[i][j] >> (channel * 8)) & 0xFF) + " ");
					} else if (((matrix[i][j] >> (channel * 8)) & 0xFF) < 100) {
						System.out.print(" 0" + ((matrix[i][j] >> (channel * 8)) & 0xFF) + " ");
					} else {
						System.out.print(" " + ((matrix[i][j] >> (channel * 8)) & 0xFF) + " ");
					}
				}
				System.out.print("\n");
			}
		} else {
			System.out.println("Channel is out of range.");
		}
	}

	/**
	 * Converts a linearly valued matrix to a greyscale integer pixel matrix.
	 * @param  matrix  matrix with linear values
	 * @return         ARGB pixel matrix
	 */
	public static int[][] linearToARGB(float[][] matrix) {
		int[][] ARGB = new int[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				ARGB[i][j] = linearToARGB(matrix[i][j]);
			}
		}
		return ARGB;
	}

	/**
	 * Converts a linearly valued matrix to a greyscale integer pixel matrix.
	 * @param  matrix  matrix with linear values
	 * @return         ARGB pixel matrix
	 */
	public static int[][] linearToARGB(double[][] matrix) {
		int[][] ARGB = new int[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				ARGB[i][j] = linearToARGB(matrix[i][j]);
			}
		}
		return ARGB;
	}

	/** Converts the greyscale linear value <code>([0, 1])</code>, into an ARGB-integer pixel value.
	 *  <p>
	 *  Duplicates greyscale value to ARGB color channels.
	 *  @param  num  linear value
	 *  @return      ARGB-integer
	 */
	public static int linearToARGB(float num) {
		int val = (int)(num * 255.0);
		return (255 << 24 | ((val & 0xFF) << 16) | ((val & 0xFF) << 8) | ((val) & 0xFF));
	}

	/** Converts the greyscale linear value <code>([0, 1])</code>, into an ARGB-integer pixel value.
	 *  <p>
	 *  Duplicates greyscale value to ARGB color channels.
	 *  @param  num  linear value
	 *  @return      ARGB-integer
	 */
	public static int linearToARGB(double num) {
		int val = (int)(num * 255.0);
		return (255 << 24 | ((val & 0xFF) << 16) | ((val & 0xFF) << 8) | ((val) & 0xFF));
	}

	/**
	 * Maps a numerical value from one domain with a set maximum value, to another.
	 * @param  in            value to map
	 * @param  fromDomain    current domain
	 * @param  toDomain      new domain
	 * @return               mapped value
	 */
	public static int mapper(int in, int fromDomain, int toDomain) {
		double ratio = ((double)(in + 1)/(double)fromDomain);
		return (int)(ratio * (double)toDomain) - 1;
	}

	/**
	 * Maps all elements in a matrix from their domain into their linear representations.
	 * @param   matrix        matrix with values to map
	 * @param   fromDomain    domain
	 * @return                normalized matrix
	 */
	public static double[][] normalize(int[][] matrix, int fromDomain) {
		double[][] linearValues = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0 ; j < matrix[i].length; j++) {
				linearValues[i][j] = normalize(matrix[i][j], fromDomain);
			}
		}
		return linearValues;
	}

	/**
	 * Maps a value from its domain into a linear representation.
	 * @param   in            value to map
	 * @param   fromDomain    domain
	 * @return                normalized value
	 */
	public static double normalize(int in, int fromDomain) {
		return ((double)in / (double)fromDomain);
	}
}
