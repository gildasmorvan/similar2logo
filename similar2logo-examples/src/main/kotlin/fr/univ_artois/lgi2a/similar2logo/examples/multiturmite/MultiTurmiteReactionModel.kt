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
package fr.univ_artois.lgi2a.similar2logo.examples.multiturmite

import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.DropMark
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.RemoveMark
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel
import java.awt.geom.Point2D

class MultiTurmiteReactionModel(parameters: MultiTurmiteSimulationParameters) : LogoDefaultReactionModel() {

	var parameters = parameters

	override fun makeRegularReaction(
			transitoryTimeMin: SimulationTimeStamp,
			transitoryTimeMax: SimulationTimeStamp,
			consistentState: ConsistentPublicLocalDynamicState,
			regularInfluencesOftransitoryStateDynamics: Set<IInfluence>,
			remainingInfluences: InfluencesMap
	) {
		var nonSpecificInfluences = LinkedHashSet<IInfluence>()
		var collisions = LinkedHashMap<Point2D, TurmiteInteraction>()

		//Organize influences by location and type
		for (influence in regularInfluencesOftransitoryStateDynamics) {
			if (influence.category.equals(DropMark.CATEGORY)) {
				var castedDropInfluence = influence as DropMark
				if (!collisions.containsKey(castedDropInfluence.mark.location)) {
					collisions.put(
							castedDropInfluence.mark.location,
							TurmiteInteraction()
					)
				}
				collisions.get(castedDropInfluence.mark.location)?.dropMarks?.add(castedDropInfluence)

			} else if (influence.category.equals(RemoveMark.CATEGORY)) {
				var castedRemoveInfluence = influence as RemoveMark
				if (!collisions.containsKey(castedRemoveInfluence.mark.location)) {
					collisions.put(
							castedRemoveInfluence.mark.location,
							TurmiteInteraction()
					)
				}
				collisions.get(castedRemoveInfluence.mark.location)?.removeMarks?.add(castedRemoveInfluence)
			} else if (influence.category.equals(ChangeDirection.CATEGORY)) {
				var castedChangeDirectionInfluence = influence as ChangeDirection
				if (!collisions.containsKey(castedChangeDirectionInfluence.target.location)) {
					collisions.put(
							castedChangeDirectionInfluence.target.location,
							TurmiteInteraction()
					)
				}
				collisions.get(castedChangeDirectionInfluence.target.location)?.changeDirections?.add(castedChangeDirectionInfluence)
			} else {
				nonSpecificInfluences.add(influence)
			}
		}

		for (collision in collisions.values) {
			if (collision.isColliding()) {
				if (!collision.dropMarks.isEmpty() && !this.parameters.inverseMarkUpdate) {
					nonSpecificInfluences.add(
							collision.dropMarks.iterator().next()
					)
				}
				if (!collision.removeMarks.isEmpty() && !this.parameters.inverseMarkUpdate) {
					nonSpecificInfluences.add(
							collision.removeMarks.iterator().next()
					)
				}
				if (!this.parameters.removeDirectionChange) {
					nonSpecificInfluences.addAll(collision.changeDirections)
				}
			} else {
				nonSpecificInfluences.addAll(collision.changeDirections)
				if (!collision.dropMarks.isEmpty()) {
					nonSpecificInfluences.add(collision.dropMarks.iterator().next())
				}
				if (!collision.removeMarks.isEmpty()) {
					nonSpecificInfluences.add(collision.removeMarks.iterator().next())
				}
			}
		}

		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax, consistentState, nonSpecificInfluences, remainingInfluences)
	}
}
