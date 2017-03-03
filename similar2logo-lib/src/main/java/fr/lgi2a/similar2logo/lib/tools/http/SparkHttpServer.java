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
package fr.lgi2a.similar2logo.lib.tools.http;

import static spark.Spark.*;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.lib.probes.InteractiveSimulationProbe;
import fr.lgi2a.similar2logo.lib.probes.JSONProbe;
import fr.lgi2a.similar2logo.lib.tools.SimulationExecutionThread;

/**
 * A http server based on Spark that allows to control and visualize Similar2Logo simulations.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com>Antoine Lecoutre</a>
 *
 */
public class SparkHttpServer {

	/**
	 * The parameters of the simulation.
	 */
	private LogoSimulationParameters simulationParameters;
	
	/**
	 * The simulation state.
	 */
	private SimulationState simulationState;

	/**
	 * The simulation model being run.
	 */
	private LogoSimulationModel model;

	/**
	 * The interactive simulation probe.
	 */
	private InteractiveSimulationProbe interactiveSimulationProbe;

	/**
	 * The JSON probe.
	 */
	public static JSONProbe jSONProbe;

	/**
	 * The web app of Similar2logo.
	 */
	private Similar2LogoWebApp webApp;
	
	/**
	 * The simulation thread of the simulation
	 */
	private SimulationExecutionThread simulationThread;
	
	/**
	 * The engine of the simulation 
	 */
	private ISimulationEngine engine ;
	
	/**
	 * Constructor of the spark server to use the default GUI
	 * @param model the simulation model
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 * @param exportPheromones <code>true</code> if pheromones are exported, <code>false</code> else.
	 */
	public SparkHttpServer(
			LogoSimulationModel model,
			boolean exportAgents,
			boolean exportMarks,
			boolean exportPheromones
			) {
		
		this.model = model;

		initServer(exportAgents, exportMarks, exportPheromones,Similar2LogoWebApp.getAppResource(Similar2LogoWebApp.class.getResourceAsStream("gridview.html")));
	
		openBrowser();
	}
	
	/**
	 * Constructor of the spark server with a URL resource
	 * @param model the simulation model
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 * @param exportPheromones <code>true</code> if pheromones are exported, <code>false</code> else.
	 * @param resource the InputStream of the HTML body
	 * @throws IOException
	 */
	public SparkHttpServer(
		LogoSimulationModel model,
		boolean exportAgents,
		boolean exportMarks,
		boolean exportPheromones,
		InputStream resource
		) throws IOException {
		
		this.model = model;
	
		initServer(exportAgents, exportMarks, exportPheromones,Similar2LogoWebApp.getAppResource(resource));
		
		openBrowser();
		
	}
	
	
	/**
	 * Constructor of the spark server with a String resource
	 * @param model the simulation model
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 * @param exportPheromones <code>true</code> if pheromones are exported, <code>false</code> else.
	 * @param htmlBody the body html of the GUI
	 * @throws IOException
	 */
	public SparkHttpServer(
		LogoSimulationModel model,
		boolean exportAgents,
		boolean exportMarks,
		boolean exportPheromones,
		String htmlBody
		) throws IOException {
		
		this.model = model;
		
		initServer(exportAgents, exportMarks, exportPheromones,htmlBody);

		openBrowser();
	}
	/**
	 * Initializes the web server
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 * @param exportPheromones <code>true</code> if pheromones are exported, <code>false</code> else.
	 * @param htmlBody The body of the web GUI.
	 */
	public void initServer(
		boolean exportAgents,
		boolean exportMarks,
		boolean exportPheromones,
		String htmlBody
	){

		//Creates the engine
		engine = new EngineMonothreadedDefaultdisambiguation( );
		
		//Creates the probes that will listen to the execution of the simulation.
		engine.addProbe( 
		   "Error printer", 
		   new ProbeExceptionPrinter( )
		);
		engine.addProbe(
			"Trace printer", 
		     new ProbeExecutionTracker( System.err, false )
		);
		
		this.webApp = new Similar2LogoWebApp(htmlBody);
		if(exportAgents || exportMarks || exportPheromones) {
			SparkHttpServer.jSONProbe = new JSONProbe(exportAgents, exportMarks, exportPheromones);
			engine.addProbe("JSON export", SparkHttpServer.jSONProbe);
		}
		
		this.interactiveSimulationProbe = new InteractiveSimulationProbe();
		engine.addProbe("InteractiveSimulation", this.interactiveSimulationProbe);
		this.simulationState = SimulationState.STOP;
		this.simulationParameters = (LogoSimulationParameters) this.model.getSimulationParameters();
		
		//Listens to 8080
		port(8080);
		
		//Inits routes
		if(exportAgents || exportMarks || exportPheromones) {
			webSocket("/webSocket", GridWebSocket.class);
		}
		
		get("/", (request, response) -> {
			response.type("text/html");
    		return Similar2LogoWebApp.getHtmlHeader(model)
					+ webApp.getHtmlBody()
					+ Similar2LogoWebApp.getHtmlFooter();
    	});
		get("/state", (request, response) -> {
    		return simulationState.toString().getBytes();
    	});
		get("/start", (request, response) -> {
			handleNewSimulationRequest();
    		return "";
    	});
		get("/stop", (request, response) -> {
			handleSimulationAbortionRequest();
    		return "";
    	});
		get("/pause", (request, response) -> {
			handleSimulationPauseRequest();
    		return "";
    	});
		get("/shutdown", (request, response) -> {
			handleSimulationAbortionRequest();
    	    stop();
    		return "";
    	});
		get("/setParameter", (request, response) -> {
			for( String param : request.queryParams()) {
				setParameter(param, request.queryParams(param));
			}
			return "";
		});
		
		get("/getParameter", (request, response) -> {
			for( String param : request.attributes()) {
				getParameter(param);
			}
		    return "";
		});
		
		for(String resource : Similar2LogoWebApp.deployedResources) {
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
				return Similar2LogoWebApp.getAppResource(SparkHttpServer.class.getResourceAsStream(resource));
			});
		}
	}

	
	/**
	 * Launches the web browser of the client.
	 */
	public void openBrowser(){
		awaitInitialization();
		//Start the browser
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
	 * Manages the creation of a new simulation.
	 */
	public void handleNewSimulationRequest() {
		this.simulationThread = new SimulationExecutionThread(this.engine,
				this.model);
		this.simulationThread.start();
		this.simulationState = SimulationState.RUN;
	}
	
	/**
	 * Manages the abortion request of the current simulation.
	 */
	public void handleSimulationAbortionRequest() {
		if (this.interactiveSimulationProbe.isPaused()) {
			this.interactiveSimulationProbe.setPaused(false);
		}
		this.engine.requestSimulationAbortion();
		this.simulationThread.interrupt();
		this.simulationState = SimulationState.STOP;
	}

	/**
	 * The state of the simulation.
	 */
	public enum SimulationState {
		STOP, RUN, PAUSED;
	}

	/**
	 * @return the engine that runs the simulation.
	 */
	public ISimulationEngine getEngine() {
		return engine;
	}
	
	/**
	 * Manages the pause and resume requests of the current simulation.
	 */
	private void handleSimulationPauseRequest() {
		this.interactiveSimulationProbe
				.setPaused(!this.interactiveSimulationProbe.isPaused());
		if (this.interactiveSimulationProbe.isPaused()) {
			this.simulationState = SimulationState.PAUSED;
		} else {
			this.simulationState = SimulationState.RUN;
		}
	}
	
	/**
	 * @return the value of a parameter.
	 * @param The name of the parameter
	 */
	private String getParameter(String parameter) {

		try {
			return simulationParameters.getClass().getField(parameter)
					.get(simulationParameters).toString();
		} catch (Exception e) {
			return "The attribute " + parameter + " does not exist.";
		}
		
	}
	
	/**
	 * Set the value of a parameter.
	 * @param parameter The name of the parameter.
	 * @param value The value of the parameter.
	 */
	private void setParameter(String parameter, String value) {
		try {
			Class<?> type = simulationParameters.getClass().getField(parameter).getType();
			if(type.equals(String.class)) {
					simulationParameters.getClass().getField(parameter).set(simulationParameters, value);
			} else if(type.equals(Integer.TYPE)) {
				simulationParameters.getClass().getField(parameter).set(simulationParameters, (int) Double.parseDouble(value));
			} else if(type.equals(Boolean.TYPE)) {
				simulationParameters.getClass().getField(parameter).set(simulationParameters, Boolean.parseBoolean(value));
			} else if(type.equals(Double.TYPE)) {
				simulationParameters.getClass().getField(parameter).set(simulationParameters, Double.parseDouble(value));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
