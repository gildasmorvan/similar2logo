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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.lib.probes.InteractiveSimulationProbe;
import fr.lgi2a.similar2logo.lib.tools.SimulationExecutionThread;

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

	/**
	 * The header of the web GUI.
	 */
	private String htmlHeader = "<!DOCTYPE html>\n<html lang='en'> <head> <meta charset='utf-8'> <meta http-equiv='X-UA-Compatible' content='IE=edge'> <meta name='viewport' content='width=device-width, initial-scale=1'> <title>Similar control</title> </head> <body onload='initInterface();'>     <div class='container'>                  <div class='page-header'>              <p class='pull-right text-info' id='simulationState'>Simulation stopped</p>             <h1>Similar control</h1>                     </div>         <div>         </div>         <div>             <button id='startSimulation' type='button' class='btn btn-primary' onclick='startSimulation()'>Start Simulation</button>             <button id='stopSimulation' type='button' class='btn btn-primary' onclick='stopSimulation()'>Stop Simulation</button>             <button id='pauseSimulation' type='button' class='btn btn-primary' onclick='pauseSimulation()'>Pause/Resume Simulation</button>         </div>     </div>         <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'>     <script src='http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js'></script>     <script type='text/javascript'>     function initInterface() {     $('#stopSimulation').prop('disabled', true);     $('#pauseSimulation').prop('disabled', true);     }     </script>     <script type='text/javascript'>         function startSimulation() {             $.get( 'start' );             $('#simulationState').text('Simulation started');             $('#startSimulation').prop('disabled', true);             $('#stopSimulation').prop('disabled', false);             $('#pauseSimulation').prop('disabled', false);         }     </script>     <script type='text/javascript'>     function stopSimulation() {     $.get( 'stop' );     $('#simulationState').text('Simulation stopped');     $('#startSimulation').prop('disabled', false);     $('#stopSimulation').prop('disabled', true);     $('#pauseSimulation').prop('disabled', true);     }     </script>     <script type='text/javascript'>     function pauseSimulation() {     $.get( 'pause' );     $('#startSimulation').prop('disabled', true);     $('#stopSimulation').prop('disabled', false);     $('#pauseSimulation').prop('disabled', false);    }     </script>";
	
	/**
	 * The body of the web GUI.
	 */
	private String htmlBody;
	
	/**
	 * The footer of the web GUI.
	 */
	private String htmlFooter = "</div>         <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'>     <script src='http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js'></script>     <script type='text/javascript'>     function initInterface() {     $('#stopSimulation').prop('disabled', true); $('#pauseSimulation').prop('disabled', true);     }     </script>     <script type='text/javascript'>         function startSimulation() {             $.get( 'start' );             $('#simulationState').text('Simulation started');             $('#startSimulation').prop('disabled', true);             $('#stopSimulation').prop('disabled', false);   $('#pauseSimulation').prop('disabled', false);       }     </script>     <script type='text/javascript'>     function stopSimulation() {     $.get( 'stop' );     $('#simulationState').text('Simulation stopped');     $('#startSimulation').prop('disabled', false);     $('#stopSimulation').prop('disabled', true);  $('#pauseSimulation').prop('disabled', true);    }     </script> </body> </html>";
	
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
	
	/**
	 * The interactive simulation probe.
	 */
	private InteractiveSimulationProbe interactiveSimulationProbe;
	
	/**
	 * 
	 * Builds an instance of this Http handler.
	 * 
	 * @param engine The simulation engine used to simulate the model.
	 * @param model The Simulation model.
	 */
	public SimilarHttpHandler(ISimulationEngine engine, LogoSimulationModel model) {
		this.engine = engine;
		this.model = model;
		this.setHtmlBody("");
		this.interactiveSimulationProbe = new InteractiveSimulationProbe();
		engine.addProbe("InteractiveSimulation", this.interactiveSimulationProbe);
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
			response = (new String(this.htmlHeader+this.getHtmlBody()+this.htmlFooter)).getBytes();
			h.add("Content-Type", "text/html");
		} else if (fileName.equals("/start")) {
			handleNewSimulationRequest( );
			response = new String("start").getBytes();
		} else if (fileName.equals("/stop")) {
			handleSimulationAbortionRequest( );
			response = new String("stop").getBytes();
		} else if (fileName.equals("/pause")) {
			handleSimulationPauseRequest( );
			response = new String("pause").getBytes();
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
	 * Manages the pause and resume requests of the current simulation.
	 */
	private void handleSimulationPauseRequest() {
		this.interactiveSimulationProbe.setPaused(
			!this.interactiveSimulationProbe.isPaused()
		);	
	}

	/**
	 * Manages the creation of a new simulation.
	 */
	public void handleNewSimulationRequest( ){
		this.simulationThread = new SimulationExecutionThread( this.engine, this.model);
		this.simulationThread.start();
	}
	
	
	/**
	 * Manages the abortion request of the current simulation.
	 */
	public void handleSimulationAbortionRequest( ){
		if(this.interactiveSimulationProbe.isPaused()) {
			this.interactiveSimulationProbe.setPaused(false);
		}
		this.engine.requestSimulationAbortion( );
		this.simulationThread.interrupt();
	}
	
	/**
	 * @return the body of the web GUI.
	 */
	public String getHtmlBody() {
		return htmlBody;
	}

	/**
	 * @param htmlBody The body of the web GUI.
	 */
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}

	

}
