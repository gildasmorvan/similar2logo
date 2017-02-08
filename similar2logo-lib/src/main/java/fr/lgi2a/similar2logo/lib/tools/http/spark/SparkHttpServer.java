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
package fr.lgi2a.similar2logo.lib.tools.http.spark;

import static spark.Spark.*;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.lib.probes.InteractiveSimulationProbe;
import fr.lgi2a.similar2logo.lib.probes.JSONProbe;
import fr.lgi2a.similar2logo.lib.tools.SimulationExecutionThread;
import fr.lgi2a.similar2logo.lib.tools.http.Similar2LogoWebApp;
import fr.lgi2a.similar2logo.lib.tools.http.SimilarHttpHandler;
import fr.lgi2a.similar2logo.lib.tools.http.SimilarHttpServer;
import spark.utils.IOUtils;

/**
 * A http server that allow to control Similar simulations.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com>Antoine Lecoutre</a>
 *
 */
public class SparkHttpServer {

	/**
	 * The simulation state.
	 */
	SimulationState simulationState;

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
	private JSONProbe jSONProbe;

	/**
	 * The web app of Similar2logo.
	 */
	private Similar2LogoWebApp webApp;
	
	/**
	 * The context of the resource to the simulation
	 */
	private String context;
	
	/**
	 * The simulation thread of the simulation
	 */
	private SimulationExecutionThread simulationThread;
	
	/**
	 * The engine of the simulation 
	 */
	private ISimulationEngine engine ;
	
	/**
	 * Constructor of the spark server
	 * @param model of the simulation
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
		
		context = initWebApp();
		
		/**
		 * HttpServer
		 */
		   
		// Create the probes that will listen to the execution of the simulation.
		engine = new EngineMonothreadedDefaultdisambiguation( );
		engine.addProbe( 
		   "Error printer", 
		   new ProbeExceptionPrinter( )
		);
		engine.addProbe(
			"Trace printer", 
		     new ProbeExecutionTracker( System.err, false )
		);
		
		this.model = model;
		this.webApp = new Similar2LogoWebApp();
		if(exportAgents || exportMarks) {
			this.jSONProbe = new JSONProbe(exportAgents, exportMarks, exportPheromones);
			engine.addProbe("JSON export", this.jSONProbe);
		}
		this.interactiveSimulationProbe = new InteractiveSimulationProbe();
		engine.addProbe("InteractiveSimulation", this.interactiveSimulationProbe);
		
		this.simulationState = SimulationState.STOP;
		this.model.getSimulationParameters();
		
		
		/**
		 * Spark
		 */
		
		port(8080);
		
		//load the resource (css, js)
		
		staticFiles.externalLocation(context);
		
		
		//Route
		
		get("/grid", (request, response) -> {
				return this.jSONProbe.getOutput();
    	});
		
		get("/", (request, response) -> {
    		return Similar2LogoWebApp.getHtmlHeader(model)
					+ webApp.getHtmlBody()
					+ getAppResource(SimilarHttpServer.class.getResource("gridview.html"))
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
    	    stop();
    		return "Bye bye ! </br>Server stopped";
    	});
		
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
	 * Constructor of the spark server with a resource
	 * @param model of the simulation
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 * @param exportPheromones <code>true</code> if pheromones are exported, <code>false</code> else.
	 * @param resource is a url of the resource to load the body
	 * @throws IOException
	 */
	public SparkHttpServer(
		LogoSimulationModel model,
		boolean exportAgents,
		boolean exportMarks,
		boolean exportPheromones,
		URL resource
		) throws IOException {
		
		String [] p = resource.toString().split("/", 2);
		InputStream myStream = new FileInputStream("/"+p[1]);
		String htmlBody = IOUtils.toString(myStream).trim();
	
		context = initWebApp();
		
		/**
		 * HttpServer
		 */
		   
		// Create the probes that will listen to the execution of the simulation.

		engine = new EngineMonothreadedDefaultdisambiguation( );
		
		engine.addProbe( 
		   "Error printer", 
		   new ProbeExceptionPrinter( )
		);
		engine.addProbe(
			"Trace printer", 
		     new ProbeExecutionTracker( System.err, false )
		);
		
		this.model = model;
		this.webApp = new Similar2LogoWebApp();
		if(exportAgents || exportMarks) {
			this.jSONProbe = new JSONProbe(exportAgents, exportMarks, exportPheromones);
			engine.addProbe("JSON export", this.jSONProbe);
		}
		this.interactiveSimulationProbe = new InteractiveSimulationProbe();
		engine.addProbe("InteractiveSimulation", this.interactiveSimulationProbe);
		this.simulationState = SimulationState.STOP;
		this.model.getSimulationParameters();
		
		
		/**
		 * Spark
		 */
		
		port(8080);
		
		//load the resource (css, js)
		
		staticFiles.externalLocation(context);

		
		
		
		//Route
		
		get("/grid", (request, response) -> {
				return this.jSONProbe.getOutput();
    	});
		
		get("/", (request, response) -> {
    		return Similar2LogoWebApp.getHtmlHeader(model)
					+ webApp.getHtmlBody()
					+ htmlBody
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
    	    stop();
    		return "Bye bye ! </br>Server stopped";
    	});
		
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
	 * The command on the interface
	 */
	public enum SimulationState {
		STOP, RUN, PAUSED;
	}

	/**
	 * the getter of the engine
	 * @return the engine of the simulation
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
	 * initialise the web application
	 * @return the context of the simulation
	 */
	private static String initWebApp() {
		
		//Create directories
		String context = "simulation";
		String[] directoryNames = {
			context,
			context+"/lib",
			context+"/lib/css",
			context+"/lib/js",
			context+"/lib/fonts"
		};
		
		for(String directoryName : directoryNames) {
			File directory = new File(directoryName);
			if (!directory.exists()) {
			    try{
			    	directory.mkdir();
			    } 
			    catch(SecurityException e){
			        e.printStackTrace();
			    }        
			}
		}
		
		//Create js and css files at the good location.
		try {
			for(String resource : Similar2LogoWebApp.deployedResources) {
				String[] splitResource = resource.split("[.]");
				String path = null;
				switch(splitResource[splitResource.length-1]) {
					case "js":
						path = context+"/lib/js/"+resource;
						break;
					case "css":
						path = context+"/lib/css/"+resource;
						break;
					default: 
						path = context+"/lib/fonts/"+resource;
				}
				Files.copy(
					new File(Similar2LogoWebApp.class.getResource(resource).toURI()).toPath(),
					new File(path).toPath()
				);
			}
		} catch (IOException | URISyntaxException e) {
//			e.printStackTrace();
		}
		return context;
	}
	
	/**
	 * @return the url of a resource of the web app.
	 */
	public static String getAppResource(URL resource) {
		try {
			return new String(
				Files.readAllBytes(
					new File(resource.toURI()).toPath()
				),
				StandardCharsets.UTF_8
			);
		} catch (IOException | URISyntaxException e) {
			return "";
		}
	}
	
	public String getContext(){
		return this.context;
	}
}
