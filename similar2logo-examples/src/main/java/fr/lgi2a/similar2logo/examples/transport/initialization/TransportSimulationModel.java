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
package fr.lgi2a.similar2logo.examples.transport.initialization;

import static fr.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;

import java.awt.geom.Point2D;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.transport.model.TransportReactionModel;
import fr.lgi2a.similar2logo.examples.transport.model.places.AbstractLeisure;
import fr.lgi2a.similar2logo.examples.transport.model.places.BusLine;
import fr.lgi2a.similar2logo.examples.transport.model.places.Station;
import fr.lgi2a.similar2logo.examples.transport.model.places.World;
import fr.lgi2a.similar2logo.examples.transport.osm.DataFromOSM;
import fr.lgi2a.similar2logo.examples.transport.osm.InterestPointsOSM;
import fr.lgi2a.similar2logo.examples.transport.osm.roadsgraph.RoadEdge;
import fr.lgi2a.similar2logo.examples.transport.osm.roadsgraph.RoadGraph;
import fr.lgi2a.similar2logo.examples.transport.osm.roadsgraph.RoadNode;
import fr.lgi2a.similar2logo.examples.transport.parameters.DestinationGenerator;
import fr.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.lgi2a.similar2logo.examples.transport.time.Clock;
import fr.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Model for the transport simulation.
 * The environment is created from real data import from <a href="http://openstreetmap.fr/">Open Street Map</a>.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class TransportSimulationModel extends AbstractLogoSimulationModel {
	
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
	 * The list of stations/stops.
	 */
	private List<Station> stations;
	
	/**
	 * The leisure place of the map
	 */
	private List<AbstractLeisure> leisures;
	
	/**
	 * The different places where a car can start.
	 */
	private List<Point2D> startingPointsForCars;
	
	/**
	 * The parameters planning
	 */
	private TransportParametersPlanning planning;
	
	/**
	 * The graph of the roads.
	 */
	private RoadGraph graph;
	
	/**
	 * The destination generator for the cars and the persons.
	 */
	private DestinationGenerator destinationGenerator;
	
	/**
	 * The world of the map with all the usefull elements
	 */
	private World world;
	
	/**
	 * The clock of the simulation
	 */
	private Clock clock;

	public TransportSimulationModel(
		LogoSimulationParameters parameters,
		InputStream osmData,
		JSONObject parametersData,
		int startHour, 
		int secondStep,int horizontal,
		int vertical
	) {
		super(parameters);
		this.data = new DataFromOSM(osmData);
		this.planning = new TransportParametersPlanning(startHour, secondStep, parametersData, horizontal, vertical);
		TransportSimulationParameters tsp = (TransportSimulationParameters) parameters;
		tsp.setSize(this.data.getHeight(), this.data.getWidth());
		startingPointsForTransports = new HashMap<>();
		startingPointsForTransports.put(RAILWAY, new ArrayList<>());
		startingPointsForTransports.put(TRAMWAY, new ArrayList<>());
		limits = new HashMap<>();
		limits.put(STREET, new ArrayList<>());
		limits.put(RAILWAY, new ArrayList<>());
		limits.put(TRAMWAY, new ArrayList<>());
		limits.put(BUSWAY, new ArrayList<>());
		stations = new ArrayList<>();
		startingPointsForCars = new ArrayList<>();
		this.graph = new RoadGraph(data.getHeight(), data.getWidth());
		this.world = new World();
		this.clock = new Clock(startHour, secondStep);
	}
	
	public TransportSimulationModel (
		LogoSimulationParameters parameters,
		InputStream osmData,
		TransportParametersPlanning planning
	) {
		super (parameters);
		this.data = new DataFromOSM(osmData);
		this.planning = planning;
		TransportSimulationParameters tsp = (TransportSimulationParameters) parameters;
		tsp.setSize(this.data.getHeight(), this.data.getWidth());
		startingPointsForTransports = new HashMap<>();
		startingPointsForTransports.put(RAILWAY, new ArrayList<>());
		startingPointsForTransports.put(TRAMWAY, new ArrayList<>());
		limits = new HashMap<>();
		limits.put(STREET, new ArrayList<>());
		limits.put(RAILWAY, new ArrayList<>());
		limits.put(TRAMWAY, new ArrayList<>());
		limits.put(BUSWAY, new ArrayList<>());
		stations = new ArrayList<>();
		startingPointsForCars = new ArrayList<>();
		this.graph = new RoadGraph(data.getHeight(), data.getWidth());
		this.world = new World();
		this.clock = planning.getClock();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(
		ISimulationParameters simulationParameters,
		Map<LevelIdentifier,ILevel> levels
	) {
		TransportSimulationParameters tsp = (TransportSimulationParameters) simulationParameters;
		AgentInitializationData aid = new AgentInitializationData();
		AgentInitialization agentInitialization = new AgentInitialization(this);
		agentInitialization.generateTransports(RAILWAY, tsp, aid);
		agentInitialization.generateTransports(TRAMWAY, tsp, aid);
		agentInitialization.generateCars(tsp, aid);
		agentInitialization.generateBuses(tsp, aid);
		agentInitialization.generateBikes(tsp, aid);
		agentInitialization.generatePersons(tsp, aid);
		agentInitialization.generateCreator(tsp, aid);
		return aid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EnvironmentInitializationData generateEnvironment (
		ISimulationParameters simulationParameters,
		Map<LevelIdentifier, ILevel> levels
	) {
		TransportSimulationParameters tsp = (TransportSimulationParameters) simulationParameters;
		//Creation of the environment with the good size.
		EnvironmentInitializationData eid = super.generateEnvironment(tsp, levels);
		LogoEnvPLS environment = (LogoEnvPLS) eid.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
		NetworkInitialization networkInitialization = new NetworkInitialization(this);
		//We add the different elements
		networkInitialization.buildWay(environment, this.data.getSecondaryRoads(), SECONDARY);
		networkInitialization.buildWay(environment, this.data.getTerciaryRoads(), TERTIARY);
		networkInitialization.buildWay(environment, this.data.getResidentialRoads(), RESIDENTIAL);
		networkInitialization.buildWay(environment, this.data.getRailway(), RAILWAY);
		networkInitialization.buildWay(environment, this.data.getTramway(), TRAMWAY);
		networkInitialization.buildStations(environment, this.data.getStations(), RAILWAY, STATION);
		networkInitialization.buildStations(environment, this.data.getTramStops(), TRAMWAY, TRAM_STOP);
		networkInitialization.buildBusLines(environment);
		InterestPointsOSM ipo = new InterestPointsOSM(startingPointsForCars, data, clock);
		this.leisures = ipo.getLeisurePlaces();
		this.destinationGenerator = new DestinationGenerator(ipo, startingPointsForCars,
				limits, world.getBusLines(), this.planning, data.getHeight(), data.getWidth());
		Map<String,List<RoadNode>> otherNodes = new HashMap<>();
		otherNodes.put(TRAMWAY, new ArrayList<>());
		otherNodes.put(RAILWAY, new ArrayList<>());
		otherNodes.put(BUSWAY, new ArrayList<>());
		//We add the stations in the graph, they make the link between the different level of the graph
		for (Station s : stations) {
			if (!s.getType().equals(BUSWAY)) {
				RoadNode rn = new RoadNode (s.getPlatform(), s.getType());
				this.graph.addLonelyPoint(rn, s.getType());
				otherNodes.get(s.getType()).add(rn);
				RoadNode rn2 = new RoadNode (s.getAccess(), STREET);
				this.graph.addLonelyPoint(rn2, STREET);
				RoadEdge re = new RoadEdge(rn, rn2, STATION);
				this.graph.addRoadEdge(re);
			}
		}
		//We add the limits of railway and tramways in the graph
		for (String type : limits.keySet()) {
			if (!type.equals(STREET)) {
				for (Point2D pt : limits.get(type)) {
					RoadNode rn = new RoadNode (pt, type);
					otherNodes.get(type).add(rn);
				}
			}
		}
		//We add the limits of the busline as a busway element in the graph
		for (BusLine bl : world.getBusLines()) {
			Point2D fl = bl.getFirstExtremity();
			RoadNode rn = new RoadNode (fl, BUSWAY);
			RoadNode rn1 = new RoadNode (bl.getBusStop().get(0).getPlatform(), BUSWAY);
			RoadNode rn1a = new RoadNode (bl.getBusStop().get(0).getAccess(), STREET);
			this.graph.addLonelyPoint(rn1a, STREET);
			this.graph.addRoadEdge(new RoadEdge(rn1, rn1a, STATION));
			this.graph.addRoadEdge(new RoadEdge(rn, rn1, BUSWAY));
			otherNodes.get(BUSWAY).add(rn);
			otherNodes.get(BUSWAY).add(rn1);
			for (int j = 1; j < bl.getBusStop().size() - 1; j++) {
				RoadNode rn2 = new RoadNode(bl.getBusStop().get(j).getPlatform(), BUSWAY);
				otherNodes.get(BUSWAY).add(rn2);
				RoadEdge rec = new RoadEdge(rn1, rn2, BUSWAY);
				this.graph.addRoadEdge(rec);
				RoadNode rn2a = new RoadNode (bl.getBusStop().get(j).getAccess(), STREET);
				this.graph.addLonelyPoint(rn2a, STREET);
				this.graph.addRoadEdge(new RoadEdge(rn2, rn2a, STATION));
				rn1 = rn2;
			}
			Point2D sl = bl.getSecondExtremity();
			RoadNode rnl = new RoadNode (sl, BUSWAY);
			otherNodes.get(BUSWAY).add(rnl);
			RoadEdge rel = new RoadEdge (rn1, rnl, BUSWAY);
			this.graph.addRoadEdge(rel);
		}
		//We connect all the tramway and railway elements
		for (String type : otherNodes.keySet()) {
			if (!type.equals(BUSWAY)) {
				for (RoadNode rn : otherNodes.get(type)) {
					for (RoadNode rn2 : otherNodes.get(type)) {
						if (!rn.equals(rn2)) {
							this.graph.addRoadEdge(new RoadEdge(rn, rn2, type));
						}
					}
				}
			}
		}
		this.world.setGraph(graph);
		this.world.setLeisures(leisures);
		this.world.setHeight(data.getHeight());
		this.world.setWidth(data.getWidth());
		this.world.setRoads(startingPointsForCars);
		this.world.setStations(stations);
		return eid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(ISimulationParameters simulationParameters) {
		TransportSimulationParameters castedSimulationParameters = (TransportSimulationParameters) simulationParameters;
		ExtendedLevel logo;
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
		List<ILevel> levelList = new LinkedList<>();
		levelList.add(logo);
		return levelList;	
	}
	

	
	
	/**
	 * @return the data
	 */
	public DataFromOSM getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(DataFromOSM data) {
		this.data = data;
	}

	/**
	 * @return the startingPointsForTransports
	 */
	public Map<String, List<Point2D>> getStartingPointsForTransports() {
		return startingPointsForTransports;
	}

	/**
	 * @param startingPointsForTransports the startingPointsForTransports to set
	 */
	public void setStartingPointsForTransports(Map<String, List<Point2D>> startingPointsForTransports) {
		this.startingPointsForTransports = startingPointsForTransports;
	}

	/**
	 * @return the limits
	 */
	public Map<String, List<Point2D>> getLimits() {
		return limits;
	}

	/**
	 * @param limits the limits to set
	 */
	public void setLimits(Map<String, List<Point2D>> limits) {
		this.limits = limits;
	}

	/**
	 * @return the stations
	 */
	public List<Station> getStations() {
		return stations;
	}

	/**
	 * @param stations the stations to set
	 */
	public void setStations(List<Station> stations) {
		this.stations = stations;
	}

	/**
	 * @return the leisures
	 */
	public List<AbstractLeisure> getLeisures() {
		return leisures;
	}

	/**
	 * @param leisures the leisures to set
	 */
	public void setLeisures(List<AbstractLeisure> leisures) {
		this.leisures = leisures;
	}

	/**
	 * @return the startingPointsForCars
	 */
	public List<Point2D> getStartingPointsForCars() {
		return startingPointsForCars;
	}

	/**
	 * @param startingPointsForCars the startingPointsForCars to set
	 */
	public void setStartingPointsForCars(List<Point2D> startingPointsForCars) {
		this.startingPointsForCars = startingPointsForCars;
	}

	/**
	 * @return the planning
	 */
	public TransportParametersPlanning getPlanning() {
		return planning;
	}

	/**
	 * @param planning the planning to set
	 */
	public void setPlanning(TransportParametersPlanning planning) {
		this.planning = planning;
	}

	/**
	 * @return the graph
	 */
	public RoadGraph getGraph() {
		return graph;
	}

	/**
	 * @param graph the graph to set
	 */
	public void setGraph(RoadGraph graph) {
		this.graph = graph;
	}

	/**
	 * @return the destinationGenerator
	 */
	public DestinationGenerator getDestinationGenerator() {
		return destinationGenerator;
	}

	/**
	 * @param destinationGenerator the destinationGenerator to set
	 */
	public void setDestinationGenerator(DestinationGenerator destinationGenerator) {
		this.destinationGenerator = destinationGenerator;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @param world the world to set
	 */
	public void setWorld(World world) {
		this.world = world;
	}

	/**
	 * @return the clock
	 */
	public Clock getClock() {
		return clock;
	}

	/**
	 * @param clock the clock to set
	 */
	public void setClock(Clock clock) {
		this.clock = clock;
	}

}