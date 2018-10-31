/** 
 *  Written in 2016 by David Blackman and Sebastiano Vigna (vigna@acm.org)
 * To the extent possible under law, the author has dedicated all copyright
 * and related and neighboring rights to this software to the public domain
 * worldwide. This software is distributed without any warranty.
 * See <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package fr.univ_artois.lgi2a.similar2logo.lib.tools.random.rng;

import java.security.SecureRandom;
import java.util.Random;

/**
 * A port of Blackman and Vigna's xoroshiro 128+ generator; should be very fast and produce high-quality output.
 * Testing shows it is within 5% the speed of LightRNG, sometimes faster and sometimes slower, and has a larger period.
 * It's called XoRo because it involves Xor as well as Rotate operations on the 128-bit pseudo-random state.
 * <br>
 * <a href="http://xoroshiro.di.unimi.it/xoroshiro128plus.c">Original version here.</a>
 * <br>
 * Written in 2016 by David Blackman and Sebastiano Vigna (vigna@acm.org)
 *
 * @author Sebastiano Vigna
 * @author David Blackman
 * @author Tommy Ettinger (if there's a flaw, use SquidLib's issues and don't bother Vigna or Blackman, it's probably a mistake in SquidLib's implementation)
 */
public final class XoRoRNG extends Random {
	
	private static final long DOUBLE_MASK = (1L << 53) - 1;
    private static final double NORM_53 = 1. / (1L << 53);
    private static final long FLOAT_MASK = (1L << 24) - 1;
    private static final double NORM_24 = 1. / (1L << 24);

	private static final long serialVersionUID = 1018744536171610262L;

    private long state0;
    private long state1;

    /**
     * Creates a new generator seeded using secure random.
     */
    public XoRoRNG() {
        this((new SecureRandom()).nextLong());
    }

    /**
     * Creates a new generator with the given seed.
     */
    public XoRoRNG(final long seed) {
        setSeed(seed);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int next(int bits) {
        return (int) (nextLong() & (1L << bits) - 1);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public long nextLong() {
        final long s0 = state0;
        long s1 = state1;
        final long result = s0 + s1;

        s1 ^= s0;
        state0 = (s0 << 55 | s0 >>> 9) ^ s1 ^ (s1 << 14);
        state1 = (s1 << 36 | s1 >>> 28);
        return result;
    }


    /**
	 * {@inheritDoc}
	 */
    @Override
    public int nextInt() {
        return (int)nextLong();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int nextInt( final int bound ) {
        if ( bound <= 0 ) {
        		return 0;
        }
        int threshold = (0x7fff_ffff - bound + 1) % bound;
        for (;;) {
            int bits = (int)(nextLong() & 0x7fff_ffff);
            if (bits >= threshold) {
            		return bits % bound;
            }
        }
    }

    /**
     * Exclusive on the upper bound. The lower bound is 0.
     * @param bound the upper bound; should be positive
     * @return a random long less than n
     */
    public long nextLong( final long bound ) {
        if ( bound <= 0 ) {
        		return 0;
        }
        long threshold = (0x7fff_ffff_ffff_ffffL - bound + 1) % bound;
        for (;;) {
            long bits = nextLong() & 0x7fff_ffff_ffff_ffffL;
            if (bits >= threshold) {
            		return bits % bound;
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public double nextDouble() {
        return (nextLong() & DOUBLE_MASK) * NORM_53;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public float nextFloat() {
        return (float) ((nextLong() & FLOAT_MASK) * NORM_24);
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
    public void nextBytes(final byte[] bytes) {
        int i = bytes.length, n = 0;
        while (i != 0) {
            n = Math.min(i, 8);
            for (long bits = nextLong(); n-- != 0; bits >>>= 8) {
                bytes[--i] = (byte) bits;
            }
        }
    }

    /**
     * Sets the seed of this generator using one long,
     * running that through LightRNG's algorithm twice to get the state.
     * @param seed the number to use as the seed
     */
    @Override
    public void setSeed(final long seed) {

        long state = seed + 0x9E37_79B9_7F4A_7C15L,
        		 z = state;
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        state0 = z ^ (z >>> 31);
        state += 0x9E37_79B9_7F4A_7C15L;
        z = state;
        z = (z ^ (z >>> 30)) * 0xBF58_476D_1CE4_E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D0_49BB_1331_11EBL;
        state1 = z ^ (z >>> 31);
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

        XoRoRNG xoRoRNG = (XoRoRNG) o;

        if (state0 != xoRoRNG.state0) {
        		return false;
        }
        return state1 == xoRoRNG.state1;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int hashCode() {
        int result = (int) (state0 ^ (state0 >>> 32));
        result = 31 * result + (int) (state1 ^ (state1 >>> 32));
        return result;
    }

}
