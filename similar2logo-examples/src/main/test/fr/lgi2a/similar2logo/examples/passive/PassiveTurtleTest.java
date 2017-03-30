package fr.lgi2a.similar2logo.examples.passive;

import org.junit.Test;

import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.lib.model.PassiveTurtleDecisionModel;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;
import junit.framework.TestCase;

public class PassiveTurtleTest extends TestCase {
	
	TurtlePLSInLogo turtle;
	
	public void setUp() {
		PassiveTurtleSimulationParameters param = new PassiveTurtleSimulationParameters();
		IAgent4Engine t = TurtleFactory.generate(
				new TurtlePerceptionModel(0, Double.MIN_VALUE, false, false, false),
				new PassiveTurtleDecisionModel(),
				new AgentCategory("passive", TurtleAgentCategory.CATEGORY),
				param.initialDirection,
				param.initialSpeed,
				param.initialAcceleration,
				param.initialX,
				param.initialY
			);
		turtle = new TurtlePLSInLogo(t, 0, 0, 0, 0, 0);
	}

	@Test
	public void test() {
		TurtlePLSInLogo turtle2 = (TurtlePLSInLogo) turtle.clone();
		assertEquals(turtle, turtle2);
		assertNotSame(turtle, turtle2);
		assertEquals(turtle.getClass(), turtle2.getClass());
	}

}
