package fr.univ_artois.lgi2a.similar2logo.examples.mle;

import fr.univ_artois.lgi2a.similar2logo.examples.mle.model.MLESimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.mle.probes.LevelOfEmergenceProbe;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.Similar2LogoWebRunner;

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
		Similar2LogoWebRunner runner = new Similar2LogoWebRunner( );
		// Configuration of the runner
		runner.getConfig().setCustomHtmlBody( MLSimulationMain.class.getResourceAsStream("mlegui.html") );

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
