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
package fr.univ_artois.lgi2a.similar2logo.lib.probes;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.view.GridWebSocket;

/**
 * A probe printing information about agent population in a given target.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class JSONProbe implements IProbe {

	/**
	 * The level that will be probed.
	 */
	private LevelIdentifier levelIdentifier;
	
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
	 * Creates a new instance of this probe for the LOGO level with default parameters.
	 */
	public JSONProbe() {
		this.exportAgents = true;
		this.exportMarks = true;
		this.exportPheromones = true;
		this.levelIdentifier = LogoSimulationLevelList.LOGO;
	}
	
	/**
	 * Creates a new instance of this probe for a given level with default parameters.
	 * @param levelIdentifier the probed level.
	 */
	public JSONProbe(LevelIdentifier levelIdentifier) {
		this.exportAgents = true;
		this.exportMarks = true;
		this.exportPheromones = true;
		this.levelIdentifier = levelIdentifier;
	}
	
	
	/**
	 * Creates a new instance of this probe for the LOGO level with given parameters.
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 * @param exportPheromones <code>true</code> if exportPheromones are exported, <code>false</code> else.
	 */
	public JSONProbe(boolean exportAgents, boolean exportMarks, boolean exportPheromones) {
		this.exportAgents = exportAgents;
		this.exportMarks = exportMarks;
		this.exportPheromones = exportPheromones;
		this.levelIdentifier = LogoSimulationLevelList.LOGO;
	}
	
	/**
	 * Creates a new instance of this probe for the LOGO level with given parameters.
	 * @param levelIdentifier the probed level.
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 * @param exportPheromones <code>true</code> if exportPheromones are exported, <code>false</code> else.
	 */
	public JSONProbe(
		LevelIdentifier levelIdentifier,
		boolean exportAgents,
		boolean exportMarks,
		boolean exportPheromones
	) {
		this.exportAgents = exportAgents;
		this.exportMarks = exportMarks;
		this.exportPheromones = exportPheromones;
		this.levelIdentifier = levelIdentifier;
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
	
	public static void appendTo3(StringBuilder builder, double d) {
	    if (d < 0) {
	        builder.append('-');
	        d = -d;
	    }
	    if (d * 1e6 + 0.5 > Long.MAX_VALUE) {
	        throw new IllegalArgumentException("number too large");
	    }
	    long scaled = (long) (d * 1e6 + 0.5);
	    long factor = 1_000_000;
	    int scale = 4;
	    long scaled2 = scaled / 10;
	    while (factor <= scaled2) {
	        factor *= 10;
	        scale++;
	    }
	    while (scale > 0) {
	        if (scale == 3) {
	        		builder.append('.');
	        }
	        long c = scaled / factor % 10;
	        factor /= 10;
	        builder.append((char) ('0' + c));
	        scale--;
	    }
	}
	
	private void exportAgents(
		IPublicLocalDynamicState simulationState,
		StringBuilder output
	) {
		LogoEnvPLS env = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
		output.append("\"agents\":[");
		for (ILocalStateOfAgent agtState : simulationState.getPublicLocalStateOfAgents()) {
			TurtlePLSInLogo castedAgtState = (TurtlePLSInLogo) agtState;
			output.append("{");
			output.append("\"x\":");
			appendTo3(output,castedAgtState.getLocation().getX() / env.getWidth());
			output.append(",");
			output.append("\"y\":");
			appendTo3(output,castedAgtState.getLocation().getY()/ env.getHeight());
			output.append(",");
			output.append("\"t\":\"");
			output.append(castedAgtState.displayedCategory());
			output.append("\"},");
		}
		output.append("{}]");
	}
	
	@SuppressWarnings("rawtypes")
	private void exportMarks(
		IPublicLocalDynamicState simulationState,
		StringBuilder output
	) {
		LogoEnvPLS env = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
		output.append("\"marks\":[");
		for (int x = 0; x < env.getWidth(); x++) {
			for (int y = 0; y < env.getHeight(); y++) {
				Iterator<Mark> marks = env.getMarksAt(x, y).iterator();
				if (marks.hasNext()) {
					Mark theMarks = marks.next();
					output.append("{\"x\":");
					appendTo3(output,((double) x) / env.getWidth());
					output.append(",");
					output.append("\"y\":");
					appendTo3(output,((double) y) / env.getHeight());
					output.append(",");
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
	
	private void exportPheromones(
		IPublicLocalDynamicState simulationState,
		StringBuilder output
	) {
		LogoEnvPLS env = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
		output.append("\"pheromones\":[");
		for(Map.Entry<Pheromone, double[][]> field : env.getPheromoneField().entrySet()) {
			for (int x = 0; x < env.getWidth(); x++) {
				for (int y = 0; y < env.getHeight(); y++) {
					if (field.getValue()[x][y] >= 0.1) {
						output.append("{\"x\":");
						appendTo3(output,((double) x) / env.getWidth());
						output.append(",");
						output.append("\"y\":");
						appendTo3(output,((double) y) / env.getHeight());
						output.append(",");
						output.append("\"t\":\"");
						output.append(field.getKey().getIdentifier());
						output.append("\",");
						output.append("\"v\":");
						appendTo3(output,field.getValue()[x][y]);
						output.append("},");
					}
				}
			}
		}
		output.append("{}]");
	}

	/**
	 * @param simulationEngine The simulation engine.
	 * @return the grid data in the JSON format
	 */
	protected String handleJSONexport(ISimulationEngine simulationEngine) {
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get(
			this.levelIdentifier
		);

		StringBuilder output =  new StringBuilder();
		output.append("{");
		if (this.exportAgents) {
			exportAgents(simulationState, output);
		}
		if (this.exportAgents && this.exportMarks) {
			output.append(",");
		}
		if (this.exportMarks) {
			exportMarks(simulationState, output);
		}
		if (this.exportAgents && this.exportPheromones) {
			output.append(",");
		}
		if (this.exportPheromones) {
			exportPheromones(simulationState, output);
		}
		output.append("}");
		return output.toString();
	
	}
}
