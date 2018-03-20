package fr.lgi2a.similar2logo.lib.tools.randomstrategies;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.lgi2a.similar2logo.lib.tools.IRandomValuesGenerator;

public abstract class AbstractRandomBasedRandomValuesGenerator implements IRandomValuesGenerator {
	
	protected Random javaRandomHelper;
	
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
		return Math.PI - javaRandomHelper.nextDouble() * 2 * Math.PI;
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
	public double randomGaussian() {
		return javaRandomHelper.nextGaussian();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shuffle(List<?> l) {
		Collections.shuffle(l, javaRandomHelper);
	}
	 
}
