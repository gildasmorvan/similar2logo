package fr.lgi2a.similar2logo.lib.tools;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;

/**
 * A thread where a Similar simulation is performed.
 * @author <a href="http://www.lgi2a.univ-artois.fr/" target="_blank">LGI2A</a> -- <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class SimulationExecutionThread extends Thread {
	/**
	 * The simulation model used for the execution of the simulation.
	 */
	private LogoSimulationModel simulationModel;
	/**
	 * The simulation engine used for the execution of the simulation.
	 */
	private ISimulationEngine simulationEngine;
	
	/**
	 * Constructor of the class {@link SimulationExecutionThread}.
	 * @param simulationEngine The simulation model used for the execution of the simulation.
	 * @param simulationModel The simulation engine used for the execution of the simulation.
	 */
	public SimulationExecutionThread( 
		ISimulationEngine simulationEngine,
		LogoSimulationModel simulationModel
	){
		this.simulationEngine = simulationEngine;
		this.simulationModel = simulationModel;
	}
	
	public void run() {
		try{
			this.getSimulationEngine().runNewSimulation( this.simulationModel );
		} catch( RuntimeException e ) {
			e.printStackTrace();
		}
	}

	public ISimulationEngine getSimulationEngine() {
		return simulationEngine;
	}

	public void setSimulationEngine(ISimulationEngine simulationEngine) {
		this.simulationEngine = simulationEngine;
	}
}
