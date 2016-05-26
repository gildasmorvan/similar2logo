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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PredatorCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorFactory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.agents.decision.RandomWalkDecisionModel;
import fr.lgi2a.similar2logo.lib.agents.perception.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * Represents aging and reproduction interactions.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class AgingAndReproductionInteraction {
	
	/**
	 * The predators involved in the interaction.
	 */
	private  List<PreyPredatorPLS> predators;
	
	/**
	 * The preys involved in the interaction.
	 */
	private  List<PreyPredatorPLS> preys;
	
	/**
	 * @param set The agents involved in the interaction.
	 */
	public AgingAndReproductionInteraction(
			Set<ILocalStateOfAgent> agents
	) {
		this.predators = new ArrayList<PreyPredatorPLS>();
		this.preys = new ArrayList<PreyPredatorPLS>();
		
		//Order agents by type
		for (ILocalStateOfAgent agent : agents) {
			if (agent.getCategoryOfAgent().isA(
					PredatorCategory.CATEGORY)) {
				predators.add((PreyPredatorPLS) agent);
			} else if (agent.getCategoryOfAgent().isA(
					PreyCategory.CATEGORY)) {
				preys.add((PreyPredatorPLS) agent);
			}
		}
		Collections.shuffle(predators);
		Collections.shuffle(preys);
	}
	
	/**
	 * 
	 * The model of prey aging.
	 * 
	 * @param parameters The parameters of the simulation.
	 * @param remainingInfluences The data structure that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 * @param transitoryTimeMin The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param transitoryTimeMax The upper bound of the transitory period of the level for which this reaction is performed.
	 */
	public void preyAging(
			PredationSimulationParameters parameters,
			InfluencesMap remainingInfluences,
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax
	) {
		for (ILocalStateOfAgent agent : preys) {
			PreyPredatorPLS preyPredatorPLS = (PreyPredatorPLS) agent;
			preyPredatorPLS.setLifeTime(preyPredatorPLS.getLifeTime() + 1);
			preyPredatorPLS.setEnergy(preyPredatorPLS.getEnergy() - 1);
			if (
			   preyPredatorPLS.getEnergy() <= 0
			   || preyPredatorPLS.getLifeTime() >= parameters.preyLifeTime
			) {
				remainingInfluences.add(new SystemInfluenceRemoveAgent(
						LogoSimulationLevelList.LOGO, transitoryTimeMin,
						transitoryTimeMax, preyPredatorPLS));
			}
		}
	}
	
	/**
	 * 
	 * The model of predator aging.
	 * 
	 * @param parameters The parameters of the simulation.
	 * @param remainingInfluences The data structure that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 * @param transitoryTimeMin The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param transitoryTimeMax The upper bound of the transitory period of the level for which this reaction is performed.
	 */
	public void predatorAging(
			PredationSimulationParameters parameters,
			InfluencesMap remainingInfluences,
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax
	) {
		for (ILocalStateOfAgent agent : predators) {
			PreyPredatorPLS preyPredatorPLS = (PreyPredatorPLS) agent;
			preyPredatorPLS.setLifeTime(preyPredatorPLS.getLifeTime() + 1);
			preyPredatorPLS.setEnergy(preyPredatorPLS.getEnergy() - 1);
			if (
			   preyPredatorPLS.getEnergy() <= 0
			   || preyPredatorPLS.getLifeTime() >= parameters.predatorLifeTime
			) {
				remainingInfluences.add(new SystemInfluenceRemoveAgent(
						LogoSimulationLevelList.LOGO, transitoryTimeMin,
						transitoryTimeMax, preyPredatorPLS));
			}
		}
		
	}
	
	/**
	 * 
	 * The model of prey reproduction.
	 * 
	 * @param parameters The parameters of the simulation.
	 * @param environment The environment of the simulation.
	 * @param remainingInfluences The data structure that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 * @param transitoryTimeMin The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param transitoryTimeMax The upper bound of the transitory period of the level for which this reaction is performed.
	 */
	public void preyReproduction(
			PredationSimulationParameters parameters,
			LogoEnvPLS environment,
			InfluencesMap remainingInfluences,
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax
	) {
		if(preys.size() >=2) {
		   for (int i = 0; i < (int) (preys.size() * parameters.preyReproductionRate); i++) {
			   remainingInfluences.add(
			      new SystemInfluenceAddAgent(
					LogoSimulationLevelList.LOGO, transitoryTimeMin,
					transitoryTimeMax, PreyPredatorFactory.generate(
					   new TurtlePerceptionModel(0, 0, false,false, false),
					   new RandomWalkDecisionModel(),
					   PreyCategory.CATEGORY,
					   RandomValueFactory.getStrategy().randomDouble() * 2 * Math.PI,
					   0,
					   0,
					   RandomValueFactory.getStrategy().randomDouble() * environment.getWidth(),
					   RandomValueFactory.getStrategy().randomDouble() * environment.getHeight(),
					   parameters.preyInitialEnergy,
					   0
				     )
			      )
			   );
		   }
		}
	}
	
	/**
	 * 
	 * The model of predator reproduction.
	 * 
	 * @param parameters The parameters of the simulation.
	 * @param environment The environment of the simulation.
	 * @param remainingInfluences The data structure that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 * @param transitoryTimeMin The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param transitoryTimeMax The upper bound of the transitory period of the level for which this reaction is performed.
	 */
	public void predatorReproduction(
			PredationSimulationParameters parameters,
			LogoEnvPLS environment,
			InfluencesMap remainingInfluences,
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax
	) {
		if(predators.size() >=2) {
		   for (int i = 0; i < (int) (predators.size() * parameters.predatorReproductionRate); i++) {
			   remainingInfluences.add(
			      new SystemInfluenceAddAgent(
					LogoSimulationLevelList.LOGO, transitoryTimeMin,
					transitoryTimeMax, PreyPredatorFactory.generate(
					   new TurtlePerceptionModel(0, 0, false,false, false),
					   new RandomWalkDecisionModel(),
					   PredatorCategory.CATEGORY,
					   RandomValueFactory.getStrategy().randomDouble() * 2 * Math.PI,
					   0,
					   0,
					   RandomValueFactory.getStrategy().randomDouble() * environment.getWidth(),
					   RandomValueFactory.getStrategy().randomDouble() * environment.getHeight(),
					   parameters.predatorInitialEnergy,
					   0
				    )
			      )
			   );
		   }
		}
	}

}
