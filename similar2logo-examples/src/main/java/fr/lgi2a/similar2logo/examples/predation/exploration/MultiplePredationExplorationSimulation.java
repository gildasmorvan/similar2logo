package fr.lgi2a.similar2logo.examples.predation.exploration;

import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.MultipleExplorationSimulation;

public class MultiplePredationExplorationSimulation extends MultipleExplorationSimulation {
	
	public MultiplePredationExplorationSimulation(int nbrSimulations,
			SimulationTimeStamp end, List<SimulationTimeStamp> pauses) {
		super(new PredationSimulationParameters(), nbrSimulations, end, pauses);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addNewSimulation() {
		this.simulations.add(new PredationExplorationSimulationModel((PredationSimulationParameters) this.parameters, this.currentTime, endTime));
	}
	
	public static void main (String[] args) {
		System.out.println("C'est le debut des emmerdes...");
		List<SimulationTimeStamp> p = new ArrayList<>();
		p.add(new SimulationTimeStamp(10));
		p.add(new SimulationTimeStamp(15));
		MultiplePredationExplorationSimulation mpes = new MultiplePredationExplorationSimulation(1, new SimulationTimeStamp(20), p);
		mpes.runSimulations();
		System.out.println("Bon bah j'ai rien dit...");
	}

}
