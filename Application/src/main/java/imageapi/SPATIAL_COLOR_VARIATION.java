
package imageapi;

import imageapi.*;

/**
 * Work in progress.
 * Based on "Drawing Parrots With Charcoal" by Alsam, Rivertz [2012]
 */
public class SPATIAL_COLOR_VARIATION extends ImageOperation {

  int GAMMA = 2;
  int ALPHA = 3;
  int BETA = 2;

  double weightRed = Luminance.WEIGHT_RED;
  double weightGreen = Luminance.WEIGHT_GREEN;
  double weightBlue = Luminance.WEIGHT_BLUE;

  public void setGamma(int gamma) {
    GAMMA = gamma;
  }

  public void setAlpha(int alpha) {
    ALPHA = alpha;
  }

  public void setBeta(int beta) {
    BETA = beta;
  }

/*
  public static void main(String[] args) {
    try {
      BufferedImage img = ImageIO.read(new File("media/testimage_small.jpg"));
      CompositeImage composite = new CompositeImage(img);
      CompositeImage greyscale = Luminance.getLuminance(composite);

      CompositeImage blurred = Blur.gaussBlur(composite, 1);
      CompositeImage difference = Arithmetic.subtract(composite, blurred);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  */
}
