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
package fr.univ_artois.lgi2a.similar2logo.examples.testperceptionmodel;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.probes.Slf4jExceptionPrinter;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.probes.Slf4jExecutionTracker;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.univ_artois.lgi2a.similar.microkernel.libs.probes.RealTimeMatcherProbe;
import fr.univ_artois.lgi2a.similar2logo.examples.passive.PassiveTurtleSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;

/**
 * The main class of the "Passive turtle" simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public final class TestPerceptionSimulationMain {

	/**
	 * Private Constructor to prevent class instantiation.
	 */
	private TestPerceptionSimulationMain() {	
	}
	
	/**
	 * The main method of the simulation.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		
		// Create the simulation engine that will run simulations
		ISimulationEngine engine = new EngineMonothreadedDefaultdisambiguation( );

		// Create the probes that will listen to the execution of the simulation.
		engine.addProbe( 
			"Error printer", 
			new Slf4jExceptionPrinter( )
		);
		engine.addProbe(
			"Trace printer", 
			new Slf4jExecutionTracker( false )
		);
		engine.addProbe(
			"Real time matcher", 
			new RealTimeMatcherProbe(10)
		);
		
		// Create the parameters used in this simulation.
		PassiveTurtleSimulationParameters parameters = new PassiveTurtleSimulationParameters();
		parameters.initialTime = new SimulationTimeStamp( 0 );
		parameters.finalTime = new SimulationTimeStamp( 10 );
		parameters.initialSpeed = 1;
		parameters.initialDirection = LogoEnvPLS.SOUTH;
		parameters.xTorus = true;
		parameters.yTorus = true;
		parameters.gridHeight = 8;
		parameters.gridWidth = 8;
		parameters.initialX=parameters.gridWidth/2.0;
		parameters.initialY=parameters.gridHeight/2.0;

		// Create the simulation model being used.
		TestPerceptionSimulationModel simulationModel = new TestPerceptionSimulationModel(
			parameters
		);
		
		// Run the simulation.
		engine.runNewSimulation( simulationModel );
		

	}

}
