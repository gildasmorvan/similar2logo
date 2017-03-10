package fr.lgi2a.similar2logo.examples.train.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.ChangedCharSetException;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

public class TrainDecisionModel extends AbstractAgtDecisionModel {
	
	/**
	 * Parameters from the simulations
	 */
	private TrainSimulationParameters parameters;
	
	/**
	 * Station of destination
	 */
	private Point2D destination;
	
	/**
	 * Last position 
	 */
	private Point2D lastPosition;
	
	/**
	 * Speed of the train
	 */
	private int speed;
	
	/**
	 * Speed maximum of the train
	 */
	private int maxSpeed;
	
	public TrainDecisionModel (TrainSimulationParameters param, Point2D des, Point2D lp) {
		super(LogoSimulationLevelList.LOGO);
		parameters = param;
		this.destination = des;
		this.lastPosition = lp;
		Random r = new Random ();
		this.maxSpeed = 1;//1 + r.nextInt(3);
		this.speed = 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences){
		Random r = new Random();
		TurtlePLSInLogo castedPublicLocalState = (TurtlePLSInLogo) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		Point2D position = castedPublicLocalState.getLocation();
		System.out.println(position);
		System.out.println(castedPublicLocalState.getSpeed());
		if (position.equals((Point2D) destination)) {
			destination = parameters.nextStations.get(position).get(r.nextInt(parameters.nextStations.get(position).size()));
			producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, castedPublicLocalState.getDirection() + Math.PI, castedPublicLocalState));
			producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
		} else {
			if (castedPerceivedData.getMarks().size() == 0) {
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, castedPublicLocalState.getDirection() + Math.PI, castedPublicLocalState));
				producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
			} else {
				double bestDirection = Math.PI, bestDistance = Double.MAX_VALUE;
				for(LocalPerceivedData<Mark> perceivedMarks : castedPerceivedData.getMarks()) {
					double dis = perceivedMarks.getContent().getLocation().distance(destination);
					if (((perceivedMarks.getContent().getCategory().equals("Station")) || (perceivedMarks.getContent().getCategory().equals("Rail"))) 
							&& (perceivedMarks.getDistanceTo() > 0) && (bestDistance > dis)) {
						//We keep the last position for avoiding the train goes back where it comes from
						//if (inStation(castedPerceivedData) || !perceivedMarks.getContent().getLocation().equals(lastPosition)) {
							bestDirection = perceivedMarks.getDirectionTo();
							bestDistance = dis;
						//}
					}
				}
				lastPosition = castedPublicLocalState.getLocation();
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, -castedPublicLocalState.getDirection() + bestDirection, castedPublicLocalState));
				producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound,- castedPublicLocalState.getSpeed() + distanceToDo(bestDirection), castedPublicLocalState));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean inStation (TurtlePerceivedData data) {
		for(LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if ((perceivedMarks.getDistanceTo() == 0) && (perceivedMarks.getContent().getCategory().equals("Station"))) {
				return true;
			}
		}
		return false;
	}
	
	private void changeSpeed (TurtlePLSInLogo pls) {
		Point2D currentPosition = pls.getLocation();
		if (this.speed == 0) {
			speed++;
		} else if ((currentPosition.distance(destination) <= speed*1.5) && speed != 1) {
			speed /= 2;
		} else if (speed < this.maxSpeed) {
			speed++;
		} else if (currentPosition.equals(destination)) {
			speed = 0;
		}
	}
	
	private double distanceToDo (double radius) {
		if ((radius % (Math.PI/2)) == 0) return 1;
		else return Math.sqrt(2);
	}
	
	private double getDirection (Point2D moi, Point2D obj) {
		double moiX = moi.getX();
		double moiY = moi.getY();
		double objX = obj.getX();
		double objY = obj.getY();
		if ((objX - moiX) < 0) {
			if ((objY - moiY) <0) return LogoEnvPLS.SOUTH_WEST;
			else if ((objY - moiY) > 0) return LogoEnvPLS.NORTH_WEST;
			else return LogoEnvPLS.WEST;
		} else if ((objX - moiX) > 0) {
			if ((objY - moiY) <0) return LogoEnvPLS.SOUTH_EAST;
			else if ((objY - moiY) > 0) return LogoEnvPLS.NORTH_EAST;
			else return LogoEnvPLS.EAST;			
		} else {
			if ((objY - moiY) <0) return LogoEnvPLS.SOUTH;
			else return LogoEnvPLS.NORTH;
		}
		
	}
	
}
