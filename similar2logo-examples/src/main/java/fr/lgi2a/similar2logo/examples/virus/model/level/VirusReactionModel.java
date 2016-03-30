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
package fr.lgi2a.similar2logo.examples.virus.model.level;

import java.util.Set;

import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar2logo.examples.virus.model.VirusSimulationParameters;
import fr.lgi2a.similar2logo.examples.virus.model.agents.PersonCategory;
import fr.lgi2a.similar2logo.examples.virus.model.agents.PersonDecisionModel;
import fr.lgi2a.similar2logo.examples.virus.model.agents.PersonFactory;
import fr.lgi2a.similar2logo.examples.virus.model.agents.PersonPLS;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.agents.perception.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * The reaction model of the virus simulation.
 * 
 * @author <a href="mailto:julienjnthn@gmail.com" target="_blank">Jonathan Julien</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class VirusReactionModel extends LogoDefaultReactionModel {

	private VirusSimulationParameters parameters;

	public VirusReactionModel(VirusSimulationParameters parameters) {
		this.parameters = parameters;

	}

	@Override
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {

		LogoEnvPLS env = (LogoEnvPLS) consistentState
				.getPublicLocalStateOfEnvironment();


		// Recovery
		for (ILocalStateOfAgent agent : consistentState
				.getPublicLocalStateOfAgents()) {
			PersonPLS castedAgentPLS = (PersonPLS) agent;

			if (castedAgentPLS.isInfected()
					&& (transitoryTimeMin.getIdentifier() >= (castedAgentPLS
							.getTimeInfected() + parameters.infectionTime))) {
				castedAgentPLS.setInfected(false);
			}
		}

		// Contagion
		for (int x = 0; x < env.getWidth(); x++) {
			for (int y = 0; y < env.getHeight(); y++) {
				int nbOfInfectedAgentsInPatch = 0;
				Set<TurtlePLSInLogo> agents = env.getTurtlesAt(x, y);
				for (TurtlePLSInLogo agent : agents) {
					PersonPLS castedAgentPLS = (PersonPLS) agent;
					if (castedAgentPLS.isInfected()) {
						nbOfInfectedAgentsInPatch++;
					}
				}
				
				double p = 1 - Math.pow(1 - parameters.probInfected,
						nbOfInfectedAgentsInPatch);

				for (TurtlePLSInLogo agent : agents) {
					PersonPLS castedAgentPLS = (PersonPLS) agent;
					if (!castedAgentPLS.isInfected()
							&& (RandomValueFactory.getStrategy().randomDouble() < p)) {
						if (castedAgentPLS.getTimeInfected() == -1) {
							castedAgentPLS.setInfected(true);
							castedAgentPLS.setTimeInfected(transitoryTimeMin
									.getIdentifier());
						} else if (RandomValueFactory.getStrategy()
								.randomDouble() >= parameters.degreeOfImmunity) {
							castedAgentPLS.setInfected(true);
							castedAgentPLS.setTimeInfected(transitoryTimeMin
									.getIdentifier());
						}

					}
				}
			}
		}

		// Deaths
		for (ILocalStateOfAgent agent : consistentState
				.getPublicLocalStateOfAgents()) {
			PersonPLS castedAgentPLS = (PersonPLS) agent;
			castedAgentPLS.setLifeTime(castedAgentPLS.getLifeTime() + 1);
			if ((castedAgentPLS.getLifeTime() > parameters.lifeTime)
					|| (castedAgentPLS.isInfected() && (RandomValueFactory
							.getStrategy().randomDouble() < parameters.deathProbability
							/ parameters.infectionTime))) {
				remainingInfluences.add(new SystemInfluenceRemoveAgent(
						LogoSimulationLevelList.LOGO, transitoryTimeMin,
						transitoryTimeMax, castedAgentPLS)

				);
			}
		}

		int nbOfInfectedAgents = 0;
		for (ILocalStateOfAgent agent : consistentState
				.getPublicLocalStateOfAgents()) {
			PersonPLS castedAgentPLS = (PersonPLS) agent;

			if (castedAgentPLS.isInfected()) {
				nbOfInfectedAgents++;
			}
		}


		// Birth
		int nbOfBirths = (int) ((consistentState.getPublicLocalStateOfAgents()
				.size() - nbOfInfectedAgents) * parameters.birth);
		LogoEnvPLS environment = (LogoEnvPLS) consistentState
				.getPublicLocalStateOfEnvironment();
		for (int i = 0; i < nbOfBirths; i++) {

			remainingInfluences.add(new SystemInfluenceAddAgent(
					LogoSimulationLevelList.LOGO, transitoryTimeMin,
					transitoryTimeMax, PersonFactory
							.generate(
									new TurtlePerceptionModel(0, 0, false,
											false, false),
									new PersonDecisionModel(),
									new AgentCategory("person",
											PersonCategory.CATEGORY),
									RandomValueFactory.getStrategy()
											.randomDouble() * 2 * Math.PI,
									0,
									0,
									RandomValueFactory.getStrategy()
											.randomDouble()
											* environment.getWidth(),
									RandomValueFactory.getStrategy()
											.randomDouble()
											* environment.getHeight(),
									false,
									-1,
									0
								)
						)

			);
		}
		

		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax,
				consistentState, regularInfluencesOftransitoryStateDynamics,
				remainingInfluences);
	}

}