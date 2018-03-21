/**
 * Written in 2015 by Sebastiano Vigna (vigna@acm.org)
 * 
 * To the extent possible under law, the author has dedicated all copyright
 * and related and neighboring rights to this software to the public domain
 * worldwide. This software is distributed without any warranty.
 * 
 * See <http://creativecommons.org/publicdomain/zero/1.0/>. 
 */
package fr.lgi2a.similar2logo.lib.tools.random.rng;

import java.util.Random;

/**
 * This is a SplittableRandom-style generator, meant to have a tiny state
 * that permits storing many different generators with low overhead.
 * It should be rather fast, though no guarantees can be made on all hardware.
 * <br>
 * Benchmarking on a Windows laptop with an i7-4700MQ processor running OpenJDK 8
 * reports generation of 64-bit random long output as 17.8x faster than generating
 * an equivalent number of random longs with java.util.Random, and generation of
 * 32-bit random int output as 9.8x faster. Specifically, generating 1 billion longs
 * took about 1.28 nanoseconds per long (1.277 seconds for the whole group) with
 * LightRNG, while java.util.Random (which is meant to produce int, to be fair) took
 * about 22.8 nanoseconds per long (22.797 seconds for the whole group). XorRNG
 * appears to be occasionally faster on int output than LightRNG, but it isn't clear
 * why or what causes that (JIT or GC internals, possibly). XorRNG is slightly
 * slower at generating 64-bit random data, including long and double, but not by
 * a significant degree (a multiplier between 0.9 and 1.2 times). The only deciding
 * factor then is state size, where LightRNG is as small as possible for any JVM
 * object with even a single field: 16 bytes (on a 64-bit JVM; 8-byte objects with
 * 4 bytes or less of non-static members may be possible on 32-bit JVMs but I can't
 * find anything confirming that guess).
 * <br>
 * So yes, this should be very fast, and with only a single long used per LightRNG,
 * it is about as memory-efficient as these generators get.
 * <br>
 * Written in 2015 by Sebastiano Vigna (vigna@acm.org)
 * @author Sebastiano Vigna
 * @author Tommy Ettinger
 */
public final class LightRNG extends Random {

	private static final long serialVersionUID = -374415589203474497L;

    public long state; /* The state can be seeded with any value. */

    /** 
     * Creates a new generator seeded using Math.random. 
     */
    public LightRNG() {
        this(
        		(long) ((Math.random() - 0.5) * 0x10_0000_0000_0000L)
          ^ (long) (((Math.random() - 0.5) * 2.0) * 0x8000_0000_0000_0000L)
        );
    }

    /** 
     * Creates a new generator with the given seed. 
     */
    public LightRNG( final long seed ) {
        setSeed(seed);
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public int next( int bits ) {
        long z = state += 0x9E37_79B9_7F4A_7C15L;
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return (int)(z ^ (z >>> 31)) >>> (32 - bits);
    }
    /**
     * Gets a pseudo-random int with at most the specified count of bits; for example, if bits is 3 this can return any
     * int between 0 and 2 to the 3 (that is, 8), exclusive on the upper end. That would mean 7 could be returned, but
     * no higher ints, and 0 could be returned, but no lower ints.
     *
     * The algorithm used here is the older version of {@link #next(int)} before some things changed on March 8 2018.
     * Using this method is discouraged unless you need to reproduce values exactly; it's slightly slower for no good
     * reason. Calling {@code next(32)} and {@code compatibleNext(32)} should have identical results, but other values
     * for bits will probably be different.
     * @param bits the number of bits to be returned; if 0 or less, or if 32 or greater, can return any 32-bit int
     * @return a pseudo-random int that uses at most the specified amount of bits
     */
    public int compatibleNext( int bits ) {
        long z = state += 0x9E37_79B9_7F4A_7C15L;
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return (int)( (z ^ (z >>> 31)) & ( 1L << bits ) - 1 );
    }
    
    /**
	 * {@inheritDoc}
	 */
	@Override
    public long nextLong() {
        long z = state += 0x9E37_79B9_7F4A_7C15L;
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return z ^ (z >>> 31);
    }

    /**
     * Produces a copy of this RandomnessSource that, if next() and/or nextLong() are called on this object and the
     * copy, both will generate the same sequence of random numbers from the point copy() was called. This just needs to
     * copy the state so it isn't shared, usually, and produce a new value with the same exact state.
     *
     * @return a copy of this RandomnessSource
     */
    public LightRNG copy() {
        return new LightRNG(state);
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public int nextInt() {
        long z = state += 0x9E37_79B9_7F4A_7C15L;
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return (int) (z ^ (z >>> 31)); 
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public int nextInt( final int bound ) {
        long z = state += 0x9E37_79B9_7F4A_7C15L;
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return (int) ((bound * ((z ^ (z >>> 31)) & 0x7FFF_FFFFL)) >> 31);
    }

    /**
     * Like {@link #compatibleNext(int)}, but for compatibility with {@link #nextInt(int)}.
     * Exclusive on the upper bound.  The lower bound is 0.
     * @param bound the upper bound; should be positive
     * @return a random int less than n and at least equal to 0
     */
    public int compatibleNextInt( final int bound ) {
        if (bound <= 0) {
        		return 0;
        }
        int threshold = (0x7fff_ffff - bound + 1) % bound;
        for (; ; ) {
            int bits = (int) (nextLong() & 0x7fff_ffff);
            if (bits >= threshold) {
            		return bits % bound;
            }
        }
    }

    /**
     * Inclusive lower, exclusive upper.
     * @param lower the lower bound, inclusive, can be positive or negative
     * @param upper the upper bound, exclusive, should be positive, must be greater than lower
     * @return a random int between lower (inclusive) and upper (exclusive)
     */
    public int compatibleNextInt( final int lower, final int upper ) {
        if ( upper - lower <= 0 ) {
        		throw new IllegalArgumentException("Upper bound must be greater than lower bound");
        }
        return lower + compatibleNextInt(upper - lower);
    }
    
    /**
	 * {@inheritDoc}
	 */
	@Override
    public double nextDouble() {
        long z = state += 0x9E37_79B9_7F4A_7C15L;
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return ((z ^ (z >>> 31)) & 0x1f_ffff_ffff_ffffL) * 0x1p-53;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public float nextFloat() {
        long z = state += 0x9E37_79B9_7F4A_7C15L;
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return ((z ^ (z >>> 31)) & 0xff_ffffL) * 0x1p-24F;

    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public boolean nextBoolean() {
        return nextLong() < 0L;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public void nextBytes( final byte[] bytes ) {
        int i = bytes.length, n;
        while( i != 0 ) {
            n = Math.min( i, 8 );
            for ( long bits = nextLong(); n-- != 0; bits >>>= 8 ) {
            		bytes[ --i ] = (byte)bits;
            }
        }
    }


    /**
	 * {@inheritDoc}
	 */
	@Override
    public void setSeed( final long seed ) {
        state = seed;
    }
    /**
     * Sets the seed (also the current state) of this generator.
     * @param seed the seed to use for this LightRNG, as if it was constructed with this seed.
     */
    public void setState( final long seed ) {
        state = seed;
    }
    /**
     * Gets the current state of this generator.
     * @return the current seed of this LightRNG, changed once per call to nextLong()
     */
    public long getState() {
        return state;
    }

    /**
     * Advances or rolls back the LightRNG's state without actually generating each number. Skips forward
     * or backward a number of steps specified by advance, where a step is equal to one call to nextLong(),
     * and returns the random number produced at that step (you can get the state with {@link #getState()}).
     *
     * @param advance Number of future generations to skip over; can be negative to backtrack, 0 gets the most recent generated number
     * @return the random long generated after skipping advance numbers
     */
    public long skip(long advance) {
        long z = (state += 0x9E37_79B9_7F4A_7C15L * advance);
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return z ^ (z >>> 31);
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public boolean equals(Object o) {
        if (this == o) {
        		return true;
        }
        if (o == null || getClass() != o.getClass()) {
        		return false;
        }

        LightRNG lightRNG = (LightRNG) o;

        return state == lightRNG.state;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public int hashCode() {
        return (int) (state ^ (state >>> 32));
    }

    public static long determine(long state) {
        return (
        		(
        			state = (
        				(state = (
        					(state *= 0x9E37_79B9_7F4A_7C15L) ^ state >>> 30) * 0xBF58_476D_1CE4_E5B9L
        				) ^ state >>> 27
        			) * 0x94D0_49BB_1331_11EBL
        		) ^ state >>> 31
        	);
    }
    public static long determine(final int a, final int b) {
        long state = 0x9E37_79B9_7F4A_7C15L + (a & 0xFFFF_FFFFL) + ((long)b << 32);
        state = ((state >>> 30) ^ state) * 0xBF58_476D_1CE4_E5B9L;
        state = (state ^ (state >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return state ^ (state >>> 31);
    }

    public static int determineBounded(long state, final int bound) {
        state = (((state *= 0x9E37_79B9_7F4A_7C15L) >>> 30) ^ state) * 0xBF58_476D_1CE4_E5B9L;
        state = (state ^ (state >>> 27)) * 0x94D0_49BB_1331_11EBL;
        return (int)((bound * ((state ^ (state >>> 31)) & 0x7FFFFFFFL)) >> 31);
    }

    /**
     * Returns a random float that is deterministic based on state; if state is the same on two calls to this, this will
     * return the same float. This is expected to be called with a changing variable, e.g. {@code determine(++state)},
     * where the increment for state should be odd but otherwise doesn't really matter. This multiplies state by
     * {@code 0x9E3779B97F4A7C15L} within this method, so using a small increment won't be much different from using a
     * very large one, as long as it is odd. The period is 2 to the 64 if you increment or decrement by 1, but there are
     * only 2 to the 30 possible floats between 0 and 1.
     * @param state a variable that should be different every time you want a different random result;
     *              using {@code determine(++state)} is recommended to go forwards or {@code determine(--state)} to
     *              generate numbers in reverse order
     * @return a pseudo-random float between 0f (inclusive) and 1f (exclusive), determined by {@code state}
     */
    public static float determineFloat(long state) {
    		return (
    			(
    				(
    					state = (
    						(
	    						state = (
	    							(state *= 0x9E37_79B9_7F4A_7C15L) ^ state >>> 30
    						) * 0xBF58_476D_1CE4_E5B9L
    					) ^ state >>> 27
    				) * 0x94D0_49BB_1331_11EBL) ^ state >>> 31
    			) & 0xFFFFFF
    		) * 0x1p-24F;
    	}

    /**
     * Returns a random double that is deterministic based on state; if state is the same on two calls to this, this
     * will return the same float. This is expected to be called with a changing variable, e.g.
     * {@code determine(++state)}, where the increment for state should be odd but otherwise doesn't really matter. This
     * multiplies state by {@code 0x9E3779B97F4A7C15L} within this method, so using a small increment won't be much
     * different from using a very large one, as long as it is odd. The period is 2 to the 64 if you increment or
     * decrement by 1, but there are only 2 to the 62 possible doubles between 0 and 1.
     * @param state a variable that should be different every time you want a different random result;
     *              using {@code determine(++state)} is recommended to go forwards or {@code determine(--state)} to
     *              generate numbers in reverse order
     * @return a pseudo-random double between 0.0 (inclusive) and 1.0 (exclusive), determined by {@code state}
     */
    public static double determineDouble(long state) {
    		return (
    			(
    				(
    					state = (
    						(
    							state = (
    								(state *= 0x9E37_79B9_7F4A_7C15L) ^ state >>> 30
    							) * 0xBF58_476D_1CE4_E5B9L
    						) ^ state >>> 27
    					) * 0x94D0_49BB_1331_11EBL
    				) ^ state >>> 31
    			) & 0x1F_FFFF_FFFF_FFFFL
    		) * 0x1p-53;
    	}
}
