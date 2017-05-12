package fr.lgi2a.similar2logo.examples.predation.exploration.data;

import org.junit.Test;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.predation.exploration.PredationExplorationSimulationModel;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import junit.framework.TestCase;

public class SimulationDataPreyPredatorTest extends TestCase {
	
	SimulationDataPreyPredator sdpp;
	PredationExplorationSimulationModel pesm;
	
	public void setUp () {
		sdpp = new SimulationDataPreyPredator(new SimulationTimeStamp(0));
		pesm = new PredationExplorationSimulationModel(new PredationSimulationParameters(), 
				new SimulationTimeStamp(0), sdpp);
		pesm.runSimulation();
	}

	@Test
	public void test() {
		PredationExplorationSimulationModel clone = (PredationExplorationSimulationModel) pesm.makeCopy(sdpp);
		assertNotSame(clone, pesm);
		assertEquals(pesm.getClass(), clone.getClass());
		assertNotSame(pesm.getData().getAgents(),clone.getData().getAgents());
		LogoEnvPLS ev1 = pesm.getData().getEnvironment();
		LogoEnvPLS ev2 = clone.getData().getEnvironment();
		System.out.println(ev1.equals(ev2));
		assertNotSame(pesm.getData().getEnvironment(), clone.getData().getEnvironment());
	}

}
