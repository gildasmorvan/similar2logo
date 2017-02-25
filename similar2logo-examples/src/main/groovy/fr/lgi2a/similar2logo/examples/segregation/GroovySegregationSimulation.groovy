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
package fr.lgi2a.similar2logo.examples.segregation

import static java.lang.Math.*
import static  Similar2LogoWebApp.getAppResource
import static fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList.LOGO
import static fr.lgi2a.similar2logo.lib.tools.RandomValueFactory.strategy as rand

import java.awt.geom.Point2D

import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters
import fr.lgi2a.similar.microkernel.AgentCategory
import fr.lgi2a.similar.microkernel.LevelIdentifier
import fr.lgi2a.similar.microkernel.SimulationTimeStamp
import fr.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData
import fr.lgi2a.similar.microkernel.agents.IGlobalState
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent
import fr.lgi2a.similar.microkernel.agents.IPerceivedData
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState
import fr.lgi2a.similar.microkernel.influences.IInfluence
import fr.lgi2a.similar.microkernel.influences.InfluencesMap
import fr.lgi2a.similar.microkernel.influences.RegularInfluence
import fr.lgi2a.similar.microkernel.levels.ILevel
import fr.lgi2a.similar2logo.examples.segregation.model.SegregationSimulationParameters
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters
import fr.lgi2a.similar2logo.kernel.model.Parameter
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel
import fr.lgi2a.similar2logo.lib.tools.http.Similar2LogoWebApp
import fr.lgi2a.similar2logo.lib.tools.http.SparkHttpServer

def parameters = new LogoSimulationParameters() {														//defines the parameters of the simulation
	@Parameter(
		name = "similarity rate",
		description = "the rate of same-color turtles that each turtle wants among its neighbors"
	 )
	 public double similarityRate = 3.0/8
	 
	 @Parameter(name = "vacancy rate", description = "the rate of vacant settling places")
	 public double vacancyRate = 0.05
	 
	 @Parameter(name = "perception distance", description = "the perception distance of agents")
	 public double perceptionDistance = sqrt(2)
}

class Move extends RegularInfluence {																	//The specific influence of this model
	def target																							//The turtle's public local state that is going to change
	static final def CATEGORY = "move"																	//the category of the influence, used as a unique identifier
																										//in the reaction to determine the nature of the influence
	Move(SimulationTimeStamp s, SimulationTimeStamp ns, TurtlePLSInLogo target) {
		super(CATEGORY, LOGO, s, ns)
		this.target = target																		
	}
}

def decisionModel = new AbstractAgtDecisionModel(LOGO) {												//defines the decision model of an agent
	void decide(
		SimulationTimeStamp s,																			//the current simulation step
		SimulationTimeStamp ns,																			//the next simulation step
		IGlobalState gs,																				//the global state of the agent
		ILocalStateOfAgent pls,																			//the public local state of the agent
		ILocalStateOfAgent prls,																		//the private local state of the agent
		IPerceivedData pd,																				//the data perceived by the agent
		InfluencesMap i																					//the influences produced by the agent
	) {
		def sr = 0																						//defines the similarity rate
		pd.turtles.each{ agent -> if(agent.content.categoryOfAgent.isA(pls.categoryOfAgent)) sr++ }		//computes the similarity rate
		if(!pd.turtles.empty) sr/= pd.turtles.size()
		if(sr < parameters.similarityRate) i.add new Move(s, ns, pls)									//if the similarity rate is too low, the agent wants to move
	}
}

def reactionModel = new LogoDefaultReactionModel() {													//defines the reaction model
	public void makeRegularReaction(																	//redefines the reaction function for regular influences
		SimulationTimeStamp s,																			//the current simulation step
		SimulationTimeStamp ns,																			//the next simulation step
		ConsistentPublicLocalDynamicState cs,															//the dynamic state of the simulation
		Set<IInfluence> influences,																		//the influences to process 
		InfluencesMap remainingInfluences																//the influences that will remain i the dynamic state
	) {
		def e = cs.publicLocalStateOfEnvironment,														//defines the environment
			li = [],																					//defines the list of influences
			vacant = []																					//defines the list of vacant housings	
		li.addAll influences																			//creates the list of influences
		Collections.shuffle li																			//shuffles the list of influences
		for(x in 0..<e.width) for(y in 0..<e.height)																
			if(e.getTurtlesAt(x, y).empty) vacant.add new Point2D.Double(x,y)							//identifies vacant housings
		Collections.shuffle vacant																		//shuffles the list of vacant housings
		def n = 0																						
		li.any{ i ->																					//moves lucky unhappy agents to vacant housings
			if(i.category == Move.CATEGORY) {
				e.turtlesInPatches[(int) i.target.location.x][(int) i.target.location.y].remove i.target
				e.turtlesInPatches[(int) vacant[n].x][(int) vacant[n].y].add i.target
				i.target.setLocation(vacant[n])
				if(++n >= vacant.size()) return true													//stops when no more housing is available
			}
		}
	}
}

def simulationModel = new LogoSimulationModel(parameters) {												
	
	 List<ILevel> generateLevels(ISimulationParameters p) {
		def logo = new ExtendedLevel(
			p.initialTime,
			LOGO,
			new PeriodicTimeModel(1,0, p.initialTime),
			reactionModel
		)
		def levelList = []
		levelList.add logo
		return levelList
	}
	
	AgentInitializationData generateAgents(ISimulationParameters p, Map<LevelIdentifier, ILevel> l) {
		def result = new AgentInitializationData()
		for(x in 0..<p.gridWidth) for(y in 0..<p.gridHeight)
			if(rand.randomDouble() >= p.vacancyRate) result.agents.add TurtleFactory.generate(
				new TurtlePerceptionModel(p.perceptionDistance, 2*PI, true, false, false),
				decisionModel,
				new AgentCategory(rand.randomBoolean() ? "a" :"b", TurtleAgentCategory.CATEGORY),
				x,y, 0, 0, 0
			)	
		return result
	}	
}

new SparkHttpServer(simulationModel, true, false, false, this.class.getResource("segregationgui.html"))	//Runs the web server