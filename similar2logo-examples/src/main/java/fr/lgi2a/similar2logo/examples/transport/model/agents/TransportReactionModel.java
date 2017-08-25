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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;

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
		List<TurtlePLSInLogo> turtlesStopped = new ArrayList<>();
		// Sort the influence following their owner.
		for (IInfluence i : regularInfluencesOftransitoryStateDynamics) {
			if (i.getCategory().equals("change direction")) {
				ChangeDirection cd = (ChangeDirection) i;
				TurtlePLSInLogo turtle = cd.getTarget();
				if (!turtle.getCategoryOfAgent().equals(PersonCategory.CATEGORY)) {
					if (!turtlesInfluences.containsKey(turtle))
						turtlesInfluences.put(turtle, new ArrayList<>());
					turtlesInfluences.get(turtle).add(cd);
				}
			} else if (i.getCategory().equals("change speed")) {
				ChangeSpeed cs = (ChangeSpeed) i;
				TurtlePLSInLogo turtle = cs.getTarget();
				if (!turtle.getCategoryOfAgent().equals(PersonCategory.CATEGORY)) {
					if (!turtlesInfluences.containsKey(turtle))
						turtlesInfluences.put(turtle, new ArrayList<>());
					turtlesInfluences.get(turtle).add(cs);
				}
			} else if (i.getCategory().equals("stop")) {
				Stop s = (Stop) i;
				TurtlePLSInLogo turtle = s.getTarget();
				if (!turtle.getCategoryOfAgent().equals(PersonCategory.CATEGORY)) {
					if (!turtlesInfluences.containsKey(turtle))
						turtlesInfluences.put(turtle, new ArrayList<>());
					turtlesInfluences.get(turtle).add(s);
					turtlesStopped.add(turtle);
				}
			}
			nonSpecificInfluences.add(i);
		}
		//We add the wagons movement
		List<IInfluence> wagonsInfluences = this.moveWagons(transitoryTimeMin, transitoryTimeMax, turtlesInfluences.keySet(), turtlesStopped);
		for (IInfluence i : wagonsInfluences) {
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
		// We determine where the turtles will be in the next turn
		for (TurtlePLSInLogo t : turtlesInfluences.keySet()) {
			Point2D pos = calculateNextPosition(t, turtlesInfluences.get(t));
			if (!nextPositions.containsKey(pos))
				nextPositions.put(pos, new ArrayList<>());
			nextPositions.get(pos).add(t);
		}
		// We blocked vehicles behind stoped elements
		for (TurtlePLSInLogo t : turtlesStopped) {
			dominoEffect(t, transitoryTimeMin, transitoryTimeMax, nonSpecificInfluences, turtlesInfluences,
					nextPositions, t.getLocation(), new ArrayList<>());
		}
		// When several turtles want to go at the same place
		for (Point2D p : nextPositions.keySet()) {
			if (nextPositions.get(p).size() > 1) {
				// We check if the vehicle aren't face to face, if it's the
				// case, we don't stop them
				if (nextPositions.get(p).size() == 2) {
					if (inConflict(nextPositions.get(p).get(0),
							nextPositions.get(p).get(1))) {
						List<TurtlePLSInLogo> win = getPriority(nextPositions.get(p));
						TurtlePLSInLogo lost = nextPositions.get(p).get(0);
						if (nextPositions.get(p).get(0).equals(win)) { lost = nextPositions.get(p).get(1);}
						nonSpecificInfluences.add(new Stop(transitoryTimeMin, transitoryTimeMax, lost));
						for (IInfluence i : turtlesInfluences.get(lost)) {
							if (i.getCategory().equals("change speed"))
								nonSpecificInfluences.remove(i);
						}
						dominoEffect(lost, transitoryTimeMin, transitoryTimeMax, nonSpecificInfluences, turtlesInfluences,
								nextPositions, lost.getLocation(), new ArrayList<>());
					}
					// if there are more than 2 vehicles, we choose randomly a
					// vehicle to let go.
				} else {
					List<TurtlePLSInLogo> safe = getPriority(nextPositions.get(p));
					for (int j = 0; j < nextPositions.get(p).size(); j++) {
						if (!safe.contains(nextPositions.get(p).get(j))) {
							TurtlePLSInLogo turtle = nextPositions.get(p).get(j);
							for (IInfluence i : turtlesInfluences.get(turtle)) {
								if (i.getCategory().equals("change speed"))
									nonSpecificInfluences.remove(i);
							}
							nonSpecificInfluences.add(new Stop(transitoryTimeMin, transitoryTimeMax, turtle));
							dominoEffect(turtle, transitoryTimeMin, transitoryTimeMax, nonSpecificInfluences, turtlesInfluences,
									nextPositions, turtle.getLocation(), new ArrayList<>());
						}
					}
				}
			}
		}
		//Creates the new wagons if it's possible
		this.createNewWagons(transitoryTimeMin, transitoryTimeMax, nonSpecificInfluences);
		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax, consistentState, nonSpecificInfluences,
				remainingInfluences);
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
			position = new Point2D.Double(cd.getTarget().getLocation().getX(), cd.getTarget().getLocation().getY());
		} else {
			position = new Point2D.Double(st.getTarget().getLocation().getX(), st.getTarget().getLocation().getY());
		}
		if (st == null) {
			double direction = turtle.getDirection() + cd.getDd();
			double x = position.getX();
			double y = position.getY();
			if (direction == LogoEnvPLS.EAST)
				position.setLocation(x + 1, y);
			else if (direction == LogoEnvPLS.NORTH)
				position.setLocation(x, y + 1);
			else if (direction == LogoEnvPLS.NORTH_EAST)
				position.setLocation(x + 1, y + 1);
			else if (direction == LogoEnvPLS.NORTH_WEST)
				position.setLocation(x - 1, y + 1);
			else if ((direction == LogoEnvPLS.SOUTH) || (direction == (-1 * LogoEnvPLS.SOUTH)))
				position.setLocation(x, y - 1);
			else if (direction == LogoEnvPLS.SOUTH_EAST)
				position.setLocation(x + 1, y - 1);
			else if (direction == LogoEnvPLS.SOUTH_WEST)
				position.setLocation(x - 1, y - 1);
			else
				position.setLocation(x - 1, y);
		}
		return position;
	}

	/**
	 * Indicates if two turtles will be in conflict if they're on the same patch
	 * @param t1 first turtle
	 * @param t2 second turtle
	 * @return true if the two turtles will be in conflict, false else.
	 */
	private boolean inConflict(TurtlePLSInLogo t1, TurtlePLSInLogo t2) {
		return (!t1.getCategoryOfAgent().equals(t2.getCategoryOfAgent()) || 
				(!(t1.getLocation().distance(t2.getLocation()) > Math.sqrt(2))));
	}

	/**
	 * Propagates the stop effect to the vehicles behind
	 * 
	 * @param begin
	 *            the time stamp of start
	 * @param end
	 *            the time stamp of end
	 * @param remainsInfluences
	 *            the influences that remains
	 * @param turtlesInfluences
	 *            the influences of each turtles
	 * @param nextPositions
	 *            the next position of each turtles
	 * @param pos
	 *            the current position where there is a problem
	 * @param alreadyVisited the positions where the domino effect already went
	 */
	private void dominoEffect(TurtlePLSInLogo turtle, SimulationTimeStamp begin, SimulationTimeStamp end, Set<IInfluence> remainsInfluences,
			Map<TurtlePLSInLogo, List<IInfluence>> turtlesInfluences, Map<Point2D, List<TurtlePLSInLogo>> nextPositions,
			Point2D pos, List<Point2D> alreadyVisited) {
		alreadyVisited.add(pos);
		if (nextPositions.containsKey(pos)) {
			for (TurtlePLSInLogo t : nextPositions.get(pos)) {
				if (!alreadyVisited.contains(t.getLocation()) && !passEachOther(t, turtle, nextPositions)) {
					for (IInfluence i : turtlesInfluences.get(t)) {
						if (i.getCategory().equals("change speed"))
							remainsInfluences.remove(i);
					}
					remainsInfluences.add(new Stop(begin, end, t));
					dominoEffect(t, begin, end, remainsInfluences, turtlesInfluences, 
							nextPositions, t.getLocation(), alreadyVisited );
				}
			}
		}
	}

	/**
	 * Gives the priority turtles in case of conflict.
	 * The trains have priority on the trams that have priority on the cars
	 * @param turtles a list of turtles
	 * @return the turtles that have the priority.
	 */
	private List<TurtlePLSInLogo> getPriority(List<TurtlePLSInLogo> turtles) {
		List<TurtlePLSInLogo> res = new ArrayList<>();
		boolean wagonTransport = false;
		boolean train = false;
		boolean tram = false;
		for (int i =0; i < turtles.size(); i++) {
			if (turtles.get(i).getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
				WagonPLS wagon = (WagonPLS) turtles.get(i);
				if (wagon.getTypeHead().equals("transport")) {
					wagonTransport = true;
				}
				res.add(turtles.get(i));
			}
		}
		if (!wagonTransport) {
			for (int i = 0; i < turtles.size(); i++) {
				if (turtles.get(i).getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
					res.add(turtles.get(i));
					train = true;
				}
			}
		}
		if (!(wagonTransport || train)) {
			for (int i = 0; i < turtles.size(); i++) {
				if (turtles.get(i).getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
					res.add(turtles.get(i));
					tram = true;
				}
			}
		}
		if (!(wagonTransport || train || tram)) {
			List<List<TurtlePLSInLogo>> nonConflictTurtles = new ArrayList<>();
			Random r = new Random();
			for (int j = 0; j < turtles.size(); j++) {
				for (int k = j+1 ; k < turtles.size(); k++) {
					if (!inConflict(turtles.get(j), turtles.get(k))) {
						List<TurtlePLSInLogo> nonConflict = new ArrayList<>();
						nonConflict.add(turtles.get(j));
						nonConflict.add(turtles.get(k));
						nonConflictTurtles.add(nonConflict);
					}
				}
			}
			if (nonConflictTurtles.size() == 0) {
				res.add(turtles.get(r.nextInt(turtles.size())));
			} else {
				for (TurtlePLSInLogo t : nonConflictTurtles.get(r.nextInt(nonConflictTurtles.size()))) {
					res.add(t);
				}
			}
		}
		return res;
	}

	/**
	 * Indicates if 2 cars can pass each other
	 * @param t1 first turtle
	 * @param t2 second turtle
	 * @param nextPositions 
	 * @return true if the cars can pass each other, false else
	 */
	private boolean passEachOther (TurtlePLSInLogo t1, TurtlePLSInLogo t2, Map<Point2D, List<TurtlePLSInLogo>> nextPositions) {
		return (nextPositions.get(t1.getLocation()) != null && nextPositions.get(t2.getLocation()) != null
				&& nextPositions.get(t1.getLocation()).contains(t2) && nextPositions.get(t2.getLocation()).contains(t1));
	}
	
	/**
	 * Gives the influences of the wagons
	 * @param timeMin the beginning
	 * @param timeMax the end
	 * @param turtles the turtles
	 * @param stoppedTurtles the stopped turtles
	 * @return a list with the influences of the 
	 */
	private List<IInfluence> moveWagons (SimulationTimeStamp timeMin, SimulationTimeStamp timeMax,
			Set<TurtlePLSInLogo> turtles, List<TurtlePLSInLogo> stoppedTurtles) {
		List<IInfluence> influences = new ArrayList<>();
		//We ask to all the wagon to move
		for (TurtlePLSInLogo turtle : turtles) {
			if (!stoppedTurtles.contains(turtle)) {
				if (turtle.getCategoryOfAgent().equals(TrainCategory.CATEGORY) || turtle.getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
					TransportPLS trPLS = (TransportPLS) turtle;
					double direction = trPLS.getDirection();
					for (int i = 1; i < trPLS.getCurrentSize(); i++) {
						WagonPLS wagon = trPLS.getWagon(i);
						influences.add(new ChangeDirection(timeMin, timeMax, -wagon.getDirection() + direction , wagon));
						influences.add(new ChangeSpeed(timeMin, timeMax, -wagon.getSpeed()+ distanceToDo(direction), wagon));
						direction = wagon.getDirection();
					}
				} else if (turtle.getCategoryOfAgent().equals(CarCategory.CATEGORY)){
					CarPLS cPLS = (CarPLS) turtle;
					double direction = cPLS.getDirection();
					for (int i = 1; i < cPLS.getCurrentSize(); i++) {
						WagonPLS wagon = cPLS.getWagon (i);
						influences.add(new ChangeDirection(timeMin, timeMax, -wagon.getDirection() + direction , wagon));
						influences.add(new ChangeSpeed(timeMin, timeMax, -wagon.getSpeed()+ distanceToDo(direction), wagon));
						direction = wagon.getDirection();
					}
				}
			} else {
				if (turtle.getCategoryOfAgent().equals(TrainCategory.CATEGORY) || turtle.getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
					TransportPLS trPLS = (TransportPLS) turtle;
					for (int i = 1; i < trPLS.getCurrentSize(); i++) {
						WagonPLS wagon = trPLS.getWagon(i);
						influences.add(new Stop(timeMin, timeMax, wagon));
					}
				} else if (turtle.getCategoryOfAgent().equals(CarCategory.CATEGORY)){
					CarPLS cPLS = (CarPLS) turtle;
					for (int i = 1; i < cPLS.getCurrentSize(); i++) {
						WagonPLS wagon = cPLS.getWagon (i);
						influences.add(new Stop(timeMin, timeMax, wagon));
					}
				}
			}
		}
		return influences;
	}
	
	private void createNewWagons (SimulationTimeStamp beginning, SimulationTimeStamp end, Set<IInfluence> influences) {
		Set<Point2D> nextPositions = new HashSet<>();
		Map<TurtlePLSInLogo,List<IInfluence>> turtlesInfluences = new HashMap<>();
		//We reclassify the influences
		for (IInfluence i : influences) {
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
			}
		}
		//We calculate all the next position
		for (TurtlePLSInLogo turtle : turtlesInfluences.keySet()) {
			nextPositions.add(this.calculateNextPosition(turtle, turtlesInfluences.get(turtle)));
		}
		//We check all the turtles that haven't reached their maximum size
		for (TurtlePLSInLogo turtle : turtlesInfluences.keySet()) {
			if (turtle.getCategoryOfAgent().equals(CarCategory.CATEGORY) || turtle.getCategoryOfAgent().equals(TramCategory.CATEGORY) ||
					turtle.getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
				//We check the category
				if (turtle.getCategoryOfAgent().equals(TramCategory.CATEGORY) || turtle.getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
					TransportPLS tp = (TransportPLS) turtle;
					//We check if the max size has been reached or not
					if (!tp.reachMaxSize()) {
						if (tp.getCurrentSize() == 1) {
							if (!nextPositions.contains(tp.getLocation())) {
								ExtendedAgent ea = WagonFactory.generate(
										new TurtlePerceptionModel(Math.sqrt(2),Math.PI,true,true,true), 
										new WagonDecisionModel(), WagonCategory.CATEGORY, 
										0, 0, 0, tp.getLocation().getX(), 
										tp.getLocation().getY(), 
										turtle, null, "transport");
								influences.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, beginning, end, ea));
								WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
								tp.setNextWagon(w);
								tp.hasOneMoreWagon();
							}
						} else {
							if (!nextPositions.contains(tp.getWagon(tp.getCurrentSize()-1))) {
								ExtendedAgent ea = WagonFactory.generate(
										new TurtlePerceptionModel(Math.sqrt(2),Math.PI,true,true,true), 
										new WagonDecisionModel(), WagonCategory.CATEGORY, 
										0, 0, 0, tp.getWagon(tp.getCurrentSize()-1).getLocation().getX(), 
										tp.getWagon(tp.getCurrentSize()-1).getLocation().getY(), 
										turtle, tp.getWagon(tp.getCurrentSize()-1), "transport");
								influences.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, beginning, end, ea));
								WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
								tp.getWagon(tp.getCurrentSize()-1).setNextWagon(w);
								tp.hasOneMoreWagon();
							}
						}
					}
				} else if (turtle.getCategoryOfAgent().equals(CarCategory.CATEGORY)) {
					CarPLS cp = (CarPLS) turtle;
					if (!cp.reachMaxSize()) {
						if (cp.currentSize == 1) {
							if (!nextPositions.contains(cp.getLocation())) {
								ExtendedAgent ea = WagonFactory.generate(
										new TurtlePerceptionModel(Math.sqrt(2),Math.PI,true,true,true), 
										new WagonDecisionModel(), WagonCategory.CATEGORY, 
										0, 0, 0, cp.getLocation().getX(), 
										cp.getLocation().getY(), 
										turtle, null, "car");
								influences.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, beginning, end, ea));
								WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
								cp.setNextWagon(w);
								cp.hasOneMoreWagon();
							}
						} else {
							if (!nextPositions.contains(cp.getWagon(cp.getCurrentSize()-1))) {
								ExtendedAgent ea = WagonFactory.generate(
										new TurtlePerceptionModel(Math.sqrt(2),Math.PI,true,true,true), 
										new WagonDecisionModel(), WagonCategory.CATEGORY, 
										0, 0, 0, cp.getWagon(cp.getCurrentSize()-1).getLocation().getX(), 
										cp.getWagon(cp.getCurrentSize()-1).getLocation().getY(), 
										turtle, cp.getWagon(cp.getCurrentSize()-1), "car");
								influences.add(new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, beginning, end, ea));
								WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
								cp.getWagon(cp.getCurrentSize()-1).setNextWagon(w);
								cp.hasOneMoreWagon();
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gives the distance to do following the direction of the train
	 * @param radius direction of the train
	 * @return the distance to do
	 */
	private double distanceToDo (double radius) {
		if (radius == LogoEnvPLS.NORTH_EAST || radius == LogoEnvPLS.NORTH_WEST || 
				radius == LogoEnvPLS.SOUTH_EAST || radius == LogoEnvPLS.SOUTH_WEST) return Math.sqrt(2);
		else return 1;
	}
}
