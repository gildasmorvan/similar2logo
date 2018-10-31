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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bus.BusCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bus.BusPLS;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.car.CarPLS;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.TrainCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.TramCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.TransportPLS;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.WagonCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail.WagonPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.Stop;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;

import static fr.univ_artois.lgi2a.similar2logo.examples.transport.model.TransportUtil.*;

/**
 * Reaction model of the transport simulation.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * 
 */
public class TransportReactionModel extends LogoDefaultReactionModel {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeRegularReaction(
		SimulationTimeStamp transitoryTimeMin,
		SimulationTimeStamp transitoryTimeMax,
		ConsistentPublicLocalDynamicState consistentState,
		Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
		InfluencesMap remainingInfluences
	) {
		Set<IInfluence> nonSpecificInfluences = new HashSet<>();
		Map<TurtlePLSInLogo, List<IInfluence>> turtlesInfluences = new HashMap<>();
		Map<Point2D, List<TurtlePLSInLogo>> nextPositions = new HashMap<>();
		Map<Point2D, List<TurtlePLSInLogo>> currentPositions = new HashMap<>();
		Map<TurtlePLSInLogo, Point2D> nPos = new HashMap<>();
		Set<TurtlePLSInLogo> turtlesStopped = new HashSet<>();
		// Sort the influence following their owner.
		for (IInfluence i : regularInfluencesOftransitoryStateDynamics) {
			if (ChangeDirection.CATEGORY.equals(i.getCategory())) {
				ChangeDirection cd = (ChangeDirection) i;
				TurtlePLSInLogo turtle = cd.getTarget();
				if (!turtlesInfluences.containsKey(turtle)) {
					turtlesInfluences.put(turtle, new ArrayList<>());
				}
				turtlesInfluences.get(turtle).add(cd);
			} else if (ChangeSpeed.CATEGORY.equals(i.getCategory())) {
				ChangeSpeed cs = (ChangeSpeed) i;
				TurtlePLSInLogo turtle = cs.getTarget();
				if (!turtlesInfluences.containsKey(turtle)) {
					turtlesInfluences.put(turtle, new ArrayList<>());
				}
				turtlesInfluences.get(turtle).add(cs);
			} else if (Stop.CATEGORY.equals(i.getCategory())) {
				Stop s = (Stop) i;
				TurtlePLSInLogo turtle = s.getTarget();
				if (!turtlesInfluences.containsKey(turtle)) {
					turtlesInfluences.put(turtle, new ArrayList<>());
				}
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
			if (CarCategory.CATEGORY.equals(t.getCategoryOfAgent())) {
				CarPLS c = (CarPLS) t;
				for (int i =0; i < c.getCurrentSize() -1; i++) {
					WagonPLS w = c.getWagon(i);
					if (!currentPositions.containsKey(w.getLocation())) {
						currentPositions.put(w.getLocation(), new ArrayList<>());
					}
					currentPositions.get(w.getLocation()).add(w);
				}
			} else if (BusCategory.CATEGORY.equals(t.getCategoryOfAgent())) {
				BusPLS c = (BusPLS) t;
				for (int i =0; i < c.getCurrentSize() -1; i++) {
					WagonPLS w = c.getWagon(i);
					if (!currentPositions.containsKey(w.getLocation())) {
						currentPositions.put(w.getLocation(), new ArrayList<>());
					}
					currentPositions.get(w.getLocation()).add(w);
				}
			} else if (TramCategory.CATEGORY.equals(t.getCategoryOfAgent())
					|| TrainCategory.CATEGORY.equals(t.getCategoryOfAgent())) {
				TransportPLS c = (TransportPLS) t;
				for (int i =0; i < c.getCurrentSize() -1; i++) {
					WagonPLS w = c.getWagon(i);
					if (!currentPositions.containsKey(w.getLocation())) {
						currentPositions.put(w.getLocation(), new ArrayList<>());
					}
					currentPositions.get(w.getLocation()).add(w);
				}
			}
			Point2D pos = calculateNextPositionOfTrain(t, turtlesInfluences.get(t));
			nPos.put(t, pos);
			if (!nextPositions.containsKey(pos)) {
				nextPositions.put(pos, new ArrayList<>());
			}
			nextPositions.get(pos).add(t);
		}
		//We block the turtles that can't move because of the wagons
		Set<TurtlePLSInLogo> newBlocked = new HashSet<>();
		for (TurtlePLSInLogo t : turtlesInfluences.keySet()) {
			if (!turtlesStopped.contains(t) && currentPositions.containsKey(nPos.get(t))) {
				for (TurtlePLSInLogo t2 : currentPositions.get(nPos.get(t))) {
					boolean dontBlockMyself = true;
					if (WagonCategory.CATEGORY.equals(t2.getCategoryOfAgent())) {
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
			if (!WagonCategory.CATEGORY.equals(t.getCategoryOfAgent())) {
				for (IInfluence i : turtlesInfluences.get(t)) {
					if (ChangeSpeed.CATEGORY.equals(i.getCategory())) {
						nonSpecificInfluences.remove(i);
					}
				}
				nonSpecificInfluences.add(new Stop(transitoryTimeMin, transitoryTimeMax, t));
				turtlesStopped.add(t);
			}
		}
		//We determine which turtles moves
		Set<TurtlePLSInLogo> turtleToMove = new HashSet<>();
		newBlocked = new HashSet<>();
		for (Point2D p : nextPositions.keySet()) {
			if (nextPositions.get(p).size() > 1) {
				Set<TurtlePLSInLogo> safe = getPriority(
					nextPositions.get(p),
					turtlesInfluences,
					turtlesStopped
				);
				for (TurtlePLSInLogo t : nextPositions.get(p)) {
					if (safe.contains(t)) {
						turtleToMove.add(t);
					} else {
						newBlocked.add(t);
					}
				}
			} else {
				if (!turtlesStopped.contains(nextPositions.get(p).get(0))) {
					turtleToMove.add(nextPositions.get(p).get(0));
				}
			}
		}
		for (TurtlePLSInLogo t : newBlocked) {
			for (IInfluence i : turtlesInfluences.get(t)) {
				if (ChangeSpeed.CATEGORY.equals(i.getCategory())) {
					nonSpecificInfluences.remove(i);
				}
			}
			nonSpecificInfluences.add(new Stop(transitoryTimeMin, transitoryTimeMax, t));
			turtlesStopped.add(t);
		}
		//We add the movement of the wagons
		List<IInfluence> wagonsMovments = moveWagons(
			transitoryTimeMin,
			transitoryTimeMax,
			turtleToMove,
			turtlesStopped
		);
		for (IInfluence i : wagonsMovments) {
			nonSpecificInfluences.add(i);
		}
		// Creates the new wagons if it's possible
		if (transitoryTimeMin.getIdentifier() != 0) {
			Set<IInfluence> newWagons = createNewWagons(
				transitoryTimeMin,
				transitoryTimeMax,
				turtleToMove,
				turtlesInfluences
			);
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
	
}
	