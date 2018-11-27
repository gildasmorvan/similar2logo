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
package fr.univ_artois.lgi2a.similar2logo.lib.model;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtPerceptionModel;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.ConnectedLogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * A cone based perception model
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
@SuppressWarnings("rawtypes")
public class ConnectedPerceptionModel extends AbstractAgtPerceptionModel {

	/**
	 * The maximal distance at which a turtle can perceive.
	 */
	private double distance;
	
	/**
	 * <code>true</code> if the turtle can perceive other turtles.
	 */
	private boolean perceiveTurtles;
	
	/**
	 * <code>true</code> if the turtle can perceive marks.
	 */
	private boolean perceiveMarks;
	
	/**
	 * <code>true</code> if the turtle can perceive pheromones.
	 */
	private boolean perceivePheromones;

	/**
	 * Builds an initialized instance of this perception model
	 * @param level the level of the perception model.
	 * @param distance The maximal distance at which a turtle can perceive.
	 * @param perceiveTurtles <code>true</code> if the turtle can perceive other turtles.
	 * @param perceiveMarks <code>true</code> if the turtle can perceive marks.
	 * @param perceivePheromones <code>true</code> if the turtle can perceive pheromones.
	 * @throws IllegalArgumentException If distance is lower than 0.
	 */
	public ConnectedPerceptionModel(
		LevelIdentifier level,
		double distance,
		boolean perceiveTurtles,
		boolean perceiveMarks,
		boolean perceivePheromones
	) {
		super(LogoSimulationLevelList.LOGO);
		if( distance < 0){
			throw new IllegalArgumentException( "The perception distance of a turtle cannot be negative." );
		} 
		
		this.distance = distance;
		this.perceiveTurtles = perceiveTurtles;
		this.perceiveMarks = perceiveMarks;
		this.perceivePheromones = perceivePheromones;
		
	}
	
	/**
	 * Builds a default initialized instance of this public local state for the LOGO level.
	 */
	public ConnectedPerceptionModel() {
		this(LogoSimulationLevelList.LOGO, 1, true, true, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPerceivedData perceive(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
		ILocalStateOfAgent privateLocalState,
		IPublicDynamicStateMap dynamicStates
	) {
		
		TurtlePLSInLogo localTurtlePLS = (TurtlePLSInLogo) publicLocalStates.get(this.getLevel());
		ConnectedLogoEnvPLS castedEnvState = (ConnectedLogoEnvPLS) dynamicStates.get(this.getLevel()).getPublicLocalStateOfEnvironment();
		
		Collection<LocalPerceivedData<TurtlePLSInLogo>> turtles = new ArrayDeque<>();
		
		Collection<LocalPerceivedData<Mark>> marks = new ArrayDeque<>();
		
		Map<String,Collection<LocalPerceivedData<Double>>> pheromones = new HashMap<>();
	
		for(Point neighbor : castedEnvState.getNeighbors(
			(int) Math.floor(localTurtlePLS.getLocation().getX()),
			(int) Math.floor(localTurtlePLS.getLocation().getY()),
			(int) Math.ceil(this.distance+1))
		) {
			
			if(this.perceiveTurtles) {	
				//Turtle perception
				perceiveTurles(castedEnvState, localTurtlePLS, neighbor, turtles);
			}
			
			if(this.perceiveMarks) {
				//Mark perception 
				perceiveMarks(castedEnvState, localTurtlePLS,neighbor, marks);
			}
			if(this.perceivePheromones) {
				//Pheromone perception 
				perceivePheromones(castedEnvState, localTurtlePLS,neighbor, pheromones);
			}
		}
		return new TurtlePerceivedData(timeLowerBound, timeUpperBound, turtles, marks, pheromones);
	}
	
	private void perceiveTurles(
		LogoEnvPLS envState,
		TurtlePLSInLogo localTurtlePLS,
		Point2D position,
		Collection<LocalPerceivedData<TurtlePLSInLogo>> turtles
	) {
		
		for(TurtlePLSInLogo perceivedTurtle : envState.getTurtlesAt(position.getX(), position.getY())) {	
			double distanceToTurtle = envState.getDistance(
					localTurtlePLS.getLocation(), perceivedTurtle.getLocation()
			);
			if(
				!perceivedTurtle.equals( localTurtlePLS ) &&
				distanceToTurtle <= this.distance
			) {
				double directionToTurtle = envState.getDirection(localTurtlePLS.getLocation(), perceivedTurtle.getLocation());
				turtles.add(
					new TurtlePerceivedData.LocalPerceivedData<TurtlePLSInLogo>(
						perceivedTurtle,
						distanceToTurtle,
						directionToTurtle
					)
				);
			}
		}
	}
	
	private void perceiveMarks(
		LogoEnvPLS envState,
		TurtlePLSInLogo localTurtlePLS,
		Point2D position,
		Collection<LocalPerceivedData<Mark>> marks
	) {
		//Mark perception 
		for(Mark perceivedMark : envState.getMarksAt(position.getX(), position.getY())) {
			double distanceToMark = envState.getDistance(
				localTurtlePLS.getLocation(), perceivedMark.getLocation()
			);
			if(distanceToMark <= this.distance) {
				double directionToMark = envState.getDirection(
					localTurtlePLS.getLocation(),
					perceivedMark.getLocation()
				);
				marks.add(
					new TurtlePerceivedData.LocalPerceivedData<Mark>(
						perceivedMark,
						distanceToMark,
						directionToMark
					)
				);
			}
		}
	}
	private void perceivePheromones(
		LogoEnvPLS envState,
		TurtlePLSInLogo localTurtlePLS,
		Point2D position,
		Map<String,Collection<LocalPerceivedData<Double>>> pheromones
	) {
		double directionToPatch = envState.getDirection(localTurtlePLS.getLocation(), position);
		
		double distanceToPatch = envState.getDistance(
			localTurtlePLS.getLocation(),
			position
		);
		if(distanceToPatch <= this.distance) {
			//Pheromone perception 
			for(Map.Entry<Pheromone, double[][]> pheromoneField : envState.getPheromoneField().entrySet()) {
				if(pheromones.get(pheromoneField.getKey().getIdentifier()) == null) {
					pheromones.put(
						pheromoneField.getKey().getIdentifier(),
						new LinkedHashSet<LocalPerceivedData<Double>>()
					);
				}
				
				pheromones.get(pheromoneField.getKey().getIdentifier()).add(
					new TurtlePerceivedData.LocalPerceivedData<Double>(
						pheromoneField.getValue()[(int) position.getX()][(int) position.getY()],
						distanceToPatch,
						directionToPatch
					)
				);
			}
		}
	}

}
