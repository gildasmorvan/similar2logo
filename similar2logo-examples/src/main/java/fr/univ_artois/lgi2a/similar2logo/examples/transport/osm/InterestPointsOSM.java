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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.osm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.Bank;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.Doctor;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.AbstractLeisure;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.Restaurant;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.School;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.Shop;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.time.Clock;

/**
 * Class for managing the leisure activity points
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class InterestPointsOSM {
	
	/**
	 * The roads of the map
	 */
	private List<Point2D> roads;
	
	/**
	 * The schools in the map
	 */
	private List<AbstractLeisure> schools;
	
	/**
	 * The restaurants in the map
	 */
	private List<AbstractLeisure> restaurants;
	
	/**
	 * The shops in the map
	 */
	private List<AbstractLeisure> shops;
	
	/**
	 * The doctors in the map
	 */
	private List<AbstractLeisure> doctors;
	
	/**
	 * The banks in the map
	 */
	private List<AbstractLeisure> banks;
	
	/**
	 * The height and the width of the map
	 */
	private int height, width;
	
	public InterestPointsOSM (List<Point2D> roads, DataFromOSM data, Clock c) {
		this.roads = roads;
		this.height = data.getHeight();
		this.width = data.getWidth();
		schools = new ArrayList<>();
		for (String s : data.getSchools()) {
			schools.add(new School (roadConnexion(data.getCoordinates(s)),c));
		}
		restaurants = new ArrayList<>();
		for (String s : data.getRestaurants()) {
			restaurants.add(new Restaurant (roadConnexion(data.getCoordinates(s)),c));
		}
		shops = new ArrayList<>();
		for (String s : data.getShops()) {
			shops.add(new Shop (roadConnexion(data.getCoordinates(s)),c));
		}
		doctors = new ArrayList<>();
		for (String s : data.getDoctors()) {
			doctors.add(new Doctor (roadConnexion(data.getCoordinates(s)),c));
		}
		banks = new ArrayList<>();
		for (String s : data.getBanks()) {
			banks.add(new Bank(roadConnexion(data.getCoordinates(s)),c));
		}
	}
	
	/**
	 * Gives pseudo randomly the position of a restaurant
	 * @return the position of a restaurant
	 */
	public Point2D getRestaurant () {
		return this.restaurants.get(
			PRNG.get().randomInt(restaurants.size())
		).getPosition();
	}
	
	/**
	 * Gives pseudo randomly the position of a shop
	 * @return the position of a shop
	 */
	public Point2D getShop () {
		return this.shops.get(
			PRNG.get().randomInt(shops.size())
		).getPosition();
	}
	
	/**
	 * Gives pseudo randomly the position of a doctor
	 * @return the position of a doctor
	 */
	public Point2D getDoctor () {
		return this.doctors.get(
			PRNG.get().randomInt(doctors.size())
		).getPosition();
	}
	
	/**
	 * Gives pseudo randomly the position of a bank
	 * @return the position of a bank
	 */
	public Point2D getBank () {
		return this.banks.get(
			PRNG.get().randomInt(banks.size())
		).getPosition();
	}
	
	/**
	 * Gives all the schools
	 * @return the list of the position of all the schools
	 */
	public List<AbstractLeisure> getAllSchools () {
		return this.schools;
	}
	
	/**
	 * Gives the entrance of the building place following where they are
	 * @param buildingPlace the place of the building
	 * @return the place where we can enter in the bulding
	 */
	private Point2D roadConnexion (Point2D buildingPlace) {
		if (roads.contains(buildingPlace)) {
			return buildingPlace;
		}
		int[][] res = new int[3][3];
		boolean[][] fatto = new boolean[3][3];
		for (int i=0; i <= 2; i++) {
			for (int j=0; j <= 2; j++) {
				res[i][j] = Integer.MAX_VALUE;
				fatto[i][j] = false;
			}
		}
		fatto[1][1] = true;
		int cpt = 0;
		int turn = 1;
		while (cpt != 8) {
			for (int i =-1; i <= 1; i++) {
				for (int j=-1; j <=1; j++) {
					if (!fatto[i+1][j+1]) {
						Point2D pos = new Point2D.Double(
							buildingPlace.getX() + i*turn, buildingPlace.getY() + j*turn
						);
						if (roads.contains(pos)) {
							fatto[i+1][j+1] = true;
							res[i+1][j+1] = turn;
							cpt++;
						} else if (!inTheEnvironment(pos)) {
							fatto[i+1][j+1] = true;
							res[i+1][j+1] = Integer.MAX_VALUE;
							cpt++;
						}
						turn++;
					}
				}
			}
		}
		int min = Integer.MAX_VALUE;
		int a =0, b=0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <=1; j++) {
				if (res[i+1][j+1] < min) {
					min = res[i+1][j+1];
					a = i;
					b = j;
				}
			}
		}
		return new Point2D.Double(
			buildingPlace.getX() + a*res[a+1][b+1], buildingPlace.getY() + b*res[a+1][b+1]
		);
	}
	
	/**
	 * Indicates if a point is in the environment
	 * @param pt a Point2D
	 * @return true if the point is in the limits of the environment, else false
	 */
	private boolean inTheEnvironment (Point2D pt) {
		return (pt.getX() >= 0)
			&& (pt.getY() >= 0) 
			&& (pt.getX() < width) 
			&& (pt.getY() < height);
	}
	
	/**
	 * Gives the list of leisure place
	 * @return the list of leisure place
	 */
	public List<AbstractLeisure> getLeisurePlaces () {
		List<AbstractLeisure> res = new ArrayList<>();
		for (AbstractLeisure l: schools) {
			res.add(l);
		}
		for (AbstractLeisure l: restaurants) {
			res.add(l);
		}
		for (AbstractLeisure l : shops) {
			res.add(l);
		}
		for (AbstractLeisure l : doctors) {
			res.add(l);
		}
		for (AbstractLeisure l : banks) {
			res.add(l);
		}
		return res;
	}

}
