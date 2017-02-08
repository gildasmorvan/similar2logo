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
import java.util.HashSet;
import java.util.List;
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
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgentFromLevel;
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
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;

/**
 * 
 * The default reaction function of a Logo level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
@SuppressWarnings("rawtypes")
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
		Set<IInfluence> naturalInfluences = new HashSet<IInfluence>();
		
		//Handle agent influences
		for(IInfluence influence : regularInfluencesOftransitoryStateDynamics) {
			switch(influence.getCategory()) {
				case RemoveMarks.CATEGORY:
					reactToRemoveMarksInfluence(castedEnvironment, (RemoveMarks) influence);
					break;
				case RemoveMark.CATEGORY:
					reactToRemoveMarkInfluence(castedEnvironment,(RemoveMark) influence);
					break;
				case DropMark.CATEGORY:
					reactToDropMarkInfluence(castedEnvironment,(DropMark) influence);
					break;
				case EmitPheromone.CATEGORY:
					reactToEmitPheromoneInfluence(castedEnvironment,(EmitPheromone) influence);
					break;
				case ChangeAcceleration.CATEGORY:
					reactToChangeAccelerationInfluence((ChangeAcceleration) influence);
					break;
				case ChangeDirection.CATEGORY:
					reactToChangeDirectionInfluence((ChangeDirection) influence);
					break;
				case ChangePosition.CATEGORY:
					reactToChangePositionInfluence(castedEnvironment,(ChangePosition) influence);
					break;
				case ChangeSpeed.CATEGORY:
					reactToChangeSpeedInfluence((ChangeSpeed) influence);
					break;
				case Stop.CATEGORY:
					reactToStopInfluence((Stop) influence);
					break;
				default:
					naturalInfluences.add(influence);
			}
		}
		
		//Handle natural influences
		for(IInfluence influence : naturalInfluences) {
			switch(influence.getCategory()) {
				case AgentPositionUpdate.CATEGORY:
					reactToAgentPositionUpdate(
					   transitoryTimeMin,
					   transitoryTimeMax,
					   consistentState.getPublicLocalStateOfAgents(),
					   castedEnvironment,
					   remainingInfluences
					);
					break;
				case PheromoneFieldUpdate.CATEGORY:
					reactToPheromoneFieldUpdate(
					   transitoryTimeMin,
					   transitoryTimeMax,
					   castedEnvironment
					); 
					break;
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
		
		for(IInfluence influence : systemInfluencesToManage) {
			// When an agent is added to the logo level, it is associated to the patch where it is located.
			if(influence.getCategory().equals(SystemInfluenceAddAgentToLevel.CATEGORY)) {
				SystemInfluenceAddAgentToLevel castedInfluence = (SystemInfluenceAddAgentToLevel) influence;
				TurtlePLSInLogo castedPLS = (TurtlePLSInLogo) castedInfluence.getPublicLocalState();
				castedEnvironment
					.getTurtlesInPatches()[(int) Math.floor(castedPLS.getLocation().getX())]
										  [(int) Math.floor(castedPLS.getLocation().getY())].add(castedPLS);
			}
			// When an agent is removed from the simulation, it is dissociated from the patch where it is located.
			else if(influence.getCategory().equals(SystemInfluenceRemoveAgentFromLevel.CATEGORY)) {
				SystemInfluenceRemoveAgentFromLevel castedInfluence = (SystemInfluenceRemoveAgentFromLevel) influence;
					
					
				TurtlePLSInLogo castedPLS = (TurtlePLSInLogo) castedInfluence.getAgentLocalState();
				castedEnvironment.getTurtlesInPatches()[(int) Math.floor(castedPLS.getLocation().getX())][(int) Math.floor(castedPLS.getLocation().getY())].remove(castedPLS);
			}
			
		}
	}
	
	/**
	 * The reaction method to RemoveMarks influence
	 * 
	 * @param environment The environment of the simulation.
	 * @param influence  The RemoveMarks influence.
	 */
	private static void reactToRemoveMarksInfluence(
			LogoEnvPLS environment,
			RemoveMarks influence
	) {
		for(Mark mark : influence.getMarks()) {
			environment.getMarks()[(int) Math.floor(mark.getLocation().getX())][(int) Math.floor(mark.getLocation().getY())].remove(mark);
		}
	}
	
	/**
	 * The reaction method to RemoveMark influence
	 * 
	 * @param environment The environment of the simulation.
	 * @param influence  The RemoveMark influence.
	 */
	private static void reactToRemoveMarkInfluence(
			LogoEnvPLS environment,
			RemoveMark influence
	) {
		environment.getMarks()[(int) Math.floor(influence.getMark().getLocation().getX())][(int) Math.floor(influence.getMark().getLocation().getY())].remove(influence.getMark());
		
	}
	
	/**
	 * The reaction method to DropMark influence
	 * 
	 * @param environment The environment of the simulation.
	 * @param influence  The DropMark influence.
	 */
	private static void reactToDropMarkInfluence(
			LogoEnvPLS environment,
			DropMark influence
	) {
		environment.getMarks()[(int) Math.floor(influence.getMark().getLocation().getX())][(int) Math.floor(influence.getMark().getLocation().getY())].add(influence.getMark());
	}
	
	/**
	 * The reaction method to EmitPheromone influence
	 * 
	 * @param environment The environment of the simulation.
	 * @param influence  The EmitPheromone influence.
	 */
	private static void reactToEmitPheromoneInfluence(
			LogoEnvPLS environment,
			EmitPheromone influence
	) {
		Pheromone targetPheromone = null;
		for(Pheromone pheromone : environment.getPheromoneField().keySet()) {
			if(pheromone.getIdentifier().equals(influence.getPheromoneIdentifier())){
				targetPheromone = pheromone;
			}
		}
		if(targetPheromone != null) {
			environment.getPheromoneField().get(targetPheromone)[(int) Math.floor(influence.getLocation().getX())][(int) Math.floor(influence.getLocation().getY())]+=influence.getValue();
		}
	}
	
	/**
	 * 
	 * The reaction method to ChangeAcceleration influence
	 * 
	 * @param influence The ChangeAcceleration influence.
	 */
	private static void reactToChangeAccelerationInfluence(ChangeAcceleration influence) {
		influence.getTarget().setAcceleration(influence.getTarget().getAcceleration()+ influence.getDa());
	}
	
	/**
	 * 
	 * The reaction method to ChangeDirection influence
	 * 
	 * @param influence The ChangeDirection influence.
	 */
	private static void reactToChangeDirectionInfluence(ChangeDirection influence) {
		influence.getTarget().setDirection(influence.getTarget().getDirection()+ influence.getDd());
	}
	
	/**
	 * The reaction method to ChangePosition influence
	 * 
	 * @param environment The environment of the simulation.
	 * @param influence  The ChangePosition influence.
	 */
	private static void reactToChangePositionInfluence(
			LogoEnvPLS environment,
			ChangePosition influence
	) {
		double newX = influence.getTarget().getLocation().getX() + influence.getDx();
		double newY = influence.getTarget().getLocation().getY() + influence.getDy();
		
		if(environment.isxAxisTorus()) {
			newX = ( ( newX % environment.getWidth()) + environment.getWidth() ) % environment.getWidth();
		}
		if(environment.isyAxisTorus()) {
			 
			newY = ( ( newY % environment.getHeight()) + environment.getHeight() ) % environment.getHeight();
		}
		
		
		if(
				newX != Math.floor(influence.getTarget().getLocation().getX()) ||
				newY != Math.floor(influence.getTarget().getLocation().getY())
			) {
			environment.getTurtlesInPatches()[(int) Math.floor(influence.getTarget().getLocation().getX())][(int) Math.floor(influence.getTarget().getLocation().getY())].remove(influence.getTarget());
			environment.getTurtlesInPatches()[(int) Math.floor(newX)][(int) Math.floor(newY)].add(influence.getTarget());
			}
		influence.getTarget().getLocation().setLocation(newX,newY);	
	}
	
	/**
	 * 
	 * The reaction method to ChangeSpeed influence
	 * 
	 * @param influence The ChangeSpeed influence.
	 */
	private static void reactToChangeSpeedInfluence(ChangeSpeed influence) {
		influence.getTarget().setSpeed(
		   influence.getTarget().getSpeed() + influence.getDs()
		);
	}
	
	/**
	 * 
	 * The reaction method to Stop influence
	 * 
	 * @param influence The Stop influence.
	 */
	private static void reactToStopInfluence(Stop influence) {
		influence.getTarget().setSpeed(0);
		influence.getTarget().setAcceleration(0);
	}
	
	/**
	 * Makes the reaction to the update of pheromone fields
	 * 
	 * @param transitoryTimeMin The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param transitoryTimeMax The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param environment the Logo Environment.
	 * 
	 */
	private static void reactToPheromoneFieldUpdate(
	   SimulationTimeStamp transitoryTimeMin,
	   SimulationTimeStamp transitoryTimeMax, 
	   LogoEnvPLS environment
	) {
		double[][] tmpField;
		int dt = transitoryTimeMax.compareTo(transitoryTimeMin);
		
		//diffusion
		for(Map.Entry<Pheromone, double[][]> field : environment.getPheromoneField().entrySet()) {
			tmpField = field.getValue().clone();
			for(int x = 0; x < field.getValue().length; x++) {
				for(int y = 0; y < field.getValue()[x].length; y++) {
					List<Position> neighbors = environment.getNeighbors(x, y, 1);
					for(Position p : neighbors) {
						if(p.x != x || p.y != y) {
							field.getValue()[p.x][p.y] = field.getValue()[p.x][p.y] + field.getKey().getDiffusionCoef()*tmpField[x][y]/8;
						}	
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
		//minValue
		for(Map.Entry<Pheromone, double[][]> field : environment.getPheromoneField().entrySet()) {
			for(int x = 0; x < field.getValue().length; x++) {
				for(int y = 0; y < field.getValue()[x].length; y++) {
					if(field.getValue()[x][y] < field.getKey().getMinValue()){
						field.getValue()[x][y] = 0;
					}
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
	private static void reactToAgentPositionUpdate(
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			Set<ILocalStateOfAgent> agents,
			LogoEnvPLS environment,
			InfluencesMap remainingInfluences
	) {
		int dt = transitoryTimeMax.compareTo(transitoryTimeMin);
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
			} else { 
				//Else the turtle's new location is set.
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
