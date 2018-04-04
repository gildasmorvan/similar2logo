/**
 * Copyright or © or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the 
 * implementation of Logo-like simulations using the SIMILAR API.
 * This software defines an API to implement such simulations, and also 
 * provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.lgi2a.similar2logo.kernel.model.environment;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import net.jafama.FastMath;

public class LogoEnvPLSTest extends TestCase {
	
	private LogoEnvPLS e1;
	
	private static final double EPSILON = 0.000000001;

	@Before
	public void setUp() throws Exception {
		Set<Pheromone> p = new HashSet<>();
		p.add(new Pheromone("p1", 1, 0.01));
		p.add(new Pheromone("p2", 50, 0.01));
		e1 = new LogoEnvPLS(50, 50, true, true,p );
	}

	@Test
	public void testClone() {
		LogoEnvPLS e2 = (LogoEnvPLS) e1.clone();
		assertEquals(e1, e2);
		assertNotSame(e1,e2);
		assertEquals(e1.getClass(),e2.getClass());
	}
	
	@Test
	public void testNeighbors() {
		int index[] = {0, 1, 2, 3, 4, e1.getWidth()-1};
		for(int x = 0; x < index.length; x++) {
			for(int y = 0; y < index.length; y++) {
				Collection<Position> neighbors = e1.getNeighbors(index[x], index[y], 1);
				assertEquals(neighbors.size(), 9);
			}
		}
	}
	
	@Test
	public void testDistance() {
		assertEquals(
			e1.getDistance(new Point2D.Double(0, 0), new Point2D.Double(1, 0)), 1, EPSILON
		);
		assertEquals(
			e1.getDistance(new Point2D.Double(0, 0), new Point2D.Double(0, 1)), 1, EPSILON
		);
		assertEquals(
			e1.getDistance(new Point2D.Double(0, 0), new Point2D.Double(1, 1)),
			FastMath.sqrt(2),
			EPSILON
		);
		assertEquals(
			e1.getDistance(new Point2D.Double(0, 0), new Point2D.Double(e1.getWidth()-1, 0)), 1, EPSILON
		);
		assertEquals(
			e1.getDistance(new Point2D.Double(0, 0), new Point2D.Double(0, e1.getHeight()-1)), 1, EPSILON
		);
		assertEquals(
			e1.getDistance(new Point2D.Double(0, 0), new Point2D.Double(e1.getWidth()-1, e1.getHeight()-1)),
			FastMath.sqrt(2),
			EPSILON
		);
		
	}
	
	@Test
	public void testDirection() {
		assertEquals(
			e1.getDirection(new Point2D.Double(0, 0), new Point2D.Double(1, 0)),
			LogoEnvPLS.EAST,
			EPSILON
		);
		assertEquals(
			e1.getDirection(new Point2D.Double(0, 0), new Point2D.Double(0, 1)),
			LogoEnvPLS.NORTH,
			EPSILON
		);
		assertEquals(
			e1.getDirection(new Point2D.Double(0, 0), new Point2D.Double(1, 1)),
			LogoEnvPLS.NORTH_EAST,
			EPSILON
		);
		assertEquals(
			e1.getDirection(new Point2D.Double(0, 0), new Point2D.Double(e1.getWidth()-1, 0)),
			LogoEnvPLS.WEST,
			EPSILON
		);
		assertEquals(
			e1.getDirection(new Point2D.Double(0, 0), new Point2D.Double(0, e1.getHeight()-1)),
			LogoEnvPLS.SOUTH,
			EPSILON
		);
		assertEquals(
			e1.getDirection(new Point2D.Double(0, 0), new Point2D.Double(e1.getWidth()-1, e1.getHeight()-1)),
			LogoEnvPLS.SOUTH_WEST,
			EPSILON
		);
		
	}

}
