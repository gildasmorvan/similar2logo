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

import java.awt.geom.Point2D;

import org.json.JSONObject;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;

/**
 * Class for getting the parameters for the transport simulation for each hour
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportParametersPlanning {

	/**
	 * The planning of the parameters
	 */
	private TransportSimulationParameters[][][] parameters;
	
	/**
	 * The simulation clock
	 */
	private Clock clock;
	
	/**
	 * The number of sections horizontal and vertical
	 */
	private int horizontal, vertical;
	
	public TransportParametersPlanning (int startHour, int step, JSONObject parameters, int n, int m) {
		this.clock = new Clock(startHour, step);
		this.parameters = new TransportSimulationParameters[24][n][m];
		this.horizontal = n;
		this.vertical = m;
		try {
		boolean zone = parameters.getBoolean("zone");
			for (int i=0; i < 24; i++) {
				for (int j=0; j < n; j++) {
					for (int k=0; k < m; k++) {
						String h = String.valueOf(i);
						TransportSimulationParameters tsp = new TransportSimulationParameters();
						tsp.speedFrequencyPerson = parameters.getJSONObject("staticParameters").getDouble("speedFrequencyPerson");
						tsp.speedFrequencyBike = parameters.getJSONObject("staticParameters").getDouble("speedFrequencyBike");
						tsp.carCapacity = parameters.getJSONObject("staticParameters").getInt("carCapacity");
						tsp.speedFrequencyCarAndBus = parameters.getJSONObject("staticParameters").getDouble("speedFrequencyCarAndBus");
						tsp.carSize = parameters.getJSONObject("staticParameters").getInt("carSize");
						tsp.busSize = parameters.getJSONObject("staticParameters").getInt("busSize");
						tsp.busCapacity = parameters.getJSONObject("staticParameters").getInt("busCapacity");
						tsp.tramwayCapacity = parameters.getJSONObject("staticParameters").getInt("tramwayCapacity");
						tsp.speedFrequencyTram = parameters.getJSONObject("staticParameters").getDouble("speedFrequencyTram");
						tsp.tramwaySize = parameters.getJSONObject("staticParameters").getInt("tramwaySize");
						tsp.trainCapacity = parameters.getJSONObject("staticParameters").getInt("trainCapacity");
						tsp.speedFrequenceTrain = parameters.getJSONObject("staticParameters").getDouble("speedFrequencyTrain");
						tsp.trainSize = parameters.getJSONObject("staticParameters").getInt("trainSize");
						tsp.recalculationPath = parameters.getJSONObject("staticParameters").getLong("recalculationPath");
						tsp.probaStayInTrain = parameters.getJSONObject("staticParameters").getDouble("probaStayInTrain");
						tsp.probaStayInTram = parameters.getJSONObject("staticParameters").getDouble("probaStayInTram");
						tsp.probaToBeACar = parameters.getJSONObject("staticParameters").getDouble("probaToBeACar");
						tsp.probaToBeABike = parameters.getJSONObject("staticParameters").getDouble("probaToBeABike");
						if (zone) {
							String z = String.valueOf(j*n+k);
							tsp.probaLeaveHome = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaLeaveHome");
							tsp.probaCreatePerson = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaCreatePerson");
							tsp.probaCreateBike = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaCreateBike");
							tsp.probaCreateCar = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaCreateCar");
							tsp.creationFrequencyBus = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("creationFrequencyBus");
							tsp.creationFrequencyTram = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("creationFrequencyTram");
							tsp.creationFrequencyTrain = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("creationFrequencyTrain");
							tsp.probaGoToSchool = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaGoToSchool");
							tsp.probaGoToShop = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaGoToShop");
							tsp.probaGoToRestaurant = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaGoToRestaurant");
							tsp.probaGoToDoctor = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaGoToDoctor");
							tsp.probaGoToBank = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaGoToBank");
							tsp.probaLeaveTownByTrain = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaLeaveTownByTrain");
							tsp.probaLeaveTownByTram = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaLeaveTownByTram");
							tsp.probaLeaveTownByBus = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaLeaveTownByBus");
							tsp.probaLeaveTownByRoad = parameters.getJSONObject("variableParameters").getJSONObject(z)
									.getJSONObject(h).getDouble("probaLeaveTownByRoad");
						} else {
							tsp.probaLeaveHome = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaLeaveHome");
							tsp.probaCreatePerson = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaCreatePerson");
							tsp.probaCreateBike = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaCreateBike");
							tsp.probaCreateCar = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaCreateCar");
							tsp.creationFrequencyBus = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("creationFrequencyBus");
							tsp.creationFrequencyTram = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("creationFrequencyTram");
							tsp.creationFrequencyTrain = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("creationFrequencyTrain");
							tsp.probaGoToSchool = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaGoToSchool");
							tsp.probaGoToShop = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaGoToShop");
							tsp.probaGoToRestaurant = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaGoToRestaurant");
							tsp.probaGoToDoctor = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaGoToDoctor");
							tsp.probaGoToBank = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaGoToBank");
							tsp.probaLeaveTownByTrain = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaLeaveTownByTrain");
							tsp.probaLeaveTownByTram = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaLeaveTownByTram");
							tsp.probaLeaveTownByBus = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaLeaveTownByBus");
							tsp.probaLeaveTownByRoad = parameters.getJSONObject("variableParameters")
									.getJSONObject(h).getDouble("probaLeaveTownByRoad");
						}
						this.parameters[i][j][k] = tsp; 
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gives the parameters for a determined simulation time stamp
	 * @param time the current simulation time stamp
	 * @param position the position of the turtle
	 * @param width the width of the world
	 * @param height the height of the world
	 * @return the transport simulation parameters of the current time
	 */
	public TransportSimulationParameters getParameters (SimulationTimeStamp time, Point2D position, int width, int height) {
		int hour = clock.getHour(time);
		Double n = position.getX()*horizontal/width;
		Double m = position.getY()*vertical/height;
		return parameters[hour][n.intValue()][m.intValue()];
	}
	
	/**
	 * Gives the number of step by second
	 * @return gives the number of step by second
	 */
	public int getStep () {
		return clock.getStep();
	}
	
	/**
	 * Gives the clock
	 * @return the clock of the planning
	 */
	public Clock getClock () {
		return this.getClock();
	}
	
	
}
