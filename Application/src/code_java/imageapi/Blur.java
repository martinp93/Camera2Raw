
/*

     NN    NN    OOOO   TTTTTTTT  EEEEEEEE   SSSSSSS
    NNNN  NN  OO    OO    TT     EE        SS
   NN NN NN  OO    OO    TT     EEEEEE     SSSSSS
  NN  NNNN  OO    OO    TT     EE              SS
 NN    NN    OOOO      TT     EEEEEEEE  SSSSSSS

// Experimental
// ℯ^((-(x - b)²) / (2c²)) / (1 + c²)

// Using sample gaussian blur matrix from the Wikipedia article on gaussian blur.
// Named GAUSSIAN_BLUR_BOX.
// Last visited 28.04.2017 23:32

for (int i = 0; i < box.length; i++) {
  for (int j = 0; j < box[i].length; j++) {
    // experimental
    // box[i][j] = (Math.exp(-(((Math.pow(j - maxOffset, 2) + Math.pow(i - maxOffset, 2)) / (2 * Math.pow(gridSize, 2) / 4))))) / (1 + Math.pow(gridSize / 4, 2));
    box[i][j] =
  }
}
return box;

*/

package imageapi;

import imageapi.*;
import imageapi.tools.Tools;

/**
 * The Blur class contains methods for handling blur requests for an image or matrix. As of now there are Gaussian and linear blur methods.
 */
public class Blur extends ImageOperation {

  /**
   * Constant used in linear blur. All values in the traversal matrix are set to this.
   */
  private static final double LINEAR_BLUR_BOX_VALUE = 1.0;

  /**
   * A 7 x 7, precalculated Gaussian distribution array used as the traversal matrix in gaussian blur.
   * <p>
   * Retrieved from <a>https://en.wikipedia.org/wiki/Gaussian_blur</a>, 00:10 13/05/2017.
   */
  private static final double[][] GAUSSIAN_BLUR_BOX =
    {{0.00000067, 0.00002292, 0.00019117, 0.00038771, 0.00019117, 0.00002292, 0.00000067},
     {0.00002292,	0.00078634, 0.00655965, 0.01330373, 0.00655965, 0.00078633, 0.00002292},
     {0.00019117,	0.00655965, 0.05472157, 0.11098164, 0.05472157, 0.00655965, 0.00019117},
     {0.00038771,	0.01330373, 0.11098164, 0.22508352, 0.11098164, 0.01330373, 0.00038771},
     {0.00019117,	0.00655965, 0.05472157, 0.11098164, 0.05472157, 0.00655965, 0.00019117},
     {0.00002292,	0.00078633, 0.00655965, 0.01330373, 0.00655965, 0.00078633, 0.00002292},
     {0.00000067, 0.00002292, 0.00019117, 0.00038771, 0.00019117, 0.00002292, 0.00000067}
    };

  /**
   * The constant grid size for the gaussian blur traversal matrix.
   */
  private static final int GRID_SIZE_GAUSS = 7;

  /**
   * The minimum length of the traversal matrix dimensions.
   */
  private static final int GRID_SIZE_MINIMUM = 3;

  /**
   * The linear blur choose value.
   * <p>
   * Equal to 1.
   */
  public static final int LINEAR_BLUR = 1;

  /**
   * The gaussian blur choose value.
   * <p>
   * Equal to 2.
   */
  public static final int GAUSSIAN_BLUR = 2;

  /**
   * Compares the dimension of an image with the set grid length of the traversal matrix.
   * @param  dimensionSize length or height of the image
   * @param  gridSize      length of the traversal matrix dimensions
   * @return               true if image dimension is larger
   */
  private static boolean sizeCheck(int dimensionSize, int gridSize) {
    return (gridSize < dimensionSize);
  }

  /**
   * Sets up traversal grid by blur type.
   * <p>
   * Types: Linear blur, Gaussian blur
   * <p>
   * LINEAR_BLUR and GAUSSIAN_BLUR (or their integer values) are the only valid input arguments.
   * @param gridSize size of traversal matrix
   * @param blurType type of blur
   * @return         traversal matrix
   */
  private static double[][] setUpGrid(int gridSize, int blurType) {
    if (blurType == LINEAR_BLUR) {
      double[][] box = new double[gridSize][gridSize];

      for (int i = 0; i < box.length; i++) {
        for (int j = 0; j < box[i].length; j++) {
          box[i][j] = LINEAR_BLUR_BOX_VALUE;
        }
      }
      return box;
    } else if (blurType == GAUSSIAN_BLUR) {
      return GAUSSIAN_BLUR_BOX;

    } else {
      return null;
    }
  }

  /**
   * Applies a linear blur to an image with the specified grid size of the traversal matrix.
   * @param  image         image
   * @param  gridSize      size of traversal matrix dimensions
   * @return               blurred image
   */
  public static CompositeImage linearBlur(CompositeImage image, int gridSize) {
    return new CompositeImage(linearBlur(image.getMatrix(), gridSize));
  }

  /**
   * Applies a linear blur to an integer pixel matrix with the specified grid size of the traversal matrix.
   * @param  image         matrix
   * @param  gridSize      size of traversal matrix dimensions
   * @return               blurred image
   */
  public static int[][] linearBlur(int[][] image, int gridSize) {
    // return linearBlur(new CompositeImage(image), gridSize);

    int width = image[0].length;
    int height = image.length;
    int[][] newArray;

    if (!sizeCheck(width, gridSize) || !sizeCheck(height, gridSize)) {
      newArray = null;
      System.err.println("gridSize is smaller than the image!");

    } else {
      newArray = new int[height][width];

      double[][] box = setUpGrid(gridSize, LINEAR_BLUR);

      // Same multiplier value in each "cell" of the array
      for (int i = 0; i < gridSize; i++) {
        for (int j = 0; j < gridSize; j++) {
          box[i][j] = LINEAR_BLUR_BOX_VALUE;
        }
      }

      int maxOffset = gridSize/2;
      int[][] temp = new int[gridSize][gridSize];

      // Jumps into the image a distance maxOffset, and quits the same distance
      // This is because of unavailable values to fill the active grid which averages values inside it. Very good english. Yes.
      // Fix this. Make a larger image?

      for (int i = maxOffset; i < height - maxOffset; i++) {
        for (int j = maxOffset; j < width - maxOffset; j++) {

          for (int k = 0; k < gridSize; k++) {
            for (int l = 0; l < gridSize; l++) {
              temp[k][l] = image[i - maxOffset + k][j - maxOffset + l];
            }
          }
          newArray[i][j] = blurOperation(temp, box);
        }
      }
    }
    return newArray;

  }

  /**
   * Applies a gaussian blur to an image a specified number of times.
   * <p>
   * Uses a precalculated 7 x 7 traversal matrix.
   * @param  image         image
   * @param  iterations    iterations of gaussian blur
   * @return               blurred image
   */
  public static CompositeImage gaussBlur(CompositeImage image, int iterations) {
    return new CompositeImage(gaussBlur(image.getMatrix(), iterations));
  }

  /**
   * Applies a gaussian blur to an integer pixel matrix a specified number of times.
   * @param  image         matrix
   * @param  iterations    iterations of gaussian blur
   * @return               blurred matrix
   */
  public static int[][] gaussBlur(int[][] image, int iterations) {
    // return gaussBlur(new CompositeImage(image), iterations);
    int width = image[0].length;
    int height = image.length;

    if (!sizeCheck(width, GRID_SIZE_GAUSS) || !sizeCheck(height, GRID_SIZE_GAUSS)) {
      return linearBlur(image, GRID_SIZE_MINIMUM);
    } else {
      int[][] newArray = new int[height][width];

      // first argument into setUpGrid(int, int) is ignored.
      double[][] box = setUpGrid(0, GAUSSIAN_BLUR);

      int maxOffset = GRID_SIZE_GAUSS / 2;
      int[][] temp = new int[GRID_SIZE_GAUSS][GRID_SIZE_GAUSS];

      int[][] mediator = Tools.copy(image);

      for (int count = 0; count < iterations; count++) {
        for (int i = maxOffset; i < height - maxOffset; i++) {
          for (int j = maxOffset; j < width - maxOffset; j++) {

            for (int k = 0; k < GRID_SIZE_GAUSS; k++) {
              for (int l = 0; l < GRID_SIZE_GAUSS; l++) {
                temp[k][l] = mediator[i - maxOffset + k][j - maxOffset + l];
              }
            }
            newArray[i][j] = blurOperation(temp, box);
          }
        }
        mediator = newArray;
      }
      return newArray;
    }
  }

  /**
   * Performs an averaging operation on the image partition with the specified multiplier matrix.
   * @param  multiplicand       partition of the original image
   * @param  multiplier         traversal (multiplier) matrix
   * @return                    average value
   */
  public static int blurOperation(int[][] multiplicand, double[][] multiplier) {

    double divisor = 0;
    for (int i = 0; i < multiplier.length; i++) {
      for (int j = 0; j < multiplier[0].length; j++) {
        divisor += multiplier[i][j];
      }
    }

    double sumRed = 0, sumGreen = 0, sumBlue = 0;

    CompositeImage multiplicandImage = new CompositeImage(multiplicand);

    int[][] reds = multiplicandImage.getColorChannel(1);
    int[][] greens = multiplicandImage.getColorChannel(2);
    int[][] blues = multiplicandImage.getColorChannel(3);

    for (int i = 0; i < multiplicand.length; i++) {
      for (int j = 0; j < multiplicand[0].length; j++) {
        sumRed += reds[i][j] * multiplier[i][j];
        sumGreen += greens[i][j] * multiplier[i][j];
        sumBlue += blues[i][j] * multiplier[i][j];
      }
    }

    return ((255 << 24) + ((int)(sumRed/divisor) << 16) + ((int)(sumGreen/divisor) << 8) + (int)(sumBlue/divisor));
  }
}
