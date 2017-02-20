package fr.lgi2a.similar2logo.examples.passive


import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters
import fr.lgi2a.similar.microkernel.AgentCategory
import fr.lgi2a.similar.microkernel.LevelIdentifier
import fr.lgi2a.similar.microkernel.SimulationTimeStamp
import fr.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine
import fr.lgi2a.similar.microkernel.levels.ILevel
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel
import fr.lgi2a.similar2logo.kernel.model.Parameter
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS
import fr.lgi2a.similar2logo.lib.model.PassiveTurtleDecisionModel
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel
import fr.lgi2a.similar2logo.lib.tools.http.spark.SparkHttpServer

//Defines the parameters of the simulation.
def simulationParameters = new LogoSimulationParameters() {

	@Parameter(
		name = "initial x",
		description = "the initial position of the turtle on the x axis"
	 )
	 public double initialX = 0
	 
	 @Parameter(
		name = "initial y",
		description = "the initial position of the turtle on the y axis"
	 )
	 public double initialY = 0
	 
	 @Parameter(
		name = "initial speed",
		description = "the initial speed of the turtle"
	 )
	 public double initialSpeed = 0.01
	 
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
def simulationModel = new LogoSimulationModel(simulationParameters) {
	protected AgentInitializationData generateAgents(
		ISimulationParameters p,
		Map<LevelIdentifier, ILevel> levels
	) {
		AgentInitializationData result = new AgentInitializationData()
		IAgent4Engine turtle = TurtleFactory.generate(
			new TurtlePerceptionModel(0, Double.MIN_VALUE, false, false, false),
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

//Launch the web server.
new SparkHttpServer(simulationModel, true, false, false)