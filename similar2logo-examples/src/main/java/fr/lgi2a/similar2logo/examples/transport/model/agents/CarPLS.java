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
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
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

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtPerceptionModel;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * Car public local state for the "transport" simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class CarPLS extends TurtlePLSInLogo implements Cloneable {
	
	/**
	 * The frequency that the car goes head
	 */
	protected double speedFrequence;
	
	/**
	 * The nbr of passengers in the car
	 */
	protected int nbrPassenger;
	
	/**
	 * The nbr of passager that can be in the car
	 */
	protected int maxCapacity;
	
	/**
	 * The current size of the car
	 */
	protected int currentSize;
	
	/**
	 * The max size of the car
	 */
	protected int maxSize;
	
	/**
	 * The first wagon of the car
	 */
	protected WagonPLS next;

	/**
	 * Constructor of the car PLS
	 * @param owner the agents that gets the PLS
	 * @param initialX initial abscissa of the car
	 * @param initialY initial ordinate of the car
	 * @param initialSpeed initial speed of the car
	 * @param initialAcceleration initial acceleration of the car
	 * @param initialDirection initial direction of the car
	 * @param speed max speed of the car
	 * @param maxCapacity the number of passengers maximum in the car
	 */
	public CarPLS(IAgent4Engine owner, double initialX, double initialY, double initialSpeed,
			double initialAcceleration, double initialDirection, double speed, int maxCapacity) {
		super(owner, initialX, initialY, initialSpeed, initialAcceleration, initialDirection);
		this.speedFrequence = speed;
		this.maxCapacity = maxCapacity;
		int i = 1;
		boolean done = false;
		while (!done && i != maxCapacity) {
			if (RandomValueFactory.getStrategy().randomDouble() <= 0.5) {
				done = true;
				this.nbrPassenger = i;
			} else {
				i++;
			}
		}
		if (!done) {
			this.nbrPassenger = maxCapacity;
		}
		currentSize = 1;
		maxSize = 1;
	}

	/**
	 * Gives the frequency of the car
	 * @return int the max speed of the car.
	 */
	public double getFrequence () {
		return this.speedFrequence;
	}
	
	/**
	 * Set the frequency of the car
	 * @param d the new frequency of the car
	 */
	public void setFrequence (double d) {
		this.speedFrequence = d;
	}
	
	/**
	 * Indicates if the car is full
	 * @return true if the car is full, false else
	 */
	public boolean isFull () {
		return this.nbrPassenger == this.maxCapacity;
	}
	
	/**
	 * Gives the number of passenger in the car
	 * @return the number of passenger in the car
	 */
	public int getNbrPassenger() {
		return this.nbrPassenger;
	}
	
	/**
	 * Adds a passenger in the exception 
	 */
	public void addPassenger (){ 
		this.nbrPassenger++;
	}
	
	/**
	 * Removes a passenger from the car
	 */
	public void removePassenger () {
		this.nbrPassenger--;
	}
	
	/**
	 * Gives the current size of the car
	 * @return the current size of the car
	 */
	public int getCurrentSize() {
		return this.currentSize;
	}
	
	/**
	 * Indicates if the car has reached its max size
	 * @return true if the car has reached its max size, false else
	 */
	public boolean reachMaxSize () {
		return this.currentSize == this.maxSize;
	}
	
	/**
	 * Indicates that the car has one more wagon
	 */
	public void hasOneMoreWagon () {
		this.currentSize++;
	}
	
	/**
	 * Gives the nth wagon of the car
	 * @param n the rank of the wagon we want
	 * @return the nth wagon
	 */
	public WagonPLS getWagon (int n) {
		if (n ==1) {
			return this.next;
		} else {
			return this.next.nextWagon(n-1);
		}
	}
	
	/**
	 * Sets the next wagon (and so the first) of the car.
	 * @param w the wagon pls to set
	 */
	public void setNextWagon (WagonPLS w) {
		this.next = w;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object clone () {
		ExtendedAgent aa = (ExtendedAgent) this.getOwner();
		IAgent4Engine ia4e = CarFactory.generate(
				(AbstractAgtPerceptionModel) aa.getPerceptionModel(LogoSimulationLevelList.LOGO),
				(AbstractAgtDecisionModel) aa.getDecisionModel(LogoSimulationLevelList.LOGO),
				this.getCategoryOfAgent(),
				this.direction ,
				this.speed ,
				this.acceleration,
				this.location.getX(),
				this.location.getY(),
				this.speedFrequence,
				this.maxCapacity
			);
		CarPLS newCar = new CarPLS(ia4e, location.getX(), location.getY(), speed, acceleration, direction, speedFrequence, maxCapacity);
		newCar.nbrPassenger = this.nbrPassenger;
		return newCar;
	}
}
