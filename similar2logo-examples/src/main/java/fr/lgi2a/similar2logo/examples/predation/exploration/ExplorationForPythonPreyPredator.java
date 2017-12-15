package fr.lgi2a.similar2logo.examples.predation.exploration;

import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.predation.exploration.data.SimulationDataPreyPredator;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.ExplorationForPython;
import fr.lgi2a.similar2logo.lib.exploration.ExplorationSimulationModel;

/**
 * Class for the prey predator exploration in python
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class ExplorationForPythonPreyPredator extends ExplorationForPython {

	public ExplorationForPythonPreyPredator(PredationSimulationParameters lsp) {
		super(lsp);
	}
	
	@Override
	protected ExplorationSimulationModel copySimulation(ExplorationSimulationModel esm) {
		SimulationDataPreyPredator sdpp = (SimulationDataPreyPredator) esm.getData();
		PredationExplorationSimulationModel pesm = new PredationExplorationSimulationModel( 
				(PredationSimulationParameters) parameters, new SimulationTimeStamp(esm.getCurrentTime()), 
				(SimulationDataPreyPredator) sdpp.clone());
		return pesm;
	}

	@Override
	public List<ExplorationSimulationModel> generateSimulation(int n) {
		List<ExplorationSimulationModel> res = new ArrayList<>();
		for (int i =0; i < n; i++) {
			new PredationExplorationSimulationModel((PredationSimulationParameters) parameters, new SimulationTimeStamp(0),
					new SimulationDataPreyPredator(new SimulationTimeStamp(0), i));
		}
		return res;
	}

}
