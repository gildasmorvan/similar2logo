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


import static net.jafama.FastMath.*;

import java.awt.geom.Point2D;
import java.util.LinkedHashSet;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeDirection;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeSpeed;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.DropMark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.RemoveMark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import groovy.ui.SystemOutputInterceptor;

/**
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class FireWorkReactionModel extends LogoDefaultReactionModel {

	private static final double EPSILON = 1;
	/**
	 * The parameters of the simulation.
	 */
	private FireWorkParameters parameters;

	/**
	 * Creates a new instance of the MultiTurmiteReactionModel class.
	 * 
	 * @param parameters
	 *            The parameter class of the multiturmite model
	 */
	public FireWorkReactionModel(FireWorkParameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {

		Set<IInfluence> influences = new LinkedHashSet<>();
		influences.addAll(regularInfluencesOftransitoryStateDynamics);
		LogoEnvPLS castedEnvironment = (LogoEnvPLS) consistentState.getPublicLocalStateOfEnvironment();
		
		for(Mark mark: castedEnvironment.getMarksAsSet()) {
			Mark<Long> castedMark = (Mark<Long>) mark;
			
			if((castedMark.getContent()+this.parameters.trailLifeTime) <= transitoryTimeMin.getIdentifier()){
				//System.out.println("time removed: " + String.valueOf(castedMark.getContent()+this.parameters.trailLifeTime)+", time: " + transitoryTimeMin.getIdentifier());
				influences.add(
						new RemoveMark(
							transitoryTimeMin,
							transitoryTimeMax,
							castedMark
						)
					);
			}
		}
		
		for (ILocalStateOfAgent agent : consistentState
				.getPublicLocalStateOfAgents()) {
			TurtlePLSInLogo castedAgent = (TurtlePLSInLogo) agent;
			
			if (castedAgent.getSpeed() < EPSILON) {
				remainingInfluences.add(new SystemInfluenceRemoveAgent(
						LogoSimulationLevelList.LOGO, transitoryTimeMin,
						transitoryTimeMax, castedAgent));

			} else {
				String type="rocket";
				if (agent.getCategoryOfAgent().isA(
						FireWorkParameters.rocketCategory)) {
	
					influences.add(new ChangeSpeed(transitoryTimeMin,
							transitoryTimeMax, -castedAgent.getSpeed()
							/ parameters.gravity,castedAgent));
					
				} else if (agent.getCategoryOfAgent().isA(
						FireWorkParameters.fragmentCategory)) {
					
					influences.add(new ChangeSpeed(transitoryTimeMin,
							transitoryTimeMax, -castedAgent.getSpeed()
							/ parameters.gravity,castedAgent));
					
					type="frag";	
				
				}
				if(this.parameters.initialTrails && PRNG.randomDouble() < this.parameters.trailProbablity) {
					influences.add(
						new DropMark(
							transitoryTimeMin,
							transitoryTimeMax,
							new Mark<Long>((Point2D.Double) castedAgent.getLocation().clone(), transitoryTimeMin.getIdentifier(), type)
						)
					);
				}
			}
		}
		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax,
				consistentState, influences, remainingInfluences);
	}

}
