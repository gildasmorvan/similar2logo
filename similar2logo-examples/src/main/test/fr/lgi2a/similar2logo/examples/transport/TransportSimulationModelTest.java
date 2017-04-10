package fr.lgi2a.similar2logo.examples.transport;

import org.junit.Test;

import fr.lgi2a.similar2logo.examples.transport.model.TransportSimulationParameters;
import junit.framework.TestCase;

public class TransportSimulationModelTest extends TestCase {
	
	private TransportSimulationModel tsm;
	private TransportSimulationParameters tsp;
	
	public void setUp() {
		this.tsp = new TransportSimulationParameters();
		this.tsm = new TransportSimulationModel(tsp, "./osm/map_maison.osm");
	}

	@Test
	public void testReadMap() {
		tsm.readMap(tsp, null);
	}

}
