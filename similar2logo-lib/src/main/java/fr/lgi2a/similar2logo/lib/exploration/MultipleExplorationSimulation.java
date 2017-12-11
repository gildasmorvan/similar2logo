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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.tools.SimulationData;
import fr.lgi2a.similar2logo.lib.exploration.treatment.ITreatment;

/**
 * Abstract class for the multiple exploration simulation. 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public abstract class MultipleExplorationSimulation {

	/**
	 * The list of simulations.
	 */
	protected List<ExplorationSimulationModel> simulations;
	
	/**
	 * Parameters of the simulation.
	 * You can have several type of parameters to choose when you make the simulation
	 */
	protected LogoSimulationParameters parameters;
	
	/**
	 * The current time of the simulation
	 */
	protected SimulationTimeStamp currentTime;
	
	/**
	 * The end time of the simulation
	 */
	protected SimulationTimeStamp endTime;
	
	/**
	 * The checkpoints of the simulation, i.e the moments when the simulations are stopped for being treated.
	 * The checkpoints bigger than the end time will be ignored. 
	 */
	protected List<SimulationTimeStamp> checkpoints;
	
	/**
	 * Treatment of the simulations
	 */
	protected ITreatment treatment;
	
	/**
	 * Id used for the print
	 */
	protected String id;
	
	/**
	 * Constructor of the Multiple Exploration Simulation
	 * @param param the parameters of the simulations
	 * @param nbrSimulations the number of simulations
	 * @param end the time when the simulations finish
	 * @param pauses the times when the simulations make a pause
	 * @param treatment the treatment to apply on the simulation after each run
	 */
	public MultipleExplorationSimulation (LogoSimulationParameters param,
			SimulationTimeStamp end, List<SimulationTimeStamp> pauses, ITreatment treatment) {
		this.simulations = new ArrayList<>();
		this.parameters = param;
		this.currentTime = new SimulationTimeStamp(0);
		this.endTime =end;
		this.checkpoints = pauses;
		this.parameters.finalTime = nextCheckpoint();
		this.treatment = treatment;
		this.id = "";
	}
	
	/**
	 * Add a new simulation to run.
	 * @param the logo simulation parameters
	 */
	protected abstract void addNewSimulation (LogoSimulationParameters lsp);
	
	/**
	 * Gives the next checkpoint in the simulation.
	 * @return The SimulationTimeStamp of the next checkpoint.
	 */
	protected SimulationTimeStamp nextCheckpoint () {
		SimulationTimeStamp next = endTime;
		long nTime = next.getIdentifier();
		long cTime = currentTime.getIdentifier();
		for (int i=0; i < checkpoints.size(); i++) {
			long lTime = checkpoints.get(i).getIdentifier();
			if ((lTime > cTime) && (lTime < nTime)) {
				next = checkpoints.get(i);
				nTime = lTime;
			}
		}
		return next;
	}
	
	/**
	 * Create all the simulation need.
	 * @param nbrSimulations The number of simulations to create
	 */
	public void initSimulation (int nbrSimulations) {
		for (int i = 0; i< nbrSimulations; i++) {
			addNewSimulation(parameters);
		}
	}
	
	/**
	 * Runs the simulations.
	 */
	public void runSimulations () {
		while (currentTime.getIdentifier() <= endTime.getIdentifier() -1) {
			this.currentTime = new SimulationTimeStamp(currentTime.getIdentifier() + this.parameters.finalTime.getIdentifier());
                        System.out.println(currentTime);
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
			try {
				FileWriter fw = new FileWriter("./output/data_"+id+"_"+currentTime.getIdentifier()+".txt");
				BufferedWriter bw = new BufferedWriter(fw);
				for (int j = 0; j < simulations.size(); j++) {
		            taskList.get(j).get();
		            SimulationData data = this.simulations.get(j).data;
		            bw.write(data.getData()+"\n");
		        }
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			es.shutdown();
			//this.exportDataFromSimulations("./output/simulations_"+(currentTime.getIdentifier()-1)+".txt");
			this.treatment.treatSimulations(simulations);
			this.parameters.initialTime = new SimulationTimeStamp(0);
			this.parameters.finalTime = new SimulationTimeStamp(nextCheckpoint().getIdentifier() - currentTime.getIdentifier());
		}
	}
	
	/**
	 * Print the results of the simulation in the path.
	 * @param path the path where write the results
	 */
	protected abstract void exportDataFromSimulations (String path);
	
	/**
	 * Set the id.
	 * @param s the id as a string.
	 */
	public void setId (String s) {
		this.id = s;
	}

	
}
