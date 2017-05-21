
package imageapi.tools;

public class Tester {

	static long nanoFrom = -1;
	static long nanoTo = -1;

	static long mem;

	public static void setFromTime() {
		nanoFrom = System.nanoTime();
	}

	public static void setToTime() {
		nanoTo = System.nanoTime();
	}

	public static void difference() {
		if (nanoFrom == -1) {
			System.out.println("nanoFrom not set");
		} else if (nanoTo == -1) {
			System.out.println("nanoTo not set");
		} else {
			System.out.println((double)(nanoTo - nanoFrom) / (double)1000000000 + " seconds.");
		}
	}

	// Divides by 1000 -> Readout in kilobytes
	public static void getMemoryUsage() {
		System.out.println((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() / 1000) + " kilobytes.");
	}
}
