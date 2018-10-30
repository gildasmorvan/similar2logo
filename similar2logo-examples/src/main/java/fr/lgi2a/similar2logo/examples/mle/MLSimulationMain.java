package fr.lgi2a.similar2logo.examples.mle;

import java.io.IOException;

import fr.lgi2a.similar2logo.examples.mle.model.MLESimulationParameters;
import fr.lgi2a.similar2logo.examples.mle.probes.LevelOfEmergenceProbe;
import fr.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.lgi2a.similar2logo.lib.tools.html.ResourceNotFoundException;
import fr.lgi2a.similar2logo.lib.tools.html.Similar2LogoHtmlRunner;

public class MLSimulationMain {
	/**
	 * Private Constructor to prevent class instantiation.
	 */
	private MLSimulationMain() {	
	}
	
	/**
	 * The main method of the simulation.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Creation of the runner
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		// Configuration of the runner
		try {
			runner.getConfig().setCustomHtmlBody( MLSimulationMain.class.getResourceAsStream("mlegui.html") );
		} catch (IOException e) {
			throw new ResourceNotFoundException(e);
		}
		runner.getConfig().setExportAgents( true );
		runner.getConfig().setExportPheromones( true );
		// Creation of the model
		MLESimulationParameters parameters = new MLESimulationParameters() ;
		AbstractLogoSimulationModel model = new MLESimulationModel( parameters);
		// Initialize the runner with the model
		runner.initializeRunner( model );
		runner.addProbe("emergence level", new LevelOfEmergenceProbe(parameters));
		// Open the GUI.
		runner.showView( );
	}
}