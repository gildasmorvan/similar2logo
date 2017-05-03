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
import fr.lgi2a.similar2logo.examples.transport.model.Station;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
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
		//At the beginning, we fix the direction of the train
		if (timeLowerBound.getIdentifier() == 0) {
			int cpt = 0;
			for (@SuppressWarnings("rawtypes") LocalPerceivedData<Mark> perceivedMarks : castedPerceivedData.getMarks()) {
				if (perceivedMarks.getContent().getCategory().equals(type) && perceivedMarks.getDistanceTo() > 0) {
					cpt++;
				}
			}
			if (cpt > 0) {
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeLowerBound, 
						getDirection(position, castedPerceivedData), castedPublicLocalState));
				producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, 
						distanceToDo(getDirection(position, castedPerceivedData)), castedPublicLocalState));
			//If the direction at the beginning isn't good, we move from a quarter of turn.
			} else {
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
						castedPublicLocalState.getDirection() + Math.PI, castedPublicLocalState));
			}
		} else {
			//If we are in a station
			if (inStation(position)) {
				//The train is stop, the passengers go down or go up in the train, and the train restarts.
				if (castedPublicLocalState.getSpeed() == 0) {
					//Go down and go up the passengers
					producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
							-castedPublicLocalState.getDirection() + getDirection(position, castedPerceivedData), castedPublicLocalState));
					producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, 
							-castedPublicLocalState.getSpeed() + distanceToDo(getDirection(position, castedPerceivedData)), castedPublicLocalState));
				} else {
					producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
				}
			//If we are at the edge of the map, the train turns around
			} else if (onEdge(position) && !(castedPublicLocalState.getSpeed() == 0)) {
				if (castedPublicLocalState.getDirection() <= 0) {
					producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
							castedPublicLocalState.getDirection() + Math.PI, castedPublicLocalState));
					producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
				} else {
					producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
							castedPublicLocalState.getDirection() - Math.PI, castedPublicLocalState));
					producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
				}
				//We concider we have a new train, so the number of passengers in the transport changes.
				castedPublicLocalState.changeRandomlyNumberPassengers();
				//If we are at destination, we change the destination.
				if (castedPublicLocalState.getLocation().equals(destination)) {
					Random r = new Random ();
					boolean different = false;
					while (!different) {
						Point2D next = limits.get(r.nextInt(limits.size()));
						if (!next.equals(castedPublicLocalState)) {
							destination = next;
							different = true;
						}
					}
				}
			//If we are in the middle of the way
			} else {
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
						-castedPublicLocalState.getDirection() + getDirection(position, castedPerceivedData), castedPublicLocalState));
				producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, 
						-castedPublicLocalState.getSpeed() + distanceToDo(getDirection(position, castedPerceivedData)), castedPublicLocalState));
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
			if (perceivedMarks.getContent().getCategory().equals(type) && (perceivedMarks.getDistanceTo() > 0)
					&& (perceivedMarks.getContent().getLocation().distance(destination) < dis)) {
				bestDirection = perceivedMarks.getDirectionTo();
				dis = perceivedMarks.getDistanceTo();
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
		if ((radius % (Math.PI/2)) == 0) return 1;
		else return Math.sqrt(2);
	}

}
