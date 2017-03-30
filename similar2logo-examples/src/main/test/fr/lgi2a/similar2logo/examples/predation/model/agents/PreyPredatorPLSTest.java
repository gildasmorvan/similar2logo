package fr.lgi2a.similar2logo.examples.predation.model.agents;

import org.junit.Test;

import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyDecisionModel;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorFactory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;
import junit.framework.TestCase;

public class PreyPredatorPLSTest extends TestCase{
	
	private PreyPredatorPLS ppp1;
	
	public void setUp() {
		PredationSimulationParameters castedParameters = new PredationSimulationParameters();
		IAgent4Engine turtle = PreyPredatorFactory.generate(
				new TurtlePerceptionModel(castedParameters.preyPerceptionDistance, 2*Math.PI, true,false, false),
				new PreyDecisionModel(),
				PreyCategory.CATEGORY,
				LogoEnvPLS.NORTH,
				1,
				0,
				RandomValueFactory.getStrategy().randomDouble() * castedParameters.gridWidth,
				RandomValueFactory.getStrategy().randomDouble() * castedParameters.gridHeight,
				castedParameters.preyInitialEnergy,
				0
		);
		ppp1 = new PreyPredatorPLS(turtle, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	public void testClone() {
		PreyPredatorPLS ppp2 = (PreyPredatorPLS) ppp1.clone();
		assertEquals(ppp1, ppp2);
		assertNotSame(ppp1,ppp2);
		assertEquals(ppp1.getClass(),ppp2.getClass());
	}

}
