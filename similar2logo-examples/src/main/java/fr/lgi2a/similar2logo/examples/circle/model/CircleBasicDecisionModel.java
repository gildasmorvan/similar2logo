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
package fr.lgi2a.similar2logo.examples.circle.model;

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

/**
 * 
 * The decision model of a circle turtle. The turle emits influence to follow the closest
 * perceived turtle on its left and adapt its speed to it.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class CircleBasicDecisionModel extends AbstractAgtDecisionModel {
	
	/**
	 * Builds an instance of this decision model.
	 */
	public CircleBasicDecisionModel() {
		super(LogoSimulationLevelList.LOGO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(
			SimulationTimeStamp timeLowerBound,
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
			double sinDirectionToTarget = 0;
			double cosDirectionToTarget = 0;
			double meanSpeed = 0;
			int nbOfTurtles = 0;
			for(LocalPerceivedData<TurtlePLSInLogo> turtle : castedPerceivedData.getTurtles()) {
				double directionToTurtle = Math.atan2(
					Math.sin(turtle.getDirectionTo()-castedPublicLocalState.getDirection()),
					Math.cos(turtle.getDirectionTo()-castedPublicLocalState.getDirection())
				);
				if(directionToTurtle > 0) {
					sinDirectionToTarget += Math.sin(directionToTurtle);
					cosDirectionToTarget += Math.cos(directionToTurtle);
					meanSpeed+=turtle.getContent().getSpeed();
					nbOfTurtles++;
				}
			}
			if(nbOfTurtles > 0) {
				producedInfluences.add(
					new ChangeDirection(
						timeLowerBound,
						timeUpperBound,
						Math.atan2(sinDirectionToTarget, cosDirectionToTarget),
						castedPublicLocalState
					)
				);
				producedInfluences.add(
					new ChangeSpeed(
						timeLowerBound,
						timeUpperBound,
						meanSpeed/nbOfTurtles - castedPublicLocalState.getSpeed(),
						castedPublicLocalState
					)
				);
			}
		}
	}

}
