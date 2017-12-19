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
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
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

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgentFromLevel;
import fr.lgi2a.similar2logo.examples.transport.model.places.Leisure;
import fr.lgi2a.similar2logo.examples.transport.model.places.World;
import fr.lgi2a.similar2logo.examples.transport.parameters.DestinationGenerator;
import fr.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;

/**
 * Decision model for the cars in the "transport" simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class CarDecisionModel extends RoadAgentDecisionModel {
	
	private SimulationTimeStamp lastMove;

	public CarDecisionModel(World world, SimulationTimeStamp bd, TransportParametersPlanning tpp,
			Point2D des, DestinationGenerator dg, List<Point2D> way) {
		super(des, world, bd, tpp, way, dg);
		this.lastMove = bd;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		CarPLS castedPublicLocalState = (CarPLS) publicLocalState;
		double frequence = castedPublicLocalState.getFrequence();
		Point2D position = castedPublicLocalState.getLocation();
		if (castedPublicLocalState.getSpeed() > 0) {
			lastMove = timeLowerBound;
		}
		TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, position, world.getWidth(), world.getHeight());
		if ((timeLowerBound.getIdentifier()-birthDate.getIdentifier())%tsp.recalculationPath == 0) {
			way = world.getGraph().wayToGo(position, destination);
		}
		//Delete the car if stuck too much time
		if (timeLowerBound.getIdentifier() - lastMove.getIdentifier() >= 500) {
			producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
			for (int i = 0; i < castedPublicLocalState.getCurrentSize() -1; i++) {
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, 
						castedPublicLocalState.getWagon(i)));
			}
		} else if ((timeLowerBound.getIdentifier()*10) % (frequence*10) == 0) {
			TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
			if (way.size() > 2 && (position.distance(way.get(0))>position.distance(way.get(1)))) way.remove(0);
			//If the car is at home or at work, it disappears.
			if (position.equals(destination)) {
				if (inLeisure(position)) {
					Leisure l = findLeisure(position);
					l.addPerson(timeLowerBound);
				}
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
				for (int i = 0; i < castedPublicLocalState.getCurrentSize() -1; i++) {
					producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, 
							castedPublicLocalState.getWagon(i)));
				}
				//The car is on a station or a stop
			} else if (way.size() > 1 && inStation(position) && way.get(0).equals(position) 
					&& (inStation(way.get(1)) || onTheBorder(way.get(1)))) {
				way.remove(0);
				for (int i=0; i < castedPublicLocalState.getNbrPassenger(); i++) {
					ExtendedAgent ae = (ExtendedAgent) generatePersonToAdd(position, tsp, timeLowerBound);
					PersonPLS person = (PersonPLS) ae.getPublicLocalState(LogoSimulationLevelList.LOGO);
					person.setMove(false);
					findStation(position).addPeopleWantingToTakeTheTransport(ae);
				}
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
				for (int i = 0; i < castedPublicLocalState.getCurrentSize() -1; i++) {
					producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, 
							castedPublicLocalState.getWagon(i)));
				}
			}
			//We update the path
			else if (way.size() > 1 && position.equals(way.get(0))) {
				way.remove(0);
				producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
				Point2D next = destination;
				if (way.size() > 0) next = way.get(0);
				producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
						-castedPublicLocalState.getDirection() + getDirectionForNextStep(position, next), castedPublicLocalState));
			}
			// if the car is on the edge of the map, we destroy it	
			else if (willGoOut(position, castedPublicLocalState.getDirection())) {
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
				for (int i = 0; i < castedPublicLocalState.getCurrentSize() -1; i++) {
					producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, 
							castedPublicLocalState.getWagon(i)));
				}
			} else {
				if (!inDeadEnd(position, castedPerceivedData)) {
					int carAroundMe = carNextToMe(position, castedPerceivedData);
					castedPublicLocalState.setFrequence(getNewFrequency(tsp.speedFrequencyCar, getRoadFactor(position, castedPerceivedData), carAroundMe));
					double dir = getDirection(position, castedPerceivedData, castedPublicLocalState.getDirection());
					producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
							-castedPublicLocalState.getDirection() + dir, castedPublicLocalState));
					producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, 
							-castedPublicLocalState.getSpeed() + distanceToDo(dir), castedPublicLocalState));
				} else { //We are in a dead end.
					castedPublicLocalState.setFrequence(tsp.speedFrequencyCar + 2);
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
	 * Gives a direction to the car to take
	 * @param position the current position of the car
	 * @param data the data perceived by the car
	 * @param currentDirection the current direction of the car
	 * @return the direction where the car has to go.
	 */
	private double getDirection (Point2D position, TurtlePerceivedData data, double currentDirection) {
		Point2D obj = destination;
		if (way.size() >1) {
			obj = way.get(0);
		}
		List<Double> directions = new ArrayList<>();
		for(@SuppressWarnings("rawtypes") LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if (perceivedMarks.getContent().getCategory().equals("Street") && (perceivedMarks.getDistanceTo() > 0)) {
				directions.add(perceivedMarks.getDirectionTo());
			}
		}
		double dis = obj.distance(nextPosition(position, directions.get(0)));
		double direction = directions.get(0);
		for (Double d : directions) {
			double newDis = obj.distance(nextPosition(position, d));
			if (newDis < dis) {
				dis = newDis;
				direction = d;
			}
		}
		return direction;
	}
	
	/**
	 * Indicates the number of cars next to a car
	 * @param position the car position
	 * @param data the data perceived by the car 
	 * @return the number of car around the car
	 */
	private int carNextToMe (Point2D position, TurtlePerceivedData data) {
		int cpt = 0;
		for (LocalPerceivedData<TurtlePLSInLogo> t : data.getTurtles()) {
			if (!t.getContent().getLocation().equals(position) && t.getContent().getCategoryOfAgent().equals(CarCategory.CATEGORY)) {
				cpt++;
			}
		}
		return cpt;
	}
	
	/**
	 * Gives the factor to apply to each rode
	 * @param position the car position
	 * @param data the data perceived by the car
	 * @return the frequency of the road
	 */
	private double getRoadFactor (Point2D position, TurtlePerceivedData data) {
		for (@SuppressWarnings("rawtypes") LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if (perceivedMarks.getContent().getCategory().equals("Street")
					&& perceivedMarks.getContent().getLocation().equals(position)) {
				Double d = (double) perceivedMarks.getContent().getContent();
				return d.doubleValue();
			}
		}
		return 1;
	}
	
	/**
	 * Generate a person to add in the simulation
	 * @param position the position to put the person
	 * @param tsp the transport simulation parameters
	 * @param sts the current simulation time stamp
	 * @return a person to insert in the simulation
	 */
	private IAgent4Engine generatePersonToAdd (Point2D position, TransportSimulationParameters tsp,
			SimulationTimeStamp sts) {
		return PersonFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new PersonDecisionModel(sts, world, planning, destination, destinationGenerator, way),
					PersonCategory.CATEGORY,
					0 ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyPerson
				);
	}
	
	private double getNewFrequency (double currentFrequency, double factor, int car) {
		double res = Math.floor(currentFrequency*factor*10);
		double carFactor = car/10;
		return res/10+carFactor;
	}

}
