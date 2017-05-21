
package imageapi.tools;

import imageapi.BayerImage;
import imageapi.tools.Tools;

public class BayerConversionManager {

  public static int[][] composeImage(BayerImage rawImage) {

    int[][] composite = new int[rawImage.getHeight()/2][rawImage.getWidth()/2];

    short[][] reds = rawImage.getColorPixels(BayerImage.RED_COORDINATE);
    short[][] greenTR = rawImage.getColorPixels(BayerImage.GREEN1_COORDINATE);
    short[][] greenBL = rawImage.getColorPixels(BayerImage.GREEN2_COORDINATE);
    short[][] blues = rawImage.getColorPixels(BayerImage.BLUE_COORDINATE);

    for (int i = 0; i < composite.length; i++) {
      for (int j = 0; j < composite[i].length; j++) {
        composite[i][j] = mergeToARGB(blues[i][j], greenTR[i][j], greenBL[i][j], reds[i][j]);
      }
    }
    return composite;
  }

  // TODO: make return type byte
  public static short from12to8(short in) {
    short result = (short)(Tools.mapper((int)in, 4095, 255));
    return result;
  }

  // Degrades image to RGB (8-bit channels)
  public static int mergeToARGB(short blueValue, short greenTRvalue, short greenBLvalue, short redValue) {
    // ARGB-format: Alpha Red Green Blue
    //                ##   ##   ##   ##
    String RGBhex = "";
    short blue = from12to8(blueValue);
    String blueString = prefixZeros(Integer.toString((int)blue, 16));

    short greenTR = from12to8(greenTRvalue);
    short greenBL = from12to8(greenBLvalue);

    short greenMedian = (short)((greenTR + greenBL)/2);
    String greenString = prefixZeros(Integer.toString((int)greenMedian, 16));

    short red = from12to8(redValue);
    String redString = prefixZeros(Integer.toString((int)red, 16));

    RGBhex += redString + greenString + blueString;

    return Integer.parseInt(RGBhex, 16);
  }

  public static String prefixZeros(String in) {
    if (in.length() < 2) {
      return "0" + in;
    } else {
      return in;
    }
  }
}
