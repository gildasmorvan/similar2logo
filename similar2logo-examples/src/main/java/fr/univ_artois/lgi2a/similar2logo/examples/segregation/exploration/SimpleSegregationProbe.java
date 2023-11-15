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
package fr.univ_artois.lgi2a.similar2logo.examples.segregation.exploration;

import java.awt.Point;
import java.util.Collection;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar2logo.examples.segregation.model.SegregationSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * A probe for the segregation simulation exploration. 
 * Allows to recover the segregation and happiness rates.
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class SimpleSegregationProbe  implements IProbe {
	
	public double segregationRate = 0;
	
	public double happinessRate = 0;
	
	private SegregationSimulationParameters parameters;

	public SimpleSegregationProbe(SegregationSimulationParameters parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(SimulationTimeStamp finalTimestamp, ISimulationEngine simulationEngine) {
		getSegregationRate(finalTimestamp, simulationEngine);
		getHappinessRate(finalTimestamp, simulationEngine);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(String errorMessage, Throwable cause) {
		System.out.println(errorMessage+"\n"+cause);
	}
	
	/**
	 * Update the segregation rate.
	 * @param timestamp The time stamp when the observation is made.
	 * @param simulationEngine The engine where the simulation is running.
	 */
	private void getSegregationRate(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	){
			double segregationRate =0;
	
			IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get( 
				LogoSimulationLevelList.LOGO
			);
			LogoEnvPLS environment = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
			
			for(int x=0; x<environment.getWidth();x++) {
				for(int y=0; y<environment.getHeight();y++) {
					Set<TurtlePLSInLogo> turtles = environment.getTurtlesAt(x, y);
					if(!turtles.isEmpty()) {
						TurtlePLSInLogo turtle = environment.getTurtlesAt(x, y).iterator().next();
						Collection<Point> neighbors = environment.getNeighbors(x, y, 1);
			
						for(Point neighbor : neighbors) {
							Set<TurtlePLSInLogo> neighborTurtles = environment.getTurtlesAt(neighbor.x, neighbor.y);
							if(!neighborTurtles.isEmpty()) {
								TurtlePLSInLogo neighborTurtle = neighborTurtles.iterator().next();
								if(neighborTurtle.getCategoryOfAgent().isA(turtle.getCategoryOfAgent())) {
									segregationRate+=1;
									
								}
								
							}
						}
						segregationRate--;
					}
				}
			}
			segregationRate/=simulationState.getPublicLocalStateOfAgents().size()*8;
			this.segregationRate = segregationRate;
			
	}
	
	/**
	 * Update the segregation rate.
	 * @param timestamp The time stamp when the observation is made.
	 * @param simulationEngine The engine where the simulation is running.
	 */
	private void getHappinessRate(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	){
			double happinessRate = 0;
			IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get( 
				LogoSimulationLevelList.LOGO
			);
			LogoEnvPLS environment = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
			
			for(int x=0; x<environment.getWidth();x++) {
				for(int y=0; y<environment.getHeight();y++) {
					Set<TurtlePLSInLogo> turtles = environment.getTurtlesAt(x, y);
					if(!turtles.isEmpty()) {
						TurtlePLSInLogo turtle = environment.getTurtlesAt(x, y).iterator().next();
						Collection<Point> neighbors = environment.getNeighbors(x, y, (int) parameters.perceptionDistance);
						int nbOfSameTurtles = 0;
						int nbOfDifferentTurtles = 0;
						for(Point neighbor : neighbors) {
							Set<TurtlePLSInLogo> neighborTurtles = environment.getTurtlesAt(neighbor.x, neighbor.y);
							if(!neighborTurtles.isEmpty()) {
								TurtlePLSInLogo neighborTurtle = neighborTurtles.iterator().next();
								if(neighborTurtle.getCategoryOfAgent().isA(turtle.getCategoryOfAgent())) {
									nbOfSameTurtles++;
								} else {
									nbOfDifferentTurtles++;
								}
								
							}
						}
						if( ((double) nbOfSameTurtles)/(nbOfSameTurtles+nbOfDifferentTurtles) >= parameters.similarityRate) {
							happinessRate+=1;
						}
					}
				}
			}
			happinessRate /= environment.getTurtles().size();
			this.happinessRate = happinessRate;
			
	}


}
