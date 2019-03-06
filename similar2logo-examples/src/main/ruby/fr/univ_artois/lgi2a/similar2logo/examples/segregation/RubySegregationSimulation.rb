=begin
Copyright or © or Copr. LGI2A

LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
Faculte des Sciences Appliquees
Technoparc Futura
62400 - BETHUNE Cedex
http://www.lgi2a.univ-artois.fr/

Email: gildas.morvan@univ-artois.fr

Contributors:
 Gildas MORVAN (creator of the IRM4MLS formalism)
 Yoann KUBERA (designer, architect and developer of SIMILAR)

This software is a computer program whose purpose is to support the 
implementation of Logo-like simulations using the SIMILAR API.
This software defines an API to implement such simulations, and also 
provides usage examples.

This software is governed by the CeCILL-B license under French law and
abiding by the rules of distribution of free software.  You can  use, 
modify and/ or redistribute the software under the terms of the CeCILL-B
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info". 

As a counterpart to the access to the source code and  rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability. 

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate,  and  that  also
therefore means  that it is reserved for developers  and  experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or 
data to be ensured and,  more generally, to use and operate it in the 
same conditions as regards security. 

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-B license and that you accept its terms.
=end

require 'java'

Dir["/Users/morvan/Logiciels/similar2logo/similar2logo-distribution/target/similar2logo-distribution-1.0-SNAPSHOT-bin/lib/*.jar"].each { |jar| require jar }

java_import 'java.util.LinkedList'
java_import 'java.awt.geom.Point2D'
java_import 'java.util.ArrayList'
java_import 'fr.univ_artois.lgi2a.similar.extendedkernel.levels.ExtendedLevel'
java_import 'fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel'
java_import 'fr.univ_artois.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel'
java_import 'fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.annotations.Parameter'
java_import 'fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.AgentCategory'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.influences.RegularInfluence'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel'
java_import 'fr.univ_artois.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel'
java_import 'fr.univ_artois.lgi2a.similar2logo.lib.tools.web.Similar2LogoWebRunner'
java_import 'fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG'

java_package 'fr.univ_artois.lgi2a.similar2logo.examples.segregation'

class SegregationSimulationParameters < LogoSimulationParameters
  
  attr_accessor :similarityRate, :vacancyRate, :perceptionDistance
  def initialize
    
    @similarityRate = 3.0/8
  
    @vacancyRate = 0.05
  
    @perceptionDistance = Math::sqrt(2)
  end
  
end

class Move < RegularInfluence
  
  attr_accessor :target
  def initialize( s, ns, target)
    super("move", LogoSimulationLevelList::LOGO, s, ns)
    @target = target
  end
  
end


class SegregationAgentDecisionModel < AbstractAgtDecisionModel
  
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
    sr = 0.0
    perceivedData.turtles.each do |perceivedTurtle|
      if perceivedTurtle.content.categoryOfAgent.isA(publicLocalState.categoryOfAgent)
        sr+=1.0
      end
    end
    if !perceivedData.turtles.empty
      sr/= perceivedData.turtles.size
    end
    if sr < @parameters.similarityRate
      producedInfluences.add(Move.new(timeLowerBound, timeUpperBound, publicLocalState))
    end
  end
end

class SegregationReactionModel < LogoDefaultReactionModel
  def makeRegularReaction(
      transitoryTimeMin,
      transitoryTimeMax,
      consistentState,
      regularInfluencesOftransitoryStateDynamics,
      remainingInfluences
    )
    if regularInfluencesOftransitoryStateDynamics.size > 2
      specificInfluences = ArrayList.new
      vacantPlaces = ArrayList.new
      specificInfluences.addAll(regularInfluencesOftransitoryStateDynamics)
      PRNG::get.shuffle(specificInfluences)
      for x in 0..consistentState.publicLocalStateOfEnvironment.width()-1
        for y in 0..consistentState.publicLocalStateOfEnvironment.height()-1
          if consistentState.publicLocalStateOfEnvironment.getTurtlesAt(x, y).empty?
            vacantPlaces.add(Point2D::Double.new(x,y))
          end
        end
      end
      PRNG::get.shuffle(vacantPlaces)
      i = 0
      specificInfluences.each do |influence|
        if influence.getCategory() == "move"
          consistentState.publicLocalStateOfEnvironment.turtlesInPatches[influence.target.location.x.floor][influence.target.location.y.floor].clear()
          consistentState.publicLocalStateOfEnvironment.turtlesInPatches[vacantPlaces[i].x.floor][vacantPlaces[i].y.floor].add(influence.target)
          influence.target.setLocation(vacantPlaces[i])
          i+=1      
        end
        if i >= vacantPlaces.size
          break
        end
      end
    end
  end 
end

class SegregationSimulationModel < AbstractLogoSimulationModel
  def generateLevels(p)
    logo = ExtendedLevel.new(
      p.initialTime,
      LogoSimulationLevelList::LOGO,
      PeriodicTimeModel.new(1,0, p.initialTime),
      SegregationReactionModel.new
    )
    levelList = LinkedList.new
    levelList.add(logo)
    return levelList
  end
  def generateAgents(p, levels)
     result =  AgentInitializationData.new
     t = ""
    for x in 0...p.gridWidth-1
      for y in 0..p.gridHeight-1
        if PRNG::get.randomDouble >= p.vacancyRate
          if PRNG::get.randomBoolean
            t = "a"
          else
            t = "b"
          end
          turtle = TurtleFactory.generate(
            ConeBasedPerceptionModel.new(p.perceptionDistance, 2*Math::PI, true, false, false),
            SegregationAgentDecisionModel.new(p),
            AgentCategory.new(t, TurtleAgentCategory::CATEGORY),
            0,
            0,
            0,
            x,
            y
          )
          result.agents.add( turtle )
        end  
      end
    end
    return result
  end
end

segregationgui = %{
    <canvas id='grid_canvas' class='center-block' width='400' height='400'></canvas>
    <script type='text/javascript'>
        drawCanvas = function (data) {
            var json = JSON.parse(data),
                canvas = document.getElementById('grid_canvas'),
                context = canvas.getContext('2d');
            context.clearRect(0, 0, canvas.width, canvas.height);
            for (var i = 0; i < json.agents.length; i++) {
                var centerX = json.agents[i].x * canvas.width;
                var centerY = json.agents[i].y * canvas.height;
                var radius = 2;
                if (json.agents[i].t == 'a') {
                    context.fillStyle = 'red';
                } else {
                    context.fillStyle = 'blue';
                }
                context.beginPath();
                context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
                context.fill();
            }
        }
</script>}
runner = Similar2LogoHtmlRunner.new
runner.config.setCustomHtmlBodyFromString(segregationgui)
runner.config.setExportAgents(true)
runner.initializeRunner(SegregationSimulationModel.new(SegregationSimulationParameters.new))
runner.showView