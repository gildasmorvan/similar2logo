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
package fr.lgi2a.similar2logo.examples.train;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.train.model.TrainCategory;
import fr.lgi2a.similar2logo.examples.train.model.TrainDecisionModel;
import fr.lgi2a.similar2logo.examples.train.model.TrainReactionModel;
import fr.lgi2a.similar2logo.examples.train.model.TrainSimulationParameters;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;

/**
 * The simulation model of "train" simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TrainSimulationModel extends LogoSimulationModel {
	
	private HashMap<Point2D,Double> angleDepart;

	/**
	 * Constructor of train simulation model
	 * @param parameters
	 */
	public TrainSimulationModel(LogoSimulationParameters parameters) {
		super(parameters);
		this.angleDepart = new HashMap<Point2D,Double>();
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
		angleDepart.put(gare1, LogoEnvPLS.EAST);
		Point2D gare2 = new Point2D.Double(30,20);
		angleDepart.put(gare2, LogoEnvPLS.WEST);
		Point2D gare3 = new Point2D.Double(25,25);
		angleDepart.put(gare3, LogoEnvPLS.EAST);
		Point2D gare4 = new Point2D.Double(18,19);
		angleDepart.put(gare4, LogoEnvPLS.SOUTH); //A corriger quand le nord sera au nord
		Point2D gare5 = new Point2D.Double(25,32);
		angleDepart.put(gare5, LogoEnvPLS.SOUTH); //A corriger quand le nord sera au nord
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
		for (int i= 26; i < 27; i++ ) {
			environment.getMarksAt(i, 20).add(new Mark<Double>(new Point2D.Double(i,20), (double) 0, "Rail"));
		}
		environment.getMarksAt(27, 21).add(new Mark<Double>(new Point2D.Double(27,21), (double) 0, "Rail"));
		environment.getMarksAt(28, 21).add(new Mark<Double>(new Point2D.Double(28,21), (double) 0, "Rail"));
		environment.getMarksAt(29, 21).add(new Mark<Double>(new Point2D.Double(29,21), (double) 0, "Rail"));
		param.stationList.add(gare1);
		param.stationList.add(gare2);
		param.stationList.add(gare3);
		param.stationList.add(gare4);
		param.stationList.add(gare5);
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
		if (castedParameters.nbrTrain > 5) castedParameters.nbrTrain = 5;
		Random r = new Random();
		List<Point2D> freeStations = new ArrayList<>();
		for (int i =0; i < 5; i++) {
			freeStations.add(castedParameters.stationList.get(i));
		}
		for(int i = 0; i < castedParameters.nbrTrain; i++) {
			System.out.println(i+" : "+freeStations);
			int rank = r.nextInt(freeStations.size());
			Point2D gare = freeStations.get(rank);
			List<Point2D> possibleStations = castedParameters.nextStations.get(gare);
			Point2D des = possibleStations.get(r.nextInt(possibleStations.size()));
			freeStations.remove(rank);
			// To edith if one day someone wants to add a new station
			result.getAgents().add(generateTrain(castedParameters, gare, des, this.angleDepart.get(gare)));
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
	private static IAgent4Engine generateTrain(TrainSimulationParameters param, Point2D dep, Point2D arr, double angle){
		return TurtleFactory.generate(
			new TurtlePerceptionModel(
				Math.sqrt(2),Math.PI,true,true,true
			),
			new TrainDecisionModel(param, arr,dep),
			TrainCategory.CATEGORY,
			angle ,//direction initiale
			1 ,
			0,
			dep.getX(),
			dep.getY()
		);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters) {
		TrainSimulationParameters castedSimulationParameters = (TrainSimulationParameters) simulationParameters;
		ExtendedLevel logo = new ExtendedLevel(
				castedSimulationParameters.getInitialTime(), 
				LogoSimulationLevelList.LOGO, 
				new PeriodicTimeModel( 
					1, 
					0, 
					castedSimulationParameters.getInitialTime()
				),
				new TrainReactionModel()
			);
		List<ILevel> levelList = new LinkedList<ILevel>();
		levelList.add(logo);
		return levelList;
	}

}
