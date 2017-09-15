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

import static spark.Spark.get;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.similar2logo.examples.transport.model.Station;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Probe for knowing the number of people waiting in the stations and some others statistics.
 * This probe has been made for the presentation of the September 23rd 2017 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class StationsProbe implements IProbe {
	
	/**
	 * The list of stations/stops for each type of transport.
	 */
	private Map<String,List<Station>> stations;
	
	private StringBuilder waitingPeoples;
	
	public StationsProbe (Map<String,List<Station>> stations) {
		this.stations = stations;
		this.waitingPeoples =  new StringBuilder();
		get("/waitingPeoples.txt", (request, response) -> {
    		return this.waitingPeoples.toString();
    	});	
	}

	@Override
	public void prepareObservation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void observeAtInitialTimes(SimulationTimeStamp initialTimestamp, ISimulationEngine simulationEngine) {
		// TODO Auto-generated method stub

	}

	@Override
	@SuppressWarnings("rawtypes")
	public void observeAtPartialConsistentTime(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		LogoEnvPLS env = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
		Set<Mark>[][] marks = env.getMarks();
		for (int i = 0; i < marks.length; i++) {
			for (int j=0; j < marks[0].length; j++) {
				for (Mark m : marks[i][j]) {
					if (m.getCategory().equals("Station")) {
						waitingPeoples.append(timestamp.getIdentifier());
						waitingPeoples.append("\t");
						waitingPeoples.append(stations.get("Railway").get(0).getWaitingPeople());
						waitingPeoples.append("\t");
						waitingPeoples.append(getContentmentRate(stations.get("Railway").get(0).meanWaitingTime(timestamp)));
						waitingPeoples.append("\t");
						waitingPeoples.append(stations.get("Railway").get(0).getNbrTransport()*10);
						waitingPeoples.append("\n");
					}
				}
			}
		}
	}

	@Override
	public void observeAtFinalTime(SimulationTimeStamp finalTimestamp, ISimulationEngine simulationEngine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToError(String errorMessage, Throwable cause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToAbortion(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endObservation() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Gives a contentment rate. 
	 * @param waitingTime the mean time people are waiting
	 * @return the mean content rate
	 */
	private double getContentmentRate (double waitingTime) {
		return Math.max(100 - waitingTime/8,0);
	}

}
