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
package fr.lgi2a.similar2logo.examples.multiturmite;

import java.awt.Color;
import java.awt.geom.Point2D;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeImageSwingJFrame;
import fr.lgi2a.similar2logo.examples.multiturmite.initializations.MultiTurmiteSimulationModel;
import fr.lgi2a.similar2logo.examples.multiturmite.model.MultiTurmiteSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.lib.probes.GridSwingView;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * 
 * Defines an instance of the multi-turmite model with four turtles.
 * This simulation results different cyclic or environment-filling behaviors
 * according to the values of parameters <code>inverseMarkUpdate</code>
 * and <code>removeDirectionChange</code>.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class NRandomTurmitesSimulationMain {

	/**
	 * Private Constructor to prevent class instantiation.
	 */
	private NRandomTurmitesSimulationMain() {	
	}
	
	/**
	 * The main method of the simulation.
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		MultiTurmiteSimulationParameters parameters = new MultiTurmiteSimulationParameters();
		parameters.initialTime = new SimulationTimeStamp( 0 );
		parameters.finalTime = new SimulationTimeStamp( 100000 );
		parameters.xTorus = true;
		parameters.yTorus = true;
		parameters.gridHeight = 100;
		parameters.gridWidth = 100;
		parameters.nbOfTurmites = 6;
		parameters.inverseMarkUpdate = true;
		parameters.removeDirectionChange = false;
		
		//Create a specific instance
		for(int i = 0; i < parameters.nbOfTurmites; i++) {
			double x = parameters.gridWidth*RandomValueFactory.getStrategy().randomDouble();
			double y = parameters.gridHeight*RandomValueFactory.getStrategy().randomDouble();
			parameters.initialLocations.add(new Point2D.Double(x,y));
			parameters.initialDirections.add(LogoEnvPLS.NORTH);
		}
		
		// Register the parameters to the agent factories.
		TurtleFactory.setParameters( parameters );
		// Create the simulation engine that will run simulations
		ISimulationEngine engine = new EngineMonothreadedDefaultdisambiguation( );
		// Create the probes that will listen to the execution of the simulation.
		engine.addProbe( 
			"Error printer", 
			new ProbeExceptionPrinter( )
		);
		engine.addProbe(
			"Trace printer", 
			new ProbeExecutionTracker( System.err, false )
		);
		engine.addProbe(
			"Swing view",
			new ProbeImageSwingJFrame( 
				"Logo level",
				new GridSwingView(
					Color.WHITE,
					true
				)
			)
		);

		// Create the simulation model being used.
		MultiTurmiteSimulationModel simulationModel = new MultiTurmiteSimulationModel(
			parameters
		);
		// Run the simulation.
		engine.runNewSimulation( simulationModel );

	}

}
