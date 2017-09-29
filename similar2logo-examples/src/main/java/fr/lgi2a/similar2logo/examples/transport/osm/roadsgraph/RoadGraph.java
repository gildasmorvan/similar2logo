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
package fr.lgi2a.similar2logo.examples.transport.osm.roadsgraph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Graph of the roads gets thanks to Open Street Map
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class RoadGraph {
	
	/**
	 * The roads of the graph
	 */
	private Set<RoadEdge> roads;
	
	/**
	 * The nodes of the graph
	 */
	private Map<RoadNode, List<RoadEdge>> nodes;
	
	public RoadGraph () {
		this.nodes = new HashMap<>();
		this.roads = new HashSet<>();
	}
	
	/**
	 * Sorts the roads of a specific Road Node by size
	 * @param rn the node to sort
	 */
	private void sortByDistance (RoadNode rn) {
		List<RoadEdge> roads = nodes.get(rn);
		for (int i=0; i < roads.size() -1; i++) {
			for (int j = i; j < roads.size(); j++) {
				if (roads.get(j).length() < roads.get(i).length()) {
					roads.add(i, roads.remove(j));
				}
			}
		}
	}
	
	/**
	 * Sorts all the edges of all the nodes
	 */
	public void sortAllNodesDistance () {
		for (RoadNode rn : nodes.keySet()) sortByDistance (rn);
	}
	
	/**
	 * Adds a edge in the graph
	 * @param re the road edge to add
	 */
	public void addRoadEdge (RoadEdge re) {
		RoadNode rn1 = re.getFirstRoadNode();
		RoadNode rn2 = re.getSecondRoadNode();
		if (!nodes.containsKey(rn1)) {
			List<RoadEdge> list = new ArrayList<>();
			list.add(re);
			nodes.put(rn1, list);
		} else nodes.get(rn1).add(re);
		if (!nodes.containsKey(rn2)) {
			List<RoadEdge> list = new ArrayList<>();
			list.add(re);
			nodes.put(rn2, list);
		} else nodes.get(rn2).add(re);
		roads.add(re);
	}
	
	/**
	 * Adds a road node in the graph
	 * @param rn the road node to add
	 */
	public void addRoadNode (RoadNode rn) {
		this.nodes.put(rn, new ArrayList<>());
	}
	
	public List<Point2D> wayToGo (Point2D start, Point2D arrival) {
		List<Point2D> res = new ArrayList<>();
		RoadEdge dep = null, arr = null;
		//We search the edge where we want to go
		/*for (RoadEdge re : roads) {
			if (re.isOnTheRoad(start)) dep = re;
			if (re.isOnTheRoad(arrival)) arr = re;
		}
		RoadNode nDep = dep.getFirstRoadNode();
		RoadNode nArr = arr.getSecondRoadNode();*/
		return res;
	}
}
