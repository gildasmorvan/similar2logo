package fr.lgi2a.similar2logo.kernel.tools;

public class FastMath {

	private static final int SIN_BITS = 12;
	private static final int SIN_MASK = ~(-1 << SIN_BITS);
	private static final int SIN_COUNT = SIN_MASK + 1;
	private static final double radFull = StrictMath.PI * 2.0;
	private static final double radToIndex = SIN_COUNT / radFull;

	private static final double[] sin = new double[SIN_COUNT];
	private static final double[] cos = new double[SIN_COUNT];

	private static final int SIZE = 1024;
	private static final double STRETCH = Math.PI;

	private static final int EZIS = -SIZE;
	private static final double[] ATAN2_TABLE_PPY = new double[SIZE + 1];
	private static final double[] ATAN2_TABLE_PPX = new double[SIZE + 1];
	private static final double[] ATAN2_TABLE_PNY = new double[SIZE + 1];
	private static final double[] ATAN2_TABLE_PNX = new double[SIZE + 1];
	private static final double[] ATAN2_TABLE_NPY = new double[SIZE + 1];
	private static final double[] ATAN2_TABLE_NPX = new double[SIZE + 1];
	private static final double[] ATAN2_TABLE_NNY = new double[SIZE + 1];
	private static final double[] ATAN2_TABLE_NNX = new double[SIZE + 1];

	static {
		for (int i = 0; i <= SIZE; i++) {
			double f = (double) i / SIZE;
			ATAN2_TABLE_PPY[i] = (double) (StrictMath.atan(f) * STRETCH / StrictMath.PI);
			ATAN2_TABLE_PPX[i] = STRETCH * 0.5f - ATAN2_TABLE_PPY[i];
			ATAN2_TABLE_PNY[i] = -ATAN2_TABLE_PPY[i];
			ATAN2_TABLE_PNX[i] = ATAN2_TABLE_PPY[i] - STRETCH * 0.5f;
			ATAN2_TABLE_NPY[i] = STRETCH - ATAN2_TABLE_PPY[i];
			ATAN2_TABLE_NPX[i] = ATAN2_TABLE_PPY[i] + STRETCH * 0.5f;
			ATAN2_TABLE_NNY[i] = ATAN2_TABLE_PPY[i] - STRETCH;
			ATAN2_TABLE_NNX[i] = -STRETCH * 0.5f - ATAN2_TABLE_PPY[i];
		}

		for (int i = 0; i < SIN_COUNT; i++) {
			sin[i] = (double) StrictMath.sin((double) (i + 0.5f) / SIN_COUNT * radFull);
			cos[i] = (double) StrictMath.cos((double) (i + 0.5f) / SIN_COUNT * radFull);
		}
	}

	/**
	 * @param y the ordinate coordinate
	 * @param x the abscissa coordinate
	 * @return the theta component of the point (r, theta) in polar coordinates that corresponds to the point (x, y) in Cartesian coordinates.
	 */
	public static final double atan2(double y, double x) {
		if (x >= 0) {
			if (y >= 0) {
				if (x >= y)
					return ATAN2_TABLE_PPY[(int) (SIZE * y / x + 0.5)];
				else
					return ATAN2_TABLE_PPX[(int) (SIZE * x / y + 0.5)];
			} else {
				if (x >= -y)
					return ATAN2_TABLE_PNY[(int) (EZIS * y / x + 0.5)];
				else
					return ATAN2_TABLE_PNX[(int) (EZIS * x / y + 0.5)];
			}
		} else {
			if (y >= 0) {
				if (-x >= y)
					return ATAN2_TABLE_NPY[(int) (EZIS * y / x + 0.5)];
				else
					return ATAN2_TABLE_NPX[(int) (EZIS * x / y + 0.5)];
			} else {
				if (x <= y)
					return ATAN2_TABLE_NNY[(int) (SIZE * y / x + 0.5)];
				else
					return ATAN2_TABLE_NNX[(int) (SIZE * x / y + 0.5)];
			}
		}
	}

	
	/**
	 * @param rad an angle, in radians.
	 * @return the sine of the argument.
	 */
	public static final double sin(double rad) {
		return sin[(int) (rad * radToIndex) & SIN_MASK];
	}

	/**
	 * @param rad an angle, in radians.
	 * @return the cosine of the argument.
	 */
	public static final double cos(double rad) {
		return cos[(int) (rad * radToIndex) & SIN_MASK];
	}

}
