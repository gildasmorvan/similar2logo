package fr.lgi2a.similar2logo.lib.exploration.tools;
import org.junit.Test;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import junit.framework.TestCase;

public class SimulationDataTest extends TestCase {
	
	private SimulationData sd;
	
	public void setUp() throws Exception {
		sd = new SimulationData(new SimulationTimeStamp(0));
		IAgent4Engine turtle = new ExtendedAgent(TurtleAgentCategory.CATEGORY);
		//sd.agents.add(new TurtlePLSInLogo(turtle , 0, 1, 1, 0, 0));
		sd.environment = new LogoEnvPLS(10, 10, false, false,null);
	}

	@Test
	public void testClone() {
		SimulationData sd2 = (SimulationData) sd.clone();
		assertNotSame(sd, sd2);
	}

}
