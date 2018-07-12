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
package fr.lgi2a.similar2logo.examples.predation.initializations;

import java.util.Map;

import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PredatorCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorFactory;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.lib.model.EmptyPerceptionModel;
import fr.lgi2a.similar2logo.lib.model.RandomWalk2DDecisionModel;
import fr.lgi2a.similar2logo.lib.tools.random.PRNG;

/**
 * The simulation model of the "random walk" predation simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class RandomWalkPredationSimulationModel extends AbstractPredationSimulationModel {

	/**
	 * Builds a new model for the random walk predation simulation.
	 * 
	 * @param parameters The parameters of this simulation model.
	 */
	public RandomWalkPredationSimulationModel(LogoSimulationParameters parameters) {
		super(parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(
			ISimulationParameters parameters,
			Map<LevelIdentifier, ILevel> levels) {
		PredationSimulationParameters castedParameters = (PredationSimulationParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();

		//Generating preys
		for (int i = 0; i < castedParameters.initialPreyPopulation; i++) {
			IAgent4Engine turtle = PreyPredatorFactory.generate(
					new EmptyPerceptionModel(),
					new RandomWalk2DDecisionModel(),
					PreyCategory.CATEGORY,
					LogoEnvPLS.NORTH,
					0,
					0,
					PRNG.get().randomDouble() * castedParameters.gridWidth,
					PRNG.get().randomDouble() * castedParameters.gridHeight,
					castedParameters.preyInitialEnergy,
					0
			);
			result.getAgents().add(turtle);
		}
		
		//Generating predators
		for (int i = 0; i < castedParameters.initialPredatorPopulation; i++) {
			IAgent4Engine turtle = PreyPredatorFactory.generate(
					new EmptyPerceptionModel(),
					new RandomWalk2DDecisionModel(),
					PredatorCategory.CATEGORY,
					LogoEnvPLS.NORTH,
					0,
					0,
					PRNG.get().randomDouble() * castedParameters.gridWidth,
					PRNG.get().randomDouble() * castedParameters.gridHeight,
					castedParameters.predatorInitialEnergy,
					0
			);
			result.getAgents().add(turtle);
		}
		return result;
	}

}
