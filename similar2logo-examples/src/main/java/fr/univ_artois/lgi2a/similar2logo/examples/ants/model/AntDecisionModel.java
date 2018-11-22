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
package fr.univ_artois.lgi2a.similar2logo.examples.ants.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.EmitPheromone;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.RemoveMark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;

import static net.jafama.FastMath.*;

/**
 * The decision model of the ants".
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target=
 *         "_blank">Gildas Morvan</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com">Antoine Lecoutre</a>
 *
 */
public class AntDecisionModel extends AbstractAgtDecisionModel {

	/**
	 * Parameters of the simulation
	 */
	private AntSimulationParameters parameters;

	/**
	 * Position of the base in the base
	 */
	private Point2D positionBase;

	/**
	 * Position of the last food item that the agent had
	 */
	private Point2D positionFood;

	/**
	 * If the agent can detect a pheromones actually
	 */
	private boolean detectePheromones;

	/**
	 * If the agent have a food actually
	 */
	private boolean haveFood;

	private LogoEnvPLS envPLS;

	/**
	 * Constructor of the decision model
	 * 
	 * @param param
	 *            is a parameters of the simulation
	 * @param x
	 *            is a position of the base on the x axe
	 * @param y
	 *            is a position of the base on the y axe
	 */
	public AntDecisionModel(AntSimulationParameters param, double x, double y) {
		super(LogoSimulationLevelList.LOGO);
		this.parameters = param;
		this.positionBase = new Point2D.Double(x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void decide(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		IGlobalState globalState,
		ILocalStateOfAgent publicLocalState,
		ILocalStateOfAgent privateLocalState,
		IPerceivedData perceivedData,
		InfluencesMap producedInfluences
	) {
		TurtlePLSInLogo castedPublicLocalState = (TurtlePLSInLogo) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;

		boolean attraction = false;
		boolean repulsion = false;
		boolean turnAround = false;

		double sinAngle = 0;
		double cosAngle = 0;
		double dd = 0;

		this.envPLS = new LogoEnvPLS(this.parameters.gridWidth, this.parameters.gridHeight, this.parameters.xTorus,
				this.parameters.yTorus, this.parameters.pheromones);

		if (!castedPerceivedData.getMarks().isEmpty()) {
			for (LocalPerceivedData<Mark> perceivedMarks : castedPerceivedData.getMarks()) {
				if (("Base".equals(perceivedMarks.getContent().getCategory())) && (this.haveFood)) {
					if (perceivedMarks.getDistanceTo() <= this.parameters.perceptionDistanceGetFood) {
						// Ant drop a food
						this.haveFood = !this.haveFood;

						// The Food lose 1
						perceivedMarks.getContent().setContent(((double) perceivedMarks.getContent().getContent()) + 1);

						// Turn around
						dd = this.envPLS.getDirection(castedPublicLocalState.getLocation(), this.positionFood)
								- castedPublicLocalState.getDirection();

						turnAround = !turnAround;

					} else if (perceivedMarks.getDistanceTo() <= this.parameters.perceptionDistance) {

						// Set attraction true
						attraction = true;
					}
				} else if (this.haveFood) {

					// Try to detect the base pheromone
					dd = goToPheromone(castedPublicLocalState, castedPerceivedData, "Base", true, 100);

				} else if (("Food".equals(perceivedMarks.getContent().getCategory())) && (!this.haveFood)) {

					if (perceivedMarks.getDistanceTo() <= this.parameters.perceptionDistanceGetFood) {
						// Ant drop a food
						this.haveFood = !this.haveFood;

						double value = (double) perceivedMarks.getContent().getContent();
						// The Food lose 1
						perceivedMarks.getContent().setContent(value - 1);

						if (((double) perceivedMarks.getContent().getContent()) <= 0) {
							producedInfluences
									.add(new RemoveMark(timeLowerBound, timeUpperBound, perceivedMarks.getContent()));
						}

						this.positionFood = perceivedMarks.getContent().getLocation();

						// Turn around
						dd = this.envPLS.getDirection(castedPublicLocalState.getLocation(), this.positionBase)
								- castedPublicLocalState.getDirection();

					} else if (perceivedMarks.getDistanceTo() <= this.parameters.perceptionDistance) {
						// Set attraction true
						attraction = true;
					}
				}
				// Use a attraction
				if (attraction) {
					sinAngle += sin(perceivedMarks.getDirectionTo() - castedPublicLocalState.getDirection());
					cosAngle += cos(perceivedMarks.getDirectionTo() - castedPublicLocalState.getDirection());

					sinAngle /= castedPerceivedData.getTurtles().size();
					cosAngle /= castedPerceivedData.getTurtles().size();
					dd = atan2(sinAngle, cosAngle);
					if (!MathUtil.areEqual(dd, 0)) {
						if (dd > parameters.maxAngle) {
							dd = parameters.maxAngle;
						} else if (dd < -parameters.maxAngle) {
							dd = -parameters.maxAngle;
						}
					}

					// Set attraction false
					attraction = false;
				}
			}
		} else {
			// Detect the Pheromones
			List<LocalPerceivedData<Double>> l = new ArrayList<>();
				l.addAll(castedPerceivedData.getPheromones().get("Food"));
			// When food pheromones are detected by a ant
			for (LocalPerceivedData<Double> pheromone : l) {
				if (pheromone.getContent() > 1) {
					detectePheromones = true;
					break;
				}
			}
			if ((!detectePheromones) && (!castedPerceivedData.getTurtles().isEmpty()) && (!this.haveFood)) {
				for (LocalPerceivedData<TurtlePLSInLogo> perceivedTurtle : castedPerceivedData.getTurtles()) {
					if (perceivedTurtle.getContent().getCategoryOfAgent().isA(AntCategory.CATEGORY)) {
						// Push the other ants if they are regroup
						if (perceivedTurtle.getDistanceTo() <= this.parameters.perceptionDistance - 1) {
							repulsion = true;
							detectePheromones = false;
						}
					}
					if (repulsion) {
						sinAngle += sin(castedPublicLocalState.getDirection() - perceivedTurtle.getDirectionTo());
						cosAngle += cos(castedPublicLocalState.getDirection() - perceivedTurtle.getDirectionTo());

						sinAngle /= castedPerceivedData.getTurtles().size();
						cosAngle /= castedPerceivedData.getTurtles().size();
						dd = atan2(sinAngle, cosAngle);
						if (!MathUtil.areEqual(dd, 0)) {
							if (dd > parameters.maxAngle) {
								dd = parameters.maxAngle;
							} else if (dd < -parameters.maxAngle) {
								dd = -parameters.maxAngle;
							}
						}
					}
				}
			}

		}
		if (this.haveFood) {
			producedInfluences.add(
					new EmitPheromone(timeLowerBound, timeUpperBound, castedPublicLocalState.getLocation(), "Food", 5));
			dd = goToPheromone(castedPublicLocalState, castedPerceivedData, "Base", true, 100);
		} else if (detectePheromones && !turnAround) {
			// Try to detect a food pheromone
			dd = goToPheromone(castedPublicLocalState, castedPerceivedData, "Food", true, 100);
		} else if (((!repulsion) || (!attraction)) && !turnAround) {
			// Random walk when nothing is detect
			if (PRNG.randomBoolean()) {

				dd = this.parameters.maxAngle / (PRNG.randomDouble()) / 50;
			} else {
				dd = -(this.parameters.maxAngle / (PRNG.randomDouble()) / 50);
			}
		}
		// Ant change here direction
		producedInfluences.add(new ChangeDirection(timeLowerBound, timeUpperBound, dd, castedPublicLocalState));
	}

	/**
	 * Method to follow a pheromone gradient.
	 * 
	 * @param castedPublicLocalState the local state of the actual agent
	 * @param castedPerceivedData the perceived data
	 * @param id the id of the pheromone field
	 * @param goToBase <code>true</code> if the ant returns to the base immediately
	 * @param d the value divided the angle to change the direction
	 * @return the direction towards the max value of perceived pheromone.
	 */
	public double goToPheromone(
		TurtlePLSInLogo castedPublicLocalState,
		TurtlePerceivedData castedPerceivedData,
		String id,
		boolean goToBase,
		double d
		) {
		
		boolean goToBaseImmediately = goToBase;
		// if my ant want find a base
		if ("Base".equals(id)) {
			if ((Math.floor(PRNG.randomDouble() * 10) / 2) <= 1) {
				goToBaseImmediately = false;
			} else {
				goToBaseImmediately = true;
			}
		}

		if (goToBaseImmediately) {
			detectePheromones = false;
			List<LocalPerceivedData<Double>> l = new ArrayList<>();
			l.addAll(castedPerceivedData.getPheromones().get(id));

			PRNG.shuffle(l);

			List<Double> dir = new ArrayList<>();
			List<Double> value = new ArrayList<>();

			// When food pheromones are detected by a ant
			for (LocalPerceivedData<Double> pheromone : l) {
				if (pheromone.getContent() > 0) {

					value.add(pheromone.getContent());

					for (int j = 0; j < pheromone.getContent(); j++) {

						dir.add(pheromone.getDirectionTo());

					}
				}
			}

			int sommes = 0;

			if (dir.isEmpty()) {
				// Random walk when nothing is detect
				if (PRNG.randomBoolean()) {

					return this.parameters.maxAngle / (PRNG.randomDouble() * d);
				} else {
					return -(this.parameters.maxAngle / (PRNG.randomDouble() * d));
				}
			} else {
				if (!value.isEmpty()) {
					for (int j = 0; j < value.size(); j++) {
						sommes += value.get(j);
					}
				}

				PRNG.shuffle(dir);

				int proba = (int) Math.floor(PRNG.randomDouble() * sommes);

				// Follow the pheromones
				return dir.get(proba) - castedPublicLocalState.getDirection();
			}
		} else {
			return this.envPLS.getDirection(castedPublicLocalState.getLocation(), this.positionBase)
					- castedPublicLocalState.getDirection();
		}
	}
}