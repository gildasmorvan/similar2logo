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
import java.util.List;

import fr.lgi2a.similar2logo.examples.transport.osm.InterestPointsOSM;

/**
 * Class allows to generate the destination of the cars and the persons.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class DestinationGenerator {
	
	/**
	 * The interest points of the map.
	 */
	private InterestPointsOSM leisure;
	
	/**
	 * The list of the train stations.
	 */
	private List<Station> trainStations;
	
	/**
	 * The list of the tram stations.
	 */
	private List<Station> tramStations;

	public DestinationGenerator (InterestPointsOSM ipo, List<Station> listTrainStation, List<Station> listTramStation) {
		this.leisure = ipo;
		this.trainStations = listTrainStation;
		this.tramStations = listTramStation;
	}
	
	public Point2D getADestination () {
		return null;
	}
	
	/**
	 * Gives the position of the closest tram station.
	 * However, it's maybe not the closest by road but as the crow flies
	 * @param position the position from where we start
	 * @return the position of the closest tram station
	 */
	private Point2D closestTramStation (Point2D position) {
		int ind = 0;
		double dis = tramStations.get(0).getAccess().distance(position);
		for (int i=1; i < tramStations.size(); i++) {
			if (dis > tramStations.get(i).getAccess().distance(position)) {
				dis = tramStations.get(i).getAccess().distance(position);
				ind = i;
			}
		}
		return this.tramStations.get(ind).getAccess();
	}
	
	/**
	 * Gives the position of the closest train station.
	 * However, it's maybe not the closest by road but as the crow flies
	 * @param position the position from where we start
	 * @return the position of the closest train station
	 */
	private Point2D closestTrainStation (Point2D position) {
		int ind = 0;
		double dis = trainStations.get(0).getAccess().distance(position);
		for (int i=1; i < trainStations.size(); i++) {
			if (dis > trainStations.get(i).getAccess().distance(position)) {
				dis = trainStations.get(i).getAccess().distance(position);
				ind = i;
			}
		}
		return this.trainStations.get(ind).getAccess();
	}
	/**
	 * Gives the position of the closes school
	 * However, it's maybe not the closest by road but as crow flies
	 * @param position the positions from where we start
	 * @return the position of the closest school
	 */
	private Point2D closestSchool (Point2D position) {
		List<Point2D> schools = leisure.getAllSchools();
		int ind = 0;
		double dis = schools.get(0).distance(position);
		for (int i=1; i < schools.size(); i++) {
			if (dis < schools.get(i).distance(position)) {
				dis = schools.get(i).distance(position);
				ind = i;
			}
		}
		return schools.get(ind);
	}
}
