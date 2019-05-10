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

java_import 'java.lang.Double'
java_import 'java.awt.geom.Point2D'
java_import 'fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel'
java_import 'fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.AgentCategory'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel'
java_import 'fr.univ_artois.lgi2a.similar.microkernel.libs.probes.RealTimeMatcherProbe'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.DropMark'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.RemoveMark'
java_import 'fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList'
java_import 'fr.univ_artois.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel'
java_import 'fr.univ_artois.lgi2a.similar2logo.lib.tools.web.Similar2LogoWebRunner'

java_package 'fr.univ_artois.lgi2a.similar2logo.examples.turmite'

class TurmiteDecisionModel < AbstractAgtDecisionModel
  
  def initialize
    super(LogoSimulationLevelList::LOGO)
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
    if perceivedData.getMarks.empty?
      producedInfluences.add(
        ChangeDirection.new(
          timeLowerBound,
          timeUpperBound,
          Math::PI/2,
          publicLocalState
        )
      )
      producedInfluences.add(
        DropMark.new(
          timeLowerBound,
          timeUpperBound,
          Mark.new(
            publicLocalState.getLocation.clone,
            nil
          )
        )
      )
    else
      producedInfluences.add(
        ChangeDirection.new(
          timeLowerBound,
          timeUpperBound,
          -Math::PI/2,
          publicLocalState
        )
      )
      producedInfluences.add(
        RemoveMark.new(
          timeLowerBound,
          timeUpperBound,
          perceivedData.getMarks.iterator.next.getContent
        )
      )
    end
  end
end

class TurmiteSimulationModel < AbstractLogoSimulationModel
  def generateAgents(p, levels)
      result =  AgentInitializationData.new
      turtle = TurtleFactory::generate(
        ConeBasedPerceptionModel.new(0, 2*Math::PI, false, true, false),
        TurmiteDecisionModel.new,
        AgentCategory.new("turmite", TurtleAgentCategory::CATEGORY),
        LogoEnvPLS::NORTH,
        1,
        0,
        10.5,
        10.5
      )
      result.agents.add(turtle)
      return result
    end
end

runner = Similar2LogoWebRunner.new
runner.config.setExportAgents(true)
runner.config.setExportMarks( true )
runner.initializeRunner(TurmiteSimulationModel.new(LogoSimulationParameters.new))
runner.showView
runner.addProbe("Real time matcher", RealTimeMatcherProbe.new(20))