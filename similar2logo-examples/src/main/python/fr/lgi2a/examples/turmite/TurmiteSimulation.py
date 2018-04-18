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
import math

from fr.lgi2a.similar.extendedkernel.libs.abstractimpl import AbstractAgtDecisionModel
from fr.lgi2a.similar.microkernel import AgentCategory
from fr.lgi2a.similar.microkernel.ISimulationModel import AgentInitializationData
from fr.lgi2a.similar2logo.kernel.initializations import AbstractLogoSimulationModel
from fr.lgi2a.similar2logo.kernel.model import LogoSimulationParameters
from fr.lgi2a.similar2logo.kernel.model.agents.turtle import TurtleFactory, \
    TurtleAgentCategory
from fr.lgi2a.similar2logo.kernel.model.environment import Mark, LogoEnvPLS
from fr.lgi2a.similar2logo.kernel.model.influences import ChangeDirection, \
    DropMark, RemoveMark
from fr.lgi2a.similar2logo.kernel.model.levels import LogoSimulationLevelList
from fr.lgi2a.similar2logo.lib.model import ConeBasedPerceptionModel
from fr.lgi2a.similar2logo.lib.probes import LogoRealTimeMatcher
from fr.lgi2a.similar2logo.lib.tools.html import Similar2LogoHtmlRunner

class TurmiteDecisionModel(AbstractAgtDecisionModel):
    
    def __init__(self):
        super(TurmiteDecisionModel, self).__init__(LogoSimulationLevelList.LOGO)
    
    def decide(self, timeLowerBound, timeUpperBound, globalState, publicLocalState, privateLocalState, perceivedData, producedInfluences):
        if perceivedData.marks.isEmpty():
            producedInfluences.add(
                ChangeDirection(
                    timeLowerBound,
                    timeUpperBound,
                    math.pi/2,
                    publicLocalState
                )
            )
            producedInfluences.add(
                DropMark(
                    timeLowerBound,
                    timeUpperBound,
                    Mark(
                        publicLocalState.location.clone(),
                        None
                    )
                )
            )
        else:
            producedInfluences.add(
                ChangeDirection(
                    timeLowerBound,
                    timeUpperBound,
                    -math.pi/2,
                    publicLocalState
                )
            )
            
            producedInfluences.add(
                RemoveMark(
                    timeLowerBound,
                    timeUpperBound,
                    perceivedData.marks.iterator().next().content
                )
            )
class TurmiteSimulationModel(AbstractLogoSimulationModel):
    
    def __init__(self, parameters):
        super(TurmiteSimulationModel, self).__init__(parameters)
    
    def generateAgents(self, parameters, levels):
        result = AgentInitializationData()
        turtle = TurtleFactory.generate(
            ConeBasedPerceptionModel(0.0, 2 * math.pi, False, True, False),
            TurmiteDecisionModel(),
            AgentCategory('turmite', [TurtleAgentCategory.CATEGORY]),
            LogoEnvPLS.NORTH,
            1.0,
            0.0,
            10.5,
            10.5
        )
        result.agents.add(turtle)
        return result
    
runner = Similar2LogoHtmlRunner()
runner.config.setExportAgents(True)
runner.config.setExportMarks(True)
model = TurmiteSimulationModel(LogoSimulationParameters())
runner.initializeRunner(model)
runner.addProbe('Real time matcher', LogoRealTimeMatcher(20.0))
runner.showView()