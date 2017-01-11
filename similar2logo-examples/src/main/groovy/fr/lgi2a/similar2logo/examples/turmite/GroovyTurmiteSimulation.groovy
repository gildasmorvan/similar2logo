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
package fr.lgi2a.similar2logo.examples.turmite

import static java.lang.Math.*
import java.awt.geom.Point2D

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters
import fr.lgi2a.similar.microkernel.AgentCategory
import fr.lgi2a.similar.microkernel.LevelIdentifier
import fr.lgi2a.similar.microkernel.SimulationTimeStamp
import fr.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine
import fr.lgi2a.similar.microkernel.agents.IGlobalState
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent
import fr.lgi2a.similar.microkernel.agents.IPerceivedData
import fr.lgi2a.similar.microkernel.influences.InfluencesMap
import fr.lgi2a.similar.microkernel.levels.ILevel
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS
import fr.lgi2a.similar2logo.kernel.model.environment.Mark
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection
import fr.lgi2a.similar2logo.kernel.model.influences.DropMark
import fr.lgi2a.similar2logo.kernel.model.influences.RemoveMark
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel
import fr.lgi2a.similar2logo.lib.tools.http.SimilarHttpServerWithGridView

def parameters = new LogoSimulationParameters(												//defines the parameters of the simulation
	finalTime: new SimulationTimeStamp(100000),
)

def decisionModel = new AbstractAgtDecisionModel(LogoSimulationLevelList.LOGO) {			//defines the decision model of the agents
	void decide(
		SimulationTimeStamp s,																//the current simulation step
		SimulationTimeStamp ns,																//the next simulation step
		IGlobalState gs,																	//the global state of the agent
		ILocalStateOfAgent pls,																//the public local state of the agent
		ILocalStateOfAgent prls,															//the private local state of the agent
		IPerceivedData pd,																	//the data perceived by the agent
		InfluencesMap i																		//the influences produced by the agent
	) {	
		if(pd.marks.empty) i.with {															//if the agent perceives no mark
			add new ChangeDirection(s, ns, PI/2, pls)										//it turns pi/2 rad
			add new DropMark(s, ns, new Mark((Point2D) pls.location.clone(), null))			//and drops a mark
		} else i.with {																		//if the agent perceives a mark
			add new ChangeDirection(s, ns, -PI/2, pls)										//it turns -pi/2 rad
			add new RemoveMark(s, ns,pd.marks.iterator().next().content)					//and removes the mark
		}
	}
}

def turmiteSimulationModel = new LogoSimulationModel(parameters) {							//defines the initial state of the simulation
	protected AgentInitializationData generateAgents(										//generates the agents
		ISimulationParameters simulationParameters,											//the parameters of the simulation
		Map<LevelIdentifier, ILevel> levels													//the levels of the simulation
	) {
		def turmite = TurtleFactory.generate(												//creates a new turmite agent										
			new TurtlePerceptionModel(0, Double.MIN_VALUE, false, true, false),				//a perception model that allows to perceive marks
			decisionModel,																	//the turmite decision model
			new AgentCategory("turmite", TurtleAgentCategory.CATEGORY),						//the turmite category
			LogoEnvPLS.NORTH,																//heading north
			1,																				//a speed of 1
			0,																				//an acceleration of 0
			10.5, 10.5																		//located at 10.5, 10.5
		),
			result = new AgentInitializationData()											//creates the agent initialization data
		result.agents.add turmite															//adds the turmite agent
		return result
	}
}

new SimilarHttpServerWithGridView(turmiteSimulationModel, "Turmite").run();						//Launches the web server that will run the simulation