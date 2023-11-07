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
package fr.univ_artois.lgi2a.similar2logo.examples.predation.exploration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.libs.engines.EngineMultithreadedDefaultdisambiguation;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.initializations.RandomWalkPredationSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.probes.SimplePopulationProbe;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;


/**
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class PredationExplorationForExplaination {
	
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
		int nbOfReplications = 1000;
		int nbOfExperiments = 100;
		
		PredationSimulationParameters parameters = new PredationSimulationParameters();	
		parameters.finalTime = new SimulationTimeStamp(2000);
		
		DecimalFormat df = new DecimalFormat("#0.00000000");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
				
		double predatorReproductionRateMinMax[] = {0, 0.05};
		double preyReproductionRateMinMax[] = {0, 0.05};
		double grassGrowthRateMinMax[] = {0, 0.1};
		double preyEnergyGainFromFoodMinMax[] = {0, 5};
		double predatorEnergyGainFromFoodMinMax[] = {0, 10};
		double predationProbabilityMinMax[] = {0, 0.5};
		
		
		for(int j = 1; j <= nbOfExperiments; j++) {
			
			PrintWriter writer = new PrintWriter(
				new FileOutputStream(new File("result.txt"), true)
			); 
			
			/*
			 * Generate random parameters
			 */
			parameters.predatorReproductionRate=PRNG.randomDouble(
				predatorReproductionRateMinMax[0], predatorReproductionRateMinMax[1]
			);
			parameters.preyReproductionRate=PRNG.randomDouble(
				preyReproductionRateMinMax[0], preyReproductionRateMinMax[1]
			);
			parameters.grassGrowthRate = PRNG.randomDouble(
				grassGrowthRateMinMax[0], grassGrowthRateMinMax[1]
			);
			parameters.preyEnergyGainFromFood = PRNG.randomDouble(
				preyEnergyGainFromFoodMinMax[0], preyEnergyGainFromFoodMinMax[1]
			);
			parameters.predatorEnergyGainFromFood = PRNG.randomDouble(
				predatorEnergyGainFromFoodMinMax[0], predatorEnergyGainFromFoodMinMax[1]
			);
			parameters.predationProbability = PRNG.randomDouble(
				predationProbabilityMinMax[0], predationProbabilityMinMax[1]
			);
			
			
			System.out.println("- Experiment "+j+"/"+nbOfExperiments);
		
			
			Map<Integer, Integer> result = new HashMap<>();
			result.put(0, 0);
			result.put(1, 0);
			result.put(2, 0);
			
			/*
			 * Run replications
			 */
			for(int i = 1; i<=nbOfReplications; i++) {
				System.out.println(" - Replication "+i+ "/"+nbOfReplications);
				
				// Create the simulation engine that will run simulations
				ISimulationEngine engine = new EngineMultithreadedDefaultdisambiguation( );
				SimplePopulationProbe populationProbe = new SimplePopulationProbe();
				engine.addProbe("population", populationProbe);
				AbstractLogoSimulationModel model = new RandomWalkPredationSimulationModel( parameters);
				engine.runNewSimulation(model);
				
				if(populationProbe.nbOfPreys == 0  && populationProbe.nbOfPredators == 0)
					result.put(0, result.get(0)+1);
				else if(populationProbe.nbOfPreys > 0  && populationProbe.nbOfPredators == 0)
					result.put(1, result.get(1)+1);
				else
					result.put(2, result.get(2)+1);
	
				
				
			}
			/*
			 * Write solutions to file
			 */
			double solution0 = ((double) result.get(0)) / nbOfReplications;
			double solution1 = ((double) result.get(1)) / nbOfReplications;
			double solution2 = ((double) result.get(2)) / nbOfReplications;
			writer.println(
				df.format(parameters.predatorReproductionRate)+"\t"+df.format(parameters.preyReproductionRate)+"\t"+df.format(parameters.grassGrowthRate)+"\t"+df.format(parameters.preyEnergyGainFromFood)+"\t"+df.format(parameters.predatorEnergyGainFromFood)+"\t"+df.format(parameters.predationProbability)+"\t"+
						df.format(solution0)+"\t"+df.format(solution1)+"\t"+df.format(solution2)
			);
			writer.close();
		
		}
		
	}
	


}

