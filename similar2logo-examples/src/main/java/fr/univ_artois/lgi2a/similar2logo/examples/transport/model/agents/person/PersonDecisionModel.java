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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.person;

import java.awt.geom.Point2D;
import java.util.List;

import fr.univ_artois.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.AbstractRoadAgentDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bike.BikeDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.AbstractLeisure;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.Station;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.World;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.parameters.DestinationGenerator;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgentFromLevel;

import static fr.univ_artois.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;

/**
 * Decision model of a person in the transport simulation.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class PersonDecisionModel extends AbstractRoadAgentDecisionModel {

	public PersonDecisionModel(
		SimulationTimeStamp bd,
		World world,
		TransportParametersPlanning tpp,
		Point2D des,
		DestinationGenerator dg,
		List<Point2D> way
	) {
		super(des, world, bd, tpp, way, dg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		PersonPLS castedPublicLocalState = (PersonPLS) publicLocalState;
		Point2D position = castedPublicLocalState.getLocation();
		TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, position, world.getWidth(), world.getHeight());
		if ((timeLowerBound.getIdentifier()-birthDate.getIdentifier())%tsp.recalculationPath == 0) {
			way = world.getGraph().wayToGoFollowingType(position, destination,PERSON);
		}
		if (MathUtil.areEqual((timeLowerBound.getIdentifier()*10) % (castedPublicLocalState.getSpeedFrequency()*10), 0)
				&& castedPublicLocalState.move) {
			TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
			if (way.size() > 2 && (position.distance(way.get(0))>position.distance(way.get(1)))) {
				way.remove(0);
			}
			//We check if the person reached his next step
			if (position.equals(destination)) {
				if (inLeisure(position)) {
					AbstractLeisure l = findLeisure(position);
					l.addPerson(timeLowerBound);
				}
				producedInfluences.add(new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState));
				//The car is on a station or a stop
			} else if (way.size() > 1 && inStation(position) && way.get(0).equals(position) 
					&& way.get(1).equals(findStation(way.get(0)).getPlatform())) {
				castedPublicLocalState.setMove(false);
				way.remove(0);
				Station s = findStation(position);
				ExtendedAgent ea = (ExtendedAgent) castedPublicLocalState.getOwner();
				s.addPeopleWantingToTakeTheTransport(ea);
				//If we are in a bus stop
			//We update the path
			} else if (way.size() > 1 && position.equals(way.get(0))) {
				way.remove(0);
				producedInfluences.add(new Stop(timeLowerBound, timeUpperBound, castedPublicLocalState));
				Point2D next = destination;
				if (!way.isEmpty()) {
					next = way.get(0);
				}
				producedInfluences.add(
					new ChangeDirection(
						timeLowerBound, 
						timeUpperBound, 
						-castedPublicLocalState.getDirection() + getDirectionForNextStep(position, next),
						castedPublicLocalState
					)
				);
			} else if (willGoOut(position, castedPublicLocalState.getDirection())) {
				// if the car is on the edge of the map, we destroy it	
				producedInfluences.add(
					new SystemInfluenceRemoveAgentFromLevel(timeLowerBound, timeUpperBound, castedPublicLocalState)
				);
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
	 * Converts the person decision model in a car decision model
	 * @return the car decision model corresponding to the person decision model
	 */
	public CarDecisionModel convertInCarDecisionModel () {
		return new CarDecisionModel(world, birthDate, planning, destination, destinationGenerator, way);
	}
	
	/**
	 * Converts the person decision model in a bike decision model
	 * @return the bike decision model corresponding to the person decision model
	 */
	public BikeDecisionModel convertInBikeDecisionModel () {
		return new BikeDecisionModel(destination, world, birthDate, planning, way, destinationGenerator);
	}
}
