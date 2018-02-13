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
 * 		  hassane.abouaissa@univ-artois.fr
 * 
 * Contributors:
 * 	Hassane ABOUAISSA (designer)
 * 	Gildas MORVAN (designer, creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is run road traffic
 * simulations using a dynamic hybrid approach.
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
package fr.lgi2a.similar2logo.lib.tools.randomstrategies;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

import fr.lgi2a.similar2logo.lib.tools.IRandomValuesGenerator;

/**
 * A Mersenne twister based implementation of the random numbers generation strategy.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan/" target="_blank">Gildas Morvan</a>
 */
public class MTRandomBasedRandomValuesGenerator implements IRandomValuesGenerator {
	
	/**
	 * The UniformRandomProvider object generating random numbers in this strategy.
	 * @see org.apache.commons.rng.UniformRandomProvider
	 */
	private UniformRandomProvider javaRandomHelper = RandomSource.create(RandomSource.MT);

	 private double nextNextGaussian;
	 private boolean haveNextNextGaussian = false;
	
	/**
	 * Builds a random values generation strategy relying on the UniformRandomProvider class.
	 * @param seed The seed used to initialize the java random values generator.
	 */
	public MTRandomBasedRandomValuesGenerator (byte[] seed) {
		this.javaRandomHelper = RandomSource.create(RandomSource.MT, seed);
	}
	
	/**
	 * Builds a random values generation strategy relying on the UniformRandomProvider class.
	 * @param seed The seed used to initialize the java random values generator.
	 */
	public MTRandomBasedRandomValuesGenerator (long seed) {
		this.javaRandomHelper = RandomSource.create(RandomSource.MT, seed);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double randomDouble() {
		return javaRandomHelper.nextDouble();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double randomAngle() {
		return Math.PI-javaRandomHelper.nextDouble( )*2*Math.PI;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean randomBoolean() {
		return javaRandomHelper.nextBoolean();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int randomInt(int bound) {
		return javaRandomHelper.nextInt(bound);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public double randomGaussian () {
		if (haveNextNextGaussian) {
		     haveNextNextGaussian = false;
		     return nextNextGaussian;
		   } else {
		     double v1, v2, s;
		     do {
		       v1 = 2 * javaRandomHelper.nextDouble() - 1;
		       v2 = 2 * javaRandomHelper.nextDouble() - 1; 
		       s = v1 * v1 + v2 * v2;
		     } while (s >= 1 || s == 0);
		     double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
		     nextNextGaussian = v2 * multiplier;
		     haveNextNextGaussian = true;
		     return v1 * multiplier;
		   }
	}

}
