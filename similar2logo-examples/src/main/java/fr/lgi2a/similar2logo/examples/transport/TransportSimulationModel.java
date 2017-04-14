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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.transport.model.TransportSimulationParameters;
import fr.lgi2a.similar2logo.examples.transport.osm.DataFromOSM;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

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

	public TransportSimulationModel(LogoSimulationParameters parameters, String path) {
		super(parameters);
		this.data = new DataFromOSM(path);
		TransportSimulationParameters tsp = (TransportSimulationParameters) parameters;
		tsp.setSize(this.data.getHeight(), this.data.getWidth());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		TransportSimulationParameters tsp = (TransportSimulationParameters) simulationParameters;
		AgentInitializationData aid = new AgentInitializationData();
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
		this.buildStreets(environment);
		this.buildRailway(environment);
		this.findStations(environment);
		return eid;
	}
	
	/**
	 * Build the raods in the simulation
	 * @param lep the logo environment pls
	 */
	protected void buildStreets (LogoEnvPLS lep) {
		List<List<String>> streets = this.data.getHighway();
		for (List<String> list : streets) {
			for (String s : list) {
				Point2D pt = data.getCoordinates(s);
				if (inTheEnvironment(pt))
					lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 0, "Street"));
				}
			}
		linkPointsRoads (streets, lep);
	}
	
	protected void buildRailway (LogoEnvPLS lep) {
		List<List<String>> rails = this.data.getRailway();
		for (List<String> list : rails) {
			for (String s : list) {
				Point2D pt = data.getCoordinates(s);
				if (inTheEnvironment(pt))
					lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 0, "Railway"));
			}
		}
		linkPointsRails (rails, lep);
	}
	
	/**
	 * Build the rails in the simulation
	 * @param lep the logo environment pls
	 */
	protected void findStations (LogoEnvPLS lep) {
		List<String> stations = data.getStations();
		for (String s : stations) {
			Point2D pt = data.getCoordinates(s);
			if (inTheEnvironment(pt))
				lep.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 0, "Station"));
		}
						
	}
	
	/**
	 * Determines the point to link for the roads
	 * @param pts the list of vectors of points
	 * @param lep the environment
	 */
	protected void linkPointsRoads (List<List<String>> pts, LogoEnvPLS lep) {
		for (List<String> liste : pts) {
			for (int i=0; i < liste.size() -1; i++) {
				Point2D ori = data.getCoordinates(liste.get(i));
				Point2D des = data.getCoordinates(liste.get(i+1));
				if ((ori != null) && (des != null))
					printRoadBetweenTwoPoints(ori, des, lep);
			}
		}
	}
	
	/**
	 * Determines the point to link for the rails
	 * @param pts the list of vectors of points
	 * @param lep the environment
	 */
	protected void linkPointsRails (List<List<String>> pts, LogoEnvPLS lep) {
		for (List<String> liste : pts) {
			for (int i=0; i < liste.size() -1; i++) {
				Point2D ori = data.getCoordinates(liste.get(i));
				Point2D des = data.getCoordinates(liste.get(i+1));
				if ((ori != null) && (des != null))
					printRailBetweenTwoPoints(ori, des, lep);
			}
		}
	}
	
	/**
	 * Add the street marks between ori and des
	 * @param ori the point of origin
	 * @param des the point of destination
	 * @param lep the environment
	 */
	protected void printRoadBetweenTwoPoints (Point2D ori, Point2D des, LogoEnvPLS lep) {
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
			if (r.nextInt(3) <= 1) {
				if ((secondNextPosition.getY() >= 0) && (secondNextPosition.getY() < lep.getHeight()) && 
						(secondNextPosition.getX() >= 0) && (secondNextPosition.getX() < lep.getWidth())) {
					lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() )
					.add(new Mark<Double>(secondNextPosition, (double) 0, "Street"));
				}
				printRoadBetweenTwoPoints(secondNextPosition, des, lep);
			} else {
				if ((nextPosition.getY() >= 0) && (nextPosition.getY() < lep.getHeight()) && 
						(nextPosition.getX() >= 0) && (nextPosition.getX() < lep.getWidth())) {
					lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() )
					.add(new Mark<Double>(nextPosition, (double) 0, "Street"));
				}
				printRoadBetweenTwoPoints(nextPosition, des, lep);
			}
		}
	}
	
	/**
	 * Add the street marks between ori and des
	 * @param ori the point of origin
	 * @param des the point of destination
	 * @param lep the environment
	 */
	protected void printRailBetweenTwoPoints (Point2D ori, Point2D des, LogoEnvPLS lep) {
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
			if (r.nextInt(3) <= 1) {
				if ((secondNextPosition.getY() >= 0) && (secondNextPosition.getY() < lep.getHeight()) && 
						(secondNextPosition.getX() >= 0) && (secondNextPosition.getX() < lep.getWidth())) {
					lep.getMarksAt((int) secondNextPosition.getX(), (int) secondNextPosition.getY() )
					.add(new Mark<Double>(secondNextPosition, (double) 0, "Railway"));
				}
				printRailBetweenTwoPoints(secondNextPosition, des, lep);
			} else {
				if ((nextPosition.getY() >= 0) && (nextPosition.getY() < lep.getHeight()) && 
						(nextPosition.getX() >= 0) && (nextPosition.getX() < lep.getWidth())) {
					lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() )
					.add(new Mark<Double>(nextPosition, (double) 0, "Railway"));
				}
				printRailBetweenTwoPoints(nextPosition, des, lep);
			}
		}
	}
	
	/**
	 * Indicates if a point is in the environment
	 * @param pt A point
	 * @return true if the point is in the limit of the environment, else false
	 */
	protected boolean inTheEnvironment (Point2D pt) {
		return ((pt.getX() >= 0) && (pt.getY() >= 0) && (pt.getX() < data.getWidth()) && (pt.getY() < data.getHeight()));
	}

}
