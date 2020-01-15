/**
 * Copyright or © or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the 
 * implementation of Logo-like simulations using the SIMILAR API.
 * This software defines an API to implement such simulations, and also 
 * provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.univ_artois.lgi2a.similar2logo.examples.projettut;

import java.util.Set;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;



/**
 * The reaction model of the simulation
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ReactionModel extends LogoDefaultReactionModel {

	private SimulationParameters parameters;

	/**
	 * Creates a new instance of the ReactionModel class.
	 */
	public ReactionModel(SimulationParameters parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Makes the reaction to position update reaction influence
	 * 
	 * @param transitoryTimeMin The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param transitoryTimeMax The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param agents The turtles local states that are changed.
	 * @param environment The public local state of the Logo environment.
	 * @param remainingInfluences The remaining influences.
	 */
	protected void reactToAgentPositionUpdate(
		SimulationTimeStamp transitoryTimeMin,
		SimulationTimeStamp transitoryTimeMax,
		Set<ILocalStateOfAgent> agents,
		LogoEnvPLS environment,
		InfluencesMap remainingInfluences
	) {
		
		long dt = transitoryTimeMax.compareToTimeStamp(transitoryTimeMin);
		//Update turtle locations
		for (ILocalStateOfAgent agentPLS : agents) {
			
			TurtlePLSInLogo castedTurtlePLS = (TurtlePLSInLogo) agentPLS;
			
			
			castedTurtlePLS.setDirection(
				castedTurtlePLS.getDirection()+parameters.maxAngularNoise*PRNG.randomDouble(-Math.PI, Math.PI)
			);
			
			castedTurtlePLS.setAcceleration(
				castedTurtlePLS.getAcceleration()+parameters.maxAccelerationNoise*PRNG.randomDouble(-1, 1)
			);
			
			
			//Computes speed
			castedTurtlePLS.setSpeed(
				castedTurtlePLS.getSpeed() + castedTurtlePLS.getAcceleration()
			);
			
			if(castedTurtlePLS.getSpeed() < 0) {
				castedTurtlePLS.setSpeed(0);
			}
			
			
			double newX = castedTurtlePLS.getLocation().getX() + castedTurtlePLS.getDX()*dt;
			double newY = castedTurtlePLS.getLocation().getY() + castedTurtlePLS.getDY()*dt;
			
			//If the agent is out of bounds or exceeds max speed, it is removed from the simulation.
			if(newX < 0
				|| newX >=  environment.getWidth()
				|| newY < 0
				|| newY >=  environment.getHeight()
				|| castedTurtlePLS.getSpeed() > parameters.maxSpeed
			) {
				SystemInfluenceRemoveAgent rmInfluence = new SystemInfluenceRemoveAgent(
					LogoSimulationLevelList.LOGO,
					transitoryTimeMax,
					transitoryTimeMin, 
					castedTurtlePLS
				);
				remainingInfluences.add( rmInfluence );
				environment.getTurtlesInPatches()[(int) Math.floor(castedTurtlePLS.getLocation().getX())][(int) Math.floor(castedTurtlePLS.getLocation().getY())].remove(castedTurtlePLS);
				
			} else { 
				//Else the turtle's new location is set.
				//Update turtle patch
				if(
					(int) Math.floor(newX) != (int) Math.floor(castedTurtlePLS.getLocation().getX()) ||
					(int) Math.floor(newY) != (int) Math.floor(castedTurtlePLS.getLocation().getY())
				) {
					environment.getTurtlesInPatches()[(int) Math.floor(castedTurtlePLS.getLocation().getX())][(int) Math.floor(castedTurtlePLS.getLocation().getY())].remove(castedTurtlePLS);
					environment.getTurtlesInPatches()[(int) Math.floor(newX)][(int) Math.floor(newY)].add(castedTurtlePLS);
				}
				castedTurtlePLS.getLocation().setLocation(
					newX,
					newY
				);
			}
		}
	}
	
	/**
	 * 
	 * The reaction method to ChangeDirection influence
	 * 
	 * @param influence The ChangeDirection influence.
	 */
	protected void reactToChangeDirectionInfluence(ChangeDirection influence) {
		if(influence.getDd() < parameters.maxAngularSpeed) {
			influence.getTarget().setDirection(influence.getTarget().getDirection()+ influence.getDd());
		} else {
			influence.getTarget().setDirection(influence.getTarget().getDirection()+ parameters.maxAngularSpeed);
		}
		
	}
}
