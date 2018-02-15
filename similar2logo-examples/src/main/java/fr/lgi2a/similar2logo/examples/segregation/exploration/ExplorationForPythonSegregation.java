package fr.lgi2a.similar2logo.examples.segregation.exploration;

import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.segregation.model.SegregationSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.ExplorationForPython;
import fr.lgi2a.similar2logo.lib.exploration.ExplorationSimulationModel;

/**
 * Class for the prey predator exploration in python
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ExplorationForPythonSegregation extends ExplorationForPython {

	public ExplorationForPythonSegregation(SegregationSimulationParameters lsp) {
		super(lsp);
	}
	
	@Override
	protected ExplorationSimulationModel copySimulation(ExplorationSimulationModel esm) {
		SimulationDataSegregation sdpp = (SimulationDataSegregation) esm.getData();
		SegregationExplorationSimulationModel pesm = new SegregationExplorationSimulationModel( 
				(SegregationSimulationParameters) parameters, new SimulationTimeStamp(esm.getCurrentTime()), 
				(SimulationDataSegregation) sdpp.clone());
		return pesm;
	}

	@Override
	public List<ExplorationSimulationModel> generateSimulation(int n) {
		List<ExplorationSimulationModel> res = new ArrayList<>();
		for (int i =0; i < n; i++) {
			res.add(
				new SegregationExplorationSimulationModel(
					(SegregationSimulationParameters) parameters,
					new SimulationTimeStamp(0),
					new SimulationDataSegregation(new SimulationTimeStamp(0), i)
				)
			);
		}
		return res;
	}

}
