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
package fr.lgi2a.similar2logo.lib.exploration;

import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.extendedkernel.simulationmodel.AbstractExtendedSimulationModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.initializations.TimeBasedEndCriterion;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.lib.exploration.tools.CloneSimulation;
import fr.lgi2a.similar2logo.lib.exploration.tools.SimulationData;
import fr.lgi2a.similar2logo.lib.probes.ExplorationProbe;

/**
 * The abstract class for the exploration simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public abstract class AbstractExplorationSimulationModel extends AbstractExtendedSimulationModel{
	
	/**
	 * Simulation to run.
	 */
	protected AbstractLogoSimulationModel simulationModel;

	/**
	 * Engine for executing the simulation (and with the probe for getting the data).
	 */
	protected EngineMonothreadedDefaultdisambiguation engine;

	/**
	 * Current time of the simulation.
	 */
	protected SimulationTimeStamp currentTime;
	
	/**
	 * The data of the simulation
	 */
	protected SimulationData data;

	/**
	 * Constructor of the exploration simulation
	 * @param parameters Parameters of the simulation
	 * @param initTime Time of the beginning of the simulation
	 * @param simulationModel Simulation of base
	 * @param simulationData The type of simulation data
	 */
	public AbstractExplorationSimulationModel(
		LogoSimulationParameters parameters,
		SimulationTimeStamp initTime, 
		AbstractLogoSimulationModel simulationModel,
		SimulationData simulationData
	) {
		super(parameters, new TimeBasedEndCriterion(parameters));
		this.currentTime = initTime;
		this.simulationModel = simulationModel;
		this.engine = new EngineMonothreadedDefaultdisambiguation();
		this.engine.addProbe("Exploration probe", new ExplorationProbe(simulationData));
		this.engine.addProbe("Error printer", new ProbeExceptionPrinter( ));
		this.data = simulationData;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(ISimulationParameters simulationParameters) {
		return this.simulationModel.generateLevels(currentTime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EnvironmentInitializationData generateEnvironment(ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		if (currentTime.getIdentifier() == 0) {
			return simulationModel.generateEnvironment(currentTime, levels);
		} else {
			ExplorationProbe ep = (ExplorationProbe) this.engine.getProbe("Exploration probe");
			EnvironmentInitializationData eid = simulationModel.generateEnvironment(currentTime, levels);
			CloneSimulation.cloneEnvironment(eid, ep.getData());
			return eid;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		if (currentTime.getIdentifier() == 0) {
			return simulationModel.generateAgents(currentTime, levels);
		} else {
			AgentInitializationData agents = new AgentInitializationData();
			ExplorationProbe ep = (ExplorationProbe) this.engine.getProbe("Exploration probe");
			CloneSimulation.cloneAgents(agents, ep.getData());
			return agents;
		}
	}
	
	/**
	 * Gives the engine uses by the exploration
	 * @return the engine of the exploration
	 */
	public EngineMonothreadedDefaultdisambiguation getEngine() {
		return engine;
	}
	
	/**
	 * Gives the current time of the simulation.
	 * If the simulation isn't over, it's the start time.
	 * @return Time of the simulation
	 */
	public SimulationTimeStamp getCurrentTime() {
		return currentTime;
	}
	
	/**
	 * Set the current time of the simulation
	 * @param ct The SimulationTimeStamp current
	 */
	public void setCurrentTime(SimulationTimeStamp ct) {
		this.currentTime = ct;
	}

	/**
	 * Runs the simulation.
	 * @return nothing
	 */
	public Void runSimulation () {
		ExplorationProbe ep = (ExplorationProbe) this.engine.getProbe("Exploration probe");
		ep.resetFinished();
		this.engine.runNewSimulation(this);
		this.currentTime = ep.getData().getTime();
		return null;
	}
	
	/**
	 * Add a probe to the engine of the simulation.
	 * @param identifier id of the probe
	 * @param probe the probe
	 */
	public final void addProbe (String identifier, IProbe probe) {
		this.engine.addProbe(identifier, probe);
	}
	
	/**
	 * Gives the data of the simulation.
	 * Can be wrong if the function is called too early.
	 * @return the data of the simulation
	 */
	public SimulationData getData () {
		return this.data;
	}
	
	/**
	 * Makes the copy of the simulation
	 * @param sd the simulation data
	 * @return the new simulation
	 */
	public abstract AbstractExplorationSimulationModel makeCopy (SimulationData sd);

}
