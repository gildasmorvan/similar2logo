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
package fr.lgi2a.similar2logo.lib.agents.perception;

import java.awt.geom.Point2D;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtPerceptionModel;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import fr.lgi2a.similar2logo.kernel.model.environment.Position;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class TurtlePerceptionModel extends AbstractAgtPerceptionModel {

	/**
	 * The maximal distance at which a turtle can perceive.
	 */
	private double distance;
	
	/**
	 * The perception angle of the turtle (in rad).
	 */
	private double angle;

	/**
	 * Builds an initialized instance of this public local state.
	 * @param distance The maximal distance at which a turtle can perceive.
	 * @param angle The perception angle of the turtle (in rad).
	 * @throws IllegalArgumentException If distance is lower than 0.
	 */
	public TurtlePerceptionModel(double distance, double angle) {
		super(LogoSimulationLevelList.LOGO);
		if( distance < 0){
			throw new IllegalArgumentException( "The perception distance of a turtle cannot be negative." );
		} else {
			this.distance = distance;
			double pi2 = 2*Math.PI;
			this.angle =  ( ( angle % pi2 ) + pi2 ) % pi2;
			if(this.angle == 0) {
				this.angle = pi2;
			}
		}
		
	}
	
	/**
	 * Builds an initialized instance of this public local state.
	 */
	public TurtlePerceptionModel() {
		super(LogoSimulationLevelList.LOGO);
		this.distance = 1;
		this.angle = 2*Math.PI;	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPerceivedData perceive(SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
			ILocalStateOfAgent privateLocalState,
			IPublicDynamicStateMap dynamicStates) {
		
		TurtlePLSInLogo localTurtlePLS = (TurtlePLSInLogo) publicLocalStates.get(LogoSimulationLevelList.LOGO);
		LogoEnvPLS castedEnvState = (LogoEnvPLS) dynamicStates.get(LogoSimulationLevelList.LOGO).getPublicLocalStateOfEnvironment();
		
		Set<LocalPerceivedData<TurtlePLSInLogo>> turtles = new LinkedHashSet<LocalPerceivedData<TurtlePLSInLogo>>();
		
		Set<LocalPerceivedData<Mark>> marks = new LinkedHashSet<LocalPerceivedData<Mark>>();
		
		Map<String,Set<LocalPerceivedData<Double>>> pheromones = new LinkedHashMap<String,Set<LocalPerceivedData<Double>>>();
	
		for(Position neighbor : castedEnvState.getNeighbors(
				(int) Math.floor(localTurtlePLS.getLocation().getX()),
				(int) Math.floor(localTurtlePLS.getLocation().getY()),
				(int) Math.ceil(this.distance))
		) {

			//Turtle perception
			for(TurtlePLSInLogo perceivedTurtle : castedEnvState.getTurtlesAt(neighbor.x, neighbor.y)) {
				double distanceToTurtle = castedEnvState.getDistance(
					localTurtlePLS.getLocation(), perceivedTurtle.getLocation()
				);
				if(
					!perceivedTurtle.equals( localTurtlePLS ) &&
					distanceToTurtle <= this.distance &&
					Math.abs(
						localTurtlePLS.getDirection() - castedEnvState.getDirection(
							localTurtlePLS.getLocation(), perceivedTurtle.getLocation()
						) 
					) <= this.angle
				) {
					turtles.add(
						new TurtlePerceivedData.LocalPerceivedData<TurtlePLSInLogo>(
							perceivedTurtle,
							distanceToTurtle,
							castedEnvState.getDirection(
								localTurtlePLS.getLocation(),
								perceivedTurtle.getLocation()
							)
						)
					);
				}
			}
			
			//Mark perception 
			for(Mark perceivedMark : castedEnvState.getMarksAt(neighbor.x, neighbor.y)) {
				double distanceToMark = castedEnvState.getDistance(
						localTurtlePLS.getLocation(), perceivedMark.getLocation()
				);
				if( 
					distanceToMark <= this.distance &&
					Math.abs(
						localTurtlePLS.getDirection() - castedEnvState.getDirection(
							localTurtlePLS.getLocation(), perceivedMark.getLocation()
						) 
					) <= this.angle
				) {
					marks.add(
						new TurtlePerceivedData.LocalPerceivedData<Mark>(
							perceivedMark,
							distanceToMark,
							castedEnvState.getDirection(
								localTurtlePLS.getLocation(),
								perceivedMark.getLocation()
							)
						)
					);
				}
			}
			
			//Pheromone perception 
			Point2D patch = new Point2D.Double(neighbor.x,neighbor.y);
			for(Map.Entry<Pheromone, double[][]> pheromoneField : castedEnvState.getPheromoneField().entrySet()) {
				double distanceToPatch = castedEnvState.getDistance(
						localTurtlePLS.getLocation(), patch
				);
				if( 
					Math.abs(
						localTurtlePLS.getDirection() - castedEnvState.getDirection(
							localTurtlePLS.getLocation(), patch
						) 
					) <= this.angle
				) {
					if(pheromones.get(pheromoneField.getKey().getIdentifier()) == null) {
						pheromones.put(
							pheromoneField.getKey().getIdentifier(),
							new LinkedHashSet<LocalPerceivedData<Double>>()
						);
					}
					pheromones.get(pheromoneField.getKey().getIdentifier()).add(
						new TurtlePerceivedData.LocalPerceivedData<Double>(
							pheromoneField.getValue()[neighbor.x][neighbor.y],
							distanceToPatch,
							castedEnvState.getDirection(
								localTurtlePLS.getLocation(),
								patch
							)
						)
					);
				}
			}
		}
		return new TurtlePerceivedData(
			timeLowerBound,
			timeUpperBound,
			turtles,
			marks,
			pheromones
		);
	}

}
