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

	private String htmlHeader = "<!DOCTYPE html>\n<html lang='en'> <head> <meta charset='utf-8'> <meta http-equiv='X-UA-Compatible' content='IE=edge'> <meta name='viewport' content='width=device-width, initial-scale=1'> <title>Similar control</title> </head> <body onload='initInterface();'>     <div class='container'>                  <div class='page-header'>              <p class='pull-right text-info' id='simulationState'>Simulation stopped</p>             <h1>Similar control</h1>                     </div>         <div>         </div>         <div>             <button id='startSimulation' type='button' class='btn btn-primary' onclick='startSimulation()'>Start Simulation</button>             <button id='stopSimulation' type='button' class='btn btn-primary' onclick='stopSimulation()'>Stop Simulation</button>         </div>     ";
	
	private String htmlUserCode;
	
	private String htmlFooter = "</div>         <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'>     <script src='http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js'></script>     <script type='text/javascript'>     function initInterface() {     $('#stopSimulation').prop('disabled', true);     }     </script>     <script type='text/javascript'>         function startSimulation() {             $.get( 'start' );             $('#simulationState').text('Simulation started');             $('#startSimulation').prop('disabled', true);             $('#stopSimulation').prop('disabled', false);         }     </script>     <script type='text/javascript'>     function stopSimulation() {     $.get( 'stop' );     $('#simulationState').text('Simulation stopped');     $('#startSimulation').prop('disabled', false);     $('#stopSimulation').prop('disabled', true);     }     </script> </body> </html>";
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
		this.setHtmlUserCode("");
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
			response = (new String(this.htmlHeader+this.getHtmlUserCode()+this.htmlFooter)).getBytes();
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
	
	public String getHtmlUserCode() {
		return htmlUserCode;
	}

	public void setHtmlUserCode(String htmlUserCode) {
		this.htmlUserCode = htmlUserCode;
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
