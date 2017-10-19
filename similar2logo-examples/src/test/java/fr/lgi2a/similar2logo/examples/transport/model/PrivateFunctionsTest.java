package fr.lgi2a.similar2logo.examples.transport.model;

import java.awt.geom.Point2D;

import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import junit.framework.TestCase;

public class PrivateFunctionsTest extends TestCase {
	
	private Point2D nextPosition (Point2D position, double direction) {
		int x,y;
		if (direction < 0) x = 1;
		else if ((direction == LogoEnvPLS.NORTH) || (direction == LogoEnvPLS.SOUTH)) x = 0;
		else x = -1;
		if ((direction >= LogoEnvPLS.NORTH_EAST) && (direction <= LogoEnvPLS.NORTH_WEST)) y = 1;
		else if ((direction == LogoEnvPLS.WEST) || (direction == LogoEnvPLS.EAST)) y = 0;
		else y = -1;
		Point2D res = new Point2D.Double(position.getX()+x,position.getY()+y);
		return res;
	}
	
	public void testNextPosition () {
		Point2D pos = new Point2D.Double(0,0);
		Point2D pos2 = nextPosition(pos, LogoEnvPLS.NORTH_WEST);
		System.out.println(pos2);
	}

}
