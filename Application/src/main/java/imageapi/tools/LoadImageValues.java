
package imageapi.tools;

import java.util.Random;

public class LoadImageValues {

  // Decodes RAW16-data from Byte[] into short[]
  public static short[][] raw16decoder (byte[] bytes, int width) {
    int length = bytes.length / 2;
    short[] pixels = new short[length];

    for (int i = 0; i < pixels.length; i++) {
      pixels[i] = (short)(bytes[2 * i] << 8);
      pixels[i] += bytes[(2 * i) + 1];
    }
    return distributeRows(pixels, width);
  }


  // Decodes RAW12-data from Byte[] into short[all elements]
  public static short[][] raw12decoder (byte[] bytes, int width) {
    int x = 0;

    Double calculationLength = new Double(bytes.length * (2.0 / 3.0));
    int length = calculationLength.intValue();

    short[] pixels = new short[length];

    short pixel1, pixel2;
    for (int i = 0; i < bytes.length; i += 3) {
      pixel1 = (short)bytes[i];
      pixel2 = (short)bytes[i+1];
      pixel1 <<= 4; pixel2 <<= 4;

      String lastByte = toByteString(bytes[i+2]);

      short lastBytePart1 = parseBitString(lastByte.substring(0,4));
      short lastBytePart2 = parseBitString(lastByte.substring(4,8));

      pixel2 += lastBytePart1;
      pixel1 += lastBytePart2;

      pixels[x] = pixel1;
      pixels[x+1] = pixel2;
      x += 2;
    }
    return distributeRows(pixels, width);
  }

  // Parse a bit-String (as in "01010101" as a String object) into an integer (short).
  public static short parseBitString(String bitString) {
    short result = 0;

    int max = bitString.length() - 1;
    for (int i = 0; i < bitString.length(); i++) {
      result += Math.pow(2, max-i) * (Character.getNumericValue(bitString.charAt(i)));
    }
    return result;
  }

  // Converts short[all elements] into short[height][width]
  public static short[][] distributeRows(short[] list, int width) {
    int rowNumber = -1;
    int x = 0;

    Double calculationHeight = new Double(list.length / width);
    short height = calculationHeight.shortValue();

    short[][] result = new short[height][width];

    for (int i = 0; i < list.length; i++) {
      if (i % width == 0) {
        rowNumber++;
      }
      x = i - (width * rowNumber);
      result[rowNumber][x] = list[i];
    }

    return result;
  }

  // Reads a String with 8-bit integer values and returns a Byte[]-matrix
  // NOTE: Assuming that the values are seperated with spaces
  public static byte[] parseImageData(String in) {
    String[] values = in.split(" ");
    byte[] byteValues = new byte[values.length];

    for (int i = 0; i < values.length; i++) {
      byteValues[i] = Byte.decode(values[i]);
    }
    return byteValues;
  }

  // Converts a byte-integer into a String representation of the same binary number
  public static String toBitString(byte b) {
    Integer integer = new Integer(b);
    return Integer.toString(integer, 2);
  }

  // Converts a short-integer into a String representation of the same binary number
  public static String toBitString(short s) {
    Integer integer = new Integer(s);
    return Integer.toString(integer, 2);
  }

  // Converts a byte-integer (whose leading zeros might be cut off) into a String representation, and adds leading zeros into a 8-digit number
  public static String toByteString(byte b) {
    Integer integer = new Integer(b);
    String bitString = Integer.toBinaryString(integer);
    String resultString = "00000000" + bitString;
    resultString = resultString.substring(bitString.length());

    return resultString;
  }

  // Converts a short-integer's bit-pattern into a String representation, and adds leading zeros into a 16-digit number
  public static String toByteString(short s) {

    Integer integer = new Integer(s);
    String bitString = Integer.toBinaryString(integer);
    String resultString = "0000000000000000" + bitString;
    resultString = resultString.substring(resultString.length()-16, resultString.length());

    return resultString;
  }

  // Quickly convert Byte[] to String[]
  public static String[] toStringBytes(byte[] bytes) {
    String[] result = new String[bytes.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = toByteString(bytes[i]);
    }
    return result;
  }

  // Assumes input-string is already formatted to "XXXXXXXX" (eight digits)
  public static String formatByteString(String in, int splitPer) {

    int numSpaces = in.length()/splitPer - 1;
    if (numSpaces < 0) {
      return in;
    } else {
      String[] segment = new String[numSpaces + 1];
      for (int i = 0; i < numSpaces + 1; i++) {
        segment[i] = in.substring(i * splitPer, i * splitPer + splitPer);
      }

      String out = new String(segment[0]);
      for (int j = 1; j < segment.length; j++) {
        out += " " + segment[j];
      }
      return out;
    }
  }

  // Unicode minus sign: −
  public static String convertHyphensToNegatives(String string) {
    return string.replace('-', '\u002D');
  }

  public static void printResultMatrix(short[][] matrix, boolean bin, int split) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        if (bin) System.out.println(formatByteString(toByteString(matrix[i][j]), split));
        else System.out.println(matrix[i][j]);
      }
      System.out.println();
    }
  }

  public static String linesToLine(String in) {
    return in.replace("\n"," ");
  }

}
