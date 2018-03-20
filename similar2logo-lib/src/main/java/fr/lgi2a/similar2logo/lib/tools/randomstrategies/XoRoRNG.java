package fr.lgi2a.similar2logo.lib.tools.randomstrategies;

import java.security.SecureRandom;
import java.util.Random;

public final class XoRoRNG extends Random {
	
	private static final long DOUBLE_MASK = (1L << 53) - 1;
    private static final double NORM_53 = 1. / (1L << 53);
    private static final long FLOAT_MASK = (1L << 24) - 1;
    private static final double NORM_24 = 1. / (1L << 24);

	private static final long serialVersionUID = 1018744536171610262L;

    private long state0, state1;

    /**
     * Creates a new generator seeded using secure random.
     */
    public XoRoRNG() {
        this((new SecureRandom()).nextLong());
    }

    public XoRoRNG(final long seed) {
        setSeed(seed);
    }

    @Override
    public final int next(int bits) {
        return (int) (nextLong() & (1L << bits) - 1);
    }

    @Override
    public final long nextLong() {
        final long s0 = state0;
        long s1 = state1;
        final long result = s0 + s1;

        s1 ^= s0;
        state0 = (s0 << 55 | s0 >>> 9) ^ s1 ^ (s1 << 14); // a, b
        state1 = (s1 << 36 | s1 >>> 28); // c
        return result;
    }


    /**
     * Can return any int, positive or negative, of any size permissible in a 32-bit signed integer.
     * @return any int, all 32 bits are random
     */
    @Override
    public int nextInt() {
        return (int)nextLong();
    }

    /**
     * Exclusive on the upper bound.  The lower bound is 0.
     * @param bound the upper bound; should be positive
     * @return a random int less than n and at least equal to 0
     */
    @Override
    public int nextInt( final int bound ) {
        if ( bound <= 0 ) return 0;
        int threshold = (0x7fffffff - bound + 1) % bound;
        for (;;) {
            int bits = (int)(nextLong() & 0x7fffffff);
            if (bits >= threshold)
                return bits % bound;
        }
    }

    /**
     * Exclusive on the upper bound. The lower bound is 0.
     * @param bound the upper bound; should be positive
     * @return a random long less than n
     */
    public long nextLong( final long bound ) {
        if ( bound <= 0 ) return 0;
        long threshold = (0x7fffffffffffffffL - bound + 1) % bound;
        for (;;) {
            long bits = nextLong() & 0x7fffffffffffffffL;
            if (bits >= threshold)
                return bits % bound;
        }
    }

    @Override
    public double nextDouble() {
        return (nextLong() & DOUBLE_MASK) * NORM_53;
    }

    @Override
    public float nextFloat() {
        return (float) ((nextLong() & FLOAT_MASK) * NORM_24);
    }

    @Override
    public boolean nextBoolean() {
        return nextLong() < 0L;
    }

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
     * Sets the seed of this generator using one long, running that through LightRNG's algorithm twice to get the state.
     * @param seed the number to use as the seed
     */
    @Override
    public void setSeed(final long seed) {

        long state = seed + 0x9E3779B97F4A7C15L,
                z = state;
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        state0 = z ^ (z >>> 31);
        state += 0x9E3779B97F4A7C15L;
        z = state;
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        state1 = z ^ (z >>> 31);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XoRoRNG xoRoRNG = (XoRoRNG) o;

        if (state0 != xoRoRNG.state0) return false;
        return state1 == xoRoRNG.state1;
    }

    @Override
    public int hashCode() {
        int result = (int) (state0 ^ (state0 >>> 32));
        result = 31 * result + (int) (state1 ^ (state1 >>> 32));
        return result;
    }

}
