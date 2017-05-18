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
package fr.lgi2a.similar2logo.examples.transport.model.agents;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgentFromLevel;
import fr.lgi2a.similar2logo.examples.transport.model.Station;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Decision model of the tram for the "transport" simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportDecisionModel extends AbstractAgtDecisionModel {
	
	/**
	 * The type of transport
	 */
	private String type;

	/**
	 * The rails at the limit of the world
	 */
	private List<Point2D> limits;
	
	/**
	 * The place where the train goes
	 * The only interest of this variable is to be ensure the train go until the limits of the map.
	 */
	private Point2D destination;
	
	/**
	 * A map with the stations and their position.
	 */
	private Map<Point2D,Station> stations;

	public TransportDecisionModel(String type, List<Point2D> limits, List<Station> stations) {
		super(LogoSimulationLevelList.LOGO);
		this.type = type;
		this.limits = limits;
		Random r = new Random();
		destination = limits.get(r.nextInt(limits.size()));
		System.out.println(destination);
		this.stations = new HashMap<>();
		for (Station s : stations) {
			this.stations.put(s.getPlatform(), s);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		TransportPLS castedPublicLocalState = (TransportPLS) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		Point2D position = castedPublicLocalState.getLocation();
		double myDirection = castedPublicLocalState.getDirection();
		double dir = getDirection(position, castedPerceivedData);
		System.out.println(position+"/"+myDirection+"/"+castedPublicLocalState.getSpeed());
		//Initialization (nothing move a t=0)
		if (timeLowerBound.getIdentifier() == 0) {
			if (!inFieldOfVision(position, myDirection, destination)) {
				System.out.println("changement");
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
						-myDirection + turnAround(myDirection), castedPublicLocalState));
			}
		} else {
			//If we are in a station
			if (inStation(position)) {
				//The train is stop, the passengers go down or go up in the train, and the train restarts.
				if (castedPublicLocalState.getSpeed() == 0) {
					//Go down and go up the passengers
					producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
							-myDirection + dir, castedPublicLocalState));
					producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, 
							-castedPublicLocalState.getSpeed() + distanceToDo(dir), castedPublicLocalState));
				} else {
					producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
				}
			//If we are at the edge of the map, the train turns around
			} else if (onEdge(position)) {
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
			} else if (seeMarks(position, castedPerceivedData) && dontFindMark(position, castedPerceivedData)) {
				producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, distanceToDo(myDirection), castedPublicLocalState));
			// If the transport perceives no data
			} else if (dontFindMark(position, castedPerceivedData)) {
				double left, right = 0;
				if (myDirection == Math.PI) {
					left = 3*Math.PI/4;
					right = -3*Math.PI/4;
				} else if (myDirection == -3*Math.PI/4) {
					right = -Math.PI/2;
					left = Math.PI;
				} else {
					left = myDirection - Math.PI/4;
					right = myDirection + Math.PI/4;
				}
				Point2D onLeft = nextPosition(position, left);
				Point2D onRight = nextPosition(position, right);
				if (castedPublicLocalState.getSpeed() == 0) {
					System.out.println("-+-+-");
					if (onLeft.distance(destination) > onRight.distance(destination)) {
						producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
								-myDirection + left, castedPublicLocalState));
					} else {
						producedInfluences.add(new ChangeDirection(timeLowerBound, timeLowerBound, 
								-myDirection + right, castedPublicLocalState));
					}
				} else {
					System.out.println("-----");
					if (onLeft.distance(destination) < onRight.distance(destination)) {
						producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
								-myDirection + left, castedPublicLocalState));
					} else {
						producedInfluences.add(new ChangeDirection(timeLowerBound, timeLowerBound, 
								-myDirection + right, castedPublicLocalState));
					}
					producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
				}
			//If we are in the middle of the way
			} else {
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
						-castedPublicLocalState.getDirection() + dir, castedPublicLocalState));
				producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, 
						-castedPublicLocalState.getSpeed() + distanceToDo(dir), castedPublicLocalState));
			}
		}
	}
	
	/**
	 * Indicates if the train is in station
	 * @param currentPosition the current position of the train
	 * @return true if the train is in a station, else false
	 */
	private boolean inStation (Point2D currentPosition) {
		return stations.keySet().contains(currentPosition);
	}
	
	/**
	 * Indicates if the train is on the edge of the world
	 * @param currentPosition the current position of the train
	 * @return true if the train is at the limit of the world, false else
	 */
	private boolean onEdge (Point2D currentPosition) {
		return limits.contains(currentPosition);
	}
	
	/**
	 * Gives the direction that the train has to take.
	 * @param currentPosition the current position of the train
	 * @param data the date perceived by the train
	 * @return the direction that the train has to take
	 */
	private double getDirection (Point2D currentPosition, TurtlePerceivedData data) {
		double bestDirection = Math.PI;
		double dis = Double.MAX_VALUE;
		for(@SuppressWarnings("rawtypes") LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if (perceivedMarks.getContent().getCategory().equals(type) && (!perceivedMarks.getContent().getLocation().equals(currentPosition))
					&& (perceivedMarks.getContent().getLocation().distance(destination) < dis)) {
				bestDirection = perceivedMarks.getDirectionTo();
				dis = perceivedMarks.getContent().getLocation().distance(destination);
			}
		}
		return bestDirection;
	}

	/**
	 * Gives the distance to do following the direction of the train
	 * @param radius direction of the train
	 * @return the distance to do
	 */
	private double distanceToDo (double radius) {
		if (radius == LogoEnvPLS.NORTH_EAST || radius == LogoEnvPLS.NORTH_WEST || 
				radius == LogoEnvPLS.SOUTH_EAST || radius == LogoEnvPLS.SOUTH_WEST) return Math.sqrt(2);
		else return 1;
	}
	
	/**
	 * Indicates if the transport doesn't fin any mark.
	 * This function helps the transport don't run away the way.
	 * @param position the position of the transport
	 * @param data the data perceived by the transport
	 * @return true if the transport doen't find any mark, false else
	 */
	private boolean dontFindMark (Point2D position, TurtlePerceivedData data) {
		for(@SuppressWarnings("rawtypes") LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if (perceivedMarks.getContent().getCategory().equals(type) && (perceivedMarks.getDistanceTo() > 0)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Indicates if the transport sees at least a mark.
	 * @param position the position of the transport
	 * @param data the data perceived by the transport
	 * @return true if the transport sees a mark, else false
	 */
	private boolean seeMarks (Point2D position, TurtlePerceivedData data) {
		int cpt = 0;
		for (@SuppressWarnings("rawtypes") LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			System.out.println("--->"+perceivedMarks.getContent().getCategory()+"/"+perceivedMarks.getContent().getLocation());
			if (perceivedMarks.getContent().getCategory().equals(type) && (perceivedMarks.getDistanceTo() > 0)) {
				cpt++;
			}
		}
		return (cpt > 0);
	}
	
	/**
	 * Gives the next position of a transport following its position and its direction
	 * @param position the current position of the transport
	 * @param direction the direction of the transport
	 * @return the next position of the transport
	 */
	private Point2D nextPosition (Point2D position, double direction) {
		int x,y;
		if (direction < 0) x = 1;
		else if ((direction == LogoEnvPLS.NORTH) || (direction == LogoEnvPLS.SOUTH)) x = 0;
		else x = -1;
		if ((direction >= LogoEnvPLS.NORTH_EAST) && (direction <= LogoEnvPLS.NORTH_WEST)) y = 1;
		else if ((direction == LogoEnvPLS.WEST) || (direction == LogoEnvPLS.EAST)) y = 0;
		else y = -1;
		Point2D res = new Point2D.Double(position.getX()+x,position.getY()+y);
		return res;
	}
	
	/**
	 * Indicates if the destination is in the field of vision of the transport (very large zone).
	 * @param position the position of the transport
	 * @param direction the direction of the transport
	 * @param objective the destination of the transport
	 * @return true if the transport can see the destination, false else
	 */
	private boolean inFieldOfVision (Point2D position, double direction, Point2D objective) {
		if (direction == LogoEnvPLS.SOUTH) return (objective.getY() <= position.getY());
		else if (direction == LogoEnvPLS.NORTH) return (objective.getY() >= position.getY());
		else if (direction == LogoEnvPLS.EAST) return (objective.getX() >= position.getX());
		else if (direction == LogoEnvPLS.WEST) return (objective.getX() <= position.getX());
		else {System.out.println("caca");
			return false;}
	}
	
	/**
	 * Makes the transport turns around.
	 * Uses at the first round of the simulation
	 * @param currentDirection the current direction of the transport
	 * @return the direction opposite to the current direction
	 */
	private double turnAround (double currentDirection) {
		if (currentDirection == LogoEnvPLS.SOUTH) return LogoEnvPLS.NORTH;
		else if (currentDirection == LogoEnvPLS.SOUTH) return LogoEnvPLS.SOUTH;
		else if (currentDirection == LogoEnvPLS.EAST) return LogoEnvPLS.WEST;
		else return LogoEnvPLS.EAST;
	}
}