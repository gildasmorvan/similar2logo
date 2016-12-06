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
package fr.lgi2a.similar2logo.examples.groovy.boids

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters
import fr.lgi2a.similar.microkernel.AgentCategory
import fr.lgi2a.similar.microkernel.LevelIdentifier
import fr.lgi2a.similar.microkernel.SimulationTimeStamp
import fr.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData
import fr.lgi2a.similar.microkernel.agents.IGlobalState
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent
import fr.lgi2a.similar.microkernel.agents.IPerceivedData
import fr.lgi2a.similar.microkernel.influences.InfluencesMap
import fr.lgi2a.similar.microkernel.levels.ILevel
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters
import fr.lgi2a.similar2logo.kernel.model.Parameter
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel
import fr.lgi2a.similar2logo.lib.tools.http.SimilarHttpServerWithGridView
import static java.lang.Math.*
import static fr.lgi2a.similar2logo.lib.tools.RandomValueFactory.strategy as rand

def parameters = new LogoSimulationParameters() {														//defines the parameters of the simulation
	
	 @Parameter(name = "repulsion distance", description = "the repulsion distance")
	 public double repulsionDistance = 6
	 
	 @Parameter(name = "attraction distance", description = "the attraction distance")
	 public double attractionDistance = 14
	 
	 @Parameter(name = "orientation distance", description = "the orientation distance")
	 public double orientationDistance = 10
	 
	 @Parameter(name = "maximal initial speed", description = "the maximal initial speed")
	 public double maxInitialSpeed = 2
	 
	 @Parameter(name = "minimal initial speed", description = "the minimal initial speed")
	 public double minInitialSpeed = 1
	 
	 @Parameter(name = "perception angle", description = "the perception angle in rad")
	 public double perceptionAngle = PI
	 
	 @Parameter(name = "number of agents", description = "the number of boids in the simulation")
	 public int nbOfAgents = 200
	
	 @Parameter(name = "max angular speed", description = "the maximal angular speed in rad/step")
	 public double maxAngle = PI/8
}

def decisionModel = new AbstractAgtDecisionModel(LogoSimulationLevelList.LOGO) {						//defines the decision model of a boid
	void decide(																						
		SimulationTimeStamp s,																			//the current simulation step
		SimulationTimeStamp ns,																			//the next simulation step
		IGlobalState gs,																				//the global state of the agent
		ILocalStateOfAgent pls,																			//the public local state of the boid
		ILocalStateOfAgent prls,																		//the private local state of the boid
		IPerceivedData pd,																				//the data perceived by the boid
		InfluencesMap i																					//the influences produced by the boid
	) {	
		if(!pd.turtles.empty) {	
			def sc = 0, 																				//defines the speed command
			sinoc = 0, 																					//defines the sin of the orientation command
			cosoc = 0, 																					//defines the cos of the orientation command
			n = 0																						//defines the number of boids in the orientation area
			pd.turtles.each{ boid ->																	//computes the commands according to 
				switch(boid.distanceTo) {																//the area in which the perceived boid is located 
					case {it <= parameters.repulsionDistance}:											//the repulsion area
						sinoc+=sin(pls.direction- boid.directionTo)
						cosoc+=cos(pls.direction- boid.directionTo)
						break
					case {it > parameters.repulsionDistance && it <= parameters.orientationDistance}:	//the orientation area
						sinoc+=sin(boid.content.direction - pls.direction)
						cosoc+=cos(boid.content.direction - pls.direction)
						sc+=boid.content.speed - pls.speed
						n++
						break
					case {it > parameters.orientationDistance && it <= parameters.attractionDistance}:	//the attraction area
						sinoc+=sin(boid.directionTo- pls.direction)
						cosoc+=cos(boid.directionTo- pls.direction)
						break
				}
			}
			def oc = atan2(sinoc/pd.turtles.size(), cosoc/pd.turtles.size())							//defines the orientation command	
			if (oc != 0) {
				if(abs(oc) > parameters.maxAngle) oc = signum(oc)*parameters.maxAngle					//ceils the orientation command	
				i.add new ChangeDirection(s, ns, oc, pls)												//emits a change direction influence
			}
			if (n > 0) i.add new ChangeSpeed(s, ns, sc/n, pls)											//emits a change speed influence
		}
	}
}

def simulationModel = new LogoSimulationModel(parameters) {												//defines the initial state of the simulation
	protected AgentInitializationData generateAgents(
		ISimulationParameters p,
		Map<LevelIdentifier, ILevel> l
	) {
		def result = new AgentInitializationData()
		p.nbOfAgents.times {																			//for each boid to be generated
			result.agents.add TurtleFactory.generate(													//generates the boid
				new TurtlePerceptionModel(p.attractionDistance,p.perceptionAngle,true,false,false),		//defines the perception model of the boid
				decisionModel,																			//defines the decision model of the boid
				new AgentCategory("b", TurtleAgentCategory.CATEGORY),									//defines the category of the boid
				rand.randomAngle(),																		//defines initial the orientation of the boid
				p.minInitialSpeed + rand.randomDouble()*(p.maxInitialSpeed-p.minInitialSpeed),			//defines the initial speed of the boid
				0,																						//defines the initial acceleration of the boid
				p.gridWidth/2,																			//defines the initial x position of the boid
				p.gridHeight/2																			//defines the initial y position of the boid
			)
		}
		return result
	}
}

new SimilarHttpServerWithGridView(simulationModel,"Boids").run() 										//Runs the web server

