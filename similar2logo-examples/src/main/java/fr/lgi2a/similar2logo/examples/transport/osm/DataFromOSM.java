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
package fr.lgi2a.similar2logo.examples.transport.osm;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.lgi2a.similar2logo.examples.transport.model.places.BusLine;

/**
 * Extracts and provides the data from the OSM data
 * The goal is to read the OSM file the less times possible.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class DataFromOSM {

	/**
	 * The nodes from OSM
	 */
	private Map<String,OSMNode> nodes;
	
	/**
	 * The ways from OSM
	 */
	private Map<String,OSMWay> ways;
	
	/**
	 * The relations from OSM
	 */
	private Map<String,OSMRelation> relations;
	
	/**
	 * The latitude minimum
	 */
	private int minlat;
	
	/**
	 * The latitude maximum
	 */
	private int maxlat;
	
	/**
	 * The longitude minimum
	 */
	private int minlon;
	
	/**
	 * The longitude maximum
	 */
	private int maxlon;
	
	/**
	 * Constructor of the data extractor of OSM
	 * @param file the path of the OSM file
	 */
	public DataFromOSM (String file) {
		this.nodes = new HashMap<>();
		this.ways = new HashMap<>();
		this.relations = new HashMap<>();
		this.readOSMData(file);
	}
	
	/**
	 * Reads the OSM file and extracts the data
	 * @param file the path to the OSM file
	 */
	private void readOSMData (String file) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(file));
			Element e = doc.getDocumentElement();
			NodeList nl = e.getChildNodes();
			for (int i=0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				//We recover the borders of the world
				if (n.getNodeName().equals("bounds")) {
					minlon = (int) (Double.parseDouble(n.getAttributes().getNamedItem("minlon").getNodeValue())*Math.pow(10, 7));
					maxlon = (int) (Double.parseDouble(n.getAttributes().getNamedItem("maxlon").getNodeValue())*Math.pow(10, 7));
					minlat = (int) (Double.parseDouble(n.getAttributes().getNamedItem("minlat").getNodeValue())*Math.pow(10, 7));
					maxlat = (int) (Double.parseDouble(n.getAttributes().getNamedItem("maxlat").getNodeValue())*Math.pow(10, 7));
				//We recover the nodes
				} else if (n.getNodeName().equals("node")) {
					String id = n.getAttributes().getNamedItem("id").getNodeValue();
					double lat = Double.parseDouble(n.getAttributes().getNamedItem("lat").getNodeValue());
					double lon = Double.parseDouble(n.getAttributes().getNamedItem("lon").getNodeValue());
					OSMNode on = new OSMNode(lon, lat);
					if (n.hasChildNodes()) {
						NodeList children = n.getChildNodes();
						for (int j=0; j < children.getLength(); j++) {
							Node n2 = children.item(j);
							if (n2.getNodeName().equals("tag")) {
								String key = n2.getAttributes().getNamedItem("k").getNodeValue();
								String value = n2.getAttributes().getNamedItem("v").getNodeValue();
								on.addTag(key, value);
							}
						}
					}
					this.nodes.put(id, on);
				//We recover the way
				} else if (n.getNodeName().equals("way")) {
					String id = n.getAttributes().getNamedItem("id").getNodeValue();
					OSMWay ow = new OSMWay();
					if (n.hasChildNodes()) {
						NodeList children = n.getChildNodes();
						for (int j=0; j < children.getLength(); j++) {
							Node n2 = children.item(j);
							//If we have a node
							if (n2.getNodeName().equals("nd")) {
								String ref = n2.getAttributes().getNamedItem("ref").getNodeValue();
								ow.addNode(ref);
							//Or a way
							} else if (n2.getNodeName().equals("tag")) {
								String key = n2.getAttributes().getNamedItem("k").getNodeValue();
								String value = n2.getAttributes().getNamedItem("v").getNodeValue();
								ow.addTag(key, value);
							}
						}
					}
					this.ways.put(id, ow);
				} else if (n.getNodeName().equals("relation")) {
					String id = n.getAttributes().getNamedItem("id").getNodeValue();
					OSMRelation relation = new OSMRelation();
					if (n.hasChildNodes()) {
						NodeList children = n.getChildNodes();
						for (int j=0; j < children.getLength(); j++) {
							Node n2 = children.item(j);
							//if we have a member
							if (n2.getNodeName().equals("member")) {
								String type = n2.getAttributes().getNamedItem("type").getNodeValue();
								if (type.equals("way")) {
									relation.addWay(n2.getAttributes().getNamedItem("ref").getNodeValue());
								} else if (type.equals("node")) {
									relation.addNode(n2.getAttributes().getNamedItem("ref").getNodeValue());
								}
							}
							//or a tag
							else if (n2.getNodeName().equals("tag")) {
								relation.addTag(n2.getAttributes().getNamedItem("k").getNodeValue(),
										n2.getAttributes().getNamedItem("v").getNodeValue());
							}
						}
					}
					this.relations.put(id, relation);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Give the height of the simulation
	 * The size is divided by 100 for that the simulations aren't too big
	 * @return the height for the simulation
	 */
	public int getHeight () {
		int dis = (int) Math.round(distance(minlat, maxlon, maxlat, maxlon,0,0)/Math.pow(10, 4));
		return dis;
	}
	
	/**
	 * Give the width of the simulation
	 * The size is divided by 100 for that the simulations aren't too big
	 * @return the width for the simulation
	 */
	public int getWidth () {
		int dis = (int) Math.round(distance(minlat,minlon,minlat,maxlon,0,0)/Math.pow(10, 4));
		return dis;
	}
	
	/**
	 * Gives the highways in the OSM file
	 * Each list contains a highway
	 * @return the list of the highway
	 */
	public List<List<String>> getHighway () {
		List<List<String>> res = new ArrayList<>();
		Set<String> keys = this.ways.keySet();
		for (String s : keys) {
			OSMWay ow = (OSMWay) this.ways.get(s);
			if (ow.isHighway()) {
				res.add(ow.getNodes());
			}
		}
		return res;
	}
	
	/**
	 * Gives the secondary roads in the OSM file
	 * @return the list of the secondary roads
	 */
	public List<List<String>> getSecondaryRoads () {
		List<List<String>> res = new ArrayList<>();
		Set<String> keys = this.ways.keySet();
		for (String s : keys) {
			OSMWay ow = (OSMWay) this.ways.get(s);
			if (ow.isSecondaryRoad()) {
				res.add(ow.getNodes());
			}
		}
		return res;
	}
	
	/**
	 * Gives the tertiary roads in the OSM file
	 * @return the list of the tertiary roads
	 */
	public List<List<String>> getTerciaryRoads () {
		List<List<String>> res = new ArrayList<>();
		Set<String> keys = this.ways.keySet();
		for (String s : keys) {
			OSMWay ow = (OSMWay) this.ways.get(s);
			if (ow.isTerciaryRoad()) {
				res.add(ow.getNodes());
			}
		}
		return res;
	}
	
	/**
	 * Gives the residential roads in the OSM file
	 * @return the list of the residential roads
	 */
	public List<List<String>> getResidentialRoads () {
		List<List<String>> res = new ArrayList<>();
		Set<String> keys = this.ways.keySet();
		for (String s : keys) {
			OSMWay ow = (OSMWay) this.ways.get(s);
			if (ow.isResidentialRoad()) {
				res.add(ow.getNodes());
			}
		}
		return res;
	}
	
	
	/**
	 * Gives the railways in the OSM file.
	 * @return the list of the railways
	 */
	public List<List<String>> getRailway () {
		List<List<String>> res = new ArrayList<>();
		Set<String> keys = this.ways.keySet();
		for (String s : keys) {
			OSMWay ow = (OSMWay) this.ways.get(s);
			if (ow.isRailway()) {
				res.add(ow.getNodes());
			}
		}
		return res;
	}
	
	/**
	 * Gives the tramway ways in the OSM file.
	 * @return the list of the rails of the tramway
	 */
	public List<List<String>> getTramway () {
		List<List<String>> res = new ArrayList<>();
		Set<String> keys = this.ways.keySet();
		for (String s : keys) {
			OSMWay ow = (OSMWay) this.ways.get(s);
			if (ow.isTramway()) {
				res.add(ow.getNodes());
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of the stations
	 * @return the list of the station
	 */
	public List<String> getStations () {
		List<String> res = new ArrayList<>();
		Set<String> keys = this.nodes.keySet();
		for (String s : keys) {
			if (this.nodes.get(s).isStation()) {
				res.add(s);
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of the level crossing
	 * @return the list of the level crossing
	 */
	public List<String> getLevelCrossing () {
		List<String> res = new ArrayList<>();
		Set<String> keys = this.nodes.keySet();
		for (String s : keys) {
			if (this.nodes.get(s).isLevelCrossing()) {
				res.add(s);
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of the tram stops.
	 * @return the list of tram stop
	 */
	public List<String> getTramStops () {
		List<String> res = new ArrayList<>();
		Set<String> keys = this.nodes.keySet();
		for (String s : keys) {
			if (this.nodes.get(s).isTramStop()) {
				res.add(s);
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of the bus stops
	 * @return the list of bus stops
	 */
	public List<String> getBusStops () {
		List<String> res = new ArrayList<>();
		Set<String> keys = this.nodes.keySet();
		for (String s : keys) {
			if (this.nodes.get(s).isBusStop()) {
				res.add(s);
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of the schools.
	 * @return the list of the schools.
	 */
	public List<String> getSchools () {
		List<String> res = new ArrayList<>();
		Set<String> keys = this.nodes.keySet();
		for (String s : keys) {
			if (this.nodes.get(s).isSchool()) {
				res.add(s);
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of the restaurants.
	 * @return the list of the restaurants
	 */
	public List<String> getRestaurants () {
		List<String> res = new ArrayList<>();
		Set<String> keys = this.nodes.keySet();
		for (String s : keys) {
			if (this.nodes.get(s).isRestaurant()) {
				res.add(s);
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of the banks
	 * @return the list of the banks
	 */
	public List<String> getBanks () {
		List<String> res = new ArrayList<>();
		Set<String> keys = this.nodes.keySet();
		for (String s : keys) {
			if (this.nodes.get(s).isBank()) {
				res.add(s);
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of the doctors
	 * @return the list of the doctors
	 */
	public List<String> getDoctors () {
		List<String> res = new ArrayList<>();
		Set<String> keys = this.nodes.keySet();
		for (String s : keys) {
			if (this.nodes.get(s).isMedecine()) {
				res.add(s);
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of the shops
	 * @return the list of the shops
	 */
	public List<String> getShops () {
		List<String> res = new ArrayList<>();
		Set<String> keys = this.nodes.keySet();
		for (String s : keys) {
			if (this.nodes.get(s).isShop()) {
				res.add(s);
			}
		}
		return res;
	}
	
	/**
	 * Gives the bus lines
	 * @return the list of the bus lines
	 */
	public List<BusLine> getBusLines () {
		List<BusLine> res = new ArrayList<>();
		for (String rel : relations.keySet()) {
			//We find all the relations that are bus lines
			if (relations.get(rel).getTags().containsKey("route") &&
					relations.get(rel).getTags().get("route").equals("bus")) {
				BusLine bl = new BusLine(relations.get(rel).getTags().get("ref"));
				for (String s : relations.get(rel).getNodes()) {
					if (nodes.containsKey(s) && nodes.get(s).isBusStop()) {
						bl.addIdBusStop(s);
					}
				}
				if (!bl.noIdBusStop()) res.add(bl);
			}
		}
		return res;
	}
	
	/**
	 * Gives the Point2D associates to a id in the file
	 * @param id the id of the point to search
	 * @return the coordinates of the id
	 */
	public Point2D getCoordinates (String id) {
		int lat = this.nodes.get(id).getLatitude();
		int lon = this.nodes.get(id).getLongitude();
		double x = (int) Math.round(((double) (lon-minlon)/(maxlon-minlon))*getWidth());
		double y = (int) Math.round(((double) (lat-maxlat)/(minlat-maxlat))*getHeight());
		Point2D pt = new Point2D.Double(x,y);
		return pt;
	}
	
	/**
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If you are not interested in height
	 * difference pass 0.0. Uses Haversine method as its base.
	 * 
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
	 * el2 End altitude in meters
	 * @returns Distance in Meters
	 */
	public static double distance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}
}
