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
package fr.univ_artois.lgi2a.similar2logo.examples.firework;

import static net.jafama.FastMath.atan2;
import static net.jafama.FastMath.hypot;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.generic.EmptyAgtDecisionModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.lib.model.EmptyPerceptionModel;

public class RocketDecisionModel extends AbstractAgtDecisionModel {

	private static final double EPSILON = 1;
	private FireWorkParameters parameters;

	public RocketDecisionModel(FireWorkParameters parameters) {
		super(LogoSimulationLevelList.LOGO);
		this.parameters = parameters;
	}

	@Override
	public void decide(SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		TurtlePLSInLogo castedPublicLocalState = (TurtlePLSInLogo) publicLocalState;
		
		if (castedPublicLocalState.getSpeed() <= EPSILON) {

		
			producedInfluences.add(new SystemInfluenceRemoveAgent(
					LogoSimulationLevelList.LOGO, timeLowerBound,
					timeUpperBound, castedPublicLocalState));

			for (int i = 0; i < this.parameters.initialNumberFragmentations; i++) {

				producedInfluences.add(
					new SystemInfluenceAddAgent(
						LogoSimulationLevelList.LOGO,
						timeLowerBound,
						timeUpperBound,
						 generateFragment(parameters, castedPublicLocalState.getLocation().x, castedPublicLocalState.getLocation().y)
					)
				);
							
				
			}
		} else if(castedPublicLocalState.getCategoryOfAgent().isA(FireWorkParameters.fragmentCategory)) {
			double Fragx=castedPublicLocalState.getDX()/parameters.gravity;
			double Fragy=castedPublicLocalState.getDY()/parameters.gravity;
			double angleFrag=-atan2(Fragx,Fragy);
			double speedFrag=hypot(Fragx,Fragy);	
			castedPublicLocalState.setSpeed(speedFrag);
			castedPublicLocalState.setDirection(angleFrag);
		}
	}
	
	/**
	 * @param p The parameters of the simulation model.
	 * @return a new boid located at the center of the grid.
	 */
	private static IAgent4Engine generateFragment( FireWorkParameters parameters, double x, double y) {
		return TurtleFactory.generate(
			new EmptyPerceptionModel(),
			new EmptyAgtDecisionModel(LogoSimulationLevelList.LOGO),
			FireWorkParameters.fragmentCategory,
			PRNG.randomAngle(),
			PRNG.randomDouble(5, 10),
			0,
			x,
			y
		);
	}

}
