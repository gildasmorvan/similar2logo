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
package fr.univ_artois.lgi2a.similar2logo.examples.passive


import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.annotations.Parameter
import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS
import fr.univ_artois.lgi2a.similar2logo.lib.model.PassiveTurtleDecisionModel
import fr.univ_artois.lgi2a.similar2logo.lib.model.EmptyPerceptionModel
import fr.univ_artois.lgi2a.similar2logo.lib.probes.LogoRealTimeMatcher
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.Similar2LogoWebRunner

//Defines the parameters of the simulation.
def simulationParameters = new LogoSimulationParameters() {

	@Parameter(
		name = "initial x",
		description = "the initial position of the turtle on the x axis"
	 )
	 public double initialX = 10
	 
	 @Parameter(
		name = "initial y",
		description = "the initial position of the turtle on the y axis"
	 )
	 public double initialY = 10
	 
	 @Parameter(
		name = "initial speed",
		description = "the initial speed of the turtle"
	 )
	 public double initialSpeed = 0.1
	 
	 @Parameter(
		name = "initial acceleration",
		description = "the initial acceleration of the turtle"
	 )
	 public double initialAcceleration = 0
	 
	 @Parameter(
		name = "initial direction",
		description = "the initial direction of the turtle"
	 )
	 public double initialDirection = LogoEnvPLS.NORTH

}

//Defines the simulation model of the simulation.
def simulationModel = new AbstractLogoSimulationModel(simulationParameters) {
	protected AgentInitializationData generateAgents(
		ISimulationParameters p,
		Map<LevelIdentifier, ILevel> levels
	) {
		AgentInitializationData result = new AgentInitializationData()
		IAgent4Engine turtle = TurtleFactory.generate(
			new EmptyPerceptionModel(),
			new PassiveTurtleDecisionModel(),
			new AgentCategory("passive", TurtleAgentCategory.CATEGORY),
			p.initialDirection,
			p.initialSpeed,
			p.initialAcceleration,
			p.initialX,
			p.initialY
		)
		result.agents.add turtle
		return result
	}
}

def runner = new Similar2LogoWebRunner( )																// Creation of the runner
runner.config.exportAgents = true																		// Configuration of the runner
runner.initializeRunner simulationModel																	// Initialize the runner
runner.showView( )																						// Open the GUI
runner.addProbe "Real time matcher", new LogoRealTimeMatcher(20)										//Add a real time matcher probe