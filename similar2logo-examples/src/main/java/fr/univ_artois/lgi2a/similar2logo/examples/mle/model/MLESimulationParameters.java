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
package fr.univ_artois.lgi2a.similar2logo.examples.mle.model;

import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.Parameter;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Pheromone;

/**
 * The parameter class of the MLE simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class MLESimulationParameters extends LogoSimulationParameters {
	
	/**
	 * The initial number of agents
	 */
	@Parameter(
	   name = "nb of agents", 
	   description = "the initial number of agents"
	)
	public int nbOfAgents;
	
	/**
	 * The initial speed of the agents.
	 */
	@Parameter(
	   name = "initial speed", 
	   description = "the initial speed of the agents"
	)
	public double initialSpeed;
	
	/**
	 * The speed factor
	 */
	@Parameter(
	   name = "speed factor", 
	   description = "the speed factor"
	)
	public double speedFactor;
	
	/**
	 * The initial presence evaporation
	 */
	@Parameter(
	   name = "init presence evaporation", 
	   description = "the initial presence evaporation"
	)
	public double initialPresenceEvaporation;
	
	/**
	 * The presence evaporation factor
	 */
	@Parameter(
	   name = "presence evaporation factor", 
	   description = "the presence evaporation factor"
	)
	public double presenceEvaporationFactor;
	
	/**
	 * The initial attraction evaporation
	 */
	@Parameter(
		name = "init attraction evaporation", 
		description = "the initial attraction evaporation"
	)
	public double initialAttractionEvaporation;
	
	/**
	 * The attraction evaporation factor
	 */
	@Parameter(
	   name = "attraction evaporation factor", 
	   description = "the attraction evaporation factor"
	)
	public double attractionEvaporationFactor;
	
	/**
	 * The initial repulsion emission
	 */
	@Parameter(
		name = "init repulsion emission", 
		description = "the initial repulsion emission"
	)
	public double initialRepulsionEvaporation;
	
	/**
	 * The repulsion evaporation factor
	 */
	@Parameter(
	   name = "repulsion evaporation factor", 
	   description = "the repulsion evaporation factor"
	)
	public double repulsionEvaporationFactor;
	
	/**
	 * The quantity of pheromone emitted at each step
	 */
	@Parameter(
		name = "quantity of pheromone emitted", 
		description = "the quantity of pheromone emitted at each step"
	)
	public double pheromoneEmission;
	
	/**
	 * The maximal level of emergence
	 */
	@Parameter(
	   name = "max level of emergence", 
	   description = "the maximal level of emergence"
	)
	public int levelMax;
	
	/**
	 * The dec mutation threshold
	 */
	@Parameter(
	   name = "dec mutation threshold", 
	   description = "the dec mutation threshold"
	)
	public double decMutationThreshold;
	
	/**
	 * The inc mutation threshold
	 */
	@Parameter(
	   name = "inc mutation threshold", 
	   description = "the inc mutation threshold"
	)
	public double incMutationThreshold;
	
	/**
	 * The dec mutation probability
	 */
	@Parameter(
	   name = "dec mutation probability", 
	   description = "the dec mutation probability"
	)
	public double decMutationProbability;
	
	/**
	 * The inc mutation probability
	 */
	@Parameter(
	   name = "inc mutation probability", 
	   description = "the inc mutation probability"
	)
	public double incMutationProbability;
	
	/**
	 * The prefix of the attraction pheromone fields
	 */
	public static final String ATT = "ATT";
	
	/**
	 * The prefix of the repulsion pheromone fields
	 */
	public static final String REP = "REP";
	
	/**
	 * The prefix of the presence pheromone fields
	 */
	public static final String PRE = "PRE";
	
	/**
	 * Builds a parameters set containing default values.
	 */
	public MLESimulationParameters() {
		super();
		this.nbOfAgents = 2000;
		this.initialSpeed = 1;
		this.speedFactor = 10;
		this.initialPresenceEvaporation = 0.1;
		this.presenceEvaporationFactor = 10;
		this.initialAttractionEvaporation = 0.01;
		this.attractionEvaporationFactor = 10;
		this.initialRepulsionEvaporation = 0.02;
		this.repulsionEvaporationFactor = 10;
		this.pheromoneEmission = 1;
		this.levelMax = 1;
		this.decMutationThreshold = 1;
		this.incMutationThreshold = 1;
		this.decMutationProbability = 0.05;
		this.incMutationProbability = 0.05;
		this.xTorus = true;
		this.yTorus = true;
		this.gridHeight = 200;
		this.gridWidth = 200;
		initPheromoneFields();
	}
	
	public void initPheromoneFields() {
		this.pheromones.clear();
		for(int i = 0; i <= this.levelMax; i++) {
			this.pheromones.add(
				new Pheromone(
					MLESimulationParameters.PRE+i,
					1,
					this.initialPresenceEvaporation/Math.pow(
						this.presenceEvaporationFactor,i
					)
				)
			);
		}
		for(int i = 1; i <= this.levelMax; i++) {
			this.pheromones.add(
				new Pheromone(
					MLESimulationParameters.ATT+i,
					1,
					this.initialAttractionEvaporation/Math.pow(
						this.attractionEvaporationFactor,i
					)
				)
			);
			this.pheromones.add(
				new Pheromone(
					MLESimulationParameters.REP+i,
					1,
					this.initialRepulsionEvaporation/Math.pow(
						this.repulsionEvaporationFactor,i
					)
				)
			);
		}
	}

}
