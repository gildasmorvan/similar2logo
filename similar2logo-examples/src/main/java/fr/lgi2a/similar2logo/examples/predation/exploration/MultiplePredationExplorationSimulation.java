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
package fr.lgi2a.similar2logo.examples.predation.exploration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.predation.exploration.data.SimulationDataPreyPredator;
import fr.lgi2a.similar2logo.examples.predation.exploration.probe.PreyPredatorPopulationForExplorationProbe;
import fr.lgi2a.similar2logo.examples.predation.exploration.treatment.PreyPredatorExplorationTreatment;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.MultipleExplorationSimulation;
import fr.lgi2a.similar2logo.lib.exploration.treatment.ITreatment;

/**
 * The class for the multiple exploration simulation with the predation simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class MultiplePredationExplorationSimulation extends MultipleExplorationSimulation {
	
	/**
	 * Constructor of the Multiple Predation Exploration Simulation
	 * @param nbrSimulations the number of simulation to do
	 * @param the logo simulations parameters array.
	 * @param end the moment when the simulation will finish
	 * @param pauses the pauses that the simulation will do
	 */
	public MultiplePredationExplorationSimulation(int nbrSimulations, LogoSimulationParameters[] parameters,
			SimulationTimeStamp end, List<SimulationTimeStamp> pauses, ITreatment treatment) {
		super(parameters, nbrSimulations, end, pauses, treatment);
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addNewSimulation(LogoSimulationParameters lsp) {
		this.simulations.add(new PredationExplorationSimulationModel((PredationSimulationParameters) lsp, this.currentTime,
				new SimulationDataPreyPredator(currentTime)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void exportDataFromSimulations(String path) {
		try {
			FileWriter fw = new FileWriter (path);
			BufferedWriter bw = new BufferedWriter (fw);
			for (int i=0; i < simulations.size(); i++) {
				PreyPredatorPopulationForExplorationProbe pppep = 
						(PreyPredatorPopulationForExplorationProbe) simulations.get(i).getEngine().getProbe("3PE Probe");
				SimulationDataPreyPredator sdpp = pppep.getData();
				bw.write(sdpp.getNbrOfPreys()+" "+sdpp.getNbOfPredator()+" "+sdpp.getNbOfGrass()+"\n");
			}
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args) {
		int k = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		int r = Integer.parseInt(args[2]);
		List<SimulationTimeStamp> p = new ArrayList<>();
		for (int i = 1 ; i <= 20; i++) p.add(new SimulationTimeStamp(i*50));
		PredationSimulationParameters psp = new PredationSimulationParameters();
		psp.predatorReproductionRate=0.044;
		LogoSimulationParameters[] lsp = {psp};
		for (int i = 1 ; i <= r; i++) {
			MultiplePredationExplorationSimulation mpes = new MultiplePredationExplorationSimulation(k*n, lsp, new SimulationTimeStamp(1001)
					, p, new PreyPredatorExplorationTreatment(k,n));
			mpes.setId(k+"_"+n+"_"+i);
			mpes.runSimulations();
		}

	}

}
