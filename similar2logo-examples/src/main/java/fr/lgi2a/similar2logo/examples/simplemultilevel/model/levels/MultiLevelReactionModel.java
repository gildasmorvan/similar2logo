package fr.lgi2a.similar2logo.examples.simplemultilevel.model.levels;

import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar2logo.examples.simplemultilevel.model.agents.MultiLevelTurtleFactory;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;

public class MultiLevelReactionModel extends LogoDefaultReactionModel {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
				//Computes speed
				castedTurtlePLS.setSpeed(
					castedTurtlePLS.getSpeed() + castedTurtlePLS.getAcceleration()
				);
				double newX = castedTurtlePLS.getLocation().getX() + castedTurtlePLS.getDX()*dt;
				double newY = castedTurtlePLS.getLocation().getY() + castedTurtlePLS.getDY()*dt;
				double newXT = newX;
				double newYT = newY;
				if(environment.isxAxisTorus()) {
					newXT = ( ( newX % environment.getWidth()) + environment.getWidth() ) % environment.getWidth();
				}
				if(environment.isyAxisTorus()) {
					 
					newYT = ( ( newY % environment.getHeight()) + environment.getHeight() ) % environment.getHeight();
				}
				
				//If the turtle is out of bounds it is added to another level.
				if(newX < 0
					|| newX >=  environment.getWidth()
					|| newY < 0
					|| newY >=  environment.getHeight()
				) {
					SystemInfluenceAddAgent addInfluence;
					SystemInfluenceRemoveAgent rmInfluence;
					
					if(agentPLS.getLevel().equals(MultiLevelSimulationLevelList.LOGO)) {
						addInfluence = new SystemInfluenceAddAgent(
							MultiLevelSimulationLevelList.LOGO2,
							transitoryTimeMax,
							transitoryTimeMax,
							MultiLevelTurtleFactory.generate(
								castedTurtlePLS,
								MultiLevelSimulationLevelList.LOGO2,
								newXT,
								newYT
							)
						);
						rmInfluence = new SystemInfluenceRemoveAgent(
							MultiLevelSimulationLevelList.LOGO,
							transitoryTimeMax,
							transitoryTimeMin, 
							castedTurtlePLS
						);
					} else {
						addInfluence = new SystemInfluenceAddAgent(
							MultiLevelSimulationLevelList.LOGO,
							transitoryTimeMax,
							transitoryTimeMax,
							MultiLevelTurtleFactory.generate(
								castedTurtlePLS,
								MultiLevelSimulationLevelList.LOGO,
								newXT,
								newYT
							)
						);
						rmInfluence = new SystemInfluenceRemoveAgent(
							MultiLevelSimulationLevelList.LOGO2,
							transitoryTimeMax,
							transitoryTimeMin, 
							castedTurtlePLS
						);
					}

					remainingInfluences.add( addInfluence );
					remainingInfluences.add( rmInfluence );
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
}
