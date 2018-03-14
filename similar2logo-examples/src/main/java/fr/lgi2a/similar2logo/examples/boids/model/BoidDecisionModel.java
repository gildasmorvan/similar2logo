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
package fr.lgi2a.similar2logo.examples.boids.model;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.kernel.tools.MathUtil;
import fr.lgi2a.similar2logo.lib.tools.math.MeanAngle;

/**
 * 
 * The decision model of a boid.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class BoidDecisionModel extends AbstractAgtDecisionModel {

	/**
	 * simulation parameters.
	 */
	private BoidsSimulationParameters parameters;

	/**
	 * Builds an instance of this decision model.
	 */
	public BoidDecisionModel(BoidsSimulationParameters parameters) {
		super(LogoSimulationLevelList.LOGO);
		this.parameters=parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		IGlobalState globalState,
		ILocalStateOfAgent publicLocalState,
		ILocalStateOfAgent privateLocalState,
		IPerceivedData perceivedData,
		InfluencesMap producedInfluences
	) {
		
		TurtlePLSInLogo castedPublicLocalState = (TurtlePLSInLogo) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		
		if(!castedPerceivedData.getTurtles().isEmpty()) {
			double orientationSpeed = 0;
			int nbOfTurtlesInOrientationArea = 0;
			MeanAngle meanAngle = new MeanAngle();
			for (LocalPerceivedData<TurtlePLSInLogo> perceivedTurtle : castedPerceivedData.getTurtles()) {
				if (perceivedTurtle.getDistanceTo() <= this.parameters.repulsionDistance) {
					meanAngle.add(
						castedPublicLocalState.getDirection()- perceivedTurtle.getDirectionTo(),
						parameters.repulsionWeight
					);
				} else if (perceivedTurtle.getDistanceTo() <= this.parameters.orientationDistance) {
					meanAngle.add(
						perceivedTurtle.getContent().getDirection() - castedPublicLocalState.getDirection(),
						parameters.orientationWeight
					);
					orientationSpeed+=perceivedTurtle.getContent().getSpeed() - castedPublicLocalState.getSpeed();
					nbOfTurtlesInOrientationArea++;
				} else if (perceivedTurtle.getDistanceTo() <= this.parameters.attractionDistance){
					meanAngle.add(
						perceivedTurtle.getDirectionTo()- castedPublicLocalState.getDirection(),
						parameters.attractionWeight
					);
				}
			}
			double dd = meanAngle.value();
			if (!MathUtil.areEqual(dd, 0)) {
				if(dd > parameters.maxAngle) {
					dd = parameters.maxAngle;
				}else if(dd<-parameters.maxAngle) {
					dd = -parameters.maxAngle;
				}
				producedInfluences.add(
					new ChangeDirection(
						timeLowerBound,
						timeUpperBound,
						dd,
						castedPublicLocalState
					)
				);
			}
			if (nbOfTurtlesInOrientationArea > 0) {
				orientationSpeed /= nbOfTurtlesInOrientationArea;
				producedInfluences.add(
					new ChangeSpeed(
						timeLowerBound,			
						timeUpperBound,
						orientationSpeed,
						castedPublicLocalState
					)
				);
			}
		}
	}	

}