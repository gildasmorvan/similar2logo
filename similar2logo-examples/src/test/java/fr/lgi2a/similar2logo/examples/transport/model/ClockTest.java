package fr.lgi2a.similar2logo.examples.transport.model;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.transport.time.Clock;
import junit.framework.TestCase;

public class ClockTest extends TestCase {

	private Clock c;
	
	public void setUp () {
		c = new Clock (0,0,4);
	}
	
	public void testGetDay () {
		System.out.println("Day : "+c.getDay(new SimulationTimeStamp(128900)));
	}
	
	public void testGetHour () {
		System.out.println("Hour : "+c.getHour(new SimulationTimeStamp(128900)));
	}
	
	public void testGetTime () {
		System.out.println(c.getTime(new SimulationTimeStamp(128900)));
	}
}
