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
package fr.lgi2a.similar2logo.lib.mecsyco;

import fr.lgi2a.similar.microkernel.dynamicstate.TransitoryPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.probes.StepSimulationProbe;
import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple2;
import mecsyco.core.type.Tuple3;

/**
 * This class represents a model-artifact for managing a Similar2Logo model 
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public abstract class AbstractSimilar2LogoModelArtifact extends ModelArtifact {

	
	/**
	 * The simulation model.
	 */
	private LogoSimulationModel simulationModel;
	
	/**
	 * The engine of the simulation.
	 */
	private EngineMonothreadedDefaultdisambiguation engine;
	
	/**
	 * The probe that step the simulation.
	 */
	private StepSimulationProbe stepStimulation;
	
	private IMecsycoProbe mecsycoProbe;
	
	
	
	/**
	 * Builds a new instance of this model artifact.
	 * 
	 * @param simulationModel The simulation model.
	 * @param parameters The parameters of the model.
	 * @param mecsycoProbe The probe that returns "X", "Y" and "Z" variables.
	 */
	public AbstractSimilar2LogoModelArtifact(
		LogoSimulationModel simulationModel,
		LogoSimulationParameters parameters,
		IMecsycoProbe mecsycoProbe
	) {
		super("ode");
		this.simulationModel = simulationModel;
		this.stepStimulation = new StepSimulationProbe();
		this.mecsycoProbe = mecsycoProbe;
		
		TurtleFactory.setParameters( parameters );
		
		engine = new EngineMonothreadedDefaultdisambiguation( );
		
		engine.addProbe( 
			"Error printer", 
			new ProbeExceptionPrinter( )
		);
		engine.addProbe(
			"Trace printer", 
			new ProbeExecutionTracker( System.err, false )
		);
		engine.addProbe(
			"Step simulation",
			stepStimulation
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processInternalEvent(double time) {
		stepStimulation.setOneStep(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getNextInternalEventTime() {
		TransitoryPublicLocalDynamicState dynamicState = (TransitoryPublicLocalDynamicState) engine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		return (double) dynamicState.getTransitoryPeriodMax().getIdentifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getLastEventTime() {
		TransitoryPublicLocalDynamicState dynamicState = (TransitoryPublicLocalDynamicState) engine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		return (double) dynamicState.getTransitoryPeriodMin().getIdentifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulEvent getExternalOutputEvent(String port) {
    	if(port.equalsIgnoreCase("obs")){
    		return new SimulEvent(
    			new Tuple2<>(this.mecsycoProbe.getX(), this.mecsycoProbe.getY()),
    			this.getLastEventTime()
    		);
    	} else if(port.equalsIgnoreCase("obs3d")){
    		return new SimulEvent(
        		new Tuple3<>(this.mecsycoProbe.getX(), this.mecsycoProbe.getY(), this.mecsycoProbe.getZ()),
        		this.getLastEventTime()
        		);
    	}else{
			return new SimulEvent(
				new Tuple2<>(this.mecsycoProbe.getX(), port),
				this.getLastEventTime()
			);
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		engine.runNewSimulation(simulationModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finishSimulation() {}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInitialParameters(String[] args) {}

}
