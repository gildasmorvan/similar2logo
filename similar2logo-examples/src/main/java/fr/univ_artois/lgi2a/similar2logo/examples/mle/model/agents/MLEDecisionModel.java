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
package fr.univ_artois.lgi2a.similar2logo.examples.mle.model.agents;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.mle.model.MLESimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.EmitPheromone;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.random.PRNG;

/**
 * The decision model of a MLE agent.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target=
 *         "_blank">Gildas Morvan</a>
 *
 */
public class MLEDecisionModel extends AbstractAgtDecisionModel {

	private MLESimulationParameters parameters;
	
	/**
	 * Builds an instance of this decision model.
	 */
	public MLEDecisionModel(MLESimulationParameters parameters) {
		super(LogoSimulationLevelList.LOGO);
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		IGlobalState globalState,
		ILocalStateOfAgent publicLocalState,
		ILocalStateOfAgent privateLocalState,
		IPerceivedData perceivedData,
		InfluencesMap producedInfluences
	) {
		MLEAgentPLS castedPLS = (MLEAgentPLS) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		Map<String, Collection<LocalPerceivedData<Double>>> pheromones = castedPerceivedData.getPheromones();
		
		//Change direction
		changeDirection(timeLowerBound, timeUpperBound, castedPLS, pheromones, producedInfluences);
		
		//Emit pheromones		
		emitPheromones(timeLowerBound, timeUpperBound, castedPLS, producedInfluences);
		
		//Mutation
		mutate(castedPLS, pheromones);

	}
	
	private void changeDirection(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		MLEAgentPLS castedPLS,
		Map<String, Collection<LocalPerceivedData<Double>>> pheromones,
		InfluencesMap producedInfluences
		
	) {
		Collection<LocalPerceivedData<Double>> attraction = null;
		Collection<LocalPerceivedData<Double>> repulsion = null;

		if(castedPLS.getLevelOfEmergence() < parameters.levelMax) {
			attraction = pheromones.get(MLESimulationParameters.ATT+(castedPLS.getLevelOfEmergence()+1));
			repulsion = pheromones.get(MLESimulationParameters.REP+(castedPLS.getLevelOfEmergence()+1));
		}
		
		if(attraction == null && repulsion == null) {
			producedInfluences.add(
				new ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					PRNG.get().randomAngle()/8,
					castedPLS
				)
			);
		} else {
			double bestAttractionValue = -Double.MAX_VALUE;
			double bestAttractionDirection = 0;
			if(attraction != null) {
				for(LocalPerceivedData<Double> pheromone : attraction) {
					if(pheromone.getContent() > bestAttractionValue) {
						bestAttractionValue = pheromone.getContent();
						bestAttractionDirection = pheromone.getDirectionTo();
					}		
				}
			}
			double bestRepulsionValue = Double.MAX_VALUE;
			double bestRepulsionDirection = 0;
			if(repulsion != null) {
				for(LocalPerceivedData<Double> pheromone : repulsion) {
					if(pheromone.getContent() < bestRepulsionValue) {
						bestRepulsionValue = pheromone.getContent();
						bestRepulsionDirection = pheromone.getDirectionTo();
					}
				}
			}
			double dd = 0;
			if(bestAttractionValue > bestRepulsionValue) {
				dd = castedPLS.getDirection()- bestAttractionDirection;	
			} else {
				dd = castedPLS.getDirection()- bestRepulsionDirection;
			}
			if (!MathUtil.areEqual(dd, 0)) {
				producedInfluences.add(
					new ChangeDirection(
						timeLowerBound,
						timeUpperBound,
						dd,
						castedPLS
					)
				);
			}
		}
	}
	
	private void emitPheromones(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		MLEAgentPLS castedPLS,
		InfluencesMap producedInfluences
	) {
		
		producedInfluences.add(
			new EmitPheromone(
				timeLowerBound,
				timeUpperBound,
				castedPLS.getLocation(),
				MLESimulationParameters.ATT+castedPLS.getLevelOfEmergence(),
				parameters.pheromoneEmission
			)
		);
		
		producedInfluences.add(
			new EmitPheromone(
				timeLowerBound,
				timeUpperBound,
				castedPLS.getLocation(),
				MLESimulationParameters.PRE+castedPLS.getLevelOfEmergence(),
				parameters.pheromoneEmission
			)
		);
		producedInfluences.add(
			new EmitPheromone(
				timeLowerBound,
				timeUpperBound,
				castedPLS.getLocation(),
				MLESimulationParameters.REP+castedPLS.getLevelOfEmergence(),
				2*parameters.pheromoneEmission
			)
		);
	}
	

	private void mutate(
		MLEAgentPLS castedPLS,
		Map<String, Collection<LocalPerceivedData<Double>>> pheromones
	) {
		//Retrieve presence fields
		Map<Integer, Double> presence = new HashMap<>();
		for(int i = Math.max(0, castedPLS.getLevelOfEmergence() - 1);
				i <= Math.min(parameters.levelMax, castedPLS.getLevelOfEmergence() + 1);
				i++
		) {
			double quantity = 0.0;
			if(pheromones.containsKey(MLESimulationParameters.PRE+i)) {
				for(LocalPerceivedData<Double> pheromone : pheromones.get(MLESimulationParameters.PRE+i)) {
					quantity += pheromone.getContent();
				}	
			}
			presence.put(i,quantity);
		}
		
		//Retrieve field values
		double presenceUp = 0;
		if(presence.containsKey(castedPLS.getLevelOfEmergence()+1)) {
			presenceUp = presence.get(castedPLS.getLevelOfEmergence()+1);
		}
		double presenceLocal = 0;
		if(presence.containsKey(castedPLS.getLevelOfEmergence())) {
			presenceLocal = presence.get(castedPLS.getLevelOfEmergence());
		}
		double presenceDown = 0;
		if(presence.containsKey(castedPLS.getLevelOfEmergence()-1)) {
			presenceDown = presence.get(castedPLS.getLevelOfEmergence()-1);
		}
		
		//Do the mutation
		if(
				castedPLS.getLevelOfEmergence() > 0
			&&	presenceDown < parameters.decMutationThreshold
			&& 	PRNG.get().randomDouble() < parameters.decMutationProbability
		) {
			castedPLS.setLevelOfEmergence(castedPLS.getLevelOfEmergence()-1);
			castedPLS.setSpeed(castedPLS.getSpeed()*parameters.speedFactor);
		} else if(
				castedPLS.getLevelOfEmergence() < parameters.levelMax
			&&	(presenceLocal - presenceUp) > parameters.incMutationThreshold
			&& PRNG.get().randomDouble() < parameters.incMutationProbability
		) {
			castedPLS.setLevelOfEmergence(castedPLS.getLevelOfEmergence()+1);
			castedPLS.setSpeed(castedPLS.getSpeed()/parameters.speedFactor);
		}
	}
	
}
