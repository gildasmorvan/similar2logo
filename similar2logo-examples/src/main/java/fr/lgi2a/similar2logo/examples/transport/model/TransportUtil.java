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
package fr.lgi2a.similar2logo.examples.transport.model;

import static fr.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
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
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.kernel.tools.MathUtil;
import fr.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.random.PRNG;

/**
 * A collection of useful functions for the transport simulation.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public final class TransportUtil {

	/**
	 * Calculate the next position of a train following its influences.
	 * 
	 * @param turtle
	 *            the train
	 * @param influences
	 *            the influences of the train
	 * @return the next position of the train
	 */
	public static Point2D calculateNextPositionOfTrain(TurtlePLSInLogo turtle, Iterable<IInfluence> influences) {
		ChangeDirection cd = null;
		Stop st = null;
		for (IInfluence influence : influences) {
			if (ChangeDirection.CATEGORY.equals(influence.getCategory())) {
				cd = (ChangeDirection) influence;
			} else if (Stop.CATEGORY.equals(influence.getCategory())) {
				st = (Stop) influence;
			}
		}
		Point2D position = new Point2D.Double(
			turtle.getLocation().getX(),
			turtle.getLocation().getY()
		);
		if (cd != null) {
			position = new Point2D.Double(
				cd.getTarget().getLocation().getX(),
				cd.getTarget().getLocation().getY()
			);
			if (st == null) {
				double direction = turtle.getDirection() + cd.getDd();
				double x = position.getX();
				double y = position.getY();
				if (MathUtil.areEqual(direction, LogoEnvPLS.EAST)) {
					position.setLocation(x + 1, y);
				} else if (MathUtil.areEqual(direction, LogoEnvPLS.NORTH)) {
					position.setLocation(x, y + 1);
				} else if (MathUtil.areEqual(direction, LogoEnvPLS.NORTH_EAST)) {
					position.setLocation(x + 1, y + 1);
				} else if (MathUtil.areEqual(direction, LogoEnvPLS.NORTH_WEST)) {
					position.setLocation(x - 1, y + 1);
				} else if (MathUtil.areEqual(direction, LogoEnvPLS.SOUTH )) {
					position.setLocation(x, y - 1);
				} else if (MathUtil.areEqual(direction, LogoEnvPLS.SOUTH_EAST)) {
					position.setLocation(x + 1, y - 1);
				} else if (MathUtil.areEqual(direction, LogoEnvPLS.SOUTH_WEST)) {
					position.setLocation(x - 1, y - 1);
				} else {
					position.setLocation(x - 1, y);
				}
			}
		} else if (st != null){
			position = new Point2D.Double(
				st.getTarget().getLocation().getX(),
				st.getTarget().getLocation().getY()
			);
		}
		return position;
	}

	/**
	 * Gives the priority turtles in case of conflict. The trains have priority on
	 * the trams that have priority on the cars
	 * @param turtles a list of turtles
	 * @param influences the influences of the turtles
	 * @param stoppedTurtles the turtles stopped previously in the reaction model        
	 * @return the turtles that have the priority.
	 */
	public static Set<TurtlePLSInLogo> getPriority(
		List<TurtlePLSInLogo> turtles,
		Map<TurtlePLSInLogo,
		List<IInfluence>> influences,
		Set<TurtlePLSInLogo> stoppedTurtles
	) {
		Set<TurtlePLSInLogo> res = new HashSet<>();
		for (int i = 0; i < turtles.size(); i++) {
			boolean noConflict = true;
			if (stoppedTurtles.contains(turtles.get(i))) {
				noConflict = false;
			} else {
				if (PersonCategory.CATEGORY.equals(turtles.get(i).getCategoryOfAgent())) {
					for (int j = 0; j < turtles.size(); j++) {
						if (noConflict && i != j) {
							if (WagonCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								WagonPLS w = (WagonPLS) turtles.get(j);
								if (TRAIN.equals(w.getTypeHead()) || TRAM.equals(w.getTypeHead())) {
									noConflict = false;
								}
							} else if (TrainCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								noConflict = false;
							} else if (TramCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								noConflict = false;
							}
						}
					}
				} else if (BikeCategory.CATEGORY.equals(turtles.get(i).getCategoryOfAgent())) {
					for (int j = 0; j < turtles.size(); j++) {
						if (noConflict && i != j) {
							if (WagonCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())
									|| TramCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())
									|| TrainCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								noConflict = false;
							} else if (CarCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent()) ||
									BusCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								noConflict &= canPassEachOther(turtles.get(i), turtles.get(j), influences);
							}
						}
					}
				} else if (CarCategory.CATEGORY.equals(turtles.get(i).getCategoryOfAgent()) ||
						BusCategory.CATEGORY.equals(turtles.get(i).getCategoryOfAgent())) {
					for (int j = 0; j < turtles.size(); j++) {
						if (noConflict && i != j) {
							if (WagonCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								WagonPLS w = (WagonPLS) turtles.get(j);
								if (CAR.equals(w.getTypeHead())) {
									if (!canPassEachOther(turtles.get(i), turtles.get(j), influences)) {
										noConflict = false;
									}
								} else {
									noConflict = false;
								}
							} else if (TramCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())
									|| TrainCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								noConflict = false;
							} else if (CarCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent()) 
								  || BusCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								noConflict &= canPassEachOther(turtles.get(i), turtles.get(j), influences);
							}
						}
					}
				} else if (TramCategory.CATEGORY.equals(turtles.get(i).getCategoryOfAgent())) {
					for (int j = 0; j < turtles.size(); j++) {
						if (noConflict && i != j) {
							if (TrainCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								noConflict = false;
							} else if (WagonCategory.CATEGORY.equals(turtles.get(j).getCategoryOfAgent())) {
								WagonPLS w = (WagonPLS) turtles.get(j);
								if (TRAIN.equals(w.getTypeHead())) {
									noConflict = false;
								} else if (TRAM.equals(w.getTypeHead())) {
									noConflict &= canPassEachOther(turtles.get(i), turtles.get(j), influences);
								}
							}
						}
					}
				}
			}
			if (noConflict) {
				res.add(turtles.get(i));
			}
		}
		if (res.isEmpty()) {
			List<TurtlePLSInLogo> remain = unstoppedTurtles(turtles, stoppedTurtles);
			if (!remain.isEmpty()) {
				res.add(remain.get(PRNG.get().randomInt(remain.size())));
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
	public static boolean canPassEachOther(TurtlePLSInLogo t1, TurtlePLSInLogo t2, Map<TurtlePLSInLogo, List<IInfluence>> influences) {
		if (BikeCategory.CATEGORY.equals(t1.getCategoryOfAgent())
		 || BikeCategory.CATEGORY.equals(t2.getCategoryOfAgent())) {
			return true;
		}
		double t1Direction = t1.getDirection();
		double t2Direction = t2.getDirection();
		if (!WagonCategory.CATEGORY.equals(t1.getCategoryOfAgent())) {
			for (IInfluence i : influences.get(t1)) {
				if (ChangeDirection.CATEGORY.equals(i.getCategory())) {
					ChangeDirection cd = (ChangeDirection) i;
					t1Direction += cd.getDd();
				}
			}
		}
		if (!WagonCategory.CATEGORY.equals(t2.getCategoryOfAgent())) {
			for (IInfluence i : influences.get(t2)) {
				if (ChangeDirection.CATEGORY.equals(i.getCategory())) {
					ChangeDirection cd = (ChangeDirection) i;
					t2Direction += cd.getDd();
				}
			}
		}
		return ((MathUtil.areEqual(t1Direction, LogoEnvPLS.NORTH) && (MathUtil.areEqual(t2Direction, LogoEnvPLS.SOUTH)
				|| MathUtil.areEqual(t2Direction, LogoEnvPLS.SOUTH_EAST) || MathUtil.areEqual(t2Direction, LogoEnvPLS.SOUTH_WEST)))
				|| (MathUtil.areEqual(t1Direction, LogoEnvPLS.NORTH_EAST) && (MathUtil.areEqual(t2Direction, LogoEnvPLS.SOUTH_WEST)
						|| MathUtil.areEqual(t2Direction, LogoEnvPLS.WEST) || MathUtil.areEqual(t2Direction, LogoEnvPLS.SOUTH)))
				|| (MathUtil.areEqual(t1Direction, LogoEnvPLS.EAST) && (MathUtil.areEqual(t2Direction, LogoEnvPLS.WEST)
						|| MathUtil.areEqual(t2Direction, LogoEnvPLS.NORTH_WEST) || MathUtil.areEqual(t2Direction, LogoEnvPLS.SOUTH_WEST)))
				|| (MathUtil.areEqual(t1Direction, LogoEnvPLS.SOUTH_EAST) && (MathUtil.areEqual(t2Direction, LogoEnvPLS.NORTH_WEST)
						|| MathUtil.areEqual(t2Direction, LogoEnvPLS.NORTH) || MathUtil.areEqual(t2Direction, LogoEnvPLS.WEST)))
				|| (MathUtil.areEqual(t1Direction, LogoEnvPLS.SOUTH) && (MathUtil.areEqual(t2Direction, LogoEnvPLS.NORTH)
						|| MathUtil.areEqual(t2Direction, LogoEnvPLS.NORTH_EAST) || MathUtil.areEqual(t2Direction, LogoEnvPLS.NORTH_WEST)))
				|| (MathUtil.areEqual(t1Direction, LogoEnvPLS.SOUTH_WEST) && (MathUtil.areEqual(t2Direction, LogoEnvPLS.NORTH_EAST)
						|| MathUtil.areEqual(t2Direction, LogoEnvPLS.NORTH) || MathUtil.areEqual(t2Direction, LogoEnvPLS.EAST)))
				|| (MathUtil.areEqual(t1Direction, LogoEnvPLS.WEST) && (MathUtil.areEqual(t2.getDirection(), LogoEnvPLS.EAST)
						|| MathUtil.areEqual(t2Direction, LogoEnvPLS.SOUTH_EAST) || MathUtil.areEqual(t2Direction, LogoEnvPLS.NORTH_EAST)))
				|| (MathUtil.areEqual(t1Direction, LogoEnvPLS.NORTH_WEST) && (MathUtil.areEqual(t2Direction, LogoEnvPLS.SOUTH_EAST)
						|| MathUtil.areEqual(t2Direction, LogoEnvPLS.SOUTH) || MathUtil.areEqual(t2Direction, LogoEnvPLS.EAST)))
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
	public static List<IInfluence> moveWagons(
		SimulationTimeStamp timeMin,
		SimulationTimeStamp timeMax,
		Iterable<TurtlePLSInLogo> turtlesToMove,
		Iterable<TurtlePLSInLogo> stoppedTurtles
	) {
		List<IInfluence> influences = new ArrayList<>();
		// We ask to all the wagon to move
		for (TurtlePLSInLogo turtle : turtlesToMove) {
			if (TrainCategory.CATEGORY.equals(turtle.getCategoryOfAgent())
				|| TramCategory.CATEGORY.equals(turtle.getCategoryOfAgent())
			) {
				TransportPLS trPLS = (TransportPLS) turtle;
				double direction = trPLS.getDirection();
				for (int i = 0; i < trPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = trPLS.getWagon(i);
					if (i == 0) {
						influences.add(
							new ChangeDirection(
								timeMin,
								timeMax,
								-wagon.getDirection() + getDirection(wagon.getLocation(),trPLS.getLocation()),
								wagon
							)
						);
						influences.add(
							new ChangeSpeed(
								timeMin,
								timeMax,
								-wagon.getSpeed() + distanceToDo(direction),
								wagon
							)
						);
					} else {
						influences.add(
							new ChangeDirection(
								timeMin,
								timeMax,
								-wagon.getDirection() + getDirection(
									wagon.getLocation(), trPLS.getWagon(i - 1).getLocation()
								),
								wagon
							)
						);
						influences.add(
							new ChangeSpeed(
								timeMin,
								timeMax, 
								-wagon.getSpeed() + distanceToDo(trPLS.getWagon(i-1).getDirection()),
								wagon
							)
						);
					}
				}
			} else if (CarCategory.CATEGORY.equals(turtle.getCategoryOfAgent())) {
				CarPLS cPLS = (CarPLS) turtle;
				for (int i = 0; i < cPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = cPLS.getWagon(i);
					if (i == 0) {
						double direction = getDirection(wagon.getLocation(), cPLS.getLocation());
						influences.add(
							new ChangeDirection(timeMin, timeMax, -wagon.getDirection() + direction, wagon)
						);
						influences.add(
							new ChangeSpeed(timeMin, timeMax, -wagon.getSpeed() + distanceToDo(direction), wagon)
						);
					} else {
						double direction = cPLS.getWagon(i-1).getDirection();
						influences.add(
							new ChangeDirection(timeMin, timeMax, -wagon.getDirection()+ direction, wagon)
						);
						influences.add(
							new ChangeSpeed(timeMin, timeMax,  -wagon.getSpeed() + distanceToDo(direction), wagon)
						);
					}
				}
			} else if (BusCategory.CATEGORY.equals(turtle.getCategoryOfAgent())) {
				BusPLS bPLS = (BusPLS) turtle;
				for (int i = 0; i < bPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = bPLS.getWagon(i);
					if (i == 0) {
						double direction = getDirection(wagon.getLocation(), bPLS.getLocation());
						influences.add(
							new ChangeDirection(timeMin, timeMax, -wagon.getDirection() + direction, wagon)
						);
						influences.add(
							new ChangeSpeed(timeMin, timeMax, -wagon.getSpeed() + distanceToDo(direction), wagon)
						);
					} else {
						double direction = bPLS.getWagon(i-1).getDirection();
						influences.add(
							new ChangeDirection(timeMin, timeMax, -wagon.getDirection()+ direction, wagon)
						);
						influences.add(
							new ChangeSpeed(timeMin, timeMax,  -wagon.getSpeed() + distanceToDo(direction), wagon)
						);
					}
				}
			}
		}
		for (TurtlePLSInLogo turtle : stoppedTurtles) {
			if (TrainCategory.CATEGORY.equals(turtle.getCategoryOfAgent())
			 || TramCategory.CATEGORY.equals(turtle.getCategoryOfAgent())) {
				TransportPLS trPLS = (TransportPLS) turtle;
				for (int i = 0; i < trPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = trPLS.getWagon(i);
					influences.add(new Stop(timeMin, timeMax, wagon));
				}
			} else if (CarCategory.CATEGORY.equals(turtle.getCategoryOfAgent())) {
				CarPLS cPLS = (CarPLS) turtle;
				for (int i = 0; i < cPLS.getCurrentSize()-1; i++) {
					WagonPLS wagon = cPLS.getWagon(i);
					influences.add(new Stop(timeMin, timeMax, wagon));
				}
			} else if (BusCategory.CATEGORY.equals(turtle.getCategoryOfAgent())) {
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
	public static Set<IInfluence> createNewWagons (
		SimulationTimeStamp begin,
		SimulationTimeStamp end,
		Iterable<TurtlePLSInLogo> movingTurtles,
		Map<TurtlePLSInLogo,List<IInfluence>> influences
	) {
		Set<IInfluence> newWagons = new HashSet<>();
		for (TurtlePLSInLogo t : movingTurtles) {
			if (TramCategory.CATEGORY.equals(t.getCategoryOfAgent())) {
				TransportPLS tra = (TransportPLS) t;
				if (!tra.reachMaxSize()) {
					if (tra.getCurrentSize() == 1) {
						Point2D nextPosition = calculateNextPositionOfTrain(t, influences.get(t));
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(t.getLocation(), nextPosition),
							0,
							0,
							t.getLocation().getX(),
							t.getLocation().getY(),
							t,
							TRAM
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} else if (tra.getCurrentSize() == 2) {
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(tra.getWagon(0).getLocation(),t.getLocation()),
							0,
							0,
							tra.getWagon(0).getLocation().getX(),
							tra.getWagon(0).getLocation().getY(),
							t,
							TRAM
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} else {
						int size = tra.getCurrentSize();
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(), WagonCategory.CATEGORY,
							getDirection(tra.getWagon(size-2).getLocation(), tra.getWagon(size-3).getLocation()),
							0,
							0,
							tra.getWagon(size-2).getLocation().getX(),
							tra.getWagon(size-2).getLocation().getY(),
							t,
							TRAM
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} 
				}
			} else if (TrainCategory.CATEGORY.equals(t.getCategoryOfAgent())) {
				TransportPLS tra = (TransportPLS) t;
				if (!tra.reachMaxSize()) {
					if (tra.getCurrentSize() == 1) {
						Point2D nextPosition = calculateNextPositionOfTrain(t, influences.get(t));
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(t.getLocation(), nextPosition),
							0,
							0,
							t.getLocation().getX(),
							t.getLocation().getY(),
							t,
							TRAIN
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} else if (tra.getCurrentSize() == 2) {
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(tra.getWagon(0).getLocation(), t.getLocation()),
							0,
							0,
							tra.getWagon(0).getLocation().getX(),
							tra.getWagon(0).getLocation().getY(),
							t,
							TRAIN
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					} else {
						int size = tra.getCurrentSize();
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(tra.getWagon(size-2).getLocation(), tra.getWagon(size-3).getLocation()),
							0,
							0,
							tra.getWagon(size-2).getLocation().getX(),
							tra.getWagon(size-2).getLocation().getY(),
							t,
							TRAIN
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						tra.addWagon(w);
					}
				}
			} else if (CarCategory.CATEGORY.equals(t.getCategoryOfAgent())) {
				CarPLS c = (CarPLS) t;
				if (!c.reachMaxSize()) {
					if (c.getCurrentSize() == 1) {
						Point2D nextPosition = calculateNextPositionOfTrain(t, influences.get(t));
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(t.getLocation(), nextPosition),
							0,
							0,
							t.getLocation().getX(),
							t.getLocation().getY(),
							t,
							CAR
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						c.addWagon(w);
					} else if (c.getCurrentSize() == 2) {
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(c.getWagon(0).getLocation(), t.getLocation()),
							0,
							0,
							c.getWagon(0).getLocation().getX(),
							c.getWagon(0).getLocation().getY(),
							t,
							CAR
						);
						newWagons.add(new SystemInfluenceAddAgent(
							LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						c.addWagon(w);
					} else {
						int size = c.getCurrentSize();
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(), WagonCategory.CATEGORY,
							getDirection(c.getWagon(size-2).getLocation(), c.getWagon(size-3).getLocation()),
							0,
							0,
							c.getWagon(size-2).getLocation().getX(),
							c.getWagon(size-2).getLocation().getY(),
							t,
							CAR
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						c.addWagon(w);
					}
				}
			} else if (BusCategory.CATEGORY.equals(t.getCategoryOfAgent())) {
				BusPLS b = (BusPLS) t;
				if (!b.reachMaxSize()) {
					if (b.getCurrentSize() == 1) {
						Point2D nextPosition = calculateNextPositionOfTrain(t, influences.get(t));
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(t.getLocation(), nextPosition),
							0,
							0,
							t.getLocation().getX(),
							t.getLocation().getY(),
							t,
							CAR
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						b.addWagon(w);
					} else if (b.getCurrentSize() == 2) {
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(b.getWagon(0).getLocation(), t.getLocation()),
							0,
							0,
							b.getWagon(0).getLocation().getX(),
							b.getWagon(0).getLocation().getY(),
							t,
							CAR
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
						WagonPLS w = (WagonPLS) ea.getPublicLocalState(LogoSimulationLevelList.LOGO);
						b.addWagon(w);
					} else {
						int size = b.getCurrentSize();
						ExtendedAgent ea = WagonFactory.generate(
							new ConeBasedPerceptionModel(Math.sqrt(2), Math.PI, true, true, true),
							new WagonDecisionModel(),
							WagonCategory.CATEGORY,
							getDirection(b.getWagon(size-2).getLocation(), b.getWagon(size-3).getLocation()),
							0,
							0,
							b.getWagon(size-2).getLocation().getX(),
							b.getWagon(size-2).getLocation().getY(),
							t,
							CAR
						);
						newWagons.add(
							new SystemInfluenceAddAgent(LogoSimulationLevelList.LOGO, begin, end, ea)
						);
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
	public static double distanceToDo(double radius) {
		if (MathUtil.areEqual(radius, LogoEnvPLS.NORTH_EAST) 
		 || MathUtil.areEqual(radius, LogoEnvPLS.NORTH_WEST)
		 || MathUtil.areEqual(radius, LogoEnvPLS.SOUTH_EAST)
		 || MathUtil.areEqual(radius, LogoEnvPLS.SOUTH_WEST)) {
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
	public static double getDirection(Point2D me, Point2D previous) {
		double x = previous.getX() - me.getX();
		double y = previous.getY() - me.getY();
		if (MathUtil.areEqual(x,-1)) {
			if (MathUtil.areEqual(y, -1)) {
				return LogoEnvPLS.SOUTH_WEST;
			} else if (MathUtil.areEqual(y, 0)) {
				return LogoEnvPLS.WEST;
			} else {
				return LogoEnvPLS.NORTH_WEST;
			}
		} else if (MathUtil.areEqual(x, 0)) {
			if (MathUtil.areEqual(y, -1)) {
				return LogoEnvPLS.SOUTH;
			} else {
				return LogoEnvPLS.NORTH;
			}
		} else {
			if (MathUtil.areEqual(y, -1)) {
				return LogoEnvPLS.SOUTH_EAST;
			} else if (MathUtil.areEqual(y, 0)) {
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
	public static List<TurtlePLSInLogo> unstoppedTurtles (
		Iterable<TurtlePLSInLogo> turtlesInAPoint,
		Collection<TurtlePLSInLogo> stoppedTurtles
	) {
		List<TurtlePLSInLogo> res = new ArrayList<>();
		for (TurtlePLSInLogo t : turtlesInAPoint) {
			if (!stoppedTurtles.contains(t)) {
				res.add(t);
			}
		}
		return res;
	}
	
	public static boolean isImpactedBy (
		TurtlePLSInLogo t1,
		TurtlePLSInLogo t2,
		Map<TurtlePLSInLogo,
		List<IInfluence>> influences
	) {
		if (t1.getCategoryOfAgent().equals(PersonCategory.CATEGORY)) {
			if (t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
				WagonPLS w = (WagonPLS) t2;
				if (TRAIN.equals(w.getTypeHead()) || TRAM.equals(w.getTypeHead())) {
					return true;
				}
			} else if (t2.getCategoryOfAgent().equals(TrainCategory.CATEGORY)
				    || t2.getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
				return true;
			}
		} else if (t1.getCategoryOfAgent().equals(BikeCategory.CATEGORY)) {
			if (t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)
			 || t2.getCategoryOfAgent().equals(TramCategory.CATEGORY)
			 || t2.getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
				return true;
			} else if (t2.getCategoryOfAgent().equals(CarCategory.CATEGORY) 
				    || t2.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
				return !canPassEachOther(t1, t2, influences);
			}
		} else if (t1.getCategoryOfAgent().equals(CarCategory.CATEGORY)
			    || t1.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
			if (t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
				WagonPLS w = (WagonPLS) t2;
				if (CAR.equals(w.getTypeHead())) {
					return !canPassEachOther(t1, t2, influences);
				} else {
					return true;
				}
			} else if (t2.getCategoryOfAgent().equals(TramCategory.CATEGORY)
					|| t2.getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
				return true;
			} else if (t2.getCategoryOfAgent().equals(CarCategory.CATEGORY)
					|| t2.getCategoryOfAgent().equals(BusCategory.CATEGORY)) {
				return !canPassEachOther(t1, t2, influences);
			}
		} else if (t1.getCategoryOfAgent().equals(TramCategory.CATEGORY)) {
			if (t2.getCategoryOfAgent().equals(TrainCategory.CATEGORY)) {
				return true;
			} else if (t2.getCategoryOfAgent().equals(WagonCategory.CATEGORY)) {
				WagonPLS w = (WagonPLS) t2;
				if (TRAIN.equals(w.getTypeHead())) {
					return true;
				} else if (TRAM.equals(w.getTypeHead())) {
					return !canPassEachOther(t1, t2, influences);
				}
			}
		}
		return false;
	}

}
