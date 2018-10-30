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
package fr.lgi2a.similar2logo.examples.segregation.model;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.Parameter;

/**
 * The parameter class of the segregation simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class SegregationSimulationParameters extends LogoSimulationParameters {
	

	/**
	 * the rate of same-color turtles that each turtle wants among its neighbors.
	 */
	@Parameter(
	   name = "similarity rate", 
	   description = "the rate of same-color turtles that each turtle wants among its neighbors"
	)
	public double similarityRate;
	
	/**
	 * The rate of vacant settling places;
	 */
	@Parameter(
	   name = "vacancy rate", 
	   description = "the rate of vacant settling places"
	)
	public double vacancyRate;
	
	/**
	 * the perception distance of agents.
	 */
	@Parameter(
	   name = "perception distance", 
	   description = "the perception distance of agents"
	)
	public double perceptionDistance;
	
	/**
	 * Builds a parameters set containing default values.
	 */
	public SegregationSimulationParameters() {
		super();
		this.similarityRate = 3.0/8;
		this.vacancyRate = 0.05;
		this.perceptionDistance = Math.sqrt(2);
		this.initialTime = new SimulationTimeStamp( 0 );
		this.finalTime = new SimulationTimeStamp( 500 );
		this.xTorus = true;
		this.yTorus = true;
		this.gridHeight = 50;
		this.gridWidth = 50;
	}

}
