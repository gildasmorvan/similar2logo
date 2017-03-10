package fr.lgi2a.similar2logo.examples.train;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.train.model.TrainCategory;
import fr.lgi2a.similar2logo.examples.train.model.TrainDecisionModel;
import fr.lgi2a.similar2logo.examples.train.model.TrainSimulationParameters;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

public class TrainSimulationModel extends LogoSimulationModel {

	public TrainSimulationModel(LogoSimulationParameters parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected EnvironmentInitializationData generateEnvironment( 
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels 
	) {
		TrainSimulationParameters param = (TrainSimulationParameters) simulationParameters;	
		EnvironmentInitializationData environmentInitializationData = super.generateEnvironment(simulationParameters, levels);
		LogoEnvPLS environment = (LogoEnvPLS) environmentInitializationData.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
		Point2D gare1 = new Point2D.Double(10,10);
		Point2D gare2 = new Point2D.Double(30,20);
		Point2D gare3 = new Point2D.Double(25,25);
		Point2D gare4 = new Point2D.Double(18,19);
		Point2D gare5 = new Point2D.Double(25,32);
		environment.getMarksAt((int) gare1.getX(), (int) gare1.getY()).add(new Mark<Double>(gare1, (double) 0, "Station"));
		environment.getMarksAt((int) gare2.getX(), (int) gare2.getY()).add(new Mark<Double>(gare2, (double) 0, "Station"));
		environment.getMarksAt((int) gare3.getX(), (int) gare3.getY()).add(new Mark<Double>(gare3, (double) 0, "Station"));
		environment.getMarksAt((int) gare4.getX(), (int) gare4.getY()).add(new Mark<Double>(gare4, (double) 0, "Station"));
		environment.getMarksAt((int) gare5.getX(), (int) gare5.getY()).add(new Mark<Double>(gare5, (double) 0, "Station"));
		for (int i=11; i <= 20; i++) {
			environment.getMarksAt(i, 10).add(new Mark<Double>(new Point2D.Double(i,10), (double) 0, "Rail"));
		}
		for (int i=1; i<10; i++) {
			environment.getMarksAt(20+i, 10+i).add(new Mark<Double>(new Point2D.Double(20+i,10+i), (double) 0, "Rail"));
		}
		for (int i=20 ; i < 32 ; i++ ) {
			if (i != 25)  {
				environment.getMarksAt(25, i).add(new Mark<Double>(new Point2D.Double(25,i), (double) 0, "Rail"));
			}
		}
		for (int i=19 ; i < 25; i++) {
			environment.getMarksAt(i, 19).add(new Mark<Double>(new Point2D.Double(i,19), (double) 0, "Rail"));
		}
		environment.getMarksAt(17, 18).add(new Mark<Double>(new Point2D.Double(17,18), (double) 0, "Rail"));
		environment.getMarksAt(16, 17).add(new Mark<Double>(new Point2D.Double(16,17), (double) 0, "Rail"));
		for (int i=11; i <17; i++) {
			environment.getMarksAt(16, i).add(new Mark<Double>(new Point2D.Double(16,i), (double) 0, "Rail"));
		}
		for (int i= 26; i < 30; i++ ) {
			environment.getMarksAt(i, 20).add(new Mark<Double>(new Point2D.Double(i,20), (double) 0, "Rail"));
		}
		param.stationList.add(gare1);
		param.stationList.add(gare2);
		param.stationList.add(gare3);
		param.stationList.add(gare4);
		param.nextStations.put(gare1,new ArrayList<Point2D>());
		param.nextStations.get(gare1).add(gare2);
		param.nextStations.get(gare1).add(gare3);
		param.nextStations.get(gare1).add(gare4);
		param.nextStations.put(gare2, new ArrayList<Point2D>());
		param.nextStations.get(gare2).add(gare1);
		param.nextStations.get(gare2).add(gare3);
		param.nextStations.get(gare2).add(gare4);
		param.nextStations.put(gare3, new ArrayList<Point2D>());
		param.nextStations.get(gare3).add(gare1);
		param.nextStations.get(gare3).add(gare2);
		param.nextStations.get(gare3).add(gare4);
		param.nextStations.get(gare3).add(gare5);
		param.nextStations.put(gare4, new ArrayList<Point2D>());
		param.nextStations.get(gare4).add(gare1);
		param.nextStations.get(gare4).add(gare2);
		param.nextStations.get(gare4).add(gare3);
		param.nextStations.put(gare5, new ArrayList<Point2D>());
		param.nextStations.get(gare5).add(gare3);
		return environmentInitializationData;
	}

	@Override
	protected AgentInitializationData generateAgents(ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		TrainSimulationParameters castedParameters = (TrainSimulationParameters) simulationParameters;
		AgentInitializationData result = new AgentInitializationData();	
		Random r = new Random();
		for(int i = 0; i < castedParameters.nbrTrain; i++) {
			Point2D gare3 = new Point2D.Double(25,25);
			Point2D des = castedParameters.nextStations.get(castedParameters.stationList.get(r.nextInt(castedParameters.stationList.size()))).get(0);
			// To edith if one day someone wants to add a new station
			result.getAgents().add(generateTrain(castedParameters, gare3, des));
		}
		return result;
		
	}
	
	/**
	 * Generate a train to the simulation
	 * @param param is a parameters of this simulation 
	 * @param x is a base on the x axe
	 * @param y is a base on the y axe
	 * @return the agents
	 */
	private static IAgent4Engine generateTrain(TrainSimulationParameters param, Point2D dep, Point2D arr){
		return TurtleFactory.generate(
			new TurtlePerceptionModel(
				Math.sqrt(2),Math.PI,true,true,true
			),
			new TrainDecisionModel(param, arr,dep),
			TrainCategory.CATEGORY,
			LogoEnvPLS.NORTH,//direction initiale
			1 ,
			0,
			dep.getX(),
			dep.getY()
		);
	}

}
