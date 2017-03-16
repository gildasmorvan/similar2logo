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
package fr.lgi2a.similar2logo.lib.tools.html.view;

import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.stop;
import static spark.Spark.webSocket;

import java.awt.Desktop;
import java.net.URI;

import fr.lgi2a.similar2logo.lib.tools.html.IHtmlControls;
import fr.lgi2a.similar2logo.lib.tools.html.IHtmlInitializationData;
import fr.lgi2a.similar2logo.lib.tools.html.IHtmlRequests;
import fr.lgi2a.similar2logo.lib.tools.html.Similar2LogoHtmlConfig;

/**
 * The Spark HTTP server used as a connection point between the HTML view on the simulation
 * and the java based simulation controller.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com>Antoine Lecoutre</a>
 */
public class SparkHttpServer implements IHtmlControls {
	/**
	 * The controller to which the requests coming from the view are forwarded.
	 */
	private IHtmlRequests controller;
	/**
	 * The object providing initialization data to this server.
	 */
	private IHtmlInitializationData initializationData;
	/**
	 * The tool generating the HTML code of the view.
	 */
	private Similar2LogoHtmlGenerator htmlCodeGenerator;
	
	/**
	 * Creates a new Spark Http Server managing the HTML view on the simulation.
	 * @param controller The controller to which the view will be bound.
	 * @param initializationData The object providing initialization data to this server.
	 */
	public SparkHttpServer(
		IHtmlRequests controller,
		IHtmlInitializationData initializationData
	) {
		this.controller = controller;
		this.initializationData = initializationData;
		this.initServer( );
	}
	
	/**
	 * Initializes the web server
	 */
	private final void initServer( ) {
		// Get the configuration class that will be used.
		Similar2LogoHtmlConfig config = this.initializationData.getConfig();
		
		// Initialize the tool generating the view
		this.htmlCodeGenerator = new Similar2LogoHtmlGenerator(
			config.getCustomHtmlBody(),
			this.initializationData
		);
		
		//Listens to 8080
		port(8080);
		
		//Inits routes
		if( config.areAgentsExported() || config.areMarksExported() || config.arePheromonesExported()) {
			webSocket("/webSocket", GridWebSocket.class);
		}
		get("/", (request, response) -> {
			response.type("text/html");
			return SparkHttpServer.this.htmlCodeGenerator.renderHtmlHeader()
					+ SparkHttpServer.this.htmlCodeGenerator.renderHtmlBody()
					+ SparkHttpServer.this.htmlCodeGenerator.renderHtmlFooter();
    	});
		get("/state", (request, response) -> {
			return SparkHttpServer.this.controller.handleSimulationStateRequest( );
    	});
		get("/start", (request, response) -> {
			SparkHttpServer.this.controller.handleNewSimulationRequest();
    		return "";
    	});
		get("/stop", (request, response) -> {
			SparkHttpServer.this.controller.handleSimulationAbortionRequest();
    		return "";
    	});
		get("/pause", (request, response) -> {
			SparkHttpServer.this.controller.handleSimulationPauseRequest();
    		return "";
    	});
		get("/shutdown", (request, response) -> {
			SparkHttpServer.this.controller.handleShutDownRequest();
    	    stop();
    		return "";
    	});
		get("/setParameter", (request, response) -> {
			for( String param : request.queryParams()) {
				SparkHttpServer.this.controller.setParameter(param, request.queryParams(param));
			}
			return "";
		});
		get("/getParameter", (request, response) -> {
			for( String param : request.attributes()) {
				SparkHttpServer.this.controller.getParameter(param);
			}
		    return "";
		});
		for(String resource : Similar2LogoHtmlGenerator.deployedResources) {
			get("/"+resource, (request, response) -> {
				String[] splitResource = resource.split("[.]");
				switch(splitResource[splitResource.length-1]) {
					case "js":
						response.type("application/javascript"); 
						break;
					case "css":
						response.type("text/css"); 
						break;
					case "html":
						response.type("text/html");
						break;
					default: 
						response.type("text/plain");
				}
				return Similar2LogoHtmlGenerator.getViewResource(SparkHttpServer.class.getResourceAsStream(resource));
			});
		}
	}

	
	/**
	 * Launches the web browser of the client, where the simulation can be viewed and controlled.
	 */
	public void showView(){
		// Waits for the end of the initialization of the view
		awaitInitialization();
		// Start the browser
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {        	
	            desktop.browse(new URI("http://localhost:8080"));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	/**
	 * Sets whether the start button is enabled or not in the view.
	 * @param active 	<code>true</code> if the start button has to be interactive. 
	 * 					<code>false</code> if it has to be deactivated in the view.
	 */
	public void setStartButtonState(boolean active) {
		//Does nothing
	}

	/**
	 * Sets whether the pause button is enabled or not in the view.
	 * @param active 	<code>true</code> if the pause button has to be interactive. 
	 * 					<code>false</code> if it has to be deactivated in the view.
	 */
	@Override
	public void setPauseButtonState(boolean active) {
		//Does nothing
	}

	/**
	 * Sets whether the abort button is enabled or not in the view.
	 * @param active 	<code>true</code> if the pause button has to be interactive. 
	 * 					<code>false</code> if it has to be deactivated in the view.
	 */
	@Override
	public void setAbortButtonState(boolean active) {
		//Does nothing
	}
	
	/**
	 * Requests the view to shut down.
	 */
	@Override
	public void shutDownView() {
	    stop();
	}
}
