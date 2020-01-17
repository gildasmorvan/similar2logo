package fr.univ_artois.lgi2a.similar2logo.examples.projettut.simulation;

import static spark.Spark.webSocket;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar.microkernel.libs.probes.RealTimeMatcherProbe;
import fr.univ_artois.lgi2a.similar2logo.examples.projettut.SimulationMain;
import fr.univ_artois.lgi2a.similar2logo.examples.projettut.model.SimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.projettut.probes.AngleDistanceProbe;
import fr.univ_artois.lgi2a.similar2logo.examples.projettut.probes.ResultProbe;
import fr.univ_artois.lgi2a.similar2logo.examples.projettut.probes.ResultWebSocket;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.Similar2LogoWebRunner;

public abstract class AbstractSimulationMain {
	
	public static void runSimulation(Class<? extends AbstractAgtDecisionModel> decisionModel) {
		webSocket("/result", ResultWebSocket.class);
		
		// Creation of the runner
		Similar2LogoWebRunner runner = new Similar2LogoWebRunner( );
		// Configuration of the runner
		runner.getConfig().setExportAgents( true );
		runner.getConfig().setExportMarks( true );
		runner.getConfig().setSimulationName("Control the robot");
		runner.getConfig().setCustomHtmlBody(SimulationMain.class.getResourceAsStream("gui.html"));
		// Creation of the model
		AbstractLogoSimulationModel model = new SimulationModel( new SimulationParameters(), decisionModel );
		// Initialize the runner with the model
		runner.initializeRunner( model );
		// Add other probes to the engine
		runner.addProbe("Real time matcher", new RealTimeMatcherProbe(20));
		runner.addProbe("Distance and angle printing", new AngleDistanceProbe());
		runner.addProbe("Win printing", new ResultProbe());
		// Open the GUI.
		runner.showView( );
	}

}
