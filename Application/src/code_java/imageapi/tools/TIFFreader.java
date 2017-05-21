
package imageapi.tools;

import java.io.*;

class TIFFreader {

  static Boolean LittleEndian = true;
  static int TOTAL_BYTES;

  public static String[] typeTable = {
    "1 BYTE",
    "2 ASCII",
    "3 SHORT",
    "4 LONG (INT)",
    "5 RATIONAL (LONG)",
    "6 SBYTE",
    "7 UNDEFINED",
    "8 SSHORT",
    "9 SLONG",
    "10 SRATIONAL",
    "11 FLOAT",
    "12 DOUBLE"
  };

  public static String[] maxTypeCountIn4Bytes = {
    "1 4",
    "2 4",
    "3 2",
    "4 1",
    "5 0",
    "6 4",
    "7 4",
    "8 2",
    "9 1",
    "10 0",
    "11 1",
    "12 0"
  };

  public static String[] tagTable = {
    "254 NewSubfileTyp",
    "255 SubfileType",
    "256 ImageWidth",
    "257 ImageLength",
    "258 BitsPerSample",
    "259 Compression",
    "262 PhotometricInterpretation",
    "263 Threshholding",
    "264 CellWidth",
    "265 CellLength",
    "266 FillOrder",
    "269 DocumentName",
    "270 ImageDescription",
    "271 Make",
    "272 Model",
    "273 StripOffsets",
    "274 Orientation",
    "277 SamplesPerPixel",
    "278 RowsPerStrip",
    "279 StripByteCounts",
    "280 MinSampleValue",
    "281 MaxSampleValue",
    "282 XResolution",
    "283 YResolution",
    "284 PlanarConfiguration",
    "288 FreeOffsets",
    "289 FreeByteCounts",
    "290 GrayResponseUnit",
    "291 GrayResponseCurve",
    "296 ResolutionUnit",
    "305 Software",
    "306 DateTime",
    "315 Artist",
    "316 HostComputer",
    "320 ColorMap",
    "338 ExtraSamples",
    "33432 Copyright"
  };

  public static int getPositionInByteStream(int available) {
    return TOTAL_BYTES - available;
  }

  public static void printPositionInByteStream(int available) {
    System.out.println(getPositionInByteStream(available));
  }

  private static void switchEndian() {
    LittleEndian = !LittleEndian;
  }

  public static int mergeBytesOverrideEndian(byte[] bytes) {
    switchEndian();
    int carrier = mergeBytes(bytes);
    switchEndian();
    return carrier;
  }

  public static int mergeBytesOverrideEndian(byte[] bytes, int startIndex, int endIndex) {
    switchEndian();
    int carrier = mergeBytes(bytes, startIndex, endIndex);
    switchEndian();
    return carrier;
  }

  // from the beginning of startIndex, to, and including, endIndex
  public static int mergeBytes(byte[] bytes, int startIndex, int endIndex) {
    int length = endIndex - startIndex + 1;
    byte[] mediator = new byte[length];
    for (int i = 0; i < length; i++) {
      mediator[i] = bytes[i + startIndex];
    }

    return mergeBytes(mediator);
  }

  public static int mergeBytes(byte[] bytes) {
    if (bytes.length > 4) {
      System.err.println("Byte array is larger than 4 bytes");
      return -1;
    } else {
      int count = bytes.length;
      int shift = 8 * (count - 1);

      int variable = 0;

      if (LittleEndian) {
        for (int i = count - 1; i >= 0; i--) {
          variable |= ((bytes[i] & 0xFF) << shift);
          shift -= 8;
        }
      } else {
        for (int i = 0; i < count; i++) {
          variable |= ((bytes[i] & 0xFF) << shift);
          shift -= 8;
        }
      }
      return variable;
    }
  }

  // Reads the "length" number of bytes next in dataStream
  // Can only read up to 4 bytes
  public static int readAndMerge(DataInputStream dataStream, int length) throws IOException {
    try {
      byte[] bytes = new byte[length];
      dataStream.read(bytes, 0, length);
      return mergeBytes(bytes);
    } catch (IOException e) {
      System.err.println("Error in readAndMerge()");
      e.printStackTrace();
      return -1;
    }
  }

  public static String readToASCII(DataInputStream dataStream, int length) throws IOException {
    try {
      byte[] bytes = new byte[length];
      dataStream.read(bytes, 0, length);
      char[] characters = new char[length];

      if (LittleEndian) {
        for (int i = length - 1; i >= 0; i--) {
          characters[i] = (char)(bytes[i]);
        }
      } else {
        for (int i = 0; i < length; i++) {
          characters[i] = (char)(bytes[i]);
        }
      }
      return new String(characters);

    } catch (IOException e) {
      System.err.println("Error in readToASCII()");
      e.printStackTrace();
      return null;
    }
  }

  public static void printBytes(byte[] bytes, int offset, int quantity) {
    if (offset + quantity > bytes.length) {
      System.err.println("Defined range of bytes to be printed exceeds actual length of array.");
    } else {
      byte[] out = new byte[quantity];
      for (int i = 0; i < quantity; i++) {
        out[i] = bytes[i + offset];
      }
      printBytes(out);
    }
  }

  public static void printBytes(byte[] bytes) {
    String zeros = "00000000";

    for (byte e : bytes) {
      int length = Integer.toBinaryString((int)e).length();
      System.out.println((zeros + Integer.toBinaryString((int)e)).substring(length));
    }
  }

  public static String findTag(int index) {
    for (String element : tagTable) {
      if (element.indexOf(Integer.toString(index)) != -1) {
        return index + " " + element.substring(Integer.toString(index).length() + 1);
      }
    }
    return index + " No tag reference found";
  }

  public static String findType(int index) {
    for (String element : typeTable) {
      if (element.indexOf(Integer.toString(index)) != -1) {
        return index + " " + element.substring(Integer.toString(index).length() + 1);
      }
    }
    return index + " No type reference found";
  }

  public static boolean localValue(int type, int count) {
    for (String element : maxTypeCountIn4Bytes) {
      String[] parts = element.split(" ");
      if (parts[0].indexOf(Integer.toString(type)) != -1) {
        if (count <= Integer.parseInt(parts[1])) {
          return true;
        }
      }
    }
    return false;
  }


  public static void main(String[] args) {

    DataInputStream dataStream = null;
    int available;

    try {

      //////////////////////////////////////////////////////////////////////////

      System.out.println("HEADER");

      dataStream = new DataInputStream(new FileInputStream("media/tiffimage_2.tiff"));
      TOTAL_BYTES = dataStream.available();
      System.out.println(TOTAL_BYTES + " bytes in stream.");

      int endianValue = readAndMerge(dataStream, 2);

      if (endianValue == 0x4949) {
        System.out.println("Little endian.");
        LittleEndian = true;
      } else if (endianValue == 0x4D4D) {
        System.out.println("Big endian.");
        LittleEndian = false;
      } else {
        System.err.println("Unable to read endian");
        LittleEndian = null;
        System.exit(0);
      }

      int life = readAndMerge(dataStream, 2);
      System.out.println("Number: " + life);

      int offsetValue = readAndMerge(dataStream, 4);
      System.out.println("\n" + "Offset to first IFD: " + "\n" + offsetValue);

      // skips to the first IFD with regards to the 8 bits we previously read
      dataStream.skipBytes(offsetValue - 8);

      available = dataStream.available();
      System.out.println("\n" + available + " bytes left.");

      //////////////////////////////////////////////////////////////////////////

      System.out.println("\n" + "Entering IFD");
      int entries = readAndMerge(dataStream, 2);

      System.out.println(entries + " entries");

      //////////////////////////////////////////////////////////////////////////
      System.out.println("\n" + "Entering directory entries");
      byte[] entry = new byte[12];
      dataStream.read(entry, 0, 12);

      String lookUp = "";

      while (mergeBytes(entry, 0, 3) != 0) {

        int tag = mergeBytes(entry, 0, 1);
        int type = mergeBytes(entry, 2, 3);
        int count = mergeBytes(entry, 4, 7);
        int value = mergeBytes(entry, 8, 11);

        String tagName = findTag(tag);
        String typeName = findType(type);

        if (!localValue(type, count)) {
          lookUp += type + " " + count + " " + "value" + "\n";
        }

        System.out.println("Tag:\t" + tagName + "\n" + "Type:\t" + typeName + "\n" + "Count:\t" + count + "\n" + "Value:\t" + value);
        System.out.println();

        dataStream.read(entry, 0, 12);
      }

      dataStream.close();
      // dataStream.

      // byte[] restBytes = new byte[dataStream.available()];
      // dataStream.read(restBytes);

      // System.out.println("\n" + dataStream.available() + " bytes left.");
      //
      // System.out.println("To skip: " + (entries * 12));
      // int skipped = dataStream.skipBytes(entries * 12);
      // System.out.println("Skipped " + skipped + " bytes.");
      //
      // byte[] zeros = new byte[4];
      // dataStream.read(zeros, 0, 4);
      //
      // for (byte e : zeros) {
      //   System.out.println(e);
      // }

    } catch (FileNotFoundException e) {
      System.err.println("File not found!");
      e.printStackTrace();
    } catch (SecurityException e) {
      System.err.println("Security exception. Shit happens.");
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("IOException.");
      e.printStackTrace();
    }
  }
}
