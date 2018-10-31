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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.osm;

import java.util.HashMap;
import java.util.Map;

import static fr.univ_artois.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;
import static net.jafama.FastMath.*;

/**
 * Node data from the OSM data
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class OSMNode {

	/**
	 * The latitude of the node
	 */
	private int lat;
	
	/**
	 * The longitude of the node
	 */
	private int lon;
	
	/**
	 * The tags of the node (often empty)
	 */
	private Map<String,String> tags;
	
	/**
	 * Constructor of the OSMNode
	 * @param lon the longitude of the node
	 * @param lat the latitude of the node
	 */
	public OSMNode (double lon, double lat) {
		this.lat = (int) (lat*pow(10,7));
		this.lon = (int) (lon*pow(10,7));
		this.tags = new HashMap<>();
	}
	
	/**
	 * Gives the latitude of the node
	 * @return the latitude of the node
	 */
	public int getLatitude () {
		return this.lat;
	}
	
	/**
	 * Gives the longitude of the node
	 * @return the longitude of the node
	 */
	public int getLongitude () {
		return this.lon;
	}
	
	/**
	 * Adds a tag associate to the node
	 * @param k key of the tag
	 * @param v value of the tag
	 */
	public void addTag (String k, String v) {
		this.tags.put(k, v);
	}
	
	/**
	 * Indicates if the node is a level crossing
	 * @return true if the node is a level crossing, false else
	 */
	public boolean isLevelCrossing () {
		if (this.tags.containsKey(RAILWAY)) {
			return LEVEL_CROSSING.equals(this.tags.get(RAILWAY));
		} else {
			return false;
		}
	}
	
	/**
	 * Indicates if the node is a station
	 * @return true if the node is a station, false else
	 */
	public boolean isStation () {
		if (this.tags.containsKey(RAILWAY)) {
			return STATION.equals(this.tags.get(RAILWAY));
		} else {
			return false;
		}
	}

	/**
	 * Indicates if the node is a tram stop
	 * @return true if the node is a tram stop, false else
	 */
	public boolean isTramStop () {
		if (this.tags.containsKey(RAILWAY)) {
			return TRAM_STOP.equals(this.tags.get(RAILWAY));
		} else {
			return false;
		}
	}
	
	/**
	 * Indicates if the node is a bus stop
	 * @return true if the node is a bus stop, false else
	 */
	public boolean isBusStop () {
		if (this.tags.containsKey(HIGHWAY)) {
			return BUS_STOP.equals(this.tags.get(HIGHWAY));
		} else {
			return false;
		}
	}
	
	/**
	 * Indicates if the node is a school
	 * @return true if the node is school, false else
	 */
	public boolean isSchool () {
		if (this.tags.containsKey(AMENITY)) {
			return SCHOOL.equals(this.tags.get(AMENITY));
		} else {
			return false;
		}
	}
	
	/**
	 * Indicates if the node is a restaurant/bar
	 * @return true if the node is a restaurant, false else
	 */
	public boolean isRestaurant () {
		if (this.tags.containsKey(AMENITY)) {
			return BAR.equals(this.tags.get(AMENITY))
				|| FAST_FOOD.equals(this.tags.get(AMENITY))
				|| RESTAURANT.equals(this.tags.get(AMENITY));
		} else {
			return false;
		}
	}
	
	/**
	 * Indicates if the node is a bank
	 * @return true if the node is a bank, false else
	 */
	public boolean isBank () {
		if (this.tags.containsKey(AMENITY)) { 
			return BANK.equals(this.tags.get(AMENITY))
				|| ATM.equals(this.tags.get(AMENITY));
		} else {
			return false;
		}
	}
	
	/**
	 * Indicates if the node is a pharmacy or a doctor office
	 * @return true if the place is a pharmacy or a doctor, false else
	 */
	public boolean isMedecine () {
		if (this.tags.containsKey(AMENITY)) {
			return PHARMACY.equals(this.tags.get(AMENITY)) 
				|| DOCTORS.equals(this.tags.get(AMENITY)) ;
		} else {
			return false;
		}
	}
	
	/**
	 * Indicates if the node is a shop
	 * @return true if the place is a shop, false else
	 */
	public boolean isShop () {
		return this.tags.containsKey(SHOP);
	}

}
