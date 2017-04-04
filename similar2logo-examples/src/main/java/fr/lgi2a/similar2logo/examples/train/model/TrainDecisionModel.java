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
package fr.lgi2a.similar2logo.examples.train.model;

import java.awt.geom.Point2D;
import java.util.Random;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * The decision model of the trains.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
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
	 * Last station
	 */
	//private Point2D origine;
	
	/**
	 * Speed of the train
	 */
	//private int speed;
	
	/**
	 * Speed maximum of the train
	 */
	//private int maxSpeed;
	
	/**
	 * Constructor of decision model
	 * @param param parameters of the simulation
	 * @param des destination of the train
	 * @param lp provenance of the train
	 */
	public TrainDecisionModel (TrainSimulationParameters param, Point2D des/*, Point2D lp*/) {
		super(LogoSimulationLevelList.LOGO);
		parameters = param;
		this.destination = des;
		//this.origine = lp;
		//Random r = new Random ();
		//this.maxSpeed = 1;//1 + r.nextInt(3);
		//this.speed = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences){
		Random r = new Random();
		TurtlePLSInLogo castedPublicLocalState = (TurtlePLSInLogo) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		Point2D position = castedPublicLocalState.getLocation();
		if (position.equals((Point2D) destination)) {
			//origine = destination;
			destination = parameters.nextStations.get(position).get(r.nextInt(parameters.nextStations.get(position).size()));
			producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, - castedPublicLocalState.getDirection() + Math.PI, castedPublicLocalState));
			producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
		} else {
			//System.out.println("Marks perceived : "+castedPerceivedData.getMarks().size());
			if (castedPerceivedData.getMarks().size() == 0) {
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, -castedPublicLocalState.getDirection(), castedPublicLocalState));
				producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
			} else {
				double bestDirection = Math.PI, bestDistance = Double.MAX_VALUE;
				for(LocalPerceivedData<Mark> perceivedMarks : castedPerceivedData.getMarks()) {
					double dis = perceivedMarks.getContent().getLocation().distance(destination);
					if (((perceivedMarks.getContent().getCategory().equals("Station")) || (perceivedMarks.getContent().getCategory().equals("Rail"))) 
							&& (perceivedMarks.getDistanceTo() > 0) && (bestDistance > dis)) {
						bestDirection = perceivedMarks.getDirectionTo();
						bestDistance = dis;
					}
				}
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, -castedPublicLocalState.getDirection() + bestDirection, castedPublicLocalState));
				producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound,- castedPublicLocalState.getSpeed() + distanceToDo(bestDirection), castedPublicLocalState));
			}
		}
	}

	/*private void changeSpeed (TurtlePLSInLogo pls) {
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
	}*/
	
	/**
	 * Give the distance to do following the direction of the train
	 * @param radius direction of the train
	 * @return the distance to do
	 */
	private double distanceToDo (double radius) {
		if ((radius % (Math.PI/2)) == 0) return 1;
		else return Math.sqrt(2);
	}
	
	/*private double getDirection (Point2D moi, Point2D obj) {
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
		
	}*/
	
}
