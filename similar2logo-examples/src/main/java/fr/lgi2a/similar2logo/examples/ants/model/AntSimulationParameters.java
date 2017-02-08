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
package fr.lgi2a.similar2logo.examples.ants.model;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.Parameter;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

public class AntSimulationParameters extends LogoSimulationParameters {

	/**
	 * Number of ants in the simulation
	 */
	@Parameter(
			name = "Number of ants",
			description = "Initial number of ants in the simulation"
			)
	public int initialNumberAnts ;
	
	/**
	 * Number of a food on the point
	 */
	@Parameter(
			name = "Quantity of food/Point",
			description = "Quantity of food in the point"
			)
	public int initialQuantityOfFood;
	
	/**
	 * Number of food point on the map
	 */
	@Parameter(
			name = "Number of food point",
			description = "Initial number of food point in the simulation"
			)
	public int initialNumberFoods ;
	
	/**
	 * Perception distance to the agent
	 */
	public double perceptionDistance ;
	
	/**
	 * Perception angle to the agent
	 */
	public double perceptionAngle;
	
	/**
	 * Speed of the agent on the map
	 */
	@Parameter(
			name = "Initial speed",
			description = "Initial speed of the ants in the simulation"
			)
	public double initialSpeed;
	
	/**
	 * Distance to the agent to get the food
	 */
	public double perceptionDistanceGetFood;
	
	/**
	 * Distance to the agent to perceiv a pheromones
	 */
	public double perceptionDistancePheromones;
	
	/**
	 * Max angulaie speed for the ant
	 */
	public double maxAngle;
	
	/**
	 * Constructor of the ant parameters
	 */
	public AntSimulationParameters()
	{
		super();
		this.initialNumberFoods = 10;
		this.pheromones.add( new Pheromone("Food", 0.000000000000000000000000000000000000000001, 0.003/*Double.MIN_NORMAL*/));
//		this.pheromones.add( new Pheromone("Food", 0.000000000000000000000000000000000000000001, Double.MIN_NORMAL));
		this.pheromones.add( new Pheromone("Base", 0.1, 0.1));
		this.initialQuantityOfFood = 100;
		this.gridHeight = 50;
		this.gridWidth = 50;
		this.initialSpeed = 0.1;
		this.perceptionDistance = 5;
		this.perceptionDistanceGetFood = 1.1;
		this.initialNumberAnts = 50;
		this.perceptionAngle = Math.PI;
		this.maxAngle = Math.PI/8;
		this.xTorus = true;
		this.yTorus = true;
		this.initialTime = new SimulationTimeStamp( 0 );
		this.finalTime = new SimulationTimeStamp( 300000 );
	}
}

