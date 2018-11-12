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
package fr.univ_artois.lgi2a.similar2logo.examples.simplemultilevel.model.levels;

import java.util.Set;

import fr.univ_artois.lgi2a.similar2logo.examples.simplemultilevel.model.agents.SimpleMultiLevelTurtleFactory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;

/**
 * The reaction model of the simple multi-level simulation
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class SimpleMultiLevelReactionModel extends LogoDefaultReactionModel {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reactToAgentPositionUpdate(
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			Set<ILocalStateOfAgent> agents,
			LogoEnvPLS environment,
			InfluencesMap remainingInfluences
		) {
			long dt = transitoryTimeMax.compareToTimeStamp(transitoryTimeMin);
			//Update turtle locations
			for (ILocalStateOfAgent agentPLS : agents) {
				TurtlePLSInLogo castedTurtlePLS = (TurtlePLSInLogo) agentPLS;
				//Computes speed
				castedTurtlePLS.setSpeed(
					castedTurtlePLS.getSpeed() + castedTurtlePLS.getAcceleration()
				);
				double newX = castedTurtlePLS.getLocation().getX() + castedTurtlePLS.getDX()*dt;
				double newY = castedTurtlePLS.getLocation().getY() + castedTurtlePLS.getDY()*dt;
				double newXT = ( ( newX % environment.getWidth()) + environment.getWidth() ) % environment.getWidth();
				double newYT = ( ( newY % environment.getHeight()) + environment.getHeight() ) % environment.getHeight();

				
				//If the turtle is out of bounds it is added to another level.
				if(
					(!environment.isxAxisTorus() && (
						newX < 0
						|| newX >=  environment.getWidth()
					))
					||(!environment.isyAxisTorus() && (
						newY < 0
						|| newY >=  environment.getHeight()
					))
				) {
					SystemInfluenceAddAgent addInfluence;
					SystemInfluenceRemoveAgent rmInfluence;
					
					if(agentPLS.getLevel().equals(SimpleMultiLevelSimulationLevelList.LOGO)) {
						addInfluence = new SystemInfluenceAddAgent(
							SimpleMultiLevelSimulationLevelList.LOGO2,
							transitoryTimeMax,
							transitoryTimeMax,
							SimpleMultiLevelTurtleFactory.generate(
								castedTurtlePLS,
								SimpleMultiLevelSimulationLevelList.LOGO2,
								newXT,
								newYT
							)
						);
						rmInfluence = new SystemInfluenceRemoveAgent(
							SimpleMultiLevelSimulationLevelList.LOGO,
							transitoryTimeMax,
							transitoryTimeMin, 
							castedTurtlePLS
						);
					} else {
						addInfluence = new SystemInfluenceAddAgent(
							SimpleMultiLevelSimulationLevelList.LOGO,
							transitoryTimeMax,
							transitoryTimeMax,
							SimpleMultiLevelTurtleFactory.generate(
								castedTurtlePLS,
								SimpleMultiLevelSimulationLevelList.LOGO,
								newXT,
								newYT
							)
						);
						rmInfluence = new SystemInfluenceRemoveAgent(
							SimpleMultiLevelSimulationLevelList.LOGO2,
							transitoryTimeMax,
							transitoryTimeMin, 
							castedTurtlePLS
						);
					}

					remainingInfluences.add( addInfluence );
					remainingInfluences.add( rmInfluence );
				} else { 
					//Else the turtle's new location is set.
					//Update turtle patch
					if(
						(int) Math.floor(newXT) != (int) Math.floor(castedTurtlePLS.getLocation().getX()) ||
						(int) Math.floor(newYT) != (int) Math.floor(castedTurtlePLS.getLocation().getY())
					) {
						environment.getTurtlesInPatches()[(int) Math.floor(castedTurtlePLS.getLocation().getX())][(int) Math.floor(castedTurtlePLS.getLocation().getY())].remove(castedTurtlePLS);
						environment.getTurtlesInPatches()[(int) Math.floor(newXT)][(int) Math.floor(newYT)].add(castedTurtlePLS);
					}
					castedTurtlePLS.getLocation().setLocation(
						newXT,
						newYT
					);
				}
			}
	}
}
