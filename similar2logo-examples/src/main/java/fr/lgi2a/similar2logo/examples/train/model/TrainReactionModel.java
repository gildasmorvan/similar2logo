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
package fr.lgi2a.similar2logo.examples.train.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;

/**
 * The reaction model of trains in the "train" simulation. Trains are developed for going back when they'll collide.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TrainReactionModel extends LogoDefaultReactionModel{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {
		//LogoEnvPLS environment = (LogoEnvPLS) consistentState.getPublicLocalStateOfEnvironment();
		HashMap<TurtlePLSInLogo,List<IInfluence>> turtlesInfluences = new HashMap<TurtlePLSInLogo,List<IInfluence>>();
		Set<IInfluence> nonSpecificInfluences = new LinkedHashSet<IInfluence>();
		List<Point2D> positionsInitiales = new ArrayList<>();
		// This list is use at the beginning of the simulation for allowing trains from the station to go one after the other.
		List<TurtlePLSInLogo> turtlesToStop = new ArrayList<>();
		// We class all the influences following their turtle
		for (IInfluence influence : regularInfluencesOftransitoryStateDynamics) {
			if (influence.getCategory().equals("change direction")) { 
				ChangeDirection cd = (ChangeDirection) influence ;
				TurtlePLSInLogo turtle = cd.getTarget();
				System.out.println(turtle);
				System.out.println("+ "+turtle.getLocation());
				System.out.println("-> "+turtle.getDirection());
				System.out.println("~ "+turtle.getSpeed());
				if (!turtlesInfluences.containsKey(turtle)) {
					turtlesInfluences.put(turtle, new ArrayList<IInfluence>());
				}
				List<IInfluence> listInfluence = turtlesInfluences.get(turtle);
				listInfluence.add(cd);
				turtlesInfluences.put(turtle, listInfluence);
				if (positionsInitiales.contains(turtle.getLocation())) {
					turtlesToStop.add(turtle);
				} else {
					positionsInitiales.add(turtle.getLocation());
				}
			} else if (influence.getCategory().equals("change speed")) {
				ChangeSpeed cs = (ChangeSpeed) influence ;
				TurtlePLSInLogo turtle = cs.getTarget();
				if (!turtlesInfluences.containsKey(turtle)) {
					turtlesInfluences.put(turtle, new ArrayList<IInfluence>());
				}
				List<IInfluence> listInfluence = turtlesInfluences.get(turtle);
				listInfluence.add(cs);
				turtlesInfluences.put(turtle, listInfluence);
			} else if (influence.getCategory().equals("stop")) {
				Stop st = (Stop) influence ;
				TurtlePLSInLogo turtle = st.getTarget();
				if (!turtlesInfluences.containsKey(turtle)) {
					turtlesInfluences.put(turtle, new ArrayList<IInfluence>());
				}
				List<IInfluence> listInfluence = turtlesInfluences.get(turtle);
				listInfluence.add(st);
				turtlesInfluences.put(turtle, listInfluence);
			} else {
				nonSpecificInfluences.add(influence);
			}
		}
		Set<TurtlePLSInLogo> turtles = turtlesInfluences.keySet();
		// We search the next position of each train
		HashMap <Point2D,List<TurtlePLSInLogo>> nextPositions = new HashMap <Point2D,List<TurtlePLSInLogo>>();
		HashMap <Point2D,List<TurtlePLSInLogo>> nextPositionsDemi = new HashMap <Point2D,List<TurtlePLSInLogo>>();
		Set<TurtlePLSInLogo> listTurtles = turtlesInfluences.keySet();
		for (TurtlePLSInLogo turtle : listTurtles) {
			List<IInfluence> influences = turtlesInfluences.get(turtle);
			Point2D nextPosition = calculateNextPositionPas1(turtle, influences);
			if (nextPositions.containsKey(nextPosition)) {
				List<TurtlePLSInLogo> list = nextPositions.get(nextPosition);
				list.add(turtle);
				nextPositions.put(nextPosition, list);
			} else {
				List<TurtlePLSInLogo> list = new ArrayList<TurtlePLSInLogo>();
				list.add(turtle);
				nextPositions.put(nextPosition, list);
			}
			Point2D nextPositionDemi = calculateNextPositionPas05(turtle, influences);
			if (nextPositionsDemi.containsKey(nextPositionDemi)) {
				List<TurtlePLSInLogo> list = nextPositionsDemi.get(nextPositionDemi);
				list.add(turtle);
				nextPositionsDemi.put(nextPositionDemi, list);
			} else {
				List<TurtlePLSInLogo> list = new ArrayList<TurtlePLSInLogo>();
				list.add(turtle);
				nextPositionsDemi.put(nextPositionDemi, list);
			}
		}
		// We get the list of train who can be at the same place at t+1 or t+0.5
		Set<TurtlePLSInLogo> conflictTrains = new HashSet<TurtlePLSInLogo>();
		Set<Point2D> nextTurn = nextPositions.keySet();
		Set<Point2D> nextTurnDemi = nextPositionsDemi.keySet();
		for (Point2D pos : nextTurn) {
			if ((nextPositions.get(pos).size() >1) /*&& !isStation(environment,pos)*/) {
				System.out.println("Conflit pas 1");
				for (TurtlePLSInLogo tur : nextPositions.get(pos)) {
					if (!turtlesToStop.contains(tur))
						conflictTrains.add(tur);
				}
			}
		}
		for (Point2D pos : nextTurnDemi) {
			if ((nextPositionsDemi.get(pos).size() >1) /*&& !isStation(environment,pos)*/) {
				System.out.println("Conflit pas 0.5");
				for (TurtlePLSInLogo tur : nextPositionsDemi.get(pos)) {
					if (!turtlesToStop.contains(tur))
						conflictTrains.add(tur);
				}
			}
		}
		// We ask to all the trains that are in conflict to go back, else we change nothing
		for (TurtlePLSInLogo tur : turtles) {
			if (conflictTrains.contains(tur)) {
				List<IInfluence> influencesToChange = turtlesInfluences.get(tur);
				for (IInfluence inf : influencesToChange) {
					if (inf.getCategory().equals("change direction")) {
						nonSpecificInfluences.add(new ChangeDirection(transitoryTimeMin, transitoryTimeMax, Math.PI, tur));
					} else  if (inf.getCategory().equals("change speed")){
						nonSpecificInfluences.add(new Stop (transitoryTimeMin,transitoryTimeMax, tur));
						//nonSpecificInfluences.add(new ChangeSpeed(transitoryTimeMin, transitoryTimeMax,this.distanceToDo(tur.getDirection()),tur));
					} else
						nonSpecificInfluences.add(inf);
				}
			} else {
				List<IInfluence> myInfluences = turtlesInfluences.get(tur);
				for (IInfluence inf : myInfluences) {
					if (inf.getCategory().equals("change speed")) {
						ChangeSpeed cs = (ChangeSpeed) inf;
						if (turtlesToStop.contains(cs.getTarget())) {
							nonSpecificInfluences.add(new Stop(transitoryTimeMin,transitoryTimeMax,cs.getTarget()));
						} else {
							nonSpecificInfluences.add(inf);
						}
					} else nonSpecificInfluences.add(inf);
				}
			}
		}
		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax, consistentState, nonSpecificInfluences, remainingInfluences);
	}
	
	/**
	 * Calculate the next position of a train following its influences with a step of 1.
	 * @param turtle the train
	 * @param influences the influences of the train
	 * @return the next position of the train
	 */
	private Point2D calculateNextPositionPas1 (TurtlePLSInLogo turtle , List<IInfluence> influences) {
		ChangeDirection cd = null;
		Stop st = null;
		@SuppressWarnings("unused")
		ChangeSpeed cs = null;
		for (IInfluence influence : influences) {
			if (influence.getCategory().equals("change direction")) {
				cd = (ChangeDirection) influence;
			} else if (influence.getCategory().equals("stop")) {
				st = (Stop) influence;
			} else if (influence.getCategory().equals("change speed")) {
				cs = (ChangeSpeed) influence ;
			}
		}
		Point2D position = new Point2D.Double(cd.getTarget().getLocation().getX(),cd.getTarget().getLocation().getY());
		if (st == null) {
			double direction = turtle.getDirection() + cd.getDd();
			double x = position.getX();
			double y = position.getY();
			if (direction == LogoEnvPLS.EAST) position.setLocation(x+1, y);
			else if (direction == LogoEnvPLS.NORTH) position.setLocation(x, y+1);
			else if (direction == LogoEnvPLS.NORTH_EAST) position.setLocation(x+1, y+1);
			else if (direction == LogoEnvPLS.NORTH_WEST) position.setLocation(x-1, y+1);
			else if ((direction == LogoEnvPLS.SOUTH) || (direction == (-1*LogoEnvPLS.SOUTH))) position.setLocation(x, y-1);
			else if (direction == LogoEnvPLS.SOUTH_EAST) position.setLocation(x+1, y-1);
			else if (direction == LogoEnvPLS.SOUTH_WEST) position.setLocation(x-1, y-1);
			else position.setLocation(x-1, y);
		}
		System.out.println("Prevision pas 1 : "+position);
		return position;
	}
	
	/**
	 * Calculate the next position of a train following its influences with a step of 0.5.
	 * @param turtle the train
	 * @param influences the influences of the train
	 * @return the next position of the train
	 */
	private Point2D calculateNextPositionPas05 (TurtlePLSInLogo turtle , List<IInfluence> influences) {
		ChangeDirection cd = null;
		Stop st = null;
		@SuppressWarnings("unused")
		ChangeSpeed cs = null;
		for (IInfluence influence : influences) {
			if (influence.getCategory().equals("change direction")) {
				cd = (ChangeDirection) influence;
			} else if (influence.getCategory().equals("stop")) {
				st = (Stop) influence;
			} else if (influence.getCategory().equals("change speed")) {
				cs = (ChangeSpeed) influence ;
			}
		}
		Point2D position = new Point2D.Double(cd.getTarget().getLocation().getX(),cd.getTarget().getLocation().getY());
		if (st == null) {
			double direction = turtle.getDirection() + cd.getDd();
			double x = position.getX();
			double y = position.getY();
			if (direction == LogoEnvPLS.EAST) position.setLocation(x+0.5, y);
			else if (direction == LogoEnvPLS.NORTH) position.setLocation(x, y+0.5);
			else if (direction == LogoEnvPLS.NORTH_EAST) position.setLocation(x+0.5, y+0.5);
			else if (direction == LogoEnvPLS.NORTH_WEST) position.setLocation(x-0.5, y+0.5);
			else if ((direction == LogoEnvPLS.SOUTH) || (direction == (-1*LogoEnvPLS.SOUTH))) position.setLocation(x, y-0.5);
			else if (direction == LogoEnvPLS.SOUTH_EAST) position.setLocation(x+0.5, y-0.5);
			else if (direction == LogoEnvPLS.SOUTH_WEST) position.setLocation(x-0.5, y-0.5);
			else position.setLocation(x-0.5, y);
		}
		System.out.println("Prevision pas 0.5 : "+position);
		return position;
	}

	/**
	 * Give the distance to do following the direction.
	 * @param radius direction of the turtle
	 * @return the distance the train has to do
	 */
	/*private double distanceToDo (double radius) {
		if ((radius % (Math.PI/2)) == 0) return 1;
		else return Math.sqrt(2);
	}*/
	
	/**
	 * Indicate if a position has a mark station
	 * @param env the environment of the simulation
	 * @param pt the position to check
	 * @return if there is a station at pt
	 */
	/*@SuppressWarnings("rawtypes")
	private boolean isStation (LogoEnvPLS env, Point2D pt) {
		Set<Mark> marks = env.getMarksAt((int) pt.getX(), (int) pt.getY());
		for ( Mark m : marks) {
			if (m.getCategory().equals("Station")) return true;
		}
		return false;
	}*/
	

}
