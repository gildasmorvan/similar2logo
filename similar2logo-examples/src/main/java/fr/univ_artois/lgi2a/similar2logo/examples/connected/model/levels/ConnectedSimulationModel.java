package fr.univ_artois.lgi2a.similar2logo.examples.connected.model.levels;

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
import fr.univ_artois.lgi2a.similar2logo.examples.connected.model.ConnectedSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.simplemultilevel.model.levels.SimpleMultiLevelReactionModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.MultiLevelTurtleFactory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.ConnectedLogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoNaturalModel;
import fr.univ_artois.lgi2a.similar2logo.lib.model.EmptyPerceptionModel;
import fr.univ_artois.lgi2a.similar2logo.lib.model.PassiveTurtleDecisionModel;
import net.jafama.FastMath;

public class ConnectedSimulationModel extends AbstractLogoSimulationModel {

	public ConnectedSimulationModel(LogoSimulationParameters parameters) {
		super(parameters);
	}

	@Override
	protected AgentInitializationData generateAgents(
		ISimulationParameters simulationParameters,
		Map<LevelIdentifier, ILevel> levels
	) {
		AgentInitializationData result = new AgentInitializationData();
		ConnectedSimulationParameters castedSimulationParameters = (ConnectedSimulationParameters) simulationParameters;
		for(int i=0; i<castedSimulationParameters.nbOfAgents; i++) {
			Map<LevelIdentifier, AbstractAgtPerceptionModel> turtlePerceptionModels = new HashMap<>();
			Map<LevelIdentifier, AbstractAgtDecisionModel> turtleDecisionModels = new HashMap<>();
			for(LevelIdentifier[] row : ConnectedSimulationLevelList.LOGO) {
				for(LevelIdentifier cell : row) {
					turtlePerceptionModels.put(
						cell,
						new EmptyPerceptionModel(cell)
					);
					turtleDecisionModels.put(
						cell,
						new PassiveTurtleDecisionModel(cell)
					);
				}
			}
			result.getAgents().add( 
				MultiLevelTurtleFactory.generate(
					turtlePerceptionModels,
					turtleDecisionModels,
					ConnectedSimulationLevelList.LOGO[0][0],
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
		for(int x = 0; x < ConnectedSimulationLevelList.LOGO.length; x++) {
			for(int y = 0; y < ConnectedSimulationLevelList.LOGO[x].length; y++) {
				levelList.add(
					new ExtendedLevel(
						simulationParameters.getInitialTime(), 
						ConnectedSimulationLevelList.LOGO[x][y], 
						new PeriodicTimeModel( 
							1, 
							0, 
							simulationParameters.getInitialTime()
						),
						new SimpleMultiLevelReactionModel()
					)
				);
			}
		}
		
		//Add influence and perception relations
		for(ILevel level1 : levelList) {
			for(ILevel level2 : levelList) {
				if(!level1.equals(level2)) {
					((ExtendedLevel) level1).addInfluenceableLevel(level2.getIdentifier());
					((ExtendedLevel) level1).addPerceptibleLevel(level2.getIdentifier());
				}
			}
		}

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
		// Define the initial behavior and the initial local state of the environment for each level.
		for(Map.Entry<LevelIdentifier, ILevel> level : levels.entrySet()) {
			environment.specifyBehaviorForLevel(
				level.getKey(), 
				new LogoNaturalModel(level.getKey())
			);
			environment.includeNewLevel(
					level.getKey(),
				new ConnectedLogoEnvPLS(
					level.getKey(),
					castedSimulationParameters.gridWidth,
					castedSimulationParameters.gridHeight,
					castedSimulationParameters.xTorus,
					castedSimulationParameters.yTorus,
					castedSimulationParameters.pheromones
				),
				new EmptyLocalStateOfEnvironment( level.getKey() )
			);
		}
		
		//Add the connections
		for(int x = 0; x < ConnectedSimulationLevelList.LOGO.length; x++) {
			for(int y = 0; y < ConnectedSimulationLevelList.LOGO[x].length; y++) {
				ConnectedLogoEnvPLS envPLS = (ConnectedLogoEnvPLS) environment.getPublicLocalState(
					ConnectedSimulationLevelList.LOGO[x][y]
				);
				for(int dx=-1; dx <= 1; dx++) {
					for(int dy=-1; dy <= 1; dy++) {
						if(dx != 1 || dy != 1) {
							int xindex = (x+dx)%3;
							int yindex = (y+dy)%3;
							double direction = -FastMath.atan2(dx, dy);
							envPLS.addConnection(
								direction,
								environment.getPublicLocalState(
									ConnectedSimulationLevelList.LOGO[xindex][yindex]
								)
							);
						}		
					}
				}
			}
		}
		
		
		return new EnvironmentInitializationData( environment );
	}

}
