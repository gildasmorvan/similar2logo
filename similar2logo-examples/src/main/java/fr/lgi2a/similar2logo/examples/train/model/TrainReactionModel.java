package fr.lgi2a.similar2logo.examples.train.model;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;

public class TrainReactionModel extends LogoDefaultReactionModel{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {
		LogoEnvPLS environment = (LogoEnvPLS) consistentState.getPublicLocalStateOfEnvironment();
		HashMap<Point2D,IAgent> nextPosition = new HashMap<Point2D,IAgent>();
		for (IInfluence influence : regularInfluencesOftransitoryStateDynamics) {
			if (influence.getCategory().equals("change direction")){
				
			}
			if (influence.getCategory().equals("stop")) {
				
			}
		}
	}
	
	private Point2D nextPosition (IAgent agent, IInfluence influence) {
		return null;
	}

}
