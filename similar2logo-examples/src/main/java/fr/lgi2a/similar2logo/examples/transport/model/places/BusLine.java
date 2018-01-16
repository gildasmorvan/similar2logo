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
	private List<Station> busStops;
	
	/**
	 * The id of the bus stop. 
	 * It's used for creating the bus stops in the transport model.
	 */
	private List<String> idBusStops;
	
	public BusLine (String id) {
		this.busStops = new ArrayList<>();
		this.idBusStops = new ArrayList<>();
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
	 * Adds the string of the bus stop id
	 * @param s the id of the bus stop
	 */
	public void addIdBusStop (String s) {
		this.idBusStops.add(s);
	}
	
	/**
	 * Gives the id of the bus stops
	 * @return
	 */
	public List<String> getIdBusStop () {
		return this.idBusStops;
	}
	
	/**
	 * Gives the next position of the bus
	 * @param currentPosition the current position of the bus
	 * @param terminus the terminus of the bus (where it exits from the map)
	 * @return the next position where the bus has to go
	 */
	public Point2D nextDestination (Point2D currentPosition, Point2D terminus) {
		if (currentPosition.equals(firstExtremity)) {
			return this.busStops.get(0).getAccess();
		} else if (currentPosition.equals(secondExtremity)) {
			return this.busStops.get(busStops.size()-1).getAccess();
		}
		int index = -1;
		for (int i = 0; i < busStops.size(); i++) {
			if (busStops.get(i).getAccess().equals(currentPosition)) {
				index = i;
			}
		}
		if (terminus.equals(secondExtremity)) {
			if (index == busStops.size() - 1) {
				return secondExtremity;
			} else {
				return busStops.get(index+1).getAccess();
			}
		} else {
			if (index == 0) {
				return firstExtremity;
			} else {
				return busStops.get(index-1).getAccess();
			}
		}
	}
	
	/**
	 * Adds a bus stop
	 * @param busStop the bus stop to add
	 */
	public void addBusStop (Station busStop) {
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
			if (firstExtremity.distance(busStops.get(0).getPlatform()) > limits.get(i).distance(busStops.get(0).getPlatform())) {
				firstExtremity = limits.get(i);
			}
			if (secondExtremity.distance(busStops.get(busStops.size()-1).getPlatform()) > 
			limits.get(i).distance(busStops.get(busStops.size()-1).getPlatform())) {
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
			res += busStops.get(i).getPlatform().toString()+" -> ";
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
	 * Indicates if there isn't id bus stop
	 * @return true if there isn't id bus stop, false else
	 */
	public boolean noIdBusStop () {
		return idBusStops.size() == 0;
	}
	
	/**
	 * Gives the list of bus stops
	 * @return the list of bus stops
	 */
	public List<Station> getBusStop () {
		return this.busStops;
	}
	
	/**
	 * Sorts the bus stop for decreasing the distance between the points
	 */
	private void sortBusStops () {
		for (int i = 0; i < busStops.size(); i++) {
			for (int j =0; j < busStops.size(); j++) {
				if (i != j) {
					List<Station> temp = new ArrayList<>();
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
	private double sumDistances (List<Station> points) {
		double res = 0;
		for (int i=0; i < points.size()-1; i++) {
			res += points.get(i).getPlatform().distance(points.get(i+1).getPlatform());
		}
		return res;
	}
	
}
