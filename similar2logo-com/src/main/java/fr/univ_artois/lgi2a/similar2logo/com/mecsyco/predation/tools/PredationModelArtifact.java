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
package fr.univ_artois.lgi2a.similar2logo.com.mecsyco.predation.tools;

import fr.univ_artois.lgi2a.similar2logo.com.mecsyco.AbstractSimilar2LogoModelArtifact;
import fr.univ_artois.lgi2a.similar2logo.com.mecsyco.predation.model.MecsycoPredationSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.predation.probes.PreyPredatorPopulationProbe;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple2;

/**
 * This class represents a Mecsyco model artifact for the predation simulation. 
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class PredationModelArtifact extends AbstractSimilar2LogoModelArtifact<MecsycoPreyPredatorPopulationProbe> {

	/**
	 * Builds a new instance of this model artifact.
	 * 
	 * @param simulationModel The simulation model.
	 */
	public PredationModelArtifact(AbstractLogoSimulationModel simulationModel) {
		super(simulationModel, new MecsycoPreyPredatorPopulationProbe(
		   (MecsycoPredationSimulationParameters) simulationModel.getSimulationParameters())
		);
		engine.addProbe("Population printing", new PreyPredatorPopulationProbe());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void processExternalInputEvent(SimulEvent aEvent, String aPort) {
		if(this.engine.getLevels().containsKey(LogoSimulationLevelList.LOGO)) {
			Tuple2 eventData = (Tuple2) aEvent.getData();
			int nbOfBirths = (int) Double.parseDouble(eventData.getItem1().toString());
			if(aPort.equals("X")) {
				mecsycoProbe.setDX(nbOfBirths);
			} else if(aPort.equals("Y")) {
				mecsycoProbe.setDY(nbOfBirths);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulEvent getExternalOutputEvent(String port) {
    	if(port.equalsIgnoreCase("X")){
    		return new SimulEvent(
    				new Tuple2<>(this.mecsycoProbe.getX(), port),
    				this.getNextInternalEventTime()
    			);
    	} else if(port.equalsIgnoreCase("Y")) {
			return new SimulEvent(
				new Tuple2<>(this.mecsycoProbe.getY(), port),
				this.getNextInternalEventTime()
			);
    	}
    	else {
    		return new SimulEvent(
    		   null,
    		   this.getNextInternalEventTime()
    		);
    	}
	}

}
