package fr.lgi2a.similar2logo.lib.tools.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;

/**
 * The Http handler of the web server.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 *
 */
@SuppressWarnings("restriction")
public class SimilarHttpHandler implements HttpHandler {

	private String mainPage = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<title>Result</title>\n<style type=\"text/css\">\n#chart_div { position: absolute; left: 10px; right: 10px; top: 40px; bottom: 10px; }\n</style>\n</head>\n<body>\n<div id=\"chart_div\"></div>\n<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">\n<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\n<script src=\"http://cdnjs.cloudflare.com/ajax/libs/dygraph/1.1.1/dygraph-combined.js\"></script>\n<script type=\"text/javascript\">\ng = new Dygraph(document.getElementById(\"chart_div\"),\n\"http://localhost:8080/result.txt\",\n{\nshowRoller: false,\ncustomBars: false,\ntitle: \"Population dynamics\",\nlabels: [\"Time\", \"nbOfAgents\", \"nbOfInfectedAgents\", \"nbOfImmuneAgents\", \"nbOfNeverInfectedAgents\"],\nlegend: \"follow\",\nlabelsSeparateLines: true } );\nwindow.intervalId = setInterval(function() {\nlocation.reload();\n},\n4000);\n</script>\n</body>\n</html>";

	/**
	 * The simulation engine used to run simulations.
	 */
	private ISimulationEngine engine;
	/**
	 * The simulation model being run.
	 */
	private LogoSimulationModel model;
	/**
	 * The thread where the current simulation is running.
	 */
	private SimulationExecutionThread simulationThread;
	
	public SimilarHttpHandler(ISimulationEngine engine, LogoSimulationModel model) {
		this.engine = engine;
		this.model = model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(HttpExchange t) throws IOException {
		OutputStream os = t.getResponseBody();

		String fileName = t.getRequestURI().getPath();
		
		Headers h = t.getResponseHeaders();
		byte[] response=null;
		if (fileName.endsWith("/")) {
			response = mainPage.getBytes();
			h.add("Content-Type", "text/html");
		} else if (fileName.equals("/start")) {
			handleNewSimulationRequest( );
			response = new String("start").getBytes();
		} else if (fileName.equals("/stop")) {
			handleSimulationAbortionRequest( );
			response = new String("stop").getBytes();
		} else {
			h.add("Content-Type", "text");
			if (Paths.get("results"+fileName).toFile().exists()) {
				response = Files.readAllBytes(Paths.get("results"+fileName));
			} else {
				response = new String("Error 404").getBytes();

			}
		}

		t.sendResponseHeaders(200, response.length);
		os.write(response);
		os.close();
	}
	
	/**
	 * Manages the creation of a new simulation.
	 */
	public void handleNewSimulationRequest( ){
		this.simulationThread = new SimulationExecutionThread( this.engine, this.model);
		this.simulationThread.start();
	}
	
	
	/**
	 * Manages the abortion of the current simulation.
	 */
	public void handleSimulationAbortionRequest( ){
		this.engine.requestSimulationAbortion( );
		this.simulationThread.interrupt();
	}
	
	/**
	 * A thread where a Similar simulation is performed.
	 * @author <a href="http://www.lgi2a.univ-artois.fr/" target="_blank">LGI2A</a> -- <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	private class SimulationExecutionThread extends Thread {
		/**
		 * The simulation model used for the execution of the simulation.
		 */
		private LogoSimulationModel simulationModel;
		/**
		 * The simulation engine used for the execution of the simulation.
		 */
		private ISimulationEngine simulationEngine;
		
		/**
		 * Constructor of the class {@link SimulationExecutionThread}.
		 * @param simulationEngine The simulation model used for the execution of the simulation.
		 * @param simulationModel The simulation engine used for the execution of the simulation.
		 */
		public SimulationExecutionThread( 
			ISimulationEngine simulationEngine,
			LogoSimulationModel simulationModel
		){
			this.simulationEngine = simulationEngine;
			this.simulationModel = simulationModel;
		}
		
		public void run() {
			try{
				this.simulationEngine.runNewSimulation( this.simulationModel );
			} catch( RuntimeException e ) {
				e.printStackTrace();
			}
		}
	}

}
