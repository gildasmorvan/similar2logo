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
package fr.univ_artois.lgi2a.similar2logo.examples.segregation

import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG
import java.awt.geom.Point2D

class SegregationReactionModel : LogoDefaultReactionModel() {

	override fun makeRegularReaction(
			transitoryTimeMin: SimulationTimeStamp,
			transitoryTimeMax: SimulationTimeStamp,
			consistentState: ConsistentPublicLocalDynamicState,
			regularInfluencesOftransitoryStateDynamics: Set<IInfluence>,
			remainingInfluences: InfluencesMap
	) {
		//If there there is at least an agent that wants to move
		if (regularInfluencesOftransitoryStateDynamics.size > 2) {
			var specificInfluences = ArrayList<IInfluence>()
			var vacantPlaces = ArrayList<Point2D>()
			specificInfluences.addAll(regularInfluencesOftransitoryStateDynamics)
			PRNG.shuffle(specificInfluences)
			//Identify vacant places
			var castedEnvState = consistentState.publicLocalStateOfEnvironment as LogoEnvPLS
			for (x in 0..castedEnvState.width-1) {
				for (y in 0..castedEnvState.height-1) {
					if (castedEnvState.getTurtlesAt(x, y).isEmpty()) {
						vacantPlaces.add(
								Point2D.Double(x.toDouble(), y.toDouble())
						)
					}
				}
			}
			PRNG.shuffle(vacantPlaces)
			//move agents
			var i = 0
			for (influence in specificInfluences) {
				if (influence.category.equals("move")) {
					var castedInfluence = influence as Move
					castedEnvState.turtlesInPatches[Math.floor(castedInfluence.target.location.x).toInt()][Math.floor(castedInfluence.target.location.y).toInt()].clear()
					castedEnvState.turtlesInPatches[Math.floor(vacantPlaces[i].x).toInt()][Math.floor(vacantPlaces[i].y).toInt()].add(castedInfluence.target)
					castedInfluence.target.location = vacantPlaces[i]
					i++
				}
				if (i >= vacantPlaces.size) {
					break
				}
			}
		}
	}
}