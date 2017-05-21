
package imageapi;

/**
 * Parent class of all image handling classes. Contains methods likely to be used by these.
 */
public class ImageOperation {

	/**
	 * Extract alpha component from pixel.
	 * @param  pixelValue    pixel value
	 * @return               alpha component
	 */
	public static int getAlpha(int pixelValue) {
		return (pixelValue >> 24) & 0xFF;
	}

	/**
	 * Extract red component from pixel.
	 * @param  pixelValue    pixel value
	 * @return               red component
	 */
	public static int getRed(int pixelValue) {
		return (pixelValue >> 16) & 0xFF;
	}

	/**
	 * Extract green component from pixel.
	 * @param  pixelValue    pixel value
	 * @return               green component
	 */
	public static int getGreen(int pixelValue) {
		return (pixelValue >> 8) & 0xFF;
	}

	/**
	 * Extract blue component from pixel.
	 * @param  pixelValue    pixel value
	 * @return               blue component
	 */
	public static int getBlue(int pixelValue) {
		return pixelValue & 0xFF;
	}
}
