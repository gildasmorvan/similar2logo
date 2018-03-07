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
package fr.lgi2a.similar2logo.examples.transport.model.agents.generator;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bike.BikeCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bike.BikeDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bike.BikeFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bus.BusCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bus.BusDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bus.BusFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bus.BusPLS;
import fr.lgi2a.similar2logo.examples.transport.model.agents.car.CarCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.car.CarDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.car.CarFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.person.PersonCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.person.PersonDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.person.PersonFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.person.PersonPLS;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.TrainCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.TramCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.TransportDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.TransportFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.TransportPLS;
import fr.lgi2a.similar2logo.examples.transport.model.places.BusLine;
import fr.lgi2a.similar2logo.examples.transport.model.places.Station;
import fr.lgi2a.similar2logo.examples.transport.model.places.World;
import fr.lgi2a.similar2logo.examples.transport.parameters.DestinationGenerator;
import fr.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.kernel.tools.FastMath;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

import static fr.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;

/**
 * Agent that creates new agents of every type.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * 
 */
public class GeneratorDecisionModel extends AbstractAgtDecisionModel {
	
	/**
	 * Limits of each type of way.
	 * The key is the type of way.
	 * The value is a list with all the points limits for this type of way
	 */
	private Map<String,List<Point2D>> limits;
	
	/**
	 * The world of the map
	 */
	private World world;
	
	/**
	 * The parameters planning
	 */
	private TransportParametersPlanning planning;
	
	/**
	 * The destination generator
	 */
	private DestinationGenerator destinationGenerator;

	public GeneratorDecisionModel(World world, Map<String,List<Point2D>> limits, TransportParametersPlanning tpp, DestinationGenerator dg) {
		super(LogoSimulationLevelList.LOGO);
		this.world = world;
		this.limits = limits;
		this.planning = tpp;
		this.destinationGenerator = dg;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		/*for (Station s : world.getStations()) {
			if (s.getType().equals(BUSWAY)) {
				System.out.println(s.toString());
				System.out.println(s.toStringPosition());
			}
		}*/
		//Adds person and car on the limit
		for (int i =0; i < limits.get(STREET).size(); i++) {
			Point2D p = limits.get(STREET).get(i);
			TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			if (RandomValueFactory.getStrategy().randomDouble() <= tsp.probaCreateCar) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generateCarToAddOnLimits(timeUpperBound, p,tsp)));
			}
			if (RandomValueFactory.getStrategy().randomDouble() <= tsp.probaCreatePerson) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generatePersonToAddOnLimits(timeUpperBound, p,tsp)));
			}
			if (RandomValueFactory.getStrategy().randomDouble() <= tsp.probaCreateBike) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound,
						generateBikeToAddOnLimits(timeUpperBound, p, tsp)));
			}
		}
		//Adds the buses at the limits of the lines
		for (int i = 0; i < world.getBusLines().size(); i++) {
			Point2D p = world.getBusLines().get(i).getFirstExtremity();
			TransportSimulationParameters tsp = planning.getParameters(
				timeUpperBound, p, world.getWidth(), world.getHeight()
			);
			if (FastMath.areEqual(timeLowerBound.getIdentifier() % tsp.creationFrequencyBus, 0)) {
				producedInfluences.add(new SystemInfluenceAddAgent(
					getLevel(),
					timeLowerBound,
					timeUpperBound, 
					generateBusToAddOnLimits(
						p,
						world.getBusLines().get(i).getSecondExtremity(),
						world.getBusLines().get(i), tsp, timeLowerBound)
					)
				);
			}
			p = world.getBusLines().get(i).getSecondExtremity();
			tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			if (FastMath.areEqual(timeLowerBound.getIdentifier() % tsp.creationFrequencyBus, 0)) {
				producedInfluences.add(
					new SystemInfluenceAddAgent(
						getLevel(),
						timeLowerBound,
						timeUpperBound, 
						generateBusToAddOnLimits(
							p,
							world.getBusLines().get(i).getFirstExtremity(),
							world.getBusLines().get(i),
							tsp,
							timeLowerBound
						)
					)
				);
			}
		}
		//adds the tramway at the limits
		for (int i = 0; i < limits.get(TRAMWAY).size(); i++) {
			Point2D p = limits.get(TRAMWAY).get(i);
			TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			if (FastMath.areEqual(timeLowerBound.getIdentifier() % tsp.creationFrequencyTram, 0)) {
				producedInfluences.add(new SystemInfluenceAddAgent(
					getLevel(),
					timeLowerBound,
					timeUpperBound, 
					generateTramToAddOnLimits(p,tsp, timeUpperBound)
				));
			}
		}
		//Adds the trains at the limits
		Point2D trainLimit = limits.get(RAILWAY).get(
			RandomValueFactory.getStrategy().randomInt(limits.get(RAILWAY).size())
		);
		TransportSimulationParameters tspTrain = planning.getParameters(
			timeUpperBound, trainLimit, world.getWidth(), world.getHeight()
		);
		if (FastMath.areEqual(timeLowerBound.getIdentifier() % tspTrain.creationFrequencyTrain, 0)) {
			producedInfluences.add(new SystemInfluenceAddAgent(
				getLevel(),
				timeLowerBound,
				timeUpperBound, 
				generateTrainToAddOnLimits(trainLimit,tspTrain, timeUpperBound)
			));
		}
		//The people go out from the stations
		if (timeLowerBound.getIdentifier() % planning.getStep()== 0) {
			for (Station st : world.getStations()) {
				Point2D p = st.getExit();
				TransportSimulationParameters tsp = planning.getParameters(
					timeUpperBound, p, world.getWidth(), world.getHeight()
				);
				if (st.getType().equals(RAILWAY)) {
					if (!st.nooneWantsToGoOut()) {
						ExtendedAgent ea = st.getPersonsWantingToGoOut().remove(0);
						PersonPLS person = (PersonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						if (person.getOriginalType().equals(CAR)) {
							producedInfluences.add(
								new SystemInfluenceAddAgent(
									getLevel(),
									timeLowerBound,
									timeUpperBound,
									createCarFromPerson(person, p, tsp)
								)
							);
						} else if (person.getOriginalType().equals(BIKE)) {
							producedInfluences.add(
								new SystemInfluenceAddAgent(
									getLevel(),
									timeLowerBound,
									timeUpperBound, 
									createBikeFromPerson(person, p, tsp)
								)
							);
						} else {
							producedInfluences.add(
								new SystemInfluenceAddAgent(
									getLevel(),
									timeLowerBound,
									timeUpperBound,
									recreatePerson(person, p, tsp)
								)
							);
						}
					}
				} else {
					if (!st.nooneWantsToGoOut()) {
						ExtendedAgent ea = st.getPersonsWantingToGoOut().remove(0);
						if (st.getType().equals(BUS_STOP)) {
							PersonDecisionModel pdm = (PersonDecisionModel) ea.getDecisionModel(LogoSimulationLevelList.LOGO);
							pdm.setWay(world.getGraph().wayToGo(p, pdm.getDestination()));
						}
						PersonPLS person = (PersonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						person.setMove(true);
						producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound,
								recreatePerson(person, p, tsp)));
					}
				}
			}
		}
		//People in the leisures
		for (int i=0; i < world.getLeisures().size(); i++) {
			Point2D p = world.getLeisures().get(i).getPosition();
			TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			if (world.getLeisures().get(i).getWaitingPeople(timeLowerBound) > 0 && 
					timeLowerBound.getIdentifier() % planning.getStep()== 0) {
				world.getLeisures().get(i).removeWaitingPeople();
				double type = RandomValueFactory.getStrategy().randomDouble();
				if (type <= tsp.probaToBeACar) {
					producedInfluences.add(
						new SystemInfluenceAddAgent(
							getLevel(),
							timeLowerBound,
							timeUpperBound, 
							generateCarToAdd(timeUpperBound, p,tsp)
						)
					);
				} else if (type <= tsp.probaToBeABike + tsp.probaToBeACar) {
					producedInfluences.add(
						new SystemInfluenceAddAgent(
							getLevel(),
							timeLowerBound,
							timeUpperBound,
							generateBikeToAdd(timeUpperBound, p, tsp)
						)
					);
				} else {
					producedInfluences.add(
						new SystemInfluenceAddAgent(
							getLevel(),
							timeLowerBound,
							timeUpperBound, 
							generatePersonToAdd(timeUpperBound, p,tsp)
						)
					);
				}
			}
		}
		//People leave their home
		for (int i =0; i < world.getRoads().size(); i++) {
			Point2D p = world.getRoads().get(i);
			TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			if (RandomValueFactory.getStrategy().randomDouble() <= tsp.probaLeaveHome) {
				double type = RandomValueFactory.getStrategy().randomDouble();
				if (type <= tsp.probaToBeACar) {
					producedInfluences.add(
						new SystemInfluenceAddAgent(
							getLevel(),
							timeLowerBound,
							timeUpperBound, 
							generateCarToAdd(timeUpperBound, p,tsp)
						)
					);
				} else if (type <= tsp.probaToBeABike + tsp.probaToBeACar) {
					producedInfluences.add(
						new SystemInfluenceAddAgent(
							getLevel(),
							timeLowerBound,
							timeUpperBound, 
							generateBikeToAdd(timeUpperBound, p, tsp)
						)
					);
				} else {
					producedInfluences.add(
						new SystemInfluenceAddAgent(
							getLevel(),
							timeLowerBound,
							timeUpperBound,
							generatePersonToAdd(timeUpperBound, p,tsp)
						)
					);
				}
			}	
		}
	}
	
	/**
	 * Generate a person to add in the simulation
	 * @param sts the simulation time stamp when the person is added
	 * @param position the position to put the person
	 * @param tsp the transport simulation parameters
	 * @return a person to insert in the simulation
	 */
	private IAgent4Engine generatePersonToAdd (SimulationTimeStamp sts, Point2D position, TransportSimulationParameters tsp) {
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
				LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
		Point2D destination = destinationGenerator.getADestination(sts, position, PERSON);
		return PersonFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new PersonDecisionModel(sts, world, planning, destination, destinationGenerator, 
							world.getGraph().wayToGoFollowingType(position, destination, PERSON)),
					PersonCategory.CATEGORY,
					starts[RandomValueFactory.getStrategy().randomInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyPerson,
					PERSON
				);
	}
	
	/**
	 * Generates a bike to add in the simulation
	 * @param sts the simulation time stamp when the bike is added
	 * @param position the position where put the bike
	 * @param tsp the transport simulation parameters
	 * @return a bike to insert in the simulation
	 */
	private IAgent4Engine generateBikeToAdd (SimulationTimeStamp sts, Point2D position, TransportSimulationParameters tsp) {
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
				LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
		Point2D destination = destinationGenerator.getADestination(sts, position, BIKE);
		return BikeFactory.generate(
				new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true), 
				new BikeDecisionModel(destination, world, sts, planning, 
						world.getGraph().wayToGoFollowingType(position, destination, BIKE), destinationGenerator), 
				BikeCategory.CATEGORY, 
				starts[RandomValueFactory.getStrategy().randomInt(starts.length)], 
				0,
				0, 
				position.getX(),
				position.getY(), 
				tsp.speedFrequencyBike);
	}
	
	/**
	 * Generate a car to add in the simulation
	 * @param the simulation time stamp when the car is added
	 * @param position the position to put the car
	 * @param tsp the transport simulation parameters
	 * @return a car to insert in the simulation
	 */
	private IAgent4Engine generateCarToAdd (SimulationTimeStamp sts, Point2D position, TransportSimulationParameters tsp) {
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
				LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
		Point2D destination = destinationGenerator.getADestination(sts, position, CAR);
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new CarDecisionModel(world, sts, planning, destination,
							destinationGenerator, world.getGraph().wayToGoFollowingType(position, destination, CAR)),
					CarCategory.CATEGORY,
					starts[RandomValueFactory.getStrategy().randomInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyCarAndBus,
					tsp.carCapacity,
					tsp.carSize
				);
	}
	
	/**
	 * Generate a person to add in the simulation
	 * @param sts simulation time stamp when the person is added
	 * @param position the position to put the person to create
	 * @param tsp the transport simulation parameters
	 * @return a person to insert in the simulation
	 */
	private IAgent4Engine generatePersonToAddOnLimits (SimulationTimeStamp sts, Point2D position, TransportSimulationParameters tsp) {
		Point2D np = startPosition(position);
		Point2D destination = destinationGenerator.getADestination(sts, np, PERSON);
		return PersonFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new PersonDecisionModel(sts, world, planning, destination, 
							destinationGenerator, world.getGraph().wayToGoFollowingType(np, destination, PERSON)),
					PersonCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					tsp.speedFrequencyPerson,
					PERSON
				);
	}
	
	/**
	 * Generate a bike to add in the simulation
	 * @param sts simulation time stamp when the bike is added
	 * @param position the position to put the bike to create
	 * @param tsp the transport simulation parameters
	 * @return a bike to insert in the simulation
	 */
	private IAgent4Engine generateBikeToAddOnLimits (SimulationTimeStamp sts, Point2D position, TransportSimulationParameters tsp) {
		Point2D np = startPosition(position);
		Point2D destination = destinationGenerator.getADestination(sts, np, BIKE);
		return 	BikeFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new BikeDecisionModel(destination, world, new SimulationTimeStamp(0), planning, 
							world.getGraph().wayToGoFollowingType(np, destination, BIKE)
							, destinationGenerator),
					BikeCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyBike
				);
	}
	
	/**
	 * Generate a car to add in the simulation
	 * @param sts the simulation time stamp when the car is added
	 * @param position the position to put the car to create
	 * @param tsp the transport simulation parameters
	 * @return a car to insert in the simulation
	 */
	private IAgent4Engine generateCarToAddOnLimits (SimulationTimeStamp sts, Point2D position, TransportSimulationParameters tsp) {
		Point2D np = startPosition(position);
		Point2D destination = destinationGenerator.getADestination(sts, np, CAR);
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new CarDecisionModel(world, sts, planning, destination, destinationGenerator,
							world.getGraph().wayToGoFollowingType(np, destination, CAR)),
					CarCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					tsp.speedFrequencyCarAndBus,
					tsp.carCapacity,
					tsp.carSize
				);
	}
	
	/**
	 * Generates a bus on the limit of the map
	 * @param position the position where create the bus
	 * @param des the destination of the bus
	 * @param bl the bus line
	 * @param tsp the transport simulation parameters
	 * @param sts the simulation time stamp
	 * @return a bus to put in the simulation
	 */
	private IAgent4Engine generateBusToAddOnLimits (Point2D position, Point2D des, BusLine bl,
			TransportSimulationParameters tsp, SimulationTimeStamp sts) {
		Point2D np = startPosition(position);
		ExtendedAgent ea = BusFactory.generate(
				new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true), 
				new BusDecisionModel(des, bl, world, sts, planning,
						world.getGraph().wayToGoFollowingType(np, bl.nextDestination(np, des),BUS), 
						destinationGenerator),
				BusCategory.CATEGORY,
				startAngle(np), 0, 0, 
				np.getX(), np.getY(), 
				tsp.speedFrequencyCarAndBus, tsp.busCapacity, tsp.busSize);
		BusPLS bPLS = (BusPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
		for (int j= 0; j < RandomValueFactory.getStrategy().randomInt(bPLS.getMaxCapacity()); j++) {
			Point2D destination = destinationGenerator.getDestinationInTransport(sts, position, BUSWAY);
			List<Point2D> way = startingWayInBus(bl, destination);
			bPLS.getPassengers().add(PersonFactory.generate(
			new TurtlePerceptionModel(
					Math.sqrt(2),Math.PI,true,true,true
				),
				new PersonDecisionModel(sts, world, planning,
						destination, destinationGenerator, way),
				PersonCategory.CATEGORY,
				0 ,
				0 ,
				0,
				position.getX(),
				position.getY(),
				tsp.speedFrequencyPerson,
				PERSON
			));
		}
		return ea;
	}
	
	/**
	 * Creates a way for people in the bus.
	 * We recalculate the way when they exit the bus stop
	 * @param bl the bus line
	 * @param destination the destination of the people
	 * @return the way for people in the buses
	 */
	private List<Point2D> startingWayInBus (BusLine bl, Point2D destination) {
		List<Point2D> res = new ArrayList<>();
		if (world.onTheLimits(destination)) {
			res.add(destination);
		} else {
			double dis = bl.getBusStop().get(0).getPlatform().distance(destination);
			Station best = bl.getBusStop().get(0);
			for (Station s : bl.getBusStop()) {
				if (s.getPlatform().distance(destination) < dis) {
					dis = s.getPlatform().distance(destination);
					best = s;
				}
			}
			res.add(best.getPlatform());
		}
		return res;
	}
	
	/**
	 * Generates a tram to add in the simulation
	 * @param position the position to the tram to create
	 * @param tsp the transport simulation parameters
	 * @return a tram to insert in the simulation
	 */
	private IAgent4Engine generateTramToAddOnLimits (Point2D position, TransportSimulationParameters tsp, SimulationTimeStamp sts) {
		Point2D np = startPosition(position), des = null;
		boolean done = false;
		while (!done) {
			des = limits.get(TRAMWAY).get(RandomValueFactory.getStrategy().randomInt(limits.get(TRAMWAY).size()));
			if (!des.equals(np)) { 
				done = true;
			}
		}
		ExtendedAgent ea =  TransportFactory.generate(
					new TurtlePerceptionModel(
							Math.sqrt(2),Math.PI,true,true,true
						),
						new TransportDecisionModel(des, world, TRAMWAY, limits.get(TRAMWAY), tsp.speedFrequencyTram),
						TramCategory.CATEGORY,
						startAngle(np) ,
						0 ,
						0,
						np.getX(),
						np.getY(),
						tsp.tramwayCapacity,
						tsp.speedFrequencyTram,
						tsp.tramwaySize
					);
		String type = PERSON;
		double proba = RandomValueFactory.getStrategy().randomDouble();
		if (proba <= tsp.probaToBeABikeOutOfTram) {
			type = BIKE;
		}
		TransportPLS tramPLS = (TransportPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
		for (int j= 0; j < RandomValueFactory.getStrategy().randomInt(tramPLS.getMaxCapacity()); j++) {
			Point2D destination = destinationGenerator.getDestinationInTransport(sts, position, TRAMWAY);
			List<Point2D> way = world.getGraph().wayToGoInTransport(position, destination, TRAMWAY);
			tramPLS.getPassengers().add(PersonFactory.generate(
			new TurtlePerceptionModel(
					Math.sqrt(2),Math.PI,true,true,true
				),
				new PersonDecisionModel(sts, world, planning,
						destination, destinationGenerator, way),
				PersonCategory.CATEGORY,
				0 ,
				0 ,
				0,
				position.getX(),
				position.getY(),
				tsp.speedFrequencyPerson,
				type
			));
		}
		return ea;
	}
	
	/**
	 * Generates a train to add in the simulation
	 * @param position the position of the train to create
	 * @param tsp the transport simulation parameters
	 * @return a train to insert in the simulation
	 */
	private IAgent4Engine generateTrainToAddOnLimits (Point2D position, TransportSimulationParameters tsp, SimulationTimeStamp sts) {
		Point2D np = startPosition(position), des = null;
		boolean done = false;
		while (!done) {
			des = limits.get(RAILWAY).get(RandomValueFactory.getStrategy().randomInt(limits.get(RAILWAY).size()));
			if (!des.equals(np)) {
				done = true;
			}
		}
		ExtendedAgent ea = TransportFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new TransportDecisionModel(des, world, RAILWAY, limits.get(RAILWAY), tsp.speedFrequenceTrain),
					TrainCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					tsp.trainCapacity,
					tsp.speedFrequenceTrain,
					tsp.trainSize
				);
		String type = PERSON;
		double proba = RandomValueFactory.getStrategy().randomDouble();
		if (tsp.probaToBeABikeOutOfTrain <= proba) {
			type = BIKE;
		} else if (tsp.probaToBeACarOutOfTrain + tsp.probaToBeABikeOutOfTrain <= proba) {
			type = CAR;
		}
		TransportPLS trainPLS = (TransportPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
		for (int j= 0; j < RandomValueFactory.getStrategy().randomInt(trainPLS.getMaxCapacity()); j++) {
			Point2D destination = destinationGenerator.getDestinationInTransport(sts, position, RAILWAY);
			List<Point2D> way = world.getGraph().wayToGoInTransport(position, destination, RAILWAY);
			trainPLS.getPassengers().add(PersonFactory.generate(
			new TurtlePerceptionModel(
					Math.sqrt(2),Math.PI,true,true,true
				),
				new PersonDecisionModel(sts, world, planning,
						destination, destinationGenerator, way),
				PersonCategory.CATEGORY,
				0 ,
				0 ,
				0,
				position.getX(),
				position.getY(),
				tsp.speedFrequencyPerson,
				type
			));
		}
		return ea;

	}
	
	/**
	 * Recreates the person when they leave the station
	 * @param pls the person public local state
	 * @param position the position where we want to recreate the person
	 * @param tsp the transport simulation parameters
	 * @return the person to add to the simulation
	 */
	private IAgent4Engine recreatePerson (PersonPLS pls, Point2D position, TransportSimulationParameters tsp) {
		ExtendedAgent ea = (ExtendedAgent) pls.getOwner();
		PersonDecisionModel pdm = (PersonDecisionModel) ea.getDecisionModel(LogoSimulationLevelList.LOGO);
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
				LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
		return PersonFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					pdm,
					PersonCategory.CATEGORY,
					starts[RandomValueFactory.getStrategy().randomInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyPerson,
					PERSON
				);
	}
	
	/**
	 * Creates a car from a person pls
	 * @param p the person pls
	 * @param position where we want to create the car
	 * @param tsp the transport simulation parameters
	 * @return the car base on the person
	 */
	private IAgent4Engine createCarFromPerson (PersonPLS p, Point2D position, TransportSimulationParameters tsp) {
		ExtendedAgent ea = (ExtendedAgent) p.getOwner();
		PersonDecisionModel pdm = (PersonDecisionModel) ea.getDecisionModel(LogoSimulationLevelList.LOGO);
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
				LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					pdm.convertInCarDecisionModel(),
					CarCategory.CATEGORY,
					starts[RandomValueFactory.getStrategy().randomInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyCarAndBus,
					tsp.carCapacity,
					tsp.carSize
				);
	}
	
	private IAgent4Engine createBikeFromPerson (PersonPLS p, Point2D position, TransportSimulationParameters tsp) {
		ExtendedAgent ea = (ExtendedAgent) p.getOwner();
		PersonDecisionModel pdm = (PersonDecisionModel) ea.getDecisionModel(LogoSimulationLevelList.LOGO);
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
				LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
		return 	BikeFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					pdm.convertInBikeDecisionModel(),
					BikeCategory.CATEGORY,
					starts[RandomValueFactory.getStrategy().randomInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyBike
				);
	}
	
	/**
	 * Gives a position where put a new car
	 * @param position on the edge of the world
	 * @return the position where put the car
	 */
	private Point2D startPosition (Point2D position) {
		if (FastMath.areEqual(position.getX(), 0)) {
			return new Point2D.Double(position.getX()+1,position.getY());
		} else if (FastMath.areEqual(position.getY(), 0)) {
			return new Point2D.Double(position.getX(),position.getY()+1);
		} else if (FastMath.areEqual(position.getX(), world.getWidth() -1.0)) {
			return new Point2D.Double(position.getX(),position.getY());
		} else {
			return new Point2D.Double(position.getX(),position.getY());
		}
	}
	
	/**
	 * Gives the angle to give to the new car following its position
	 * @param position the next position of the new car
	 * @return the angle which the car starts
	 */
	private double startAngle (Point2D position) {
		if (FastMath.areEqual(position.getX(), 1)) {
			return LogoEnvPLS.EAST;
		} else if (FastMath.areEqual(position.getY(), 1)) {
			return LogoEnvPLS.NORTH;
		} else if (FastMath.areEqual(position.getX(), world.getWidth() -1.0)) {
			return LogoEnvPLS.WEST;
		} else {
			return LogoEnvPLS.SOUTH;
		}		
	}

}