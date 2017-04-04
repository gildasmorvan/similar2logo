package fr.lgi2a.similar2logo.kernel.model.environment;
import java.awt.geom.Point2D;

import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import junit.framework.TestCase;

public class MarkTest extends TestCase{
	
	private Mark<Double> m1;
	
	public void setUp() {
		m1 = new Mark<Double>(new Point2D.Double(0,3),(double) 7);
	}

	@SuppressWarnings("unchecked")
	public void testClone() {
		Mark<Double> m2 = (Mark<Double>) m1.clone();
		assertEquals(m1.getCategory(), m2.getCategory());
		assertEquals(m1.getLocation(),m2.getLocation());
		assertEquals(m1.getContent(),m2.getContent());
		//Verification du clone
		assertNotSame(m1,m2);
		assertEquals(m1.getClass(),m2.getClass());
		System.out.println("fin");
	}

}
