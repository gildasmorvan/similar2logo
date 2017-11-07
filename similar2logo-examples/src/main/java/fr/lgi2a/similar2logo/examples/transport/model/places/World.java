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
import java.util.List;

import fr.lgi2a.similar2logo.examples.transport.osm.roadsgraph.RoadGraph;

/**
 * Class with all the elements of the map.
 * The goal of this class is to reduce the number of parameters in each element.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class World {
	
	/**
	 * The height and the width of the world
	 */
	private int height, width;

	/**
	 * The stations of the map
	 */
	private List<Station> stations;
	
	/**
	 * The leisure places of the map
	 */
	private List<Leisure> leisures;
	
	/**
	 * The roads of the map
	 */
	private List<Point2D> roads;
	
	/**.
	 * The graph of the map
	 */
	private RoadGraph graph;
	
	/**
	 * Gives the height of the world
	 * @return the height of the world
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Sets the height of the world
	 * @param h the height of the world
	 */
	public void setHeight (int h) {
		this.height = h;
	}

	/**
	 * Gives the width of the world
	 * @return the width of the world
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Sets the width of the world
	 * @param w
	 */
	public void setWidth (int w) {
		this.width = w;
	}

	/**
	 * Gives the list of the stations
	 * @return the list of the stations
	 */
	public List<Station> getStations() {
		return stations;
	}
	
	/**
	 * Sets the list of the stations
	 * @param s the list of the stations
	 */
	public void setStations (List<Station> s) {
		this.stations = s;
	}

	/**
	 * Gives the list of leisure places
	 * @return the list of leisure places
	 */
	public List<Leisure> getLeisures() {
		return leisures;
	}
	
	/**
	 * Sets the list of leisure places
	 * @param l the list of leisure places
	 */
	public void setLeisures (List<Leisure> l) {
		this.leisures = l;
	}

	/**
	 * Gives the roads on the map
	 * @return the road on the map
	 */
	public List<Point2D> getRoads() {
		return roads;
	}
	
	/**
	 * Sets the roads
	 * @param r the roads
	 */
	public void setRoads (List<Point2D> r) {
		this.roads = r;
	}

	/**
	 * Gives the graph of the world
	 * @return the graph of the world
	 */
	public RoadGraph getGraph() {
		return graph;
	}
	
	/**
	 * Sets the road graph
	 * @param rg the road graph
	 */
	public void setGraph (RoadGraph rg) {
		this.graph = rg;
	}

}
