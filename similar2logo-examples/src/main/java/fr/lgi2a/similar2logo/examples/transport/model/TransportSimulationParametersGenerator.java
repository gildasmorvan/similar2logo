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
package fr.lgi2a.similar2logo.examples.transport.model;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Class for generate data for the parameters planning
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class TransportSimulationParametersGenerator {
	
	/**
	 * Factors to apply for each parameter for each hour
	 */
	private static double[][] factors = {
			{0.05, 1, 0.05, 1, 1, 2, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0.05, 0.05, 0, 0}, //midnight
			{0.0025, 1, 0.0025, 1, 1, 2, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0.0025, 0.0025, 0, 0}, //1h
			{0, 1, 0, 1, 1, 2, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0}, //2h
			{0, 1, 0, 1, 1, 2, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0}, //3h
			{0, 1, 0, 1, 1, 2, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0}, //4h
			{0.1, 1, 0.1, 1, 1, 1, 0.3, 1, 1, 0.5, 1, 1, 0.5, 1, 1, 0.5, 0.1, 0.1, 0.5, 0.5}, //5h
			{0.5, 1, 0.5, 1, 1, 0.2, 1.2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.5, 0.5, 1, 1}, //6h
			{1.2, 1, 1.2, 1, 1, 0.8, 2.5, 1, 1, 1.2, 1, 1, 2, 1, 1, 1, 1.2, 1.2, 1.2, 2}, //7h
			{1, 1, 1, 1, 1, 2, 2, 1, 1, 1.2, 1, 1, 1.5, 1, 1, 1, 1, 1,  1.2, 1.5}, //8h
			{1, 1, 1, 1, 1, 1.5, 1, 1, 1, 1.2, 1, 1, 1, 1, 1, 1, 1, 1, 1.2, 1}, //9h
			{0.8, 1, 0.8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.8, 1, 1, 1, 0.8, 0.8, 1, 0.8}, // 10h
			{0.8, 1, 0.8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.8, 1, 1, 1, 0.8, 0.8, 1, 0.8}, //11h
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1.1, 1, 1, 1, 1, 1, 1, 1, 1, 1.1, 1}, //noon
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1.1, 1, 1, 1, 1, 1, 1, 1, 1, 1.1, 1}, //13h
			{0.8, 1, 0.8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.8, 1, 1, 1, 0.8, 0.8, 1, 0.8}, //14h
			{0.8, 1, 0.8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.8, 0.8, 1, 1}, //15h
			{1, 1, 1, 1, 1, 1, 1.5, 1, 1, 1.1, 1, 1, 1.2, 1, 1, 1, 1, 1, 1.1, 1.2}, //16h
			{1.3, 1, 1.3, 1, 1, 0.8, 2, 1, 1, 1.2, 1, 1, 2, 1, 1, 1, 1.3, 1.3, 1.2, 2}, //17h
			{1.2, 1, 1.2, 1, 1, 3, 1.8, 1, 1, 1.2, 1, 1, 1.5, 1, 1, 1, 1.2, 1.2, 1.2, 1.5}, //18h
			{0.9, 1, 0.9, 1, 1, 1.2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.9, 0.9, 1, 1}, //19h
			{0.7, 1, 0.7, 1, 1, 1.5, 0.5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.7, 0.7, 1, 1}, //20h
			{0.5, 1, 0.5, 1, 1, 2, 0.4, 1, 1, 0.5, 1, 1, 0.8, 1, 1, 0.5, 0.5, 0.5, 0.5, 0.8}, //21h
			{0.3, 1, 0.3, 1, 1, 2, 0.3, 1, 1, 0, 1, 1, 0.5, 1, 1, 0.2, 0.3, 0.3, 0, 0.5}, //22h
			{0.1, 1, 0.1, 1, 1, 2, 0.2, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0.1, 0.1, 0, 0} //23h
	}; 
	
	
	/**
	 * Prints the default parameters in a file
	 * @param path the path toward the file to fill
	 * @param line the number of line to print
	 */
	public static void printDefaultParameters (String path, int line) {
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			TransportSimulationParameters tsp = new TransportSimulationParameters();
			for (int i =0; i < line; i++) {
				String s = "";
				s += tsp.nbrPersons+" "+tsp.speedFrequencyPerson+" "+tsp.nbrCars+" "+tsp.carCapacity+" "+tsp.speedFrenquecyCar+" "+
						tsp.probaBeAtHome+" "+tsp.probaLeaveHome+" "+tsp.probaBecomeCar+" "+tsp.probaBecomePerson+" "+tsp.nbrTramways+" "+
						tsp.tramwayCapacity+" "+tsp.speedFrequencyTram+" "+tsp.nbrTrains+" "+tsp.trainCapacity+" "+tsp.speedFrequenceTrain+" "+
						tsp.probaTakeTransport+" "+tsp.probaCreatePerson+" "+tsp.probaCreateCar+" "+tsp.probaCreateTram+" "+
						tsp.probaCreateTrain+" "+tsp.carReactionOnly+"\n";
				bw.write(s);
			}
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gives the parameters for a specific hour
	 * @param hour the hour
	 * @param carsOnly if the simulation reaction model takes only the cars or not
	 * @return a transport simulation parameters for the hour
	 */
	public static TransportSimulationParameters parametersOfTheHour (int hour, boolean carsOnly) {
		int rk = hour %24;
		TransportSimulationParameters tsp = new TransportSimulationParameters();
		tsp.nbrPersons *= factors[rk][0];
		tsp.speedFrequencyPerson *= factors[rk][1];
		tsp.nbrCars *= factors[rk][2];
		tsp.carCapacity *= factors[rk][3];
		tsp.speedFrenquecyCar *= factors[rk][4];
		tsp.probaBeAtHome *= factors[rk][5];
		tsp.probaLeaveHome *= factors[rk][6];
		tsp.probaBecomeCar *= factors[rk][7];
		tsp.probaBecomePerson *= factors[rk][8];
		tsp.nbrTramways *= factors[rk][9];
		tsp.tramwayCapacity *= factors[rk][10];
		tsp.speedFrequencyTram *= factors[rk][11];
		tsp.nbrTrains *= factors[rk][12];
		tsp.trainCapacity *= factors[rk][13];
		tsp.speedFrequenceTrain *= factors[rk][14];
		tsp.probaTakeTransport *= factors[rk][15];
		tsp.probaCreatePerson *= factors[rk][16];
		tsp.probaCreateCar *= factors[rk][17];
		tsp.probaCreateTram *= factors[rk][18];
		tsp.probaCreateTrain *= factors[rk][19];
		tsp.carReactionOnly = carsOnly;
		return tsp;
	}
}
