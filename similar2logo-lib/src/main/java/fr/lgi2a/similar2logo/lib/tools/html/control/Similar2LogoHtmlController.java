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
package fr.lgi2a.similar2logo.lib.tools.html.control;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.lib.tools.html.IHtmlControls;
import fr.lgi2a.similar2logo.lib.tools.html.IHtmlRequests;

/**
 * The controller managing the synchronization between the simulation engine and the HTML 
 * view and control buttons of the simulation.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com>Antoine Lecoutre</a>
 */
public class Similar2LogoHtmlController implements IProbe, IHtmlRequests {	
	/**
	 * The engine of the simulation 
	 */
	private ISimulationEngine engine;
	/**
	 * The object forwarding update requests of the view.
	 */
	private IHtmlControls viewControls;
	/**
	 * <code>true</code> if the controller can listen and react to view requests.
	 * <code>false</code> else.
	 */
	private boolean listenToViewRequests;
	
	/**
	 * Creates an instance of the controller for the provided engine and view.
	 * The initialization has to be completed by a separate call to the <code>setViewControls</code> method.
	 * @param engine The simulation engine used to perform simulations.
	 * @param viewControls The object forwarding update requests of the view.
	 */
	public Similar2LogoHtmlController(
		ISimulationEngine engine
	){
		this.listenToViewRequests = false;
		this.engine = engine;
	}
	
	/**
	 * Sets the object forwarding update requests of the view.
	 * @param viewControls The object forwarding update requests of the view.
	 */
	public void setViewControls( IHtmlControls viewControls ){
		this.viewControls = viewControls;
	}
	
	/**
	 * Tells the controller that it can start listening to and reacting to
	 * the requests sent by the view.
	 */
	public void listenToViewRequests() {
		this.listenToViewRequests = true;
	}

	@Override
	public String handleSimulationStateRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleNewSimulationRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleSimulationAbortionRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleSimulationPauseRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleShutDownRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getParameter(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameter(String parameter, String value) {
		// TODO Auto-generated method stub
	}

	
	
	@Override
	public void prepareObservation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void observeAtInitialTimes(SimulationTimeStamp initialTimestamp, ISimulationEngine simulationEngine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void observeAtPartialConsistentTime(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void observeAtFinalTime(SimulationTimeStamp finalTimestamp, ISimulationEngine simulationEngine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToError(String errorMessage, Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToAbortion(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endObservation() {
		// TODO Auto-generated method stub
		
	}
}