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
package fr.lgi2a.similar2logo.lib.tools.mecsyco;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.probes.StepSimulationProbe;
import fr.lgi2a.similar2logo.lib.tools.SimulationExecutionThread;
import mecsyco.core.model.ModelArtifact;

/**
 * This class represents a model-artifact for managing a Similar2Logo model 
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *         
 * @param <T> The type of probe that serves as a proxy between this artifact and the Similar2Logo simulation.
 *
 */
public abstract class AbstractSimilar2LogoModelArtifact<T extends IProbe> extends ModelArtifact {

	
	/**
	 * The simulation model.
	 */
	protected LogoSimulationModel simulationModel;
	
	/**
	 * The engine of the simulation.
	 */
	protected ISimulationEngine engine;
	
	/**
	 * The probe that step the simulation.
	 */
	protected StepSimulationProbe stepStimulation;
	
	/**
	 * The probe that communicates with Mecsyco.
	 */
	protected T mecsycoProbe;
	
	/**
	 * The thread in which the Similar2Logo is executed.
	 */
	protected Thread t;
	
	
	/**
	 * Builds a new instance of this model artifact.
	 * 
	 * @param simulationModel The simulation model.
	 * @param mecsycoProbe The probe that communicates with Mecsyco.
	 */
	public AbstractSimilar2LogoModelArtifact(
		LogoSimulationModel simulationModel,
		T mecsycoProbe
	) {
		super("ode");
		this.simulationModel = simulationModel;
		this.stepStimulation = new StepSimulationProbe();
		this.mecsycoProbe = mecsycoProbe;
		
		TurtleFactory.setParameters((LogoSimulationParameters) simulationModel.getSimulationParameters());
		
		engine = new EngineMonothreadedDefaultdisambiguation();
		
		engine.addProbe( 
			"Error printer", 
			new ProbeExceptionPrinter()
		);
		engine.addProbe(
			"Trace printer", 
			new ProbeExecutionTracker( System.err, false )
		);
		
		engine.addProbe("Mecsyco", mecsycoProbe);
		
		engine.addProbe(
			"Step simulation",
			stepStimulation
		);

		this.t = new Thread(
		   new SimulationExecutionThread(
		      engine,
		      simulationModel
		   )
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processInternalEvent(double time) {
		stepStimulation.step();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getNextInternalEventTime() {
		if(!engine.getSimulationDynamicStates().keySet().contains(LogoSimulationLevelList.LOGO)) {
			return 0;
		}
		ConsistentPublicLocalDynamicState dynamicState = (ConsistentPublicLocalDynamicState) engine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);

		return (double) (dynamicState.getTime().getIdentifier()+1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getLastEventTime() {
		if(!engine.getSimulationDynamicStates().keySet().contains(LogoSimulationLevelList.LOGO)) {
			return 0;
		}
		ConsistentPublicLocalDynamicState dynamicState = (ConsistentPublicLocalDynamicState) engine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO);
		return (double) dynamicState.getTime().getIdentifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		t.start();
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
