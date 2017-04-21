package fr.lgi2a.similar2logo.examples.predation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.predation.initializations.RandomWalkPredationSimulationModel;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.examples.predation.probes.PreyPredatorPopulationProbe;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.lib.tools.html.Similar2LogoHtmlRunner;

public class ExplorationPredationMain {
	public static void main(String[] args) throws IOException {
		// Creation of the runner
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		
		PredationSimulationParameters parameters = new PredationSimulationParameters();
		
		parameters.finalTime = new SimulationTimeStamp(1000);
		parameters.predatorReproductionRate=0.044;
		
		// Creation of the model
		LogoSimulationModel model = new RandomWalkPredationSimulationModel( parameters);
		// Configuration of the runner
		runner.getConfig().setCustomHtmlBody( RandomWalkPredationSimulationMain.class.getResourceAsStream("predationgui.html") );
		// Initialize the runner
		runner.initializeRunner( model );
		IProbe populationProbe = new PreyPredatorPopulationProbe();
		// Add other probes to the engine
		runner.addProbe("Population printing", populationProbe);
		for(int i = 0; i<150; i++) {
			System.out.println("Start Simulation "+i);
			runner.getEngine().runNewSimulation(model);
			URL obj = new URL("http://localhost:8080/result.txt");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			PrintWriter writer = new PrintWriter("result"+i+".txt", "UTF-8");
			while ((inputLine = in.readLine()) != null) {
				writer.println(inputLine);
			}
			in.close();

			writer.close();
		}
	}
}
