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
package fr.univ_artois.lgi2a.similar2logo.examples.projettut.model;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.annotations.Parameter;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;

/**
 * The parameter class of the passive turtle simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class SimulationParameters extends LogoSimulationParameters {
	

	public double initialX;
	
	
	public double initialY;
	
	/**
	 * The initial speed of the turtle.
	 */
	@Parameter(
	   name = "initial speed", 
	   description = "the initial speed of the turtle"
	)
	public double initialSpeed;
	
	public double initialDirection;
	

	@Parameter(
	   name = "max acceleration noise", 
	   description = "max acceleration noise"
	)
	public double maxAccelerationNoise;
	
	@Parameter(
			   name = "max angular noise", 
			   description = "max angular noise"
			)
	public double maxAngularNoise;
	
	
	@Parameter(
			   name = "max acceleration", 
			   description = "the maximal acceleration that an agent can handle. It is destroyed if exceeded."
			)
	public double maxAcceleration;
	
	@Parameter(
			   name = "max angular speed", 
			   description = "max angular speed"
			)
	public double maxAngularSpeed;

	@Parameter(
	   name = "inertia", 
	   description = "inertia"
	)
	public double inertia;
	
	@Parameter(
		name = "nb of obstacles", 
		description = "number of obstacles"
	)
	public int obstacles;
	
	
	
	
	/**
	 * Builds a parameters set containing default values.
	 */
	public SimulationParameters() {
		super();
		this.initialX = 0;
		this.initialY = 10;
		this.initialDirection = LogoEnvPLS.EAST;
		this.initialSpeed = 0.1;
		this.xTorus = false;
		this.yTorus = false;
		this.gridHeight = 40;
		this.gridWidth = 40;
		this.finalStep = 500;
		this.finalTime = new SimulationTimeStamp( this.finalStep );
		this.maxAccelerationNoise = 0.2;
		this.maxAngularNoise = 0.2;
		this.maxAcceleration = 1;
		this.maxAngularSpeed = Math.PI/4;
		this.inertia = 0.1;
		this.obstacles = 4;
	}

}
