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
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
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
		}
		else {
			this.distance = distance;
			this.angle = angle%2*Math.PI;
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
		
		Set<TurtlePLSInLogo> turtles = new LinkedHashSet<TurtlePLSInLogo>();
		
		//TODO manage perception
		/*for (ILocalStateOfAgent perceivedAgentPLS : dynamicStates.get(LogoSimulationLevelList.LOGO).getPublicLocalStateOfAgents()) {
			TurtlePLSInLogo castedPerceivedTurtlePLS = (TurtlePLSInLogo) perceivedAgentPLS;
		}*/
		Set<Mark> marks = new LinkedHashSet<Mark>();
		
		Set<Position> patches = new LinkedHashSet<Position>();
	
		for(Position neighbor : castedEnvState.getNeighbors(
				(int) Math.floor(localTurtlePLS.getLocation().getX()),
				(int) Math.floor(localTurtlePLS.getLocation().getY()),
				(int) Math.ceil(this.distance))
		) {
			if(
				Math.abs(localTurtlePLS.getDirection() - castedEnvState.getDirection(
					localTurtlePLS.getLocation(), new Point2D.Double(neighbor.x, neighbor.y)
				) ) <= this.angle
			) {
				for(TurtlePLSInLogo perceivedTurtle : castedEnvState.getTurtlesAt(neighbor.x, neighbor.y)) {
					if(
						castedEnvState.getDistance(
							localTurtlePLS.getLocation(), perceivedTurtle.getLocation()
						) < this.distance
					) {
						turtles.add(perceivedTurtle);
					}
				}
			}
		}
		
		return null;
	}

}
