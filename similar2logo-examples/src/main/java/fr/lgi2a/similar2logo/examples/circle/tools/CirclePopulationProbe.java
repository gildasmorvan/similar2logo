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
package fr.lgi2a.similar2logo.examples.circle.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.similar2logo.examples.circle.model.TurnLeftCategory;
import fr.lgi2a.similar2logo.examples.circle.model.TurnRightCategory;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * A probe printing information about agent population in a given target.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class CirclePopulationProbe implements IProbe {
	/**
	 * The stream where the data are written.
	 */
	protected PrintStream target;
	
	/**
	 * The last mean orientation of the "turn left" agents.
	 */
	private double lastMeanOrientationTurnLeft = 0;
	
	/**
	 * The last mean orientation of the "turn right" agents.
	 */
	private double lastMeanOrientationTurnRight = 0;
	
	
	/**
	 * Creates an instance of this probe.
	 * 
	 */
	public CirclePopulationProbe(){}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() { 
		
		try {
			File resultDir = new File("results");
			
			if (!resultDir.exists()) {
			    try{
			    	resultDir.mkdir();
			    } 
			    catch(SecurityException e){
			        e.printStackTrace();
			    }        
			}
			this.target = new PrintStream(new FileOutputStream("results/result.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

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
	 * Update the population of agents in x, y and z local fields.
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

		
		double sinMeanOrientationLeft = 0;
		double cosMeanOrientationLeft = 0;
		double sinMeanOrientationRight = 0;
		double cosMeanOrientationRight = 0;
		
		double meanX = 0;
		double meanY = 0;
		
		for( ILocalStateOfAgent agtState : simulationState.getPublicLocalStateOfAgents() ){
			TurtlePLSInLogo castedAgtState = (TurtlePLSInLogo) agtState;
			if(castedAgtState.getCategoryOfAgent().isA(TurnLeftCategory.CATEGORY)) {
			   sinMeanOrientationLeft += Math.sin(castedAgtState.getDirection());
			   cosMeanOrientationLeft += Math.cos(castedAgtState.getDirection());
			} else if(castedAgtState.getCategoryOfAgent().isA(TurnRightCategory.CATEGORY)) {
			   sinMeanOrientationRight += Math.sin(castedAgtState.getDirection());
			   cosMeanOrientationRight += Math.cos(castedAgtState.getDirection());
			} 
			meanX += castedAgtState.getLocation().getX();
			meanY += castedAgtState.getLocation().getY();
		}
		
		double meanOrientationTurnLeft = Math.atan2(sinMeanOrientationLeft, cosMeanOrientationLeft);
		double meanOrientationTurnRight = Math.atan2(sinMeanOrientationRight, cosMeanOrientationRight);
		
		
		this.target.println( 
				timestamp.getIdentifier() + 
				"\t" + meanOrientationTurnLeft  + 
				"\t" + meanOrientationTurnRight +
				"\t" + Math.atan2(
					Math.sin(meanOrientationTurnLeft - this.lastMeanOrientationTurnLeft),
					Math.cos(meanOrientationTurnLeft - this.lastMeanOrientationTurnLeft)
				)  + 
				"\t" + Math.atan2(
					Math.sin(meanOrientationTurnRight - this.lastMeanOrientationTurnRight),
					Math.cos(meanOrientationTurnRight - this.lastMeanOrientationTurnRight)
				) +
				"\t" + (meanX/simulationState.getPublicLocalStateOfAgents().size()) + 
				"\t" + (meanY/simulationState.getPublicLocalStateOfAgents().size())
		);
		
		
		this.lastMeanOrientationTurnLeft = meanOrientationTurnLeft;	
		this.lastMeanOrientationTurnRight = meanOrientationTurnRight;
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
