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
package fr.lgi2a.similar2logo.examples.transport.osm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private List<Point2D> schools;
	
	/**
	 * The restaurants in the map
	 */
	private List<Point2D> restaurants;
	
	/**
	 * The shops in the map
	 */
	private List<Point2D> shops;
	
	/**
	 * The doctors in the map
	 */
	private List<Point2D> doctors;
	
	/**
	 * The banks in the map
	 */
	private List<Point2D> banks;
	
	/**
	 * The height and the width of the map
	 */
	private int height, width;
	
	public InterestPointsOSM (List<Point2D> roads, DataFromOSM data) {
		this.roads = roads;
		this.height = data.getHeight();
		this.width = data.getWidth();
		schools = new ArrayList<>();
		for (String s : data.getSchools()) {
			schools.add(roadConnexion(data.getCoordinates(s)));
		}
		restaurants = new ArrayList<>();
		for (String s : data.getRestaurants()) {
			restaurants.add(roadConnexion(data.getCoordinates(s)));
		}
		shops = new ArrayList<>();
		for (String s : data.getShops()) {
			shops.add(roadConnexion(data.getCoordinates(s)));
		}
		doctors = new ArrayList<>();
		for (String s : data.getDoctors()) {
			doctors.add(roadConnexion(data.getCoordinates(s)));
		}
		banks = new ArrayList<>();
		for (String s : data.getBanks()) {
			banks.add(roadConnexion(data.getCoordinates(s)));
		}
	}
	
	/**
	 * Gives pseudo randomly the position of a school
	 * @return the position of a school
	 */
	public Point2D getSchool () {
		Random r = new Random ();
		return this.schools.get(r.nextInt(schools.size()));
	}
	
	/**
	 * Gives pseudo randomly the position of a restaurant
	 * @return the position of a restaurant
	 */
	public Point2D getRestaurant () {
		Random r = new Random ();
		return this.restaurants.get(r.nextInt(restaurants.size()));
	}
	
	/**
	 * Gives pseudo randomly the position of a shop
	 * @return the position of a shop
	 */
	public Point2D getShop () {
		Random r = new Random ();
		return this.shops.get(r.nextInt(shops.size()));
	}
	
	/**
	 * Gives pseudo randomly the position of a doctor
	 * @return the position of a doctor
	 */
	public Point2D getDoctor () {
		Random r = new Random ();
		return this.doctors.get(r.nextInt(doctors.size()));
	}
	
	/**
	 * Gives pseudo randomly the position of a bank
	 * @return the position of a bank
	 */
	public Point2D getBank () {
		Random r = new Random ();
		return this.banks.get(r.nextInt(banks.size()));
	}
	
	/**
	 * Gives the entrance of the building place following where they are
	 * @param buildingPlace the place of the building
	 * @return the place where we can enter in the bulding
	 */
	private Point2D roadConnexion (Point2D buildingPlace) {
		if (roads.contains(buildingPlace)) return buildingPlace;
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
					if (!fatto[i][j]) {
						Point2D pos = new Point2D.Double(buildingPlace.getX() + i*turn, buildingPlace.getY() + j*turn);
						if (roads.contains(pos)) {
							fatto[i][j] = true;
							res[i][j] = turn;
							cpt++;
						} else if (!inTheEnvironment(pos)) {
							fatto[i][j] = true;
							res[i][j] = Integer.MAX_VALUE;
							cpt++;
						}
					}
				}
			}
		}
		int min = Integer.MAX_VALUE;
		int a =0, b=0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <=1; j++) {
				if (res[i][j] < min) {
					min = res[i][j];
					a = i;
					b = j;
				}
			}
		}
		return new Point2D.Double(buildingPlace.getX() + a*res[a][b], buildingPlace.getY() + b*res[a][b]);
	}
	
	/**
	 * Indicates if a point is in the environment
	 * @param pt a Point2D
	 * @return true if the point is in the limits of the environment, else false
	 */
	private boolean inTheEnvironment (Point2D pt) {
		return ((pt.getX() >= 0) && (pt.getY() >= 0) && (pt.getX() < width) && (pt.getY() < height));
	}

}
