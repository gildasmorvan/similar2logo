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
package fr.lgi2a.similar2logo.lib.tools.randomstrategies;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.math3.random.MersenneTwister;

/**
 * 
 * A synchronized version of the Mersenne Twister PRNG based on apache commons
 * implementation. Each instance has its own PRNG.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 * target= "_blank">Gildas Morvan</a>
 *
 */
public final class SynchronizedMersenneTwister extends Random {

	private static final long serialVersionUID = -4586969514356530381L;

	private static final Random SEEDER;

	private static final SynchronizedMersenneTwister INSTANCE;

	private static final ThreadLocal<MersenneTwister> LOCAL_RANDOM;

	static {
		SEEDER = new SecureRandom();

		LOCAL_RANDOM = new ThreadLocal<MersenneTwister>() {

			@Override
			protected MersenneTwister initialValue() {
				synchronized (SEEDER) {
					return new MersenneTwister(SEEDER.nextLong());
				}
			}

		};

		INSTANCE = new SynchronizedMersenneTwister();
	}

	private SynchronizedMersenneTwister() {
		super();
	}

	/**
	 * @return an instance of MersenneTwister
	 */
	public static SynchronizedMersenneTwister getInstance() {
		return INSTANCE;
	}

	private static MersenneTwister current() {
		return LOCAL_RANDOM.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setSeed(long seed) {
		current().setSeed(seed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void nextBytes(byte[] bytes) {
		current().nextBytes(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int nextInt() {
		return current().nextInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int nextInt(int n) {
		return current().nextInt(n);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long nextLong() {
		return current().nextLong();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean nextBoolean() {
		return current().nextBoolean();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float nextFloat() {
		return current().nextFloat();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double nextDouble() {
		return current().nextDouble();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized double nextGaussian() {
		return current().nextGaussian();
	}

}
