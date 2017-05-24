
package imageapi;

import imageapi.Gradient;
import imageapi.Luminance;
import imageapi.Arithmetic;

/**
 * Work in progress.
 */
public class Contrast extends ImageOperation {
  public static CompositeImage enhanceContrast(CompositeImage composite) {
    System.out.println("Entered method");
    float[][] intensityValues = Gradient.minimizeByExtremes(Luminance.getLuminance(composite).getMatrix());
    CompositeImage intensities = new CompositeImage(Gradient.gradientsToPixels(intensityValues));
    return Arithmetic.multiply(composite, intensities);
  }
}
