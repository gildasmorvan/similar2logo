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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
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
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;

/**
 * Decision model of the person in the transport simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class PersonDecisionModel extends AbstractAgtDecisionModel {
	
	/**
	 * The probability to take a transport when the car is in a station.
	 */
	private double probabilityTakeTransport;
	
	/**
	 * The stations on the map.
	 */
	private List<Station> stations;
	
	/**
	 * Height and width of the world
	 */
	private int height, width;
	
	/**
	 * The speed frequency of the person
	 */
	private int speedFrequency;
	
	/**
	 * The probability to be at home (or at work) for a car, and to be deleted from the simulation
	 */
	private double probaToBeAtHome;
	
	/**
	 * The probability a person takes his car and a car becomes a person.
	 */
	private double probaToBecomeCar, probaToBecomePerson;
	
	/**
	 * The frequency of the car.
	 * Necessary for creating a new car.
	 */
	private int carFrequency;

	public PersonDecisionModel(double probaTakeTransport, List<Station> stations, int height, int widht, int frequency, int carFrequency,
			double probaBeAtHome, double probaBecomeACar, double probaBecomeAPerson) {
		super(LogoSimulationLevelList.LOGO);
		this.probabilityTakeTransport = probaTakeTransport;
		this.stations = stations;
		this.height = height;
		this.width = widht;
		this.speedFrequency = frequency;
		this.carFrequency = carFrequency;
		this.probaToBeAtHome = probaBeAtHome;
		this.probaToBecomeCar = probaBecomeACar;
		this.probaToBecomePerson = probaBecomeAPerson;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		PersonPLS castedPublicLocalState = (PersonPLS) publicLocalState;
		if (timeLowerBound.getIdentifier() % speedFrequency == 0) {
			TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
			Point2D position = castedPublicLocalState.getLocation();
			//The car is on a station or a stop
			if (inStation(position)) {
				//The passenger goes up in the transport following the transportTakeTransport probability.
				if (Math.random() <= probabilityTakeTransport) {
					findStation(position).addWaitingPeopleGoOut();
					producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
				}
			}
			//If the car is at home or at work, it disappears. We use a probability for knowing if the car can disappear.
			else if (Math.random() <= probaToBeAtHome) {
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
			} else if (Math.random() <= probaToBecomeCar) {
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generateCarToAdd(position, castedPublicLocalState.getDirection())));
			}
			// if the car is on the edge of the map, we destroy it	
			if (willGoOut(position, castedPublicLocalState.getDirection())) {
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
			} else {
				if (!inDeadEnd(position, castedPerceivedData)) {
					double dir = getDirection(position, castedPerceivedData);
					producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
							-castedPublicLocalState.getDirection() + dir, castedPublicLocalState));
					producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, 
							-castedPublicLocalState.getSpeed() + distanceToDo(dir), castedPublicLocalState));
				} else { //We are in a dead end.
					producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
					if (castedPublicLocalState.getDirection() <= 0) {
						producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
								Math.PI, castedPublicLocalState));
					} else {
						producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
								- Math.PI, castedPublicLocalState));
					}
				}
			}
		} else {
			producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
		}
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
	
	/**
	 * Indicates if the car is somewhere where the passenger can take a transport.
	 * @param position where is the car
	 * @return true if there is an access toward a station/stop, else false.
	 */
	private boolean inStation (Point2D position) {
		for (Station s : this.stations) {
			if (s.getAccess().equals(position))
				return true;
		}
		return false;
	}
	
	/**
	 * Gives the station where is the car
	 * @param position the position of the car
	 * @return the Station that has an access in position. Returns null if the car isn't in station.
	 */
	private Station findStation (Point2D position) {
		for (Station s : this.stations) {
			if (s.getAccess().equals(position))
				return s;
		}
		return null;
	}
	
	/**
	 * Indicates if the transport will leave the map
	 * @param currentPosition current positon of the transport
	 * @param direction current direction of the transport
	 * @return true if the transport will leave the map, false else
	 */
	private boolean willGoOut (Point2D currentPosition, double direction) {
		Point2D p = nextPosition(currentPosition, direction);
		return ((p.getX() < 0) || (p.getX() >= width) || (p.getY() <0) || (p.getY() >= height));
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
	 * Indicates if the car is in a dead end.
	 * @param position the position of the car
	 * @param data the data perceived by the car
	 * @return true if the car is in a dead end, false else
	 */
	private boolean inDeadEnd (Point2D position, TurtlePerceivedData data) {
		for(@SuppressWarnings("rawtypes") LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if (perceivedMarks.getContent().getCategory().equals("Street") && (perceivedMarks.getDistanceTo() > 0)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gives a direction to the car to take
	 * @param position the current position of the car
	 * @param data the data perceived by the car
	 * @return the direction where the car has to go.
	 */
	private double getDirection (Point2D position, TurtlePerceivedData data) {
		List<Double> directions = new ArrayList<>();
		for(@SuppressWarnings("rawtypes") LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if (perceivedMarks.getContent().getCategory().equals("Street") && (perceivedMarks.getDistanceTo() > 0)) {
				directions.add(perceivedMarks.getDirectionTo());
			}
		}
		Random r = new Random();
		return directions.get(r.nextInt(directions.size()));
	}
	
	/**
	 * Generate a car to add in the simulation
	 * @param position the position to put the car
	 * @param direction the direction to give to the car
	 * @return a car to insert in the simulation
	 */
	private IAgent4Engine generateCarToAdd (Point2D position, double direction) {
		// We unit the list of the station;
		List<Station> stop = new ArrayList<>();
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new CarDecisionModel(probabilityTakeTransport, stop, height, width, carFrequency, speedFrequency, probaToBeAtHome,
							probaToBecomePerson, probaToBecomeCar),
					CarCategory.CATEGORY,
					direction ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					1
				);
	}

}
