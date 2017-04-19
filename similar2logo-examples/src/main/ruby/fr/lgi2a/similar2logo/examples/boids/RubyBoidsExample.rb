require 'java'
Dir["/Users/morvan/Logiciels/similar2logo/similar2logo-distribution/target/similar2logo-distribution-0.9-SNAPSHOT-bin/lib/*.jar"].each { |jar| require jar }

java_import 'java.lang.Double'
java_import 'fr.lgi2a.similar2logo.lib.tools.RandomValueFactory'
java_import 'fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel'
java_import 'fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters'
java_import 'fr.lgi2a.similar.microkernel.AgentCategory'
java_import 'fr.lgi2a.similar.microkernel.LevelIdentifier'
java_import 'fr.lgi2a.similar.microkernel.SimulationTimeStamp'
java_import 'fr.lgi2a.similar.microkernel.ISimulationModel'
java_import 'fr.lgi2a.similar.microkernel.agents.IGlobalState'
java_import 'fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent'
java_import 'fr.lgi2a.similar.microkernel.agents.IPerceivedData'
java_import 'fr.lgi2a.similar.microkernel.influences.InfluencesMap'
java_import 'fr.lgi2a.similar.microkernel.levels.ILevel'
java_import 'fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel'
java_import 'fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters'
java_import 'fr.lgi2a.similar2logo.kernel.model.Parameter'
java_import 'fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory'
java_import 'fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory'
java_import 'fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection'
java_import 'fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed'
java_import 'fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList'
java_import 'fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel'
java_import 'fr.lgi2a.similar2logo.lib.tools.html.Similar2LogoHtmlRunner'
java_import 'fr.lgi2a.similar2logo.kernel.tools.FastMath'

java_package 'fr.lgi2a.similar2logo.examples.boids'

class BoidsSimulationParameters < LogoSimulationParameters
  
  def repulsionDistance; 6 end
  
  def attractionDistance; 14 end
  
  def orientationDistance; 10 end
  
  def maxInitialSpeed; 2 end
  
  def minInitialSpeed; 1 end
  
  def perceptionAngle; Math::PI end
  
  def nbOfAgents; 200 end
 
  def maxAngle; Math::PI/8 end
  
end

class BoidDecisionModel < AbstractAgtDecisionModel
  
  def initialize(parameters)
    super(LogoSimulationLevelList::LOGO)
    @parameters = parameters
  end
  
  def decide(
    timeLowerBound,
    timeUpperBound,
    globalState,
    publicLocalState,
    privateLocalState,
    perceivedData,
    producedInfluences
  )
    if !perceivedData.getTurtles.empty?
      orientationSpeed = 0
      sinAngle = 0
      cosAngle = 0
      nbOfTurtlesInOrientationArea = 0
      perceivedData.getTurtles.each do |perceivedTurtle|
        if perceivedTurtle  != publicLocalState
          if perceivedTurtle.getDistanceTo <= @parameters.repulsionDistance
            sinAngle+=Math.sin(publicLocalState.getDirection - perceivedTurtle.getDirectionTo)
            cosAngle+=Math.cos(publicLocalState.getDirection- perceivedTurtle.getDirectionTo)
          elsif perceivedTurtle.getDistanceTo <= @parameters.orientationDistance
            sinAngle+=Math.sin(perceivedTurtle.getContent.getDirection - publicLocalState.getDirection)
            cosAngle+=Math.cos(perceivedTurtle.getContent.getDirection - publicLocalState.getDirection)
            orientationSpeed+=perceivedTurtle.getContent.getSpeed - publicLocalState.getSpeed
            nbOfTurtlesInOrientationArea+=1
          elsif perceivedTurtle.getDistanceTo <= @parameters.attractionDistance
            sinAngle+=Math.sin(perceivedTurtle.getDirectionTo- publicLocalState.getDirection)
            cosAngle+=Math.cos(perceivedTurtle.getDirectionTo- publicLocalState.getDirection)
          end
        end
      end
      sinAngle /= perceivedData.getTurtles().size()
      cosAngle /= perceivedData.getTurtles().size()
      dd = FastMath::atan2(sinAngle, cosAngle)
      if dd.abs >= Double::MIN_VALUE
        if dd > @parameters.maxAngle
          dd = @parameters.maxAngle
        elsif dd<-@parameters.maxAngle
          dd = -@parameters.maxAngle
        end
        producedInfluences.add(
          ChangeDirection.new(
           timeLowerBound,
           timeUpperBound,
           dd,
           publicLocalState
         )
       )
     end
     if nbOfTurtlesInOrientationArea > 0
        orientationSpeed /= nbOfTurtlesInOrientationArea
        producedInfluences.add(
          ChangeSpeed.new(
            timeLowerBound,
            timeUpperBound,
            orientationSpeed,
            publicLocalState
          )
        )
     end
    end
  end
end

class BoidsSimulationModel < LogoSimulationModel
  def generateAgents(p, levels)
     result =  AgentInitializationData.new
     p.nbOfAgents.times do |i|
      result.getAgents.add(
        TurtleFactory::generate(
         TurtlePerceptionModel.new(p.attractionDistance,p.perceptionAngle,true,false,false),
         BoidDecisionModel.new(p),
         AgentCategory.new("b", TurtleAgentCategory::CATEGORY),
         Math::PI-RandomValueFactory::getStrategy.randomDouble*2*Math::PI,
         p.minInitialSpeed + RandomValueFactory::getStrategy.randomDouble*(
           p.maxInitialSpeed-p.minInitialSpeed
         ),
         0,
         p.gridWidth/2,
         p.gridHeight/2
       )
      )
    end
    return result
  end
end

runner = Similar2LogoHtmlRunner.new
runner.config.setExportAgents(true)
runner.initializeRunner(BoidsSimulationModel.new(BoidsSimulationParameters.new))
runner.showView