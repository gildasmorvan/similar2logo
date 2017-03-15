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
package fr.lgi2a.similar2logo.lib.tools.html;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.lib.probes.JSONProbe;
import fr.lgi2a.similar2logo.lib.tools.html.control.Similar2LogoHtmlController;
import fr.lgi2a.similar2logo.lib.tools.html.view.SparkHttpServer;

/**
 * Facilitates the execution of Similar2Logo simulations using the HTML web interface.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com>Antoine Lecoutre</a>
 */
public class Similar2LogoHtmlRunner implements IHtmlInitializationData {
	/**
	 * The configuration of the runner.
	 */
	private Similar2LogoHtmlConfig config;
	/**
	 * The parameters of the simulation.
	 */
	private LogoSimulationParameters simulationParameters;
	/**
	 * The engine of the simulation 
	 */
	private ISimulationEngine engine;
	/**
	 * The object managing the HTML view.
	 */
	private SparkHttpServer view;
	/**
	 * The controller managing the interaction between the engine and the view.
	 */
	private Similar2LogoHtmlController controller;
	
	/**
	 * Creates a new runner using the default configuration.
	 */
	public Similar2LogoHtmlRunner( ) {
		this.config = new Similar2LogoHtmlConfig( );
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
	public void initializeRunner(LogoSimulationModel model) throws IllegalStateException {
		// Check if the runner can be initialized
		if( this.config.isAlreadyInitialized() ) {
			throw new IllegalStateException( "The runner is alread initialized" );
		}
		// Define the name of the simulation.
		if( this.config.getSimulationName() == null ) {
			this.config.setSimulationName( "Unamed simulation" );
			if( model != null ) {
				this.config.setSimulationName( model.getClass().getSimpleName() );
			}
		}
		// Tag the runner as initializing
		this.config.finalizeConfiguration( );
		// Create the engine
		this.engine = new EngineMonothreadedDefaultdisambiguation( );
		// Creates the probes that will listen to the execution of the simulation.
		this.engine.addProbe( 
			"Error printer", 
			new ProbeExceptionPrinter( )
		);
		this.engine.addProbe(
			"Trace printer", 
		    new ProbeExecutionTracker( System.err, false )
		);
		if( this.config.areAgentsExported() || this.config.areMarksExported() || this.config.arePheromonesExported() ) {
			engine.addProbe("JSON export", new JSONProbe(
					this.config.areAgentsExported(), 
					this.config.areMarksExported(), 
					this.config.arePheromonesExported()));
		}
		// Identify the simulation parameters
		this.simulationParameters = (LogoSimulationParameters) model.getSimulationParameters();
		// Create the controller managing the interaction between the engine and the view.
		this.controller = new Similar2LogoHtmlController( this.engine, model );
		// Create the SPARK HTTP server that will generate the HTML pages
		this.view = new SparkHttpServer( this.controller, this );
		// Bind the view and the controller
		this.controller.setViewControls( this.view );
	}
	
	/**
	 * Opens the view on the simulation.
	 * @throws IllegalStateException If the runner is not initialized.
	 */
	public void showView( ) {
		// If the runner is not initialized, the view cannot be shown.
		if( ! this.config.isAlreadyInitialized() ) {
			throw new IllegalStateException( "The runner is not initialized." );
		}
		// Show the GUI
		this.view.showView( );
		// Then tell the controller that it has to listen to events
		this.controller.listenToViewRequests();
	}

	/**
	 * Gets the configuration that will be used by the runner and the view.
	 * @return The configuration.
	 */
	@Override
	public Similar2LogoHtmlConfig getConfig() {
		return this.config;
	}

	/**
	 * Gets the parameters of the simulation.
	 * @return The parameters of the simulation.
	 */
	@Override
	public LogoSimulationParameters getSimulationParameters() {
		return this.simulationParameters;
	}
	
	/**
	 * Adds a probe to the registered probes of the simulation engine.
	 * @param name The name of the probe.
	 * @param probe The probe to register.
	 * @throws IllegalStateException If the runner is not initialized yet.
	 */
	public void addProbe(
		String name,
		IProbe probe
	) throws IllegalStateException {
		if( ! this.config.isAlreadyInitialized() ) {
			throw new IllegalStateException( "The runner has to be initialized before adding other probes." );
		}
		this.engine.addProbe(name, probe);
	}
}