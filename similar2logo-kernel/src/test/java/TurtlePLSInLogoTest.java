import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;

public class TurtlePLSInLogoTest {
	
	@Before
	public void setUp() throws CloneNotSupportedException {
		TurtlePLSInLogo turtle = new TurtlePLSInLogo( null, 0, 0, 0, 0, 0);
		TurtlePLSInLogo clone = (TurtlePLSInLogo) turtle.clone();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	public void assertSame () {
		
	}

}
