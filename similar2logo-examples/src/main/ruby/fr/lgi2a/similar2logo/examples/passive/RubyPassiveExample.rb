require 'java'
Dir["/Users/morvan/Logiciels/similar2logo/similar2logo-distribution/target/similar2logo-distribution-0.9-SNAPSHOT-bin/lib/*.jar"].each { |jar| require jar }

java_import 'java.lang.Double'
java_import 'fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters'
java_import 'fr.lgi2a.similar.microkernel.AgentCategory'
java_import 'fr.lgi2a.similar.microkernel.LevelIdentifier'
java_import 'fr.lgi2a.similar.microkernel.SimulationTimeStamp'
java_import 'fr.lgi2a.similar.microkernel.ISimulationModel'
java_import 'fr.lgi2a.similar.microkernel.agents.IAgent4Engine'
java_import 'fr.lgi2a.similar.microkernel.levels.ILevel'
java_import 'fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel'
java_import 'fr.lgi2a.similar2logo.kernel.model.Parameter'
java_import 'fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters'
java_import 'fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory'
java_import 'fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory'
java_import 'fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS'
java_import 'fr.lgi2a.similar2logo.lib.model.PassiveTurtleDecisionModel'
java_import 'fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel'
java_import 'fr.lgi2a.similar2logo.lib.probes.LogoRealTimeMatcher'
java_import 'fr.lgi2a.similar2logo.lib.tools.html.Similar2LogoHtmlRunner'

java_package 'fr.lgi2a.similar2logo.examples.passive'

class PassiveSimulationParameters < LogoSimulationParameters
  
  def initialX; 10 end
  
  def initialY; 10 end
  
  def initialSpeed; 1 end
  
  def initialAcceleration; 0 end
  
  def initialDirection; LogoEnvPLS::NORTH end
  
end

class PassiveSimulationModel < LogoSimulationModel
  def generateAgents(p, levels)
    result =  AgentInitializationData.new
    turtle = TurtleFactory::generate(
      TurtlePerceptionModel.new(0, Double::MIN_VALUE, false, false, false),
      PassiveTurtleDecisionModel.new,
      AgentCategory.new("passive", TurtleAgentCategory::CATEGORY),
      p.initialDirection,
      p.initialSpeed,
      p.initialAcceleration,
      p.initialX,
      p.initialY
    )
    result.agents.add(turtle)
    return result
  end
end

simulationParameters = PassiveSimulationParameters.new
simulationModel = PassiveSimulationModel.new(simulationParameters)

runner = Similar2LogoHtmlRunner.new
runner.config.setExportAgents(true)
runner.initializeRunner(simulationModel)
runner.showView
runner.addProbe("Real time matcher", LogoRealTimeMatcher.new(20))