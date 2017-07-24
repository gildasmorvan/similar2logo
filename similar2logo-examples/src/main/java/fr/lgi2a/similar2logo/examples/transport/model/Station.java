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

import java.awt.geom.Point2D;

/**
 * Class for the stops/stations for the "transport" simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class Station {
	
	/**
	 * The number of persons who are waiting at the stop/station for going up in a transport.
	 */
	private int waitingPeopleForTakingTransport;
	
	/**
	 * The number of persons who are waiting for going out of the station.
	 */
	private int waitingPeopleGoOut;
	
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
	
	public Station (Point2D access, Point2D exit, Point2D platform) {
		this.waitingPeopleForTakingTransport = 0;
		this.waitingPeopleGoOut = 0;
		this.access = access;
		this.exit = exit;
		this.platform = platform;
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
	 * Adds a waiting people to go up in a transport
	 */
	public void addWaitingPeopleToGoUp () {
		this.waitingPeopleForTakingTransport++;
	}
	
	/**
	 * Removes a waiting people to go up in a transport
	 */
	public void removeWaitingPeopleToGoUp () {
		this.waitingPeopleForTakingTransport--;
	}
	
	/**
	 * Indicates if there is someone who wants to go up in a transport
	 * @return true if the station/stop is empty, else false
	 */
	public boolean noWaitingPeopleToGoUp () {
		return (this.waitingPeopleForTakingTransport == 0);
	}
	
	/**
	 * Adds a waiting people for going out of the station
	 */
	public void addWaitingPeopleGoOut () {
		this.waitingPeopleGoOut++;
	}
	
	/**
	 * Removes a waiting people for going out of the station
	 */
	public void removeWaitingPeopleGoOut () {
		this.waitingPeopleGoOut--;
	}
	
	/**
	 * Indicates if there is someone who wants to go out of the station
	 * @return true if someone wants to leave the station, false else
	 */
	public boolean noWaitingPeopleToGoOut () {
		return (this.waitingPeopleGoOut == 0);
	}

}
