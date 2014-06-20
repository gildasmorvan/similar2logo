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
package fr.lgi2a.similar2logo.kernel.model.levels;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.extendedkernel.levels.ILevelReactionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgentToLevel;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import fr.lgi2a.similar2logo.kernel.model.environment.Position;
import fr.lgi2a.similar2logo.kernel.model.influences.AgentPositionUpdate;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeAcceleration;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangePosition;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.DropMark;
import fr.lgi2a.similar2logo.kernel.model.influences.EmitPheromone;
import fr.lgi2a.similar2logo.kernel.model.influences.PheromoneFieldUpdate;
import fr.lgi2a.similar2logo.kernel.model.influences.RemoveMark;
import fr.lgi2a.similar2logo.kernel.model.influences.RemoveMarks;

/**
 * 
 * The default reaction function of a Logo level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class LogoDefaultReactionModel implements ILevelReactionModel {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {
		LogoEnvPLS castedEnvironment = (LogoEnvPLS) consistentState.getPublicLocalStateOfEnvironment();
		int dt = transitoryTimeMax.compareTo(transitoryTimeMin);
		
		//Manage agent influences
		for(IInfluence influence : regularInfluencesOftransitoryStateDynamics) {
			
			if(influence.getCategory().equals(RemoveMarks.CATEGORY)) {
				RemoveMarks castedInfluence = (RemoveMarks) influence;
				for(Mark mark : castedInfluence.getMarks()) {
					castedEnvironment.getMarks()[(int) Math.floor(mark.getLocation().getX())][(int) Math.floor(mark.getLocation().getY())].remove(mark);
				}
			}
			if(influence.getCategory().equals(RemoveMark.CATEGORY)) {
				RemoveMark castedInfluence = (RemoveMark) influence;
				castedEnvironment.getMarks()[(int) Math.floor(castedInfluence.getMark().getLocation().getX())][(int) Math.floor(castedInfluence.getMark().getLocation().getY())].remove(castedInfluence.getMark());
			}
			if(influence.getCategory().equals(DropMark.CATEGORY)) {
				DropMark castedInfluence = (DropMark) influence;
				castedEnvironment.getMarks()[(int) Math.floor(castedInfluence.getMark().getLocation().getX())][(int) Math.floor(castedInfluence.getMark().getLocation().getY())].add(castedInfluence.getMark());			
			}		
			if(influence.getCategory().equals(EmitPheromone.CATEGORY)) {
				EmitPheromone castedInfluence = (EmitPheromone) influence;	
				castedEnvironment.getPheromoneField().get(
					castedInfluence.getPheromone())[(int) Math.floor(castedInfluence.getLocation().getX())][(int) Math.floor(castedInfluence.getLocation().getY())]+=castedInfluence.getValue();
			}
			
			if(influence.getCategory().equals(ChangeAcceleration.CATEGORY)) {
				ChangeAcceleration castedInfluence = (ChangeAcceleration) influence;
				castedInfluence.getTarget().setAcceleration(
					castedInfluence.getTarget().getAcceleration()
					+ castedInfluence.getDa()
				);
			}
			if(influence.getCategory().equals(ChangeDirection.CATEGORY)) {
				ChangeDirection castedInfluence = (ChangeDirection) influence;
				castedInfluence.getTarget().setDirection(
					castedInfluence.getTarget().getDirection()
					+ castedInfluence.getDd()
				);
			}
			
			if(influence.getCategory().equals(ChangePosition.CATEGORY)) {
				ChangePosition castedInfluence = (ChangePosition) influence;
				castedInfluence.getTarget().getLocation().setLocation(
					castedInfluence.getTarget().getLocation().getX()
					+ castedInfluence.getDx(),
					castedInfluence.getTarget().getLocation().getY()
					+ castedInfluence.getDy() 
				);
			}
			if(influence.getCategory().equals(ChangeSpeed.CATEGORY)) {
				ChangeSpeed castedInfluence = (ChangeSpeed) influence;
				castedInfluence.getTarget().setSpeed(
					castedInfluence.getTarget().getSpeed()
					+ castedInfluence.getDs()
				);
			}
		}
		//Manage natural influences
		for(IInfluence influence : regularInfluencesOftransitoryStateDynamics) {
			if(influence.getCategory().equals(AgentPositionUpdate.CATEGORY)) {
				agentPositionUpdateReaction(
					transitoryTimeMin,
					transitoryTimeMax,
					consistentState.getPublicLocalStateOfAgents(),
					castedEnvironment,
					dt,
					remainingInfluences
				);
			}
			if(influence.getCategory().equals(PheromoneFieldUpdate.CATEGORY)) {
				pheromoneFieldReaction(castedEnvironment, dt); 
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeSystemReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Collection<IInfluence> systemInfluencesToManage,
			boolean happensBeforeRegularReaction,
			InfluencesMap newInfluencesToProcess) {
		LogoEnvPLS castedEnvironment = (LogoEnvPLS) consistentState.getPublicLocalStateOfEnvironment();
		// When an agent is added to the logo level, it is associated to the patch where it is located.
		for(IInfluence influence : systemInfluencesToManage) {
			if(influence.getCategory().equals(SystemInfluenceAddAgentToLevel.CATEGORY)) {
				SystemInfluenceAddAgentToLevel castedInfluence = (SystemInfluenceAddAgentToLevel) influence;
				TurtlePLSInLogo castedPLS = (TurtlePLSInLogo) castedInfluence.getPublicLocalState();
				castedEnvironment.getTurtlesInPatches()[(int) Math.floor(castedPLS.getLocation().getX())][(int) Math.floor(castedPLS.getLocation().getY())].add(castedPLS);
			}
		}
	}
	
	/**
	 * Makes the reaction to the update of pheromone fields
	 * 
	 * @param environment the Logo Environment.
	 * @param dt the duration of the the simulation step.
	 */
	private void pheromoneFieldReaction(LogoEnvPLS environment, int dt) {
		double[][] tmpField;
		//diffusion
		for(Map.Entry<Pheromone, double[][]> field : environment.getPheromoneField().entrySet()) {
			tmpField = field.getValue().clone();
			for(int x = 0; x < field.getValue().length; x++) {
				for(int y = 0; y < field.getValue()[x].length; y++) {
					for(Position p : environment.getNeighbors(x, y, 1)) {
						field.getValue()[p.x][p.y] += field.getKey().getDiffusionCoef()*tmpField[x][y];
					}
				}
			}
		}
		//evaporation
		for(Map.Entry<Pheromone, double[][]> field : environment.getPheromoneField().entrySet()) {
			for(int x = 0; x < field.getValue().length; x++) {
				for(int y = 0; y < field.getValue()[x].length; y++) {
					field.getValue()[x][y] -= field.getKey().getEvaporationCoef()*field.getValue()[x][y]*dt;
				}
			}
		}
		
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
	private void agentPositionUpdateReaction(
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			Set<ILocalStateOfAgent> agents,
			LogoEnvPLS environment,
			int dt,
			InfluencesMap remainingInfluences
	) {
		//Update turtle locations
		for (ILocalStateOfAgent agentPLS : agents) {
			TurtlePLSInLogo castedTurtlePLS = (TurtlePLSInLogo) agentPLS;
			//Computes speed
			castedTurtlePLS.setSpeed(
				castedTurtlePLS.getSpeed() + castedTurtlePLS.getAcceleration()
			);
			double newX = castedTurtlePLS.getLocation().getX()
				+ castedTurtlePLS.getSpeed()*dt*Math.cos(Math.PI/2+castedTurtlePLS.getDirection());
			double newY = castedTurtlePLS.getLocation().getY()
				+ castedTurtlePLS.getSpeed()*dt*Math.cos(castedTurtlePLS.getDirection());
			if(environment.isxAxisTorus()) {
				newX = ( ( newX % environment.getWidth()) + environment.getWidth() ) % environment.getWidth();
			}
			if(environment.isyAxisTorus()) {
				 
				newY = ( ( newY % environment.getHeight()) + environment.getHeight() ) % environment.getHeight();
			}
			
			//If the turtle is out of bounds the it is removed from the simulation.
			if(newX < 0
				|| newX >  environment.getWidth()
				|| newY < 0
				|| newY >  environment.getHeight()
			) {
				SystemInfluenceRemoveAgent rmInfluence = new SystemInfluenceRemoveAgent(
					LogoSimulationLevelList.LOGO,
					transitoryTimeMax,
					transitoryTimeMin, 
					castedTurtlePLS
				);
				remainingInfluences.add( rmInfluence );
			} else { //Else the turtle's new location is set.
				//Update turtle patch
				if(
					newX != Math.floor(castedTurtlePLS.getLocation().getX()) ||
					newY != Math.floor(castedTurtlePLS.getLocation().getY())
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

}
