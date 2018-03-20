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
package fr.lgi2a.similar2logo.lib.tools.html.control;

import java.nio.charset.StandardCharsets;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.lib.tools.SimulationExecutionThread;
import fr.lgi2a.similar2logo.lib.tools.html.IHtmlControls;
import fr.lgi2a.similar2logo.lib.tools.html.IHtmlRequests;
import fr.lgi2a.similar2logo.lib.tools.html.ParameterNotFoundException;

/**
 * The controller managing the synchronization between the simulation engine and the HTML 
 * view and control buttons of the simulation.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com>Antoine Lecoutre</a>
 */
public class Similar2LogoHtmlController implements IProbe, IHtmlRequests {	
	/**
	 * The engine of the simulation 
	 */
	private ISimulationEngine engine;
	/**
	 * The model of the simulation
	 */
	private AbstractLogoSimulationModel model;
	/**
	 * The parameters of the simulation.
	 */
	private LogoSimulationParameters simulationParameters;
	/**
	 * The thread where the current simulation is performed.
	 */
	private SimulationExecutionThread simuThread;
	/**
	 * The object forwarding update requests of the view.
	 */
	private IHtmlControls viewControls;
	/**
	 * <code>true</code> if the controller can listen and react to view requests.
	 * <code>false</code> else.
	 */
	private boolean listenToViewRequests;
	/**
	 * The current state of the simulation engine of the controller.
	 */
	private volatile EngineState engineState;
	/**
	 * <code>true</code> whenever a user requests to pause (or to resume) a running simulation,
	 * and the request has not been processed yet. 
	 */
	private volatile boolean togglePause;
	
	/**
	 * Creates an instance of the controller for the provided engine and view.
	 * The initialization has to be completed by a separate call to the <code>setViewControls</code> method.
	 * @param engine The simulation engine used to perform simulations.
	 * @param model The model of the simulation.
	 * @param viewControls The object forwarding update requests of the view.
	 */
	public Similar2LogoHtmlController(
		ISimulationEngine engine,
		AbstractLogoSimulationModel model
	){
		this.listenToViewRequests = false;
		this.engine = engine;
		this.engine.addProbe( "Simulation control", this );
		this.engineState = EngineState.IDLE;
		this.togglePause = false;
		this.model = model;
		this.simulationParameters = (LogoSimulationParameters) this.model.getSimulationParameters();
	}
	
	/**
	 * Sets the object forwarding update requests of the view.
	 * @param viewControls The object forwarding update requests of the view.
	 */
	public void setViewControls( IHtmlControls viewControls ){
		this.viewControls = viewControls;
		// Force the update of the view buttons using the engine state change method.
		synchronized ( this.engineState ) {
			if( this.engineState.allowsNewRun() ) {
				this.changeEngineState( EngineState.IDLE );
			}
		}
	}
	
	/**
	 * Tells the controller that it can start listening to and reacting to
	 * the requests sent by the view.
	 */
	public void listenToViewRequests() {
		this.listenToViewRequests = true;
	}


	/**
	 * Called by the view to have a byte array version of the current state of the simulation engine.
	 * @return A byte representation of the state of the engine.
	 */
	@Override
	public byte[] handleSimulationStateRequest() {
		return this.engineState.toString().getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Called by the view whenever the user wants to start a new simulation.
	 */
	@Override
	public void handleNewSimulationRequest() {
		// If the controller has to not listen to view events, the method does nothing.
		if( ! this.listenToViewRequests ) {
			return;
		}
		// Synchronized block since all of these operations have to be consistent with the
		// state of the simulation engine.
		synchronized( this.engineState ) {
			// If the simulation is not in an appropriate state, the request is ignored.
			EngineState currentState = this.engineState; 
			if( ! currentState.allowsNewRun() || ( this.simuThread != null && ! this.simuThread.hasFinished() ) ) {
				System.err.println(
					"Ignored a simulation start request (current state : "
					+ currentState 
					+ ")."
				);
				return;
			}
			// Else, start a new simulation thread.
			this.changeEngineState(EngineState.RUN_PLANNED);
			this.togglePause = false;
			this.simuThread = new SimulationExecutionThread( this.engine, this.model );
			this.simuThread.start();
		}
	}

	/**
	 * Called by the view whenever the user wants to abort the currently running simulation.
	 */
	@Override
	public void handleSimulationAbortionRequest() {
		// If the controller has to not listen to view events, the method does nothing.
		if( ! this.listenToViewRequests ) {
			return;
		}
		// Synchronized block since all of these operations have to be consistent with the
		// state of the simulation engine.
		synchronized( this.engineState ) {
			// If the simulation is not in an appropriate state, the request is ignored.
			EngineState currentState = this.engineState; 
			if( ! currentState.allowsAbort() ) {
				System.err.println(
					"Ignored a simulation abortion request (current state : "
					+ currentState
					+ ")."
				);
				return;
			}
			// Else, the abortion of the simulation is requested.
			this.changeEngineState( EngineState.ABORT_REQUESTED );
			this.engine.requestSimulationAbortion( );
		}
	}

	/**
	 * Called by the view whenever the user wants to start or end the pause mode of the simulation.
	 */
	@Override
	public void handleSimulationPauseRequest( ) {
		// If the controller has to not listen to view events, the method does nothing.
		if( ! this.listenToViewRequests ) {
			return;
		}
		// Synchronized block since all of these operations have to be consistent with the
		// state of the simulation engine.
		synchronized( this.engineState ) {
			// If the simulation is not in an appropriate state, the request is ignored.
			EngineState currentState = this.engineState; 
			if( ! currentState.allowsPause() ) {
				System.err.println(
					"Ignored a simulation pause request (current state : "
					+ currentState
					+ ")."
				);
				return;
			}
			// Else, the request is memorized.
			this.togglePause = true;
		}
	}

	/**
	 * Called by the view whenever the user wants to shut down the server displaying the simulation.
	 */
	@Override
	public void handleShutDownRequest() {
		// If the controller has to not listen to view events, the method does nothing.
		if( ! this.listenToViewRequests ) {
			return;
		}
		// Synchronized block since all of these operations have to be consistent with the
		// state of the simulation engine.
		synchronized( this.engineState ) {
			// If the simulation is not in an appropriate state, the request is ignored.
			EngineState currentState = this.engineState; 
			if( ! currentState.allowsEject() ) {
				System.err.println(
					"Ignored a server shutdown request (current state : "
					+ currentState
					+ ")."
				);
				return;
			}
			// Request the abortion of the currently running simulation and tell the controller to go 
			// into the inactive state.
			this.changeEngineState( EngineState.SHUTDOWN_REQUESTED );
			this.engine.requestSimulationAbortion( );
		}
	}

	/**
	 * Asks the requested for the value of a specific simulation parameter.
	 * @param parameter The name of the parameter.
	 * @return The value of the parameter, or an error text if the parameter cannot be found.
	 */
	@Override
	public String getParameter(String parameter) {
		if( ! this.listenToViewRequests ) {
			return "";
		}
		try {
			return this.simulationParameters.getClass().getField(parameter).get(
				this.simulationParameters
			).toString();
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new ParameterNotFoundException(e);
		}
	}

	/**
	 * Asks the requested to modify the value of a specific simulation parameter.
	 * @param parameter The name of the parameter.
	 * @param value The value of the parameter.
	 */
	@Override
	public void setParameter(String parameter, String value) {
		if( ! this.listenToViewRequests ) {
			return;
		}
		try {
			Class<?> type = this.simulationParameters.getClass().getField(parameter).getType();
			if(type.equals(String.class)) {
				this.simulationParameters.getClass().getField(parameter).set(
					this.simulationParameters, value
				);
			} else if(type.equals(Integer.TYPE)) {
				this.simulationParameters.getClass().getField(parameter).set(
					this.simulationParameters, (int) Double.parseDouble(value)
				);
				if("finalStep".equals(parameter)) {
					this.simulationParameters.getClass().getField("finalTime").set(
						this.simulationParameters, new SimulationTimeStamp(
							this.simulationParameters.getClass().getField(parameter).getInt(this.simulationParameters)
						)
					);
				}
			} else if(type.equals(Boolean.TYPE)) {
				this.simulationParameters.getClass().getField(parameter).set(
					this.simulationParameters, Boolean.parseBoolean(value)
				);
			} else if(type.equals(Double.TYPE)) {
				this.simulationParameters.getClass().getField(parameter).set(
					this.simulationParameters, Double.parseDouble(value)
				);
			}
		} catch (
				IllegalArgumentException 
			  | IllegalAccessException
			  | NoSuchFieldException
			  | SecurityException e
		) {
			throw new ParameterNotFoundException(e);
		}
	}

	
	/**
	 * Called by the engine before the initialization of a new simulation.
	 */
	@Override
	public void prepareObservation() {
		/*
		Synchronized block since all of these operations have to be consistent with the
		state of the simulation engine.
		*/
		synchronized( this.engineState ) {
			/*
			Tell that the simulation is initializing if no shutdown or abortion has been
			requested.
			*/
			if( ! this.engineState.isAborting() && ! this.engineState.isShuttingDown() ){
				this.changeEngineState( EngineState.INITIALIZING );
			}
		}
	}

	/**
	 * Called by the engine after the initialization of a new simulation, right before the execution
	 * of the first step.
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTimestamp,
		ISimulationEngine simulationEngine
	) {
		/*
		Synchronized block since all of these operations have to be consistent with the
		state of the simulation engine.
		*/
		synchronized( this.engineState ) {
			/*
			Tell that the simulation is running if no shutdown or abortion has been
			requested.
			*/
			if( ! this.engineState.isAborting() && ! this.engineState.isShuttingDown() ){
				this.changeEngineState( EngineState.RUN );
			}
		}
	}

	/**
	 * Called by the engine at the end of each time step of the simulation.
	 */
	@Override
	public void observeAtPartialConsistentTime(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		/*
		If the simulation is running and a pause is requested, the pause starts.
		Synchronized block since all of these operations have to be consistent with the
		state of the simulation engine.
		*/
		boolean paused = false;
		synchronized( this.engineState ) {
			if( this.engineState.allowsPause() ) {
				paused = this.engineState.equals( EngineState.RUN ) && this.togglePause;
				this.togglePause = false;
				if( paused ){
					this.changeEngineState( EngineState.PAUSED );
				}
			}
		}
		// While the simulation is paused, continue to wait.
		while( paused ) {
			// Wait a little before checking again if the pause has to end.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();	
			}
			/*
			Check if the pause has to end.
			Synchronized block since all of these operations have to be consistent with the
			state of the simulation engine.
			*/
			synchronized( this.engineState ) {
				paused = !this.togglePause
					  && ! this.engineState.isAborting()
					  && ! this.engineState.isShuttingDown();
			}
		}
		/*
		The pause has ended. The simulation is tagged as running again, unless it has to abort or is shutting down.
		Synchronized block since all of these operations have to be consistent with the
		state of the simulation engine.
		*/
		synchronized( this.engineState ) {
			this.togglePause = false;
			if(	 	!this.engineState.isAborting()
				 && !this.engineState.isShuttingDown()
				 && !this.engineState.equals( EngineState.RUN )
			) {
				this.changeEngineState( EngineState.RUN );
			}
		}
	}

	/**
	 * Called by the engine after the last simulation time step.
	 */
	@Override
	public void observeAtFinalTime(
		SimulationTimeStamp finalTimestamp,
		ISimulationEngine simulationEngine
	) {
		/*
	    Does nothing.
		Everything related to the end of a simulation is delegated
		to the "endObservation" method.
		*/
	}

	/**
	 * Called by the engine whenever an error was met during simulation.
	 */
	@Override
	public void reactToError(String errorMessage, Throwable cause) {
		/*
		If an error was met during the simulation, then the state of the engine is inconsistent.
		Therefore, the execution of other simulations has to be prevented.
		*/
		synchronized( this.engineState ) {
			this.viewControls.shutDownView( );
			this.changeEngineState( EngineState.INACTIVE );
		}
	}

	/**
	 * Called by the engine whenever the simulation has stopped because it was aborted.
	 */
	@Override
	public void reactToAbortion(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {
		/*
		Synchronized block since all of these operations have to be consistent with the
		state of the simulation engine.
		*/
		synchronized( this.engineState ) {
			if( ! this.engineState.isShuttingDown() ) {
				// The simulation goes from the ABORT_REQUESTED to the ABORTING state.
				this.changeEngineState( EngineState.ABORTING );
			}
		}
	}

	/**
	 * Called by the engine after the end of a simulation (because it has finished, it was 
	 * aborted or the server is shutting down).
	 */
	@Override
	public void endObservation() {
		/*
		Synchronized block since all of these operations have to be consistent with the
		state of the simulation engine.
		*/
		synchronized( this.engineState ) {
			if( this.engineState.isShuttingDown() ) {
				// The simulation goes from the SHUTDOWN_REQUESTED to the INACTIVE state.
				this.changeEngineState( EngineState.INACTIVE );
			} else {
				// The simulation goes to the IDLE state and waits for a new simulation to start.
				this.changeEngineState( EngineState.IDLE );
			}
		}
	}
	
	/**
	 * Modifies the state of the simulation engine to the designated state.
	 * 
	 * This method has to be called in a consistent environment (usually, a synchronized block
	 * over the engineState field.
	 * @param newState The new state of the simulation engine.
	 */
	private void changeEngineState( EngineState newState ){
		this.engineState = newState;
		// Update the control button in the view
		this.viewControls.setStartButtonState( newState.allowsNewRun() );
		this.viewControls.setPauseButtonState( newState.allowsPause() );
		this.viewControls.setAbortButtonState( newState.allowsAbort() );
	}
}