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
package fr.univ_artois.lgi2a.similar2logo.examples.transport.exploration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import fr.univ_artois.lgi2a.similar2logo.examples.transport.exploration.data.SimulationDataTransport;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.initialization.TransportSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.parameters.TransportSimulationParametersGenerator;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.AbstractMultipleExplorationSimulation;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.selection.ISelectionOperator;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.selection.NoSelection;

/**
 * The class for the multiple exploration for the transport simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class MultipleTransportExplorationSimulation extends AbstractMultipleExplorationSimulation {
	
	/**
	 * The data
	 */
	private InputStream data;
	
	/**
	 * The number of horizontal divisions
	 */
	private int n;
	
	/**
	 * The number of vertical divisions
	 */
	private int m;
	
	/**
	 * The planning of the parameters
	 */
	private TransportParametersPlanning planning;
	
	/**
	 * The number of steps by second
	 */
	private int step;

	/**
	 * Constructor of the multiple transport exploration simulation
	 * @param simulationParameters the array of the parameters
	 * @param end the moment when the simulation will finish
	 * @param checkpoints the pauses the simulation will do
	 * @param treatment the treatment to apply when the simulation is in pause
	 * @param data the path toward the data
	 * @param n the number of horizontal divisions
	 * @param m the number of vertical divisions
	 * @param hour the start hour of the simulation
	 * @param step the number of step by second
	 * @param dataParameter the JSON object with the parameters
	 */
	public MultipleTransportExplorationSimulation(
		LogoSimulationParameters simulationParameters,
		SimulationTimeStamp end, 
		List<SimulationTimeStamp> checkpoints,
		ISelectionOperator treatment,
		InputStream data,
		int n,
		int m,
		int hour,
		int step,
		JSONObject dataParameter
	) {
		super(simulationParameters, end, checkpoints, treatment);
		this.data = data;
		this.n = n;
		this.m = m;
		this.step = step;
		this.planning = new TransportParametersPlanning(hour, step, dataParameter, n, m);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addNewSimulation(LogoSimulationParameters lsp, int id) {
		this.simulations.add(new TransportExplorationSimulationModel((TransportSimulationParameters) lsp, this.currentTime, 
				new SimulationDataTransport(currentTime, m, n), data, n, m, step, planning));
	}
	
	public static void main (String[] args) {
		List<SimulationTimeStamp> p = new ArrayList<>();
		for (int i = 1 ; i <= 60; i++) {
			p.add(new SimulationTimeStamp(i*240));
		}
		TransportSimulationParameters tsp = new TransportSimulationParameters();
		JSONObject param = TransportSimulationParametersGenerator.parametersByZoneJSON(
			TransportSimulationParametersGenerator.staticParametersByDefaultJSON(), 
			TransportSimulationParametersGenerator.variableParametersByDefaultJSON(), 
			TransportSimulationModel.class.getResourceAsStream("parameters/factors.txt"),
			TransportSimulationModel.class.getResourceAsStream("parameters/zone.txt")
		);
		MultipleTransportExplorationSimulation mtes = new MultipleTransportExplorationSimulation(
			tsp,
			new SimulationTimeStamp(30_001), 
			p,
			new NoSelection(),
			TransportSimulationModel.class.getResourceAsStream("osm/map_valenciennes_edited.osm"),
			5,
			5,
			0,
			40,
			param
		);
		mtes.initSimulation(3);
		mtes.runSimulations();
	}

}
