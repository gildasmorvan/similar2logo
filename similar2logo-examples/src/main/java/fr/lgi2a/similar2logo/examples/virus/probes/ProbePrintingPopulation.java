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
package fr.lgi2a.similar2logo.examples.virus.probes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.similar2logo.examples.virus.model.agents.PersonCategory;
import fr.lgi2a.similar2logo.examples.virus.model.agents.PersonPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * A probe printing information about agent population in a given target.
 * 
 * @author <a href="mailto:julienjnthn@gmail.com" target="_blank">Jonathan Julien</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ProbePrintingPopulation implements IProbe {
	/**
	 * The stream where the data are written.
	 */
	protected PrintStream target;
	
	/**
	 * Creates an instance of this probe writing in a specific print stream.
	 * @param target The stream where the data are written.
	 * @throws FileNotFoundException 
	 * @throws IllegalArgumentException If the <code>target</code> is <code>null</code>.
	 */
	public ProbePrintingPopulation() throws FileNotFoundException{
		
		File resultDir = new File("results");

		// if the directory does not exist, create it
		if (!resultDir.exists()) {
		    try{
		    	resultDir.mkdir();
		    } 
		    catch(SecurityException e){
		        e.printStackTrace();
		    }        
		}
		
		this.target = new PrintStream(new FileOutputStream("results/result.txt", false));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
			SimulationTimeStamp initialTimestamp,
			ISimulationEngine simulationEngine
	) {
		this.displayPopulation( initialTimestamp, simulationEngine );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	) {
		this.displayPopulation( timestamp, simulationEngine );
	}
	
	/**
	 * Displays the location of the particles on the print stream.
	 * @param timestamp The time stamp when the observation is made.
	 * @param simulationEngine The engine where the simulation is running.
	 */
	private void displayPopulation(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	){
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get( 
				LogoSimulationLevelList.LOGO
		);
		int nbOfAgents = simulationEngine.getAgents(LogoSimulationLevelList.LOGO).size();
		int nbOfInfectedAgents = 0;
		int nbOfImmuneAgents = 0;
		int nbOfNeverInfectedAgents = 0;
		
		for( ILocalStateOfAgent agtState : simulationState.getPublicLocalStateOfAgents() ){
			if( agtState.getCategoryOfAgent().isA( PersonCategory.CATEGORY ) ){
				PersonPLS castedAgtState = (PersonPLS) agtState;
				if(castedAgtState.isInfected()) {
					nbOfInfectedAgents++;
				} else if (castedAgtState.getTimeInfected() == -1){
					nbOfNeverInfectedAgents++;
				} else {
					nbOfImmuneAgents++;
				}
				
			}
		}
		this.target.println( 
				timestamp.getIdentifier() + 
				"\t" + nbOfAgents + 
				"\t" + nbOfInfectedAgents  + 
				"\t" + nbOfImmuneAgents +
				"\t" + nbOfNeverInfectedAgents
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
			SimulationTimeStamp finalTimestamp,
			ISimulationEngine simulationEngine
	) {	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() {
		this.target.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(
			String errorMessage, 
			Throwable cause
	) { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	) { }
}