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
package fr.lgi2a.similar2logo.examples.transport.model.places;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * The bus line in the transport simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class BusLine {
	
	/**
	 * The id of the bus line
	 */
	private String id;
	
	/**
	 * The two extremities of the line
	 */
	private Point2D firstExtremity, secondExtremity;
	
	/**
	 * The bus stops of the lines
	 */
	private List<Point2D> busStops;
	
	public BusLine (String id) {
		this.busStops = new ArrayList<>();
		this.id = id;
	}
	
	/**
	 * Gives the id of the line
	 * @return the id of the line
	 */
	public String getId () {
		return this.id;
	}
	
	/**
	 * Gives all the bus stop where a bus has to go
	 * @param currentPosition the current position of the bus (must be a bus stop or an extremity)
	 * @param destination the destination of the bus
	 * @return the list of bus stops where the bus has to go
	 */
	public List<Point2D> getItinerary (Point2D currentPosition, Point2D destination) {
		List<Point2D> res = new ArrayList<>();
		if (currentPosition.equals(firstExtremity)) {
			for (int i=0; i < busStops.size(); i++)
				res.add(busStops.get(i));
		} else if (currentPosition.equals(secondExtremity)) {
			for (int i = busStops.size() -1; i >= 0; i--)
				res.add(busStops.get(i));
		} else {
			if (destination.equals(secondExtremity)) {
				boolean start = false;
				for (int i =0; i < busStops.size(); i++) {
					if (busStops.get(i).equals(currentPosition))
						start = true;
					if (start)
						res.add(busStops.get(i));
				}
			} else {
				boolean start = false;
				for (int i = busStops.size() - 1; i >= 0; i--) {
					if (busStops.get(i).equals(currentPosition))
						start = true;
					if (start)
						res.add(busStops.get(i));
				}
			}
		}
		res.add(destination);
		return res;
	}
	
	/**
	 * Adds a bus stop
	 * @param busStop the bus stop to add
	 */
	public void addBusStop (Point2D busStop) {
		this.busStops.add(busStop);
	}
	
	/**
	 * Calculate the extremities when the bus stops have been found
	 * @param limits the limits of the map
	 */
	public void calculateExtremities (List<Point2D> limits) {
		this.sortBusStops();
		this.firstExtremity = limits.get(0);
		this.secondExtremity = limits.get(0);
		for (int i = 1; i < limits.size(); i++) {
			if (firstExtremity.distance(busStops.get(0)) > limits.get(i).distance(busStops.get(0))) {
				firstExtremity = limits.get(i);
			}
			if (secondExtremity.distance(busStops.get(busStops.size()-1)) > limits.get(i).distance(busStops.get(busStops.size()-1))) {
				secondExtremity = limits.get(i);
			}
		}
	}
	
	/**
	 * Gives the first extremity of the bus line
	 * @return the first extremity of the bus line
	 */
	public Point2D getFirstExtremity () {
		return this.firstExtremity;
	}
	
	/**
	 * Gives the second extremity of the bus line
	 * @return the second extremity of the bus line
	 */
	public Point2D getSecondExtremity () {
		return this.secondExtremity;
	}
	
	public String toString() {
		String res = "";
		res += "Bus line "+id+"\n";
		res += firstExtremity.toString()+" -> ";
		for (int i =0; i < busStops.size(); i++) {
			res += busStops.get(i).toString()+" -> ";
		}
		res += secondExtremity.toString();
		return res;
	}
	
	/**
	 * Indicates if there isn't bus stop
	 * @return true if there isn't bus stop, false else
	 */
	public boolean noBusStop () {
		return busStops.size() == 0;
	}
	
	/**
	 * Sorts the bus stop for decreasing the distance between the points
	 */
	private void sortBusStops () {
		for (int i = 0; i < busStops.size(); i++) {
			for (int j =0; j < busStops.size(); j++) {
				if (i != j) {
					List<Point2D> temp = new ArrayList<>();
					for (int k = 0; k < busStops.size(); k++) {
						if (k != i && k != j) {
							temp.add(busStops.get(k));
						} else if (k == i) {
							temp.add(busStops.get(j));
						} else {
							temp.add(busStops.get(i));
						}
					}
					if (sumDistances(temp) < sumDistances(busStops))
						busStops = temp;
				}
			}
		}
	}
	
	/**
	 * Sums the distances between all the point
	 * @param points the list of point
	 * @return the distance between all the point
	 */
	private double sumDistances (List<Point2D> points) {
		double res = 0;
		for (int i=0; i < points.size()-1; i++) {
			res += points.get(i).distance(points.get(i+1));
		}
		return res;
	}
	
}
