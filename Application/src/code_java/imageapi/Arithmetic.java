
package imageapi;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * The Arithmetic class contains methods for arithmetic operations on CompositeImages and matrices.
 */
public class Arithmetic extends ImageOperation {

	/**
	 * Sums each integer element pair in two identically dimensioned images.
	 * <p>
	 * Calculation: image1 value + image2 value
	 * @param  image1        first image term
	 * @param  image2        second image term
	 * @return               sum of both images
	 */
	public static CompositeImage add(CompositeImage image1, CompositeImage image2) {
		return new CompositeImage(add(image1.getMatrix(), image2.getMatrix()));
	}

	/**
	 * Sums each integer element pair in two identically dimensioned matrices.
	 * <p>
	 * Calculation: array1 value + array2 value
	 * @param  array1        first matrix term
	 * @param  array2        second matrix term
	 * @return               sum of these
	 */
	public static int[][] add(int[][] array1, int[][] array2) {
		if (array1.length != array2.length || array1[0].length != array2[0].length) {
			System.out.println("ImageOperations.add(): array1 and array2 are not equal in size");
			return null;
		} else {
			int[][] temp = new int[array1.length][array1[0].length];

			int bitPos = 16;
			for (int x = 0; x < 3; x++) {
				for (int i = 0; i < array1.length; i++) {
					for (int j = 0; j < array1[i].length; j++) {
						if ((((array2[i][j] >> bitPos) & 0xFF) + ((array1[i][j] >> bitPos) & 0xFF)) > 255) {
							temp[i][j] += (255 << bitPos);
						} else {
							temp[i][j] += ((array2[i][j] >> bitPos) & 0xFF) + ((array1[i][j] >> bitPos) & 0xFF) << bitPos;
						}
					}
				}
				bitPos -= 8;
			}
			return temp;
		}
	}

	/**
	 * Find the difference for each integer element pair in two identically dimensioned images.
	 * <p>
	 * Calculation: image1 value - image2 value
	 * @param  image1        first image term
	 * @param  image2        second image term
	 * @return               sum of both images
	 */
	public static CompositeImage subtract(CompositeImage image1, CompositeImage image2) {
		return new CompositeImage(subtract(image1.getMatrix(), image2.getMatrix()));
	}

	/**
	 * Find the difference for each integer element pair in two identically dimensioned matrices.
	 * <p>
	 * Calculation: array1 value - array2 value
	 * @param  array1        minuend matrix
	 * @param  array2        subtrahend matrix
	 * @return               difference matrix
	 */
	public static int[][] subtract(int[][] array1, int[][] array2) {
		if (array1.length != array2.length || array1[0].length != array2[0].length) {
			System.out.println("ImageOperations.add(): array1 and array2 are not equal in size");
			return null;
		} else {
			int[][] temp = new int[array1.length][array1[0].length];

			int bitPos = 16;
			for (int x = 0; x < 3; x++) {
				for (int i = 0; i < array1.length; i++) {
					for (int j = 0; j < array1[i].length; j++) {

						if (((array1[i][j] >> bitPos) & 0xFF) - ((array2[i][j] >> bitPos) & 0xFF) < 0) {
							temp[i][j] += (0 << bitPos);
						} else {
							temp[i][j] += ((array1[i][j] >> bitPos) & 0xFF) - ((array2[i][j] >> bitPos) & 0xFF) << bitPos;
						}
					}
				}
				bitPos -= 8;
			}
			return temp;
		}
	}

	/**
	 * Multiply each integer element pair in the two identically dimensioned images.
	 * <p>
	 * Calculation: image1 value * image2 value
	 * @param  composite1    first factor image
	 * @param  composite2    second factor image
	 * @return               product image
	 */
	public static CompositeImage multiply(CompositeImage composite1, CompositeImage composite2) {
		return new CompositeImage(multiply(composite1.getMatrix(), composite2.getMatrix()));
	}

	/**
	 * Multiply each integer element pair in the two identically dimensioned matrices.
	 * <p>
	 * Calculation: array1 value * array2 value
	 * @param  array1        first factor matrix
	 * @param  array2        second factor matrix
	 * @return               product matrix
	 */
	public static int[][] multiply(int[][] array1, int[][] array2) {
		if (array1.length != array2.length || array1[0].length != array2[0].length) {
			System.out.println("ImageOperations.add(): array1 and array2 are not equal in size");
			return null;
		} else {
			int[][] result = new int[array1.length][array1[0].length];
			double temp;
			int bitPos = 16;
			for (int x = 0; x < 3; x++) {
				for (int i = 0; i < array1.length; i++) {
					for (int j = 0; j < array1[i].length; j++) {
						temp = (((array1[i][j] >> bitPos) & 0xFF) / 255.0) * (((array2[i][j] >> bitPos) & 0xFF) / 255.0);
						result[i][j] += (((array1[i][j] >> bitPos) & 0xFF) * ((array2[i][j] >> bitPos) & 0xFF)) << bitPos;
					}
				}
				bitPos -= 8;
			}
			return result;
		}
	}

	/**
	 * Multiply each integer element in the image matrix with a factor.
	 * <p>
	 * Calculation: image value * factor
	 * @param  composite     multiplicand image
	 * @param  factor        multiplier value
	 * @return               product image
	 */
	public static CompositeImage multiply(CompositeImage composite, double factor) {
			return new CompositeImage(multiply(composite.getMatrix(), factor));
	}

	/**
	 * Multiply each integer element with a factor.
	 * <p>
	 * Calculation: array value * factor
	 * @param  array         multiplicand matrix
	 * @param  factor        multiplier value
	 * @return               product matrix
	 */
	public static int[][] multiply(int[][] array, double factor) {
		int[][] result = new int[array.length][array[0].length];
		double temp;
		int bitPos = 16;
		for (int x = 0; x < 3; x++) {
			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < array[i].length; j++) {
					// If the value goes over 255, set it to 255. THIS MIGHT SCREW WITH THE ALGORITHM //
					if ((((array[i][j] >> bitPos) & 0xFF) / 255.0) * factor > 1.0) {
						result[i][j] += (255 << bitPos);
					} else {
						temp = (((array[i][j] >> bitPos) & 0xFF) / 255.0) * factor;
						result[i][j] += ((int)(temp * 255.0) & 0xFF) << bitPos;
					}
				}
			}
			bitPos -= 8;
		}
		return result;
	}

	/**
	 * Divide each integer element pair in the two identically dimensioned images.
	 * <p>
	 * Calculation: image1 value / image2 value
	 *
	 * @param  composite1    dividend image
	 * @param  composite2    divisor image
	 * @return               quotient image
	 */
	public static CompositeImage divide(CompositeImage composite1, CompositeImage composite2) {
		return new CompositeImage(divide(composite1.getMatrix(), composite2.getMatrix()));
	}

	/**
	 * Divide each integer element pair in the two identically dimensioned matrices.
	 * <p>
	 * Calculation: array1 value / array2 value
	 * @param  array1        dividend matrix
	 * @param  array2        divisor matrix
	 * @return               quotient matrix
	 */
	public static int[][] divide(int[][] array1, int[][] array2) {
		if (array1.length != array2.length || array1[0].length != array2[0].length) {
			System.out.println("ImageOperations.add(): array1 and array2 are not equal in size");
			return null;
		} else {
			int[][] result = new int[array1.length][array1[0].length];
			double temp;
			int bitPos = 16;
			for (int x = 0; x < 3; x++) {
				for (int i = 0; i < array1.length; i++) {
					for (int j = 0; j < array1[i].length; j++) {
						// If denominator is 0, set pixel to white (avoid division by zero)
						if (((array2[i][j] >> bitPos) & 0xFF) == 0) {
							result[i][j] += 255 << bitPos;
						// If numerator is larger than denominator, set pixel to white
						} else if (((double)((array1[i][j] >> bitPos) & 0xFF) / 255.0) / ((double)((array2[i][j] >> bitPos) & 0xFF) / 255.0) > 1) {
							result[i][j] += 255 << bitPos;
						} else {
							// System.out.println(((array1[i][j] >> bitPos) & 0xFF) + " / " + ((array2[i][j] >> bitPos) & 0xFF));
							temp = ((double)((array1[i][j] >> bitPos) & 0xFF) / 255.0) / ((double)((array2[i][j] >> bitPos) & 0xFF) / 255.0);
							result[i][j] += ((int)(temp * 255.0) << bitPos);
							// result[i][j] += (((array1[i][j] >> bitPos) & 0xFF) * ((array2[i][j] >> bitPos) & 0xFF)) << bitPos;
						}
					}
				}
				bitPos -= 8;
			}
			return result;
		}
	}
}
