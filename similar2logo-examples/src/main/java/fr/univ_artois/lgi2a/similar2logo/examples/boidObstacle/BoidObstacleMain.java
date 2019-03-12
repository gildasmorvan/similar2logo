package fr.univ_artois.lgi2a.similar2logo.examples.boidObstacle;

import java.io.IOException;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.ResourceNotFoundException;
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

		
		try {
			
			runner.getConfig().setCustomHtmlBody(BoidObstacleMain.class.getResourceAsStream("boidsgui.html"));
			
		} catch (IOException e) {
			throw new ResourceNotFoundException(e);
		}
		
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

