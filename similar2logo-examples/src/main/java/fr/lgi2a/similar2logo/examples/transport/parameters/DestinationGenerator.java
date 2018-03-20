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
package fr.lgi2a.similar2logo.examples.transport.parameters;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.transport.model.places.BusLine;
import fr.lgi2a.similar2logo.examples.transport.model.places.AbstractLeisure;
import fr.lgi2a.similar2logo.examples.transport.osm.InterestPointsOSM;
import fr.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.lgi2a.similar2logo.lib.tools.PRNG;

import static fr.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;

/**
 * Class allows to generate the destination of the cars and the persons.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class DestinationGenerator {
	
	/**
	 * The interest points of the map.
	 */
	private InterestPointsOSM leisure;
	
	/**
	 * The limits of the maps for each type of way
	 */
	private Map<String,List<Point2D>> limits;
	
	/**
	 * The roads on the map
	 */
	private List<Point2D> roads;
	
	/**
	 * The planning of parameters
	 */
	private TransportParametersPlanning planning;
	
	/**
	 * The bus lines
	 */
	private List<BusLine> busLines;
	
	/**
	 * The height and the width of the world
	 */
	private int height, width;

	public DestinationGenerator (InterestPointsOSM ipo, List<Point2D> roads, Map<String,List<Point2D>> limits, List<BusLine> busLines,
			TransportParametersPlanning tpp, int h, int w) {
		this.leisure = ipo;
		this.limits = limits;
		this.roads = roads;
		this.planning = tpp;
		this.height = h;
		this.width = w;
		this.busLines = busLines;
	}
	
	/**
	 * Gives the destination
	 * @param sts the simulation time stamp when the choice is done
	 * @param position the position of the agent
	 * @return where the agent will go
	 */
	public Point2D getADestination (SimulationTimeStamp sts, Point2D position) {
		TransportSimulationParameters tsp = planning.getParameters(sts, position, width, height);
		double random = PRNG.get().randomDouble();
		double sum = tsp.probaGoToSchool;
		if (random <= sum) {
			return closestSchool(position);
		}
		sum += tsp.probaGoToRestaurant;
		if (random <= sum) {
			return leisure.getRestaurant();
		}
		sum += tsp.probaGoToBank;
		if (random <= sum) {
			return leisure.getBank();
		}
		sum += tsp.probaGoToDoctor;
		if (random <= sum) {
			return leisure.getDoctor();
		}
		sum += tsp.probaGoToShop;
		if (random <= sum) {
			return leisure.getShop();
		}
		sum += tsp.probaLeaveTownByTrain;
		if (random <= sum) {
			List <Point2D> l = limits.get(RAILWAY);
			return l.get(PRNG.get().randomInt(l.size()));
		}
		sum += tsp.probaLeaveTownByTram;
		if (random <= sum) {
			List<Point2D> l = limits.get(TRAMWAY);
			return l.get(PRNG.get().randomInt(l.size()));
		}
		sum += tsp.probaLeaveTownByBus;
		if (random <= sum) {
			int line = PRNG.get().randomInt(busLines.size());
			int ext = PRNG.get().randomInt(2);
			if (ext == 0) {
				return busLines.get(line).getFirstExtremity();
			} else {
				return busLines.get(line).getSecondExtremity();
			}
		}
		sum += tsp.probaLeaveTownByRoad;
		if (random <= sum) {
			List<Point2D> l = limits.get(STREET);
			return l.get(PRNG.get().randomInt(l.size()));
		}
		return roads.get(PRNG.get().randomInt(roads.size()));
	}
	
	/**
	 * Gives the destination to an agent
	 * @param sts the simulation time stamp when the agent is created
	 * @param position the position of the agent
	 * @param type the type of the agent
	 * @return the destination where will go the agent
	 */
	public Point2D getADestination (SimulationTimeStamp sts, Point2D position, String type) {
		TransportSimulationParameters tsp = planning.getParameters(sts, position, width, height);
		double random = PRNG.get().randomDouble();
		double denominator = 1;
		if (type.equals(BIKE)) {
			denominator -= tsp.probaLeaveTownByBus;
		} else if (type.equals(CAR)) {
			denominator -= (tsp.probaLeaveTownByBus + tsp.probaLeaveTownByTram);
		}
		double sum = tsp.probaGoToSchool*denominator;
		if (random <= sum) {
			return closestSchool(position);
		}
		sum += tsp.probaGoToRestaurant*denominator;
		if (random <= sum) {
			return leisure.getRestaurant();
		}
		sum += tsp.probaGoToBank*denominator;
		if (random <= sum) {
			return leisure.getBank();
		}
		sum += tsp.probaGoToDoctor*denominator;
		if (random <= sum) {
			return leisure.getDoctor();
		}
		sum += tsp.probaGoToShop*denominator;
		if (random <= sum) {
			return leisure.getShop();
		}
		sum += tsp.probaLeaveTownByTrain*denominator;
		if (random <= sum) {
			List <Point2D> l = limits.get(RAILWAY);
			return l.get(PRNG.get().randomInt(l.size()));
		}
		if (!type.equals(CAR)) {
			sum += tsp.probaLeaveTownByTram*denominator;
			if (random <= sum) {
				List<Point2D> l = limits.get(TRAMWAY);
				return l.get(PRNG.get().randomInt(l.size()));
			}
		}
		if (!type.equals(BIKE) && !type.equals(CAR)) {
			sum += tsp.probaLeaveTownByBus*denominator;
			if (random <= sum) {
				int line = PRNG.get().randomInt(busLines.size());
				int ext = PRNG.get().randomInt(2);
				if (ext == 0) {
					return busLines.get(line).getFirstExtremity();
				} else {
					return busLines.get(line).getSecondExtremity();
				}
			}
		}
		sum += tsp.probaLeaveTownByRoad;
		if (random <= sum) {
			List<Point2D> l = limits.get(STREET);
			return l.get(PRNG.get().randomInt(l.size()));
		}
		return roads.get(PRNG.get().randomInt(roads.size()));
	}
	
	/**
	 * Gives a destination for persons in the transport.
	 * The probability to exit by train in this case is null.
	 * @param sts the current simulation time stamp
	 * @param position the position of the transport
	 * @param type the type of transport
	 * @return the destination where the person can go
	 */
	public Point2D getDestinationInTransport (SimulationTimeStamp sts, Point2D position, String type) {
		TransportSimulationParameters tsp = planning.getParameters(sts, position, width, height);
		double random = PRNG.get().randomDouble();
		if (type.equals(RAILWAY)) {
			if (random <= tsp.probaStayInTrain) {
				return this.limits.get(RAILWAY).get(PRNG.get().randomInt(this.limits.get(RAILWAY).size()));
			}
		} else {
			if (random <= tsp.probaStayInTram) {
				return this.limits.get(TRAMWAY).get(PRNG.get().randomInt(this.limits.get(TRAMWAY).size()));
			}
		}
		random = PRNG.get().randomDouble();
		double factor = 1 - tsp.probaLeaveTownByTrain - tsp.probaLeaveTownByTram;
		double sum = tsp.probaGoToSchool/factor;
		if (random <= sum) {
			return closestSchool(position);
		}
		sum += tsp.probaGoToRestaurant/factor;
		if (random <= sum) {
			return leisure.getRestaurant();
		}
		sum += tsp.probaGoToBank/factor;
		if (random <= sum) {
			return leisure.getBank();
		}
		sum += tsp.probaGoToDoctor/factor;
		if (random <= sum) {
			return leisure.getDoctor();
		}
		sum += tsp.probaGoToShop/factor;
		if (random <= sum) {
			return leisure.getShop();
		}
		sum += tsp.probaLeaveTownByRoad/factor;
		if (random <= sum) {
			List<Point2D> l = limits.get(STREET);
			return l.get(PRNG.get().randomInt(l.size()));
		}
		return roads.get(PRNG.get().randomInt(roads.size()));
	}
	
	
	
	/**
	 * Gives the position of the closes school
	 * However, it's maybe not the closest by road but as crow flies
	 * @param position the positions from where we start
	 * @return the position of the closest school
	 */
	private Point2D closestSchool (Point2D position) {
		List<AbstractLeisure> schools = leisure.getAllSchools();
		int ind = 0;
		double dis = schools.get(0).getPosition().distance(position);
		for (int i=1; i < schools.size(); i++) {
			if (dis < schools.get(i).getPosition().distance(position)) {
				dis = schools.get(i).getPosition().distance(position);
				ind = i;
			}
		}
		return schools.get(ind).getPosition();
	}
}
