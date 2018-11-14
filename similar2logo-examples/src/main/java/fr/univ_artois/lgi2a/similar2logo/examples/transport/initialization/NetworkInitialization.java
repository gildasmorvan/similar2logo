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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.BusLine;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.Station;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.osm.roadsgraph.RoadEdge;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.osm.roadsgraph.RoadNode;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;

/**
 * Initialization of the network.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class NetworkInitialization {
	
	
	/**
	 * The simulation model to initialize
	 */
	private TransportSimulationModel transportSimulationModel;
	
	/**
	 * Builds a new NetworkInitialization object
	 * @param transportSimulationModel the simulation model to initialize
	 */
	public NetworkInitialization(TransportSimulationModel transportSimulationModel) {
		this.transportSimulationModel = transportSimulationModel;
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
				Point2D pt = transportSimulationModel.getData().getCoordinates(s);
				if (InitializationUtil.inTheEnvironment(transportSimulationModel.getData(),pt)) {
					switch(type) {
						case SECONDARY:
							lep.getMarksAt(
								(int) pt.getX(),
								(int) pt.getY()
							).add(new Mark<Double>(pt, (double) 1, STREET));						
							break;
						case TERTIARY:
							lep.getMarksAt(
								(int) pt.getX(),
								(int) pt.getY()
							).add(new Mark<Double>(pt, (double) 1.2, STREET));
							break;
						case RESIDENTIAL:
							lep.getMarksAt(
								(int) pt.getX(), (int) pt.getY()
							).add(new Mark<Double>(pt, (double) 1.5, STREET));
							break;
						default:
							lep.getMarksAt(
								(int) pt.getX(),
								(int) pt.getY()
							).add(new Mark<Double>(pt, (double) 2, type));
					}
					if (InitializationUtil.onEdge(transportSimulationModel.getData(),pt)) {
						if (SECONDARY.equals(type) || TERTIARY.equals(type) || RESIDENTIAL.equals(type)) {
							transportSimulationModel.getLimits().get(STREET).add(pt);
						} else { 
							transportSimulationModel.getLimits().get(type).add(pt);
						}
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
			Point2D pt = transportSimulationModel.getData().getCoordinates(s);
			if (InitializationUtil.inTheEnvironment(transportSimulationModel.getData(),pt)) {
				lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 0, typeStop));
				int[][] disAccess = distanceToMark(pt, STREET, lep);
				int[][] disPlatform = distanceToMark(pt, type, lep);
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
				Point2D access = new Point2D.Double(
					pt.getX() + (x1-1)*minDisAccess,
					pt.getY() + (y1-1)*minDisAccess
				);
				Point2D platform = new Point2D.Double(
					pt.getX() + (x2-1)*minDisPlaform,
					pt.getY() +(y2-1)*minDisPlaform
				);
				if (STATION.equals(typeStop)) {
					Point2D exit = new Point2D.Double(
						pt.getX()+ (x3-1)*minDisExit,
						pt.getY() + (y3-1)*minDisExit
					);
					transportSimulationModel.getStations().add(new Station(access, exit, platform, type));
				} else {
					transportSimulationModel.getStations().add(new Station(access, access, platform, type));
				}
			}	
		}						
	}
	
	/**
	 * Builds the bus lines and the bus stops
	 */
	protected void buildBusLines (LogoEnvPLS environment) {
		List<BusLine> lines = transportSimulationModel.getData().getBusLines();
		Map<String,Station> busStops = new HashMap<>();
		for (BusLine bl : lines) {
			for (String s : bl.getIdBusStop()) {
				if (busStops.containsKey(s)) {
					bl.addBusStop(busStops.get(s));
				} else {
					Point2D pt = transportSimulationModel.getData().getCoordinates(s);
					if (InitializationUtil.inTheEnvironment(transportSimulationModel.getData(),pt)) {
						environment.getMarksAt(
							(int) pt.getX(),
							(int) pt.getY()
						).add(new Mark<Double>(pt, (double) 0, BUS_STOP));
						int[][] disAccess = distanceToMark(pt, STREET, environment);
						int x1 = 0,y1 = 0;
						int minDisAccess = Integer.MAX_VALUE-1;
						for (int i =0; i < disAccess.length; i++) {
							for (int j= 0; j< disAccess[0].length; j++) {
								if (disAccess[i][j] < minDisAccess) {
									x1 = i;
									y1 = j;
									minDisAccess = disAccess[i][j]; 
								}
							}
						}
						Point2D access = new Point2D.Double(
							pt.getX() + (x1-1)*minDisAccess,
							pt.getY() + (y1-1)*minDisAccess
						);
						Station sta = new Station(access, access, pt, BUSWAY);
						transportSimulationModel.getStations().add(sta);
						busStops.put(s, sta);
						bl.addBusStop(sta);
					}
				}
			}
			bl.calculateExtremities(transportSimulationModel.getLimits().get(STREET));
			if (!transportSimulationModel.getLimits().get(BUSWAY).contains(bl.getFirstExtremity())) {
				transportSimulationModel.getLimits().get(BUSWAY).add(bl.getFirstExtremity());
			}
			if (!transportSimulationModel.getLimits().get(BUSWAY).contains(bl.getSecondExtremity())) {
				transportSimulationModel.getLimits().get(BUSWAY).add(bl.getSecondExtremity());
			}
		}
		transportSimulationModel.getWorld().setBusLine(lines);
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
				Point2D ori = transportSimulationModel.getData().getCoordinates(liste.get(i));
				Point2D des = transportSimulationModel.getData().getCoordinates(liste.get(i+1));
				if (InitializationUtil.inTheEnvironment(transportSimulationModel.getData(), ori) 
				  ||InitializationUtil.inTheEnvironment(transportSimulationModel.getData(), des)
				) {
					printWayBetweenTwoPoints(ori, ori, des, lep,type);
				}
			}
		}
	}
	
	/**
	 * Prints the way between two points.
	 * Do nothing if isn't in the environment
	 * @param debut the point where the creation starts
	 * @param ori the origin of the way
	 * @param des the end of the way
	 * @param lep the environment where to build the way
	 * @param type the type of way
	 */
	protected void printWayBetweenTwoPoints (Point2D debut, Point2D ori, Point2D des, LogoEnvPLS lep, String type) {
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
						double distance = Point2D.distance(
							testPoint.getX(), testPoint.getY(), des.getX(), des.getY()
						);
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
			if (PRNG.randomInt(5) < -1) {
				if ((secondNextPosition.getY() >= 0) && (secondNextPosition.getY() < lep.getHeight()) && 
						(secondNextPosition.getX() >= 0) && (secondNextPosition.getX() < lep.getWidth())) {
					if (type.equals(SECONDARY)) {
						lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() ).add(new Mark<Double>(secondNextPosition, (double) 1, STREET));
					} else if (type.equals(TERTIARY)) {
						lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() ).add(new Mark<Double>(secondNextPosition, (double) 1.2, STREET));
					} else if (type.equals(RESIDENTIAL)) {
						lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() ).add(new Mark<Double>(secondNextPosition, (double) 1.5, STREET));
					} else {
						lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() ).add(new Mark<Double>(secondNextPosition, (double) 2, type));
					}
					if (InitializationUtil.onEdge(transportSimulationModel.getData(), secondNextPosition)) {
						if (!type.equals(TRAMWAY) && !type.equals(RAILWAY)) {
							transportSimulationModel.getGraph().addRoadNode(new RoadNode(secondNextPosition, convertTypeNode(type)));
							transportSimulationModel.getGraph().addRoadEdge(new RoadEdge(new RoadNode(debut, convertTypeNode(type)), 
									new RoadNode (secondNextPosition, convertTypeNode(type)), type));
						}
						if (type.equals(SECONDARY) || type.equals(TERTIARY) || type.equals(RESIDENTIAL)) {
							transportSimulationModel.getLimits().get(STREET).add(secondNextPosition);
						} else {
							transportSimulationModel.getLimits().get(type).add(secondNextPosition);
						}
					}	
					if ((type.equals(SECONDARY) || type.equals(TERTIARY) || type.equals(RESIDENTIAL)) 
						&& !transportSimulationModel.getStartingPointsForCars().contains(secondNextPosition)) {
						transportSimulationModel.getStartingPointsForCars().add(secondNextPosition);
					}
				}
				printWayBetweenTwoPoints(debut, secondNextPosition, des, lep, type);
			} else {
				if ((nextPosition.getY() >= 0) && (nextPosition.getY() < lep.getHeight()) && 
						(nextPosition.getX() >= 0) && (nextPosition.getX() < lep.getWidth())) {
					if (type.equals(SECONDARY)) {
						lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() ).add(new Mark<Double>(nextPosition, (double) 1, STREET));
					} else if (type.equals(TERTIARY)) {
						lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() ).add(new Mark<Double>(nextPosition, (double) 1.2, STREET));
					} else if (type.equals(RESIDENTIAL)) {
						lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() ).add(new Mark<Double>(nextPosition, (double) 1.5, STREET));
					} else {
						lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() ).add(new Mark<Double>(nextPosition, (double) 2, type));
					}
					if (InitializationUtil.onEdge(transportSimulationModel.getData(),nextPosition)) {
						if (!type.equals(TRAMWAY) && !type.equals(RAILWAY)) {
							transportSimulationModel.getGraph().addRoadNode(
								new RoadNode(nextPosition, convertTypeNode(type))
							);
							transportSimulationModel.getGraph().addRoadEdge(
								new RoadEdge(new RoadNode(debut, convertTypeNode(type)), 
								new RoadNode (nextPosition, convertTypeNode(type)),
								type)
							);
						}
						if (type.equals(SECONDARY) || type.equals(TERTIARY) || type.equals(RESIDENTIAL)) {
							transportSimulationModel.getLimits().get(STREET).add(nextPosition);
						} else { 
							transportSimulationModel.getLimits().get(type).add(nextPosition);
						}
					}
					if ((type.equals(SECONDARY) || type.equals(TERTIARY) || type.equals(RESIDENTIAL)) 
						&& !transportSimulationModel.getStartingPointsForCars().contains(nextPosition)) {
						transportSimulationModel.getStartingPointsForCars().add(nextPosition);
					}
				}
				printWayBetweenTwoPoints(debut, nextPosition, des, lep,type);
			}
		} else if (InitializationUtil.inTheEnvironment(transportSimulationModel.getData(), des) && !(type.equals(TRAMWAY) || type.equals(RAILWAY))) {
			transportSimulationModel.getGraph().addRoadEdge(new RoadEdge(
				new RoadNode(debut, convertTypeNode(type)),
				new RoadNode(des, convertTypeNode(type)),
				type
			));
		}
	}
	
	/**
	 * Gives the distance between a station/stop and a way
	 * @param pt the point where is the station/stop
	 * @param typeRoad the type of road to search
	 * @param lep the logo environment pls
	 * @return int[][] a table with the distance to the way for the Moore's neighborhood
	 */
	protected int[][] distanceToMark (Point2D pt, String typeRoad, LogoEnvPLS lep) {
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
						if (!InitializationUtil.inTheEnvironment(transportSimulationModel.getData(), nextPos)) {
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
	
	/**
	 * Gives the type of node
	 * @param type the original type
	 * @return the new type
	 */
	private static String convertTypeNode (String type) {
		if (type.equals(SECONDARY) || type.equals(TERTIARY) || type.equals(RESIDENTIAL)) {
			return STREET;
		} else {
			return type;
		}
	}
}
