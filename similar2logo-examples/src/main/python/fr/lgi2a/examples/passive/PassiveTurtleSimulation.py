import os, fnmatch, sys


maven_path = os.getenv('HOME') + '/.m2/repository/'

for root, dir, files in os.walk(maven_path): 
    for items in fnmatch.filter(files, "*.jar"): 
        sys.path.append(os.path.join(root,items))
        
from fr.lgi2a.similar2logo.kernel.initializations import AbstractLogoSimulationModel
from fr.lgi2a.similar2logo.kernel.model import LogoSimulationParameters
from fr.lgi2a.similar2logo.kernel.model.agents.turtle import TurtleFactory, \
    TurtleAgentCategory
from fr.lgi2a.similar2logo.kernel.model.environment import LogoEnvPLS
from fr.lgi2a.similar2logo.lib.model import EmptyPerceptionModel, \
    PassiveTurtleDecisionModel
from fr.lgi2a.similar2logo.lib.probes import LogoRealTimeMatcher
from fr.lgi2a.similar2logo.lib.tools.html import Similar2LogoHtmlRunner
from fr.lgi2a.similar.microkernel import AgentCategory
from fr.lgi2a.similar.microkernel.ISimulationModel import AgentInitializationData



class PassiveTurtleSimulationParameters(LogoSimulationParameters): 

    def __init__(self):
        self.initialX = 10.0
        self.initialY = 10.0
        self.initialSpeed = 0.1
        self.initialAcceleration = 0.0
        self.initialDirection = LogoEnvPLS.NORTH

class PassiveTurtleSimulationModel(AbstractLogoSimulationModel):
    
    def __init__(self, parameters):
        super(PassiveTurtleSimulationModel, self).__init__(parameters)
    
    def generateAgents(self, parameters, levels):
        result = AgentInitializationData()
        turtle = TurtleFactory.generate(
            EmptyPerceptionModel(),
            PassiveTurtleDecisionModel(),
            AgentCategory('passive', [TurtleAgentCategory.CATEGORY]),
            parameters.initialDirection,
            parameters.initialSpeed,
            parameters.initialAcceleration,
            parameters.initialX,
            parameters.initialY
        )
        result.agents.add(turtle)
        return result
        
runner = Similar2LogoHtmlRunner()
runner.config.setExportAgents(True)
model = PassiveTurtleSimulationModel(PassiveTurtleSimulationParameters())
runner.initializeRunner(model)
runner.addProbe('Real time matcher', LogoRealTimeMatcher(20.0))
runner.showView()