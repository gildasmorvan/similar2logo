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
import java.util.List;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar2logo.examples.transport.model.agents.person.PersonDecisionModel;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Class for the stops/stations for the "transport" simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class Station {
	
	/**
	 * The people waiting for going up in the train
	 */
	private List<ExtendedAgent> waitingPeopleForTakingTransport;
	
	/**
	 * The number of persons who are waiting for going out of the station.
	 */
	private List<ExtendedAgent> waitingPeopleForGoingOut;
	
	/**
	 * Place where people can join the stop/station.
	 */
	private Point2D access;
	
	/**
	 * Place where people can leave the stop/station
	 */
	private Point2D exit;
	
	/**
	 * Place where people can go up/go down in/from the transport.
	 */
	private Point2D platform;
	
	/**
	 * The type of the station
	 */
	private String type;
	
	public Station (Point2D place, String type) {
		this.waitingPeopleForTakingTransport = new ArrayList<>();
		this.waitingPeopleForGoingOut = new ArrayList<>();
		this.platform = place;
		this.type = type;
	}
	
	public Station (Point2D access, Point2D exit, Point2D platform, String type) {
		this.waitingPeopleForTakingTransport = new ArrayList<>();
		this.waitingPeopleForGoingOut = new ArrayList<>();
		this.access = access;
		this.exit = exit;
		this.platform = platform;
		this.type = type;
	}
	
	/**
	 * Set the accesses of the station
	 * @param entrance the entrance of the station
	 * @param exit the exit of the station
	 */
	public void setAccesses (Point2D entrance, Point2D exit) {
		this.access = entrance;
		this.exit = exit;
	}
	
	/**
	 * Gives the place where the persons can enter in the station/stop.
	 * @return Point2D the access
	 */
	public Point2D getAccess () {
		return this.access;
	}
	
	/**
	 * Gives the places where the persons can leave the station/stop
	 * @return
	 */
	public Point2D getExit () {
		return this.exit;
	}
	
	/**
	 * Gives the place where the transport can take persons from the station.
	 * @return Point2D the platform
	 */
	public Point2D getPlatform () {
		return this.platform;
	}	
	
	/**
	 * Gives the list of peoples who go in direction of destination
	 * @param currentPosition the current position of the transport
	 * @param destination the destination of the transport
	 * @return the list of people to add in the transport
	 */
	public List<ExtendedAgent> personsTakingTheTransport (Point2D currentPosition, Point2D destination) {
		List<ExtendedAgent> res = new ArrayList<>();
		for (ExtendedAgent p : waitingPeopleForTakingTransport) {
			PersonDecisionModel pdm = (PersonDecisionModel) p.getDecisionModel(LogoSimulationLevelList.LOGO);
			//The first argument is for removing an error when the way is of size 0
			if (pdm.getWay().isEmpty() || pdm.getWay().get(0).equals(destination)
					|| pdm.getWay().get(0).distance(destination) < currentPosition.distance(destination)) {
				res.add(p);
			}
		}
		return res;
	}
	
	/**
	 * Gives the persons wanting to take the bus for the destination
	 * @param currentPosition the current position of the bus
	 * @param destination the destination of the bus
	 * @param bl the bus line of the bus
	 * @return the persons who want to go up in the bus
	 */
	public List<ExtendedAgent> personsTakingTheBus (Point2D currentPosition, Point2D destination, BusLine bl) {
		List<ExtendedAgent> res = new ArrayList<>();
		for (ExtendedAgent p : waitingPeopleForTakingTransport) {
			PersonDecisionModel pdm = (PersonDecisionModel) p.getDecisionModel(LogoSimulationLevelList.LOGO);
			if (pdm.getWay().isEmpty() || bl.between2Stops(currentPosition, destination).contains(pdm.getWay().get(0))) {
				res.add(p);
			}
		}
		return res;
	}
	
	/**
	 * Gives the list of peoples wanting to go out
	 * @return the list of people wanting to go out
	 */
	public List<ExtendedAgent> getPersonsWantingToGoOut () {
		return this.waitingPeopleForGoingOut;
	}
	
	/**
	 * Adds a person who wants to take a transport
	 * @param person who wants to take a transport
	 */
	public void addPeopleWantingToTakeTheTransport (ExtendedAgent person) {
		this.waitingPeopleForTakingTransport.add(person);
	}
	
	/**
	 * Adds a person who wants to go out from the station
	 * @param person who wants to go out from the station
	 */
	public void addPeopleWantingToGoOut (ExtendedAgent person) {
		this.waitingPeopleForGoingOut.add(person);
	}
	
	/**
	 * Removes a person who wants to take a transport
	 * @param person who went in a transport
	 */
	public void removeWaitingPeopleForTakingTransport (ExtendedAgent person) {
		this.waitingPeopleForTakingTransport.remove(person);
	}
	
	/**
	 * Removes a person wanting to go out from the station
	 * @param person who wants to go out from the station
	 */
	public void removeWaitingPeopleForGoingOut (ExtendedAgent person) {
		this.waitingPeopleForGoingOut.remove(person);
	}
	
	/**
	 * Indicates if noone wants to leave the station
	 * @return true if noone wants to leave the station, false else
	 */
	public boolean nooneWantsToGoOut () {
		return this.waitingPeopleForGoingOut.isEmpty();
	}
	
	/**
	 * Gives the type of the station
	 * @return the type of the station
	 */
	public String getType () {
		return this.type;
	}
	
	public String toString () {
		return "Station type "+type+" "+", position : "+platform+", peoples wanting to take transport : "+waitingPeopleForTakingTransport.size()+
				", peoples wanting to go out : "+waitingPeopleForGoingOut.size();
	}
	
	public String toStringPosition () {
		return "Access : "+access.toString()+", platform : "+platform.toString()+", exit : "+exit.toString(); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((platform == null) ? 0 : platform.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object o) {
		if(this == o) {
			return true;
		}
		if(o == null) {
			return false;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		Station s = (Station) o;
		return this.platform.equals(s.getPlatform());
	}


	
	
}
