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
package fr.univ_artois.lgi2a.similar2logo.examples.heatbugs.model.agents;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.heatbugs.model.HeatBugsSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.EmitPheromone;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * The decision model of a heat bug.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class HeatBugDecisionModel extends AbstractAgtDecisionModel {

	/**
	 * Builds an instance of this decision model.
	 */
	public HeatBugDecisionModel() {
		super(LogoSimulationLevelList.LOGO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		TurtlePLSInLogo castedPLS = (TurtlePLSInLogo) publicLocalState;
		HeatBugHLS castedHLS = (HeatBugHLS) privateLocalState;
		
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		
		double bestValue;
		
		double bestDirection = castedPLS.getDirection();
		
		double diff = 0;
		
		for(LocalPerceivedData<Double> pheromone : castedPerceivedData.getPheromones().get("heat")) {
			if(MathUtil.areEqual(pheromone.getDistanceTo(), 0)) {
				diff = (pheromone.getContent() - castedHLS.getOptimalTemperature())/castedHLS.getOptimalTemperature();
				break;
			}
		}
		
		if(diff > castedHLS.getUnhappiness()) {
			//If the current patch is too hot
			bestValue = Double.MAX_VALUE;
			for(LocalPerceivedData<Double> pheromone : castedPerceivedData.getPheromones().get("heat")) {
				if(pheromone.getContent() < bestValue) {
					bestValue = pheromone.getContent();
					bestDirection = pheromone.getDirectionTo();
				}
			}
			producedInfluences.add(
				new ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					bestDirection - castedPLS.getDirection(),
					castedPLS
				)
			);
			if(MathUtil.areEqual(castedPLS.getSpeed(), 0)) {
				producedInfluences.add(
					new ChangeSpeed(
						timeLowerBound,
						timeUpperBound,
						1,
						castedPLS
					)
				);
			}
		} else if(diff < -castedHLS.getUnhappiness()) {
			//If the current patch is too cool
			bestValue = -Double.MAX_VALUE;
			for(LocalPerceivedData<Double> pheromone : castedPerceivedData.getPheromones().get("heat")) {
				if(pheromone.getContent() > bestValue) {
					bestValue = pheromone.getContent();
					bestDirection = pheromone.getDirectionTo();
				}
			}
			producedInfluences.add(
				new ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					bestDirection - castedPLS.getDirection(),
					castedPLS
				)
			);
			if(MathUtil.areEqual(castedPLS.getSpeed(), 0)) {
				producedInfluences.add(
					new ChangeSpeed(
						timeLowerBound,
						timeUpperBound,
						1,
						castedPLS
					)
				);
			}
		} else {
			// If the turtle is on the best patch
			if(castedHLS.getRandomMoveProbability() > PRNG.get().randomDouble()) {
				producedInfluences.add(
					new ChangeDirection(
						timeLowerBound,
						timeUpperBound,
						PRNG.get().randomDouble()*2*Math.PI,
						castedPLS
					)
				);
				if(MathUtil.areEqual(castedPLS.getSpeed(), 0)) {
					producedInfluences.add(
						new ChangeSpeed(
							timeLowerBound,
							timeUpperBound,
							1,
							castedPLS
						)
					);
				}
			} else if(!MathUtil.areEqual(castedPLS.getSpeed(), 0)) {
				producedInfluences.add(
					new Stop(
						timeLowerBound,
						timeUpperBound,
						castedPLS
					)
				);
			}
		}
		producedInfluences.add(
			new EmitPheromone(
				timeLowerBound,
				timeUpperBound,
				castedPLS.getLocation(),
				HeatBugsSimulationParameters.HEAT_FIELD_ID,
				castedHLS.getOutputHeat()
			)
		);
	}

}
