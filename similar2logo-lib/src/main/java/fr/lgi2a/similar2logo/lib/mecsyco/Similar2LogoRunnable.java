package fr.lgi2a.similar2logo.lib.mecsyco;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.ISimulationModel;

/**
 * This class represents a runnable instance of a predation simulation. 
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class Similar2LogoRunnable implements Runnable {

	
	private ISimulationEngine engine;
	private ISimulationModel model;
	public Similar2LogoRunnable
	  (
	    ISimulationEngine engine,
	    ISimulationModel model
	  ) {
		this.engine = engine;
		this.model = model;
	}
	
	@Override
	public void run() {
		engine.runNewSimulation(model);
	}

}
