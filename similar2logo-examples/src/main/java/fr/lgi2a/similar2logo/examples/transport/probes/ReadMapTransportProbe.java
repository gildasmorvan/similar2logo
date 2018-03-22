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
package fr.lgi2a.similar2logo.examples.transport.probes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractProbe;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Probe for reading the marks only one time at the beginning.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class ReadMapTransportProbe extends AbstractProbe {
	
	public ReadMapTransportProbe(){
		// Does nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(SimulationTimeStamp initialTimestamp, ISimulationEngine simulationEngine) {
		if(MapWebSocket.wsLaunch){
			MapWebSocket.sendJsonProbe(recoverWorld(simulationEngine,true));
		}
	}

	/**
	 * Convert the marks in a JSon string
	 * @param simulationEngine the simulation engine
	 * @param getMarks indicate if we have to take the marks
	 * We don't take them during the simulation
	 * @return a string including the marks in Json format
	 */
	@SuppressWarnings("rawtypes")
	private static String recoverWorld (ISimulationEngine simulationEngine, boolean getMarks) {
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		LogoEnvPLS env = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
		formatter.applyPattern("#0.000");
		
		StringBuilder output =  new StringBuilder();
		output.append("{");
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
		if (getMarks) {
		output.append(",");
		
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
		}
		
		output.append("{}]");
		output.append("}");
		return output.toString();
	}
}
