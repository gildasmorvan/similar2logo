package fr.univ_artois.lgi2a.similar2logo.examples.predation.probes;

import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.agents.PredatorCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.agents.PreyCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;


public class SimplePopulationProbe implements IProbe {
	
	public int nbOfPreys = 0;
	public int nbOfPredators = 0;
	
	@Override
	public void prepareObservation() {
		nbOfPreys = 0;
		nbOfPredators = 0;
	}

	@Override
	public void observeAtFinalTime(
		SimulationTimeStamp initialTimestamp,
		ISimulationEngine simulationEngine
	) {
		this.displayPopulation( initialTimestamp, simulationEngine );
	}

	
	/**
	 * Update the population of agents in x, y and z local fields.
	 * @param timestamp The time stamp when the observation is made.
	 * @param simulationEngine The engine where the simulation is running.
	 */
	private void displayPopulation(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	){
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get( 
			LogoSimulationLevelList.LOGO
		);

		nbOfPreys = 0;
		nbOfPredators = 0;
		
		for( ILocalStateOfAgent agtState : simulationState.getPublicLocalStateOfAgents() ){
			if( agtState.getCategoryOfAgent().isA( PreyCategory.CATEGORY ) ){
				nbOfPreys++;
			} else if( agtState.getCategoryOfAgent().isA( PredatorCategory.CATEGORY ) ){
				nbOfPredators++;
			}
		}


	}
}