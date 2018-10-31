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
package fr.univ_artois.lgi2a.similar2logo.examples.boids

import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters
import fr.univ_artois.lgi2a.similar2logo.kernel.model.Parameter

class BoidsSimulationParameters : LogoSimulationParameters() {


	@Parameter(
			name = "repulsion distance",
			description = "the repulsion distance"
	)
	var repulsionDistance = 1.0

	@Parameter(
			name = "orientation distance",
			description = "the orientation distance"
	)
	var orientationDistance = 2.0

	@Parameter(
			name = "attraction distance",
			description = "the attraction distance"
	)
	var attractionDistance = 4.0

	@Parameter(
			name = "repulsion weight",
			description = "the repulsion weight"
	)
	var repulsionWeight = 10.0

	@Parameter(
			name = "orientation weight",
			description = "the orientation weight"
	)
	var orientationWeight = 20.0

	@Parameter(
			name = "attraction weight",
			description = "the attraction weight"
	)
	var attractionWeight = 0.1

	@Parameter(
			name = "maximal initial speed",
			description = "the maximal initial speed of boids"
	)
	var maxInitialSpeed = 2.0

	@Parameter(
			name = "minimal initial speed",
			description = "the minimal initial speed of boids"
	)
	var minInitialSpeed = 1.0

	@Parameter(
			name = "perception angle",
			description = "the perception angle of the boids in rad"
	)
	var perceptionAngle = Math.PI

	@Parameter(
			name = "number of agents",
			description = "the number of agents in the simulation"
	)
	var nbOfAgents = 2000

	@Parameter(
			name = "max angular speed",
			description = "the maximal angular speed of the boids in rad/step"
	)
	var maxAngle = Math.PI / 4

}
