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
package fr.lgi2a.similar2logo.examples.multiturmite

import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters
import fr.lgi2a.similar.microkernel.AgentCategory
import fr.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData
import fr.lgi2a.similar.microkernel.LevelIdentifier
import fr.lgi2a.similar.microkernel.levels.ILevel
import fr.lgi2a.similar2logo.examples.turmite.TurmiteDecisionModel
import fr.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList
import fr.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel
import fr.lgi2a.similar2logo.lib.tools.random.PRNG
import java.util.LinkedList

class MultiTurmiteSimulationModel(parameters: LogoSimulationParameters) : AbstractLogoSimulationModel(parameters) {

	
	override fun generateLevels(
			simulationParameters: ISimulationParameters
	): List<ILevel> {
		var logo = ExtendedLevel(
				simulationParameters.initialTime,
				LogoSimulationLevelList.LOGO,
				PeriodicTimeModel(
						1,
						0,
						simulationParameters.initialTime
				),
				MultiTurmiteReactionModel(simulationParameters as MultiTurmiteSimulationParameters)
		)
		var levelList = LinkedList<ILevel>()
		levelList.add(logo)
		return levelList
	}
	
	override fun generateAgents(
			parameters: ISimulationParameters,
			levels: Map<LevelIdentifier, ILevel>
	): AgentInitializationData {
		var result = AgentInitializationData()
	    var castedSimulationParameters = simulationParameters as MultiTurmiteSimulationParameters
		if(castedSimulationParameters.initialLocations.isEmpty()) {
			for(i in 1..castedSimulationParameters.nbOfTurmites) {
				var turtle = TurtleFactory.generate(
					ConeBasedPerceptionModel(0.0, 2*Math.PI, false, true, false),
					TurmiteDecisionModel(),
					AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
					randomDirection(),
					1.0,
					0.0,
					Math.floor(PRNG.get().randomDouble()*castedSimulationParameters.gridWidth),
					Math.floor(PRNG.get().randomDouble()*castedSimulationParameters.gridHeight)
				)
				result.getAgents().add( turtle )
			}
		} else {
			if(
				castedSimulationParameters.nbOfTurmites != castedSimulationParameters.initialDirections.size ||
				castedSimulationParameters.nbOfTurmites != castedSimulationParameters.initialLocations.size
			) {
				throw UnsupportedOperationException("Inital locations and directions must be specified for each turmite")
			}
			for(i in 0..castedSimulationParameters.nbOfTurmites-1) {
				var turtle = TurtleFactory.generate(
					ConeBasedPerceptionModel(0.0, 2*Math.PI, false, true, false),
					TurmiteDecisionModel(),
					AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
					castedSimulationParameters.initialDirections[i],
					1.0,
					0.0,
					castedSimulationParameters.initialLocations[i].x,
					castedSimulationParameters.initialLocations[i].y
				)
				result.getAgents().add( turtle )
			}
		}
		return result
	}
	
	fun randomDirection(): Double {
		var rand = PRNG.get().randomDouble()
		if(rand < 0.25) {
			return LogoEnvPLS.NORTH
		} else if ( rand < 0.5 ) {
			return LogoEnvPLS.WEST
		} else if ( rand < 0.75 ) {
			return LogoEnvPLS.SOUTH
		}
		return LogoEnvPLS.EAST
	}

}