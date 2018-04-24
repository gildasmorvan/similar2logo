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
package fr.lgi2a.similar2logo.examples.boids.model;

import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.Parameter;

/**
 * The parameter class of the boids simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class BoidsSimulationParameters extends LogoSimulationParameters {

	/**
	 * the repulsion distance.
	 */
	@Parameter(
	   name = "repulsion distance", 
	   description = "the repulsion distance"
	)
	public double repulsionDistance;
	
	/**
	 * the orientation distance.
	 */
	@Parameter(
	   name = "orientation distance", 
	   description = "the orientation distance"
	)
	public double orientationDistance;
	
	/**
	 * the attraction distance.
	 */
	@Parameter(
       name = "attraction distance", 
	   description = "the attraction distance"
	)
	public double attractionDistance;
	
	/**
	 * the repulsion weight.
	 */
	@Parameter(
	   name = "repulsion weight", 
	   description = "the repulsion weight"
	)
	public double repulsionWeight;
	
	
	/**
	 * the orientation weight.
	 */
	@Parameter(
	   name = "orientation weight", 
	   description = "the orientation weight"
	)
	public double orientationWeight;
	
	/**
	 * the attraction weight.
	 */
	@Parameter(
       name = "attraction weight", 
	   description = "the attraction weight"
	)
	public double attractionWeight;
	
	/**
	 * The maximal initial speed of boids.
	 */
	@Parameter(
	   name = "maximal initial speed", 
	   description = "the maximal initial speed of boids"
	)
	public double maxInitialSpeed;
	
	/**
	 * The minimal initial speed of turtles.
	 */
	@Parameter(
		name = "minimal initial speed", 
		description = "the minimal initial speed of boids"
	)
	public double minInitialSpeed;
	
	/**
	 * The perception angle of boids.
	 */
	@Parameter(
	   name = "perception angle", 
	   description = "the perception angle of the boids in rad"
	)
	public double perceptionAngle;
	
	/**
	 * The number of agents in the simulation.
	 */
	@Parameter(
	   name = "number of agents", 
	   description = "the number of agents in the simulation"
	)
	public int nbOfAgents;

	/**
	 * The maximal angular speed (rad/step)
	 */
	@Parameter(
	   name = "max angular speed", 
	   description = "the maximal angular speed of the boids in rad/step"
	)
	public double maxAngle;
	
	/**
	 * Builds a parameters set containing default values.
	 */
	public BoidsSimulationParameters() {
		super();
		this.gridHeight = 100;
		this.gridWidth = 100;
		this.nbOfAgents = 2000;
		this.repulsionDistance = 1;
		this.orientationDistance = 2;
		this.attractionDistance = 4;
		this.repulsionWeight = 10;
		this.orientationWeight = 50;
		this.attractionWeight = 0.1;
		this.maxAngle = Math.PI/4;
		this.maxInitialSpeed = 2;
		this.minInitialSpeed = 1;
		this.perceptionAngle = Math.PI;
		this.xTorus = true;
		this.yTorus = true;
		
	}

}
