
package imageapi;

public class ALGEBRAIC_COLOR_TO_GREYSCALE extends ImageOperation {

	// Standard values
	double gamma = 1.0, mu = 0.5;

	// Luminance weights
	final double WEIGHT_RED = 1.0/3.0, WEIGHT_GREEN = 1.0/3.0, WEIGHT_BLUE = 1.0/3.0;

	CompositeImage H1, H2, H3;
	CompositeImage Y1, Y2, Y3;
	CompositeImage result;

	public ALGEBRAIC_COLOR_TO_GREYSCALE(CompositeImage source) {
		Y1 = new CompositeImage(getYn(source, 1));
		Y2 = new CompositeImage(getYn(source, 2));
		Y3 = new CompositeImage(getYn(source, 3));

		H1 = Arithmetic.subtract(Blur.gaussBlur(Y1, 1), Y1);
		H2 = Arithmetic.subtract(Y1.exponentiate(2), Y2);
		H3 = Arithmetic.add(Arithmetic.subtract(Arithmetic.multiply(Arithmetic.multiply(Y1, Y2), 3), Y3), Arithmetic.multiply(Y1.exponentiate(3), 2));

		// Don't know if this works as optimization, do it anyway
		Y2 = null; Y3 = null;
		result = formula();
		Y1 = null; H1 = null; H2 = null; H3 = null;
	}

	public ALGEBRAIC_COLOR_TO_GREYSCALE(CompositeImage source, double gamma, double mu) {
		this.gamma = gamma;
		this.mu = mu;

		Y1 = new CompositeImage(getYn(source, 1));
		Y2 = new CompositeImage(getYn(source, 2));
		Y3 = new CompositeImage(getYn(source, 3));

		H1 = Arithmetic.subtract(Y1, Blur.gaussBlur(Y1, 1));
		H2 = Arithmetic.subtract(Y2, Y1.exponentiate(2));
		H3 = Arithmetic.add(Arithmetic.subtract(Y3, Arithmetic.multiply(Arithmetic.multiply(Y1, Y2), 3)), Arithmetic.multiply(Y1.exponentiate(3), 2));

		// Don't know if this works as optimization, do it anyway
		Y2 = null; Y3 = null;
		result = formula();
		Y1 = null; H1 = null; H2 = null; H3 = null;
	}

	public CompositeImage getResult() {
		return result;
	}

	private CompositeImage formula() {
		CompositeImage numerator = Arithmetic.add(Arithmetic.multiply(Arithmetic.multiply(H1, H2), mu), Arithmetic.multiply(H3, gamma));
		CompositeImage denominator = Arithmetic.add(H1.exponentiate(2), H2);
		CompositeImage fraction = Arithmetic.divide(numerator, denominator);
		return Arithmetic.add(Y1, fraction);

	}

	private int[][] getYn(CompositeImage source, int n) {
		int[][] composite = new int[source.getHeight()][source.getWidth()];
		double sum, redNum, greenNum, blueNum;
		for (int i = 0; i < source.getHeight(); i++) {
			for (int j = 0; j < source.getWidth(); j++) {
				redNum = Math.pow((double)((source.getPixel(j, i) >> 16) & 0xFF) / 255.0, n) * 255;
				greenNum = Math.pow((double)((source.getPixel(j, i) >> 8) & 0xFF) / 255.0, n) * 255;
				blueNum = Math.pow((double)(source.getPixel(j, i) & 0xFF) / 255.0, n) * 255;
				sum = (redNum * WEIGHT_RED + greenNum * WEIGHT_GREEN + blueNum * WEIGHT_BLUE);
				composite[i][j] = ((255 << 24) + ((int)sum << 16) + ((int)sum << 8) + (int)sum);
			}
		}
		return composite;
	}
}
