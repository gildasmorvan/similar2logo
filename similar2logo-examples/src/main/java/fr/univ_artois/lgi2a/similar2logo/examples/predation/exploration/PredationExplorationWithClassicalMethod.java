/**
 * Copyright or © or Copr. LGI2A
 *
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 *
 * Email: gildas.morvan@univ-artois.fr
 *
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 *
 * This software is a computer program whose purpose is to support the
 * implementation of Logo-like simulations using the SIMILAR API.
 * This software defines an API to implement such simulations, and also
 * provides usage examples.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.univ_artois.lgi2a.similar2logo.examples.predation.exploration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.RandomWalkPredationSimulationMain;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.initializations.RandomWalkPredationSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.probes.PreyPredatorPopulationProbe;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.html.Similar2LogoHtmlRunner;

/**
 * The main class of the exploration of the predation model using the classical method 
 * (cf. J. Banks, J.S. Carson II, B.L. Nelson, and D.M. Nicol. Discrete-event system
 * simulation. Pearson, 5th edition, 2010.).
 * 
 * The model parameters have been tuned to produce 3 types of solutions:
 * <ul>
 * <li>preys and predators survive</li>
 * <li>preys survive and predators die</li>
 * <li>preys and predators die</li>
 * </ul>
 * 
 * The simulations are executed 3600 times to be able to correctly estimate the mean values
 * of prey and predator populations as well as the quantity of grass in the environment.
 * 
 * The number of replications has been determined from a variance analysis performed on 150
 * initial replications:
 * <table summary="Results used to determine the number of replications">
 *   <th>
 *      <td>observable</td>
 *      <td>variance</td>
 *      <td>mean</td>
 *      <td>nb of replications</td>
 *   </th>
 *   <tr>
 *      <td>prey pop.</td>
 *      <td>697.8284</td>
 *      <td>783.7667</td>
 *      <td>1219</td>
 *   </tr>
 *   <tr>
 *      <td>predator pop.</td>
 *      <td>196.9461</td>
 *      <td>128.6733</td>
 *      <td>3600</td>
 *   </tr>
 *   <tr>
 *      <td>vegetation</td>
 *      <td>342.1814</td>
 *      <td>700.5257</td>
 *      <td>367</td>
 *   </tr>
 * </table> 
 * 
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class PredationExplorationWithClassicalMethod {
	
	/**
	 * The main method of the exploration of the predation model.
	 * @param args The command line arguments.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// Creation of the runner
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		
		PredationSimulationParameters parameters = new PredationSimulationParameters();
		
		parameters.finalTime = new SimulationTimeStamp(1000);
		parameters.predatorReproductionRate=0.044;
		
		// Creation of the model
		AbstractLogoSimulationModel model = new RandomWalkPredationSimulationModel( parameters);
		// Configuration of the runner
		runner.getConfig().setCustomHtmlBody(
			RandomWalkPredationSimulationMain.class.getResourceAsStream("predationgui.html")
		);
		// Initialize the runner
		runner.initializeRunner( model );
		IProbe populationProbe = new PreyPredatorPopulationProbe();
		// Add other probes to the engine
		runner.addProbe("Population printing", populationProbe);
		for(int i = 0; i<3600; i++) {
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
