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

import static fr.univ_artois.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;

import static net.jafama.FastMath.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.BusLine;

/**
 * Extracts and provides the data from the OSM data
 * The goal is to read the OSM file the less times possible.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * 
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
	 * @param osmData the OSM data
	 */
	public DataFromOSM (InputStream osmData) {
		this.nodes = new HashMap<>();
		this.ways = new HashMap<>();
		this.relations = new HashMap<>();
		this.readOSMData(osmData);
	}
	
	/**
	 * Reads the OSM file and extracts the data
	 * @param file the path to the OSM file
	 */
	private void readOSMData (InputStream osmData) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(osmData);
				Element e = doc.getDocumentElement();
				NodeList nl = e.getChildNodes();
				for (int i=0; i < nl.getLength(); i++) {
					Node n = nl.item(i);
					switch(n.getNodeName()) {
						case BOUNDS: //Recover the borders of the world
							addBounds(n);
							break;
						case NODE: //Recover the nodes
							addNode(n);
							break;
						case WAY: //Recover the ways
							addWay(n);
							break;
						case RELATION: //Recover the relations
							addRelation(n);
							break;
					}
				}
			} catch (ParserConfigurationException e) {
				throw new ReadOSMException(e);
			} catch (SAXException e) {
				throw new ReadOSMException(e);
			} catch (IOException e) {
				throw new ReadOSMException(e);
			}
			
	}
	
	private void addBounds(Node n) {
		minlon = (int) (Double.parseDouble(
			n.getAttributes().getNamedItem(MINLON).getNodeValue())*pow(10, 7)
		);
		maxlon = (int) (Double.parseDouble(
			n.getAttributes().getNamedItem(MAXLON).getNodeValue())*pow(10, 7)
		);
		minlat = (int) (Double.parseDouble(
			n.getAttributes().getNamedItem(MINLAT).getNodeValue())*pow(10, 7)
		);
		maxlat = (int) (Double.parseDouble(
			n.getAttributes().getNamedItem(MAXLAT).getNodeValue())*pow(10, 7)
		);
	}
	
	private void addNode(Node n) {
		String id = n.getAttributes().getNamedItem(ID).getNodeValue();
		double lat = Double.parseDouble(
			n.getAttributes().getNamedItem(LAT).getNodeValue()
		);
		double lon = Double.parseDouble(
			n.getAttributes().getNamedItem(LON).getNodeValue()
		);
		OSMNode on = new OSMNode(lon, lat);
		if (n.hasChildNodes()) {
			NodeList children = n.getChildNodes();
			for (int j=0; j < children.getLength(); j++) {
				Node n2 = children.item(j);
				if (n2.getNodeName().equals(TAG)) {
					String key = n2.getAttributes().getNamedItem(K).getNodeValue();
					String value = n2.getAttributes().getNamedItem(V).getNodeValue();
					on.addTag(key, value);
				}
			}
		}
		this.nodes.put(id, on);
	}
	
	private void addWay(Node n) {
		String id = n.getAttributes().getNamedItem(ID).getNodeValue();
		OSMWay ow = new OSMWay();
		if (n.hasChildNodes()) {
			NodeList children = n.getChildNodes();
			for (int j=0; j < children.getLength(); j++) {
				Node n2 = children.item(j);
				//If we have a node
				if (n2.getNodeName().equals(ND)) {
					String ref = n2.getAttributes().getNamedItem(REF).getNodeValue();
					ow.addNode(ref);
				//Or a way
				} else if (n2.getNodeName().equals(TAG)) {
					String key = n2.getAttributes().getNamedItem(K).getNodeValue();
					String value = n2.getAttributes().getNamedItem(V).getNodeValue();
					ow.addTag(key, value);
				}
			}
		}
		this.ways.put(id, ow);
	}
	
	private void addRelation(Node n) {
		String id = n.getAttributes().getNamedItem(ID).getNodeValue();
		OSMRelation relation = new OSMRelation();
		if (n.hasChildNodes()) {
			NodeList children = n.getChildNodes();
			for (int j=0; j < children.getLength(); j++) {
				Node n2 = children.item(j);
				//if we have a member
				if (n2.getNodeName().equals(MEMBER)) {
					String type = n2.getAttributes().getNamedItem(TYPE).getNodeValue();
					if (type.equals(WAY)) {
						relation.addWay(n2.getAttributes().getNamedItem(REF).getNodeValue());
					} else if (type.equals(NODE)) {
						relation.addNode(n2.getAttributes().getNamedItem(REF).getNodeValue());
					}
				} else if (n2.getNodeName().equals(TAG)) {
					//or a tag
					relation.addTag(
						n2.getAttributes().getNamedItem(K).getNodeValue(),
						n2.getAttributes().getNamedItem(V).getNodeValue()
					);
				}
			}
		}
		this.relations.put(id, relation);
	}
	
	/**
	 * Give the height of the simulation
	 * The size is divided by 100 for that the simulations aren't too big
	 * @return the height for the simulation
	 */
	public int getHeight () {
		int dis = (int) round(distance(minlat, maxlon, maxlat, maxlon,0,0)/pow(10, 4));
		return dis;
	}
	
	/**
	 * Give the width of the simulation
	 * The size is divided by 100 for that the simulations aren't too big
	 * @return the width for the simulation
	 */
	public int getWidth () {
		int dis = (int) round(distance(minlat,minlon,minlat,maxlon,0,0)/pow(10, 4));
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
			if (ow.isTertiaryRoad()) {
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
			if (relations.get(rel).getTags().containsKey(ROUTE) &&
					relations.get(rel).getTags().get(ROUTE).equals(BUS)) {
				BusLine bl = new BusLine(relations.get(rel).getTags().get(REF));
				for (String s : relations.get(rel).getNodes()) {
					if (nodes.containsKey(s) && nodes.get(s).isBusStop()) {
						bl.addIdBusStop(s);
					}
				}
				if (!bl.noIdBusStop()) {
					res.add(bl);
				}
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
		double x = (int) round(((double) (lon-minlon)/(maxlon-minlon))*getWidth());
		double y = (int) round(((double) (lat-maxlat)/(minlat-maxlat))*getHeight());
		Point2D pt = new Point2D.Double(x,y);
		return pt;
	}
	
	/**
	 * Calculate distance between two points
	 * using the <a href="https://en.wikipedia.org/wiki/Haversine_formula">
	 * Haversine formula</a>.
	 * 
	 * @param lat1 the latitude of the first point in meters
	 * @param lat2 the latitude of the second point in meters
	 * @param lon1 the longitude of the first point in meters
	 * @param lon2 the longitude of the second point in meters
	 * @param el1 the elevation of the first point in meters
	 * @param el2 the elevation of the second point in meters
	 * @return the distance in meters
	 */
	public static double distance(
		double lat1,
		double lat2,
		double lon1,
	    double lon2,
	    double el1,
	    double el2
	   ) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = toRadians(lat2 - lat1);
	    double lonDistance = toRadians(lon2 - lon1);
	    double a = sin(latDistance / 2) * sin(latDistance / 2)
	            + cos(toRadians(lat1)) * cos(toRadians(lat2))
	            * sin(lonDistance / 2) * sin(lonDistance / 2);
	    double c = 2 * atan2(sqrt(a), sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = pow(distance, 2) + pow(height, 2);

	    return sqrt(distance);
	}
}
