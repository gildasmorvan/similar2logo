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
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
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
package fr.lgi2a.similar2logo.examples.transport;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.transport.model.TransportSimulationParameters;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Model for the transport simulation.
 * The environment is created from real data import from <a href="http://openstreetmap.fr/">Open Street Map</a>.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportSimulationModel extends LogoSimulationModel {
	
	/**
	 * The path where are the data to import in the simulation.
	 */
	private String data;
	
	/**
	 * Border west of the simulation.
	 */
	private int minlon;
	
	/**
	 * Border east of the simulation.
	 */
	private int maxlon;
	
	/**
	 * Border north of the simulation
	 */
	private int minlat;
	
	/**
	 * Border south of the simulation.
	 */
	private int maxlat;

	public TransportSimulationModel(LogoSimulationParameters parameters, String path) {
		super(parameters);
		this.data = path;
		TransportSimulationParameters tsp = (TransportSimulationParameters) parameters;
		this.minlon =0;
		this.maxlon = tsp.gridWidth;
		this.minlat = tsp.gridHeight;
		this.maxlat = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		TransportSimulationParameters tsp = (TransportSimulationParameters) simulationParameters;
		AgentInitializationData aid = new AgentInitializationData();
		return aid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected EnvironmentInitializationData generateEnvironment (ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		TransportSimulationParameters tsp = (TransportSimulationParameters) simulationParameters;
		return this.readMap(tsp, levels);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters) {
		TransportSimulationParameters castedSimulationParameters = (TransportSimulationParameters) simulationParameters;
		ExtendedLevel logo = new ExtendedLevel(
					castedSimulationParameters.getInitialTime(), 
					LogoSimulationLevelList.LOGO, 
					new PeriodicTimeModel( 
							1, 
							0, 
							castedSimulationParameters.getInitialTime()
							),
					new LogoDefaultReactionModel()
					);
		List<ILevel> levelList = new LinkedList<ILevel>();
		levelList.add(logo);
		return levelList;	
	}
	
	/**
	 * Read the environment extracts from Open Street Map.
	 * @param tsp the parameters of the simulation
	 * @param levels the level of the simulation
	 * @return the environment for the simulation
	 */
	protected EnvironmentInitializationData readMap (TransportSimulationParameters tsp, Map<LevelIdentifier, ILevel> levels ) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(this.data));
			Element e = doc.getDocumentElement();
			NodeList nl = e.getChildNodes();
			for (int i=0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				if (n.getNodeName().equals("bounds")) {
					//We recover the borders of the world
					minlon = (int) (Double.parseDouble(n.getAttributes().getNamedItem("minlon").getNodeValue())*Math.pow(10, 7));
					maxlon = (int) (Double.parseDouble(n.getAttributes().getNamedItem("maxlon").getNodeValue())*Math.pow(10, 7));
					minlat = (int) (Double.parseDouble(n.getAttributes().getNamedItem("minlat").getNodeValue())*Math.pow(10, 7));
					maxlat = (int) (Double.parseDouble(n.getAttributes().getNamedItem("maxlat").getNodeValue())*Math.pow(10, 7));
					//We set the size of the world
					tsp.setSize((maxlat-minlat)/100+1,(maxlon-minlon)/100+1 );
				}
			}
			//We search the nodes that belong to the street
			Set<String> streets = new HashSet<>();
			List<List<String>> ptsToLink = new ArrayList<>();
			for (int i=0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				//We recover the way of the streets
				if (n.getNodeName().equals("way")) {
					NodeList way = n.getChildNodes();
					for (int j=0; j < way.getLength(); j++) {
						if (way.item(j).getNodeName().equals("tag") && way.item(j).getAttributes().getNamedItem("k").getNodeValue().equals("highway")) {
							List<String> pts = new ArrayList<>();
							for (int k=0; k < way.getLength(); k++) {
								if (way.item(k).getNodeName().equals("nd")) {
									streets.add(way.item(k).getAttributes().getNamedItem("ref").getNodeValue());
									pts.add(way.item(k).getAttributes().getNamedItem("ref").getNodeValue());
								}
							}
							ptsToLink.add(pts);
						}
					}
				}
			}
			//We add the point of the street in the simulation
			//Creation of the environment with the good size.
			EnvironmentInitializationData eid = super.generateEnvironment(tsp, levels);
			LogoEnvPLS environment = (LogoEnvPLS) eid.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
			for (int i=0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				if (n.getNodeName().equals("node") && streets.contains(n.getAttributes().getNamedItem("id").getNodeValue())) {
					int lat = (int) (Double.parseDouble(n.getAttributes().getNamedItem("lat").getNodeValue())*Math.pow(10, 7));
					int lon = (int) (Double.parseDouble(n.getAttributes().getNamedItem("lon").getNodeValue())*Math.pow(10, 7));
					if ((lat >= minlat) && (lat <= maxlat) && (lon >= minlon) && (lon <= maxlon)) {
						int x = Math.round((lon - minlon)/100);
						int y = Math.round((maxlat - lat)/100);
						Point2D pt = new Point2D.Double((double) x, (double) y);
						environment.getMarksAt((int) pt.getX(), (int) pt.getY() ).add(new Mark<Double>(pt, (double) 0, "Street"));
					}
				}
			}
			linkPoints (ptsToLink, environment);
			return eid;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Determines the point to link
	 * @param pts the list of vectors of points
	 * @param lep the environment
	 */
	protected void linkPoints (List<List<String>> pts, LogoEnvPLS lep) {
		for (List<String> liste : pts) {
			for (int i=0; i < liste.size() -1; i++) {
				Point2D ori = getCoordinates(data, liste.get(i));
				Point2D des = getCoordinates(data, liste.get(i+1));
				if ((ori != null) && (des != null))
					printRoadBetweenTwoPoints(ori, des, lep);
			}
		}
	}
	
	/**
	 * Gives the Point2D associates to a id in the file
	 * @param file data from OSM
	 * @param id the id of the point to search
	 * @return the coordinates of the id
	 */
	protected Point2D getCoordinates (String file, String id) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(this.data));
			Element e = doc.getDocumentElement();
			NodeList nl = e.getChildNodes();
			for (int i=0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				if (n.getNodeName().equals("node") && n.getAttributes().getNamedItem("id").getNodeValue().equals(id)) {
					int lat = (int) (Double.parseDouble(n.getAttributes().getNamedItem("lat").getNodeValue())*Math.pow(10, 7));
					int lon = (int) (Double.parseDouble(n.getAttributes().getNamedItem("lon").getNodeValue())*Math.pow(10, 7));
					int x = Math.round((lon - minlon)/100);
					int y = Math.round((maxlat - lat)/100);
					Point2D pt = new Point2D.Double((double) x, (double) y);
					return pt;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Add the street marks between ori and des
	 * @param ori the point of origin
	 * @param des the point of destination
	 * @param lep the environment
	 */
	protected void printRoadBetweenTwoPoints (Point2D ori, Point2D des, LogoEnvPLS lep) {
		if (!ori.equals(des)) {
			//we test all the 8 directions for knowing what is the best way
			Point2D nextPosition = new Point2D.Double(ori.getX()+1, ori.getY()); //We start by the south
			double bestDistance = Point2D.distance(nextPosition.getX(), nextPosition.getY(), des.getX(), des.getY());
			for (int i = -1 ; i <=1; i++) {
				for (int j= -1; j <= 1; j++) {
					if (!((i ==0) && (j ==0)) && !((i == 1) && (j == 0))) {
						Point2D testPoint = new Point2D.Double(ori.getX()+i, ori.getY()+j);
						double distance = Point2D.distance(testPoint.getX(), testPoint.getY(), des.getX(), des.getY());
						if (distance < bestDistance) {
							nextPosition = testPoint;
							bestDistance = distance;
						}
					}
				}
			}
			if ((nextPosition.getY() >= 0) && (nextPosition.getY() < lep.getHeight()) && 
					(nextPosition.getX() >= 0) && (nextPosition.getX() < lep.getWidth())) {
				lep.getMarksAt((int) nextPosition.getX(), (int) nextPosition.getY() ).add(new Mark<Double>(nextPosition, (double) 0, "Street"));
			}
			printRoadBetweenTwoPoints(nextPosition, des, lep);
		}
	}
	


}
