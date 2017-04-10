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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	public TransportSimulationModel(LogoSimulationParameters parameters, String path) {
		super(parameters);
		this.data = path;
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
			int minlon = 0;
			int maxlon = tsp.gridWidth;
			int minlat = tsp.gridHeight;
			int maxlat = 0;
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
			//Creation of the environment with the good size.
			EnvironmentInitializationData eid = super.generateEnvironment(tsp, levels);
			LogoEnvPLS environment = (LogoEnvPLS) eid.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
			for (int i=0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				//We add all the streets
				if (n.getNodeName().equals("node")) {
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
			return eid;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
