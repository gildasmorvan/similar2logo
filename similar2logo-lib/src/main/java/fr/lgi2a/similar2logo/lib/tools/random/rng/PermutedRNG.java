/*
 * Ported to Java from the PCG library. Its copyright header follows:
 *
 * PCG Random Number Generation for C++
 *
 * Copyright 2014 Melissa O'Neill <oneill@pcg-random.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For additional information about the PCG random number generation scheme,
 * including its license and other licensing options, visit
 *
 *     http://www.pcg-random.org
 */
package fr.lgi2a.similar2logo.lib.tools.random.rng;

import java.security.SecureRandom;
import java.util.Random;

/**
 * This is a RandomnessSource in the PCG-Random family. It performs pseudo-
 * random modifications to the output based on the techniques from the
 * Permuted Congruential Generators created by M.E. O'Neill.
 * Specifically, this variant is:
 * RXS M XS -- random xorshift, mcg multiply, fixed xorshift
 *
 * The most statistically powerful generator, but all those steps
 * make it slower than some of the others.
 * <br>
 * Even though benchmarks on similar programs in C would lead you to
 * believe this should be somewhat faster than LightRNG, benchmarking
 * with JMH seems to show LightRNG being roughly 16% faster than
 * PermutedRNG, and both drastically faster than java.util.Random .
 * This generator was implemented incorrectly for a large part of its history,
 * but it seems correct now, though it may be a little slower.
 * @author Melissa E. O'Neill (Go HMC!)
 * @author Tommy Ettinger
 */
public final class PermutedRNG extends Random {
	/** 2 raised to the 53, - 1. */
    private static final long DOUBLE_MASK = ( 1L << 53 ) - 1;
    /** 2 raised to the -53. */
    private static final double NORM_53 = 1. / ( 1L << 53 );
    /** 2 raised to the 24, -1. */
    private static final long FLOAT_MASK = ( 1L << 24 ) - 1;
    /** 2 raised to the -24. */
    private static final double NORM_24 = 1. / ( 1L << 24 );
    /**
     * The state can be seeded with any value.
     */
    public long state;

	private static final long serialVersionUID = 3748443966125527657L;

    /**
     * Creates a new generator seeded using SecureRandom.
     */
    public PermutedRNG() {
        this((long) (((new SecureRandom()).nextLong() - 0.5) * 0x10_0000_0000_0000L)
                ^ (long) ((((new SecureRandom()).nextLong() - 0.5) * 2.0) * 0x8000_0000_0000_0000L));
    }

    /**
     * Constructs a new PermutedRNG with the given seed as its state, exactly.
     * @param seed a long that will be used as-is for the state of a new PermutedRNG
     */
    public PermutedRNG(final long seed) {
        state = seed;
    }

    /**
     * Gets a random int with at most the specified number of bits.
     * Equivalent in its effect on the state to calling nextLong() exactly one time.
     * @param bits the number of bits to be returned, between 1 and 32
     * @return a pseudo-random int with at most the specified bits
     */
    @Override
    public int next( final int bits ) {
        long p = (state = state * 0x5851_F42D_4C95_7F2DL + 0x1405_7B7E_F767_814FL);
        p = (p ^ p >>> (5 + (p >>> 59))) * 0xAEF1_7502_108E_F2D9L;
        return (int)(p ^ p >>> 43) >>> (32 - bits);
    }

    /**
     * Can return any int, positive or negative, of any size permissible in a 32-bit signed integer.
     * Equivalent in its effect on the state to calling nextLong() exactly one time.
     * @return any int, all 32 bits are random
     */
    @Override
    public int nextInt() {
        long p = (state = state * 0x5851_F42D_4C95_7F2DL + 0x1405_7B7E_F767_814FL);
        p = (p ^ p >>> (5 + (p >>> 59))) * 0xAEF1_7502_108E_F2D9L;
        return (int)(p ^ p >>> 43);
    }
    /**
     * Can return any long, positive or negative, of any size permissible in a 64-bit signed integer.
     *
     * @return any long, all 64 bits are random
     */
    @Override
    public long nextLong() {
        // increment  = 1442695040888963407L;
        // multiplier = 6364136223846793005L;

        long p = (state = state * 0x5851_F42D_4C95_7F2DL + 0x1405_7B7E_F767_814FL);
        p = (p ^ p >>> (5 + (p >>> 59))) * 0xAEF1_7502_108E_F2D9L;
        return (p ^ p >>> 43);
    }

    /**
     * Exclusive on the outer bound; the inner bound is 0.
     * Will call nextLong() with no arguments exactly once.
     * @param bound the upper bound; can be positive or negative
     * @return a random int less than n and at least equal to 0
     */
    @Override
    public int nextInt( final int bound ) {
        return (int)((bound * (nextLong() & 0x7FFF_FFFFL)) >> 31);
    }

    /**
     * Inclusive lower, exclusive upper. The upper bound is really the outer bound, and the lower bound the inner bound,
     * because upper is permitted to be less than lower, though upper is still exclusive there.
     * Will call nextLong() with no arguments exactly once.
     * @param lower the lower bound, inclusive, can be positive or negative
     * @param upper the upper bound, exclusive, can be positive or negative
     * @return a random int at least equal to lower and less than upper (if upper is less than lower, then the result
     *         will be less than or equal to lower and greater than upper)
     */
    public int nextInt( final int lower, final int upper ) {
        return lower + nextInt(upper - lower);
    }

    /**
     * Exclusive on the upper bound n. The lower bound is 0.
     *
     * Will call nextLong() with no arguments at least 1 time, possibly more.
     * @param n the upper bound; should be positive
     * @return a random long less than n
     */
    public long nextLong( final long n ) {
        if ( n <= 0 ) {
        	return 0;
        }
        long threshold = (0x7fff_ffff_ffff_ffffL - n + 1) % n;
        for (;;) {
            long bits = nextLong() & 0x7fff_ffff_ffff_ffffL;
            if (bits >= threshold) {
            	return bits % n;
            }
        }
    }

    /**
     * Exclusive on the upper bound n. The lower bound is 0.
     *
     * Will call nextLong() at least 1 time, possibly more.
     * @param lower the lower bound, inclusive, can be positive or negative
     * @param upper the upper bound, exclusive, should be positive, must be greater than lower
     * @return a random long at least equal to lower and less than upper
     */
    public long nextLong( final long lower, final long upper ) {
        if ( upper - lower <= 0 ) {
        	return 0;
        }
        return lower + nextLong(upper - lower);
    }

    /**
     * Gets a uniform random double in the range [0.0,1.0)
     *
     * Calls nextLong() exactly one time.
     *
     * @return a random double at least equal to 0.0 and less than 1.0
     */
    @Override
    public double nextDouble() {
        return ( nextLong() & DOUBLE_MASK ) * NORM_53;
    }

    /**
     * Gets a uniform random double in the range [0.0,outer) given a positive parameter outer. If outer
     * is negative, it will be the (exclusive) lower bound and 0.0 will be the (inclusive) upper bound.
     *
     * Calls nextLong() exactly one time.
     *
     *  @param outer the exclusive outer bound, can be negative
     * @return a random double between 0.0 (inclusive) and outer (exclusive)
     */
    public double nextDouble(final double outer) {
        return nextDouble() * outer;
    }

    /**
     * Gets a uniform random float in the range [0.0,1.0)
     *
     * Calls nextLong() exactly one time.
     *
     * @return a random float at least equal to 0.0f and less than 1.0f
     */
    @Override
    public float nextFloat() {
        return (float)( ( nextLong() & FLOAT_MASK ) * NORM_24 );
    }

    /**
     * Gets a random value, true or false.
     * Calls nextLong() once.
     * @return a random true or false value.
     */
    @Override
    public boolean nextBoolean() {
        return nextLong() < 0L;
    }

    /**
     * Given a byte array as a parameter, this will fill the array with random bytes (modifying it
     * in-place). Calls nextLong() {@code Math.ceil(bytes.length / 8.0)} times.
     * @param bytes a byte array that will have its contents overwritten with random bytes.
     */
    @Override
    public void nextBytes( final byte[] bytes ) {
        int i = bytes.length, n = 0;
        while( i != 0 ) {
            n = Math.min(i, 8 );
            for ( long bits = nextLong(); n-- != 0; bits >>>= 8 ) {
            	bytes[ --i ] = (byte)bits;
            }
        }
    }


    /**
     * Sets the seed of this generator (which is also the current state).
     * @param seed the seed to use for this PermutedRNG, as if it was constructed with this seed.
     */
    @Override
    public void setSeed( final long seed ) {
        state = seed;
    }
    /**
     * Sets the seed (also the current state) of this generator.
     * @param seed the seed to use for this PermutedRNG, as if it was constructed with this seed.
     */
	public void setState( final long seed ) {
        state = seed;
    }
    /**
     * Gets the current state of this generator.
     * @return the current seed of this PermutedRNG, changed once per call to nextLong()
     */
	public long getState( ) {
        return state;
    }

    /**
     * Advances or rolls back the PermutedRNG's state without actually generating each number. Skips forward
     * or backward a number of steps specified by advance, where a step is equal to one call to nextLong(),
     * and returns the random number produced at that step (you can get the state with {@link #getState()}).
     * Skipping ahead or behind takes more than constant time, unlike with {@link LightRNG}, but less time
     * than calling nextLong() {@code advance} times. Skipping backwards by one step is the worst case for this.
     * @param advance Number of future generations to skip past. Can be negative to backtrack.
     * @return the number that would be generated after generating advance random numbers.
     */
    public long skip(long advance) {
        // The method used here is based on Brown, "Random Number Generation
        // with Arbitrary Stride,", Transactions of the American Nuclear
        // Society (Nov. 1994).  The algorithm is very similar to fast
        // exponentiation.
        //
        // Even though advance is a signed long, it is treated as unsigned, effectively, for the purposes
        // of how many iterations it goes through (at most 63 for forwards, 64 for "backwards").
        long acc_mult = 1, acc_plus = 0, cur_mult = 0x5851_F42D_4C95_7F2DL, cur_plus = 0x1405_7B7E_F767_814FL;

        while (advance > 0L) {
            if ((advance & 1L) != 0L) {
                acc_mult *= cur_mult;
                acc_plus = acc_plus*cur_mult + cur_plus;
            }
            cur_plus *= (cur_mult+1L);
            cur_mult *= cur_mult;
            advance >>>= 1;
        }
        long p = (state = acc_mult * state + acc_plus);
        p = (p ^ p >>> (5 + (p >>> 59))) * 0xAEF1_7502_108E_F2D9L;
        return (p ^ p >>> 43);
    }

    /**
     * Given suitably-different inputs as {@code state}, this will permute that state to get a seemingly-unrelated
     * number. Unlike {@link LightRNG#determine(long)}, this will not work with inputs that are sequential, and it is
     * recommended that subsequent calls change state with a linear congruential generator like
     * {@code PermutedRNG.determine(state = state * 0x5851F42D4C957F2DL + 0x14057B7EF767814FL)}. It will be correct for
     * any inputs, but if {@code state} is 0, then this will return 0.
     * @param state a long that should be changed with {@code state = state * 0x5851F42D4C957F2DL + 0x14057B7EF767814FL}
     * @return a pseudo-random long determined from state
     */
    public static long determine(long state) {
        state = (state ^ state >>> (5 + (state >>> 59))) * 0xAEF1_7502_108E_F2D9L;
        return (state >>> 43) ^ state;
    }

    /**
     * Given suitably-different inputs as {@code state}, this will permute that state to get a seemingly-unrelated
     * number as an int between 0 and bound. Unlike {@link LightRNG#determine(long)}, this will not work with inputs
     * that are sequential, and it is recommended that subsequent calls change state with a linear congruential
     * generator like {@code PermutedRNG.determine(state = state * 0x5851F42D4C957F2DL + 0x14057B7EF767814FL)}. It will
     * be correct for any inputs, but if {@code state} is 0, then this will return 0.
     * @param state a long that should be changed with {@code state = state * 0x5851F42D4C957F2DL + 0x14057B7EF767814FL}
     * @param bound the exclusive outer bound on the numbers this can produce, as an int
     * @return a pseudo-random int between 0 (inclusive) and bound (exclusive) determined from state
     */
    public static int determineBounded(long state, final int bound) {
        state ^= state >>> (5 + (state >>> 59));
        return (int)((bound * ((((state *= 0xAEF1_7502_108E_F2D9L) >>> 43) ^ state) & 0x7FFF_FFFFL)) >> 31);
    }
}
