
package imageapi.help;

import imageapi.*;
import imageapi.tools.*;

/**
 * This class contains documentation for terms used in the rest of the API.
 * <p>
 * There is no use of this in any type of image processing, but to serve as a type of javadoc index page.
 * <p>
 * Although there is already an index page in generated javadocs, this will be a placeholder for the terms used as not to cause confusion within other classes.
 */
public class Terminology {
  /**
   * A decomposed image where the individual values in its matrix contain information of a single color. This is often the same RAW data retrieved from an image sensor.
   */
  public BayerImage BAYER_IMAGE;

  /**
  * A composite image is an image where the color composites of a pixel (Alpha channel, Reds, Greens and Blues) are contained in single values (32-bit integer numbers).
  * <p>
  * This is opposed to a Bayer Image, where each individual value in its matrix represents only a single channel of color.
  */
  public CompositeImage COMPOSITE_IMAGE;

  /**
  * Used to denote a single color component in an entire image or a single pixel.
  * <p>
  * "Channel" and "Component" may be used interchangeably, though "channel" is used to refer more to the hexadecimal digit pair in an integer value (represented hexadeciamally).
  * <p>
  * "A single color channel" / "A single color component."
  */
  public String CHANNEL, COMPONENT;

  /**
  * The integer pixel contains a number representing the color information of a pixel.
  * <p>
  * The value spans from 0x00000000 to 0xFFFFFFFF (hexadecimal notation), which is 32-bits.
  * <p>
  * Each pair of two hexadecimal digits represents a color channel. In an example: 0xFFAABBCC, the digits FF represent a channel, the digits AA represent another and so on.
  * <p>
  * The color channels are: Alpha, Red, Green, Blue, or ARGB for short. And in our example over the pairs follow this abbreviation. (FF - Alpha, AA - Red, BB - Green, CC - Blue).
  * <p>
  * Every pair of two hexadecimal digits make up 8 bits of depth, 256 different values int the domain [0 - 255].
  */
  public int INTEGER_PIXEL;

  /**
  * The integer pixel matrix contains integer pixel values in a grid with the same dimensions of its image. It is essentially the image data.
  */
  public int[][] INTEGER_PIXEL_MATRIX;

  /**
   * Equivalent to the principle of an array or a grid; a two-dimensional object containing values with height and width; a rectangle with smaller rectangles inside it, and each rectangle holds a value; boxes with numbers within a bigger box, capiche?
   */
  public int[][] MATRIX;

  /**
   * To map a value from it's original domain (eg [0 - 255]), to the zero-to-one-domain.
   * <p>
   * Pseudo code:
   * <p> maximum possible value = 20;
   * <p> value = 5;
   * <p> new_value = normalize(value);
   * <p> reading out new_value gives us 0.25, because 5 / 20 = 0.25.
   * <p>
   * The normalizing operation is just a simple division, and the ratio between the value and its possible maximum is the normalized value.
   * <p>
   * This ratio can also be used to map the value to other domains if we multiply it with a new maximum.
   *
   */
  public String NORMALIZE;

  /**
   * A pixel is referred to as the smallest component in an image, containing information for all color channels at that point.
   * <p>
   * If you zoom in extremely close on an image, the single-coloured squares that would be able to differentiate are these pixels.
   * <p>
   * Pixels are also the unit of length and size in an image. ie 200 x 200 pixels would be a square image with dimensions of length 200, and its <em>size</em> would be product of these: 200 x 200 = 40 000 pixels.
   */
  public String PIXEL;

  /**
  * A design pattern used to describe any class that encapsulates underlying code logic for the sake of abstraction and useability.
  */
  public String WRAPPER_CLASS;
}
