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
package fr.univ_artois.lgi2a.similar2logo.examples.fire.probes;

import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.model.TreeAgentPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * A probe returning the combustion rate
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class SimpleFireProbe implements IProbe {


	private int initialNumberOfTrees;
	
	private int remainingNumberOfTrees;

	private long finalTimeStep ;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTimestamp,
		ISimulationEngine simulationEngine
	) {
		initialNumberOfTrees = this.countTrees(simulationEngine);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		remainingNumberOfTrees = this.countTrees(simulationEngine);
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get(
			LogoSimulationLevelList.LOGO
		);
		boolean stopSimulation = true;
		for(ILocalStateOfAgent agentPLS : simulationState.getPublicLocalStateOfAgents()) {
			TreeAgentPLS treeAgentPLS = (TreeAgentPLS)agentPLS;
			if(treeAgentPLS.isBurning()) {
				stopSimulation = false;
				break;
			}
		}
		if(stopSimulation) {
			simulationEngine.requestSimulationAbortion();
			finalTimeStep = timestamp.getIdentifier();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		remainingNumberOfTrees = this.countTrees(simulationEngine);
		finalTimeStep = timestamp.getIdentifier();
		
	}

	/**
	 * Update the population of trees in x, y and z local fields.
	 * @param simulationEngine
	 *            The engine where the simulation is running.
	 */
	private int countTrees(ISimulationEngine simulationEngine) {
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get(
			LogoSimulationLevelList.LOGO
		);

		return simulationState.getPublicLocalStateOfAgents().size();

	}

	public double getCombustionRate() {
		return 1 - ((double) remainingNumberOfTrees) / initialNumberOfTrees;
	}
	
	public long getFinalTimeStep() {
		return finalTimeStep;
	}
	
}
