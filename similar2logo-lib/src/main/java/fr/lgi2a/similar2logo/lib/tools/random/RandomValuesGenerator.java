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
package fr.lgi2a.similar2logo.lib.tools.random;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import org.apache.commons.math3.random.SynchronizedRandomGenerator;
import org.apache.commons.math3.random.Well1024a;
import org.eclipse.jetty.util.log.Log;

import fr.lgi2a.similar2logo.lib.tools.random.rng.LightRNG;
import fr.lgi2a.similar2logo.lib.tools.random.rng.PermutedRNG;
import fr.lgi2a.similar2logo.lib.tools.random.rng.XoRoRNG;
import fr.lgi2a.similar2logo.lib.tools.random.rng.XorRNG;

public class RandomValuesGenerator {
	
    public static final String XORO = "xoroshiro128+";
    
    public static final String XOR = "xorshift128+";
    
    public static final String LIGHT = "SplitMix64";
    
    public static final String MT_64 = "MT19937-64";
    
    public static final String WELL_1024 = "Well1024a";
    
    public static final String PCG = "PCG";
    
    public static final String JDK = "jdk";
	
	private static final int SHUFFLE_THRESHOLD = 5;
    
	protected RandomGenerator random;
	
	/**
	 * Build a new instance of this class with a given rng.
	 * @param random the rng to be used
	 */
	public RandomValuesGenerator(RandomGenerator random) {
		this.random = random;
	}
	
	/**
	 * Build a new instance of this class with a given rng.
	 * @param random the rng to be used
	 * @param sync <code>true</code> if the generator is synchronized
	 */
	public RandomValuesGenerator(RandomGenerator random, boolean sync) {
		if(sync) {
			this.random = new SynchronizedRandomGenerator(random);
		} else {
			this.random = random;
		}
	}
	
	/**
	 * Build a new instance of this class with a given Random instance.
	 * @param random the Random instance to be used as generator
	 * @param sync <code>true</code> if the generator is synchronized
	 */
	public RandomValuesGenerator(Random random, boolean sync) {
		if(sync) {
			this.random = new SynchronizedRandomGenerator(
				RandomGeneratorFactory.createRandomGenerator(random)
			);
		} else {
			this.random = RandomGeneratorFactory.createRandomGenerator(random);
		}
	}
	
	/**
	 * Build a new instance of this class with a given Random instance.
	 * @param random the Random instance to be used as generator
	 */
	public RandomValuesGenerator(Random random) {
		this.random = RandomGeneratorFactory.createRandomGenerator(random);
	}
	
	/**
	 * Build a new instance of this class with a given rng name.
	 * @param random thename of the rng.
	 */
	public RandomValuesGenerator(String random) {
		this.random = getRandomGenerator(random, false);
	}
	
	/**
	 * Build a new instance of this class with a given rng name.
	 * @param random thename of the rng.
	 * @param sync <code>true</code> if the generator is synchronized
	 */
	public RandomValuesGenerator(String random, boolean sync) {
		this.random = getRandomGenerator(random, sync);
	}
	
	/**
	 * Build a new instance of this class with a given rng name and seed.
	 * @param random the name of the rng
	 * @param seed the seed of the rng
	 */
	public RandomValuesGenerator(String random, long seed) {
		this.random = getRandomGenerator(random, false);
		this.random.setSeed(seed);
	}
	
	/**
	 * Build a new instance of this class with a given rng name and seed.
	 * @param random the name of the rng
	 * @param seed the seed of the rng
	 * @param sync <code>true</code> if the generator is synchronized
	 */
	public RandomValuesGenerator(String random, long seed, boolean sync) {
		this.random = getRandomGenerator(random, sync);
		this.random.setSeed(seed);
	}
	
	/**
	 * @param randomLib  the name of the rng
	 * @param sync <code>true</code> if the generator is synchronized
	 * @return the corresponding rng
	 */
	private static RandomGenerator getRandomGenerator(String randomLib, boolean sync) {
		RandomGenerator random;
		switch(randomLib) {
		case XORO:
			random = RandomGeneratorFactory.createRandomGenerator(new XoRoRNG());
			break;
		case XOR:
			random = RandomGeneratorFactory.createRandomGenerator(new XorRNG());
			break;
		case LIGHT:
			random = RandomGeneratorFactory.createRandomGenerator(new LightRNG());
			break;	
		case MT_64:
			random = new MersenneTwister();
			break;
		case JDK:
			random = RandomGeneratorFactory.createRandomGenerator(new Random());
			break;
		case WELL_1024:
			random = new Well1024a();
			break;
		case PCG:
			random = RandomGeneratorFactory.createRandomGenerator(new PermutedRNG());
			break;
		default:
			Log.getRootLogger().warn("Unknown RNG ("+randomLib+"). Set to default ("+XORO+")");
			random = RandomGeneratorFactory.createRandomGenerator(new XoRoRNG());
		}
		if(sync) {
			return new SynchronizedRandomGenerator(random);
		}
		return random;
	}
	
	/**
	 * Gets a random number between 0 (included) and 1 (excluded).
	 * @return A random number between 0 (included) and 1 (excluded).
	 */
	public double randomDouble() {
		return random.nextDouble();
	}
	
	/**
	 * Generates a random double within a range.
	 * @param lowerBound The lower bound of the generation (included).
	 * @param higherBound The higher bound of the generation (excluded).
	 * @return A random double within the range <code>[lowerBound, higherBound[</code>.
	 * @throws IllegalArgumentException If <code>lowerBound</code> is 
	 * higher or equal to <code>higherBound</code>.
	 */
	public double randomDouble(
			double lowerBound, 
			double higherBound
	) {
		if( lowerBound >= higherBound ) {
			throw new IllegalArgumentException( "The lower bound " + lowerBound + " is greater " +
					"or equal to the higher bound " + higherBound  );
		}
		return (higherBound - lowerBound) * this.random.nextDouble() + lowerBound;
	}

	/**
	 * Gets a random angle between -pi (included) and pi (excluded).
	 * @return a random angle between -pi (included) and pi (excluded).
	 */
	public double randomAngle() {
		return Math.PI - random.nextDouble() * 2 * Math.PI;
	}

	/**
	 * Gets a random boolean.
	 * @return A random boolean.
	 */
	public boolean randomBoolean() {
		return random.nextBoolean();
	}

	/**
	 * Gets a random integer.
	 * @return A random integer.
	 */
	public int randomInt(int bound) {
		return random.nextInt(bound);
	}

	/**
	 * Gets a Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0
	 * @return A Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0
	 */
	public double randomGaussian() {
		return random.nextGaussian();
	}
	
	/**
	 *  Shuffles the given collection.
	 *  
	 * @param l the collection to shuffle
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void shuffle(List<?> list) {
		int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i=size; i>1; i--) {
            		swap(list, i-1, random.nextInt(i));
            }
        } else {
            Object arr[] = list.toArray();

            // Shuffle array
            for (int i=size; i>1; i--) {
            		swap(arr, i-1, random.nextInt(i));
            }

            // Dump array back into list
            // instead of using a raw type here, it's possible to capture
            // the wildcard but it will require a call to a supplementary
            // private method
            ListIterator it = list.listIterator();
            for (int i=0; i<arr.length; i++) {
                it.next();
                it.set(arr[i]);
            }
        }

	}
	
    
	 /**
     * Swaps the elements at the specified positions in the specified list.
     * (If the specified positions are equal, invoking this method leaves
     * the list unchanged.)
     *
     * @param list The list in which to swap elements.
     * @param i the index of one element to be swapped.
     * @param j the index of the other element to be swapped.
     * @throws IndexOutOfBoundsException if either <tt>i</tt> or <tt>j</tt>
     *         is out of range (i &lt; 0 || i &gt;= list.size()
     *         || j &lt; 0 || j &gt;= list.size()).
     * @since 1.4
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void swap(List<?> list, int i, int j) {
        // instead of using a raw type here, it's possible to capture
        // the wildcard but it will require a call to a supplementary
        // private method
        final List l = list;
        l.set(i, l.set(j, l.get(i)));
    }
    
    /**
     * Swaps the two specified elements in the specified array.
     */
    private static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

	 
}
