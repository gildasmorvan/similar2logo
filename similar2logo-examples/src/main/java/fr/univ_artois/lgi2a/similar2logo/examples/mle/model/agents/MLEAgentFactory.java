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
package fr.univ_artois.lgi2a.similar2logo.examples.mle.model.agents;

import fr.univ_artois.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.generic.IdentityAgtGlobalStateRevisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.mle.model.MLESimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfAgent;
import fr.univ_artois.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel;

/**
 * 
 * The factory creating instances of MLE agents.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class MLEAgentFactory {
    
    /**
     * This constructor is unused since this class only defines static values.
	 * It is declared as protected to prevent the instantiation of this class while 
	 * supporting inheritance.
     */
	protected MLEAgentFactory() {}
     
     /**
 	 * Generates a new MLE agent.
 	 * @param parameters The parameters of the simulation
 	 * @return The newly created instance.
 	 */
 	public static ExtendedAgent generate(MLESimulationParameters parameters){
 		ExtendedAgent turtle = new ExtendedAgent(TurtleAgentCategory.CATEGORY);
 		// Defines the revision model of the global state.
 		turtle.specifyGlobalStateRevisionModel(
 			new IdentityAgtGlobalStateRevisionModel( )
 		);
 		
 		//Defines the behavior of the turtle.
 		turtle.specifyBehaviorForLevel(
 			LogoSimulationLevelList.LOGO, 
 			new ConeBasedPerceptionModel(Math.sqrt(2), 2*Math.PI, false, false, true), 
 			new MLEDecisionModel(parameters)
 		);
 		
 		// Define the initial global state of the turtle.
 		turtle.initializeGlobalState( new EmptyGlobalState( ) );
 		turtle.includeNewLevel(
			LogoSimulationLevelList.LOGO,
			new MLEAgentPLS( 
				turtle, 
				PRNG.randomInt(parameters.gridWidth-1),
				PRNG.randomInt(parameters.gridHeight-1), 
				parameters.initialSpeed,
				0,
				Math.PI/PRNG.randomInt(8)
			),
			new EmptyLocalStateOfAgent(
				LogoSimulationLevelList.LOGO, 
				turtle
			)
		);
 		
 		return turtle;
 	}

}
