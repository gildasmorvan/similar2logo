package fr.univ_artois.lgi2a.similar2logo.examples.projettut.model;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeAcceleration;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

public abstract class AbstractLogoDecisionModel extends AbstractAgtDecisionModel {

	
	private SimulationTimeStamp timeLowerBound;
	private SimulationTimeStamp timeUpperBound;
	private InfluencesMap producedInfluences;
	
	/**
	 * simulation parameters.
	 */
	protected SimulationParameters parameters;
	
	public AbstractLogoDecisionModel(SimulationParameters parameters) {
		super(LogoSimulationLevelList.LOGO);
		this.parameters=parameters;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void decide(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		IGlobalState globalState,
		ILocalStateOfAgent publicLocalState,
		ILocalStateOfAgent privateLocalState,
		IPerceivedData perceivedData,
		InfluencesMap producedInfluences
	) {
		
		this.timeLowerBound = timeLowerBound;
		this.timeUpperBound = timeUpperBound;
		this.producedInfluences = producedInfluences;
		
		LocalPerceivedData<Mark> goal = null;
		
		List<LocalPerceivedData<Mark>> obstacles = new ArrayList<>();
		
		for(LocalPerceivedData<Mark> mark : ((TurtlePerceivedData) perceivedData).getMarks()) {
			if("goal".equals(mark.getContent().getCategory())) {
				goal = mark;
			} else if("obstacle".equals(mark.getContent().getCategory())) {
				obstacles.add(mark);
			}
		}	
		
		decide((TurtlePLSInLogo) publicLocalState, goal);
		
	}

	
	/**
	 * @param state the state of the agent
	 * @param goal the target
	 */
	@SuppressWarnings("rawtypes")
	public abstract void decide(TurtlePLSInLogo state, LocalPerceivedData<Mark> goal);
	
	public void changeAcceleration(TurtlePLSInLogo publicLocalState, double da) {
		producedInfluences.add(
			new ChangeAcceleration(
				timeLowerBound,			
				timeUpperBound,
				da,
				publicLocalState
			)
		);
	}
	
	public void changeOrientation(TurtlePLSInLogo publicLocalState, double da) {
		producedInfluences.add(
				new ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					da,
					publicLocalState
				)
			);
	}
		
	

}
