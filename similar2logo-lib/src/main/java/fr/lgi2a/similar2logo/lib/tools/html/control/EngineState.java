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

/**
 * The state of the simulation engine.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com>Antoine Lecoutre</a>
 */
public enum EngineState {
	/**
	 * State of the engine when it has nothing to do
	 */
	IDLE( true, false, false, true, false, false ), 
	/**
	 * State of the engine when a new simulation run is planned by has not started yet.
	 */
	RUN_PLANNED( false, true, true, true, false, false ),
	/**
	 * State of the engine when a new simulation run is currently initializing.
	 */
	INITIALIZING( false, true, true, true, false, false ),
	/**
	 * State of the engine when the simulation is currently running.
	 */
	RUN( false, true, true, true, false, false ),
	/**
	 * State of the engine when it is paused, and waits for a user input to resume.
	 */
	PAUSED( false, true, true, true, false, false ),
	/**
	 * State of the engine when a simulation was running by the user requested its abortion.
	 */
	ABORT_REQUESTED( false, false, false, true, true, false ),
	/**
	 * State of the engine when its abortion has been requested.
	 */
	ABORTING( false, false, false, true, true, false ),
	/**
	 * State of the engine when the shutdown of the server has been requested.
	 */
	SHUTDOWN_REQUESTED( false, false, false, false, false, true ),
	/**
	 * State of the engine when the server is shut down.
	 */
	INACTIVE( false, false, false, false, false, true );

	/**
	 * <code>true</code> if the state of the engine allows for new simulations to run.
	 */
	private boolean allowsNewRun;
	/**
	 * <code>true</code> if the state of the engine allows for the current simulation to pause.
	 */
	private boolean allowsPause;
	/**
	 * <code>true</code> if the state of the engine allows for the current simulation to be aborted.
	 */
	private boolean allowsAbort;
	/**
	 * <code>true</code> if the state of the engine allows for the shutdown of the Spark Server.
	 */
	private boolean allowsEject;
	/**
	 * <code>true</code> if the state of the engine tells that the current simulation is aborting.
	 */
	private boolean pendingAbortion;
	/**
	 * <code>true</code> if the state of the engine tells that the server is shutting down or already stopped.
	 */
	private boolean pendingShutdown;
	
	/**
	 * Builds a new item of the enumeration providing the appropriate values to its fields.
	 */
	private EngineState(
		boolean allowsNewRun,
		boolean allowsPause,
		boolean allowsAbort,
		boolean allowsEject,
		boolean pendingAbortion,
		boolean pendingShutdown
	){
		this.allowsNewRun = allowsNewRun;
		this.allowsPause = allowsPause;
		this.allowsAbort = allowsAbort;
		this.allowsEject = allowsEject;
		this.pendingAbortion = pendingAbortion;
		this.pendingShutdown = pendingShutdown;
	}

	/**
	 * @return <code>true</code> if the state of the engine allows for new simulations to run.
	 */
	public boolean allowsNewRun( ) {
		return this.allowsNewRun;
	}
	/**
	 * @return <code>true</code> if the state of the engine allows for the current simulation to pause.
	 */
	public boolean allowsPause( ) {
		return this.allowsPause;
	}
	/**
	 * @return <code>true</code> if the state of the engine allows for the current simulation to be aborted.
	 */
	public boolean allowsAbort( ) {
		return this.allowsAbort;
	}
	/**
	 * @return <code>true</code> if the state of the engine allows for the shutdown of the Spark Server.
	 */
	public boolean allowsEject( ) {
		return this.allowsEject;
	}
	/**
	 * <code>true</code> if the state of the engine tells that the current simulation is aborting.
	 */
	public boolean isAborting(){
		return this.pendingAbortion;
	}
	/**
	 * <code>true</code> if the state of the engine tells that the server is shutting down or already stopped.
	 */
	public boolean isShuttingDown(){
		return this.pendingShutdown;
	}
}