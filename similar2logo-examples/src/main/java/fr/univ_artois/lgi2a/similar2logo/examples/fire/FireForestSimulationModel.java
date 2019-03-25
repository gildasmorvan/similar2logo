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
package fr.univ_artois.lgi2a.similar2logo.examples.fire;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.univ_artois.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.generic.IdentityAgtGlobalStateRevisionModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfAgent;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.model.FireForestParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.model.FireReactionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.model.TreeAgentPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.lib.model.EmptyPerceptionModel;
import fr.univ_artois.lgi2a.similar2logo.lib.model.PassiveTurtleDecisionModel;

/**
 * The simulation model of the fire simulation.
 * 
 * <a xavier_szkudlarek@univ-artois.fr target="_blank">Szkudlarek Xavier</a>
 *
 */
public class FireForestSimulationModel extends AbstractLogoSimulationModel {

	/**
	 * Builds an instance of this simulation model.
	 * 
	 * @param parameters
	 *            The parameters of the simulation model.
	 */
	public FireForestSimulationModel(LogoSimulationParameters parameters) {
		super(parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		FireForestParameters param = (FireForestParameters) simulationParameters;

		AgentInitializationData result = new AgentInitializationData();

		for (int x = 0; x < param.gridWidth; x++) {
			for (int y = 0; y < param.gridHeight; y++) {
				if (100 * PRNG.randomDouble() < param.initialTreeDensity) {
					if (x == 0)
						result.getAgents().add(generateTree(x, y, true));
					else
						result.getAgents().add(generateTree(x, y, false));
				}
			}

		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters) {
		FireForestParameters castedSimulationParameters = (FireForestParameters) simulationParameters;
		ExtendedLevel logo = new ExtendedLevel(
				castedSimulationParameters.getInitialTime(),
				LogoSimulationLevelList.LOGO, new PeriodicTimeModel(1, 0,
						castedSimulationParameters.getInitialTime()),
				new FireReactionModel(castedSimulationParameters));
		List<ILevel> levelList = new LinkedList<>();
		levelList.add(logo);
		return levelList;
	}


	/**
	 * @param x x position
	 * @param y y position
	 * @param burning is the tree burning
	 * @return a tree.
	 */
	public static ExtendedAgent generateTree(int x, int y, boolean burning) {
		ExtendedAgent turtle = new ExtendedAgent(new AgentCategory("tree",
				TurtleAgentCategory.CATEGORY));
		turtle.specifyGlobalStateRevisionModel(new IdentityAgtGlobalStateRevisionModel());
		turtle.specifyBehaviorForLevel(LogoSimulationLevelList.LOGO,
				new EmptyPerceptionModel(), new PassiveTurtleDecisionModel());
		turtle.initializeGlobalState(new EmptyGlobalState());
		turtle.includeNewLevel(LogoSimulationLevelList.LOGO, new TreeAgentPLS(
				turtle, x, y, 0, 0, 0, burning), new EmptyLocalStateOfAgent(
				LogoSimulationLevelList.LOGO, turtle));

		return turtle;
	}

}
