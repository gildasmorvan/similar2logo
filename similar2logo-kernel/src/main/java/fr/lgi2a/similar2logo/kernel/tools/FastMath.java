/**
 * Copyright or © or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the 
 * implementation of Logo-like simulations using the SIMILAR API.
 * This software defines an API to implement such simulations, and also 
 * provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.lgi2a.similar2logo.kernel.tools;

/**
 * A fast implementation of logarithmic functions using lookup-tables.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class FastMath {

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
	
	/**
	 * Equality threshold for doubles
	 */
	private static final double EPSILON = 1.0e-8;

	static {
		for (int i = 0; i <= SIZE; i++) {
			double f = (double) i / SIZE;
			ATAN2_TABLE_PPY[i] = (double) (StrictMath.atan(f) * STRETCH / StrictMath.PI);
			ATAN2_TABLE_PPX[i] = STRETCH * 0.5 - ATAN2_TABLE_PPY[i];
			ATAN2_TABLE_PNY[i] = -ATAN2_TABLE_PPY[i];
			ATAN2_TABLE_PNX[i] = ATAN2_TABLE_PPY[i] - STRETCH * 0.5;
			ATAN2_TABLE_NPY[i] = STRETCH - ATAN2_TABLE_PPY[i];
			ATAN2_TABLE_NPX[i] = ATAN2_TABLE_PPY[i] + STRETCH * 0.5;
			ATAN2_TABLE_NNY[i] = ATAN2_TABLE_PPY[i] - STRETCH;
			ATAN2_TABLE_NNX[i] = -STRETCH * 0.5 - ATAN2_TABLE_PPY[i];
		}
	}
	
	/**
	 * @param d1 first double to compare
	 * @param d2 second double to compare
	 * @return <code>true</code> if d1 and d2 are equal
	 */
	public static final boolean areEqual(double d1, double d2) {
		return Math.abs(d1 - d2) <= EPSILON;
	}

	/**
	 * @param y the ordinate coordinate
	 * @param x the abscissa coordinate
	 * @return the theta component of the point (r, theta) in polar coordinates that corresponds to the point (x, y) in Cartesian coordinates.
	 */
	public static final double atan2(double y, double x) {
		if (x >= 0) {
			if (y >= 0) {
				if (x >= y) {
					return ATAN2_TABLE_PPY[(int) (SIZE * y / x + 0.5)];
				} else {
					return ATAN2_TABLE_PPX[(int) (SIZE * x / y + 0.5)];
				}
			} else {
				if (x >= -y) {
					return ATAN2_TABLE_PNY[(int) (EZIS * y / x + 0.5)];
				} else {
					return ATAN2_TABLE_PNX[(int) (EZIS * x / y + 0.5)];
				}
			}
		} else {
			if (y >= 0) {
				if (-x >= y) {
					return ATAN2_TABLE_NPY[(int) (EZIS * y / x + 0.5)];
				} else {
					return ATAN2_TABLE_NPX[(int) (EZIS * x / y + 0.5)];
				}
			} else {
				if (x <= y)  {
					return ATAN2_TABLE_NNY[(int) (SIZE * y / x + 0.5)];
				} else {
					return ATAN2_TABLE_NNX[(int) (SIZE * x / y + 0.5)];
				}
			}
		}
	}

}
