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
package fr.univ_artois.lgi2a.similar2logo.examples.predation.exploration.probe;

import org.eclipse.jetty.util.log.Log;

import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.exploration.data.SimulationDataPreyPredator;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.agents.PredatorCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.agents.PreyCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * A probe for the prey predation simulation exploration. 
 * Allows to recover the number of preys, predators and the quantity of grass.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class PreyPredatorPopulationForExplorationProbe implements IProbe {
	
	private SimulationDataPreyPredator data;
	
	public PreyPredatorPopulationForExplorationProbe(SimulationDataPreyPredator sim) {
		this.data = sim;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {
		this.getPopulation(timestamp, simulationEngine);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(SimulationTimeStamp finalTimestamp, ISimulationEngine simulationEngine) {
		this.getPopulation(finalTimestamp, simulationEngine);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(String errorMessage, Throwable cause) {
		Log.getRootLogger().warn(errorMessage+"\n"+cause);
	}
	
	/**
	 * Update the population of agents in x, y and z local fields.
	 * @param timestamp The time stamp when the observation is made.
	 * @param simulationEngine The engine where the simulation is running.
	 */
	@SuppressWarnings("unchecked")
	private void getPopulation(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	){
		int nbOfPreys =0;
		int nbOfPredators =0;
		double nbOfGrass =0;
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get( 
			LogoSimulationLevelList.LOGO
		);
		
		for( ILocalStateOfAgent agtState : simulationState.getPublicLocalStateOfAgents() ){
			if( agtState.getCategoryOfAgent().isA( PreyCategory.CATEGORY ) ){
				nbOfPreys++;
			} else if( agtState.getCategoryOfAgent().isA( PredatorCategory.CATEGORY ) ){
				nbOfPredators++;
			}
		}
		
		LogoEnvPLS environment = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
		for(int x=0; x<environment.getWidth();x++) {
			for(int y=0; y<environment.getHeight();y++) {
				Mark<Double> grass = (Mark<Double>) environment.getMarksAt(x, y).iterator().next();
				nbOfGrass+=(Double) grass.getContent();
			}
		}
		data.getNbOfPreys().add(nbOfPreys);
		data.getNbOfPredators().add(nbOfPredators);
		data.getNbOfGrass().add(nbOfGrass);
	}
	
	/**
	 * Gives the data of the simulation.
	 * @return the data of the simulation
	 */
	public SimulationDataPreyPredator getData () {
		return this.data;
	}

}
