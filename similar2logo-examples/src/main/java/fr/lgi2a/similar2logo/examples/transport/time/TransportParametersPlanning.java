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
package fr.lgi2a.similar2logo.examples.transport.time;

import java.io.BufferedReader;
import java.io.FileReader;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.transport.model.TransportSimulationParameters;

/**
 * Class for getting the parameters for the transport simulation for each hour
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportParametersPlanning {

	/**
	 * The planning of the paramters
	 */
	private TransportSimulationParameters[][][] parameters;
	
	/**
	 * The simulation clock
	 */
	private Clock clock;
	
	public TransportParametersPlanning (int startHour, int step, String parametersData, int n, int m) {
		this.clock = new Clock(startHour, step);
		this.parameters = new TransportSimulationParameters[24][n][m];
		try {
			FileReader fr = new FileReader(parametersData);
			BufferedReader br = new BufferedReader(fr);
			for (int i =0; i < 24; i++) {
				for (int j =0; j < n; j++) {
					for (int k =0; k < m; k++) {
						parameters[i][j][k] = readParamters(br.readLine());
					}
				}
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Transforms the data from the file in a transport simulation parameters
	 * @param data the string with all the values of the parameters
	 * @return a transport simulation parameters with the parameters from the file line
	 */
	private TransportSimulationParameters readParamters (String data) {
		TransportSimulationParameters tsp = new TransportSimulationParameters();
		String[] separateParamters = data.split(" ");
		tsp.nbrPersons = Integer.parseInt(separateParamters[0]);
		tsp.speedFrequencyPerson = Integer.parseInt(separateParamters[1]);
		tsp.nbrCars = Integer.parseInt(separateParamters[2]);
		tsp.carCapacity = Integer.parseInt(separateParamters[3]);
		tsp.speedFrenquecyCar = Integer.parseInt(separateParamters[4]);
		tsp.probaBeAtHome = Double.parseDouble(separateParamters[5]);
		tsp.probaLeaveHome = Double.parseDouble(separateParamters[6]);
		tsp.probaBecomeCar = Double.parseDouble(separateParamters[7]);
		tsp.probaBecomePerson = Double.parseDouble(separateParamters[8]);
		tsp.nbrTramways = Integer.parseInt(separateParamters[9]);
		tsp.tramwayCapacity = Integer.parseInt(separateParamters[10]);
		tsp.speedFrequencyTram = Integer.parseInt(separateParamters[11]);
		tsp.nbrTrains = Integer.parseInt(separateParamters[12]);
		tsp.trainCapacity = Integer.parseInt(separateParamters[13]);
		tsp.speedFrequenceTrain = Integer.parseInt(separateParamters[14]);
		tsp.probaTakeTransport = Double.parseDouble(separateParamters[15]);
		tsp.probaCreatePerson = Double.parseDouble(separateParamters[16]);
		tsp.probaCreateCar = Double.parseDouble(separateParamters[17]);
		tsp.probaCreateTram = Double.parseDouble(separateParamters[18]);
		tsp.probaCreateTrain = Double.parseDouble(separateParamters[19]);
		tsp.carReactionOnly = Boolean.getBoolean(separateParamters[20]);
		return tsp;
	}
	
	/**
	 * Gives the parameters for a determined simulation time stamp
	 * @param time the current simulation time stamp
	 * @param n the horizontal zone
	 * @param m the vertical zone
	 * @return the transport simulation parameters of the current time
	 */
	public TransportSimulationParameters getParameters (SimulationTimeStamp time, int n, int m) {
		int hour = clock.getHour(time);
		return parameters[hour][n][m];
	}
	
	
}
