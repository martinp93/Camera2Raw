
package imageapi.tools;

import java.util.Random;
import java.awt.image.BufferedImage;
import imageapi.*;

public class Tools {

	/**
	 * Get the pixel matrix from a BufferedImage object
	 * @param image image
	 * @return      pixel matrix
	 */
	public static int[][] getPixelsFast(BufferedImage image) {
		int width = image.getWidth();
		int[] values = image.getRGB(0, 0, width, image.getHeight(), null, 0, width);
		return distributeRows(values, width);
	}


	public static int[][] distributeRows(int[] values, int width) {
		int[][] result = new int[values.length / width][width];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = values[(i * width) + j];
			}
		}
		return result;
	}

	public static int[][] from256toARGB(CompositeImage image) {
		return from256toARGB(image.getMatrix());
	}

  // Duplicates a single color channel into every channel (ARGB-format) Sets A to 255.
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

  // Generates random values for a single color channel
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

	// Generates random values for all color channels
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

	public static CompositeImage copy(CompositeImage source) {
		return new CompositeImage(source.getMatrix());
	}

  public static int[][] copy(int[][] array) {
		int[][] temp = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				temp[i][j] = array[i][j];
			}
		}
		return temp;
	}

	// Converts from linear to ARGB
	public static int[][] convertToARGB(double[][] array) {
		int[][] temp = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				temp[i][j] = fromLinearToARGB(array[i][j]);
			}
		}
		return temp;
	}

  // Only for a single color channel
	public static void displayArray(int[][] array, int channel) {
		System.out.print("\n");

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (((array[i][j] >> (channel * 8)) & 0xFF) < 10) {
					System.out.print(" 00" + ((array[i][j] >> (channel * 8)) & 0xFF) + " ");
				} else if (((array[i][j] >> (channel * 8)) & 0xFF) < 100) {
					System.out.print(" 0" + ((array[i][j] >> (channel * 8)) & 0xFF) + " ");
				} else {
					System.out.print(" " + ((array[i][j] >> (channel * 8)) & 0xFF) + " ");
				}
			}
			System.out.print("\n");
		}
	}

	// Converts the greyscale linear value [0 - 1], into ARGB-integer (duplicates greyscale value to RGB color channels).
	public static int fromLinearToARGB(double num) {
		int val = (int)(num * 255.0);
		return (255 << 24 | ((val & 0xFF) << 16) | ((val & 0xFF) << 8) | ((val) & 0xFF));
	}

	/**
	 * Maps a numerical value from one domain, with a set maximum value, to another.
	 * @param  in            value to map
	 * @param  fromDomain    current domain
	 * @param  toDomain      new domain
	 * @return               mapped value
	 */
	public static int mapper(int in, int fromDomain, int toDomain) {
		double ratio = ((double)(in + 1)/(double)fromDomain);
		return (int)(ratio * (double)toDomain) - 1;
	}

}
