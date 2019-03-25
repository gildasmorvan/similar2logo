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
package fr.univ_artois.lgi2a.similar2logo.lib.tools.web;

import static spark.Spark.*;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.probes.Slf4jExceptionPrinter;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.probes.Slf4jExecutionTracker;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.SimilarWebRunner;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.control.SimilarWebController;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.AbstractExtendedSimulationModel;
import fr.univ_artois.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.univ_artois.lgi2a.similar2logo.lib.probes.JSONProbe;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.view.GridWebSocket;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.view.Similar2LogoHttpServer;

/**
 * Facilitates the execution of Similar2Logo simulations using the HTML web interface.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com">Antoine Lecoutre</a>
 */
public class Similar2LogoWebRunner extends SimilarWebRunner {
	
	/**
	 * Creates a new runner using the default configuration.
	 */
	public Similar2LogoWebRunner( ) {
		this.config = new Similar2LogoWebConfig( );
	}
	
	/**
	 * Initializes the runner with a specific simulation model, using the current 
	 * configuration of the runner.
	 * 
	 * This operation can only be performed once.
	 * 
	 * @param model The model of the simulation.
	 * @throws IllegalStateException If the runner has already been initialized.
	 */
	@Override
	public void initializeRunner(AbstractExtendedSimulationModel model) {
		
		if( model == null ){
			throw new IllegalArgumentException( "The model cannot be Null" );
		}
		
		// Check if the runner can be initialized
		if( this.config.isAlreadyInitialized() ) {
			throw new IllegalStateException( "The runner is alread initialized" );
		}
		// Define the name of the simulation.
		if( this.config.getSimulationName() == null ) {
			this.config.setSimulationName( model.getClass().getSimpleName() );
		}
		// Tag the runner as initializing
		this.config.finalizeConfiguration( );
		// Create the engine
		this.engine = new EngineMonothreadedDefaultdisambiguation( );
		
		Similar2LogoWebConfig config = (Similar2LogoWebConfig) this.config;
		
		if( config.areAgentsExported() || config.areMarksExported() || config.arePheromonesExported()) {
			webSocket("/grid", GridWebSocket.class);
		}
		
		if( config.areAgentsExported() || config.areMarksExported() || config.arePheromonesExported() ) {
			engine.addProbe("JSON export", new JSONProbe(
				config.areAgentsExported(), 
				config.areMarksExported(), 
				config.arePheromonesExported()));
		}
		
		// Creates the probes that will listen to the execution of the simulation.
		this.engine.addProbe( 
			"Error printer", 
			new Slf4jExceptionPrinter( )
		);
		this.engine.addProbe(
			"Trace printer", 
		    new Slf4jExecutionTracker( false )
		);
		// Identify the simulation parameters
		this.simulationParameters = model.getSimulationParameters();
		// Create the controller managing the interaction between the engine and the view.
		this.controller = new SimilarWebController( this.engine, model );
		// Create the SPARK HTTP server that will generate the HTML pages
		this.view = new Similar2LogoHttpServer( this.controller, this );
		this.view.initServer();
		// Bind the view and the controller
		this.controller.setViewControls( this.view );
	}
	
	/**
	 * Gets the configuration that will be used by the runner and the view.
	 * @return The configuration.
	 */
	@Override
	public Similar2LogoWebConfig getConfig() {
		return (Similar2LogoWebConfig) this.config;
	}
}