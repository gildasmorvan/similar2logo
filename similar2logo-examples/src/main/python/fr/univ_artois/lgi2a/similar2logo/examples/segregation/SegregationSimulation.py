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

from fr.univ_artois.lgi2a.similar.extendedkernel.levels import ExtendedLevel
from fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl import AbstractAgtDecisionModel
from fr.univ_artois.lgi2a.similar.extendedkernel.libs.timemodel import PeriodicTimeModel
from fr.univ_artois.lgi2a.similar.microkernel import AgentCategory
from fr.univ_artois.lgi2a.similar.microkernel.ISimulationModel import AgentInitializationData
from fr.univ_artois.lgi2a.similar.microkernel.influences import RegularInfluence
from fr.univ_artois.lgi2a.similar2logo.kernel.initializations import AbstractLogoSimulationModel
from fr.univ_artois.lgi2a.similar2logo.kernel.model import LogoSimulationParameters
from fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle import TurtleFactory, \
    TurtleAgentCategory
from fr.univ_artois.lgi2a.similar2logo.kernel.model.levels import LogoSimulationLevelList, \
    LogoDefaultReactionModel
from fr.univ_artois.lgi2a.similar2logo.lib.model import ConeBasedPerceptionModel
from fr.univ_artois.lgi2a.similar2logo.lib.tools.web import Similar2LogoWebRunner
from fr.univ_artois.lgi2a.similar.extendedkernel.libs.random import PRNG
from java.awt.geom import Point2D
from java.util import ArrayList

class SegregationSimulationParameters(LogoSimulationParameters): 

    def __init__(self):
        self.similarityRate = 3.0/8
        self.vacancyRate = 0.05
        self.perceptionDistance = math.sqrt(2)
        
class Move(RegularInfluence):
    
    CATEGORY = 'move'
    
    def __init__(self, timeLowerBound, timeUpperBound, target):
        self.target = target
        super(Move, self).__init__(
            self.CATEGORY,
            LogoSimulationLevelList.LOGO,
            timeLowerBound,
            timeUpperBound
        )
        
class SegregationDecisionModel(AbstractAgtDecisionModel):
    
    def __init__(self, parameters):
        self.parameters = parameters
        super(SegregationDecisionModel, self).__init__(
            LogoSimulationLevelList.LOGO
        )
        
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
        similarityRate = 0.0
        for perceivedTurtle in perceivedData.turtles:
            if perceivedTurtle.content.categoryOfAgent.isA(
               publicLocalState.categoryOfAgent
            ):
                similarityRate+=1

        if not perceivedData.turtles.isEmpty():
            similarityRate /= perceivedData.turtles.size()
        if similarityRate < self.parameters.similarityRate:
            producedInfluences.add(
                Move(timeLowerBound, timeUpperBound, publicLocalState)
            )
            
class SegregationReactionModel(LogoDefaultReactionModel):
        
        def __init__(self,parameters):
            self.parameters = parameters

        def makeRegularReaction(
            self,
            transitoryTimeMin,
            transitoryTimeMax,
            consistentState,
            regularInfluencesOftransitoryStateDynamics,
            remainingInfluences
        ):
            #If there there is at least an agent that wants to move
            if regularInfluencesOftransitoryStateDynamics.size() > 2:
                specificInfluences = ArrayList()
                vacantPlaces = ArrayList()
                specificInfluences.addAll(
                    regularInfluencesOftransitoryStateDynamics
                )
                PRNG.shuffle(specificInfluences)
                #Identify vacant places
                envState = consistentState.publicLocalStateOfEnvironment
                for x in range(0, envState.width):
                    for y in range(0,envState.height):
                        if envState.getTurtlesAt(x, y).isEmpty():
                            vacantPlaces.add(
                                Point2D.Double(x, y)
                            )
                PRNG.shuffle(vacantPlaces)
                #move agents
                i = 0
                for influence in specificInfluences:
                    if influence.category == Move.CATEGORY:
                        envState.turtlesInPatches[
                            int(math.floor(influence.target.location.x))
                        ][
                            int(math.floor(influence.target.location.y))
                        ].clear()
                        envState.turtlesInPatches[
                            int(math.floor(vacantPlaces[i].x))
                        ][
                            int(math.floor(vacantPlaces[i].y))
                        ].add(influence.target)
                        influence.target.location = vacantPlaces[i]
                        i+=1
                    if i >= vacantPlaces.size():
                        break
                    
class SegregationSimulationModel(AbstractLogoSimulationModel):
    
    def __init__(self, parameters):
        super(SegregationSimulationModel, self).__init__(parameters)
        
    def generateLevels(self, simulationParameters):
        logo = ExtendedLevel(
            simulationParameters.initialTime,
            LogoSimulationLevelList.LOGO,
            PeriodicTimeModel(
                1,
                0,
                simulationParameters.initialTime
            ),
            SegregationReactionModel(simulationParameters)
        )
        levelList = []
        levelList.append(logo)
        return levelList
    
    def generateAgents(self, parameters, levels):
        result = AgentInitializationData()
        t = ''
        for x in range(0, parameters.gridWidth):
            for y in range(0, parameters.gridHeight):
                if PRNG.randomDouble() >= parameters.vacancyRate:
                    if PRNG.randomBoolean():
                        t = 'a'
                    else:
                        t = 'b'
                    turtle = TurtleFactory.generate(
                            ConeBasedPerceptionModel(
                                parameters.perceptionDistance,
                                2 * math.pi,
                                True,
                                False,
                                False
                            ),
                            SegregationDecisionModel(parameters),
                            AgentCategory(t, [TurtleAgentCategory.CATEGORY]),
                            0.0,
                            0.0,
                            0.0,
                            x,
                            y
                    )
                    result.agents.add(turtle)
        return result
    
segregationgui = '''
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
    </script>'''

runner = Similar2LogoWebRunner()
runner.config.setExportAgents(True)
model = SegregationSimulationModel(SegregationSimulationParameters())
runner.config.setCustomHtmlBodyFromString(segregationgui)
runner.initializeRunner(model)
runner.showView()      