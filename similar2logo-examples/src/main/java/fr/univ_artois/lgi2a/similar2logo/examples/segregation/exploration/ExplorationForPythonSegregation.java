package fr.univ_artois.lgi2a.similar2logo.examples.segregation.exploration;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.lgi2a.similar2logo.examples.segregation.model.SegregationSimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.AbstractExplorationForPython;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.AbstractExplorationSimulationModel;

/**
 * Class for segregation exploration in python
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ExplorationForPythonSegregation extends AbstractExplorationForPython {

	public ExplorationForPythonSegregation(SegregationSimulationParameters lsp) {
		super(lsp);
	}
	
	/**
	 * Makes n copies of the simulation
	 * @param esm the simulation to copy
	 * @param n the number to copy to produce
	 * @return a list with all the copies
	 */
	public List<AbstractExplorationSimulationModel> makeCopies (SegregationExplorationSimulationModel esm, int n) {
		SimulationDataSegregation sdpp = (SimulationDataSegregation) esm.getData();
		sdpp.setWeight(sdpp.getWeight()/n);
		return super.makeCopies(esm, n);
	}

	@Override
	protected AbstractExplorationSimulationModel copySimulation(AbstractExplorationSimulationModel esm) {
		SimulationDataSegregation sdpp = (SimulationDataSegregation) esm.getData();
		return new SegregationExplorationSimulationModel( 
			(SegregationSimulationParameters) parameters,
			new SimulationTimeStamp(esm.getCurrentTime()), 
			(SimulationDataSegregation) sdpp.clone()
		);
	}

	@Override
	public List<AbstractExplorationSimulationModel> generateSimulation(int n) {
		List<AbstractExplorationSimulationModel> res = new ArrayList<>();
		for (int i =0; i < n; i++) {
			SimulationDataSegregation sdpp = new SimulationDataSegregation(new SimulationTimeStamp(0), i);
			sdpp.setWeight((float) 1./n);
			res.add(
				new SegregationExplorationSimulationModel(
					(SegregationSimulationParameters) parameters,
					new SimulationTimeStamp(0),
					sdpp
				)
			);
		}
		return res;
	}

}
