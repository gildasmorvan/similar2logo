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

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.Parameter;

/**
 * Transport simulation parameters
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportSimulationParameters extends LogoSimulationParameters {
	
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
	@Parameter(
			name = "Person speed frequency",
			description = "The frequency the persons go head"
	)
	public int speedFrequencyPerson;
	
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
	@Parameter(
			name = "Car capacity",
			description= "The capacity of the cars"
	)
	public int carCapacity;
	
	/**
	 * The frequency speed of the cars
	 */
	@Parameter(
			name = "Car speed frequency",
			description = "The frequency the cars go head"
	)
	public int speedFrenquecyCar;
	
	/**
	 * The probability for a car to be at home
	 */
	@Parameter(
			name = "Probability to be at home",
			description = "The probability for a car to be at home"
	)
	public double probaBeAtHome;
	
	/**
	 * The probability for a car to leave home
	 */
	@Parameter(
			name = "Probability to leave home",
			description = "The probability for a car to leave home"
	)
	public double probaLeaveHome;
	
	/**
	 * The probability for a person to take his car.
	 */
	@Parameter(
			name = "Probability to take car",
			description = "The probability for a person to take his car"
	)
	public double probaBecomeCar;
	
	/**
	 * The probability for a person to leave his car.
	 */
	@Parameter(
			name = "Probability to leave car",
			description = "The probability for a person to leave his car"
	)
	public double probaBecomePerson;
	
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
	@Parameter(
			name = "Tramway capacity",
			description = "Capacity of each tramway"
	)
	public int tramwayCapacity;
	
	/**
	 * The frequency the tram go head
	 */
	@Parameter(
			name = "Tram speed frequency",
			description = "The frequency the trams go head"
	)
	public int speedFrequencyTram;
	
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
	@Parameter(
			name = "Train capacity",
			description = "Capacity of each train"
	)
	public int trainCapacity;
	
	/**
	 * The speed frequency of the trains
	 */
	@Parameter(
			name = "Train speed frequency",
			description = "The frequency the trains go head")
	public int speedFrequenceTrain;
	
	/**
	 * The probability for a car to enter in a station.
	 */
	@Parameter(
			name = "Probability take transport",
			description = "Probability to take a transport when a car is at a station"
	)
	public double probaTakeTransport;
	
	/**
	 * The probability to create a person on the limits
	 */
	@Parameter(
			name = "Probability create person",
			description = "Probability to create a person on the limits"
	)
	public double probaCreatePerson;
	
	/**
	 * The probability to create a car on the limits.
	 */
	@Parameter(
			name = "Probability create car",
			description = "Probability to create a car on the limits"
	)
	public double probaCreateCar;
	
	/**
	 * The probability to create a tram on the limits.
	 */
	@Parameter(
			name = "Probability create tram",
			description = "Probability to create a tram on the limits"
	)
	public double probaCreateTram;
	
	/**
	 * The probability to create a train on the limits.
	 */
	@Parameter(
			name = "Probability create train",
			description = "Probability to create a train on the limits"
	)
	public double probaCreateTrain;
	
	/**
	 * Indicates if only the car are concerned by the reaction model
	 */
	@Parameter(
			name = "Car reaction only",
			description = "Indicates if only the cars are concerned by the reaction model")
	public boolean carReactionOnly;
	
	/**
	 * Constructor of the transport simulation parameters.
	 */
	public TransportSimulationParameters () {
		super();
		this.nbrPersons = 2500;
		this.speedFrequencyPerson = 32;
		this.nbrCars = 667;
		this.carCapacity = 5;
		this.speedFrenquecyCar = 3;
		this.probaBeAtHome = 0.007;
		this.probaLeaveHome = 0.0001;
		this.probaBecomeCar = 0.0025;
		this.probaBecomePerson = 0.0025;
		this.nbrTramways = 6;
		this.tramwayCapacity = 240;
		this.speedFrequencyTram = 5;
		this.nbrTrains = 3;
		this.trainCapacity = 500;
		this.speedFrequenceTrain = 1;
		this.probaTakeTransport = 0.25;
		this.probaCreatePerson = 0.001;
		this.probaCreateCar = 0.002;
		this.probaCreateTram = 0.0018;
		this.probaCreateTrain = 0.001;
		this.carReactionOnly = false;
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
