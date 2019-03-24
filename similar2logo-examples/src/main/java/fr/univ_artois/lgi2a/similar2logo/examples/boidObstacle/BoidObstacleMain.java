package fr.univ_artois.lgi2a.similar2logo.examples.boidObstacle;

import fr.univ_artois.lgi2a.similar2logo.examples.boidObstacle.model.BoidSimulationObstacleParameters;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.Similar2LogoWebRunner;


public final class BoidObstacleMain {

	/**
	 * Private Constructor to prevent class instantiation.
	 */
	private BoidObstacleMain() {
	}

	/**
	 * Main class of the simulation
	 */
	public static void main(String[] args) {
		// Creation of the runner
		Similar2LogoWebRunner runner = new Similar2LogoWebRunner();

		runner.getConfig().setCustomHtmlBody(BoidObstacleMain.class.getResourceAsStream("boidsgui.html"));
		
		runner.getConfig().setExportAgents(true);
		runner.getConfig().setExportMarks(true);
		// Creation of the model
		BoidsObstacleSimulationModel model = new BoidsObstacleSimulationModel(new BoidSimulationObstacleParameters());
		// Initialize the runner with the model
		runner.initializeRunner(model);
		// Open the GUI.
		runner.showView();
	}
}

