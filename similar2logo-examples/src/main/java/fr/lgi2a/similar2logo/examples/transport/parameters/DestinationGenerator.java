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
import java.util.Random;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.transport.model.places.Leisure;
import fr.lgi2a.similar2logo.examples.transport.osm.InterestPointsOSM;
import fr.lgi2a.similar2logo.examples.transport.time.Clock;
import fr.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * Class allows to generate the destination of the cars and the persons.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
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
	 * The height and the width of the world
	 */
	private int height, width;

	public DestinationGenerator (InterestPointsOSM ipo, List<Point2D> roads, Map<String,List<Point2D>> limits,
			TransportParametersPlanning tpp, int h, int w) {
		this.leisure = ipo;
		this.limits = limits;
		this.roads = roads;
		this.planning = tpp;
		this.height = h;
		this.width = w;
	}
	
	public Point2D getADestination (SimulationTimeStamp sts, Point2D position) {
		TransportSimulationParameters tsp = planning.getParameters(sts, position, width, height);
		Random r = new Random ();
		double random = RandomValueFactory.getStrategy().randomDouble();
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
			List <Point2D> l = limits.get("Railway");
			return l.get(r.nextInt(l.size()));
		}
		sum += tsp.probaLeaveTownByTram;
		if (random <= sum) {
			List<Point2D> l = limits.get("Tramway");
			return l.get(r.nextInt(l.size()));
		}
		sum += tsp.probaLeaveTownByRoad;
		if (random <= sum) {
			List<Point2D> l = limits.get("Street");
			return l.get(r.nextInt(l.size()));
		}
		return roads.get(r.nextInt(roads.size()));
	}
	
	/**
	 * Gives the position of the closes school
	 * However, it's maybe not the closest by road but as crow flies
	 * @param position the positions from where we start
	 * @return the position of the closest school
	 */
	private Point2D closestSchool (Point2D position) {
		List<Leisure> schools = leisure.getAllSchools();
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
