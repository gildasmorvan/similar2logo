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
import fr.lgi2a.similar2logo.lib.tools.random.rng.XoRoRNG;

public class RandomValuesGenerator implements IRandomValuesGenerator {
	
    public static final String XORO = "xoroshiro128+";
    
    public static final String XOR = "xorshift128+";
    
    public static final String LIGHT = "SplitMix64";
    
    public static final String MT_64 = "MT19937-64";
    
    public static final String WELL_1024 = "Well1024a";
    
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
			random = RandomGeneratorFactory.createRandomGenerator(new XoRoRNG());
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
	 * {@inheritDoc}
	 */
	@Override
	public double randomDouble() {
		return random.nextDouble();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * {@inheritDoc}
	 */
	@Override
	public double randomAngle() {
		return Math.PI - random.nextDouble() * 2 * Math.PI;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean randomBoolean() {
		return random.nextBoolean();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int randomInt(int bound) {
		return random.nextInt(bound);
	}

	/**
	 * {@inheritDoc}
	 */
	public double randomGaussian() {
		return random.nextGaussian();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void shuffle(List<?> list) {
		int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i=size; i>1; i--)
                swap(list, i-1, random.nextInt(i));
        } else {
            Object arr[] = list.toArray();

            // Shuffle array
            for (int i=size; i>1; i--)
                swap(arr, i-1, random.nextInt(i));

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
    public static void swap(List<?> list, int i, int j) {
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
