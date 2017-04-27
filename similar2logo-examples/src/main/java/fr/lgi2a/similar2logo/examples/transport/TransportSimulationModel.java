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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.transport.model.TransportSimulationParameters;
import fr.lgi2a.similar2logo.examples.transport.model.agents.TrainCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.TrainDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.TransportFactory;
import fr.lgi2a.similar2logo.examples.transport.osm.DataFromOSM;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
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
	 * List of points where a train can be created
	 */
	private List<Point2D> startingPointsForTrains;
	
	/**
	 * Limits of each type of way.
	 * The key is the type of way.
	 * The value is a list with all the points limits for this type of way
	 */
	private Map<String,Set<Point2D>> limits;

	public TransportSimulationModel(LogoSimulationParameters parameters, String path) {
		super(parameters);
		this.data = new DataFromOSM(path);
		TransportSimulationParameters tsp = (TransportSimulationParameters) parameters;
		tsp.setSize(this.data.getHeight(), this.data.getWidth());
		startingPointsForTrains = new ArrayList<>();
		limits = new HashMap<>();
		limits.put("Street", new HashSet<>());
		limits.put("Railway", new HashSet<>());
		limits.put("Tramway", new HashSet<>());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		TransportSimulationParameters tsp = (TransportSimulationParameters) simulationParameters;
		AgentInitializationData aid = new AgentInitializationData();
		generateTrains(tsp, aid);
		return aid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected EnvironmentInitializationData generateEnvironment (ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		TransportSimulationParameters tsp = (TransportSimulationParameters) simulationParameters;
		return this.readMap(tsp, levels);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters) {
		TransportSimulationParameters castedSimulationParameters = (TransportSimulationParameters) simulationParameters;
		ExtendedLevel logo = new ExtendedLevel(
					castedSimulationParameters.getInitialTime(), 
					LogoSimulationLevelList.LOGO, 
					new PeriodicTimeModel( 
							1, 
							0, 
							castedSimulationParameters.getInitialTime()
							),
					new LogoDefaultReactionModel()
					);
		List<ILevel> levelList = new LinkedList<ILevel>();
		levelList.add(logo);
		return levelList;	
	}
	
	/**
	 * Read the environment extracts from Open Street Map.
	 * @param tsp the parameters of the simulation
	 * @param levels the level of the simulation
	 * @return the environment for the simulation
	 */
	protected EnvironmentInitializationData readMap (TransportSimulationParameters tsp, Map<LevelIdentifier, ILevel> levels ) {
		//Creation of the environment with the good size.
		EnvironmentInitializationData eid = super.generateEnvironment(tsp, levels);
		LogoEnvPLS environment = (LogoEnvPLS) eid.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
		//We add the different elements
		this.buildWay(environment, this.data.getHighway(), "Street");
		this.buildWay(environment, this.data.getRailway(), "Railway");
		this.buildWay(environment, this.data.getTramway(), "Tramway");
		this.buildSpecificPlaces(environment, this.data.getStations(), "Station");
		this.buildSpecificPlaces(environment, this.data.getTramStops(), "Tram_stop");
		this.buildSpecificPlaces(environment, this.data.getLevelCrossing(), "Level_crossing");
		return eid;
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
					lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 0, type));
					if (onEdge(pt)) {
						limits.get(type).add(pt);
					}
				}
			}
		}
		linkPointsWay (ways, lep, type);
	}
	
	/**
	 * Builds specific places
	 * @param lep the environment where build the places
	 * @param places the places to build
	 * @param type the type of place
	 */
	protected void buildSpecificPlaces (LogoEnvPLS lep, List<String> places, String type) {
		for (String s : places) {
			Point2D pt = data.getCoordinates(s);
			if (inTheEnvironment(pt))
				lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 0, type));
		}						
	}
	
	protected void generateTrains (TransportSimulationParameters tsp, AgentInitializationData aid) {
		int nbr = tsp.nbrTrains;
		System.out.println("train");
		for (List<String> list : this.data.getRailway()) {
			for (String s : list) {
				Point2D pt = data.getCoordinates(s);
				if (inTheEnvironment(pt) && !startingPointsForTrains.contains(pt)) {
					startingPointsForTrains.add(pt);
				}
			}
		}
		//We add train while we can
		for (int i = 0; i < nbr; i++) {
			try {
				double[] starts = {-3/4*Math.PI,-1/2*Math.PI,-1/4*Math.PI,0,1/4*Math.PI,1/2*Math.PI,-3/4*Math.PI,Math.PI};
				Random r = new Random();
				Point2D position = this.findRailForTrain();
				aid.getAgents().add(TransportFactory.generate(
						new TurtlePerceptionModel(
								Math.sqrt(2),Math.PI,true,true,true
							),
							new TrainDecisionModel(limits.get("Railway")),
							TrainCategory.CATEGORY,
							starts[r.nextInt(starts.length)] ,
							0 ,
							0,
							position.getX(),
							position.getY(),
							1,
							1
						));
			} catch (Exception e) {
				//Does nothing, we don't add train
			}
		}
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
			Point2D nextPosition = new Point2D.Double(ori.getX()+1, ori.getY()); //We start by the south
			double bestDistance = Point2D.distance(nextPosition.getX(), nextPosition.getY(), des.getX(), des.getY());
			Point2D secondNextPosition = new Point2D.Double(ori.getX()-1, ori.getY());
			double secondBestDistance = Point2D.distance(secondNextPosition.getX(), secondNextPosition.getY(), des.getX(), des.getY());
			if (secondBestDistance < bestDistance) {
				double tmp = bestDistance;
				bestDistance = secondBestDistance;
				secondBestDistance = tmp;
				Point2D pt = nextPosition;
				nextPosition = secondNextPosition;
				secondNextPosition = pt;
			}
			for (int i = -1 ; i <=1; i++) {
				for (int j= -1; j <= 1; j++) {
					if (!(i ==0)) {
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
			if (false/*r.nextInt(10) <= 2*/) {
				if ((secondNextPosition.getY() >= 0) && (secondNextPosition.getY() < lep.getHeight()) && 
						(secondNextPosition.getX() >= 0) && (secondNextPosition.getX() < lep.getWidth())) {
					lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() )
					.add(new Mark<Double>(secondNextPosition, (double) 0, type));
					if (onEdge(secondNextPosition)) {
						limits.get(type).add(secondNextPosition);
					}
				}
				printWayBetweenTwoPoints(secondNextPosition, des, lep, type);
			} else {
				if ((nextPosition.getY() >= 0) && (nextPosition.getY() < lep.getHeight()) && 
						(nextPosition.getX() >= 0) && (nextPosition.getX() < lep.getWidth())) {
					lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() )
					.add(new Mark<Double>(nextPosition, (double) 0, type));
					if (onEdge(nextPosition)) {
						limits.get(type).add(nextPosition);
					}
				}
				printWayBetweenTwoPoints(nextPosition, des, lep,type);
			}
		}
	}
	
	/**
	 * Gives a place to put a train.
	 * Removes the place from the stratingPointsForTrains list
	 * @return Point2D where put a train
	 * @throws Exception if there is no more place available
	 */
	protected Point2D findRailForTrain () throws Exception {
		if (startingPointsForTrains.isEmpty()) {
			throw new Exception ("No more place for adding train.");
		} else {
			Random r = new Random ();
			Point2D res = startingPointsForTrains.remove(r.nextInt(startingPointsForTrains.size()));
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

}
