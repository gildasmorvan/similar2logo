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
package fr.univ_artois.lgi2a.similar2logo.lib.probes;

import java.util.HashSet;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractProbe;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.tools.SimulationData;

/**
 * Probe uses for the exploration simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class ExplorationProbe extends AbstractProbe {
	
	public static final String DEFAULT_NAME = "Exploration probe";
	
	/**
	 * Data of the simulation recover at the end of the simulation.
	 */
	private SimulationData data;
	
	/**
	 * <code>true</code> if the simulation is finished.
	 */
	private boolean finished;
	
	public ExplorationProbe (SimulationData sim) {
		this.data = sim;
		this.finished = false;
	}


	/**
	 * Gives the data simulation when the simulation is over.
	 * @return the data of the simulation
	 */
	public SimulationData getData() {
		return data;
	}

	/**
	 * Reset finished attribute to <code>false</code>.
	 */
	public void resetFinished() {
		this.finished = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(SimulationTimeStamp finalTimestamp, ISimulationEngine simulationEngine) {
		LogoEnvPLS oldEnvironment = (LogoEnvPLS) simulationEngine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO)
				.getPublicLocalStateOfEnvironment();
		data.setEnvironment(oldEnvironment);
		Set<ILocalStateOfAgent> oldAgents = simulationEngine.getSimulationDynamicStates().get(LogoSimulationLevelList.LOGO)
				.getPublicLocalStateOfAgents();
		Set<TurtlePLSInLogo> newAgents = new HashSet<TurtlePLSInLogo>();
		for (ILocalStateOfAgent a : oldAgents) {
			TurtlePLSInLogo ta = (TurtlePLSInLogo) a;
			newAgents.add(ta);
		}
		data.setAgents(newAgents);
		data.setTime(finalTimestamp);
		this.finished = true;
	}

	/**
	 * @return <code>true</code> if the simulation is finished.
	 */
	public boolean isFinished() {
		return finished;
	}

}
