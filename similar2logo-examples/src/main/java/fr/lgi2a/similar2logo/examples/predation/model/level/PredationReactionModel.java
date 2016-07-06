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
package fr.lgi2a.similar2logo.examples.predation.model.level;

import java.util.HashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;

/**
 * The reaction model of the predation simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class PredationReactionModel extends LogoDefaultReactionModel {

	/**
	 * The parameters of the simulation.
	 */
	private PredationSimulationParameters parameters;

	/**
	 * Creates a new instance of the SegregationReactionModel class.
	 * 
	 * @param parameters
	 */
	public PredationReactionModel(PredationSimulationParameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void makeRegularReaction(
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences
	) {
		
		LogoEnvPLS environment = (LogoEnvPLS) consistentState.getPublicLocalStateOfEnvironment();
		Set<TurtlePLSInLogo> dyingPreys = new HashSet<TurtlePLSInLogo>();
		Set<TurtlePLSInLogo> dyingPredators = new HashSet<TurtlePLSInLogo>();

		//Local predation and grass growth interactions
		for (int x = 0; x < environment.getWidth(); x++) {
			for (int y = 0; y < environment.getHeight(); y++) {
				
				Set<TurtlePLSInLogo> agents = environment.getTurtlesAt(x, y);
				Mark<Double> grass = environment.getMarksAt(x,y).iterator().next();
				
				//Initializes local interactions
				PredationInteraction predationInteraction = new PredationInteraction(agents,grass);
				GrassGrowthInteraction grassGrowthInteraction = new GrassGrowthInteraction(grass);
				
				//Preys eat grass
				predationInteraction.PreysEatGrass(parameters);
				
				//Predators eat preys
				predationInteraction.PredatorsEatPreys(
				   parameters,
				   remainingInfluences,
				   transitoryTimeMin,
				   transitoryTimeMax,
				   dyingPreys
				);
				
				//Grass growth
				grassGrowthInteraction.grow(parameters);
			}
		}
		
		//Initializes global interaction
		AgingAndReproductionInteraction arInteraction = new AgingAndReproductionInteraction(
		   consistentState.getPublicLocalStateOfAgents()
		);
		
		//Aging
		arInteraction.preyAging(
	       parameters,
	       remainingInfluences,
	       transitoryTimeMin,
	       transitoryTimeMax,
	       dyingPreys
	    );
		
		arInteraction.predatorAging(
	       parameters,
	       remainingInfluences,
	       transitoryTimeMin,
	       transitoryTimeMax,
	       dyingPredators
	    );
		
		//Reproduction
		arInteraction.preyReproduction(
	       parameters,
	       environment,
	       remainingInfluences,
	       transitoryTimeMin,
	       transitoryTimeMax,
	       dyingPreys.size()
	    );
		
		arInteraction.predatorReproduction(
	       parameters,
	       environment,
	       remainingInfluences,
	       transitoryTimeMin,
	       transitoryTimeMax,
	       dyingPredators.size()
	    );


		super.makeRegularReaction(
	       transitoryTimeMin,
	       transitoryTimeMax,
	       consistentState,
	       regularInfluencesOftransitoryStateDynamics,
	       remainingInfluences
	    );

	}

}
