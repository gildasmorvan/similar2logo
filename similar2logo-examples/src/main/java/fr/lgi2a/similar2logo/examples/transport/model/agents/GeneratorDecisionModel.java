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
import java.util.Map;
import java.util.Random;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar2logo.examples.transport.model.Station;
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
	 * Probability to create a person, a car, tram and a train
	 */
	private double probaCreatePerson, probaCreateCar, probaCreateTram, probaCreateTrain;
	
	/**
	 * The height and the with of the environment
	 */
	private int height, width;
	
	/**
	 * Limits of each type of way.
	 * The key is the type of way.
	 * The value is a list with all the points limits for this type of way
	 */
	private Map<String,List<Point2D>> limits;
	
	/**
	 * The list of stations/stops for each type of transport.
	 */
	private Map<String,List<Station>> stations;
	
	/**
	 * The capacities of the car, train and the tram.
	 */
	private int carCapacity, trainCapacity, tramCapacity;
	
	/**
	 * The probability to take a transport for a car
	 */
	private double probaTakeTransport;
	
	/**
	 * The speed frequency of the transport and person
	 */
	private int speedFrequencyPerson, speedFrequencyCar, speedFrequencyTram, speedFrequencyTrain;
	
	/**
	 * The probability for a car to be at home and the probability to leave home.
	 */
	private double probaBeAtHome, probaLeaveHome;
	
	/**
	 * All the street positions.
	 */
	private List<Point2D> streets;
	
	/**
	 * Probability for a person to take his car or for a person to leave his car.
	 */
	private double probaBecomeACar, probaBecomeAPerson;

	public GeneratorDecisionModel(double probaPerson, double probaCar, double probaTram, double probaTrain, int carCapacity,
			int tramCapacity, int trainCapacity, int height, int width, Map<String,List<Point2D>> limits, Map<String,List<Station>> stations, 
			List<Point2D> streets, double probaTakeTransport, int sfperson, int sfcar, int sftram, int sftrain, double probaAtHome, 
			double probaLeaveHome, double probaBecomeACar, double probaBecomeAPerson) {
		super(LogoSimulationLevelList.LOGO);
		this.probaCreatePerson = probaPerson;
		this.probaCreateTram = probaTram;
		this.probaCreateCar = probaCar;
		this.probaCreateTrain = probaTrain;
		this.carCapacity = carCapacity;
		this.trainCapacity = trainCapacity;
		this.tramCapacity = tramCapacity;
		this.height = height;
		this.width = width;
		this.limits = limits;
		this.stations = stations;
		this.probaTakeTransport = probaTakeTransport;
		this.speedFrequencyPerson = sfperson;
		this.speedFrequencyCar = sfcar;
		this.speedFrequencyTram = sftram;
		this.speedFrequencyTrain = sftrain;
		this.probaBeAtHome = probaAtHome;
		this.probaLeaveHome = probaLeaveHome;
		this.streets = streets;
		this.probaBecomeACar = probaBecomeACar;
		this.probaBecomeAPerson = probaBecomeAPerson;
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
			if (RandomValueFactory.getStrategy().randomDouble() <= probaCreateCar) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generateCarToAddOnLimits(limits.get("Street").get(i))));
			}
			if (RandomValueFactory.getStrategy().randomDouble() <= probaCreatePerson) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generatePersonToAddOnLimits(limits.get("Street").get(i))));
			}
		}
		//adds the tramway at the limits
		for (int i = 0; i < limits.get("Tramway").size(); i++) {
			if (RandomValueFactory.getStrategy().randomDouble() <= probaCreateTram) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generateTramToAddOnLimits(limits.get("Tramway").get(i))));
			}
		}
		//Adds the trains at the limits
		for (int i = 0; i < limits.get("Railway").size(); i++) {
			if (RandomValueFactory.getStrategy().randomDouble() <= probaCreateTrain) {
				producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
						generateTrainToAddOnLimits(limits.get("Railway").get(i))));
			}
		}
		//The people go out from the stations
		for (String s : stations.keySet()) {
			for (Station st : stations.get(s)) {
				if (!st.noWaitingPeopleToGoOut()) {
					st.removeWaitingPeopleGoOut();
					producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
							generateCarToAdd(st.getAccess())));
				}
				if (timeLowerBound.getIdentifier() % speedFrequencyPerson == 0) {
					int sortie = r.nextInt(4);
					while (sortie-- != 0 && !st.noWaitingPeopleToGoOut()) {
						st.removeWaitingPeopleGoOut();
						producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound,
								generatePersonToAdd(st.getAccess())));
					}
				}
			}
		}
		//People leave their home
		for (int i =0; i < streets.size(); i++) {
			if (RandomValueFactory.getStrategy().randomDouble() <= probaLeaveHome) {
				if (r.nextInt(3) > 0)
					producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
							generateCarToAdd(streets.get(i))));
				else
					producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound,
							generatePersonToAdd(streets.get(i))));
			}	
		}
		
	}
	
	/**
	 * Generate a person to add in the simulation
	 * @param position the position to put the person
	 * @return a person to insert in the simulation
	 */
	private IAgent4Engine generatePersonToAdd (Point2D position) {
		// We unit the list of the station;
		List<Station> stop = new ArrayList<>();
		for (Station s : stations.get("Railway")) {
			stop.add(s);
		}
		for (Station s : stations.get("Tramway")) {
			stop.add(s);
		}
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
				LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
		Random r = new Random();
		return PersonFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new PersonDecisionModel(probaTakeTransport, stop, height, width, speedFrequencyPerson, speedFrequencyCar, carCapacity,
							probaBeAtHome, probaBecomeACar, probaBecomeACar),
					PersonCategory.CATEGORY,
					starts[r.nextInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					speedFrequencyCar
				);
	}
	
	/**
	 * Generate a car to add in the simulation
	 * @param position the position to put the car
	 * @return a car to insert in the simulation
	 */
	private IAgent4Engine generateCarToAdd (Point2D position) {
		// We unit the list of the station;
		List<Station> stop = new ArrayList<>();
		for (Station s : stations.get("Railway")) {
			stop.add(s);
		}
		for (Station s : stations.get("Tramway")) {
			stop.add(s);
		}
		double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
				LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
		Random r = new Random();
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new CarDecisionModel(probaTakeTransport, stop, height, width, speedFrequencyCar, carCapacity, speedFrequencyPerson,
							probaBeAtHome, probaBecomeAPerson, probaBecomeACar),
					CarCategory.CATEGORY,
					starts[r.nextInt(starts.length)] ,
					0 ,
					0,
					position.getX(),
					position.getY(),
					speedFrequencyCar,
					carCapacity
				);
	}
	
	/**
	 * Generate a person to add in the simulation
	 * @param position the position to put the person to create
	 * @return a person to insert in the simulation
	 */
	private IAgent4Engine generatePersonToAddOnLimits (Point2D position) {
		// We unit the list of the station;
		List<Station> stop = new ArrayList<>();
		for (Station s : stations.get("Railway")) {
			stop.add(s);
		}
		for (Station s : stations.get("Tramway")) {
			stop.add(s);
		}
		Point2D np = startPosition(position);
		return PersonFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new PersonDecisionModel(probaTakeTransport, stop, height, width, speedFrequencyPerson, speedFrequencyCar, carCapacity,
							probaBeAtHome, probaBecomeACar, probaBecomeAPerson),
					PersonCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					speedFrequencyPerson
				);
	}
	
	/**
	 * Generate a car to add in the simulation
	 * @param position the position to put the car to create
	 * @return a car to insert in the simulation
	 */
	private IAgent4Engine generateCarToAddOnLimits (Point2D position) {
		// We unit the list of the station;
		List<Station> stop = new ArrayList<>();
		for (Station s : stations.get("Railway")) {
			stop.add(s);
		}
		for (Station s : stations.get("Tramway")) {
			stop.add(s);
		}
		Point2D np = startPosition(position);
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new CarDecisionModel(probaTakeTransport, stop, height, width, speedFrequencyCar, carCapacity, speedFrequencyPerson,
							probaBeAtHome, probaBecomeAPerson, probaBecomeACar),
					CarCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					speedFrequencyCar,
					carCapacity
				);
	}
	
	/**
	 * Generates a tram to add in the simulation
	 * @param position the position to the tram to create
	 * @return a tram to insert in the simulation
	 */
	private IAgent4Engine generateTramToAddOnLimits (Point2D position) {
		Random r = new Random();
		Point2D np = startPosition(position);
			 return TransportFactory.generate(
					new TurtlePerceptionModel(
							Math.sqrt(2),Math.PI,true,true,true
						),
						new TransportDecisionModel("Tramway", limits.get("Tramway"), stations.get("Tramway"), 
								height, width, speedFrequencyTram),
						TramCategory.CATEGORY,
						startAngle(np) ,
						0 ,
						0,
						np.getX(),
						np.getY(),
						r.nextInt(tramCapacity+1),
						speedFrequencyTram
					);
		}
	
	/**
	 * Generates a train to add in the simulation
	 * @param position the position of the train to create
	 * @return a train to insert in the simulation
	 */
	private IAgent4Engine generateTrainToAddOnLimits (Point2D position) {
		Random r = new Random ();
		Point2D np = startPosition(position);
		return TransportFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new TransportDecisionModel("Railway", limits.get("Railway"), stations.get("Railway"), 
							height, width, speedFrequencyTrain),
					TrainCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					r.nextInt(trainCapacity+1),
					speedFrequencyTrain
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
		else if (position.getX() == (width -1)) return new Point2D.Double(position.getX()-1,position.getY());
		else return new Point2D.Double(position.getX(),position.getY()-1);
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
		} else if (position.getX() == (width -1)) 
			return LogoEnvPLS.WEST;
		else
			return LogoEnvPLS.SOUTH;
	}

}
