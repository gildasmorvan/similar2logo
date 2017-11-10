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
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar2logo.examples.transport.model.places.Station;
import fr.lgi2a.similar2logo.examples.transport.model.places.World;
import fr.lgi2a.similar2logo.examples.transport.parameters.DestinationGenerator;
import fr.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * Agent that creates new agents of every type.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
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
		Random r = new Random ();
		//Adds person and car on the limit
		for (int i =0; i < limits.get("Street").size(); i++) {
			Point2D p = limits.get("Street").get(i);
			TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			if (RandomValueFactory.getStrategy().randomDouble() <= tsp.probaCreateCar) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generateCarToAddOnLimits(timeUpperBound, p,tsp)));
			}
			if (RandomValueFactory.getStrategy().randomDouble() <= tsp.probaCreatePerson) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generatePersonToAddOnLimits(timeUpperBound, p,tsp)));
			}
		}
		//adds the tramway at the limits
		for (int i = 0; i < limits.get("Tramway").size(); i++) {
			Point2D p = limits.get("Tramway").get(i);
			TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			if (timeLowerBound.getIdentifier() % tsp.creationFrequencyTram == 0) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generateTramToAddOnLimits(p,tsp, timeUpperBound)));
			}
		}
		//Adds the trains at the limits
		Point2D trainLimit = limits.get("Railway").get(r.nextInt(limits.get("Railway").size()));
		TransportSimulationParameters tspTrain = planning.getParameters(timeUpperBound, trainLimit, world.getWidth(), world.getHeight());
		if (timeLowerBound.getIdentifier() % tspTrain.creationFrequencyTrain == 0) {
			producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
					generateTrainToAddOnLimits(trainLimit,tspTrain, timeUpperBound)));
		}
		//The people go out from the stations
		for (Station st : world.getStations()) {
			Point2D p = st.getExit();
			TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			if (st.getType().equals("Railway")) {
				if (!st.nooneWantsToGoOut() && 
						timeLowerBound.getIdentifier() % planning.getStep()== 0) {
					ExtendedAgent ea = st.getPersonsWantingToGoOut().remove(0);
					PersonPLS person = (PersonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
					producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
							createCarFromPerson(person, p, tsp)));
				}
			}
			if (timeLowerBound.getIdentifier() % tsp.speedFrequencyPerson == 0) {
				int sortie = r.nextInt(5);
				while (sortie-- != 0 && !st.nooneWantsToGoOut()) {
					ExtendedAgent ea = st.getPersonsWantingToGoOut().remove(0);
					PersonPLS person = (PersonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
					person.setMove(true);
					producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound,
							recreatePerson(person, p, tsp)));
				}
			}
		}
		//People in the leisures
		for (int i=0; i < world.getLeisures().size(); i++) {
			Point2D p = world.getLeisures().get(i).getPosition();
			TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			List<PersonPLS> persons = world.getLeisures().get(i).getWaitingPeople(timeLowerBound);
			if (persons.size() > 0 && 
					timeLowerBound.getIdentifier() % planning.getStep()== 0) {
				PersonPLS person = persons.remove(0);
			producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						createCarFromPerson(person, p, tsp)));
			}
			if (timeLowerBound.getIdentifier() % tsp.speedFrequencyPerson == 0) {
				int sortie = r.nextInt(5);
				while (sortie-- != 0 && persons.size() >0) {
						PersonPLS person = persons.remove(0);
						person.setMove(true);
						producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound,
						recreatePerson(person, p, tsp)));
				}
			}
		}
		//People leave their home
		for (int i =0; i < world.getRoads().size(); i++) {
			Point2D p = world.getRoads().get(i);
			TransportSimulationParameters tsp = planning.getParameters(timeUpperBound, p, world.getWidth(), world.getHeight());
			if (RandomValueFactory.getStrategy().randomDouble() <= tsp.probaLeaveHome) {
				if (r.nextInt(3) > 0) {
					producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
							generateCarToAdd(timeUpperBound, p,tsp)));
				}
				else {
					producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound,
							generatePersonToAdd(timeUpperBound, p,tsp)));
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
		Random r = new Random();
		Point2D destination = destinationGenerator.getADestination(sts, position);
		return PersonFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new PersonDecisionModel(sts, world, planning, destination, destinationGenerator, 
							world.getGraph().wayToGo(position, destination)),
					PersonCategory.CATEGORY,
					starts[r.nextInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyPerson
				);
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
		Random r = new Random();
		Point2D destination = destinationGenerator.getADestination(sts, position);
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new CarDecisionModel(world, sts, planning, destination,
							destinationGenerator, world.getGraph().wayToGo(position, destination)),
					CarCategory.CATEGORY,
					starts[r.nextInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyCar,
					tsp.carCapacity
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
		Point2D destination = destinationGenerator.getADestination(sts, np);
		return PersonFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new PersonDecisionModel(sts, world, planning, destination, 
							destinationGenerator, world.getGraph().wayToGo(np, destination)),
					PersonCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					tsp.speedFrequencyPerson
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
		Point2D destination = destinationGenerator.getADestination(sts, np);
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new CarDecisionModel(world, sts, planning, destination, destinationGenerator,
							world.getGraph().wayToGo(np, destination)),
					CarCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					tsp.speedFrequencyCar,
					tsp.carCapacity
				);
	}
	
	/**
	 * Generates a tram to add in the simulation
	 * @param position the position to the tram to create
	 * @param tsp the transport simulation parameters
	 * @return a tram to insert in the simulation
	 */
	private IAgent4Engine generateTramToAddOnLimits (Point2D position, TransportSimulationParameters tsp, SimulationTimeStamp sts) {
		Random r = new Random();
		Point2D np = startPosition(position), des = null;
		boolean done = false;
		while (!done) {
			des = limits.get("Tramway").get(r.nextInt(limits.get("Tramway").size()));
			if (!des.equals(np)) done = true;
		}
		ExtendedAgent ea =  TransportFactory.generate(
					new TurtlePerceptionModel(
							Math.sqrt(2),Math.PI,true,true,true
						),
						new TransportDecisionModel(des, world, "Tramway", limits.get("Tramway"), tsp.speedFrequencyTram),
						TramCategory.CATEGORY,
						startAngle(np) ,
						0 ,
						0,
						np.getX(),
						np.getY(),
						r.nextInt(tsp.tramwayCapacity+1),
						tsp.speedFrequencyTram
					);
		TransportPLS tramPLS = (TransportPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
		for (int j= 0; j < r.nextInt(tramPLS.getMaxCapacity()); j++) {
			Point2D destination = destinationGenerator.getDestinationInTransport(sts, position, "Tramway");
			List<Point2D> way = world.getGraph().wayToGoInTransport(position, destination, "Tramway");
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
				tsp.speedFrequencyPerson
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
		Random r = new Random ();
		Point2D np = startPosition(position), des = null;
		boolean done = false;
		while (!done) {
			des = limits.get("Railway").get(r.nextInt(limits.get("Railway").size()));
			if (!des.equals(np)) done = true;
		}
		ExtendedAgent ea = TransportFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new TransportDecisionModel(des, world, "Railway", limits.get("Railway"), tsp.speedFrequenceTrain),
					TrainCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					r.nextInt(tsp.trainCapacity+1),
					tsp.speedFrequenceTrain
				);
		TransportPLS trainPLS = (TransportPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
		for (int j= 0; j < r.nextInt(trainPLS.getMaxCapacity()); j++) {
			Point2D destination = destinationGenerator.getDestinationInTransport(sts, position, "Railway");
			List<Point2D> way = world.getGraph().wayToGoInTransport(position, destination, "Railway");
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
				tsp.speedFrequencyPerson
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
		Random r = new Random();
		return PersonFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					pdm,
					PersonCategory.CATEGORY,
					starts[r.nextInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyPerson
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
		Random r = new Random();
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					pdm.convertInCarDecisionMode(),
					CarCategory.CATEGORY,
					starts[r.nextInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					tsp.speedFrequencyCar,
					tsp.carCapacity
				);
	}
	
	/**
	 * Gives a position where put a new car
	 * @param position on the edge of the world
	 * @return the position where put the car
	 */
	private Point2D startPosition (Point2D position) {
		if (position.getX() == 0) return new Point2D.Double(position.getX()+1,position.getY());
		else if (position.getY() == 0) return new Point2D.Double(position.getX(),position.getY()+1);
		else if (position.getX() == (world.getWidth() -1)) return new Point2D.Double(position.getX(),position.getY());
		else return new Point2D.Double(position.getX(),position.getY());
	}
	
	/**
	 * Gives the angle to give to the new car following its position
	 * @param position the next position of the new car
	 * @return the angle which the car starts
	 */
	private double startAngle (Point2D position) {
		if (position.getX() == 1) {
			return LogoEnvPLS.EAST;
		} else if (position.getY() == 1) {
			return LogoEnvPLS.NORTH;
		} else if (position.getX() == (world.getWidth() -1)) 
			return LogoEnvPLS.WEST;
		else
			return LogoEnvPLS.SOUTH;
	}

}
