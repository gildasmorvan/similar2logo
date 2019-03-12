package fr.univ_artois.lgi2a.similar2logo.examples.boidObstacle;

import java.awt.geom.Point2D;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;
import fr.univ_artois.lgi2a.similar2logo.examples.boidObstacle.model.BoidDecisionObstacleModel;
import fr.univ_artois.lgi2a.similar2logo.examples.boidObstacle.model.BoidSimulationObstacleParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.boids.BoidsSimulationModel;
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

	/* (non-Javadoc)
	 * @see fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel#generateEnvironment(fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters, java.util.Map)
	 */
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
		for(x = 10; x < 100; x += 10)
		{
			for (double y = 0; y < 10 ; y+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
				
			}
			
		} // Tracé verticale
		
		double a = 20;
			for(int u = 0; u<=3; u++)
			{
			int y = 10; 
			for (x = a; x < (a+5) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 20;
			
		} // Tracé horizontale
		
		a = 5;
		for(int u = 0; u<=3; u++)
		{
			int y = 15; 
			for (x = a; x < (a+10) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 25;
			
		} // Tracé horizontale milieu
		
		
		
		for(x = 5; x < 100; x += 15)
		{
			for (double y = 20; y < 30 ; y++) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			
		}
		
		a = 10;
		for(int u = 0; u<=3; u++)
		{
			int y = 20; 
			for (x = a; x < (a+10) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 20;
			
		} // Tracé horizontale
		
		for(x = 15; x < 100; x += 15)
		{
			for (double y = 40; y < 55 ; y++) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			
		}
		
		a = 10;
		for(int u = 0; u<=2; u++)
		{
			int y = 35; 
			for (x = a; x < (a+20) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 30;
		
		} // Tracé horizontale milieu
		
		
		for(x = 45; x <= 60; x++)
		{
			int y = 40;
			environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
		} // Tracé horizontale
		
		
		a = 15;
		for(int u = 0; u<2; u++)
		{
			int y = 55; 
			for (x = a; x < (a+16) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 60;
			
		} // Tracé horizontale
		
		
		for(x = 7; x < 100; x += 14)
		{
			for (int y = 60; y < 70 ; y++) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			
		}
		
		a = 21;
		for(int u = 0; u<=2; u++)
		{
			int y = 60; 
			for (x = a; x < (a+7) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 28;
			
		} // Tracé horizontale
		
		a = 7;
		for(int u = 0; u<=2; u++)
		{
			int y = 70; 
			for (x = a; x < (a+7) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 28;
			
		} // Tracé horizontale
	
		a = 0;
		for(int u = 0; u<=2; u++)
		{
			int y = 75; 
			for (x = a; x < (a+25) ; x+=0.1) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			a += 35;
			
		} // Tracé horizontale milieu
		
		for(x = 10; x < 100; x += 20)
		{
			for (int y = 80; y < 100 ; y++) {

				environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
			}
			
		}
		
		for(x = 30; x <= 50; x++)
		{
			int y = 80;
			environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
		} // Tracé horizontale
		
		for(x = 70; x <= 90; x++)
		{
			int y = 80;
			environment.getMarksAt(x, y).add(new Mark<>(new Point2D.Double(x, y)));
		} // Tracé horizontale


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