package fr.lgi2a.similar2logo.examples.segregation.exploration;

import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.segregation.model.SegregationSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.AbstractExplorationForPython;
import fr.lgi2a.similar2logo.lib.exploration.AbstractExplorationSimulationModel;

/**
 * Class for segregation exploration in python
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ExplorationForPythonSegregation extends AbstractExplorationForPython {

	public ExplorationForPythonSegregation(SegregationSimulationParameters lsp) {
		super(lsp);
	}
	
	@Override
	protected AbstractExplorationSimulationModel copySimulation(AbstractExplorationSimulationModel esm) {
		SimulationDataSegregation sdpp = (SimulationDataSegregation) esm.getData();
		SegregationExplorationSimulationModel pesm = new SegregationExplorationSimulationModel( 
				(SegregationSimulationParameters) parameters, new SimulationTimeStamp(esm.getCurrentTime()), 
				(SimulationDataSegregation) sdpp.clone());
		return pesm;
	}

	@Override
	public List<AbstractExplorationSimulationModel> generateSimulation(int n) {
		List<AbstractExplorationSimulationModel> res = new ArrayList<>();
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
