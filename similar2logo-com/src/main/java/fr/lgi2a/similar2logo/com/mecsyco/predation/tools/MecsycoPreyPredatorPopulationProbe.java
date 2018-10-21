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
package fr.lgi2a.similar2logo.com.mecsyco.predation.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar2logo.com.mecsyco.predation.model.MecsycoPredationSimulationParameters;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PredatorCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorFactory;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.model.RandomWalk2DDecisionModel;
import fr.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.random.PRNG;

/**
 * This class represents a probe acting as a proxy between the Similar2Logo
 * simulation and the model artifact. 
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class MecsycoPreyPredatorPopulationProbe implements IProbe {

	/**
	 * The "X" population calculated by the Similar2Logo simulation.
	 */
	private double x;
	
	/**
	 * The "Y" variable calculated by the Similar2Logo simulation.
	 */
	private double y;
	
	/**
	 * The variation of the "X" population calculated by a 
	 * {@link fr.lgi2a.similar2logo.com.mecsyco.predation.model.LinearGrowthEquation}
	 */
	private double dX;
	
	/**
	 * The variation of the "X" population calculated by a 
	 * {@link fr.lgi2a.similar2logo.com.mecsyco.predation.model.LinearGrowthEquation}
	 */
	private double dY;
	
	/**
	 * The parameters of the simulation.
	 */
	private MecsycoPredationSimulationParameters parameters;
	
	/**
	 * Creates an instance of this probe.
	 * 
	 */
	public MecsycoPreyPredatorPopulationProbe(MecsycoPredationSimulationParameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
			SimulationTimeStamp initialTimestamp,
			ISimulationEngine simulationEngine
	) {
		this.updatePopulationVariables( initialTimestamp, simulationEngine );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	) {
		this.updatePopulationVariables( timestamp, simulationEngine );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
			SimulationTimeStamp finalTimestamp,
			ISimulationEngine simulationEngine
	) {
		this.updatePopulationVariables( finalTimestamp, simulationEngine );	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(String errorMessage, Throwable cause) {}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(SimulationTimeStamp timestamp, ISimulationEngine simulationEngine) {}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() {}
	
	/**
	 * 
	 * @param timestamp The time stamp when the observation is made.
	 * @param simulationEngine The engine where the simulation is running.
	 */
	private void updatePopulationVariables(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	){
		IPublicLocalDynamicState simulationState = simulationEngine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		
		int nbOfPreys = 0;
		int nbOfPredators = 0;
		
		for( ILocalStateOfAgent agtState : simulationState.getPublicLocalStateOfAgents() ){
			if( agtState.getCategoryOfAgent().isA( PreyCategory.CATEGORY ) ){
				nbOfPreys++;
			} else if( agtState.getCategoryOfAgent().isA( PredatorCategory.CATEGORY ) ){
				nbOfPredators++;
			}
		}	
		this.x = nbOfPreys;
		this.y = nbOfPredators;
		
		updateX(timestamp.getIdentifier(), simulationState, (int) this.dX);
		updateY(timestamp.getIdentifier(), simulationState, (int) this.dY);

	}
	
	/**
	 * @param timestamp The simulation step of the update. 
	 * @param simulationState The state of the simulation.
	 * @param nbOfBirths The number of births.
	 */
	public void updateX(
			long timestamp,
			IPublicLocalDynamicState simulationState,
			int nbOfBirths
	) {
		List<IInfluence> influences = new ArrayList<IInfluence>();
		for(int i = 0; i < nbOfBirths; i++) {
			influences.add(
			   new SystemInfluenceAddAgent(
			   LogoSimulationLevelList.LOGO,
			   new SimulationTimeStamp(timestamp),
			   new SimulationTimeStamp(timestamp+1),
			   PreyPredatorFactory.generate(
			      new ConeBasedPerceptionModel(0, 0, false,false, false),
			      new RandomWalk2DDecisionModel(),
			      PreyCategory.CATEGORY,
			      PRNG.get().randomDouble() * 2 * Math.PI,
			      0,
			      0,
			      PRNG.get().randomDouble() * parameters.gridWidth,
			      PRNG.get().randomDouble() * parameters.gridHeight,
			      parameters.preyInitialEnergy,
			      0
			   )
		 	 )
		   );	
		}
		Set<IInfluence> systemInfluences = simulationState.getSystemInfluencesOfStateDynamics();
		synchronized (systemInfluences) {
			systemInfluences.addAll(influences);
		}
	}
	
	/**
	 * @param timestamp The simulation step of the update. 
	 * @param simulationState The state of the simulation.
	 * @param nbOfBirths The number of births.
	 */
	public void updateY(
			long timestamp,
			IPublicLocalDynamicState simulationState,
			int nbOfBirths
	) {
		List<IInfluence> influences = new ArrayList<IInfluence>();
		for(int i = 0; i < nbOfBirths; i++) {
			influences.add(
			   new SystemInfluenceAddAgent(
			   LogoSimulationLevelList.LOGO,
			   new SimulationTimeStamp(timestamp),
			   new SimulationTimeStamp(timestamp+1),
			   PreyPredatorFactory.generate(
			      new ConeBasedPerceptionModel(0, 0, false,false, false),
			      new RandomWalk2DDecisionModel(),
			      PredatorCategory.CATEGORY,
			      PRNG.get().randomDouble() * 2 * Math.PI,
			      0,
			      0,
			      PRNG.get().randomDouble() * parameters.gridWidth,
			      PRNG.get().randomDouble() * parameters.gridHeight,
			      parameters.predatorInitialEnergy,
			      0
			   )
		 	 )
		   );	
		}
		Set<IInfluence> systemInfluences = simulationState.getSystemInfluencesOfStateDynamics();
		synchronized (systemInfluences) {
			systemInfluences.addAll(influences);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public double getX() {
		synchronized(this){
			return x;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public double getY() {
		synchronized(this){
			return y;
		}
	}
	
	
	/**
	 * @param dX The variation of the "X" population calculated by a 
	 * {@link fr.lgi2a.similar2logo.com.mecsyco.predation.model.LinearGrowthEquation}
	 */
	public void setDX(double dX) {
		synchronized(this){
			this.dX=dX;
		}
	}

	/**
	 * @param dY The variation of the "Y" population calculated by a 
	 * {@link fr.lgi2a.similar2logo.com.mecsyco.predation.model.LinearGrowthEquation}
	 */
	public void setDY(double dY) {
		synchronized(this){
			this.dY=dY;
		}
	}
	

}
