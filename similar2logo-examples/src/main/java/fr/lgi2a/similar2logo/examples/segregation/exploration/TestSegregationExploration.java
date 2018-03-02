package fr.lgi2a.similar2logo.examples.segregation.exploration;

import java.util.List;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.segregation.model.SegregationSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.ExplorationSimulationModel;

public class TestSegregationExploration {

	public static void main(String[] args) {
		SegregationSimulationParameters param = new SegregationSimulationParameters();
		param.similarityRate = 0.5;
		ExplorationForPythonSegregation exploration = new ExplorationForPythonSegregation(param);
		List<ExplorationSimulationModel> list_simu = exploration.generateSimulation(100);
		for(int i = 0; i < 10; i++) {
			SimulationTimeStamp time_stamp = new SimulationTimeStamp(10);
			exploration.setNextStep(time_stamp);
			exploration.runSimulations(list_simu);
			while(!exploration.isExplorationOver(list_simu)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();	
				}
			}
			System.out.println("copy");
			for(int j = 0; j < 100; j++)
				list_simu.set(j, exploration.copySimulation(list_simu.get(j)));
		}

	}

}
