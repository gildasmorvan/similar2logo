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
package fr.univ_artois.lgi2a.similar2logo.examples.simplemultilevel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.environment.ExtendedEnvironment;
import fr.univ_artois.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtPerceptionModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar2logo.examples.simplemultilevel.model.SimpleMultiLevelSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.simplemultilevel.model.levels.SimpleMultiLevelReactionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.simplemultilevel.model.levels.SimpleMultiLevelSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.MultiLevelTurtleFactory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoNaturalModel;
import fr.univ_artois.lgi2a.similar2logo.lib.model.EmptyPerceptionModel;
import fr.univ_artois.lgi2a.similar2logo.lib.model.PassiveTurtleDecisionModel;

/**
 * The simulation model of the simple multi-level simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class SimpleMultiLevelSimulationModel extends AbstractLogoSimulationModel {

	/**
	 * Builds an instance of this simulation model.
	 * @param parameters The parameters of the simulation model.
	 */
	public SimpleMultiLevelSimulationModel(SimpleMultiLevelSimulationParameters parameters) {
		super(parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(
		ISimulationParameters simulationParameters,
		Map<LevelIdentifier, ILevel> levels
	) {
		AgentInitializationData result = new AgentInitializationData();
		SimpleMultiLevelSimulationParameters castedSimulationParameters = (SimpleMultiLevelSimulationParameters) simulationParameters;
		for(int i=0; i<castedSimulationParameters.nbOfAgents; i++) {
			Map<LevelIdentifier, AbstractAgtPerceptionModel> turtlePerceptionModels = new HashMap<>();
			Map<LevelIdentifier, AbstractAgtDecisionModel> turtleDecisionModels = new HashMap<>();
			turtlePerceptionModels.put(SimpleMultiLevelSimulationLevelList.LOGO, new EmptyPerceptionModel(SimpleMultiLevelSimulationLevelList.LOGO));
			turtlePerceptionModels.put(SimpleMultiLevelSimulationLevelList.LOGO2, new EmptyPerceptionModel(SimpleMultiLevelSimulationLevelList.LOGO2));
			turtleDecisionModels.put(SimpleMultiLevelSimulationLevelList.LOGO, new PassiveTurtleDecisionModel(SimpleMultiLevelSimulationLevelList.LOGO));
			turtleDecisionModels.put(SimpleMultiLevelSimulationLevelList.LOGO2, new PassiveTurtleDecisionModel(SimpleMultiLevelSimulationLevelList.LOGO2));
			result.getAgents().add( 
				MultiLevelTurtleFactory.generate(
					turtlePerceptionModels,
					turtleDecisionModels,
					SimpleMultiLevelSimulationLevelList.LOGO,
					new AgentCategory("passive", TurtleAgentCategory.CATEGORY),
					PRNG.randomAngle(),
					1,
					0,
					PRNG.randomDouble(0, castedSimulationParameters.gridWidth),
					PRNG.randomDouble(0, castedSimulationParameters.gridWidth)
				)
			);
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
		ISimulationParameters simulationParameters
	) {
		List<ILevel> levelList = new LinkedList<>();
		ExtendedLevel logo = new ExtendedLevel(
			simulationParameters.getInitialTime(), 
			SimpleMultiLevelSimulationLevelList.LOGO, 
			new PeriodicTimeModel( 
				1, 
				0, 
				simulationParameters.getInitialTime()
			),
			new SimpleMultiLevelReactionModel()
		);
		ExtendedLevel logo2 = new ExtendedLevel(
			simulationParameters.getInitialTime(), 
			SimpleMultiLevelSimulationLevelList.LOGO2, 
			new PeriodicTimeModel( 
				1, 
				0, 
				simulationParameters.getInitialTime()
			),
			new SimpleMultiLevelReactionModel()
		);
		
		//Add influence relations
		logo.addInfluenceableLevel(SimpleMultiLevelSimulationLevelList.LOGO2);
		logo2.addInfluenceableLevel(SimpleMultiLevelSimulationLevelList.LOGO);
		levelList.add(logo);
		levelList.add(logo2);
		return levelList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EnvironmentInitializationData generateEnvironment(
		ISimulationParameters simulationParameters,
		Map<LevelIdentifier, ILevel> levels
	) {
		
		LogoSimulationParameters castedSimulationParameters = (LogoSimulationParameters) simulationParameters;
		// Create the environment.
		ExtendedEnvironment environment = new ExtendedEnvironment( );
		// Define the initial behavior of the environment for each level.
		environment.specifyBehaviorForLevel(
			SimpleMultiLevelSimulationLevelList.LOGO, 
			new LogoNaturalModel(SimpleMultiLevelSimulationLevelList.LOGO)
		);
		
		environment.specifyBehaviorForLevel(
				SimpleMultiLevelSimulationLevelList.LOGO2, 
				new LogoNaturalModel(SimpleMultiLevelSimulationLevelList.LOGO2)
			);
		
		// Set the initial local state of the environment for each level.
		environment.includeNewLevel(
				SimpleMultiLevelSimulationLevelList.LOGO,
			new LogoEnvPLS(
				SimpleMultiLevelSimulationLevelList.LOGO,
				castedSimulationParameters.gridWidth,
				castedSimulationParameters.gridHeight,
				castedSimulationParameters.xTorus,
				castedSimulationParameters.yTorus,
				castedSimulationParameters.pheromones
			),
			new EmptyLocalStateOfEnvironment( SimpleMultiLevelSimulationLevelList.LOGO )
		);
		environment.includeNewLevel(
				SimpleMultiLevelSimulationLevelList.LOGO2,
				new LogoEnvPLS(
					SimpleMultiLevelSimulationLevelList.LOGO2,
					castedSimulationParameters.gridWidth,
					castedSimulationParameters.gridHeight,
					castedSimulationParameters.xTorus,
					castedSimulationParameters.yTorus,
					castedSimulationParameters.pheromones
				),
				new EmptyLocalStateOfEnvironment( SimpleMultiLevelSimulationLevelList.LOGO2 )
			);
		return new EnvironmentInitializationData( environment );
	}

}
