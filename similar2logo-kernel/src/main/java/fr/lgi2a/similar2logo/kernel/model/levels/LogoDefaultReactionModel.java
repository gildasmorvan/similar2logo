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
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import fr.lgi2a.similar2logo.kernel.model.environment.Position;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeAcceleration;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangePosition;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.DropMark;
import fr.lgi2a.similar2logo.kernel.model.influences.EmitPheromone;
import fr.lgi2a.similar2logo.kernel.model.influences.PheromoneFieldUpdate;
import fr.lgi2a.similar2logo.kernel.model.influences.RemoveMark;

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
		for(IInfluence influence : regularInfluencesOftransitoryStateDynamics) {
			if(influence.getCategory().equals(PheromoneFieldUpdate.CATEGORY)) {
				pheromoneFieldReaction(castedEnvironment,dt);
			}
		}
		
		//Manage agent influences
		for(IInfluence influence : regularInfluencesOftransitoryStateDynamics) {
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
			if(influence.getCategory().equals(DropMark.CATEGORY)) {
				DropMark castedInfluence = (DropMark) influence;
				castedEnvironment.getMarks().add(castedInfluence.getMark());
			}
			if(influence.getCategory().equals(RemoveMark.CATEGORY)) {
				RemoveMark castedInfluence = (RemoveMark) influence;	
				castedEnvironment.getMarks().remove(castedInfluence.getMark());
			}
			
			if(influence.getCategory().equals(EmitPheromone.CATEGORY)) {
				EmitPheromone castedInfluence = (EmitPheromone) influence;	
				castedEnvironment.getPheromoneField().get(
					castedInfluence.getPheromone())[(int) Math.floor(castedInfluence.getLocation().getX())][(int) Math.floor(castedInfluence.getLocation().getY())]+=castedInfluence.getValue();
			}
			
		}
		
		//Update turtle locations
		for (ILocalStateOfAgent agentPLS : consistentState.getPublicLocalStateOfAgents()) {
			TurtlePLSInLogo castedTurtlePLS = (TurtlePLSInLogo) agentPLS;
			//Computes speed
			castedTurtlePLS.setSpeed(
				castedTurtlePLS.getSpeed() + castedTurtlePLS.getAcceleration()
			);
			double newX = castedTurtlePLS.getLocation().getX()
				+ castedTurtlePLS.getSpeed()*dt*Math.cos(Math.PI/2+castedTurtlePLS.getDirection());
			double newY = castedTurtlePLS.getLocation().getY()
				+ castedTurtlePLS.getSpeed()*dt*Math.cos(castedTurtlePLS.getDirection());
			if(castedEnvironment.isxAxisTorus()) {
				newX %= castedEnvironment.getWidth();
			}
			if(castedEnvironment.isyAxisTorus()) {
				newY %= castedEnvironment.getWidth();
			}
				
			//If the turtle is out of bounds the it is removed from the simulation.
			if(castedTurtlePLS.getLocation().getX() < 0
				|| castedTurtlePLS.getLocation().getX() >  castedEnvironment.getWidth()
				|| castedTurtlePLS.getLocation().getY() < 0
				|| castedTurtlePLS.getLocation().getY() >  castedEnvironment.getHeight()
			) {
				SystemInfluenceRemoveAgent rmInfluence = new SystemInfluenceRemoveAgent(
					LogoSimulationLevelList.LOGO,
					transitoryTimeMax,
					transitoryTimeMin, 
					castedTurtlePLS
				);
				remainingInfluences.add( rmInfluence );
			}
			//Else the turtle's new location is set.
			else {
				castedTurtlePLS.getLocation().setLocation(
					newX,
					newY
				);
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
			InfluencesMap newInfluencesToProcess) {}
	
	/**
	 * make the reaction to the update of pheromone fields
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
					for(Position p : environment.getNeighbors(x, y)) {
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

}
