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
package fr.lgi2a.similar2logo.examples.transport;

import static spark.Spark.webSocket;

import java.io.IOException;

import org.json.JSONObject;

import fr.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParametersGenerator;
import fr.lgi2a.similar2logo.examples.transport.probes.MapWebSocket;
import fr.lgi2a.similar2logo.examples.transport.probes.ReadMapTransportProbe;
import fr.lgi2a.similar2logo.examples.transport.probes.ZoneDataWebSocket;
import fr.lgi2a.similar2logo.lib.tools.html.GUINotFoundException;
import fr.lgi2a.similar2logo.lib.tools.html.Similar2LogoHtmlRunner;

/**
 * Main class of the transport simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public final class TransportSimulationMain {
	
	private TransportSimulationMain () {}
	
	public static void main (String[] args) {
		
		JSONObject staticP = TransportSimulationParametersGenerator.staticParametersByDefaultJSON();
		JSONObject variableP = TransportSimulationParametersGenerator.variableParametersByDefaultJSON();
		JSONObject test = TransportSimulationParametersGenerator.parametersHourJSON(staticP, variableP, "./transportparameters/factors.txt");
		
		webSocket("/webSocketMap", MapWebSocket.class);
		webSocket("/webSocketZoneData", ZoneDataWebSocket.class);
		
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		runner.getConfig().setExportAgents( true );
		runner.getConfig().setExportMarks( true );
		try {
			runner.getConfig().setCustomHtmlBody( TransportSimulationMain.class.getResourceAsStream("transportgui.html") );
		} catch (IOException e) {
			throw new GUINotFoundException(e);
		}
		TransportSimulationModel tsm = new TransportSimulationModel(new TransportSimulationParameters(), 
				"./osm/map_valenciennes_edited.osm",
				test, 10, 20, 5, 5);
		runner.initializeRunner( tsm );
		runner.addProbe("Map", new ReadMapTransportProbe());
		runner.showView( );
	}

}
