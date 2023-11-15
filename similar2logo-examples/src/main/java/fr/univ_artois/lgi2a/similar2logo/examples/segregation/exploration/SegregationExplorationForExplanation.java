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
package fr.univ_artois.lgi2a.similar2logo.examples.segregation.exploration;

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
import fr.univ_artois.lgi2a.similar2logo.examples.segregation.SegregationSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.examples.segregation.model.SegregationSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;


/**
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class SegregationExplorationForExplanation {
	
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
		
		SegregationSimulationParameters parameters = new SegregationSimulationParameters();	
		parameters.finalTime = new SimulationTimeStamp(100);
		
		DecimalFormat df = new DecimalFormat("#0.00000000");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
				
		double similarityRateMinMax[] = {0, 1};
		double vacancyRateMinMax[] = {0.05, 0.5};
		double perceptionDistanceMinMax[] = {Math.sqrt(2), 4*Math.sqrt(2)};
		
		
		for(int j = 1; j <= nbOfExperiments; j++) {
			
			PrintWriter writer = new PrintWriter(
				new FileOutputStream(new File("segregation.txt"), true)
			); 
			
			/*
			 * Generate random parameters
			 */
			parameters.similarityRate=PRNG.randomDouble(
				similarityRateMinMax[0], similarityRateMinMax[1]
			);
			parameters.vacancyRate=PRNG.randomDouble(
				vacancyRateMinMax[0], vacancyRateMinMax[1]
			);
			parameters.perceptionDistance = PRNG.randomDouble(
				perceptionDistanceMinMax[0], perceptionDistanceMinMax[1]
			);
			
			System.out.println("- Experiment "+j+"/"+nbOfExperiments);
		
			
			double segregationRate[] = new double [nbOfReplications];
			double happinessRate[] = new double [nbOfReplications];
			
			
			/*
			 * Run replications
			 */
			for(int i = 0; i< nbOfReplications; i++) {
				System.out.println(" - Replication "+String.valueOf(i+1)+ "/"+nbOfReplications);
				
				// Create the simulation engine that will run simulations
				ISimulationEngine engine = new EngineMultithreadedDefaultdisambiguation( );
				SimpleSegregationProbe segregationProbe = new SimpleSegregationProbe(parameters);
				engine.addProbe("segregation", segregationProbe);
				AbstractLogoSimulationModel model = new SegregationSimulationModel( parameters);
				engine.runNewSimulation(model);
				
				segregationRate[i] = segregationProbe.segregationRate;
				happinessRate[i] = segregationProbe.happinessRate;
				
			}
			/*
			 * Write solutions to file
			 */
			double segregationResults[] = stats(segregationRate);
			double happinessResults[] = stats(happinessRate);
			writer.println(
				df.format(parameters.similarityRate)+"\t"+df.format(parameters.vacancyRate)+"\t"+df.format(parameters.perceptionDistance)+"\t"+
				df.format(segregationResults[0])+"\t"+df.format(segregationResults[1])+"\t"+
				df.format(happinessResults[0])+"\t"+df.format(happinessResults[1])
			);
			writer.close();
		
		}
		
	}
	
	public static double[] stats(double[] array) {

	    // get the sum of array
	    double sum = 0.0;
	    for (double i : array) {
	        sum += i;
	    }

	    // get the mean of array
	    int length = array.length;
	    double mean = sum / length;

	    // calculate the standard deviation
	    double standardDeviation = 0.0;
	    for (double num : array) {
	        standardDeviation += Math.pow(num - mean, 2);
	    }
	    double result[] = {mean, Math.sqrt(standardDeviation / length)};
	    return result;
	}


}

