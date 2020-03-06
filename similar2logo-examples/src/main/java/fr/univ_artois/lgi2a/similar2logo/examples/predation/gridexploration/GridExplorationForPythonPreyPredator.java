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
package fr.univ_artois.lgi2a.similar2logo.examples.predation.gridexploration;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.gridexploration.data.SimulationGridDataPreyPredator;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.AbstractExplorationForPython;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.AbstractExplorationSimulationModel;

/**
 * Class for the prey predator exploration in python
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class GridExplorationForPythonPreyPredator extends AbstractExplorationForPython {

	public GridExplorationForPythonPreyPredator(PredationSimulationParameters lsp) {
		super(lsp);
	}
	
	/**
	 * Makes n copies of the simulation
	 * @param esm the simulation to copy
	 * @param n the number to copy to produce
	 * @return a list with all the copies
	 */
	public List<AbstractExplorationSimulationModel> makeCopies (PredationGridExplorationSimulationModel esm, int n) {
		SimulationGridDataPreyPredator sdpp = (SimulationGridDataPreyPredator) esm.getData();
		sdpp.setWeight(sdpp.getWeight()/n);
		return super.makeCopies(esm, n);
	}
	
	@Override
	protected AbstractExplorationSimulationModel copySimulation(AbstractExplorationSimulationModel esm) {
		SimulationGridDataPreyPredator sdpp = (SimulationGridDataPreyPredator) esm.getData();
		return new PredationGridExplorationSimulationModel( 
			(PredationSimulationParameters) parameters,
			new SimulationTimeStamp(esm.getCurrentTime()), 
			(SimulationGridDataPreyPredator) sdpp.clone()
		);
	}

	@Override
	public List<AbstractExplorationSimulationModel> generateSimulation(int n) {
		List<AbstractExplorationSimulationModel> res = new ArrayList<>();
		
		for (int i =0; i < n; i++) {
			SimulationGridDataPreyPredator sdpp = new SimulationGridDataPreyPredator(new SimulationTimeStamp(0), i);
			sdpp.setWeight((float) 1./n);
			res.add(
				new PredationGridExplorationSimulationModel(
					(PredationSimulationParameters) parameters,
					new SimulationTimeStamp(0),
					sdpp
				)
			);
		}
		return res;
	}

}
