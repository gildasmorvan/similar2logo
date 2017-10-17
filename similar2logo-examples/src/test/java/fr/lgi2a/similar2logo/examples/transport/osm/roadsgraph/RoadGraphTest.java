package fr.lgi2a.similar2logo.examples.transport.osm.roadsgraph;

import java.awt.geom.Point2D;
import java.util.List;

import junit.framework.TestCase;

public class RoadGraphTest extends TestCase {
	
	private RoadGraph rg;
	
	public void setUp() {
		rg = new RoadGraph();
		RoadNode rn1 = new RoadNode (new Point2D.Double(0,0), true);
		RoadNode rn2 = new RoadNode (new Point2D.Double(1,0), true);
		RoadNode rn3 = new RoadNode (new Point2D.Double(1,1), true);
		RoadNode rn4 = new RoadNode (new Point2D.Double(1,2), true);
		RoadNode rn5 = new RoadNode (new Point2D.Double(2,1), true);
		RoadNode rn6 = new RoadNode (new Point2D.Double(2,2), true);
		RoadNode rn7 = new RoadNode (new Point2D.Double(0, 5), true);
		RoadNode rn8 = new RoadNode (new Point2D.Double(1, 6), true);
		rg.addRoadEdge(new RoadEdge(rn1, rn2,"Street"));
		rg.addRoadEdge(new RoadEdge(rn2,rn3,"Street"));
		rg.addRoadEdge(new RoadEdge(rn3, rn4,"Street"));
		rg.addRoadEdge(new RoadEdge (rn3,rn5,"Street"));
		rg.addRoadEdge(new RoadEdge(rn3,rn6,"Street"));
		rg.addRoadEdge(new RoadEdge(rn5, rn6,"Street"));
		rg.addRoadEdge(new RoadEdge (rn1, rn7,"Street"));
		rg.addRoadEdge(new RoadEdge(rn4, rn7, "Tramway"));
		rg.addRoadEdge(new RoadEdge(rn7, rn8, "Street"));
	}
	
	public void testWayToGo () {
		List<Point2D> res = rg.wayToGo(new Point2D.Double(0.9, 6), new Point2D.Double(0.1, 0));
		for (Point2D p : res) {
			System.out.println(p.toString());
		}
	}

}
