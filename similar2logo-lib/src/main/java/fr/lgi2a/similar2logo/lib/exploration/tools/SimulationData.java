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
package fr.lgi2a.similar2logo.lib.exploration.tools;

import java.util.HashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;

/**
 * Class for the management of the data of a simulation. Can be extended in
 * rewriting the methods exportData and importData.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class SimulationData implements Cloneable {

	/**
	 * Time when the simulation started
	 */
	protected SimulationTimeStamp currentTime;

	/**
	 * Current time of the simulation
	 */
	protected SimulationTimeStamp endTime;

	/**
	 * Agents for the next simulation
	 */
	protected Set<TurtlePLSInLogo> agents;

	/**
	 * Environment for the next simulation
	 */
	protected LogoEnvPLS environment;

	/**
	 * Indicates if the simulation is over or not
	 */
	protected boolean isOver;

	/**
	 * Constructor of the simulation data.
	 * @param currentTime time when the simulation started
	 */
	public SimulationData(SimulationTimeStamp startTime) {
		isOver = false;
		this.currentTime = startTime;
	}

	/**
	 * Give the agents for launching a new simulation.
	 * @return The agents for the next simulation
	 */
	public Set<TurtlePLSInLogo> getAgents() {
		return agents;
	}

	/**
	 * Set the agents in forecast of the next simulation.
	 * @param agents The agents for the next simulation
	 */
	public void setAgents(Set<TurtlePLSInLogo> agents) {
		this.agents = agents;
	}

	/**
	 * Give the environment for launching a new simulation.
	 * @return The environment for the next simulation
	 */
	public LogoEnvPLS getEnvironment() {
		return environment;
	}

	/**
	 * Set the environment in forecast of the next simulation.
	 * @param environment The environment for the next simulation
	 */
	public void setEnvironment(LogoEnvPLS environment) {
		this.environment = environment;
	}

	/**
	 * Export the data of the simulation in the file in path.
	 * @param path The path of the file that will receive the data
	 */
	public void exportData(String path) {
		// The base class does nothing
	}

	/**
	 * Import the data of the simulation from the file in path.
	 * @param path The path of the file where are the data
	 */
	public void importData(String path) {
		// The base class does nothing
	}

	/**
	 * Give the moment when the simulation has been stoped.
	 * @return The time of the simulation data
	 */
	public SimulationTimeStamp getTime() {
		return this.currentTime;
	}

	/**
	 * Set the end time of the simulation
	 * @param end the time of the end of the simulation
	 */
	public void setTime(SimulationTimeStamp end) {
		this.currentTime = new SimulationTimeStamp(currentTime.getIdentifier() + end.getIdentifier());
		this.endTime = end;
	}

	/**
	 * Indicate if the simulation is over
	 * @return if the simulation is over
	 */
	public boolean getIsOver() {
		return this.isOver;
	}

	/**
	 * Prevents that the simulation is over.
	 */
	public void preventEndSimulation() {
		this.isOver = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object clone () {
		System.out.println("clone");
		SimulationData sd = new SimulationData(new SimulationTimeStamp(currentTime.getIdentifier()));
		sd.agents = new HashSet<>();
		for (TurtlePLSInLogo turtle : agents) {
			sd.agents.add((TurtlePLSInLogo) turtle.clone());
		}
		sd.environment = (LogoEnvPLS) this.environment.clone();
		sd.currentTime = new SimulationTimeStamp(currentTime.getIdentifier());
		sd.endTime = new SimulationTimeStamp(endTime.getIdentifier());
		/*try {
			sd = (SimulationData) super.clone();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		sd.agents = new HashSet<>();
		for (TurtlePLSInLogo turtle : agents)
			sd.agents.add((TurtlePLSInLogo) turtle.clone());
		sd.environment = (LogoEnvPLS) this.environment.clone();
		sd.currentTime = new SimulationTimeStamp(currentTime.getIdentifier());
		sd.endTime = new SimulationTimeStamp(endTime.getIdentifier());*/
		return sd;
	}

}
