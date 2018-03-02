package fr.lgi2a.similar2logo.examples.predation.exploration;

import java.util.List;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.ExplorationSimulationModel;

public class TestPredationExploration {

	public static void main(String[] args) {
		PredationSimulationParameters param = new PredationSimulationParameters();
		ExplorationForPythonPreyPredator exploration = new ExplorationForPythonPreyPredator(param);
		List<ExplorationSimulationModel> list_simu = exploration.generateSimulation(1);
		//System.out.println(list_simu.get(0).getData().getAgents().size());
		SimulationTimeStamp time_stamp = new SimulationTimeStamp(10);
		exploration.setNextStep(time_stamp);
		exploration.runSimulations(list_simu);
		while(!exploration.isExplorationOver(list_simu)) {
		}
		System.out.println("block ?");
		list_simu.set(0, exploration.copySimulation(list_simu.get(0)));
		System.out.println(list_simu.size());
		time_stamp = new SimulationTimeStamp(10);
		exploration.setNextStep(time_stamp);
		exploration.runSimulations(list_simu);
		

	}

}
