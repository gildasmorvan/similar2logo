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
package fr.lgi2a.similar2logo.lib.tools.html;

/**
 * The parent interface of all the classes reacting to requests coming from the 
 * HTML web view of Similar2Logo.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IHtmlRequests {
	/**
	 * Asks the requested for a byte array version of the current state of the simulation engine.
	 * @return A byte representation of the state of the engine.
	 */
	byte[] handleSimulationStateRequest( );
	
	/**
	 * Asks the requested for the beginning of a new simulation.
	 */
	void handleNewSimulationRequest( );
	
	/**
	 * Asks the requested for the abortion of the current simulation.
	 */
	void handleSimulationAbortionRequest( );
	
	/**
	 * Asks the requested to pause or resume the current simulation.
	 */
	void handleSimulationPauseRequest( );
	
	/**
	 * Asks the requested to prepare for a shut down of the view.
	 */
	void handleShutDownRequest( );
	
	/**
	 * Asks the requested for the value of a specific simulation parameter.
	 * @param parameter The name of the parameter.
	 * @return The value of the parameter, or an error text if the parameter cannot be found.
	 */
	String getParameter( String parameter );

	/**
	 * Asks the requested to modify the value of a specific simulation parameter.
	 * @param parameter The name of the parameter.
	 * @param value The value of the parameter.
	 */
	void setParameter( String parameter, String value );
}
