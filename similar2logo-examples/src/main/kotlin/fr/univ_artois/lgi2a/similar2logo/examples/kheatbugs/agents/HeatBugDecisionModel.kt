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
package fr.univ_artois.lgi2a.similar2logo.examples.kheatbugs.agents

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap
import fr.univ_artois.lgi2a.similar2logo.examples.kheatbugs.HeatBugsSimulationParameters
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.EmitPheromone
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.Stop
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList
import fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG

class HeatBugDecisionModel : AbstractAgtDecisionModel(LogoSimulationLevelList.LOGO) {

	override fun decide(
			timeLowerBound: SimulationTimeStamp,
			timeUpperBound: SimulationTimeStamp,
			globalState: IGlobalState,
			publicLocalState: ILocalStateOfAgent,
			privateLocalState: ILocalStateOfAgent,
			perceivedData: IPerceivedData,
			producedInfluences: InfluencesMap
	) {
		var castedPLS = publicLocalState as TurtlePLSInLogo
		var castedHLS = privateLocalState as HeatBugHLS
		var castedPerceivedData = perceivedData as TurtlePerceivedData

		var bestValue: Double
		var bestDirection = castedPLS.direction
		var diff = 0.0

		for (pheromone in castedPerceivedData.pheromones.get("heat").orEmpty()) {
			if (MathUtil.areEqual(pheromone.getDistanceTo(), 0.0)) {
				diff = (pheromone.content - castedHLS.optimalTemperature) / castedHLS.optimalTemperature
				break
			}
		}

		if (diff > castedHLS.unhappiness) {
			//If the current patch is too hot
			bestValue = Double.MAX_VALUE;
			for (pheromone in castedPerceivedData.pheromones.get("heat").orEmpty()) {
				if (pheromone.content < bestValue) {
					bestValue = pheromone.content
					bestDirection = pheromone.directionTo
				}
			}
			producedInfluences.add(
					ChangeDirection(
							timeLowerBound,
							timeUpperBound,
							bestDirection - castedPLS.direction,
							castedPLS
					)
			)
			if (MathUtil.areEqual(castedPLS.getSpeed(), 0.0)) {
				producedInfluences.add(
						ChangeSpeed(
								timeLowerBound,
								timeUpperBound,
								1.0,
								castedPLS
						)
				)
			}
		} else if (diff < -castedHLS.unhappiness) {
			//If the current patch is too cool
			bestValue = -Double.MAX_VALUE;
			for (pheromone in castedPerceivedData.pheromones.get("heat").orEmpty()) {
				if (pheromone.content > bestValue) {
					bestValue = pheromone.content
					bestDirection = pheromone.directionTo
				}
			}
			producedInfluences.add(
					ChangeDirection(
							timeLowerBound,
							timeUpperBound,
							bestDirection - castedPLS.getDirection(),
							castedPLS
					)
			)
			if (MathUtil.areEqual(castedPLS.getSpeed(), 0.0)) {
				producedInfluences.add(
						ChangeSpeed(
								timeLowerBound,
								timeUpperBound,
								1.0,
								castedPLS
						)
				)
			}
		} else {
			// If the turtle is on the best patch
			if (castedHLS.randomMoveProbability > PRNG.randomDouble()) {
				producedInfluences.add(
						ChangeDirection(
								timeLowerBound,
								timeUpperBound,
								PRNG.randomDouble() * 2 * Math.PI,
								castedPLS
						)
				)
				if (MathUtil.areEqual(castedPLS.getSpeed(), 0.0)) {
					producedInfluences.add(
							ChangeSpeed(
									timeLowerBound,
									timeUpperBound,
									1.0,
									castedPLS
							)
					)
				}
			} else if (!MathUtil.areEqual(castedPLS.getSpeed(), 0.0)) {
				producedInfluences.add(
						Stop(
								timeLowerBound,
								timeUpperBound,
								castedPLS
						)
				)
			}
		}
		producedInfluences.add(
				EmitPheromone(
						timeLowerBound,
						timeUpperBound,
						castedPLS.getLocation(),
						"heat",
						castedHLS.outputHeat
				)
		)
	}
}