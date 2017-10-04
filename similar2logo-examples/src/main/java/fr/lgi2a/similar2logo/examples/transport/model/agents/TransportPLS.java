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
package fr.lgi2a.similar2logo.examples.transport.model.agents;

import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtPerceptionModel;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Public Local State of the transport in the "transport" simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportPLS extends TurtlePLSInLogo implements Cloneable {
	
	/**
	 * The number of people that can be in the transport
	 */
	protected int maxCapacity;
	
	/**
	 * The frequency that the transport goes head
	 */
	protected double speedFrequence;
	
	/**
	 * The size of the transport.
	 */
	protected int maxSize;
	
	/**
	 * The current size of the transport
	 */
	protected int currentSize;
	
	/**
	 * The transport nextWagon
	 */
	protected WagonPLS nextWagon;
	
	protected List<PersonPLS> passengers;

	/**
	 * Constructor of the Transport PLS
	 * @param owner The agent that gets the PLS
	 * @param initialX initial abscissa of the agent
	 * @param initialY initial ordinate of the agent
	 * @param initialSpeed initial speed of the agent
	 * @param initialAcceleration initial acceleration of the agent
	 * @param initialDirection initial direction of the agent
	 * @param maxCapacity max capacity of the transport
	 * @param size max size of the transport
	 * @param speedFrequencyTram max speed of the transport
	 */
	public TransportPLS(IAgent4Engine owner, double initialX, double initialY, double initialSpeed,
			double initialAcceleration, double initialDirection, int maxCapacity, /*int size,*/ double speedFrequencyTram) {
		super(owner, initialX, initialY, initialSpeed, initialAcceleration, initialDirection);
		this.maxCapacity = maxCapacity;
		this.speedFrequence = speedFrequencyTram;
		//this.size = size;
		this.currentSize = 1;
		this.maxSize = 1;
		this.passengers = new ArrayList<>();
	}
	
	/**
	 * Gives the max capacity of the transport
	 * @return int the max capacity of the transport
	 */
	public int getMaxCapacity () {
		return this.maxCapacity;
	}
	
	/**
	 * Gives the max speed of the transport
	 * @return int the max speed of the transport
	 */
	public double getMaxSpeed () {
		return this.speedFrequence;
	}
	
	/**
	 * Indicates if the transport is full
	 * @return true if the transport is full, false else
	 */
	public boolean isFull () {
		return (this.passengers.size() == maxCapacity);
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
	public void addPassenger (PersonPLS person){
		this.passengers.add(person);
	}
	
	/**
	 * Removes a passenger from the transport
	 * @param person to remove
	 */
	public void removePassenger (PersonPLS person) {
		passengers.remove(person);
	}
	
	/**
	 * Gives the list of the passengers
	 * @return the list of the passengers
	 */
	public List<PersonPLS> getPassengers () {
		return this.passengers;
	}
	
	/**
	 * Gives the max size of the transport
	 * @return the size of the transport
	 */
	public int maxSize () {
		return this.maxSize;
	}
	
	/**
	 * Indicates if the transport has reached its maximum size
	 * @return true if the transport has reached its maximum size, false else
	 */
	public boolean reachMaxSize () {
		return this.maxSize == this.currentSize;
	}
	
	/**
	 * Indicates that the transport has one more wagon
	 */
	public void hasOneMoreWagon () {
		this.currentSize++;
	}
	
	/**
	 * Set the next wagon of the transport
	 * @param wagon the wagon that follows the transport
	 */
	public void setNextWagon (WagonPLS wagon) {
		this.nextWagon = wagon;
	}
	
	/**
	 * Gives the current size of the transport
	 * @return the size of the transport
	 */
	public int getCurrentSize () {
		return this.currentSize;
	}
	
	/**
	 * Returns the nth wagon
	 * @param n the rank of the wagon
	 * @return the nth wagon
	 */
	public WagonPLS getWagon (int n) {
		if (n == 1) {
			return this.nextWagon;
		} else
		return this.nextWagon.nextWagon(n-1);
	}
	
	public Object clone () {
		ExtendedAgent aa = (ExtendedAgent) this.getOwner();
		IAgent4Engine ia4e = TransportFactory.generate(
				(AbstractAgtPerceptionModel) aa.getPerceptionModel(LogoSimulationLevelList.LOGO),
				(AbstractAgtDecisionModel) aa.getDecisionModel(LogoSimulationLevelList.LOGO),
				this.getCategoryOfAgent(),
				this.direction ,
				this.speed ,
				this.acceleration,
				this.location.getX(),
				this.location.getY(), 
				this.maxCapacity, 
				this.speedFrequence
			);
		return new TransportPLS(
				ia4e, 
				location.getX(), 
				location.getY(), 
				speed, 
				acceleration, 
				direction, 
				maxCapacity, 
				speedFrequence);
	}

}
