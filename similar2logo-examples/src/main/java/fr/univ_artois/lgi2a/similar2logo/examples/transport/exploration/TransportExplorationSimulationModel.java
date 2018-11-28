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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.exploration;

import java.io.InputStream;

import fr.univ_artois.lgi2a.similar2logo.examples.transport.exploration.data.SimulationDataTransport;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.exploration.probes.TransportSimilationForExplorationProbe;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.initialization.TransportSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.AbstractExplorationSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.tools.SimulationData;

/**
 * Class for the exploration of the transport simulation.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class TransportExplorationSimulationModel extends AbstractExplorationSimulationModel {
	
	/**
	 * The place where are the map data
	 */
	private InputStream dataPath;
	
	/**
	 * The number of horizontal divisions.
	 */
	private int n;
	
	/**
	 * The number of vertical divisions.
	 */
	private int m;
	
	/**
	 * The parameters planning
	 */
	private TransportParametersPlanning planning;
	
	/**
	 * The number of steps by second
	 */
	private int step;

	public TransportExplorationSimulationModel(TransportSimulationParameters parameters, SimulationTimeStamp initTime, 
			SimulationData sm, InputStream resource, int n, int m, int step, TransportParametersPlanning tpp) {
		super(parameters, initTime, new TransportSimulationModel(parameters, resource, tpp), sm);
		this.dataPath = resource;
		this.planning = tpp;
		this.step = step;
		this.addProbe("traffic probe", new TransportSimilationForExplorationProbe((SimulationDataTransport) data, n, m, step));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExplorationSimulationModel makeCopy(SimulationData sd) {
		SimulationDataTransport sdt = (SimulationDataTransport) sd;
		return new TransportExplorationSimulationModel(
			new TransportSimulationParameters(), currentTime, 
			(SimulationDataTransport) sdt.clone(), dataPath, n, m, step, planning
		);
	}

}
