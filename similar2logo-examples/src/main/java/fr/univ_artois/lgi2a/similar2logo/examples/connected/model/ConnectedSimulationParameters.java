package fr.univ_artois.lgi2a.similar2logo.examples.connected.model;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.annotations.Parameter;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;

public class ConnectedSimulationParameters extends LogoSimulationParameters {

	/**
	 * The number of agents in the simulation.
	 */
	@Parameter(
	   name = "number of agents", 
	   description = "the number of agents in the simulation"
	)
	public int nbOfAgents;
	
	/**
	 * Builds a parameters set containing default values.
	 */
	public ConnectedSimulationParameters() {
		super();
		this.gridHeight = 100;
		this.gridWidth = 100;
		this.nbOfAgents = 2000;
		this.xTorus = false;
		this.yTorus = false;
	}
	
}
