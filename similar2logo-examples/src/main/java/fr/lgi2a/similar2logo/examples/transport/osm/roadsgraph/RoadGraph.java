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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar2logo.kernel.tools.FastMath;

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
	
	/**
	 * The height and the width of the world
	 */
	private int height, width;
	
	public RoadGraph (int h, int w) {
		this.nodes = new HashMap<>();
		this.roads = new HashSet<>();
		this.height = h;
		this.width = w;
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
		if (!this.nodes.containsKey(rn))
			this.nodes.put(rn, new ArrayList<>());
	}
	
	/**
	 * Adds a lonely point to the graph.
	 * The link is done in the function
	 * @param rn the road node to add
	 * @param type the type of road we want to create
	 */
	public void addLonelyPoint (RoadNode rn, String type) {
		for (RoadEdge re : roads ) {
			if (re.isOnTheRoad(rn.getPosition()) && compatibleType(type, re.getType())) {
				RoadEdge re1 = new RoadEdge(rn, re.getFirstRoadNode(), type);
				RoadEdge re2 = new RoadEdge(rn, re.getSecondRoadNode(), type);
				this.addRoadEdge(re1);
				this.addRoadEdge(re2);
				break;
			}
		}
	}
	
	/**
	 * Gives the way for going from start to arrival
	 * @param start the starting point
	 * @param arrival the arrival point
	 * @return the list with all the point we have to go for reaching arrival
	 */
	public List<Point2D> wayToGo (Point2D start, Point2D arrival) {
		List<Point2D> res = new ArrayList<>();
		RoadEdge dep = null, arr = null;
		//We search the edge where we want to go
		for (RoadEdge re : roads) {
			if (isAStreet(re.getType()) && re.isOnTheRoad(start)) {dep = re;}
			if (inTheLimits(arrival)) {
				if (re.isOnTheRoad(arrival) && isAStreet(re.getType())) arr = re;
			} else {
				if (re.isOnTheRoad(arrival)) {arr = re;}
			}
		}
		try {
		RoadNode nDep = dep.getFirstRoadNode();
		RoadNode nArr = arr.getSecondRoadNode();
		double[] dis = new double[nodes.keySet().size()];
		Set<RoadNode> notDone = new HashSet<>();
		RoadNode[] tableNodes = new RoadNode[nodes.keySet().size()];
		Map<RoadNode,RoadNode> predecessor = new HashMap<>();
		Map<RoadNode,Integer> index = new HashMap<>();
		int cpt = 0;
		//Tables initialization
		for (RoadNode rn : nodes.keySet()) {
			if (rn.equals(nDep))  dis[cpt] = 0;
			else dis[cpt] = Double.MAX_VALUE;
			tableNodes[cpt] = rn;
			notDone.add(rn);
			index.put(rn, cpt);
			cpt++;
		}
		//Research
		while (notDone.size() != 0) {
			int ind = lowestIndex(dis, notDone, tableNodes);
			RoadNode n = tableNodes[ind];
			for (RoadNode rn : getAdjacentNodes(n)) {
				int p = index.get(rn);
				int pr = index.get(n);
				double distance = distance(n,rn);
				double factor = getFactorFollowingType(getTypeEdge(n, rn));
				if (dis[p] > dis[pr]+distance*factor) {
					dis[p] = dis[pr]+distance*factor;
					predecessor.put(rn, n);
				}
			}
			notDone.remove(n);
		}
		boolean complete = false;
		RoadNode current = nArr;
		res.add(current.getPosition());
		while (!complete) {
			if (predecessor.get(current) == null) return new ArrayList<>();
			current = predecessor.get(current);
			res.add(current.getPosition());
			if (current.equals(nDep)) complete = true;
		}
		Collections.reverse(res);
		if (res.size() > 1 && res.get(0).distance(nArr.getPosition()) > start.distance(nArr.getPosition())) res.remove(0);
		if (res.size() > 1) {
			Point2D p1 = res.get(res.size()-1);
			Point2D p2 = res.get(res.size()-2);
			if ((((p1.getX() <= arrival.getX() && arrival.getX() <= p2.getX()) 
				|| (p2.getX() <= arrival.getX() && arrival.getX() <= p1.getX())) 
				&& ((p1.getY() <= arrival.getY() && arrival.getY() <= p2.getY()) 
				|| (p2.getY() <= arrival.getY() && arrival.getY() <= p1.getY())))) {
				res.remove(res.size()-1);
			}
		}
		res.add(arrival);
		} catch (Exception e) {res.add(arrival); return res;}
		return res;
	}
	
	/**
	 * Gives the way for going from start to arrival for an agent of type agent
	 * @param start the start point
	 * @param arrival the destination of the agent
	 * @param type the type of the agent
	 * @return the list of points where the agent has to go
	 */
	public List<Point2D> wayToGoFollowingType (Point2D start, Point2D arrival, String type) {
		List<Point2D> res = new ArrayList<>();
		RoadGraph subGraph = null;
		if (type.equals("bike")) {
			subGraph = this.getSubGraph(true, false, true, true);
		} else if (type.equals("car")) {
			subGraph = this.getSubGraph(true, false, false, true);
		} else if (type.equals("bus")) {
			subGraph = this.getSubGraph(true, false, false, false);
		} else { // The persons
			subGraph = this.getSubGraph(true, true, true, true);
		}
		RoadEdge dep = null, arr = null;
		//We search the edge where we want to go
		for (RoadEdge re : subGraph.roads) {
			if (re.isOnTheRoad(start)) {dep = re;}
			if (inTheLimits(arrival)) {
				if (re.isOnTheRoad(arrival) && isAStreet(re.getType())) arr = re;
			} else {
				if (re.isOnTheRoad(arrival)) {arr = re;}
			}
		}
		try {
			RoadNode nDep = dep.getFirstRoadNode();
			RoadNode nArr = arr.getSecondRoadNode();
			double[] dis = new double[subGraph.nodes.keySet().size()];
			Set<RoadNode> notDone = new HashSet<>();
			RoadNode[] tableNodes = new RoadNode[subGraph.nodes.keySet().size()];
			Map<RoadNode,RoadNode> predecessor = new HashMap<>();
			Map<RoadNode,Integer> index = new HashMap<>();
			int cpt = 0;
			//Tables initialization
			for (RoadNode rn : subGraph.nodes.keySet()) {
				if (rn.equals(nDep))  dis[cpt] = 0;
				else dis[cpt] = Double.MAX_VALUE;
				tableNodes[cpt] = rn;
				notDone.add(rn);
				index.put(rn, cpt);
				cpt++;
			}
			//Research
			while (notDone.size() != 0) {
				int ind = lowestIndex(dis, notDone, tableNodes);
				RoadNode n = tableNodes[ind];
				for (RoadNode rn : subGraph.getAdjacentNodes(n)) {
					int p = index.get(rn);
					int pr = index.get(n);
					double distance = distance(n,rn);
					double factor = getFactorFollowingType(getTypeEdge(n, rn));
					if (dis[p] > dis[pr]+distance*factor && !(inTheLimits(rn.getPosition()) && !rn.getPosition().equals(arrival))) {
						dis[p] = dis[pr]+distance*factor;
						predecessor.put(rn, n);
					}
				}
				notDone.remove(n);
			}
			boolean complete = false;
			RoadNode current = nArr;
			res.add(current.getPosition());
			while (!complete) {
				if (predecessor.get(current) == null) return new ArrayList<>();
				current = predecessor.get(current);
				res.add(current.getPosition());
				if (current.equals(nDep)) complete = true;
			}
			Collections.reverse(res);
			if (res.size() > 1 && res.get(0).distance(nArr.getPosition()) > start.distance(nArr.getPosition())) res.remove(0);
			if (res.size() > 1) {
				Point2D p1 = res.get(res.size()-1);
				Point2D p2 = res.get(res.size()-2);
				if (!(((p2.getX() <= p1.getX() && p1.getX() <= arrival.getX()) 
					|| (arrival.getX() <= p1.getX() && p1.getX() <= p2.getX())) 
					&& ((p2.getY() <= p1.getY() && p1.getY() <= arrival.getY()) 
					|| (arrival.getY() <= p1.getY() && p1.getY() <= p2.getY())))
						|| p1.equals(arrival) || p1.equals(p2)) {
					res.remove(res.size()-1);
				}
			}
		} catch (Exception e) {e.printStackTrace();res.add(arrival); return res;}
		return res;
	}
	
	/**
	 * Gives the way for agent in the creation of the transports
	 * @param start the place where the transport is created
	 * @param arrival the destination of the agent
	 * @param type the type of the transport 
	 * @return the way for the agent for going from start to arrival
	 */
	public List<Point2D> wayToGoInTransport (Point2D start, Point2D arrival, String type) {
		List<Point2D> res = new ArrayList<>();
		RoadEdge dep = null, arr = null;
		//We search the edge where we want to go
		for (RoadEdge re : roads) {
			if (re.getType().equals(type) && dep == null) dep =re;
			if (re.getType().equals(type) && arr == null) dep = re; 
			if (re.isOnTheRoad(start)) {dep = re;}
			if (inTheLimits(arrival)) {
				if (re.isOnTheRoad(arrival) && isAStreet(re.getType())) arr = re;
			} else {
				if (re.isOnTheRoad(arrival)) {arr = re;}
			}
		}
		RoadNode nDep = dep.getFirstRoadNode();
		RoadNode nArr = arr.getSecondRoadNode();
		double[] dis = new double[nodes.keySet().size()];
		Set<RoadNode> notDone = new HashSet<>();
		RoadNode[] tableNodes = new RoadNode[nodes.keySet().size()];
		Map<RoadNode,RoadNode> predecessor = new HashMap<>();
		Map<RoadNode,Integer> index = new HashMap<>();
		int cpt = 0;
		//Tables initialization
		for (RoadNode rn : nodes.keySet()) {
			if (rn.equals(nDep))  dis[cpt] = 0;
			else dis[cpt] = Double.MAX_VALUE;
			tableNodes[cpt] = rn;
			notDone.add(rn);
			index.put(rn, cpt);
			cpt++;
		}
		//Research
		while (notDone.size() != 0) {
			int ind = lowestIndex(dis, notDone, tableNodes);
			RoadNode n = tableNodes[ind];
			for (RoadNode rn : getAdjacentNodes(n)) {
				int p = index.get(rn);
				int pr = index.get(n);
				double distance = distance(n,rn);
				double factor = getFactorFollowingType(getTypeEdge(n, rn));
				if (dis[p] > dis[pr]+distance*factor) {
					dis[p] = dis[pr]+distance*factor;
					predecessor.put(rn, n);
				}
			}
			notDone.remove(n);
		}
		boolean complete = false;
		RoadNode current = nArr;
		res.add(current.getPosition());
		while (!complete) {
			if (predecessor.get(current) == null) {
				res.add(arrival);
				return res;
			}
			current = predecessor.get(current);
			res.add(current.getPosition());
			if (current.equals(nDep)) complete = true;
		}
		Collections.reverse(res);
		if (res.size() > 1 && res.get(0).distance(nArr.getPosition()) > start.distance(nArr.getPosition())) res.remove(0);
		if (res.size() > 1) {
			Point2D p1 = res.get(res.size()-1);
			Point2D p2 = res.get(res.size()-2);
			if ((((p1.getX() <= arrival.getX() && arrival.getX() <= p2.getX()) 
				|| (p2.getX() <= arrival.getX() && arrival.getX() <= p1.getX())) 
				&& ((p1.getY() <= arrival.getY() && arrival.getY() <= p2.getY()) 
				|| (p2.getY() <= arrival.getY() && arrival.getY() <= p1.getY())))) {
				res.remove(res.size()-1);
			}
		}
		res.add(arrival);
		return res;
	}
	
	/**
	 * Gives the index of the lowest value available
	 * @param table the table of lowest distance
	 * @param done the set of the nodes not visited
	 * @param nodes the tables of the nodes
	 * @return the index of the lowest value not visited
	 */
	private int lowestIndex (double[] table, Set<RoadNode> done, RoadNode[] nodes) {
		boolean init = false;
		int ind =-1;
		double val=-1;
		for (int i = 0; i < table.length; i++) {
			if (done.contains(nodes[i])) {
				if (!init) {
					val = table[i];
					ind = i;
					init = true;
				} else {
					if (table[i] < val) {
						val = table[i];
						ind = i;
					}
				}
			}
		}
		return ind;
	}
	
	/**
	 * Gives the adjacent node to a node
	 * @param node the node which we want to know the adjacent nodes
	 * @return the adjacent nodes
	 */
	private List<RoadNode> getAdjacentNodes (RoadNode node) {
		List<RoadEdge> adjacents = nodes.get(node);
		List<RoadNode> res = new ArrayList<>();
		for (int i=0; i < adjacents.size(); i++) {
			if (adjacents.get(i).getFirstRoadNode().equals(node))
				res.add(adjacents.get(i).getSecondRoadNode());
			else
				res.add(adjacents.get(i).getFirstRoadNode());
		}
		return res;
	}
	
	/**
	 * Gives the distance between 2 nodes
	 * @param rn1 the first node
	 * @param rn2 the second node
	 * @return the distance between the 2 nodes
	 */
	private double distance (RoadNode rn1, RoadNode rn2) {
		for (RoadEdge re : nodes.get(rn1)) {
			if (re.getFirstRoadNode().equals(rn2) || re.getSecondRoadNode().equals(rn1))
				return re.length();
		}
		return 0;
	}
	
	/**
	 * Returns the type of the edge between two road nodes
	 * @param rn1 the first road node
	 * @param rn2 the second road node
	 * @return the type of road between the two nodes
	 */
	private String getTypeEdge (RoadNode rn1, RoadNode rn2) {
		for (RoadEdge re : nodes.get(rn1)) {
			if (re.getFirstRoadNode().equals(rn2) || re.getSecondRoadNode().equals(rn2))
				return re.getType();
		}
		return null;
	}
	
	/**
	 * Gives the factor to apply following the type of road
	 * @param type the type of road
	 * @return the factor associate to the type of road
	 */
	private double getFactorFollowingType (String type) {
		if (type.equals("Tramway"))
			return 0.5;
		else if (type.equals("Railway")) {
			return 0;
		} else if (type.equals("Busway")) {
			return 1;
		} else
			return 1;
	}
	
	/**
	 * Indicates if a point is on the limits
	 * @param pt
	 * @return
	 */
	private boolean inTheLimits (Point2D pt) {
		return FastMath.areEqual(pt.getX(), 0)
			|| FastMath.areEqual(pt.getX()+1, height)
			&& FastMath.areEqual(pt.getY(), 0)
			&& FastMath.areEqual(pt.getY()+1, width);
	}
	
	/**
	 * Indicates if a road is a street
	 * @param type the type of the road
	 * @return true if the road is a street, false else
	 */
	private boolean isAStreet (String type) {
		return type.equals("Residential") || type.equals("Secondary") || type.equals("Tertiary");
	}
	
	/**
	 * Gives a sub graph following we want to consider the street, the busway, the tramway and the railway
	 * @param street if we take the streets
	 * @param busway if we take the busway
	 * @param tramway if we take the tramway
	 * @param railway if the we take the railway
	 * @return the sub graph with the chosen element
	 */
	private RoadGraph getSubGraph (boolean street, boolean busway, boolean tramway, boolean railway) {
		RoadGraph res = new RoadGraph(height, width);
		for (RoadEdge re : roads) {
			if (
				isAStreet(re.getType()) && street
			 || re.getType().equals("Busway") && busway
			 || re.getType().equals("Tramway") && tramway
			 || re.getType().equals("Railway") && railway
			) {
				res.addRoadEdge(re);
			}
		}
		return res;
	}
	
	private boolean compatibleType (String t1, String t2) {
		if (t1.equals("Street") || isAStreet(t1)) {
			return t2.equals("Street") || isAStreet(t2);
		} else {
			return t1.equals(t2);
		}
	}
}