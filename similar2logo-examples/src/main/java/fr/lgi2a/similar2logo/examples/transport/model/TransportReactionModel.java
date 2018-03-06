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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bike.BikeCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bus.BusCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.bus.BusPLS;
import fr.lgi2a.similar2logo.examples.transport.model.agents.car.CarCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.car.CarPLS;
import fr.lgi2a.similar2logo.examples.transport.model.agents.person.PersonCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.TrainCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.TramCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.TransportPLS;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.WagonCategory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.WagonDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.WagonFactory;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.WagonPLS;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.kernel.tools.FastMath;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * Reaction model of the transport simulation.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportReactionModel extends LogoDefaultReactionModel {

	/**
	 * {@inheritDoc}
	 */
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin, SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics, InfluencesMap remainingInfluences) {
		Set<IInfluence> nonSpecificInfluences = new HashSet<>();
		Map<TurtlePLSInLogo, List<IInfluence>> turtlesInfluences = new HashMap<>();
		Map<Point2D, List<TurtlePLSInLogo>> nextPositions = new HashMap<>();
		Map<Point2D, List<TurtlePLSInLogo>> currentPositions = new HashMap<>();
		Map<TurtlePLSInLogo, Point2D> nPos = new HashMap<>();
		Set<TurtlePLSInLogo> turtlesStopped = new HashSet<>();
		//System.out.println(transitoryTimeMin.toString());
		// Sort the influence following their owner.
		for (IInfluence i : regularInfluencesOftransitoryStateDynamics) {
			if (i.getCategory().equals("change direction")) {
				ChangeDirection cd = (ChangeDirection) i;
				TurtlePLSInLogo turtle = cd.getTarget();
				if (!turtlesInfluences.containsKey(turtle))
					turtlesInfluences.put(turtle, new ArrayList<>());
				turtlesInfluences.get(turtle).add(cd);
			} else if (i.getCategory().equals("change speed")) {
				ChangeSpeed cs = (ChangeSpeed) i;
				TurtlePLSInLogo turtle = cs.getTarget();
				if (!turtlesInfluences.containsKey(turtle))
					turtlesInfluences.put(turtle, new ArrayList<>());
				turtlesInfluences.get(turtle).add(cs);
			} else if (i.getCategory().equals("stop")) {
				Stop s = (Stop) i;
				TurtlePLSInLogo turtle = s.getTarget();
				if (!turtlesInfluences.containsKey(turtle))
					turtlesInfluences.put(turtle, new ArrayList<>());
				turtlesInfluences.get(turtle).add(s);
				turtlesStopped.add(turtle);
			}
			nonSpecificInfluences.add(i);
		}
		// We determine where the turtles are and will be in the next turn
		for (TurtlePLSInLogo t : turtlesInfluences.keySet()) {
			Point2D currentPos = t.getLocation();
			if (!currentPositions.containsKey(currentPos)) {
				currentPositions.put(currentPos, new ArrayList<>());
			}
			currentPositions.get(currentPos).add(t);
			if (t.getCategoryOfAgent().equals(CarCategory.CATEGORY)) {
				CarPLS c = (CarPLS) t;
				for (int i =0; i < c.getCurrentSize() -1; i++) {
					WagonPLS w = c.getWagon(i);
					if (!currentPositions.containsKey(w.getLocation())) {
						currentPositions.put(w.getLocation(), new ArrayList<>());
					}
					currentPositions.get(w.getLocation()).add(w);
				}
			} else if (t.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
				BusPLS c = (BusPLS) t;
				for (int i =0; i < c.getCurrentSize() -1; i++) {
					WagonPLS w = c.getWagon(i);
					if (!currentPositions.containsKey(w.getLocation())) {
						currentPositions.put(w.getLocation(), new ArrayList<>());
					}
					currentPositions.get(w.getLocation()).add(w);
				}
			} else if (t.getCategoryOfAgent().equals(TramCategory.CATEGORY) || t.getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
				TransportPLS c = (TransportPLS) t;
				for (int i =0; i < c.getCurrentSize() -1; i++) {
					WagonPLS w = c.getWagon(i);
					if (!currentPositions.containsKey(w.getLocation())) {
						currentPositions.put(w.getLocation(), new ArrayList<>());
					}
					currentPositions.get(w.getLocation()).add(w);
				}
			}
			Point2D pos = calculateNextPosition(t, turtlesInfluences.get(t));
			nPos.put(t, pos);
			if (!nextPositions.containsKey(pos))
				nextPositions.put(pos, new ArrayList<>());
			nextPositions.get(pos).add(t);
		}
		//We block the turtles that can't move because of the wagons
		Set<TurtlePLSInLogo> newBlocked = new HashSet<>();
		for (TurtlePLSInLogo t : turtlesInfluences.keySet()) {
			if (!turtlesStopped.contains(t) && currentPositions.containsKey(nPos.get(t))) {
				for (TurtlePLSInLogo t2 : currentPositions.get(nPos.get(t))) {
					boolean dontBlockMyself = true;
					if (t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
						WagonPLS w = (WagonPLS) t2;
						if (w.getHead().equals(t)) {
							dontBlockMyself = false;
						}
					}
					if (dontBlockMyself && isImpactedBy(t, t2, turtlesInfluences)) {
						newBlocked.add(t);
					}
				}
			}
		}
		for (TurtlePLSInLogo t : newBlocked) {
			if (!t.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
				for (IInfluence i : turtlesInfluences.get(t))
					if (i.getCategory().equals("change speed"))
						nonSpecificInfluences.remove(i);
				nonSpecificInfluences.add(new Stop(transitoryTimeMin, transitoryTimeMax, t));
				turtlesStopped.add(t);
			}
		}
		//We determine which turtles moves
		Set<TurtlePLSInLogo> turtleToMove = new HashSet<>();
		newBlocked = new HashSet<>();
		for (Point2D p : nextPositions.keySet()) {
			if (nextPositions.get(p).size() > 1) {
				Set<TurtlePLSInLogo> safe = getPriority(nextPositions.get(p), turtlesInfluences, turtlesStopped);
				for (TurtlePLSInLogo t : nextPositions.get(p)) {
					if (safe.contains(t))
						turtleToMove.add(t);
					else 
						newBlocked.add(t);
				}
			} else {
				if (!turtlesStopped.contains(nextPositions.get(p).get(0)))
					turtleToMove.add(nextPositions.get(p).get(0));
			}
		}
		for (TurtlePLSInLogo t : newBlocked) {
			for (IInfluence i : turtlesInfluences.get(t))
				if (i.getCategory().equals("change speed"))
					nonSpecificInfluences.remove(i);
			nonSpecificInfluences.add(new Stop(transitoryTimeMin, transitoryTimeMax, t));
			turtlesStopped.add(t);
		}
		//We add the movement of the wagons
		List<IInfluence> wagonsMovments = moveWagons(transitoryTimeMin, transitoryTimeMax, turtleToMove, turtlesStopped);
		for (IInfluence i : wagonsMovments) {
			nonSpecificInfluences.add(i);
		}
		// Creates the new wagons if it's possible
		if (transitoryTimeMin.getIdentifier() != 0) {
			Set<IInfluence> newWagons = this.createNewWagons(transitoryTimeMin, transitoryTimeMax, turtleToMove, turtlesInfluences);
			for (IInfluence i : newWagons) {
				remainingInfluences.add(i);
			}
		}
		super.makeRegularReaction(
			transitoryTimeMin,
			transitoryTimeMax,
			consistentState,
			nonSpecificInfluences,
			remainingInfluences
		);
	}

	/**
	 * Calculate the next position of a train following its influences.
	 * 
	 * @param turtle
	 *            the train
	 * @param influences
	 *            the influences of the train
	 * @return the next position of the train
	 */
	private Point2D calculateNextPosition(TurtlePLSInLogo turtle, List<IInfluence> influences) {
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
				cs = (ChangeSpeed) influence;
			}
		}
		Point2D position;
		if (cd != null) {
			position = new Point2D.Double(
				cd.getTarget().getLocation().getX(),
				cd.getTarget().getLocation().getY()
			);
		} else {
			position = new Point2D.Double(
				st.getTarget().getLocation().getX(),
				st.getTarget().getLocation().getY()
			);
		}
		if (st == null) {
			double direction = turtle.getDirection() + cd.getDd();
			double x = position.getX();
			double y = position.getY();
			if (FastMath.areEqual(direction, LogoEnvPLS.EAST)) position.setLocation(x + 1, y);
			else if (FastMath.areEqual(direction, LogoEnvPLS.NORTH)) {
				position.setLocation(x, y + 1);
			}
			else if (FastMath.areEqual(direction, LogoEnvPLS.NORTH_EAST)) {
				position.setLocation(x + 1, y + 1);
			}
			else if (FastMath.areEqual(direction, LogoEnvPLS.NORTH_WEST)) {
				position.setLocation(x - 1, y + 1);
			}
			else if (FastMath.areEqual(direction, LogoEnvPLS.SOUTH )) {
				position.setLocation(x, y - 1);
			}
			else if (FastMath.areEqual(direction, LogoEnvPLS.SOUTH_EAST)) {
				position.setLocation(x + 1, y - 1);
			}
			else if (FastMath.areEqual(direction, LogoEnvPLS.SOUTH_WEST)) {
				position.setLocation(x - 1, y - 1);
			}
			else {
				position.setLocation(x - 1, y);
			}
		}
		return position;
	}

	/**
	 * Gives the priority turtles in case of conflict. The trains have priority on
	 * the trams that have priority on the cars
	 * @param turtles a list of turtles
	 * @param influences the influences of the turtles
	 * @param stoppedTurltes the turtles stopped previously in the reaction model        
	 * @return the turtles that have the priority.
	 */
	private Set<TurtlePLSInLogo> getPriority(List<TurtlePLSInLogo> turtles, Map<TurtlePLSInLogo, List<IInfluence>> influences,
			Set<TurtlePLSInLogo> stoppedTurtles) {
		Set<TurtlePLSInLogo> res = new HashSet<>();
		for (int i = 0; i < turtles.size(); i++) {
			boolean noConflict = true;
			if (stoppedTurtles.contains(turtles.get(i))) {
				noConflict = false;
			} else {
				if (turtles.get(i).getCategoryOfAgent().equals(PersonCategory.CATEGORY)) {
					for (int j = 0; j < turtles.size(); j++) {
						if (noConflict && i != j) {
							if (turtles.get(j).getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
								WagonPLS w = (WagonPLS) turtles.get(j);
								if (w.getTypeHead().equals("train") || w.getTypeHead().equals("tram"))
									noConflict = false;
							} else if (turtles.get(j).getCategoryOfAgent().equals(TrainCategory.CATEGORY))
								noConflict = false;
							else if (turtles.get(j).getCategoryOfAgent().equals(TramCategory.CATEGORY))
								noConflict = false;
						}
					}
				} else if (turtles.get(i).getCategoryOfAgent().equals(BikeCategory.CATEGORY)) {
					for (int j = 0; j < turtles.size(); j++) {
						if (noConflict && i != j) {
							if (turtles.get(j).getCategoryOfAgent().equals(WagonCategory.CATEGORY)
									|| turtles.get(j).getCategoryOfAgent().equals(TramCategory.CATEGORY)
									|| turtles.get(j).getCategoryOfAgent().equals(TrainCategory.CATEGORY))
								noConflict = false;
							else if (turtles.get(j).getCategoryOfAgent().equals(CarCategory.CATEGORY) ||
									turtles.get(j).getCategoryOfAgent().equals(BusCategory.CATEGORY))
								noConflict &= passEachOther(turtles.get(i), turtles.get(j), influences);
						}
					}
				} else if (turtles.get(i).getCategoryOfAgent().equals(CarCategory.CATEGORY) ||
						turtles.get(i).getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
					for (int j = 0; j < turtles.size(); j++) {
						if (noConflict && i != j) {
							if (turtles.get(j).getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
								WagonPLS w = (WagonPLS) turtles.get(j);
								if (w.getTypeHead().equals("car")) {
									if (!passEachOther(turtles.get(i), turtles.get(j), influences))
										noConflict = false;
								} else {
									noConflict = false;
								}
							} else if (turtles.get(j).getCategoryOfAgent().equals(TramCategory.CATEGORY)
									|| turtles.get(j).getCategoryOfAgent().equals(TrainCategory.CATEGORY))
								noConflict = false;
							else if (turtles.get(j).getCategoryOfAgent().equals(CarCategory.CATEGORY) ||
									turtles.get(j).getCategoryOfAgent().equals(BusCategory.CATEGORY))
								noConflict &= passEachOther(turtles.get(i), turtles.get(j), influences);
						}
					}
				} else if (turtles.get(i).getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
					for (int j = 0; j < turtles.size(); j++) {
						if (noConflict && i != j) {
							if (turtles.get(j).getCategoryOfAgent().equals(TrainCategory.CATEGORY))
								noConflict = false;
							else if (turtles.get(j).getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
								WagonPLS w = (WagonPLS) turtles.get(j);
								if (w.getTypeHead().equals("train"))
									noConflict = false;
								else if (w.getTypeHead().equals("tram"))
									noConflict &= passEachOther(turtles.get(i), turtles.get(j), influences);
							}
						}
					}
				}
			}
			if (noConflict)
				res.add(turtles.get(i));
		}
		if (res.isEmpty()) {
			List<TurtlePLSInLogo> remain = unstoppedTurtles(turtles, stoppedTurtles);
			if (!remain.isEmpty()) {
				res.add(remain.get(RandomValueFactory.getStrategy().randomInt(remain.size())));
			}
		}
		return res;
	}

	/**
	 * Indicates if 2 cars can pass each other
	 * 
	 * @param t1 first turtle
	 * @param t2 second turtle
	 * @param influences the influences of the turtles
	 * @return true if the cars can pass each other, false else
	 */
	private boolean passEachOther(TurtlePLSInLogo t1, TurtlePLSInLogo t2, Map<TurtlePLSInLogo, List<IInfluence>> influences) {
		if (t1.getCategoryOfAgent().equals(BikeCategory.CATEGORY)
				|| t2.getCategoryOfAgent().equals(BikeCategory.CATEGORY))
			return true;
		double t1Direction = t1.getDirection();
		double t2Direction = t2.getDirection();
		if (!t1.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
			for (IInfluence i : influences.get(t1)) {
				if (i.getCategory().equals("change direction")) {
					ChangeDirection cd = (ChangeDirection) i;
					t1Direction += cd.getDd();
				}
			}
		}
		if (!t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
			for (IInfluence i : influences.get(t2)) {
				if (i.getCategory().equals("change direction")) {
					ChangeDirection cd = (ChangeDirection) i;
					t2Direction += cd.getDd();
				}
			}
		}
		return ((FastMath.areEqual(t1Direction, LogoEnvPLS.NORTH) && (FastMath.areEqual(t2Direction, LogoEnvPLS.SOUTH)
				|| FastMath.areEqual(t2Direction, LogoEnvPLS.SOUTH_EAST) || FastMath.areEqual(t2Direction, LogoEnvPLS.SOUTH_WEST)))
				|| (FastMath.areEqual(t1Direction, LogoEnvPLS.NORTH_EAST) && (FastMath.areEqual(t2Direction, LogoEnvPLS.SOUTH_WEST)
						|| FastMath.areEqual(t2Direction, LogoEnvPLS.WEST) || FastMath.areEqual(t2Direction, LogoEnvPLS.SOUTH)))
				|| (FastMath.areEqual(t1Direction, LogoEnvPLS.EAST) && (FastMath.areEqual(t2Direction, LogoEnvPLS.WEST)
						|| FastMath.areEqual(t2Direction, LogoEnvPLS.NORTH_WEST) || FastMath.areEqual(t2Direction, LogoEnvPLS.SOUTH_WEST)))
				|| (FastMath.areEqual(t1Direction, LogoEnvPLS.SOUTH_EAST) && (FastMath.areEqual(t2Direction, LogoEnvPLS.NORTH_WEST)
						|| FastMath.areEqual(t2Direction, LogoEnvPLS.NORTH) || FastMath.areEqual(t2Direction, LogoEnvPLS.WEST)))
				|| (FastMath.areEqual(t1Direction, LogoEnvPLS.SOUTH) && (FastMath.areEqual(t2Direction, LogoEnvPLS.NORTH)
						|| FastMath.areEqual(t2Direction, LogoEnvPLS.NORTH_EAST) || FastMath.areEqual(t2Direction, LogoEnvPLS.NORTH_WEST)))
				|| (FastMath.areEqual(t1Direction, LogoEnvPLS.SOUTH_WEST) && (FastMath.areEqual(t2Direction, LogoEnvPLS.NORTH_EAST)
						|| FastMath.areEqual(t2Direction, LogoEnvPLS.NORTH) || FastMath.areEqual(t2Direction, LogoEnvPLS.EAST)))
				|| (FastMath.areEqual(t1Direction, LogoEnvPLS.WEST) && (FastMath.areEqual(t2.getDirection(), LogoEnvPLS.EAST)
						|| FastMath.areEqual(t2Direction, LogoEnvPLS.SOUTH_EAST) || FastMath.areEqual(t2Direction, LogoEnvPLS.NORTH_EAST)))
				|| (FastMath.areEqual(t1Direction, LogoEnvPLS.NORTH_WEST) && (FastMath.areEqual(t2Direction, LogoEnvPLS.SOUTH_EAST)
						|| FastMath.areEqual(t2Direction, LogoEnvPLS.SOUTH) || FastMath.areEqual(t2Direction, LogoEnvPLS.EAST)))
			   );
	}

	/**
	 * Gives the influences of the wagons
	 * 
	 * @param timeMin
	 *            the beginning
	 * @param timeMax
	 *            the end
	 * @param turtlesToMove
	 *            the turtles which move
	 * @param stoppedTurtles
	 *            the stopped turtles
	 * @return a list with the influences of the
	 */
	private List<IInfluence> moveWagons(SimulationTimeStamp timeMin, SimulationTimeStamp timeMax,
			Set<TurtlePLSInLogo> turtlesToMove, Set<TurtlePLSInLogo> stoppedTurtles) {
		List<IInfluence> influences = new ArrayList<>();
		// We ask to all the wagon to move
		for (TurtlePLSInLogo turtle : turtlesToMove) {
			if (turtle.getCategoryOfAgent().equals(TrainCategory.CATEGORY)
					|| turtle.getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
				TransportPLS trPLS = (TransportPLS) turtle;
				double direction = trPLS.getDirection();
				for (int i = 0; i < trPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = trPLS.getWagon(i);
					if (i == 0) {
						influences.add(new ChangeDirection(timeMin, timeMax,
								-wagon.getDirection() + getDirection(wagon.getLocation(), trPLS.getLocation()), wagon));
						influences.add(new ChangeSpeed(timeMin, timeMax, -wagon.getSpeed() + distanceToDo(direction), wagon));
					} else {
						influences.add(new ChangeDirection(timeMin, timeMax,
								-wagon.getDirection() + getDirection(wagon.getLocation(), trPLS.getWagon(i - 1).getLocation()), wagon));
						influences.add(new ChangeSpeed(timeMin, timeMax, 
										-wagon.getSpeed() + distanceToDo(trPLS.getWagon(i-1).getDirection()), wagon));
					}
				}
			} else if (turtle.getCategoryOfAgent().equals(CarCategory.CATEGORY)) {
				CarPLS cPLS = (CarPLS) turtle;
				for (int i = 0; i < cPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = cPLS.getWagon(i);
					if (i == 0) {
						double direction = getDirection(wagon.getLocation(), cPLS.getLocation());
						influences.add(new ChangeDirection(timeMin, timeMax, -wagon.getDirection() + direction, wagon));
						influences.add(new ChangeSpeed(timeMin, timeMax, -wagon.getSpeed() + distanceToDo(direction), wagon));
					} else {
						double direction = cPLS.getWagon(i-1).getDirection();
						influences.add(new ChangeDirection(timeMin, timeMax, -wagon.getDirection()+ direction, wagon));
						influences.add(new ChangeSpeed(timeMin, timeMax,  -wagon.getSpeed() + distanceToDo(direction), wagon));
					}
				}
			} else if (turtle.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
				BusPLS bPLS = (BusPLS) turtle;
				for (int i = 0; i < bPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = bPLS.getWagon(i);
					if (i == 0) {
						double direction = getDirection(wagon.getLocation(), bPLS.getLocation());
						influences.add(new ChangeDirection(timeMin, timeMax, -wagon.getDirection() + direction, wagon));
						influences.add(new ChangeSpeed(timeMin, timeMax, -wagon.getSpeed() + distanceToDo(direction), wagon));
					} else {
						double direction = bPLS.getWagon(i-1).getDirection();
						influences.add(new ChangeDirection(timeMin, timeMax, -wagon.getDirection()+ direction, wagon));
						influences.add(new ChangeSpeed(timeMin, timeMax,  -wagon.getSpeed() + distanceToDo(direction), wagon));
					}
				}
			}
		}
		for (TurtlePLSInLogo turtle : stoppedTurtles) {
			if (turtle.getCategoryOfAgent().equals(TrainCategory.CATEGORY)
					|| turtle.getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
				TransportPLS trPLS = (TransportPLS) turtle;
				for (int i = 0; i < trPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = trPLS.getWagon(i);
					influences.add(new Stop(timeMin, timeMax, wagon));
				}
			} else if (turtle.getCategoryOfAgent().equals(CarCategory.CATEGORY)) {
				CarPLS cPLS = (CarPLS) turtle;
				for (int i = 0; i < cPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = cPLS.getWagon(i);
					influences.add(new Stop(timeMin, timeMax, wagon));
				}
			} else if (turtle.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
				BusPLS bPLS = (BusPLS) turtle;
				for (int i = 0; i < bPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = bPLS.getWagon(i);
					influences.add(new Stop(timeMin, timeMax, wagon));
				}
			}
		}
		return influences;
	}
	
	/**
	 * Gives the influences of creation of the wagon
	 * @param begin the beginning of the reaction
	 * @param end the end of the reaction
	 * @param movingTurtles the turtles which move
	 * @param influences the influences of the turtles
	 * @return the new influences for the creation of the wagons
	 */
	private Set<IInfluence> createNewWagons (SimulationTimeStamp begin, SimulationTimeStamp end, Set<TurtlePLSInLogo> movingTurtles,
			Map<TurtlePLSInLogo,List<IInfluence>> influences) {
		Set<IInfluence> newWagons = new HashSet<>();
		for (TurtlePLSInLogo t : movingTurtles) {
			if (t.getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
				TransportPLS tra = (TransportPLS) t;
				if (!tra.reachMaxSize()) {
					if (tra.getCurrentSize() == 1) {
						Point2D nextPosition = calculateNextPosition(t, influences.get(t));
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(t.getLocation(), nextPosition), 0, 0,
								t.getLocation().getX(), t.getLocation().getY(), t, "tram");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} else if (tra.getCurrentSize() == 2) {
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(tra.getWagon(0).getLocation(), t.getLocation()), 0, 0,
								tra.getWagon(0).getLocation().getX(), tra.getWagon(0).getLocation().getY(), t, "tram");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} else {
						int size = tra.getCurrentSize();
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(tra.getWagon(size-2).getLocation(), 
										tra.getWagon(size-3).getLocation()), 0, 0,
								tra.getWagon(size-2).getLocation().getX(), tra.getWagon(size-2).getLocation().getY(), t, "tram");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} 
				}
			} else if (t.getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
				TransportPLS tra = (TransportPLS) t;
				if (!tra.reachMaxSize()) {
					if (tra.getCurrentSize() == 1) {
						Point2D nextPosition = calculateNextPosition(t, influences.get(t));
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(t.getLocation(), nextPosition), 0, 0,
								t.getLocation().getX(), t.getLocation().getY(), t, "train");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} else if (tra.getCurrentSize() == 2) {
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(tra.getWagon(0).getLocation(), t.getLocation()), 0, 0,
								tra.getWagon(0).getLocation().getX(), tra.getWagon(0).getLocation().getY(), t, "train");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} else {
						int size = tra.getCurrentSize();
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(tra.getWagon(size-2).getLocation(), 
										tra.getWagon(size-3).getLocation()), 0, 0,
								tra.getWagon(size-2).getLocation().getX(), tra.getWagon(size-2).getLocation().getY(), t, "train");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					}
				}
			} else if (t.getCategoryOfAgent().equals(CarCategory.CATEGORY)) {
				CarPLS c = (CarPLS) t;
				if (!c.reachMaxSize()) {
					if (c.getCurrentSize() == 1) {
						Point2D nextPosition = calculateNextPosition(t, influences.get(t));
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(t.getLocation(), nextPosition), 0, 0,
								t.getLocation().getX(), t.getLocation().getY(), t, "car");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						c.addWagon(w);
					} else if (c.getCurrentSize() == 2) {
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(c.getWagon(0).getLocation(), t.getLocation()), 0, 0,
								c.getWagon(0).getLocation().getX(), c.getWagon(0).getLocation().getY(), t, "car");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						c.addWagon(w);
					} else {
						int size = c.getCurrentSize();
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(c.getWagon(size-2).getLocation(), 
										c.getWagon(size-3).getLocation()), 0, 0,
								c.getWagon(size-2).getLocation().getX(), c.getWagon(size-2).getLocation().getY(), t, "car");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						c.addWagon(w);
					}
				}
			} else if (t.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
				BusPLS b = (BusPLS) t;
				if (!b.reachMaxSize()) {
					if (b.getCurrentSize() == 1) {
						Point2D nextPosition = calculateNextPosition(t, influences.get(t));
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(t.getLocation(), nextPosition), 0, 0,
								t.getLocation().getX(), t.getLocation().getY(), t, "car");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						b.addWagon(w);
					} else if (b.getCurrentSize() == 2) {
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(b.getWagon(0).getLocation(), t.getLocation()), 0, 0,
								b.getWagon(0).getLocation().getX(), b.getWagon(0).getLocation().getY(), t, "car");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						b.addWagon(w);
					} else {
						int size = b.getCurrentSize();
						ExtendedAgent ea = WagonFactory.generate(
								new TurtlePerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
								new WagonDecisionModel(), WagonCategory.CATEGORY,
								getDirection(b.getWagon(size-2).getLocation(), 
										b.getWagon(size-3).getLocation()), 0, 0,
								b.getWagon(size-2).getLocation().getX(), b.getWagon(size-2).getLocation().getY(), t, "car");
						newWagons.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end,
								ea));
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						b.addWagon(w);
					}
				}
			}
		}
		return newWagons;
	}

	/**
	 * Gives the distance to do following the direction of the train
	 * 
	 * @param radius
	 *            direction of the train
	 * @return the distance to do
	 */
	private double distanceToDo(double radius) {
		if (FastMath.areEqual(radius, LogoEnvPLS.NORTH_EAST) || FastMath.areEqual(radius, LogoEnvPLS.NORTH_WEST) || FastMath.areEqual(radius, LogoEnvPLS.SOUTH_EAST)
				|| FastMath.areEqual(radius, LogoEnvPLS.SOUTH_WEST)) {
			return Math.sqrt(2);
		} else {
			return 1;
		}
	}

	/**
	 * Gives the direction to take for going somewhere
	 * 
	 * @param me
	 *            the current position
	 * @param previous
	 *            the position where we want to go
	 * @return the direction where to go
	 */
	private double getDirection(Point2D me, Point2D previous) {
		double x = previous.getX() - me.getX();
		double y = previous.getY() - me.getY();
		if (FastMath.areEqual(x,-1)) {
			if (FastMath.areEqual(y, -1)) {
				return LogoEnvPLS.SOUTH_WEST;
			} else if (FastMath.areEqual(y, 0)) {
				return LogoEnvPLS.WEST;
			} else {
				return LogoEnvPLS.NORTH_WEST;
			}
		} else if (FastMath.areEqual(x, 0)) {
			if (FastMath.areEqual(y, -1)) {
				return LogoEnvPLS.SOUTH;
			} else {
				return LogoEnvPLS.NORTH;
			}
		} else {
			if (FastMath.areEqual(y, -1)) {
				return LogoEnvPLS.SOUTH_EAST;
			} else if (FastMath.areEqual(y, 0)) {
				return LogoEnvPLS.EAST;
			} else {
				return LogoEnvPLS.NORTH_EAST;
			}
		}
	}
	
	/**
	 * Gives the turtles which want to go in a specific point which aren't stopped
	 * @param turtlesInAPoint the turtles which want to go somewhere
	 * @param stoppedTurtles the turtles stopped
	 * @return the list of turtles that can continue
	 */
	private List<TurtlePLSInLogo> unstoppedTurtles (List<TurtlePLSInLogo> turtlesInAPoint, Set<TurtlePLSInLogo> stoppedTurtles) {
		List<TurtlePLSInLogo> res = new ArrayList<>();
		for (TurtlePLSInLogo t : turtlesInAPoint) {
			if (!stoppedTurtles.contains(t)) {
				res.add(t);
			}
		}
		return res;
	}
	
	private boolean isImpactedBy (TurtlePLSInLogo t1, TurtlePLSInLogo t2, Map<TurtlePLSInLogo, List<IInfluence>> influences) {
		if (t1.getCategoryOfAgent().equals(PersonCategory.CATEGORY)) {
			if (t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
				WagonPLS w = (WagonPLS) t2;
				if (w.getTypeHead().equals("train") || w.getTypeHead().equals("tram"))
					return true;
			} else if (t2.getCategoryOfAgent().equals(TrainCategory.CATEGORY) || t2.getCategoryOfAgent().equals(TramCategory.CATEGORY))
				return true;
		} else if (t1.getCategoryOfAgent().equals(BikeCategory.CATEGORY)) {
			if (t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)
							|| t2.getCategoryOfAgent().equals(TramCategory.CATEGORY)
							|| t2.getCategoryOfAgent().equals(TrainCategory.CATEGORY))
				return true;
			else if (t2.getCategoryOfAgent().equals(CarCategory.CATEGORY) || t2.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
				return !passEachOther(t1, t2, influences);
			}
		} else if (t1.getCategoryOfAgent().equals(CarCategory.CATEGORY) || t1.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
			if (t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
				WagonPLS w = (WagonPLS) t2;
				if (w.getTypeHead().equals("car")) {
					return !passEachOther(t1, t2, influences);
				} else {
					return true;
				}
			} else if (t2.getCategoryOfAgent().equals(TramCategory.CATEGORY)
							|| t2.getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
				return true;
			} else if (t2.getCategoryOfAgent().equals(CarCategory.CATEGORY) || t2.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
				return !passEachOther(t1, t2, influences);
			}
		} else if (t1.getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
			if (t2.getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
				return true;
			} else if (t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
				WagonPLS w = (WagonPLS) t2;
				if (w.getTypeHead().equals("train")) {
					return true;
				} else if (w.getTypeHead().equals("tram")) {
					return !passEachOther(t1, t2, influences);
				}
			}
		}
		return false;
	}

}