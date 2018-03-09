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
package fr.lgi2a.similar2logo.examples.transport.model.agents.bus;

import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtPerceptionModel;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.WagonPLS;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Class of the buses PLS for the transport simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class BusPLS extends TurtlePLSInLogo {
	
	/**
	 * The frequency of the bus
	 */
	protected double frequency;
	
	/**
	 * The passengers of the bus
	 */
	protected List<ExtendedAgent> passengers;
	
	/**
	 * The current and max size of the bus
	 */
	protected int currentSize;
	
	/**
	 * The maximum capacity of the bus
	 */
	protected int maxCapacity;
	
	/**
	 * The wagons of the bus
	 */
	protected WagonPLS[] wagons;

	public BusPLS(
		IAgent4Engine owner,
		double initialX,
		double initialY,
		double initialSpeed,
		double initialAcceleration,
		double initialDirection,
		double frequency,
		int capacity,
		int maxSize
	) {
		super(owner, initialX, initialY, initialSpeed, initialAcceleration, initialDirection);
		this.frequency = frequency;
		this.passengers = new ArrayList<>();
		this.maxCapacity = capacity;
		this.wagons = new WagonPLS[maxSize-1];
		this.currentSize = 1;
	}
	
	/**
	 * Gives the frequency of the bus
	 * @return the frequency of the bus
	 */
	public double getFrequency() {
		return this.frequency;
	}
	
	/**
	 * Gives the current size of the bus
	 * @return the current size of the bus
	 */
	public int getCurrentSize () {
		return this.currentSize;
	}
	
	/**
	 * Gives the maximal capacity of the bus
	 * @return the maximal capacity of the bus
	 */
	public int getMaxCapacity () {
		return this.maxCapacity;
	}
	
	/**
	 * Indicates if the bus if full
	 * @return true if the bus is full, false else
	 */
	public boolean isFull () {
		return this.passengers.size() == this.maxCapacity;
	}
	
	/**
	 * Gives the number of passengers in the transport
	 * @return int the number of passengers
	 */
	public int getNbrPassengers () {
		return this.passengers.size();
	}
	
	/**
	 * Adds a new person in the transport.
	 * @param person to add
	 */
	public void addPassenger (ExtendedAgent person){
		this.passengers.add(person);
	}
	
	/**
	 * Removes a passenger from the transport
	 * @param person to remove
	 */
	public void removePassenger (ExtendedAgent person) {
		passengers.remove(person);
	}
	
	/**
	 * Gives the list of the passengers
	 * @return the list of the passengers
	 */
	public List<ExtendedAgent> getPassengers () {
		return this.passengers;
	}
	
	/**
	 * Adds a wagon to the transport
	 * @param wagon the wagon that follows the transport
	 */
	public void addWagon (WagonPLS wagon) {
		this.wagons[currentSize-1] = wagon;
		this.currentSize++;
	}
	
	/**
	 * Returns the nth wagon
	 * @param n the rank of the wagon
	 * @return the nth wagon
	 */
	public WagonPLS getWagon (int n) {
		return wagons[n];
	}
	
	public boolean reachMaxSize () {
		return this.currentSize == wagons.length + 1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone () {
		ExtendedAgent aa = (ExtendedAgent) this.getOwner();
		IAgent4Engine ia4e = BusFactory.generate(
			(AbstractAgtPerceptionModel) aa.getPerceptionModel(LogoSimulationLevelList.LOGO),
			(AbstractAgtDecisionModel) aa.getDecisionModel(LogoSimulationLevelList.LOGO),
			this.getCategoryOfAgent(),
			this.direction ,
			this.speed ,
			this.acceleration,
			this.location.getX(),
			this.location.getY(),
			this.frequency,
			this.maxCapacity,
			this.wagons.length
		);
		return new BusPLS(
			ia4e, 
			location.getX(), 
			location.getY(), 
			speed, 
			acceleration, 
			direction, 
			frequency,
			maxCapacity,
			wagons.length
		);
	}

}
