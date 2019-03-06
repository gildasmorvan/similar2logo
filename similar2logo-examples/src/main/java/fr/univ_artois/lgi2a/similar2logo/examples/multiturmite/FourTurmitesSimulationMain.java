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
package fr.univ_artois.lgi2a.similar2logo.examples.multiturmite;

import java.awt.geom.Point2D;

import fr.univ_artois.lgi2a.similar2logo.examples.multiturmite.model.MultiTurmiteSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.lib.probes.LogoRealTimeMatcher;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.Similar2LogoWebRunner;

/**
 * 
 * Defines an instance of the multi-turmite model with four turtles.
 * This simulation results different cyclic or environment-filling behaviors
 * according to the values of parameters <code>inverseMarkUpdate</code>
 * and <code>removeDirectionChange</code>.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public final class FourTurmitesSimulationMain {

	/**
	 * Private Constructor to prevent class instantiation.
	 */
	private FourTurmitesSimulationMain() {	
	}
	
	/**
	 * The main method of the simulation.
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		// Creation of the runner
		Similar2LogoWebRunner runner = new Similar2LogoWebRunner( );
		// Configuration of the runner
		runner.getConfig().setExportAgents( true );
		runner.getConfig().setExportMarks( true );
		
		// Creation of the simulation parameters
		MultiTurmiteSimulationParameters parameters = new MultiTurmiteSimulationParameters();
		parameters.xTorus = true;
		parameters.yTorus = true;
		parameters.gridHeight = 60;
		parameters.gridWidth = 60;
		parameters.nbOfTurmites = 4;
		parameters.inverseMarkUpdate = true;
		parameters.removeDirectionChange = false;
		
		//Create a specific instance
		parameters.initialLocations.add(
			new Point2D.Double(
				Math.floor(parameters.gridWidth/2.0),
				Math.floor(parameters.gridHeight/2.0)
			)
		);
		parameters.initialDirections.add(LogoEnvPLS.NORTH);
		parameters.initialLocations.add(
			new Point2D.Double(
				Math.floor(parameters.gridWidth/2.0),
				Math.floor(parameters.gridHeight/2.0) + 1
			)
		);
		parameters.initialDirections.add(LogoEnvPLS.SOUTH);
		parameters.initialLocations.add(
			new Point2D.Double(
				Math.floor(parameters.gridWidth/2.0) + 10,
				Math.floor(parameters.gridHeight/2.0)
			)
		);
		parameters.initialDirections.add(LogoEnvPLS.NORTH);
		parameters.initialLocations.add(
			new Point2D.Double(
				Math.floor(parameters.gridWidth/2.0) + 10,
				Math.floor(parameters.gridHeight/2.0) + 1
			)
		);
		parameters.initialDirections.add(LogoEnvPLS.SOUTH);
		
		// Create the simulation model being used.
		MultiTurmiteSimulationModel model = new MultiTurmiteSimulationModel(
			parameters
		);
		
		// Initialize the runner with the model
		runner.initializeRunner( model );
		// Add other probes to the engine
		runner.addProbe("Real time matcher", new LogoRealTimeMatcher(20));
		// Open the GUI.
		runner.showView( );
		
	}

}
