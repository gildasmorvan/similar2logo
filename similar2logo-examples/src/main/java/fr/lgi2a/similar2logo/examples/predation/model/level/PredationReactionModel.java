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
package fr.lgi2a.similar2logo.examples.predation.model.level;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PredatorCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorFactory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorPLS;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.agents.decision.RandomWalkDecisionModel;
import fr.lgi2a.similar2logo.lib.agents.perception.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class PredationReactionModel extends LogoDefaultReactionModel {

	/**
	 * The parameters of the simulation.
	 */
	private PredationSimulationParameters parameters;

	/**
	 * Creates a new instance of the SegregationReactionModel class.
	 * 
	 * @param parameters
	 */
	public PredationReactionModel(PredationSimulationParameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {
		LogoEnvPLS env = (LogoEnvPLS) consistentState
				.getPublicLocalStateOfEnvironment();

		// Predation
		for (int x = 0; x < env.getWidth(); x++) {
			for (int y = 0; y < env.getHeight(); y++) {
				Set<TurtlePLSInLogo> agents = env.getTurtlesAt(x, y);
				List<TurtlePLSInLogo> predators = new ArrayList<TurtlePLSInLogo>();
				List<PreyPredatorPLS> preys = new ArrayList<PreyPredatorPLS>();
				for (TurtlePLSInLogo agent : agents) {
					if (agent.getCategoryOfAgent().isA(
							PredatorCategory.CATEGORY)) {
						predators.add(agent);
					} else if (agent.getCategoryOfAgent().isA(
							PreyCategory.CATEGORY)) {
						preys.add((PreyPredatorPLS) agent);
					}
				}
				Collections.shuffle(predators);
				Collections.shuffle(preys);
				
				//Preys eat grass
				for (PreyPredatorPLS prey : preys) {
					Point2D location = prey.getLocation();
					Set<Mark> marks = env.getMarksAt((int) location.getX(),
							(int) location.getY());
					if (((Double) marks.iterator().next().getContent()) >= 1) {
						prey.setEnergy(
							Math.max(
								prey.getEnergy()
								+ parameters.preyEnergyGainFromFood,
								parameters.maximalPreyEnergy
							)
						);
						marks.iterator()
								.next()
								.setContent(
										((Double) marks.iterator().next()
												.getContent()) - 1);
					}
				}
				
				//Predators eat preys
				for (int i = 0; i < predators.size() && i < preys.size(); i++) {
					PreyPredatorPLS predatorPLS = (PreyPredatorPLS) predators
							.get(i);
					if ((predatorPLS.getEnergy() < this.parameters.maximalPredatorEnergy) && (RandomValueFactory.getStrategy().randomDouble() < this.parameters.predationProbability)) {
						remainingInfluences.add(new SystemInfluenceRemoveAgent(
								LogoSimulationLevelList.LOGO,
								transitoryTimeMin, transitoryTimeMax, preys
										.get(i)));
						predatorPLS.setEnergy(
							Math.max(
								predatorPLS.getEnergy()
								+ this.parameters.predatorEnergyGainFromFood,
								this.parameters.maximalPredatorEnergy
							)
						);
					}
				}
			}
		}

		int nbOfPreys = 0;
		int nbOfPredators = 0;
		// Aging and reproduction
		for (ILocalStateOfAgent agent : consistentState
				.getPublicLocalStateOfAgents()) {
			PreyPredatorPLS preyPredatorPLS = (PreyPredatorPLS) agent;
			preyPredatorPLS.setLifeTime(preyPredatorPLS.getLifeTime() + 1);
			preyPredatorPLS.setEnergy(preyPredatorPLS.getEnergy() - 1);
			if (
					(preyPredatorPLS.getEnergy() <= 0)
					|| (preyPredatorPLS.getCategoryOfAgent().isA(
							PredatorCategory.CATEGORY) 
						&& (preyPredatorPLS.getLifeTime() >= this.parameters.predatorLifeTime))
					|| (preyPredatorPLS.getCategoryOfAgent().isA(PreyCategory.CATEGORY)
						&& preyPredatorPLS.getLifeTime() >= this.parameters.preyLifeTime)) {
				remainingInfluences.add(new SystemInfluenceRemoveAgent(
						LogoSimulationLevelList.LOGO, transitoryTimeMin,
						transitoryTimeMax, preyPredatorPLS));
			}
			if (preyPredatorPLS.getCategoryOfAgent().isA(
					PredatorCategory.CATEGORY)) {
				nbOfPredators++;
			} else if (preyPredatorPLS.getCategoryOfAgent().isA(
					PreyCategory.CATEGORY)) {
				nbOfPreys++;
			}
		}

		for (int i = 0; i < (int) (nbOfPreys * parameters.preyReproductionRate); i++) {
			remainingInfluences.add(new SystemInfluenceAddAgent(
					LogoSimulationLevelList.LOGO, transitoryTimeMin,
					transitoryTimeMax, PreyPredatorFactory
							.generate(new TurtlePerceptionModel(0, 0, false,
									false, false),
									new RandomWalkDecisionModel(),
									PreyCategory.CATEGORY,
									RandomValueFactory.getStrategy()
											.randomDouble() * 2 * Math.PI, 0,
									0, RandomValueFactory.getStrategy()
											.randomDouble() * env.getWidth(),
									RandomValueFactory.getStrategy()
											.randomDouble() * env.getHeight(),
									parameters.preyInitialEnergy, 0))

			);
		}
		for (int i = 0; i < (int) (nbOfPredators * parameters.predatorReproductionRate); i++) {
			remainingInfluences.add(new SystemInfluenceAddAgent(
					LogoSimulationLevelList.LOGO, transitoryTimeMin,
					transitoryTimeMax, PreyPredatorFactory
							.generate(new TurtlePerceptionModel(0, 0, false,
									false, false),
									new RandomWalkDecisionModel(),
									PredatorCategory.CATEGORY,
									RandomValueFactory.getStrategy()
											.randomDouble() * 2 * Math.PI, 0,
									0, RandomValueFactory.getStrategy()
											.randomDouble() * env.getWidth(),
									RandomValueFactory.getStrategy()
											.randomDouble() * env.getHeight(),
									parameters.predatorInitialEnergy, 0))

			);
		}

		// Grass Growth
		for (int x = 0; x < env.getWidth(); x++) {
			for (int y = 0; y < env.getHeight(); y++) {
				Set<Mark> marks = env.getMarksAt(x, y);
				marks.iterator()
						.next()
						.setContent(
								(((Double) marks.iterator().next().getContent()) + ((Double) marks
										.iterator().next().getContent())
										* parameters.grassGrowthRate)
										* (1 - ((Double) marks.iterator()
												.next().getContent())
												/ parameters.maximalGrassDensity));
			}
		}

		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax,
				consistentState, regularInfluencesOftransitoryStateDynamics,
				remainingInfluences);

	}

}
