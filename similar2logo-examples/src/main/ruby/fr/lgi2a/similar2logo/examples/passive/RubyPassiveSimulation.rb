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