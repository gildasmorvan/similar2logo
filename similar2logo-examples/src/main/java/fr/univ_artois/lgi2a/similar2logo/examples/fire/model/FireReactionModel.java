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
 * implementation of Logo-like simulations using the SIMILAR API.
 * This software defines an API to implement such simulations, and also 
 * provides usage examples.
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
package fr.univ_artois.lgi2a.similar2logo.examples.fire.model;

import java.awt.Point;
import java.util.Set;

import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;



/**
 * The decision model of the fire agents.
 * 
 * @author <a xavier_szkudlarek@univ-artois.fr target="_blank">Szkudlarek Xavier</a>
 *       
 *
 */
public class FireReactionModel extends LogoDefaultReactionModel {

	/**
	 * The parameters of the simulation.
	 */
	private FireForestParameters parameters;
	
	/**
	 * Creates a new instance of the FireForestSimulationModel class.
	 * @param parameters The parameter class of the fire model
	 */
	public FireReactionModel(FireForestParameters parameters) {
		this.setParameters(parameters);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {
		
		
		LogoEnvPLS env = (LogoEnvPLS) consistentState.getPublicLocalStateOfEnvironment();
		
		for(ILocalStateOfAgent agent : consistentState.getPublicLocalStateOfAgents()) {
			TreeAgentPLS tree = (TreeAgentPLS) agent;
			if(tree.isBurning()) {
				tree.burn(parameters.combustionSpeed);
				for (Point neighbor: env.getNeighbors((int) tree.getLocation().x, (int) tree.getLocation().y, 1)){
					for(TurtlePLSInLogo otherAgent : env.getTurtlesAt(neighbor)) {
						TreeAgentPLS otherTree = (TreeAgentPLS) otherAgent;
						if(!otherTree.isBurning() && PRNG.randomDouble() < parameters.firePropagationProba) {
							otherTree.setBurning(true);
						}
					}
				}
			}
			if(tree.isBurned()) {
				
				remainingInfluences.add(
						new SystemInfluenceRemoveAgent(
							LogoSimulationLevelList.LOGO,
							transitoryTimeMin, transitoryTimeMax, tree
						)
					);
			}
		}
		
		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax, consistentState, regularInfluencesOftransitoryStateDynamics, remainingInfluences);

	}

	public FireForestParameters getParameters() {
		return parameters;
	}

	public void setParameters(FireForestParameters parameters) {
		this.parameters = parameters;
	}
	
}
