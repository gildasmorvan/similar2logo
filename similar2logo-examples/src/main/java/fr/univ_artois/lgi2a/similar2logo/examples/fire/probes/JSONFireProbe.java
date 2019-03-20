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
package fr.univ_artois.lgi2a.similar2logo.examples.fire.probes;

import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractProbe;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.model.TreeAgentPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.view.GridWebSocket;

/**
 * A probe printing information about agent population in a given target.
 * 
 * @author <a xavier_szkudlarek@univ-artois.fr target="_blank">Szkudlarek
 *         Xavier</a>
 * 
 *
 */
public class JSONFireProbe extends AbstractProbe {

	/**
	 * The level that will be probed.
	 */
	private LevelIdentifier levelIdentifier;

	/**
	 * Creates a new instance of this probe for the LOGO level with default
	 * parameters.
	 */
	public JSONFireProbe() {
		this.levelIdentifier = LogoSimulationLevelList.LOGO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine) {
		if (GridWebSocket.wsLaunch) {
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

	/**
	 * @param simulationEngine
	 *            The simulation engine.
	 * @return the grid data in the JSON format
	 */
	protected String handleJSONexport(ISimulationEngine simulationEngine) {
		IPublicLocalDynamicState simulationState = simulationEngine
				.getSimulationDynamicStates().get(this.levelIdentifier);
		LogoEnvPLS env = (LogoEnvPLS) simulationState
				.getPublicLocalStateOfEnvironment();

		StringBuilder output = new StringBuilder();
		output.append("{");
		output.append("\"agents\":[");
		for (ILocalStateOfAgent agtState : simulationState
				.getPublicLocalStateOfAgents()) {
			TreeAgentPLS castedAgtState = (TreeAgentPLS) agtState;
			output.append("{");
			output.append("\"x\":");
			appendTo3(output,
					castedAgtState.getLocation().getX() / env.getWidth());
			output.append(",");
			output.append("\"y\":");
			appendTo3(output,
					castedAgtState.getLocation().getY() / env.getHeight());
			output.append(",");
			output.append("\"b\":\"");
			appendTo3(output, castedAgtState.getBurned());
			output.append("\"},");
		}
		output.append("{}]");
		output.append("}");
		return output.toString();

	}
}
