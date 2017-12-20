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
package fr.lgi2a.similar2logo.lib.exploration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.lib.probes.ExplorationProbe;

/**
 * Abstract class for the usage of the exploration in python
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public abstract class ExplorationForPython {
	
	/**
	 * The parameters of the simulations
	 */
	protected LogoSimulationParameters parameters;
	
	public ExplorationForPython (LogoSimulationParameters lsp) {
		this.parameters = lsp;
	}
	
	/**
	 * @param simulations the simulations to check
	 * @return <code>true</code> if the simulations are finished.
	 */
	public boolean isExplorationOver(List<ExplorationSimulationModel> simulations) {
		for(ExplorationSimulationModel simulation : simulations) {
			if(!( (ExplorationProbe) simulation.getEngine().getProbe("Exploration probe")).isFinished()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Runs the simulation for the time chose
	 * @param simulations the simulations to runs
	 * @return the simulations after they run
	 */
	public List<ExplorationSimulationModel> runSimulations (List<ExplorationSimulationModel> simulations) {
		int thread = Runtime.getRuntime().availableProcessors();
		ExecutorService es = Executors.newFixedThreadPool(thread);
		List<Future<Void>> taskList = new ArrayList<>();
		for (int i= 0; i < simulations.size(); i++) {
			int tmp = i;
			Future<Void> futureTask = es.submit(new Callable<Void>() {
        		public Void call() {
            		return simulations.get(tmp).runSimulation();
            	}
        	});
			taskList.add(futureTask);
		}
		es.shutdown();
		return simulations;
	}
	
	/**
	 * Set the duration of the next simulation
	 * @param sts the duration of the next simulation
	 */
	public void setNextStep (SimulationTimeStamp sts) {
		this.parameters.initialTime = new SimulationTimeStamp(0);
		this.parameters.finalTime = new SimulationTimeStamp(sts);
	}
	
	/**
	 * Makes n copies of the simulation
	 * @param esm the simulation to copy
	 * @param n the number to copy to produce
	 * @return a list with all the copies
	 */
	public List<ExplorationSimulationModel> makeCopies (ExplorationSimulationModel esm, int n) {
		List<ExplorationSimulationModel> res = new ArrayList<>();
		for (int i =0; i < n; i++) {
			res.add(copySimulation(esm));
		}
		return res;
	}
	
	/**
	 * Make a copy of a simulation
	 * @param esm the simulation to copy
	 * @return the copy of the simulation
	 */
	protected abstract ExplorationSimulationModel copySimulation (ExplorationSimulationModel esm);
	
	/**
	 * Generates the simulation at the beginning
	 * @param n the quantity to generate
	 * @return the simulations for the beginning of the exploration
	 */
	public abstract List<ExplorationSimulationModel> generateSimulation (int n);
	
}
