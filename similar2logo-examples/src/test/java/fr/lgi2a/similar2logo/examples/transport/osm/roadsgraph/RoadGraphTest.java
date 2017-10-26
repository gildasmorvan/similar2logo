package fr.lgi2a.similar2logo.examples.transport.osm.roadsgraph;

import java.awt.geom.Point2D;
import java.util.List;

import junit.framework.TestCase;

public class RoadGraphTest extends TestCase {
	
	private RoadGraph rg;
	
	public void setUp() {
		rg = new RoadGraph();
		RoadNode rn1 = new RoadNode (new Point2D.Double(0,0));
		RoadNode rn2 = new RoadNode (new Point2D.Double(1,0));
		RoadNode rn3 = new RoadNode (new Point2D.Double(1,1));
		RoadNode rn4 = new RoadNode (new Point2D.Double(1,2));
		RoadNode rn5 = new RoadNode (new Point2D.Double(2,1));
		RoadNode rn6 = new RoadNode (new Point2D.Double(2,2));
		RoadNode rn7 = new RoadNode (new Point2D.Double(0, 5));
		RoadNode rn8 = new RoadNode (new Point2D.Double(1, 6));
		rg.addRoadEdge(new RoadEdge(rn1, rn2,"Secondary"));
		rg.addRoadEdge(new RoadEdge(rn2,rn3,"Secondary"));
		rg.addRoadEdge(new RoadEdge(rn3, rn4,"Secondary"));
		rg.addRoadEdge(new RoadEdge (rn3,rn5,"Secondary"));
		rg.addRoadEdge(new RoadEdge(rn3,rn6,"Secondary"));
		rg.addRoadEdge(new RoadEdge(rn5, rn6,"Secondary"));
		rg.addRoadEdge(new RoadEdge (rn1, rn7,"Secondary"));
		rg.addRoadEdge(new RoadEdge(rn4, rn7, "Tramway"));
		rg.addRoadEdge(new RoadEdge(rn7, rn8, "Secondary"));
	}
	
	public void testWayToGo () {
		List<Point2D> res = rg.wayToGo(new Point2D.Double(0.1, 0), new Point2D.Double(0.9, 6));
		for (Point2D p : res) {
			System.out.println(p.toString());
		}
	}

}
