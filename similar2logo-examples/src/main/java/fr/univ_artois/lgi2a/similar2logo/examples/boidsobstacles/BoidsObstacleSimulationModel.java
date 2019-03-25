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
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
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
package fr.univ_artois.lgi2a.similar2logo.examples.boidsobstacles;

import java.awt.geom.Point2D;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;
import fr.univ_artois.lgi2a.similar2logo.examples.boids.BoidsSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.examples.boidsobstacles.model.BoidDecisionObstacleModel;
import fr.univ_artois.lgi2a.similar2logo.examples.boidsobstacles.model.BoidSimulationObstacleParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel;

public class BoidsObstacleSimulationModel extends BoidsSimulationModel {

	public BoidsObstacleSimulationModel(LogoSimulationParameters parameters) {
		super(parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EnvironmentInitializationData generateEnvironment(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {

		EnvironmentInitializationData environmentInitializationData = 
				super.generateEnvironment(simulationParameters, levels);
		LogoEnvPLS environment = 
		(LogoEnvPLS) environmentInitializationData.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
		
		/**
		 *  horizontal and vertical layout.
		 */
		
		double x;
		//Vertical line
		for(x = 10; x < 100; x += 10) {
			for (double y = 0; y < 10 ; y+=0.1) {
				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
				
			}
			
		} 
		
		double a = 20;
		//Horizontal line
		for(int u = 0; u<=3; u++) {
			int y = 10; 
			for (x = a; x < (a+5) ; x+=0.1) {
				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 20;
			
		} 
		
		a = 5;
		//Horizontal line middle
		for(int u = 0; u<=3; u++) {
			int y = 15; 
			for (x = a; x < (a+10) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 25;
			
		} 	
		
		for(x = 5; x < 100; x += 15) {
			for (double y = 20; y < 30 ; y++) {
				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			
		}
		
		a = 10;
		//Horizontal line
		for(int u = 0; u<=3; u++) {
			int y = 20; 
			for (x = a; x < (a+10) ; x+=0.1) {
				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 20;
			
		} 
		
		for(x = 15; x < 100; x += 15) {
			for (double y = 40; y < 55 ; y++) {
				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			
		}
		
		a = 10;
		//Horizontal line middle
		for(int u = 0; u<=2; u++) {
			int y = 35; 
			for (x = a; x < (a+20) ; x+=0.1) {
				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 30;
		
		} 
		
		//Horizontal line
		for(x = 45; x <= 60; x++) {
			int y = 40;
			environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
		} 
		
		
		a = 15;
		//Horizontal line
		for(int u = 0; u<2; u++) {
			int y = 55; 
			for (x = a; x < (a+16) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 60;
			
		} 
		
		
		for(x = 7; x < 100; x += 14){
			for (int y = 60; y < 70 ; y++) {
				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}	
		}
		
		a = 21;
		//Horizontal line
		for(int u = 0; u<=2; u++) {
			int y = 60; 
			for (x = a; x < (a+7) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 28;
			
		} 
		
		a = 7;
		//Horizontal line
		for(int u = 0; u<=2; u++) {
			int y = 70; 
			for (x = a; x < (a+7) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 28;
			
		} 
	
		a = 0;
		//Horizontal line middle
		for(int u = 0; u<=2; u++) {
			int y = 75; 
			for (x = a; x < (a+25) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 35;
			
		} 
		
		for(x = 10; x < 100; x += 20) {
			for (int y = 80; y < 100 ; y++) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			
		}
		
		//Horizontal line
		for(x = 30; x <= 50; x++) {
			int y = 80;
			environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
		} 
		
		for(x = 70; x <= 90; x++) {
			int y = 80;
			environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
		} //Horizontal line


		return environmentInitializationData;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	protected AgentInitializationData generateAgents(
			ISimulationParameters parameters,
			Map<LevelIdentifier, ILevel> levels) {
		BoidSimulationObstacleParameters castedParameters = (BoidSimulationObstacleParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();
		for (int i = 0; i < castedParameters.nbOfAgents; i++) {
			result.getAgents().add(generateBoid(castedParameters));
		}
		return result;
	}

	/**
	 * @param p
	 *            The parameters of the simulation model.
	 * @return a new boid located at the center of the grid.
	 */
	protected static IAgent4Engine generateBoid(BoidSimulationObstacleParameters p) {
		return TurtleFactory.generate(new ConeBasedPerceptionModel(
				p.attractionDistance, p.perceptionAngle, true, true, false),
				new BoidDecisionObstacleModel(p), new AgentCategory("b",
				TurtleAgentCategory.CATEGORY), PRNG.randomAngle(),
				p.minInitialSpeed + PRNG.randomDouble()
				* (p.maxInitialSpeed - p.minInitialSpeed), 0,
				PRNG.randomDouble() * p.gridWidth, PRNG.randomDouble()* p.gridHeight);
	}

}