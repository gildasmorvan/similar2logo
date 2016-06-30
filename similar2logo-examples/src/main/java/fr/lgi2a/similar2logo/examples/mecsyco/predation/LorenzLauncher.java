package fr.lgi2a.similar2logo.examples.mecsyco.predation;

import fr.lgi2a.similar2logo.examples.predation.initializations.PredationSimulationModel;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.exception.CausalityException;
import mecsycoscholar.application.ode.model.EquationModelArtifact;
import mecsycoscholar.application.ode.model.LorenzX;
 
public class LorenzLauncher {
	// set the global simulation time
	public final static double maxSimulationTime=80;
	
	// set the step size of the X model (between {0.01,0.001,0.0001})
	public final static double time_discretization_X= 1;
	
	// set the step size of the Y model (between {0.01,0.001,0.0001})
	public final static double time_discretization_Y= 1;
	

	
	public static void main(String args[]){
		
			// create the model agent for the X model
			EventMAgent XAgent = new EventMAgent("agent_x", maxSimulationTime);
			
			// create the model artifact and link it with the agent
			EquationModelArtifact XModelArtifact = new EquationModelArtifact(
				"lorenzX", new LorenzX (0.1, 0.1, 0)
			);
			XAgent.setModelArtifact(XModelArtifact);
			
			// create the model agent for the Y model
			EventMAgent YAgent = new EventMAgent("agent_y", maxSimulationTime);
			// create the model artifact and link it with the agent
			EquationModelArtifact YModelArtifact = new EquationModelArtifact(
				"lorenzY", new LorenzX (0.1, 0.1, 0)
			);
			YAgent.setModelArtifact(YModelArtifact);
			
			
			// create the model agent for the predation model
			EventMAgent predationAgent = new EventMAgent("agent_predation", 1, maxSimulationTime);
			predationAgent.setModelArtifact(
				new PredationModelArtifact(
			       new PredationSimulationModel(
			          new PredationSimulationParameters()
			       )
				)
			);
						
			
			// create the coupling artifacts
			CentralizedEventCouplingArtifact XOutputToPredation =new CentralizedEventCouplingArtifact();
			CentralizedEventCouplingArtifact YOutputToPredation =new CentralizedEventCouplingArtifact();
			CentralizedEventCouplingArtifact PredationOutputToX =new CentralizedEventCouplingArtifact();
			CentralizedEventCouplingArtifact PredationOutputToY =new CentralizedEventCouplingArtifact();

			
			// set the links
			XAgent.addOutputCouplingArtifact(XOutputToPredation,"X");
			XAgent.addInputCouplingArtifact(PredationOutputToX,"X");
			
			YAgent.addOutputCouplingArtifact(YOutputToPredation,"Y");
			YAgent.addInputCouplingArtifact(PredationOutputToY,"Y");
			
			predationAgent.addOutputCouplingArtifact(PredationOutputToX, "X");
			predationAgent.addInputCouplingArtifact(XOutputToPredation, "X");
			predationAgent.addOutputCouplingArtifact(PredationOutputToY, "Y");
			predationAgent.addInputCouplingArtifact(YOutputToPredation, "Y");
			
			// initialize the model
			XAgent.startModelSoftware();
			YAgent.startModelSoftware();
			predationAgent.startModelSoftware();
			
			// set model parameters
			String argX[]={ String.valueOf(time_discretization_X) };
			String argY[]={ String.valueOf(time_discretization_Y) };
			XAgent.setModelParameters(argX);
			YAgent.setModelParameters(argY);


			// send the initial value of each models
			try {
				XAgent.coInitialize();
				YAgent.coInitialize();
				predationAgent.coInitialize();
	
				XAgent.start();
				YAgent.start();
				predationAgent.start();
			
			// this should never happen
			} catch (CausalityException e) {
				e.printStackTrace();
			}
	}
}
