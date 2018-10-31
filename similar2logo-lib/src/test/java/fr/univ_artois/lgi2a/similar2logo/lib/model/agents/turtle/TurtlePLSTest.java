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
package fr.univ_artois.lgi2a.similar2logo.lib.model.agents.turtle;

import org.junit.Before;
import org.junit.Test;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel;
import fr.univ_artois.lgi2a.similar2logo.lib.model.PassiveTurtleDecisionModel;
import junit.framework.TestCase;

public class TurtlePLSTest extends TestCase {
	
	private TurtlePLSInLogo pls;
	
	private static final double EPSILON = 0.000000001;

	@Before
	public void setUp() throws Exception {
		IAgent4Engine ia4e = TurtleFactory.generate(
				new ConeBasedPerceptionModel(2, 2*Math.PI, true, false, false),
				new PassiveTurtleDecisionModel(),
				TurtleAgentCategory.CATEGORY,
				0 ,
				0 ,
				0,
				0,
				0
			);
		pls = new TurtlePLSInLogo(
			ia4e,
			0 ,
			0 ,
			0,
			0,
			0
		);
	}

	@Test
	public void testClone() {
		TurtlePLSInLogo pls2 = (TurtlePLSInLogo) pls.clone();
		assertEquals(pls.getLocation(), pls2.getLocation());
		assertEquals(pls.getDirection(), pls2.getDirection());
		assertEquals(pls.getSpeed(), pls2.getSpeed());
		assertEquals(pls.getAcceleration(), pls2.getAcceleration());
		assertNotSame(pls.getOwner(), pls2.getOwner());
		assertNotSame(pls,pls2);
		assertEquals(pls.getClass(),pls2.getClass());
	}
	
	@Test
	public void testDXY() {
		pls.setDirection(LogoEnvPLS.NORTH);
		assertEquals(pls.getDX(), 0, EPSILON);
		assertEquals(pls.getDY(), 0, EPSILON);
		pls.setSpeed(1);
		assertEquals(pls.getDX(), 0, EPSILON);
		assertEquals(pls.getDY(), 1, EPSILON);
		pls.setDirection(LogoEnvPLS.SOUTH);
		assertEquals(pls.getDX(), 0, EPSILON);
		assertEquals(pls.getDY(), -1, EPSILON);
		pls.setDirection(LogoEnvPLS.EAST);
		assertEquals(pls.getDX(), 1, EPSILON);
		assertEquals(pls.getDY(), 0, EPSILON);
		pls.setDirection(LogoEnvPLS.WEST);
		assertEquals(pls.getDX(), -1, EPSILON);
		assertEquals(pls.getDY(), 0, EPSILON);
	}

}
