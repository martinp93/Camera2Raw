
package imageapi;

import imageapi.tools.Tools;


/**
 * Contains methods for gradient analysis and getting an image representation of this.
 */
public class Gradient extends ImageOperation {

  /**
   * Grid size used in two-dimensional gradient analysis.
   */
  public static int[][] GRID_3 = new int[3][3];

  /**
   * Vector size used in one-dimensional gradient analysis.
   */
  public static float[] VECTOR = new float[3];

  /**
  * Gets the result of a gradient opertion on a single-channel composite image.
  * @param  composite     composite image (single channel)
  * @return               gradient image
  */
  public static CompositeImage getGradient(CompositeImage composite) {
    return new CompositeImage(getGradient(composite.getMatrix()));
  }

  /**
  * Gets the result of a gradient operation on a single-channel pixel matrix.
  * @param  channel  integer pixel matrix (single channel)
  * @return          gradient matrix
  */
  public static int[][] getGradient(int[][] channel) {
    // float[][] minimalizedGreyscale = minimize255(greyscale);
    int width = channel[0].length;
    int height = channel.length;
    float[][] minimizedChannel = minimizeByExtremes(channel);
    float[] column = new float[height], row = new float[width];

    float[][] gradientHorizontal = new float[height][width];
    float[][] gradientVertical = new float[width][height];
    float[][] gradientSum = new float[height][width];

    System.out.println("ROW: " + row.length + "\n" + "COLUMN: " + column.length);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        row[j] = minimizedChannel[i][j];
      }
      gradientHorizontal[i] = convoluteAxis(row);
    }
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        column[j] = minimizedChannel[j][i];
      }
      gradientVertical[i] = convoluteAxis(column);
    }

    for (int i = 0; i < gradientSum.length; i++) {
      for (int j = 0; j < gradientSum[i].length; j++) {
        gradientSum[i][j] = (gradientVertical[j][i] + gradientHorizontal[i][j]) / 2;
      }
    }

    return gradientsToPixels(gradientHorizontal);
  }

  /**
   * Find the global maximum and minimum value of a single component image and normalize with regards to that value.
   * @param data  integer pixel matrix
   * @return      zero-to-one-normalized values
   */
  public static float[][] minimizeByExtremes(int[][] data) {
    int max = 0;
    int min = 255;

    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        if (((data[i][j]) & 0xFF) > max) max = ((data[i][j]) & 0xFF);
        if (((data[i][j]) & 0xFF) < min) min = ((data[i][j]) & 0xFF);
      }
    }
    float fmax = (float)(max);
    float fmin = (float)(min);
    float fdiff = fmax - fmin;

    float[][] result = new float[data.length][data[0].length];
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        result[i][j] = ((data[i][j] & 0xFF) - fmin) / fdiff;
      }
    }
    return result;
  }

  /**
   * Normalize values with respect to the highest single-channel value (255).
   * <p>
   * If an image has a narrow luminance spectrum
   * @param  data  integer pixel data (single channel)
   * @return       zero-to-one-normalized matrix
   */
  public static float[][] minimize256(int[][] data) {
    float[][] result = new float[data.length][data[0].length];
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        result[i][j] = (float)(data[i][j]) / 255.0f;
      }
    }
    return result;
  }

  /**
   * Get convolution result from a line (a one diminsional array) of elements.
   * @param  line  single line of pixel values
   * @return       convolution of line
   */
  private static float[] convoluteAxis(float[] line) {
    float[] direction = new float[line.length];
    VECTOR[1] = 0.0f;
    for (int i = 0; i < line.length; i++) {
      // VECTOR[1] = line[i];
      if (i == 0) {
        VECTOR[0] = line[i+1];
        VECTOR[2] = line[i+1];
      } else if (i == line.length - 1) {
        VECTOR[0] = line[i-1];
        VECTOR[2] = line[i-1];
      } else {
        VECTOR[0] = line[i-1];
        VECTOR[2] = line[i+1];
      }
      direction[i] = VECTOR[1] - (VECTOR[2] - VECTOR[0]);
    }
    return direction;
  }

  /**
   * Convert convolution result (of decimal values) into corresponding pixel values.
   * @param  gradientMap  convolution matrix
   * @return              integer pixel matrix
   */
  public static int[][] gradientsToPixels(float[][] gradientMap) {
    int[][] result = new int[gradientMap.length][gradientMap[0].length];
    for (int i = 0; i < result.length; i ++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = (int)(127.5f + gradientMap[i][j] * 127.5f);
      }
    }
    return Tools.from256toARGB(result);
  }
}
