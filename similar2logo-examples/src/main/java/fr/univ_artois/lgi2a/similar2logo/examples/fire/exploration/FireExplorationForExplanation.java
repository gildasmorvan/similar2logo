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
package fr.univ_artois.lgi2a.similar2logo.examples.fire.exploration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.libs.engines.EngineMultithreadedDefaultdisambiguation;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.FireForestSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.model.FireForestParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.probes.SimpleFireProbe;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.math.BasicStats;


/**
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class FireExplorationForExplanation {
	
	/**
	 * The main method of the exploration of the predation model.
	 * @param args The command line arguments.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		
		//writer.println("#predatorRepRate\tpreyRepRate\tgrassGrowthRate\tpreyEnergyGain\tpredatorEnergyGain\tpredationProbability\tSolution0\tSolution1\tSolution2");
		
		/*
		 * Define static parameters
		 */
		int nbOfReplications = 10;
		int nbOfExperiments = 40;
		
		FireForestParameters parameters = new FireForestParameters();	
		parameters.finalTime = new SimulationTimeStamp(2000);
		
		DecimalFormat df = new DecimalFormat("#0.00000000");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
				
		double initialTreeDensityMinMax[] = {0, 100};
		double firePropagationProbaMinMax[] = {0, 1};
		double combustionSpeedMinMax[] = {0.1, 1};
		
		
		for(int j = 1; j <= nbOfExperiments; j++) {
			
			PrintWriter writer = new PrintWriter(
				new FileOutputStream(new File("fire.txt"), true)
			); 
			
			/*
			 * Generate random parameters
			 */
			parameters.initialTreeDensity=PRNG.randomDouble(
				initialTreeDensityMinMax[0], initialTreeDensityMinMax[1]
			);
			parameters.firePropagationProba=PRNG.randomDouble(
				firePropagationProbaMinMax[0], firePropagationProbaMinMax[1]
			);
			parameters.combustionSpeed = PRNG.randomDouble(
				combustionSpeedMinMax[0], combustionSpeedMinMax[1]
			);
			
			
			System.out.println("- Experiment "+j+"/"+nbOfExperiments);
		
			double combustionRate[] = new double [nbOfReplications];
			double finalTimeStep[] = new double [nbOfReplications];
			
			
			/*
			 * Run replications
			 */
			for(int i = 0; i<nbOfReplications; i++) {
				System.out.println(" - Replication "+String.valueOf(i+1)+ "/"+nbOfReplications);
				
				
				// Create the simulation engine that will run simulations
				ISimulationEngine engine = new EngineMultithreadedDefaultdisambiguation( );
				SimpleFireProbe fireProbe = new SimpleFireProbe();
				engine.addProbe("fire", fireProbe);
				AbstractLogoSimulationModel model = new FireForestSimulationModel( parameters);
				engine.runNewSimulation(model);
				
				combustionRate[i] = fireProbe.getCombustionRate();
				finalTimeStep[i] = fireProbe.getFinalTimeStep();
				
				
			}
			/*
			 * Write solutions to file
			 */
			double combustionRateResults[] = BasicStats.meanAndSD(combustionRate);
			double finalTimeStepResults[] = BasicStats.meanAndSD(finalTimeStep);
			writer.println(
				df.format(parameters.initialTreeDensity)+"\t"+df.format(parameters.firePropagationProba)+"\t"+df.format(parameters.combustionSpeed)+"\t"+
				df.format(combustionRateResults[0])+"\t"+df.format(combustionRateResults[1])+"\t"+
				df.format(finalTimeStepResults[0])+"\t"+df.format(finalTimeStepResults[1])
			);
			writer.close();
		
		}
		
	}
	


}

