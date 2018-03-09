package fr.lgi2a.similar2logo.examples.predation.exploration;

import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.predation.exploration.data.SimulationDataPreyPredator;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.AbstractExplorationForPython;
import fr.lgi2a.similar2logo.lib.exploration.AbstractExplorationSimulationModel;

/**
 * Class for the prey predator exploration in python
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class ExplorationForPythonPreyPredator extends AbstractExplorationForPython {

	public ExplorationForPythonPreyPredator(PredationSimulationParameters lsp) {
		super(lsp);
	}
	
	@Override
	protected AbstractExplorationSimulationModel copySimulation(AbstractExplorationSimulationModel esm) {
		SimulationDataPreyPredator sdpp = (SimulationDataPreyPredator) esm.getData();
		return new PredationExplorationSimulationModel( 
			(PredationSimulationParameters) parameters,
			new SimulationTimeStamp(esm.getCurrentTime()), 
			(SimulationDataPreyPredator) sdpp.clone()
		);
	}

	@Override
	public List<AbstractExplorationSimulationModel> generateSimulation(int n) {
		List<AbstractExplorationSimulationModel> res = new ArrayList<>();
		for (int i =0; i < n; i++) {
			res.add(
				new PredationExplorationSimulationModel(
					(PredationSimulationParameters) parameters,
					new SimulationTimeStamp(0),
					new SimulationDataPreyPredator(new SimulationTimeStamp(0), i)
				)
			);
		}
		return res;
	}

}
