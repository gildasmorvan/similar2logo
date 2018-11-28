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
package fr.univ_artois.lgi2a.similar2logo.examples.predation.model.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.agents.PredatorCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.agents.PreyCategory;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;

/**
 * Represents a predation interaction between preys, predators and grass.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class PredationInteraction {

	/**
	 * The predators involved in the interaction.
	 */
	private  List<PreyPredatorPLS> predators;
	
	/**
	 * The preys involved in the interaction.
	 */
	private  List<PreyPredatorPLS> preys;
	
	/**
	 * The grass involved in the interaction.
	 */
	private  Mark<Double> grass;
	
	/**
	 * @param agents The agents involved in the interaction.
	 * @param grass The grass involved in the interaction.
	 */
	public PredationInteraction(
			Set<TurtlePLSInLogo> agents,
			Mark<Double> grass	
	) {
		this.grass = grass;
		this.predators = new ArrayList<>();
		this.preys = new ArrayList<>();
		
		//Order agents by type
		for (TurtlePLSInLogo agent : agents) {
			if (agent.getCategoryOfAgent().isA(
					PredatorCategory.CATEGORY)) {
				predators.add((PreyPredatorPLS) agent);
			} else if (agent.getCategoryOfAgent().isA(
					PreyCategory.CATEGORY)) {
				preys.add((PreyPredatorPLS) agent);
			}
		}
		PRNG.shuffle(predators);
		PRNG.shuffle(preys);
	}
	
	/**
	 * 
	 * The model of prey/grass predation.
	 * 
	 * @param parameters The parameters of the simulation.
	 */
	public void preysEatGrass(PredationSimulationParameters parameters) {
		for (PreyPredatorPLS prey : preys) {
			if ((grass.getContent()) >= 1) {
				prey.setEnergy(
					Math.max(
						prey.getEnergy()
						+ parameters.preyEnergyGainFromFood,
						parameters.maximalPreyEnergy
					)
				);
				grass.setContent(grass.getContent() - 1);
			}
		}
	}
	
	/**
	 * 
	 * The model of predator/prey predation.
	 * 
	 * @param parameters The parameters of the simulation.
	 * @param remainingInfluences The data structure that will contain the influences that
	 * were produced by the user during the invocation of this method, or the influences that
	 * persist after this reaction.
	 * @param transitoryTimeMin The lower bound of the transitory period of the level for which
	 * this reaction is performed.
	 * @param transitoryTimeMax The upper bound of the transitory period of the level for which
	 * this reaction is performed.
	 * @param dyingPreys The preys eaten by the predators.
	 */
	public void predatorsEatPreys(
		PredationSimulationParameters parameters,
		InfluencesMap remainingInfluences,
		SimulationTimeStamp transitoryTimeMin,
		SimulationTimeStamp transitoryTimeMax,
		Collection<TurtlePLSInLogo> dyingPreys
	) {
		for (int i = 0; i < predators.size() && i < preys.size(); i++) {
			PreyPredatorPLS predatorPLS = predators.get(i);
			if ((predatorPLS.getEnergy() < parameters.maximalPredatorEnergy) && (PRNG.randomDouble() < parameters.predationProbability)) {
				remainingInfluences.add(
					new SystemInfluenceRemoveAgent(
						LogoSimulationLevelList.LOGO,
						transitoryTimeMin, transitoryTimeMax, preys.get(i)
					)
				);
				predatorPLS.setEnergy(
					Math.max(
						predatorPLS.getEnergy()
						+ parameters.predatorEnergyGainFromFood,
						parameters.maximalPredatorEnergy
					)
				);
				dyingPreys.add(preys.get(i));
			}
		}
	}
	
}
