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
package fr.lgi2a.similar2logo.examples.transport.model.agents.rail;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgentFromLevel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.person.PersonPLS;
import fr.lgi2a.similar2logo.examples.transport.model.places.Station;
import fr.lgi2a.similar2logo.examples.transport.model.places.World;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.kernel.tools.MathUtil;
import fr.lgi2a.similar2logo.lib.tools.random.PRNG;

/**
 * Decision model of the tram for the "transport" simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportDecisionModel extends AbstractTransportAgentDecisionModel {
	
	/**
	 * The type of transport
	 */
	private String type;
	
	/**
	 * A map with the stations and their position.
	 */
	private Map<Point2D,Station> stations;
	
	/**
	 * The last directions that had the transport.
	 */
	private List<Double> lastDirections;
	
	public TransportDecisionModel(Point2D des, World world, String type, List<Point2D> limits, double speedFrequencyTram) {
		super(des, world);
		this.type = type;
		destination = limits.get(PRNG.get().randomInt(limits.size()));
		this.stations = new HashMap<>();
		for (Station s : world.getStations()) {
			this.stations.put(s.getPlatform(), s);
		}
		lastDirections = new ArrayList<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		TransportPLS castedPublicLocalState = (TransportPLS) publicLocalState;
		if (MathUtil.areEqual((timeLowerBound.getIdentifier()*10) % (castedPublicLocalState.speedFrequence*10), 0)) {
			TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
			Point2D position = castedPublicLocalState.getLocation();
			double myDirection = castedPublicLocalState.getDirection();
			double dir = getDirection(position, castedPerceivedData);
			//Initialization (nothing move a t=0)
			if (timeLowerBound.getIdentifier() == 0) {
				if (!inFieldOfVision(position, myDirection, destination)) {
					producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
							-myDirection + turnAround(myDirection), castedPublicLocalState));
				}
			} else {
				if (lastDirections.size() == 4) {
					lastDirections.remove(0);
				}
				lastDirections.add(myDirection);
				//If we are in a station
				if (inStation(position)) {
					//The train is stop, the passengers go down or go up in the train, and the train restarts.
					if (MathUtil.areEqual(castedPublicLocalState.getSpeed(), 0)) {
						//Go down and go up the passengers
						List<ExtendedAgent> toRemove = new ArrayList<>();
						for (ExtendedAgent ea : castedPublicLocalState.getPassengers()) {
							PersonPLS p = (PersonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
							//for (int i =0; i < p.getWay().size(); i++) {
								//TODO Empty loop
							//}
							if (p.getWay().contains(stations.get(position).getAccess())) {
								toRemove.add(ea);
								while (!p.getWay().get(0).equals(stations.get(position).getAccess())) {
									p.getWay().remove(0);
								}
								p.getWay().remove(0);
							}
						}
						for (int i=0; i < toRemove.size(); i++) {
							castedPublicLocalState.removePassenger(toRemove.get(i));
							stations.get(position).addPeopleWantingToGoOut(toRemove.get(i));
						}
						List<ExtendedAgent> wantToGoUp = stations.get(position).personsTakingTheTransport(position, destination);
						while (!castedPublicLocalState.isFull() && !wantToGoUp.isEmpty()) {
							ExtendedAgent ea = wantToGoUp.remove(0);
							castedPublicLocalState.addPassenger(ea);
							stations.get(position).removeWaitingPeopleForTakingTransport(ea);
						}
						producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
								-myDirection + dir, castedPublicLocalState));
						producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, 
								-castedPublicLocalState.getSpeed() + distanceToDo(dir), castedPublicLocalState));
					} else {
						producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
					}
				//If we are at the edge of the map, the train turns around
				} else if (willGoOut(position, myDirection)) {
					producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
					for (int i = 0; i < castedPublicLocalState.getCurrentSize()-1; i++) {
						producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, 
								castedPublicLocalState.getWagon(i)));
					}
					//We remove the persons in the transport
					for (ExtendedAgent ea :castedPublicLocalState.getPassengers()) {
						PersonPLS p = (PersonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						producedInfluences.add(new SystemInfluenceRemoveAgent(LogoSimulationLevelList.LOGO, timeLowerBound, timeUpperBound, p));
					}
				} else if (seeMarks(position, castedPerceivedData) && dontFindMark(position, castedPerceivedData)) {
					producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, distanceToDo(myDirection), castedPublicLocalState));
				// If the transport perceives no data
				} else if (dontFindMark(position, castedPerceivedData)) {
					lastDirections.remove(lastDirections.size()-1);
					double left, right = 0;
					if (MathUtil.areEqual(myDirection, Math.PI)) {
						left = 3*Math.PI/4;
						right = -3*Math.PI/4;
					} else if (MathUtil.areEqual(myDirection, -3*Math.PI/4)) {
						right = -Math.PI/2;
						left = Math.PI;
					} else {
						left = myDirection - Math.PI/4;
						right = myDirection + Math.PI/4;
					}
					Point2D onLeft = nextPosition(position, left);
					Point2D onRight = nextPosition(position, right);
					if (MathUtil.areEqual(castedPublicLocalState.getSpeed(), 0)) {
						if (onLeft.distance(destination) > onRight.distance(destination)) {
							producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
									-myDirection + getAlternativeDirection(), castedPublicLocalState));
						} else {
							producedInfluences.add(new ChangeDirection(timeLowerBound, timeLowerBound, 
									-myDirection + getAlternativeDirection(), castedPublicLocalState));
						}
					} else {
						if (onLeft.distance(destination) < onRight.distance(destination)) {
							producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
									-myDirection + right, castedPublicLocalState));
						} else {
							producedInfluences.add(new ChangeDirection(timeLowerBound, timeLowerBound, 
									-myDirection + left, castedPublicLocalState));
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
		} else {
			producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
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
					&& (perceivedMarks.getContent().getLocation().distance(destination) <= dis)) {
				bestDirection = perceivedMarks.getDirectionTo();
				dis = perceivedMarks.getContent().getLocation().distance(destination);
			}
		}
		return bestDirection;
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
			if (perceivedMarks.getContent().getCategory().equals(type) && (perceivedMarks.getDistanceTo() > 0)) {
				cpt++;
			}
		}
		return (cpt > 0);
	}
	
	/**
	 * Indicates if the destination is in the field of vision of the transport (very large zone).
	 * @param position the position of the transport
	 * @param direction the direction of the transport
	 * @param objective the destination of the transport
	 * @return true if the transport can see the destination, false else
	 */
	private static boolean inFieldOfVision (Point2D position, double direction, Point2D objective) {
		if (MathUtil.areEqual(direction, LogoEnvPLS.SOUTH)) {
			return objective.getY() <= position.getY();
		} else if (MathUtil.areEqual(direction, LogoEnvPLS.NORTH)) {
			return objective.getY() >= position.getY();
		} else if (MathUtil.areEqual(direction, LogoEnvPLS.EAST)) {
			return objective.getX() >= position.getX();
		} else if (MathUtil.areEqual(direction, LogoEnvPLS.WEST)) {
			return objective.getX() <= position.getX();
		} else {
			return false;
		}
	}
	
	/**
	 * Makes the transport turns around.
	 * Uses at the first round of the simulation
	 * @param currentDirection the current direction of the transport
	 * @return the direction opposite to the current direction
	 */
	private static double turnAround (double currentDirection) {
		if (MathUtil.areEqual(currentDirection, LogoEnvPLS.SOUTH)) {
			return LogoEnvPLS.NORTH;
		} else if (MathUtil.areEqual(currentDirection, LogoEnvPLS.NORTH)) {
			return LogoEnvPLS.SOUTH;
		} else if (MathUtil.areEqual(currentDirection, LogoEnvPLS.EAST)) {
			return LogoEnvPLS.WEST;
		} else {
			return LogoEnvPLS.EAST;
		}
	}
	
	private double getAlternativeDirection () {
		double mean = 0;
		for (int i = 0 ; i < lastDirections.size(); i++) {
			mean += lastDirections.get(i);
		}
		mean /= lastDirections.size();
		if (mean <= LogoEnvPLS.SOUTH_WEST + Math.PI/8 && mean > LogoEnvPLS.SOUTH_WEST - Math.PI/8) {
			return LogoEnvPLS.SOUTH_WEST;
		} else if (mean <= LogoEnvPLS.WEST + Math.PI/8 && mean > LogoEnvPLS.WEST - Math.PI/8) {
			return LogoEnvPLS.WEST;
		} else if (mean <= LogoEnvPLS.NORTH_WEST + Math.PI/8 && mean > LogoEnvPLS.NORTH_WEST - Math.PI/8) {
			return LogoEnvPLS.NORTH_WEST;
		} else if (mean <= LogoEnvPLS.NORTH + Math.PI/8 && mean > LogoEnvPLS.NORTH - Math.PI/8) {
			return LogoEnvPLS.NORTH;
		} else if (mean <= LogoEnvPLS.NORTH_EAST + Math.PI/8 && mean > LogoEnvPLS.NORTH_EAST - Math.PI/8) {
			return LogoEnvPLS.NORTH_EAST;
		} else if (mean <= LogoEnvPLS.EAST + Math.PI/8 && mean > LogoEnvPLS.EAST - Math.PI/8) {
			return LogoEnvPLS.EAST;
		} else if (mean <= LogoEnvPLS.SOUTH_EAST + Math.PI/8 && mean > LogoEnvPLS.SOUTH_EAST - Math.PI/8) {
			return LogoEnvPLS.SOUTH_EAST;
		} else {
			return LogoEnvPLS.SOUTH;
		}
	}
}
