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
package fr.lgi2a.similar2logo.examples.transport;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.transport.model.Station;
import fr.lgi2a.similar2logo.examples.transport.model.TransportSimulationParameters;
import fr.lgi2a.similar2logo.examples.transport.model.agents.CarCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.CarDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.CarFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.CarsOnlyTransportReactionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.GeneratorDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.GeneratorFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.PersonCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.PersonDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.PersonFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.TrainCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.TramCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.TransportDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.TransportFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.TransportReactionModel;
import fr.lgi2a.similar2logo.examples.transport.osm.DataFromOSM;
import fr.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;

/**
 * Model for the transport simulation.
 * The environment is created from real data import from <a href="http://openstreetmap.fr/">Open Street Map</a>.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportSimulationModel extends LogoSimulationModel {
	
	/**
	 * The data extract from the OSM file.
	 */
	private DataFromOSM data;
	
	/**
	 * List of points where a transport can be created
	 */
	private Map<String,List<Point2D>> startingPointsForTransports;
	
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
	 * The different places where a car can start.
	 */
	private List<Point2D> startingPointsForCars;
	
	/**
	 * The parameters planning
	 */
	private TransportParametersPlanning planning;

	public TransportSimulationModel(LogoSimulationParameters parameters, String osmData, JSONObject parametersData, int startHour, 
			int secondStep, int horizontal, int vertical ) {
		super(parameters);
		this.data = new DataFromOSM(osmData);
		this.planning = new TransportParametersPlanning(startHour, secondStep, parametersData, horizontal, vertical);
		TransportSimulationParameters tsp = (TransportSimulationParameters) parameters;
		tsp.setSize(this.data.getHeight(), this.data.getWidth());
		startingPointsForTransports = new HashMap<>();
		startingPointsForTransports.put("Railway", new ArrayList<>());
		startingPointsForTransports.put("Tramway", new ArrayList<>());
		limits = new HashMap<>();
		limits.put("Street", new ArrayList<>());
		limits.put("Railway", new ArrayList<>());
		limits.put("Tramway", new ArrayList<>());
		stations = new HashMap<>();
		stations.put("Railway", new ArrayList<>());
		stations.put("Tramway", new ArrayList<>());
		startingPointsForCars = new ArrayList<>();
	}
	
	public TransportSimulationModel (LogoSimulationParameters parameters, String osmData, TransportParametersPlanning planning) {
		super (parameters);
		this.data = new DataFromOSM(osmData);
		this.planning = planning;
		TransportSimulationParameters tsp = (TransportSimulationParameters) parameters;
		tsp.setSize(this.data.getHeight(), this.data.getWidth());
		startingPointsForTransports = new HashMap<>();
		startingPointsForTransports.put("Railway", new ArrayList<>());
		startingPointsForTransports.put("Tramway", new ArrayList<>());
		limits = new HashMap<>();
		limits.put("Street", new ArrayList<>());
		limits.put("Railway", new ArrayList<>());
		limits.put("Tramway", new ArrayList<>());
		stations = new HashMap<>();
		stations.put("Railway", new ArrayList<>());
		stations.put("Tramway", new ArrayList<>());
		startingPointsForCars = new ArrayList<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		TransportSimulationParameters tsp = (TransportSimulationParameters) simulationParameters;
		AgentInitializationData aid = new AgentInitializationData();
		generateTransports("Railway", tsp, aid);
		generateTransports("Tramway", tsp, aid);
		generateCars(tsp, aid);
		generatePersons(tsp, aid);
		generateCreator(tsp, aid);
		return aid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected EnvironmentInitializationData generateEnvironment (ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		TransportSimulationParameters tsp = (TransportSimulationParameters) simulationParameters;
		//Creation of the environment with the good size.
		EnvironmentInitializationData eid = super.generateEnvironment(tsp, levels);
		LogoEnvPLS environment = (LogoEnvPLS) eid.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
		//We add the different elements
		this.buildWay(environment, this.data.getSecondaryRoads(), "Secondary");
		this.buildWay(environment, this.data.getTerciaryRoads(), "Tertiary");
		this.buildWay(environment, this.data.getResidentialRoads(), "Residential");
		this.buildWay(environment, this.data.getRailway(), "Railway");
		this.buildWay(environment, this.data.getTramway(), "Tramway");
		this.buildStations(environment, this.data.getStations(), "Railway", "Station");
		this.buildStations(environment, this.data.getTramStops(), "Tramway", "Tram_stop");
		return eid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters) {
		TransportSimulationParameters castedSimulationParameters = (TransportSimulationParameters) simulationParameters;
		ExtendedLevel logo;
		if (!castedSimulationParameters.carReactionOnly) {
			logo = new ExtendedLevel(
					castedSimulationParameters.getInitialTime(), 
					LogoSimulationLevelList.LOGO, 
					new PeriodicTimeModel( 
							1, 
							0, 
							castedSimulationParameters.getInitialTime()
							),
					new TransportReactionModel()
					);
		} else {
			logo = new ExtendedLevel(
					castedSimulationParameters.getInitialTime(), 
					LogoSimulationLevelList.LOGO, 
					new PeriodicTimeModel( 
							1, 
							0, 
							castedSimulationParameters.getInitialTime()
							),
					new CarsOnlyTransportReactionModel()
					);
		}
		List<ILevel> levelList = new LinkedList<ILevel>();
		levelList.add(logo);
		return levelList;	
	}
	
	/**
	 * Builds the ways
	 * @param lep the environment where build the ways
	 * @param ways the ways to build
	 * @param type the type of way
	 */
	protected void buildWay (LogoEnvPLS lep, List<List<String>> ways, String type) {
		for (List<String> list : ways) {
			for (String s : list) {
				Point2D pt = data.getCoordinates(s);
				if (inTheEnvironment(pt)) {
					if (type.equals("Secondary"))
						lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 3, "Street"));
					else if (type.equals("Tertiary"))
						lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 3, "Street"));
					else if (type.equals("Residential"))
						lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 5, "Street"));
					else
						lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 32, type));
					if (onEdge(pt)) {
						if (type.equals("Secondary") || type.equals("Tertiary") || type.equals("Residential"))
							limits.get("Street").add(pt);
						else
							limits.get(type).add(pt);
					}
				}
			}
		}
		linkPointsWay (ways, lep, type);
	}
	
	/**
	 * Builds the stations
	 * @param lep the environment where build the stations
	 * @param places the place where to build the stations
	 * @param type the type of transport associates at the station
	 * @param typeStop the type of station associates at the station
	 */
	protected void buildStations (LogoEnvPLS lep, List<String> places, String type, String typeStop) {
		for (String s : places) {
			Point2D pt = data.getCoordinates(s);
			if (inTheEnvironment(pt)) {
				lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 0, typeStop));
				int[][] disAccess = distanceToMark(pt, "Street", typeStop, lep);
				int[][] disPlatform = distanceToMark(pt, type, typeStop, lep);
				int x1 = 0,y1 = 0,x2=0,y2=0, x3 =0, y3=0;
				int minDisAccess = Integer.MAX_VALUE-1;
				int minDisPlaform = Integer.MAX_VALUE;
				int minDisExit = Integer.MAX_VALUE;
				for (int i =0; i < disAccess.length; i++) {
					for (int j= 0; j< disAccess[0].length; j++) {
						if (disAccess[i][j] < minDisAccess) {
							x3 = x1;
							y3 = y1;
							minDisExit = minDisAccess;
							x1 = i;
							y1 = j;
							minDisAccess = disAccess[i][j]; 
						} else if (disAccess[i][j] < minDisExit) {
							x3 = i;
							y3= j;
							minDisExit = disAccess[i][j];
						}
						if (disPlatform[i][j] < minDisPlaform) {
							x2 = i;
							y2 = j;
							minDisPlaform = disPlatform[i][j];
						}
					}
				}
				Point2D access = new Point2D.Double(pt.getX() + (x1-1)*minDisAccess, pt.getY() + (y1-1)*minDisAccess);
				Point2D platform = new Point2D.Double(pt.getX() + (x2-1)*minDisPlaform, pt.getY() +(y2-1)*minDisPlaform);
				Point2D exit = new Point2D.Double(pt.getX()+ (x3-1)*minDisExit, pt.getY() + (y3-1)*minDisExit);
				this.stations.get(type).add(new Station(access, exit, platform));
			}	
		}						
	}
	
	/**
	 * Generates a transport following its type
	 * @param type the type of the transport, "Railway" for a train, "Tramway" for a tram.
	 * @param tsp the transport simulation parameters.
	 * @param aid the agent initialization data.
	 */
	protected void generateTransports (String type, TransportSimulationParameters tsp, AgentInitializationData aid) {
		List<List<String>> list = null;
		int nbr = 0;
		if (type.equals("Railway")) {
			nbr = tsp.nbrTrains;
			list = this.data.getRailway();
		} else if (type.equals("Tramway")){
			nbr = tsp.nbrTramways;
			list = this.data.getTramway();
		}
		for (List<String> l : list) {
			for (String s : l) {
				Point2D pt = data.getCoordinates(s);
				if (inTheEnvironment(pt) && !startingPointsForTransports.get(type).contains(pt)) {
					startingPointsForTransports.get(type).add(pt);
				}
			}
		}
		//We add transport while we can
		for (int i = 0; i < nbr; i++) {
			try {
				double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.WEST,LogoEnvPLS.SOUTH,LogoEnvPLS.NORTH};
				Random r = new Random();
				Point2D position = this.findPlaceForTransport(type);
				if (type.equals("Railway")) {
					aid.getAgents().add(TransportFactory.generate(
						new TurtlePerceptionModel(
								Math.sqrt(2),Math.PI,true,true,true
							),
							new TransportDecisionModel(type, limits.get(type), stations.get(type), 
									data.getHeight(), data.getWidth(), tsp.speedFrequenceTrain),
							TrainCategory.CATEGORY,
							starts[r.nextInt(starts.length)] ,
							0 ,
							0,
							position.getX(),
							position.getY(),
							tsp.trainCapacity,
							tsp.speedFrequenceTrain
						));
				} else if (type.equals("Tramway")) {
					aid.getAgents().add(TransportFactory.generate(
							new TurtlePerceptionModel(
									Math.sqrt(2),Math.PI,true,true,true
								),
								new TransportDecisionModel(type, limits.get(type), stations.get(type), 
										data.getHeight(), data.getWidth(), tsp.speedFrequencyTram),
								TramCategory.CATEGORY,
								starts[r.nextInt(starts.length)] ,
								0 ,
								0,
								position.getX(),
								position.getY(),
								tsp.tramwayCapacity,
								tsp.speedFrequencyTram
							));
				}
			} catch (Exception e) {
				//Does nothing, we don't add transport
			}
		}
	}
	
	/**
	 * Generates the car in the simulation
	 * @param tsp the transport simulation parameters
	 * @param aid the agents at the beginning
	 */
	protected void generateCars (TransportSimulationParameters tsp, AgentInitializationData aid) {
		int nbr = tsp.nbrCars;
		for (List<String> list : this.data.getHighway()) {
			for (String s : list) {
				Point2D pt = data.getCoordinates(s);
				if (inTheEnvironment(pt) && !startingPointsForCars.contains(pt)) {
					startingPointsForCars.add(pt);
				}
			}
		}
		List<Integer> aPrendre = new ArrayList<>();
		for (int i=0; i < startingPointsForCars.size(); i++) {
			aPrendre.add(i);
		}
		// We unit the list of the station;
		List<Station> stop = new ArrayList<>();
		for (Station s : stations.get("Railway")) {
			stop.add(s);
		}
		for (Station s : stations.get("Tramway")) {
			stop.add(s);
		}
		for (int i = 0; i < nbr; i++) {
			try {
				double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
						LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
				Random r = new Random();
				int p = aPrendre.remove(r.nextInt(aPrendre.size()));
				Point2D position = startingPointsForCars.get(p);			
					aid.getAgents().add(CarFactory.generate(
						new TurtlePerceptionModel(
								Math.sqrt(2),Math.PI,true,true,true
							),
							new CarDecisionModel(stop,  data.getHeight(), data.getWidth(), planning),
							CarCategory.CATEGORY,
							starts[r.nextInt(starts.length)] ,
							0 ,
							0,
							position.getX(),
							position.getY(),
							tsp.speedFrequencyCar,
							tsp.carCapacity
						));
			} catch (Exception e) {
				//Does nothing, we don't add train
			}
		}
	}
	
	/**
	 * Generates the persons in the simulation
	 * @param tsp the transport simulation parameters
	 * @param aid the agents at the beginning
	 */
	protected void generatePersons (TransportSimulationParameters tsp, AgentInitializationData aid) {
		int nbr = tsp.nbrPersons;
		// We unit the list of the station;
		List<Station> stop = new ArrayList<>();
		for (Station s : stations.get("Railway")) {
			stop.add(s);
		}
		for (Station s : stations.get("Tramway")) {
			stop.add(s);
		}
		for (int i = 0; i < nbr; i++) {
			try {
				double[] starts = {LogoEnvPLS.EAST,LogoEnvPLS.NORTH,LogoEnvPLS.NORTH_EAST,LogoEnvPLS.NORTH_WEST,
						LogoEnvPLS.SOUTH, LogoEnvPLS.SOUTH_EAST, LogoEnvPLS.SOUTH_WEST, LogoEnvPLS.WEST};
				Random r = new Random();
				Point2D position = startingPointsForCars.get(r.nextInt(startingPointsForCars.size()));			
					aid.getAgents().add(PersonFactory.generate(
						new TurtlePerceptionModel(
								Math.sqrt(2),Math.PI,true,true,true
							),
							new PersonDecisionModel(stop, data.getHeight(), data.getWidth(), planning),
							PersonCategory.CATEGORY,
							starts[r.nextInt(starts.length)] ,
							0 ,
							0,
							position.getX(),
							position.getY(),
							tsp.speedFrequencyPerson
						));
			} catch (Exception e) {
				//Does nothing, we don't add train
			}
		}
	}
	
	/**
	 * Generates the creator of the simulation
	 * @param tsp the transport simulation parameter
	 * @param aid the agents initialization data
	 */
	protected void generateCreator (TransportSimulationParameters tsp, AgentInitializationData aid) {
		aid.getAgents().add(GeneratorFactory.generate(new GeneratorDecisionModel
				(data.getHeight(), data.getWidth(), limits, stations, startingPointsForCars, planning)));
	}
	
	/**
	 * Links the ways
	 * @param pts the points to link
	 * @param lep the environment where to build the ways
	 * @param type the type of way
	 */
	protected void linkPointsWay (List<List<String>> pts, LogoEnvPLS lep, String type) {
		for (List<String> liste : pts) {
			for (int i=0; i < liste.size() -1; i++) {
				Point2D ori = data.getCoordinates(liste.get(i));
				Point2D des = data.getCoordinates(liste.get(i+1));
				if (!(!inTheEnvironment(ori) && !(inTheEnvironment(des))))
					printWayBetweenTwoPoints(ori, des, lep,type);
			}
		}
	}
	
	/**
	 * Prints the way between two points.
	 * Do nothing if isn't in the environment
	 * @param ori the origin of the way
	 * @param des the end of the way
	 * @param lep the environment where to build the way
	 * @param type the type of way
	 */
	protected void printWayBetweenTwoPoints (Point2D ori, Point2D des, LogoEnvPLS lep, String type) {
		if (!ori.equals(des)) {
			//we test all the 8 directions for knowing what is the best way
			Point2D nextPosition = ori;
			double bestDistance = Double.MAX_VALUE - 1;
			Point2D secondNextPosition = ori;
			double secondBestDistance = Double.MAX_VALUE;
			for (int i = -1 ; i <=1; i++) {
				for (int j= -1; j <= 1; j++) {
					if (!(i ==0 && j==0)) {
						Point2D testPoint = new Point2D.Double(ori.getX()+i, ori.getY()+j);
						double distance = Point2D.distance(testPoint.getX(), testPoint.getY(), des.getX(), des.getY());
						if (distance < bestDistance) {
							secondNextPosition = nextPosition;
							secondBestDistance = bestDistance;
							nextPosition = testPoint;
							bestDistance = distance;
						} else if (distance < secondBestDistance) {
							secondNextPosition = testPoint;
							secondBestDistance = distance;
						}
					}
				}
			}
			Random r = new Random();
			if (r.nextInt(5) < -1) {
				if ((secondNextPosition.getY() >= 0) && (secondNextPosition.getY() < lep.getHeight()) && 
						(secondNextPosition.getX() >= 0) && (secondNextPosition.getX() < lep.getWidth())) {
					if (type.equals("Secondary"))
						lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() )
						.add(new Mark<Double>(secondNextPosition, (double) 2, "Street"));
					else if (type.equals("Tertiary"))
						lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() )
						.add(new Mark<Double>(secondNextPosition, (double) 3, "Street"));
					else if (type.equals("Residential"))
						lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() )
						.add(new Mark<Double>(secondNextPosition, (double) 4, "Street"));
					else
						lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() )
						.add(new Mark<Double>(secondNextPosition, (double) 0, type));
					if (onEdge(secondNextPosition)) {
						if (type.equals("Secondary") || type.equals("Tertiary") || type.equals("Residential"))
							limits.get("Street").add(secondNextPosition);
						else
							limits.get(type).add(secondNextPosition);
					}	
					if ((type.equals("Secondary") || type.equals("Tertiary") || type.equals("Residential")) 
						&& !startingPointsForCars.contains(secondNextPosition)) 
						startingPointsForCars.add(secondNextPosition);
				}
				printWayBetweenTwoPoints(secondNextPosition, des, lep, type);
			} else {
				if ((nextPosition.getY() >= 0) && (nextPosition.getY() < lep.getHeight()) && 
						(nextPosition.getX() >= 0) && (nextPosition.getX() < lep.getWidth())) {
					if (type.equals("Secondary"))
						lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() )
						.add(new Mark<Double>(nextPosition, (double) 2, "Street"));
					else if (type.equals("Tertiary"))
						lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() )
						.add(new Mark<Double>(nextPosition, (double) 3, "Street"));
					else if (type.equals("Residential"))
						lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() )
						.add(new Mark<Double>(nextPosition, (double) 4, "Street"));
					else
						lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() )
						.add(new Mark<Double>(nextPosition, (double) 0, type));
					if (onEdge(nextPosition)) {
						if (type.equals("Secondary") || type.equals("Tertiary") || type.equals("Residential"))
							limits.get("Street").add(nextPosition);
						else
							limits.get(type).add(nextPosition);
					}
					if ((type.equals("Secondary") || type.equals("Tertiary") || type.equals("Residential")) 
						&& !startingPointsForCars.contains(nextPosition)) 
						startingPointsForCars.add(nextPosition);
				}
				printWayBetweenTwoPoints(nextPosition, des, lep,type);
			}
		}
	}
	
	/**
	 * Gives a place to put a vehicle.
	 * Removes the place from the stratingPointsForTransport list
	 * @param type the type of way where the transport can go
	 * @return Point2D where put a vehicle
	 * @throws Exception if there is no more place available
	 */
	protected Point2D findPlaceForTransport (String type) throws Exception {
		if (startingPointsForTransports.get(type).isEmpty()) {
			throw new Exception ("No more place for a transport of type "+type+".");
		} else {
			Random r = new Random ();
			Point2D res = startingPointsForTransports.get(type).remove(r.nextInt(startingPointsForTransports.get(type).size()));
			return res;
		}
	}
	
	/**
	 * Indicates if a point is in the environment
	 * @param pt a Point2D
	 * @return true if the point is in the limits of the environment, else false
	 */
	protected boolean inTheEnvironment (Point2D pt) {
		return ((pt.getX() >= 0) && (pt.getY() >= 0) && (pt.getX() < data.getWidth()) && (pt.getY() < data.getHeight()));
	}
	
	/**
	 * Indicates if a point is on the edge ot the environment
	 * @param pt a Point2D
	 * @return true if the is on the edge of the edge of the environment, else false
	 */
	protected boolean onEdge (Point2D pt) {
		return ((pt.getX() == 0) || (pt.getY() == 0) || (pt.getX() == (data.getWidth()-1)) || (pt.getY() == (data.getHeight()-1)));
	}

	/**
	 * Gives the distance between a station/stop and a way
	 * @param pt the point where is the station/stop
	 * @param typeRoad the type of road to search
	 * @param typeStop the type of the station
	 * @param lep the logo environment pls
	 * @return int[][] a table with the distance to the way for the Moore's neighborhood
	 */
	protected int[][] distanceToMark (Point2D pt, String typeRoad, String typeStop, LogoEnvPLS lep) {
		int[][] res = new int[3][3];
		boolean[][] fatto = new boolean[3][3];
		for (int i=0; i <= 2; i++) {
			for (int j=0; j <= 2; j++) {
				res[i][j] = Integer.MAX_VALUE;
				fatto[i][j] = false;
			}
		}
		fatto[1][1] = true;
		@SuppressWarnings("rawtypes")
		Iterator<Mark> ite = lep.getMarksAt((int) pt.getX(), (int) pt.getY()).iterator();
		while (ite.hasNext()) {
			String category = ite.next().getCategory();
			if (category.equals(typeRoad)) {
				res[1][1] = 0;
				return res;
			}
		}
		int done = 0;
		int dis = 1;
		while (done != 8) {
			for (int i=0; i <= 2; i++) {
				for (int j=0; j <= 2; j++) {
					if (!fatto[i][j]) {
						Point2D nextPos = new Point2D.Double(pt.getX() +(i-1)*dis,pt.getY() +(j-1)*dis);
						if (!inTheEnvironment(nextPos)) {
							fatto[i][j] = true;
							done++;
						} else {
							ite = lep.getMarksAt((int) pt.getX() +(i-1)*dis, (int) pt.getY() +(j-1)*dis).iterator();
							while (ite.hasNext() && !fatto[i][j]) {
								if (ite.next().getCategory().equals(typeRoad)) {
									res[i][j] = dis;
									fatto[i][j] = true;
									done++;
								}
							}
						}
					}
				}
			}
			dis++;
		}
		return res;
	}

}
