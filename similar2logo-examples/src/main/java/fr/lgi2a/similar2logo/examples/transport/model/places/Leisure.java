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
package fr.lgi2a.similar2logo.examples.transport.model.places;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.transport.time.Clock;

/**
 * Class for the leisure place of the map.
 * It allows to manage the persons and cars who go in there.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public abstract class Leisure {
	
	/**
	 * The list of the persons who want to leave the leisure place
	 */
	protected int nbrPersonsWantingToGoOut;
	
	/**
	 * The last time the list of persons wanting to go out has been upadated
	 */
	protected SimulationTimeStamp lastUpdate;
	
	/**
	 * The number of peoples who have to exit at a precise moment
	 */
	protected Map <SimulationTimeStamp,Integer> exitTime;
	
	/**
	 * The entrance of the leisure place
	 */
	protected Point2D entrance;
	
	/**
	 * The clock of the simulation
	 */
	protected Clock clock;
	
	public Leisure (Point2D position, Clock c) {
		this.nbrPersonsWantingToGoOut = 0;
		this.lastUpdate = new SimulationTimeStamp(0);
		this.exitTime = new HashMap<>();
		this.entrance = position;
		this.clock = c;
	}
	
	/**
	 * Gives the number of the persons who want to leave from the leisure
	 * @param currentTime the current time of the simulation
	 * @return the number of peoples who want to leave the place
	 */
	public int getWaitingPeople (SimulationTimeStamp currentTime) {
		List<SimulationTimeStamp> toRemove = new ArrayList<>();
		for (SimulationTimeStamp sts : exitTime.keySet()) {
			if (sts.getIdentifier() <= currentTime.getIdentifier() && sts.getIdentifier() > lastUpdate.getIdentifier()) {
				nbrPersonsWantingToGoOut += exitTime.get(sts);
				toRemove.add(sts);
			}
		}
		for (SimulationTimeStamp s : toRemove)
			exitTime.remove(s);
		lastUpdate = currentTime;
		//System.out.println(this+" "+personsWantingToGoOut.size());
		return this.nbrPersonsWantingToGoOut;
	}
	
	/**
	 * Adds a person in the leisure place
	 * @param time the moment when the person arrives
	 */
	public abstract void addPerson (SimulationTimeStamp time);
	
	/**
	 * Returns the position of the leisure place
	 * @return the position of the leisure place
	 */
	public Point2D getPosition () {
		return this.entrance;
	}

	/**
	 * Removes a person who waits for exiting
	 */
	public void removeWaitingPeople () {
		this.nbrPersonsWantingToGoOut--;
	}
}
