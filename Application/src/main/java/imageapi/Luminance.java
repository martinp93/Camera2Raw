
package imageapi;

import imageapi.tools.Tools;

/**
 * Handles luminance calculation of composite images.
 * <p>
 * Modified version of the Android.graphics.color method to work without its dependencies.
 * Default method is based on the formula for relative luminance defined in WCAG 2.0, W3C Recommendation 11 December 2008.
 */
public class Luminance extends ImageOperation {

  /**
   * Component to multiply with normalized red values.
   */
  public static float WEIGHT_RED = 0.2126f;

  /**
   * Component to multiply with normalized green values.
   */
  public static float WEIGHT_GREEN = 0.7152f;

  /**
   * Component to multiply with normalized blue values.
   */
  public static float WEIGHT_BLUE = 0.0722f;

  /**
   * A threshold value used in luminance calculation.
   * <p>
   * Standard defined in WCAG 2.0, W3c Recommendation 11 December 2008
   */
  public static float THRESHOLD = 0.03928f;

  /**
   * An alternative threshold value used in luminance calculation.
   * <p>
   * Standard used by IEC 61966-2-1.
   */
  public static float ALTERNATIVE_THRESHOLD = 0.04045f;

  /**
   * A number in the formula for calculating relative luminance from sRGB.
   */
  public static float ADDEND = 0.055f;

  /**
   * Gamma factor to adjust with.
   */
  public static float GAMMA = 2.4f;

  /**
   * Composes a greyscale image containing the relative luminance of each pixel.
   * @param   img           image to calculate the luminance of
   * @return                a luminance image
   */
  public static CompositeImage getLuminance(CompositeImage img) {
    float[][] luminanceValues = new float[img.getHeight()][img.getWidth()];
    for (int i = 0; i < img.getHeight(); i++) {
      for (int j = 0; j < img.getWidth(); j++) {
        luminanceValues[i][j] = getLuminance(img.getPixel(j, i));
      }
    }
    return new CompositeImage(Tools.linearToARGB(luminanceValues));
  }

  /**
   * Calculates the luminance of a single pixel.
   * @param  color         integer pixel
   * @return               normalized luminance value
   */
  public static float getLuminance(int color) {
    double red = getRed(color) / 255.0;
    red = red < THRESHOLD ? red / 12.92 : Math.pow((red + ADDEND) / (1 + ADDEND), GAMMA);
    double green = getGreen(color) / 255.0;
    green = green < THRESHOLD ? green / 12.92 : Math.pow((green + ADDEND) / (1 + ADDEND), GAMMA);
    double blue = getBlue(color) / 255.0;
    blue = blue < THRESHOLD ? blue / 12.92 : Math.pow((blue + ADDEND) / (1 + ADDEND), GAMMA);
    return (float) ((WEIGHT_RED * red) + (WEIGHT_GREEN * green) + (WEIGHT_BLUE * blue));
  }
}
