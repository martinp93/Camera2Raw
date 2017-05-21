
package imageapi;

/**
 * A wrapper class for matrices containing masaics of RAW data.
 */
public class BayerImage implements ImageHolder {

  /**
   *
   */
  public static final int[] RED_COORDINATE    = {1, 1};

  public static final int[] GREEN1_COORDINATE = {1, 0};

  public static final int[] GREEN2_COORDINATE = {0, 1};

  public static final int[] BLUE_COORDINATE   = {0, 0};

  /**
   *
   */
  short[][] bayerData;

  /**
   *
   */
  int width;

  /**
   *
   */
  int height;

  /**
   *
   */
  int totalPixels;

  public BayerImage(short[][] bayerData) {
    this.bayerData = bayerData;

    this.height = bayerData.length;
    this.width = bayerData[0].length;
    totalPixels = width * height;

    bayerData = adjustBayerData(bayerData);
  }

  /**
   * Adjusts 12-bit bayer data from [-2048, 2047] to [0, 4095]
   * @param in bayer data to adjust
   * @return   adjusted bayer data
   */
  public short[][] adjustBayerData(short[][] in) {
    short[][] result = new short[in.length][in[0].length];
    for (int i = 0; i < in.length; i++) {
      for (int j = 0; j < in[i].length; j++) {
        if (in[i][j] < 0) result[i][j] = (short)(4096 + in[i][j]);
        else result[i][j] = in[i][j];
      }
    }
    return result;
  }

  // COORDINATES
  // BLUE      =   0 , 0
  // RED       =   1 , 1
  // GREENTR   =   0 , 1
  // GREENBL   =   1 , 0

  /**
   * Gets the specified color component values from a bayer-pattern mosaiced image
   * @param coordinates   coordinates to the color component
   * @return              matrix of color values
   */
  public short[][] getColorPixels(int[] coordinates) {
    short[][] result = new short[height/2][width/2];
    int x = 0, y = 0;
    for (int i = coordinates[0]; i < height; i += 2) {
      for (int j = coordinates[1]; j < width; j += 2) {
        result[y][x] = bayerData[i][j];
        // System.out.println(result[y][x]);
        x++;
      }
      x = 0;
      y++;
    }
    return result;
  }

  /**
   * Get bayer image data.
   * @return image data
   */
  public short[][] getBayerImage() {
    return bayerData;
  }

  public int getHeight() {
    return height;
  }
  public int getWidth() {
    return width;
  }
}
