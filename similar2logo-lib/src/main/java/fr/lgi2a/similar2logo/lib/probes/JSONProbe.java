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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.tools.html.view.GridWebSocket;

/**
 * A probe printing information about agent population in a given target.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class JSONProbe implements IProbe {

	/**
	 * <code>true</code> if agent states are exported, <code>false</code> else.
	 */
	private boolean exportAgents;

	/**
	 * <code>true</code> if marks are exported, <code>false</code> else.
	 */
	private boolean exportMarks;
	
	/**
	 * <code>true</code> if pheromones are exported, <code>false</code> else.
	 */
	private boolean exportPheromones;

	/**
	 * Creates a new instance of this probe with default parameters.
	 */
	public JSONProbe() {
		this.exportAgents = true;
		this.exportMarks = true;
		this.exportPheromones = true;
	}
	
	/**
	 * Creates a new instance of this probe with given parameters.
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 */
	public JSONProbe(boolean exportAgents, boolean exportMarks, boolean exportPheromones) {
		this.exportAgents = exportAgents;
		this.exportMarks = exportMarks;
		this.exportPheromones = exportPheromones;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() {
		//Does nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTimestamp,
		ISimulationEngine simulationEngine
	) {
		//Does nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		if(GridWebSocket.wsLaunch){
			GridWebSocket.sendJsonProbe(handleJSONexport(simulationEngine));
		}
	}

	/**
	 * @param simulationEngine The simulation engine.
	 * @return the grid data in the JSON format
	 */
	@SuppressWarnings("rawtypes")
	private String handleJSONexport(ISimulationEngine simulationEngine) {
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		LogoEnvPLS env = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
		formatter.applyPattern("#0.000");
		
		StringBuilder output =  new StringBuilder();
		output.append("{");
		if (this.exportAgents) {
			output.append("\"agents\":[");
			for (ILocalStateOfAgent agtState : simulationState.getPublicLocalStateOfAgents()) {
				TurtlePLSInLogo castedAgtState = (TurtlePLSInLogo) agtState;
				output.append("{");
				output.append("\"x\":\"");
				output.append(formatter.format(castedAgtState.getLocation().getX() / env.getWidth()));
				output.append("\",");
				output.append("\"y\":\"");
				output.append(formatter.format(castedAgtState.getLocation().getY()/ env.getHeight()));
				output.append("\",");
				output.append("\"t\":\"");
				output.append(castedAgtState.getCategoryOfAgent());
				output.append("\"},");
			}
			output.append("{}]");
		}
		if (this.exportAgents && this.exportMarks) {
			output.append(",");
		}
		if (this.exportMarks) {
			output.append("\"marks\":[");
			LogoEnvPLS environment = (LogoEnvPLS) simulationState
					.getPublicLocalStateOfEnvironment();

			Set<Mark>[][] marks = environment.getMarks();
			for (int x = 0; x < environment.getWidth(); x++) {
				for (int y = 0; y < environment.getHeight(); y++) {
					if (!environment.getMarksAt(x, y).isEmpty()) {
						Mark theMarks = marks[x][y].iterator().next();
						output.append("{\"x\":\"");
						output.append(formatter.format(((double) x) / env.getWidth()));
						output.append("\",");
						output.append("\"y\":\"");
						output.append(formatter.format(((double) y) / env.getHeight()));
						output.append("\",");
						output.append("\"t\":\"");
						output.append(theMarks.getCategory());
						output.append("\",");
						output.append("\"v\":\"");
						output.append(theMarks.getContent());
						output.append("\"},");
					}
				}
			}
			
			output.append("{}]");
		}
		if (this.exportAgents && this.exportPheromones) {
			output.append(",");
		}
		if (this.exportPheromones) {
			output.append("\"pheromones\":[");
			LogoEnvPLS environment = (LogoEnvPLS) simulationState
					.getPublicLocalStateOfEnvironment();
			for(Map.Entry<Pheromone, double[][]> field : environment.getPheromoneField().entrySet()) {
				for (int x = 0; x < environment.getWidth(); x++) {
					for (int y = 0; y < environment.getHeight(); y++) {
						if (!(field.getValue()[x][y] < 1)) {
							output.append("{\"x\":\"");
							output.append(formatter.format(((double) x) / env.getWidth()));
							output.append("\",");
							output.append("\"y\":\"");
							output.append(formatter.format(((double) y) / env.getHeight()));
							output.append("\",");
							output.append("\"v\":\"");
							output.append(field.getValue()[x][y]);
							output.append("\"},");
						}
					}
				}
			}
			output.append("{}]");
		}
		output.append("}");
		return output.toString();
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(SimulationTimeStamp finalTimestamp,
			ISimulationEngine simulationEngine) {
		//Does nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() {
		//Does nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(String errorMessage, Throwable cause) {
		//Does nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine) {
		//Does nothing
	}
}
