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
import fr.lgi2a.similar2logo.examples.transport.model.places.BusLine;
import fr.lgi2a.similar2logo.examples.transport.model.places.Leisure;
import fr.lgi2a.similar2logo.examples.transport.model.places.Station;
import fr.lgi2a.similar2logo.examples.transport.model.places.World;
import fr.lgi2a.similar2logo.examples.transport.parameters.DestinationGenerator;
import fr.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Decision model of the buses in the transport simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class BusDecisionModel extends RoadAgentDecisionModel {
	
	/**
	 * The STS when the car did its last move
	 */
	private SimulationTimeStamp lastMove;
	
	/**
	 * The bus line where the bus drives
	 */
	private BusLine line;
	
	/**
	 * The bus stations on the line
	 */
	private Map<Point2D, Station> stations;

	public BusDecisionModel(Point2D destination, BusLine bl, World world, SimulationTimeStamp bd, TransportParametersPlanning tpp,
			List<Point2D> way, DestinationGenerator dg) {
		super(destination, world, bd, tpp, way, dg);
		this.lastMove = bd;
		this.line = bl;
		this.stations = new HashMap<>();
		for (Station s : line.getBusStop()) {
			stations.put(s.getAccess(), s);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		BusPLS castedPublicLocalState = (BusPLS) publicLocalState;
		double frequence = castedPublicLocalState.getFrequency();
		Point2D position = castedPublicLocalState.getLocation();
		if (castedPublicLocalState.getSpeed() > 0) {
			lastMove = timeLowerBound;
		}
		TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, position, world.getWidth(), world.getHeight());
		if ((timeLowerBound.getIdentifier()-birthDate.getIdentifier())%tsp.recalculationPath == 0 && way.size() != 0) {
			way = world.getGraph().wayToGo(position, way.get(way.size()-1));
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
				for (ExtendedAgent ea :castedPublicLocalState.getPassengers()) {
					PersonPLS p = (PersonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
					producedInfluences.add(new SystemInfluenceRemoveAgent(LogoSimulationLevelList.LOGO, timeLowerBound, timeUpperBound, p));
				}
				//The bus is on a station or a stop
			} else if (inStation(position)) {
				if (castedPublicLocalState.getSpeed() == 0) {
					double myDirection = castedPublicLocalState.getDirection();
					double dir = getDirection(position, castedPerceivedData);
					List<ExtendedAgent> toRemove = new ArrayList<>();
					for (ExtendedAgent ea : castedPublicLocalState.getPassengers()) {
						PersonPLS p = (PersonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						for (int i =0; i < p.getWay().size(); i++) {
						}
						if (p.getWay().contains(stations.get(position).getAccess())) {
							toRemove.add(ea);
							while (!p.getWay().get(0).equals(stations.get(position).getAccess()))
								p.getWay().remove(0);
							p.getWay().remove(0);
						}
					}
					for (int i=0; i < toRemove.size(); i++) {
						castedPublicLocalState.removePassenger(toRemove.get(i));
						stations.get(position).addPeopleWantingToGoOut(toRemove.get(i));
					}
					List<ExtendedAgent> wantToGoUp = stations.get(position).personsTakingTheBus(position,destination, line);
					while (!castedPublicLocalState.isFull() && wantToGoUp.size() >0) {
						ExtendedAgent ea = wantToGoUp.remove(0);
						castedPublicLocalState.addPassenger(ea);
						stations.get(position).removeWaitingPeopleForTakingTransport(ea);
					}
					producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, 
							-myDirection + dir, castedPublicLocalState));
					producedInfluences.add(new ChangeSpeed(timeLowerBound, timeUpperBound, 
							-castedPublicLocalState.getSpeed() + distanceToDo(dir), castedPublicLocalState));
				Point2D nextDestination = line.nextDestination(position, destination);
				way = world.getGraph().wayToGoFollowingType(position, nextDestination,"bus");
				//The passengers go up and down.
				} else 
					producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
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
			// if the bus is on the edge of the map, we destroy it	
			else if (willGoOut(position, castedPublicLocalState.getDirection())) {
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
				for (int i = 0; i < castedPublicLocalState.getCurrentSize() -1; i++) {
					producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, 
							castedPublicLocalState.getWagon(i)));
				}
				for (ExtendedAgent ea :castedPublicLocalState.getPassengers()) {
					PersonPLS p = (PersonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
					producedInfluences.add(new SystemInfluenceRemoveAgent(LogoSimulationLevelList.LOGO, timeLowerBound, timeUpperBound, p));
				}
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
	 * {@inheritDoc}
	 */
	public boolean inStation (Point2D position) {
		for (Station s : line.getBusStop()) {
			if (s.getAccess().equals(position))
				return true;
		}
		return false;
	}

}
