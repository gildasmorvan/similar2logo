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
        
from fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl import AbstractAgtDecisionModel
from fr.univ_artois.lgi2a.similar.microkernel import AgentCategory
from fr.univ_artois.lgi2a.similar.microkernel.ISimulationModel import AgentInitializationData
from fr.univ_artois.lgi2a.similar2logo.kernel.initializations import AbstractLogoSimulationModel
from fr.univ_artois.lgi2a.similar2logo.kernel.model import LogoSimulationParameters
from fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle import TurtleFactory, \
    TurtleAgentCategory
from fr.univ_artois.lgi2a.similar2logo.kernel.model.influences import ChangeDirection, \
    ChangeSpeed
from fr.univ_artois.lgi2a.similar2logo.kernel.model.levels import LogoSimulationLevelList
from fr.univ_artois.lgi2a.similar2logo.kernel.tools import MathUtil
from fr.univ_artois.lgi2a.similar2logo.lib.model import ConeBasedPerceptionModel
from fr.univ_artois.lgi2a.similar2logo.lib.tools.web import Similar2LogoWebRunner
from fr.univ_artois.lgi2a.similar2logo.lib.tools.math import MeanAngle
from fr.univ_artois.lgi2a.similar.extendedkernel.libs.random import PRNG

class BoidsSimulationParameters(LogoSimulationParameters): 

    def __init__(self):
        self.repulsionDistance = 1.0
        self.orientationDistance = 2.0
        self.attractionDistance = 4.0
        self.repulsionWeight = 10.0
        self.orientationWeight = 20.0
        self.attractionWeight = 0.1
        self.maxInitialSpeed = 2.0
        self.minInitialSpeed = 1.0
        self.perceptionAngle = math.pi
        self.nbOfAgents = 2000
        self.maxAngle = math.pi / 4

class BoidDecisionModel(AbstractAgtDecisionModel):
    
    def __init__(self, parameters):
        self.parameters = parameters
        super(BoidDecisionModel, self).__init__(LogoSimulationLevelList.LOGO)
    
    def decide(
        self,
        timeLowerBound,
        timeUpperBound,
        globalState,
        publicLocalState,
        privateLocalState,
        perceivedData,
        producedInfluences
    ):
        if not perceivedData.turtles.isEmpty():
            orientationSpeed = 0.0
            nbOfTurtlesInOrientationArea = 0
            meanAngle = MeanAngle()
            for perceivedTurtle in perceivedData.turtles:
                if perceivedTurtle.distanceTo <= self.parameters.repulsionDistance:
                    meanAngle.add(
                        publicLocalState.direction - perceivedTurtle.directionTo,
                        self.parameters.repulsionWeight
                    )
                elif perceivedTurtle.distanceTo <= self.parameters.orientationDistance:
                    meanAngle.add(
                        perceivedTurtle.content.direction - publicLocalState.direction,
                        self.parameters.orientationWeight
                    )
                    orientationSpeed += perceivedTurtle.content.speed - publicLocalState.speed
                    nbOfTurtlesInOrientationArea+=1
                elif perceivedTurtle.distanceTo <= self.parameters.attractionDistance:
                    meanAngle.add(
                        perceivedTurtle.directionTo - publicLocalState.direction,
                        self.parameters.attractionWeight
                    )
   
            dd = meanAngle.value()
            if not MathUtil.areEqual(dd, 0.0):
                if dd > self.parameters.maxAngle:
                    dd = self.parameters.maxAngle
                elif dd < -self.parameters.maxAngle:
                    dd = -self.parameters.maxAngle

                producedInfluences.add(
                    ChangeDirection(
                        timeLowerBound,
                        timeUpperBound,
                        dd,
                        publicLocalState
                    )
                )
                
            if  nbOfTurtlesInOrientationArea > 0:
                orientationSpeed /= nbOfTurtlesInOrientationArea;
                producedInfluences.add(
                    ChangeSpeed(
                        timeLowerBound,
                        timeUpperBound,
                        orientationSpeed,
                        publicLocalState
                    )
                )
           
class BoidsSimulationModel(AbstractLogoSimulationModel):
    
    def __init__(self, parameters):
        super(BoidsSimulationModel, self).__init__(parameters)
    
    def generateBoid(self, p):
        return TurtleFactory.generate(
            ConeBasedPerceptionModel(
                p.attractionDistance, p.perceptionAngle, True, False, False
            ),
            BoidDecisionModel(p),
            AgentCategory("b", [TurtleAgentCategory.CATEGORY]),
            PRNG.randomAngle(),
            p.minInitialSpeed + PRNG.randomDouble() * (
                p.maxInitialSpeed - p.minInitialSpeed
            ),
            0.0,
            PRNG.randomDouble() * p.gridWidth,
            PRNG.randomDouble() * p.gridHeight
        )
        
    def generateAgents(self, parameters, levels):
        result = AgentInitializationData()
        for i in range(0, parameters.nbOfAgents):
            result.agents.add(self.generateBoid(parameters))
        return result
    
runner = Similar2LogoWebRunner()
runner.config.setExportAgents(True)
model = BoidsSimulationModel(BoidsSimulationParameters())
runner.initializeRunner(model)
runner.showView()