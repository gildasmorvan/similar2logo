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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.probes;

import static spark.Spark.get;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractProbe;

/**
 * Probe for knowing the state of the traffic in the simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TrafficProbe  extends AbstractProbe {
	
	/**
	 * The number of subdivisions in abscissa and ordinate
	 */
	private int n,m;
	
	/**
	 * The number of step by second.
	 */
	private int step;
	
	/**
	 * The StringBuilder where the data are written.
	 */
	private StringBuilder output;
	
	/**
	 * The StringBuilder where the JSON data are written.
	 */
	private StringBuilder heatmapOutput;
	
	public TrafficProbe(int n, int m, int step) {
		this.n = n;
		this.m = m;
		this.step = step;
		this.output =  new StringBuilder();
		get("/result.txt", (request, response) -> {
    			return this.getOutputAsString();
		});	
	}

	@Override
	public void observeAtPartialConsistentTime(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		LogoEnvPLS env = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
		heatmapOutput = new StringBuilder();
		int[][] nbrCar = new int[n][m];
		double[][] frequency = new double[n][m];
		double[][] nbrPassengers = new double[n][m];
		List<Double> means = new ArrayList<>();
		double meanSpeed = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				nbrCar[i][j] = 0;
				frequency[i][j] = 0;
				nbrPassengers[i][j] = 0;
			}
		}
		for (int i = 0; i < env.getWidth(); i++) {
			for (int j = 0; j < env.getHeight(); j++) {
				for (TurtlePLSInLogo t : env.getTurtlesAt(i, j)) {
					if (t.getCategoryOfAgent().equals(CarCategory.CATEGORY)) {
						CarPLS car = (CarPLS) t;
						Double x = t.getLocation().getX()*n/env.getWidth();
						Double y = t.getLocation().getY()*m/env.getHeight();
						nbrCar[x.intValue()][y.intValue()]++;
						frequency[x.intValue()][y.intValue()] += car.getFrequence();
						nbrPassengers[x.intValue()][y.intValue()] += car.getNbrPassenger();
					}
				}
			}
		}
		heatmapOutput.append("[ { \"type\" : \"heatmap\" }, { \"z\" : [");
		for (int i = 0; i < n; i++) {
			heatmapOutput.append("[");
			for (int j=0; j < m ; j++) {
				if (nbrCar[i][j] != 0) {
					frequency[i][j] /= nbrCar[i][j];
					frequency[i][j] = (step/frequency[i][j])*3.6;
					nbrPassengers[i][j] /= nbrCar[i][j];
					meanSpeed += frequency[i][j];
					means.add(frequency[i][j]);
					if (j != (m-1)) {
						heatmapOutput.append(frequency[i][j]+", ");
					} else {
						heatmapOutput.append(frequency[i][j]+"]");
					}
				} else {
					if (j != (m-1)) {
						heatmapOutput.append("0, ");
					} else {
						heatmapOutput.append("0]");
					}
				}
			}
			if (i != (n-1))  {
				heatmapOutput.append(",");
			}
		}
		heatmapOutput.append("] } ]");
		output.append(timestamp.getIdentifier());
		output.append("\t");
		output.append(meanSpeed/(n*m));
		output.append("\t");
		output.append(Collections.min(means));
		output.append("\t");
		output.append(Collections.max(means));
		output.append("\n");
		if(ZoneDataWebSocket.wsLaunch){
			ZoneDataWebSocket.sendJsonProbe(heatmapOutput.toString());
		}
	}
	
	private String getOutputAsString() {
		return output.toString();
	}

}
