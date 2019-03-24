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
package fr.univ_artois.lgi2a.similar2logo.com.mecsyco.predation;

import fr.univ_artois.lgi2a.similar2logo.com.mecsyco.predation.model.LinearGrowthEquation;
import fr.univ_artois.lgi2a.similar2logo.com.mecsyco.predation.model.MecsycoPredationSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.com.mecsyco.predation.tools.PredationModelArtifact;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.exception.CausalityException;
import mecsycoscholar.application.ode.model.EquationModelArtifact;
 
/**
 * This class represents a launcher for the predation model. 
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class PredationMain {

	
	public static void main(String args[]){
		
		MecsycoPredationSimulationParameters parameters = new MecsycoPredationSimulationParameters();
		
			// create the model agent for the X model
			EventMAgent xAgent = new EventMAgent(
				"agent_x",
				parameters.finalTime.getIdentifier()
			);
			
			// create the model artifact and link it with the agent
			EquationModelArtifact xModelArtifact = new EquationModelArtifact(
				"LinearX", new LinearGrowthEquation(
				   "X",
				   parameters.initialPreyPopulation,
				   parameters.preyReproductionRate,
				   1
				)
			);
			xAgent.setModelArtifact(xModelArtifact);
			
			// create the model agent for the Y model
			EventMAgent yAgent = new EventMAgent(
				"agent_y",
				parameters.finalTime.getIdentifier()
			);
			
			// create the model artifact and link it with the agent
			EquationModelArtifact yModelArtifact = new EquationModelArtifact(
			   "LinearY", new LinearGrowthEquation(
				  "Y",
			      parameters.initialPredatorPopulation,
			      parameters.predatorReproductionRate,
			      1
			   )
			);
			yAgent.setModelArtifact(yModelArtifact);
			
			
			// create the model agent for the predation model
			EventMAgent predationAgent = new EventMAgent(
				"agent_predation",
				parameters.finalTime.getIdentifier()
			);
			
			predationAgent.setModelArtifact(
				new PredationModelArtifact(
			       new MecsycoPredationSimulationModel(
			          new MecsycoPredationSimulationParameters()
			       )
				)
			);
						
			
			// create the coupling artifacts
			CentralizedEventCouplingArtifact xOutputToPredation =new CentralizedEventCouplingArtifact();
			CentralizedEventCouplingArtifact yOutputToPredation =new CentralizedEventCouplingArtifact();
			CentralizedEventCouplingArtifact predationOutputToX =new CentralizedEventCouplingArtifact();
			CentralizedEventCouplingArtifact predationOutputToY =new CentralizedEventCouplingArtifact();

			
			// set the links
			xAgent.addOutputCouplingArtifact(xOutputToPredation,"X");
			xAgent.addInputCouplingArtifact(predationOutputToX,"X");
			
			yAgent.addOutputCouplingArtifact(yOutputToPredation,"Y");
			yAgent.addInputCouplingArtifact(predationOutputToY,"Y");
			
			predationAgent.addOutputCouplingArtifact(predationOutputToX, "X");
			predationAgent.addInputCouplingArtifact(xOutputToPredation, "X");
			predationAgent.addOutputCouplingArtifact(predationOutputToY, "Y");
			predationAgent.addInputCouplingArtifact(yOutputToPredation, "Y");
			
			// initialize the model
			xAgent.startModelSoftware();
			yAgent.startModelSoftware();
			predationAgent.startModelSoftware();
			
			// set model parameters
			String argX[]={ String.valueOf(parameters.timeDiscretizationX) };
			String argY[]={ String.valueOf(parameters.timeDiscretizationY) };
			xAgent.setModelParameters(argX);
			yAgent.setModelParameters(argY);

			try {
				predationAgent.coInitialize();
				xAgent.coInitialize();
				yAgent.coInitialize();
				predationAgent.start();
				xAgent.start();
				yAgent.start();
			} catch (CausalityException e) {
				e.printStackTrace();
			}
	}
}