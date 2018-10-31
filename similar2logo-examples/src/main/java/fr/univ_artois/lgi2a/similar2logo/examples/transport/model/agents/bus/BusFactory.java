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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.bus;

import fr.univ_artois.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtPerceptionModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.generic.IdentityAgtGlobalStateRevisionModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfAgent;

/**
 * The class for making the buses in the transport simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class BusFactory {
	
	/**
     * This constructor is unused since this class only defines static values.
	 * It is declared as protected to prevent the instantiation of this class while 
	 * supporting inheritance.
     */
	protected BusFactory () {
	}
	
	/**
	 * Generates a new bus turtle.
	 * @param turtlePerceptionModel the perception model of the turtle
	 * @param turtleDecisionModel the decision model of the turtle
	 * @param category the category of the turtle
	 * @param initialDirection the initial direction of the turtle
	 * @param initialSpeed the initial speed of the turtle
	 * @param initialAcceleration the initial acceleration of the turtle
	 * @param initialX the initial x coordinate of the turtle
	 * @param initialY the initial y coordinate of the turtle
	 * @param maxCapacity the maximum capacity of the turtle
	 * @param speedFrequencyBus the number of move that the turtle can do by turn
	 * @param size the size of the transport
	 * @return the newly created instance
	 */
	public static ExtendedAgent generate (
 			AbstractAgtPerceptionModel turtlePerceptionModel,
 			AbstractAgtDecisionModel turtleDecisionModel,
 			AgentCategory category,
 			double initialDirection,
 			double initialSpeed,
 			double initialAcceleration,
 			double initialX,
 			double initialY,
 			double speedFrequencyBus,
 			int maxCapacity,
 			int size) {
		if( ! category.isA(TurtleAgentCategory.CATEGORY) ) {
 			throw new IllegalArgumentException( "Only turtle agents are accepted." );
 		}
 		ExtendedAgent turtle = new ExtendedAgent( category );
 		// Defines the revision model of the global state.
 		turtle.specifyGlobalStateRevisionModel(
 			new IdentityAgtGlobalStateRevisionModel( )
 		);
 		
 		//Defines the behavior of the turtle.
 		turtle.specifyBehaviorForLevel(
 				LogoSimulationLevelList.LOGO, 
 				turtlePerceptionModel, 
 			turtleDecisionModel
 			);
 		
 		// Define the initial global state of the turtle.
 		turtle.initializeGlobalState( new EmptyGlobalState( ) );
 		turtle.includeNewLevel(
 				LogoSimulationLevelList.LOGO,
 				new BusPLS(
 						turtle,
 						initialX,
 						initialY, 
 						initialSpeed,
 						initialAcceleration,
 						initialDirection,
 						speedFrequencyBus,
 						maxCapacity,
 						size
 					),
 				new EmptyLocalStateOfAgent(
 						LogoSimulationLevelList.LOGO,
 						turtle
 						
 				)
 				
				
		);
 		
 		return turtle;
	}

}
