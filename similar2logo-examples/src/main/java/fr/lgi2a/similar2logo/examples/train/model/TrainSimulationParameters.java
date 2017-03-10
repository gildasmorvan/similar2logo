package fr.lgi2a.similar2logo.examples.train.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.Parameter;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;

public class TrainSimulationParameters extends LogoSimulationParameters {
	
	/**
	 * Number of trains in the simulation
	 */
	@Parameter(
			name = "Number of trains",
			description ="Number of trains in the simulation"
	)
	public int nbrTrain;
	
	/**
	 * List of the stations
	 */
	public List<Point2D> stationList;
	
	
	/**
	 * Permit to know what stations are linked
	 */
	public HashMap<Point2D,List<Point2D>> nextStations;
	
	/**
	 * Constructor of the trains parameter
	 */

	public TrainSimulationParameters () {
		super();
		this.nbrTrain = 3;
		this.gridHeight = 50;
		this.gridWidth = 50;
		this.initialTime = new SimulationTimeStamp( 0 );
		this.finalTime = new SimulationTimeStamp( 300000 );
		this.xTorus = false;
		this.yTorus = false;
		this.stationList= new ArrayList<Point2D>();
		this.nextStations = new HashMap<Point2D,List<Point2D>>();
	}
}
