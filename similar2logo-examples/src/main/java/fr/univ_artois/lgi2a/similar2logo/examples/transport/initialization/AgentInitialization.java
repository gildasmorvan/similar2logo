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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.initialization;

import static fr.univ_artois.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bike.BikeCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bike.BikeDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bike.BikeFactory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bus.BusCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bus.BusDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bus.BusFactory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bus.BusPLS;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarFactory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.generator.GeneratorDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.generator.GeneratorFactory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.person.PersonCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.person.PersonDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.person.PersonFactory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.TrainCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.TramCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.TransportDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.TransportFactory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.TransportPLS;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.BusLine;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.Station;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData;
import fr.univ_artois.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel;

/**
 * Initialization of the agents.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class AgentInitialization {
	
	/**
	 * The simulation model to initialize
	 */
	private TransportSimulationModel transportSimulationModel;
	
	/**
	 * Builds a new AgentInitialization object
	 * @param transportSimulationModel the simulation model to initialize
	 */
	public AgentInitialization(TransportSimulationModel transportSimulationModel) {
		this.transportSimulationModel = transportSimulationModel;
	}
	
	/**
	 * Generates a transport following its type
	 * @param type the type of the transport, RAILWAY for a train, TRAMWAY for a tram.
	 * @param tsp the transport simulation parameters.
	 * @param aid the agent initialization transportSimulationModel.getData().
	 */
	protected void generateTransports (
		String type, 
		TransportSimulationParameters tsp,
		AgentInitializationData aid
	) {
		Point2D neutral = new Point2D.Double(0, 0);
		TransportSimulationParameters newParam = transportSimulationModel.getPlanning().getParameters(
			transportSimulationModel.getInitialTime(), neutral,
			transportSimulationModel.getData().getWidth(), transportSimulationModel.getData().getHeight()
		);
		List<List<String>> list = null;
		int nbr = 0;
		if (RAILWAY.equals(type)) {
			nbr = tsp.nbrTrains;
			list = transportSimulationModel.getData().getRailway();
		} else if (TRAMWAY.equals(type)){
			nbr = tsp.nbrTramways;
			list = transportSimulationModel.getData().getTramway();
		}
		for (List<String> l : list) {
			for (String s : l) {
				Point2D pt = transportSimulationModel.getData().getCoordinates(s);
				if (InitializationUtil.inTheEnvironment(transportSimulationModel.getData(), pt)
				 && !transportSimulationModel.getStartingPointsForTransports().get(type).contains(pt)) {
					transportSimulationModel.getStartingPointsForTransports().get(type).add(pt);
				}
			}
		}
		//We add transport while we can
		for (int i = 0; i < nbr; i++) {
			Point2D des = null, position = this.findPlaceForTransport(type);
			boolean done = false;
			while (!done) {
				des = transportSimulationModel.getLimits().get(type).get(
					PRNG.get().randomInt(transportSimulationModel.getLimits().get(type).size())
				);
				if (!des.equals(position)) { 
					done = true;
				}
			}
			if (RAILWAY.equals(type)) {
				aid.getAgents().add(
					railwayInitialization(position, des, tsp, newParam)
				);
			} else if (TRAMWAY.equals(type)) {
				aid.getAgents().add(
					tramwayInitialization(position, des, tsp, newParam)
				);
			}
		}
	}
	
	private ExtendedAgent railwayInitialization(
		Point2D position,
		Point2D des,
		TransportSimulationParameters tsp,
		TransportSimulationParameters newParam
	) {
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.WEST,LogoEnvPLS.SOUTH,LogoEnvPLS.NORTH};
		String typeI = PERSON;
		double random = PRNG.get().randomDouble();
		if (tsp.probaToBeABikeOutOfTrain <= random) {
			typeI= BIKE;
		} else if (tsp.probaToBeACarOutOfTrain+tsp.probaToBeABikeOutOfTrain <= random) {
			typeI = CAR;
		}
		ExtendedAgent train = TransportFactory.generate(
				new ConeBasedPerceptionModel(
						SQRT_2,Math.PI,true,true,false
					),
					new TransportDecisionModel(des, transportSimulationModel.getWorld(), RAILWAY, transportSimulationModel.getLimits().get(RAILWAY), newParam.speedFrequenceTrain),
					TrainCategory.CATEGORY,
					starts[PRNG.get().randomInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					newParam.trainCapacity,
					newParam.speedFrequenceTrain,
					newParam.trainSize
				);
		TransportPLS trainPLS = (TransportPLS) train.getPublicLocalState(LogoSimulationLevelList.LOGO);
		for (int j= 0; j < PRNG.get().randomInt(trainPLS.getMaxCapacity()); j++) {
			Point2D destination = transportSimulationModel.getDestinationGenerator().getDestinationInTransport(transportSimulationModel.getInitialTime(), position, RAILWAY);
			List<Point2D> way = transportSimulationModel.getGraph().wayToGoInTransport(position, destination, RAILWAY);
			trainPLS.getPassengers().add(PersonFactory.generate(
				new ConeBasedPerceptionModel(
					SQRT_2,Math.PI,true,true,false
				),
				new PersonDecisionModel(new SimulationTimeStamp(0), transportSimulationModel.getWorld(), transportSimulationModel.getPlanning(),
						destination, transportSimulationModel.getDestinationGenerator(), way),
				PersonCategory.CATEGORY,
				0 ,
				0 ,
				0,
				position.getX(),
				position.getY(),
				newParam.speedFrequencyPerson,
				typeI
			));
		}
		return train;
	}

	private ExtendedAgent tramwayInitialization(
			Point2D position,
			Point2D des,
			TransportSimulationParameters tsp,
			TransportSimulationParameters newParam
		) {
		String typeI = PERSON;
		double random = PRNG.get().randomDouble();
		if (tsp.probaToBeABikeOutOfTram <= random) { 
			typeI = BIKE;
		}
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.WEST,LogoEnvPLS.SOUTH,LogoEnvPLS.NORTH};
		ExtendedAgent tramway = TransportFactory.generate(
				new ConeBasedPerceptionModel(
						SQRT_2,Math.PI,true,true,false
					),
					new TransportDecisionModel(des, transportSimulationModel.getWorld(), TRAMWAY, transportSimulationModel.getLimits().get(TRAMWAY), newParam.speedFrequencyTram),
					TramCategory.CATEGORY,
					starts[PRNG.get().randomInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					newParam.tramwayCapacity,
					newParam.speedFrequencyTram,
					newParam.tramwaySize
				);
		TransportPLS tramPLS = (TransportPLS) tramway.getPublicLocalState(LogoSimulationLevelList.LOGO);
		for (int j= 0; j < PRNG.get().randomInt(tramPLS.getMaxCapacity()); j++) {
			Point2D destination = transportSimulationModel.getDestinationGenerator().getDestinationInTransport(transportSimulationModel.getInitialTime(), position, TRAMWAY);
			List<Point2D> way = transportSimulationModel.getGraph().wayToGoInTransport(position, destination, TRAMWAY);
			tramPLS.getPassengers().add(PersonFactory.generate(
			new ConeBasedPerceptionModel(
					SQRT_2,Math.PI,true,true,false
				),
				new PersonDecisionModel(new SimulationTimeStamp(0), transportSimulationModel.getWorld(), transportSimulationModel.getPlanning(),
						destination, transportSimulationModel.getDestinationGenerator(), way),
				PersonCategory.CATEGORY,
				0 ,
				0 ,
				0,
				position.getX(),
				position.getY(),
				newParam.speedFrequencyPerson,
				typeI
			));
		}
		return tramway;
	}
	
	/**
	 * Generates the car in the simulation
	 * @param tsp the transport simulation parameters
	 * @param aid the agents at the beginning
	 */
	protected void generateCars (TransportSimulationParameters tsp, AgentInitializationData aid) {
		Point2D neutral = new Point2D.Double(0, 0);
		TransportSimulationParameters newParam = transportSimulationModel.getPlanning().getParameters(transportSimulationModel.getInitialTime(), neutral, transportSimulationModel.getData().getWidth(), transportSimulationModel.getData().getHeight());
		int nbr = tsp.nbrCars;
		for (List<String> list : transportSimulationModel.getData().getHighway()) {
			for (String s : list) {
				Point2D pt = transportSimulationModel.getData().getCoordinates(s);
				if (InitializationUtil.inTheEnvironment(transportSimulationModel.getData(), pt) && !transportSimulationModel.getStartingPointsForCars().contains(pt)) {
					transportSimulationModel.getStartingPointsForCars().add(pt);
				}
			}
		}
		List<Integer> aPrendre = new ArrayList<>();
		for (int i=0; i < transportSimulationModel.getStartingPointsForCars().size(); i++) {
			aPrendre.add(i);
		}
		for (int i = 0; i < nbr; i++) {
			int p = aPrendre.remove(PRNG.get().randomInt(aPrendre.size()));
			Point2D position = transportSimulationModel.getStartingPointsForCars().get(p);
			Point2D destination = transportSimulationModel.getDestinationGenerator().getADestination(transportSimulationModel.getInitialTime(), position, CAR);
			List<Point2D> way = transportSimulationModel.getGraph().wayToGoFollowingType(position, destination, CAR);
			Point2D firstStep = destination;
			if (way.size() > 1) {
				firstStep = way.get(0);
			}
			aid.getAgents().add(CarFactory.generate(
				new ConeBasedPerceptionModel(
						SQRT_2,Math.PI,true,true,false
					),
					new CarDecisionModel(
						transportSimulationModel.getWorld(),
						new SimulationTimeStamp(0),
						transportSimulationModel.getPlanning(), destination, transportSimulationModel.getDestinationGenerator(), way
					),
					CarCategory.CATEGORY,
					getDirectionForStarting(position, firstStep) ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					newParam.speedFrequencyCarAndBus,
					newParam.carCapacity,
					newParam.carSize
				)
			);
		}
	}
	
	/**
	 * Generates the buses in the simulation
	 * @param tsp the transport simulation parameters
	 * @param aid the agents at the beginning
	 */
	protected void generateBuses (TransportSimulationParameters tsp, AgentInitializationData aid) {
		Point2D neutral = new Point2D.Double(0, 0);
		TransportSimulationParameters newParam = transportSimulationModel.getPlanning().getParameters(transportSimulationModel.getInitialTime(), neutral, transportSimulationModel.getData().getWidth(), transportSimulationModel.getData().getHeight());
		int nbr = tsp.nbrBuses;
		Map<Point2D,BusLine> stops = new HashMap<>();
		for (BusLine bl : transportSimulationModel.getWorld().getBusLines()) {
			for (Station s : bl.getBusStop()) {
				if (!stops.containsKey(s.getAccess())) {
					stops.put(s.getAccess(), bl);
				}
			}
		}
		List<Point2D> startingPointsForBuses = new ArrayList<>();
		for (Point2D p : stops.keySet()) {
			startingPointsForBuses.add(p);
		}
		List<Integer> aPrendre = new ArrayList<>();
		for (int i=0; i < startingPointsForBuses.size(); i++) {
			aPrendre.add(i);
		}
		for (int i = 0; i < nbr; i++) {
			int line = PRNG.get().randomInt(transportSimulationModel.getWorld().getBusLines().size());
			BusLine bl = transportSimulationModel.getWorld().getBusLines().get(line);
			int pos = PRNG.get().randomInt(bl.getBusStop().size());
			Point2D position = bl.getBusStop().get(pos).getAccess();
			int d = PRNG.get().randomInt(2);
			Point2D destination = position;
			if (d == 0) {
				destination = bl.getFirstExtremity();
			} else {
				destination = bl.getSecondExtremity();
			}
			Point2D nextDestination = bl.nextDestination(position, destination);
			List<Point2D> way = transportSimulationModel.getGraph().wayToGoFollowingType(position, nextDestination, BUS);
			Point2D firstStep = nextDestination;
			if (way.size() > 1) {
				firstStep = way.get(0);
			}
			ExtendedAgent bus = BusFactory.generate(
				new ConeBasedPerceptionModel(SQRT_2,Math.PI,true,true,false),
				new BusDecisionModel(
					destination,
					bl,
					transportSimulationModel.getWorld(),
					new SimulationTimeStamp(0),
					transportSimulationModel.getPlanning(),
					way,
					transportSimulationModel.getDestinationGenerator()
				),
				BusCategory.CATEGORY,
				getDirectionForStarting(position, firstStep) ,
				0 ,
				0,
				position.getX(),
				position.getY(),
				newParam.speedFrequencyCarAndBus,
				newParam.busCapacity,
				newParam.busSize
			);
			BusPLS bPLS = (BusPLS) bus.getPublicLocalState(LogoSimulationLevelList.LOGO);
			for (int j= 0; j < PRNG.get().randomInt(bPLS.getMaxCapacity()); j++) {
				Point2D destinationP = transportSimulationModel.getDestinationGenerator().getDestinationInTransport(
					new SimulationTimeStamp(0), position, BUSWAY
				);
				List<Point2D> wayP = new ArrayList<>();
				if (InitializationUtil.onEdge(transportSimulationModel.getData(), destinationP)) {
					wayP.add(destinationP);
				} else {
					double dis = bl.getBusStop().get(0).getAccess().distance(destinationP);
					int ind = 0;
					for (int k = 1; k < bl.getBusStop().size(); k++) {
						if (dis > bl.getBusStop().get(k).getAccess().distance(destinationP)) {
							dis = bl.getBusStop().get(k).getAccess().distance(destinationP);
							ind = k;
						}
					}
					wayP.add(bl.getBusStop().get(ind).getAccess());
				}
				bPLS.getPassengers().add(PersonFactory.generate(
				new ConeBasedPerceptionModel(
						SQRT_2,Math.PI,true,true,false
					),
					new PersonDecisionModel(new SimulationTimeStamp(0), transportSimulationModel.getWorld(), transportSimulationModel.getPlanning(),
							destination, transportSimulationModel.getDestinationGenerator(), way),
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
			aid.getAgents().add(bus);
		}
	}
	
	/**
	 * Generates the bikes in the simulation
	 * @param tsp the transport simulation parameters
	 * @param aid the agents at the beginning
	 */
	protected void generateBikes (TransportSimulationParameters tsp, AgentInitializationData aid) {
		Point2D neutral = new Point2D.Double(0, 0);
		TransportSimulationParameters newParam = transportSimulationModel.getPlanning().getParameters(
			transportSimulationModel.getInitialTime(),
			neutral,
			transportSimulationModel.getData().getWidth(),
			transportSimulationModel.getData().getHeight()
		);
		int nbr = tsp.nbrBikes;
		for (int i = 0; i < nbr; i++) {
			Point2D position = transportSimulationModel.getStartingPointsForCars().get(
				PRNG.get().randomInt(transportSimulationModel.getStartingPointsForCars().size())
			);	
			Point2D destination = transportSimulationModel.getDestinationGenerator().getADestination(
				transportSimulationModel.getInitialTime(), position, BIKE
			);
			List<Point2D> way = transportSimulationModel.getGraph().wayToGoFollowingType(position, destination, BIKE);
			Point2D firstStep = destination;
			if (way.size() > 1) {
				firstStep = way.get(0);
			}
			aid.getAgents().add(BikeFactory.generate(
				new ConeBasedPerceptionModel(
						SQRT_2,Math.PI,true,true,false
				),
				new BikeDecisionModel(destination, transportSimulationModel.getWorld(), new SimulationTimeStamp(0), transportSimulationModel.getPlanning(), way
						, transportSimulationModel.getDestinationGenerator()),
				BikeCategory.CATEGORY,
				getDirectionForStarting(position, firstStep) ,
				0 ,
				0,
				position.getX(),
				position.getY(),
				newParam.speedFrequencyBike
			));
		}
	}
	
	/**
	 * Generates the persons in the simulation
	 * @param tsp the transport simulation parameters
	 * @param aid the agents at the beginning
	 */
	protected void generatePersons (TransportSimulationParameters tsp, AgentInitializationData aid) {
		Point2D neutral = new Point2D.Double(0, 0);
		TransportSimulationParameters newParam = transportSimulationModel.getPlanning().getParameters(
			transportSimulationModel.getInitialTime(), neutral, transportSimulationModel.getData().getWidth(), transportSimulationModel.getData().getHeight()
		);
		int nbr = tsp.nbrPersons;
		for (int i = 0; i < nbr; i++) {
			Point2D position = transportSimulationModel.getStartingPointsForCars().get(
				PRNG.get().randomInt(transportSimulationModel.getStartingPointsForCars().size())
			);	
			Point2D destination = transportSimulationModel.getDestinationGenerator().getADestination(transportSimulationModel.getInitialTime(), position, PERSON);
			List<Point2D> way = transportSimulationModel.getGraph().wayToGoFollowingType(position, destination,PERSON);
			Point2D firstStep = destination;
			if (way.size() > 1) {
				firstStep = way.get(0);
			}
			aid.getAgents().add(PersonFactory.generate(
				new ConeBasedPerceptionModel(
					SQRT_2,Math.PI,true,true,false
				),
				new PersonDecisionModel(new SimulationTimeStamp(0), transportSimulationModel.getWorld(), transportSimulationModel.getPlanning(),
						destination, transportSimulationModel.getDestinationGenerator(), way),
				PersonCategory.CATEGORY,
				getDirectionForStarting(position, firstStep) ,
				0 ,
				0,
				position.getX(),
				position.getY(),
				newParam.speedFrequencyPerson,
				PERSON
			));
		}
	}
	
	/**
	 * Generates the creator of the simulation
	 * @param tsp the transport simulation parameter
	 * @param aid the agents initialization data
	 */
	protected void generateCreator (TransportSimulationParameters tsp, AgentInitializationData aid) {
		aid.getAgents().add(GeneratorFactory.generate(
			new GeneratorDecisionModel(
				transportSimulationModel.getWorld(),
				transportSimulationModel.getLimits(),
				transportSimulationModel.getPlanning(),
				transportSimulationModel.getDestinationGenerator())
			)
		);
	}
	
	
	
	/**
	 * Gives a place to put a vehicle.
	 * Removes the place from the stratingPointsForTransport list
	 * @param type the type of way where the transport can go
	 * @return Point2D where put a vehicle
	 * @throws Exception if there is no more place available
	 */
	protected Point2D findPlaceForTransport (String type) {
		return transportSimulationModel.getStartingPointsForTransports().get(type).remove(
			PRNG.get().randomInt(transportSimulationModel.getStartingPointsForTransports().get(type).size())
		);
	}
	


	
	
	/**
	 * Gives the best direction for the new car and person
	 * @param position the start position of the cars and the persons
	 * @param firstStep the first position where they have to go
	 * @return the direction they have to go
	 */
	protected double getDirectionForStarting (Point2D position, Point2D firstStep) {
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
				LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
		int ind = 0;
		double dis = nextPosition(position, starts[0]).distance(firstStep);
		for (int i =1; i < starts.length; i++) {
			double newDis = nextPosition(position, starts[i]).distance(firstStep);
			if (newDis < dis) {
				dis = newDis;
				ind = i;
			}
		}
		return starts[ind];
	}
	
	/**
	 * Gives the next position of a transport following its position and its direction
	 * @param position the current position of the transport
	 * @param direction the direction of the transport
	 * @return the next position of the transport
	 */
	private static Point2D nextPosition (Point2D position, double direction) {
		int x,y;
		if (direction < 0) {
			x = 1;
		} else if (MathUtil.areEqual(direction,LogoEnvPLS.NORTH) || MathUtil.areEqual(direction, LogoEnvPLS.SOUTH)) {
			x = 0;
		} else {
			x = -1;
		}
		if ((direction >= LogoEnvPLS.NORTH_EAST) && (direction <= LogoEnvPLS.NORTH_WEST)) {
			y = 1;
		} else if (MathUtil.areEqual(direction,LogoEnvPLS.WEST) || MathUtil.areEqual(direction, LogoEnvPLS.EAST)) {
			y = 0;
		} else {
			y = -1;
		}
		Point2D res = new Point2D.Double(position.getX()+x,position.getY()+y);
		return res;
	}
}
