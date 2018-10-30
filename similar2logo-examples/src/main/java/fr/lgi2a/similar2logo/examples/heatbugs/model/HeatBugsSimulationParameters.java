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
package fr.lgi2a.similar2logo.examples.heatbugs.model;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.Parameter;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;

/**
 * The parameters of the Heatbugs simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class HeatBugsSimulationParameters extends LogoSimulationParameters {


	
	/**
	 * The number of bugs in the simulation.
	 */
	@Parameter(
	   name = "number of bugs", 
	   description = "the number of bugs in the simulation"
	)
	public int nbOfBugs;
	
	/**
	 * The percentage of the world's heat that evaporates each cycle.
	 * A lower number means a world which cools slowly, a higher number
	 * is a world which cools quickly.
	 */
	@Parameter(
	   name = "evaporation rate", 
	   description = "the percentage of the world's heat that evaporates each cycle"
	)
	public double evaporationRate;
	
	/**
	 * How much heat a patch (a spot in the world) diffuses to its neighbors.
	 * A higher number means that heat diffuses through the world quickly.
	 * A lower number means that patches retain more of their heat.
	 */
	@Parameter(
	   name = "diffusion rate", 
	   description = "How much heat a patch (a spot in the world) diffuses to its neighbors"
	)
	public double diffusionRate;
	
	/**
	 * The minimum ideal temperatures for heatbugs. Each bug is given an ideal temperature
	 * between the min and max ideal temperature.
	 */
	@Parameter(
	   name = "min optimal temperature", 
	   description = "the minimum ideal temperatures for heatbugs"
	)
	public double minOptimalTemperature;
	
	/**
	 * The maximum ideal temperatures for heatbugs. Each bug is given an ideal temperature
	 * between the min and max ideal temperature.
	 */
	@Parameter(
	   name = "max optimal temperature", 
	   description = "the maximum ideal temperatures for heatbugs"
	)
	public double maxOptimalTemperature;
	
	/**
	 * The minimum heat that heatbugs generate each cycle. Each bug is given a
	 * output-heat value between the min and max output heat.
	 */
	@Parameter(
	   name = "min output heat", 
	   description = "the minimum heat that heatbugs generate each cycle"
	)
	public double minOutputHeat;
	
	/**
	 * The maximum heat that heatbugs generate each cycle. Each bug is given a
	 * output-heat value between the min and max output heat.
	 */
	@Parameter(
	   name = "max output heat", 
	   description = "the maximum heat that heatbugs generate each cycle"
	)
	public double maxOutputHeat;
	
	/**
	 * The chance that a bug will make a random move even if it would prefer to
	 * stay where it is (because no more ideal patch is available).
	 */
	@Parameter(
	   name = "random move probability", 
	   description = "the chance that a bug will make a random move even if it would prefer to stay where it is"
	)
	public double randomMoveProbability;
	
	/**
	 * The relative difference between real and optimal temperature that triggers moves.
	 */
	@Parameter(
	   name = "unhappiness", 
	   description = "the relative difference between real and optimal temperature that triggers moves"
	)
	public double unhappiness;
	
	/**
	 * The identifier of the pheromone field that represents heat.
	 */
	public static final String HEAT_FIELD_ID = "heat";
	
	/**
	 * Builds a parameters set containing default values.
	 */
	public HeatBugsSimulationParameters() {
		super();
		this.nbOfBugs = 200;
		this.evaporationRate = 0.01;
		this.diffusionRate = 0.1;
		this.maxOptimalTemperature = 35;
		this.minOptimalTemperature = 20;
		this.maxOutputHeat = 3;
		this.minOutputHeat = 1;
		this.randomMoveProbability = 0.1;
		this.unhappiness = 0.1;
		this.finalTime = new SimulationTimeStamp( 30000 );
		this.gridWidth = 100;
		this.gridHeight = 100;
		this.xTorus = true;
		this.yTorus = true;
		this.pheromones.add(
			new Pheromone(HEAT_FIELD_ID, this.diffusionRate, this.evaporationRate)
		);
	}

}
