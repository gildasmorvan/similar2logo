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
package fr.univ_artois.lgi2a.similar2logo.examples.kheatbugs

import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.annotations.Parameter

class HeatBugsSimulationParameters: LogoSimulationParameters() {
	
	@Parameter(
	   name = "number of bugs", 
	   description = "the number of bugs in the simulation"
	)
	var nbOfBugs = 20
	
	@Parameter(
	   name = "evaporation rate", 
	   description = "the percentage of the world's heat that evaporates each cycle"
	)
	var evaporationRate = 0.1
	
	@Parameter(
	   name = "diffusion rate", 
	   description = "How much heat a patch (a spot in the world) diffuses to its neighbors"
	)
	var diffusionRate = 0.1
	
	@Parameter(
	   name = "min optimal temperature", 
	   description = "the minimum ideal temperatures for heatbugs"
	)
	var minOptimalTemperature = 10.0
	
	@Parameter(
	   name = "max optimal temperature", 
	   description = "the maximum ideal temperatures for heatbugs"
	)
	var maxOptimalTemperature = 25.0
	
	@Parameter(
	   name = "min output heat", 
	   description = "the minimum heat that heatbugs generate each cycle"
	)
	var minOutputHeat = 1.0
	
	@Parameter(
	   name = "max output heat", 
	   description = "the maximum heat that heatbugs generate each cycle"
	)
	var maxOutputHeat = 3.0
	
	@Parameter(
	   name = "random move probability", 
	   description = "the chance that a bug will make a random move even if it would prefer to stay where it is"
	)
	var randomMoveProbability = 0.1
	
	@Parameter(
	   name = "unhappiness", 
	   description = "the relative difference between real and optimal temperature that triggers moves"
	)
	var unhappiness = 0.1
	
}