package fr.lgi2a.similar2logo.kernel.model.environment;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import junit.framework.TestCase;

public class LogoEnvPLSTest extends TestCase {
	
	private LogoEnvPLS e1;

	@Before
	public void setUp() throws Exception {
		Set<Pheromone> p = new HashSet<>();
		p.add(new Pheromone("Fleur", 1, 0.01));
		p.add(new Pheromone("Excrement", 50, 0.01));
		e1 = new LogoEnvPLS(50, 45, true, false,p );
	}

	@Test
	public void testClone() {
		LogoEnvPLS e2 = (LogoEnvPLS) e1.clone();
		assertEquals(e1, e2);
		assertNotSame(e1,e2);
		assertEquals(e1.getClass(),e2.getClass());
	}

}
