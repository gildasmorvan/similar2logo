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
package fr.lgi2a.similar2logo.examples.transport.parameters;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.Parameter;

/**
 * Transport simulation parameters
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportSimulationParameters extends LogoSimulationParameters {
	
	//Parameters for the simulation working
	
	/**
	 * The number of persons in the simulation.
	 */
	@Parameter(
			name = "Number of persons",
			description = "Number of persons in the simulation"
	)
	public int nbrPersons;
	
	/**
	 * The speed frequency of the person.
	 */
	public double speedFrequencyPerson;
	
	/**
	 * The number of bikes in the simulation.
	 */
	@Parameter(
			name = "Number of bikes",
			description = "Number of bikes in the simulation"
	)
	public int nbrBikes;
	
	/**
	 * The speed frequency of the bikes
	 */
	public double speedFrequencyBike;
	
	/**
	 * The number of cars in the simulation.
	 */
	@Parameter(
			name = "Number of cars",
			description = "Number of cars in the simulation"
	) 
	public int nbrCars;
	
	/**
	 * The capacity of the cars
	 */
	public int carCapacity;
	
	/**
	 * The frequency speed of the cars and buses
	 */
	public double speedFrequencyCarAndBus;
	
	/**
	 * The size of the car
	 */
	public int carSize;
	
	/**
	 * The probability for a car to leave home
	 */
	public double probaLeaveHome;
	
	/**
	 * The number of buses in the simulation.
	 */
	@Parameter(
			name = "Number of buses",
			description = "Number of buses in the simulation"
	)
	public int nbrBuses;
	
	/**
	 * The size of the buses
	 */
	public int busSize;
	
	/**
	 * The capacity of the buses
	 */
	public int busCapacity;
	
	/**
	 * The number of tramways in the simulation.
	 */
	@Parameter(
			name = "Number of tramways",
			description = "Number of tramways in the simulation"
	)
	public int nbrTramways;
	
	/**
	 * The number of passenger that can be in a tramway
	 */
	public int tramwayCapacity;
	
	/**
	 * The frequency the tram go head
	 */
	public double speedFrequencyTram;
	
	/**
	 * The size of the tramways
	 */
	public int tramwaySize;
	
	/**
	 * The number of trains in the simulation.
	 */
	@Parameter(
			name = "Number of trains",
			description = "Number of trains in the simulation"
	)
	public int nbrTrains;
	
	/**
	 * The capacity of the trains
	 */
	public int trainCapacity;
	
	/**
	 * The speed frequency of the trains
	 */
	public double speedFrequenceTrain;
	
	/**
	 * The size of the trains
	 */
	public int trainSize;
	
	/**
	 * The probability to create a person on the limits
	 */
	public double probaCreatePerson;
	
	/**
	 * The probability to create a bike on the limits
	 */
	public double probaCreateBike;
	
	/**
	 * The probability to create a car on the limits.
	 */
	public double probaCreateCar;
	
	/**
	 * The frequency the buses are created
	 */
	public double creationFrequencyBus;
	
	/**
	 * The frequency the trams are created
	 */
	public double creationFrequencyTram;
	
	/**
	 * The frequency the trains are created
	 */
	public double creationFrequencyTrain;
	
	//Parameters for the choice of the destination of the cars and persons
	
	/**
	 * Indicates the probability to go to school
	 */
	public double probaGoToSchool;
	
	/**
	 * Indicates the probability to go to shop
	 */
	public double probaGoToShop;
	
	/**
	 * Indicates the probability to go to the restaurant
	 */
	public double probaGoToRestaurant;
	
	/**
	 * Indicates the probability to go to the doctor
	 */
	public double probaGoToDoctor;
	
	/**
	 * Indicates the probability to go in a bank
	 */
	public double probaGoToBank;
	
	/**
	 * Indicates the probability to leave the town by train
	 */
	public double probaLeaveTownByTrain;
	
	/**
	 * Indicates the probability to leave the town by tram
	 */
	public double probaLeaveTownByTram;
	
	/**
	 * Indicates the probability to leave the town by bus
	 */
	public double probaLeaveTownByBus;
	
	/**
	 * Indicates the probability to leave the town by the road
	 */
	public double probaLeaveTownByRoad;
	
	/**
	 * Indicates the time before the path of the cars and the persons is recalculated 
	 */
	public long recalculationPath;
	
	/**
	 * The probability for someone to stay in a train
	 */
	public double probaStayInTrain;
	
	/**
	 * The probability for someone to stay in a tramway
	 */
	public double probaStayInTram;
	
	/**
	 * The probability to be a car when exit from a house or special place
	 */
	public double probaToBeACar;
	
	/**
	 * The probability to be a bike when exit from a house or special place
	 */
	public double probaToBeABike;
	
	/**
	 * The probability to be a car when exit from a station
	 */
	public double probaToBeACarOutOfTrain;
	
	/**.
	 * The probability to be a bike when exit from a station
	 */
	public double probaToBeABikeOutOfTrain;
	
	/**
	 * The probability to be a bike when exit from a tram station
	 */
	public double probaToBeABikeOutOfTram;

	/**
	 * Constructor of the transport simulation parameters.
	 */
	public TransportSimulationParameters () {
		super();
		this.nbrPersons = 500;
		this.nbrBikes = 333;
		this.nbrCars = 667;
		this.nbrBuses = 5;
		this.nbrTramways = 3;
		this.nbrTrains = 1;
		this.gridHeight = 1500;
		this.gridWidth = 1500;
		this.initialTime = new SimulationTimeStamp( 0 );
		this.finalTime = new SimulationTimeStamp( 300000 );
		this.xTorus = false;
		this.yTorus = false;
	}
	
	/**
	 * Set the size of the simulation
	 * @param height the height to set
	 * @param width the width to set
	 */
	public void setSize (int height, int width) {
		this.gridHeight = height;
		this.gridWidth = width;
	}

}
