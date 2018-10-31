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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.exploration.probes;

import fr.univ_artois.lgi2a.similar2logo.examples.transport.exploration.data.SimulationDataTransport;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;

/**
 * Probe for filling the data of the transport simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class TransportSimilationForExplorationProbe implements IProbe {
	
	/**
	 * The transport simulation data
	 */
	private SimulationDataTransport data;
	
	/**
	 * The number of horizontal and vertical sections.
	 */
	private int n,m;
	
	/**
	 * The number of step by second
	 */
	private int step;
	
	public TransportSimilationForExplorationProbe (SimulationDataTransport sdt, int n, int m, int step) {
		data = sdt;
		this.n = n;
		this.m = m;
		this.step = step;
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
	public void observeAtPartialConsistentTime(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void observeAtFinalTime(SimulationTimeStamp finalTimestamp, ISimulationEngine simulationEngine) {
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		LogoEnvPLS env = (LogoEnvPLS) simulationState.getPublicLocalStateOfEnvironment();
		int[][] nbrCar = new int[n][m];
		double[][] frequency = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				nbrCar[i][j] = 0;
				frequency[i][j] = 0;
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
					}
				}
			}
		}
		data.setNumberCars(nbrCar);
		data.setMeanFrequencies(frequency);
		for (int i = 0; i < n; i++) {
			for (int j=0; j < m ; j++) {
				if (nbrCar[i][j] != 0) {
					frequency[i][j] /= nbrCar[i][j];
					frequency[i][j] = step/frequency[i][j]*10000/3600;
				}
			}
		}
	}

	@Override
	public void reactToError(String errorMessage, Throwable cause) {
		//Does nothing

	}

	@Override
	public void reactToAbortion(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {
		//Does nothing

	}

	@Override
	public void endObservation() {
		//Does nothing
	}

}
