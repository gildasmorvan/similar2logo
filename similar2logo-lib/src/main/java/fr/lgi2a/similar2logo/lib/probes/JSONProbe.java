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
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
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
package fr.lgi2a.similar2logo.lib.probes;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * A probe printing information about agent population in a given target.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class JSONProbe implements IProbe {

	/**
	 * The JSON output
	 */
	private byte[] output;

	/**
	 * <code>true</code> if agent states are exported, <code>false</code> else.
	 */
	private boolean exportAgents;

	/**
	 * <code>true</code> if marks are exported, <code>false</code> else.
	 */
	private boolean exportMarks;

	/**
	 * Creates a new instance of this probe with default parameters.
	 */
	public JSONProbe() {
		this.exportAgents = true;
		this.exportMarks = true;
	}
	
	/**
	 * Creates a new instance of this probe with given parameters.
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 */
	public JSONProbe(boolean exportAgents, boolean exportMarks) {
		this.exportAgents = exportAgents;
		this.exportMarks = exportMarks;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(SimulationTimeStamp initialTimestamp,
			ISimulationEngine simulationEngine) {
		this.output = handleJSONexport(simulationEngine);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine) {
		this.output = handleJSONexport(simulationEngine);
	}

	/**
	 * @return the grid data in the JSON format
	 */
	private byte[] handleJSONexport(ISimulationEngine simulationEngine) {
		IPublicLocalDynamicState simulationState = simulationEngine
				.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		LogoEnvPLS env = (LogoEnvPLS) simulationState
				.getPublicLocalStateOfEnvironment();
		String output = "{";
		if (this.exportAgents) {
			output += "\"agents\": [";
			for (ILocalStateOfAgent agtState : simulationState
					.getPublicLocalStateOfAgents()) {
				TurtlePLSInLogo castedAgtState = (TurtlePLSInLogo) agtState;
				output += "{";
				output += "\"x\": \"" + castedAgtState.getLocation().getX()
						/ env.getWidth() + "\",";
				output += "\"y\": \"" + castedAgtState.getLocation().getY()
						/ env.getHeight() + "\",";
				output += "\"type\": \"" + castedAgtState.getCategoryOfAgent()
						+ "\"";
				output += "},";
			}
			output += "{}]";
		}
		if (this.exportAgents && this.exportMarks) {
			output += ",";
		}
		if (this.exportMarks) {
			output += "\"marks\": [";
			LogoEnvPLS environment = (LogoEnvPLS) simulationState
					.getPublicLocalStateOfEnvironment();
			for (int x = 0; x < environment.getWidth(); x++) {
				for (int y = 0; y < environment.getHeight(); y++) {
					if (!environment.getMarksAt(x, y).isEmpty()) {
						output += "{";
						output += "\"x\": \"" + ((double) x) / env.getWidth()
								+ "\",";
						output += "\"y\": \"" + ((double) y) / env.getHeight()
								+ "\"";
						output += "},";
					}
				}
			}
			output += "{}]";
		}
		output += "}";
		return output.getBytes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(SimulationTimeStamp finalTimestamp,
			ISimulationEngine simulationEngine) {

		this.output = handleJSONexport(simulationEngine);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(String errorMessage, Throwable cause) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine) {
	}

	/**
	 * @return the grid in the JSON format
	 */
	public byte[] getOutput() {
		return output;
	}
}
