
package imageapi;

/**
 * An interface for image-type classes. Contains few but necessary methods needed in implementations of it.
 */
public interface ImageHolder {

  /**
   * Returns height of image
   * @return height of image
   */
  public int getHeight();

  /**
   * Returns width of image
   * @return width of image
   */
  public int getWidth();
}
