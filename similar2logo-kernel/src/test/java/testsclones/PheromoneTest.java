package testsclones;
import org.junit.Test;

import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import junit.framework.TestCase;

public class PheromoneTest extends TestCase {
	
	private Pheromone p1;

	public void setUp() {
		p1 = new Pheromone("Odeur", 0, 0);
	}

	@Test
	public void testClone() {
		Pheromone p2 = (Pheromone) p1.clone();
		assertEquals(p1, p2);
		assertNotSame(p1,p2);
		assertEquals(p1.getClass(),p2.getClass());
	}

}
