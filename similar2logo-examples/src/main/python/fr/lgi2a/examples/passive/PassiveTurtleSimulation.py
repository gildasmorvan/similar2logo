#!/usr/bin/env jython
# -*- coding: utf-8 -*-
'''
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

@author: <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
'''
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