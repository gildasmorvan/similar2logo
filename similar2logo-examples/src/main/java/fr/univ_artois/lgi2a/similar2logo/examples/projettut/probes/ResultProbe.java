/**
  * Copyright or © or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.univ_artois.lgi2a.similar2logo.examples.projettut.probes;

import static spark.Spark.get;

import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar2logo.examples.projettut.model.ControllerCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.probes.MapWebSocket;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * A probe printing information about agent population in a given target.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ResultProbe implements IProbe {
	
	/**
	 * The StringBuilder where the data are written.
	 */
	private StringBuilder output;
	
	private long timeWon = 0;
	
	private int timeToGoal = 0;
	
	
	/**
	 * Creates an instance of this probe.
	 * 
	 */
	public ResultProbe(){
		this.output =  new StringBuilder();
		get("/result.txt", (request, response) ->  this.getOutputAsString());	
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() {
		this.output =  new StringBuilder();
		this.timeWon = 0;
		this.timeToGoal = 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTimestamp,
		ISimulationEngine simulationEngine
	) {
		ResultWebSocket.sendData("");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		this.displayPopulation( timestamp, simulationEngine );
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
			SimulationTimeStamp finalTimestamp,
			ISimulationEngine simulationEngine
	) {
		this.displayPopulation( finalTimestamp, simulationEngine );
		System.out.println("Goal not reached!");
		ResultWebSocket.sendData("Goal not reached!");
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
		
		//if(ResultWebSocket.wsLaunch){
			
		
			IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get( 
				LogoSimulationLevelList.LOGO		
			);

				
			LogoEnvPLS environment = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
				
			Mark<?> goal = environment.getMarksAsSet().iterator().next();
			
			int nbOfControllers = 0;
			
			for( ILocalStateOfAgent agtState : simulationState.getPublicLocalStateOfAgents() ){
				if( agtState.getCategoryOfAgent().isA( ControllerCategory.CATEGORY ) ){
					nbOfControllers++;
					TurtlePLSInLogo castedAgtState = (TurtlePLSInLogo) agtState;
					double distance = environment.getDistance(castedAgtState, goal);
					if(distance < 1) {
						timeToGoal++;
					} else {
						timeToGoal = 0;
					}
					if(timeToGoal > 10) {
						timeWon = timestamp.getIdentifier();
						ResultWebSocket.sendData("Goal reached in "+ timeWon + "steps!");
						simulationEngine.requestSimulationAbortion();
					}
				}
			} 
			if(nbOfControllers == 0) {
				ResultWebSocket.sendData("The agent was destroyed at step "+ timestamp.getIdentifier());
				simulationEngine.requestSimulationAbortion();
			}
		
		}

	//}
	
	private String getOutputAsString() {
		return output.toString();
	}
	
}
