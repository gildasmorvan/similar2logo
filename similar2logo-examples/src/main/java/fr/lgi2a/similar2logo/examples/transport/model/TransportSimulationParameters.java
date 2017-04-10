package fr.lgi2a.similar2logo.examples.transport.model;

import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;

public class TransportSimulationParameters extends LogoSimulationParameters {
	
	/**
	 * Set the size of the simulation
	 * @param height the height to set
	 * @param width the width to set
	 */
	public void setSize (int height, int width) {
		this.gridHeight = height;
		this.gridWidth = width;
	}

}
