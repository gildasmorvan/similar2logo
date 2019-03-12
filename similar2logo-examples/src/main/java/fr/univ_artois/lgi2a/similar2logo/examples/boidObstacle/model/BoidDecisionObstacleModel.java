package fr.univ_artois.lgi2a.similar2logo.examples.boidObstacle.model;

import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar2logo.examples.boids.model.BoidDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.boids.model.BoidsSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil;

public class BoidDecisionObstacleModel extends BoidDecisionModel {

	/**
	 * Builds an instance of this decision model.
	 */
	public BoidDecisionObstacleModel(BoidsSimulationParameters parameters) {
		super(parameters);
	}

	@SuppressWarnings("rawtypes")
	public void decide(SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {

		TurtlePLSInLogo castedPublicLocalState = (TurtlePLSInLogo) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;

		
		double distance = Double.MAX_VALUE;
		double angle = 0;
		
		if (!castedPerceivedData.getMarks().isEmpty()) {
			for (LocalPerceivedData<Mark> perceivedMark : castedPerceivedData.getMarks()) {
				
				
				if(perceivedMark.getDistanceTo() < distance) {
					angle = castedPublicLocalState.getDirection() - perceivedMark.getDirectionTo();
				}
				
				
			}
			
			
			
			if (!MathUtil.areEqual(angle, 0)) {
				producedInfluences.add(new ChangeDirection(timeLowerBound,
						timeUpperBound, angle, castedPublicLocalState));
			}
		} else if (!castedPerceivedData.getTurtles().isEmpty()) {

			super.decide(
				timeLowerBound,
				timeUpperBound,
				globalState,
				publicLocalState,
				privateLocalState,
				perceivedData,
				producedInfluences
				);

		}
	}

}
