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
        
from fr.lgi2a.similar.extendedkernel.levels import ExtendedLevel
from fr.lgi2a.similar.extendedkernel.libs.timemodel import PeriodicTimeModel
from fr.lgi2a.similar.microkernel import AgentCategory
from fr.lgi2a.similar.microkernel.ISimulationModel import AgentInitializationData
from fr.lgi2a.similar2logo.examples.turmite import TurmiteDecisionModel
from fr.lgi2a.similar2logo.kernel.initializations import AbstractLogoSimulationModel
from fr.lgi2a.similar2logo.kernel.model import LogoSimulationParameters
from fr.lgi2a.similar2logo.kernel.model.agents.turtle import TurtleFactory, \
    TurtleAgentCategory
from fr.lgi2a.similar2logo.kernel.model.environment import LogoEnvPLS
from fr.lgi2a.similar2logo.kernel.model.influences import DropMark, RemoveMark, \
    ChangeDirection
from fr.lgi2a.similar2logo.kernel.model.levels import LogoDefaultReactionModel, \
    LogoSimulationLevelList
from fr.lgi2a.similar2logo.lib.model import ConeBasedPerceptionModel
from fr.lgi2a.similar2logo.lib.probes import LogoRealTimeMatcher
from fr.lgi2a.similar2logo.lib.tools.html import Similar2LogoHtmlRunner
from fr.lgi2a.similar2logo.lib.tools.random import PRNG
from java.awt.geom import Point2D
from java.util import LinkedHashSet
      
class MultiTurmiteSimulationParameters(LogoSimulationParameters): 

    def __init__(self):
        self.removeDirectionChange = False
        self.inverseMarkUpdate = False
        self.nbOfTurmites = 4
        self.initialLocations = [
            Point2D.Double(
                math.floor(self.gridWidth / 2.0),
                math.floor(self.gridHeight / 2.0)
            ),
            Point2D.Double(
                math.floor(self.gridWidth / 2.0),
                math.floor(self.gridHeight / 2.0) + 1
            ),
            Point2D.Double(
                math.floor(self.gridWidth / 2.0) + 10,
                math.floor(self.gridHeight / 2.0)
            ),
            Point2D.Double(
                math.floor(self.gridWidth / 2.0) + 10,
                math.floor(self.gridHeight / 2.0) + 1
            )
        ]
        self.initialDirections = [
            LogoEnvPLS.NORTH,
            LogoEnvPLS.SOUTH,
            LogoEnvPLS.NORTH,
            LogoEnvPLS.SOUTH
        ]
        
class TurmiteInteraction:
    
    def __init__(self):
        self.dropMarks = LinkedHashSet()
        self.removeMarks = LinkedHashSet()
        self.changeDirections = LinkedHashSet()
    
    def isColliding(self):
        return self.removeMarks.size() > 1 or self.dropMarks.size() > 1
    
class MultiTurmiteReactionModel(LogoDefaultReactionModel):
        
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
            nonSpecificInfluences = LinkedHashSet()
            collisions = {}
            
            for influence in regularInfluencesOftransitoryStateDynamics:
                if influence.category == DropMark.CATEGORY:
                    if not influence.mark.location in collisions:
                        collisions[influence.mark.location] = TurmiteInteraction()
                    collisions[influence.mark.location].dropMarks.add(influence)
                elif influence.category == RemoveMark.CATEGORY:
                    if not influence.mark.location in collisions:
                        collisions[influence.mark.location] = TurmiteInteraction()
                    collisions[influence.mark.location].removeMarks.add(influence)
                elif influence.category == ChangeDirection.CATEGORY:
                    if not influence.target.location in collisions:
                        collisions[influence.target.location] = TurmiteInteraction()
                    collisions[influence.target.location].changeDirections.add(influence)
                else:
                    nonSpecificInfluences.add(influence)

            for collision in collisions.values():
                if collision.isColliding():
                    if not collision.dropMarks.isEmpty() and not self.parameters.inverseMarkUpdate:
                        nonSpecificInfluences.add(
                            collision.dropMarks.iterator().next()
                        )
                    if not collision.removeMarks.isEmpty() and not self.parameters.inverseMarkUpdate:
                        nonSpecificInfluences.add(
                            collision.removeMarks.iterator().next()
                        )        
                    if not self.parameters.removeDirectionChange:
                        nonSpecificInfluences.addAll(collision.changeDirections)
                else:
                    nonSpecificInfluences.addAll(collision.changeDirections)
                    if not collision.dropMarks.isEmpty():
                        nonSpecificInfluences.add(
                            collision.dropMarks.iterator().next()
                        )
                    if not collision.removeMarks.isEmpty():
                        nonSpecificInfluences.add(
                            collision.removeMarks.iterator().next()
                        )
            super(MultiTurmiteReactionModel, self).makeRegularReaction(
                transitoryTimeMin,
                transitoryTimeMax,
                consistentState,
                nonSpecificInfluences,
                remainingInfluences
            )
            
class MultiTurmiteSimulationModel(AbstractLogoSimulationModel):
    
    def __init__(self, parameters):
        super(MultiTurmiteSimulationModel, self).__init__(parameters)
    
    def randomDirection(self):
        rand = PRNG.get().randomDouble()
        if rand < 0.25:
            return LogoEnvPLS.NORTH
        elif rand < 0.5:
            return LogoEnvPLS.WEST
        elif  rand < 0.75:
            return LogoEnvPLS.SOUTH
        return LogoEnvPLS.EAST
    
    
    def generateLevels(self, simulationParameters):
        logo = ExtendedLevel(
            simulationParameters.initialTime,
            LogoSimulationLevelList.LOGO,
            PeriodicTimeModel(
                1,
                0,
                simulationParameters.initialTime
            ),
            MultiTurmiteReactionModel(simulationParameters)
        )
        levelList = []
        levelList.append(logo)
        return levelList
    
    def generateAgents(self, parameters, levels):
        result = AgentInitializationData()
        if not parameters.initialLocations:
            for i in range(0, parameters.nbOfTurmites):
                turtle = TurtleFactory.generate(
                    ConeBasedPerceptionModel(0.0, 2*math.pi, False, True, False),
                    TurmiteDecisionModel(),
                    AgentCategory('turmite', [TurtleAgentCategory.CATEGORY]),
                    self.randomDirection(),
                    1.0,
                    0.0,
                    math.floor(PRNG.get().randomDouble()*parameters.gridWidth),
                    math.floor(PRNG.get().randomDouble()*parameters.gridHeight)
                )
                result.getAgents().add( turtle )
        else:
            for i in range(0, parameters.nbOfTurmites):
                turtle = TurtleFactory.generate(
                    ConeBasedPerceptionModel(0.0, 2*math.pi, False, True, False),
                    TurmiteDecisionModel(),
                    AgentCategory('turmite', [TurtleAgentCategory.CATEGORY]),
                    parameters.initialDirections[i],
                    1.0,
                    0.0,
                    parameters.initialLocations[i].x,
                    parameters.initialLocations[i].y
                )
                result.getAgents().add( turtle )
        return result
        
runner = Similar2LogoHtmlRunner()
runner.config.setExportAgents(True)
runner.config.setExportMarks(True)
model = MultiTurmiteSimulationModel(MultiTurmiteSimulationParameters())
runner.initializeRunner(model)
runner.addProbe('Real time matcher', LogoRealTimeMatcher(20.0))
runner.showView()       